package org.springframework.context.annotation;

import org.springframework.HuayuApplicationContext;
import org.springframework.annotation.*;
import org.springframework.annotationAop.Aop;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.configAop.EnableAspectJAutoProxy;
import org.springframework.coreTransactional.EnableTransactionalManager;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.core.metrics.StartupStep;
import org.springframework.stereotype.Component;
import org.springframework.util.ParameterUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {

    private Class configClass;

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    private final AnnotatedBeanDefinitionReader reader;

    private final ClassPathBeanDefinitionScanner scanner;

    private ApplicationStartup applicationStartup = ApplicationStartup.DEFAULT;

    public AnnotationConfigApplicationContext(Class<?>... componentClasses) throws InvocationTargetException, IllegalAccessException {
        //容器启动时先调用无参的构造方法
        this();
        register(componentClasses);
        refresh();
    }

    public AnnotationConfigApplicationContext(Class configClass) throws InvocationTargetException, IllegalAccessException {
        this.reader = null;
        this.scanner = null;
        //容器启动时先调用无参的构造方法
        this.configClass = configClass;
        refresh();
    }

    public AnnotationConfigApplicationContext() {
        //这里将使用默认的startup，很奇怪，这里end没有任何作用，这是性能监控和分析的类，完全可以不写
        StartupStep createAnnotatedBeanDefReader = getApplicationStartup().start("spring.context.annotated-bean-reader.create");
        //容器会把自己传递到reader中去，reader的构造方法会使用工具类，用于在给定的 BeanDefinitionRegistry 中注册与注解配置相关的处理器，如各种PostProcesser
        //工具类会获取到reader的档案馆，并设置档案馆的自动注入处理器，和处理器的BeanFactory（容器自己）
        this.reader = new AnnotatedBeanDefinitionReader(this);
        createAnnotatedBeanDefReader.end();
        this.scanner = new ClassPathBeanDefinitionScanner(this);
    }

    @Override
    public void register(Class<?>... componentClasses) {
        this.reader.register(componentClasses);
    }

    @Override
    public void scan(String... basePackages) {

    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return new String[0];
    }

    @Override
    public int getBeanDefinitionCount() {
        return 0;
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return null;
    }

    @Override
    protected void refreshBeanFactory() throws IllegalStateException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        loadBeanDefinitions(beanFactory);
    }

    private void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        //扫描路径-BeanDefinition->放入beanDefinitionMap
        scan(configClass);
        scanAndCreateConfigBean(configClass);
        enableTransactional();
        enableAop();
        for (Map.Entry<String, BeanDefinition> entry : beanFactory.getBeanDefinitionMap().entrySet()) {
            String beanName = entry.getKey();
            // 判断这个bean是否已经在AutoWire的时候被创建了
            BeanDefinition beanDefinition = entry.getValue();
            if ("singleton".equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                //将单例bean放到单例池中
                beanFactory.putSingletonObjects(beanName, bean);
            }
        }
    }

    private void scanAndCreateConfigBean(Class configClass) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        for (Method method : configClass.getMethods()) {
            // 判断配置类有没有Bean注解
            if (method.isAnnotationPresent(Bean.class)) {
                createConfigBean(configClass, method);
            }
        }
    }

    private Object createConfigBean(Class configClass, Method method) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Bean annotation = method.getAnnotation(Bean.class);
        // 获得Bean对应的名字
        String beanName = annotation.value();
        // 判断容器中是否存在这个bean
        if (beanFactory.getSingletonObjects().containsKey(beanName)) {
            // 如果已经有了就直接返回
            return null;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        Parameter[] parameters = method.getParameters();
        Object instance = configClass.getDeclaredConstructor().newInstance();
        if (parameterTypes.length == 0) {
            Object invoke = method.invoke(instance, null);
            GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition(invoke.getClass());
            genericBeanDefinition.setScope("singleton");
            beanFactory.registerBeanDefinition(beanName, genericBeanDefinition);
            beanFactory.putSingletonObjects(beanName, invoke);
            return invoke;
        } else {
            Object[] args = new Object[parameterTypes.length];
            // 获取参数
            for (int i = 0; i < parameterTypes.length; i++) {
                // 判断是否为基础数据类型
                if (ParameterUtils.isPrimitiveOrWrapperOrString(parameterTypes[i])) {
                    System.out.println("暂时不支持对@Bean注解的方法注入基本数据类型及其包装类");
                    return null;
                }
                // 不是基础数据类型则自动Autowire判断有没有
                String parameterBeanName = parameters[i].getDeclaredAnnotation(BeanName.class).value();
                if (!beanFactory.getSingletonObjects().containsKey(parameterBeanName)) {
                    // 如果不存在这个Bean则遍历Config的全部方法，查看哪个方法上存在这个Bean注解上有对应的名字，并执行这个方法，然后注入到里面
                    // 这里其实不太好，遍历全部性能不是很好，但是要做到遍历剩余的方法比较复杂，不作为重点，源码也不可能这样写，这只是为了实现事务做的缓兵之策
                    for (Method configClassMethod : configClass.getMethods()) {
                        if (configClassMethod.getAnnotation(Bean.class).value().equals(parameterBeanName)) {
                            // 递归创建这个Bean
                            args[i] = createConfigBean(configClass, configClassMethod);
                            break;
                        }
                    }
                    // 如果这个方法被执行了其实可以不要继续执行了，所以在二次执行时,putBean定义和Bean将没有意义，所以为了安全考虑，方法开始前我们可以先判断是否存在这个Bean
                } else {
                    // 如果有则获取这个对象并注入
                    args[i] = beanFactory.getSingletonObjects().get(parameterBeanName);
                }
            }
            Object invoke = method.invoke(instance, args);
            GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition(invoke.getClass());
            genericBeanDefinition.setScope("singleton");
            beanFactory.registerBeanDefinition(beanName, genericBeanDefinition);
            beanFactory.putSingletonObjects(beanName, invoke);
            return invoke;

        }
    }

    private void enableTransactional() {

        if (!configClass.isAnnotationPresent(EnableTransactionalManager.class)) {
            return;
        }
        //解析配置类
        EnableTransactionalManager enableTransactionalManager = (EnableTransactionalManager) configClass.getDeclaredAnnotation(EnableTransactionalManager.class);
        Import importAnnotation = enableTransactionalManager.annotationType().getDeclaredAnnotation(Import.class);
        Class<?>[] value = importAnnotation.value();
        importBean(value);
    }

    @Override
    public ApplicationStartup getApplicationStartup() {
        return this.applicationStartup;
    }

    private void enableAop() {
        if (!configClass.isAnnotationPresent(EnableAspectJAutoProxy.class)) {
            return;
        }
        //解析配置类
        EnableAspectJAutoProxy enableAspectJAutoProxyAnnotation = (EnableAspectJAutoProxy) configClass.getDeclaredAnnotation(EnableAspectJAutoProxy.class);
        Import importAnnotation = enableAspectJAutoProxyAnnotation.annotationType().getDeclaredAnnotation(Import.class);
        Class<?>[] value = importAnnotation.value();
        importBean(value);

    }

    /**
     * 目前Import默认要有component有点怪
     *
     * @param value
     */
    private void importBean(Class<?>[] value) {
        for (Class<?> aClass : value) {
            ClassLoader classLoader = AnnotationConfigApplicationContext.class.getClassLoader();
            dependencyInjection(classLoader, aClass.getName());
        }
    }

    private void dependencyInjection(ClassLoader classLoader, String className) {
        try {
            //根据类路径加载类
            Class<?> clazz = classLoader.loadClass(className);
            if (clazz.isAnnotationPresent(Component.class)) {
                // 进一步处理
                //解析类--->BeanDefinition
                if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                    try {
                        BeanPostProcessor instance = createBeanPostProcessor(clazz);
                        beanPostProcessorList.add(instance);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                //判断当前bean是单例bean还是原型bean

                Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                String beanName = componentAnnotation.value();

                GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition(clazz);
                if (clazz.isAnnotationPresent(Scope.class)) {
                    Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                    genericBeanDefinition.setScope(scopeAnnotation.value());
                } else {
                    genericBeanDefinition.setScope("singleton");
                }
                beanFactory.registerBeanDefinition(beanName, genericBeanDefinition);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Object createBean(String beanName, BeanDefinition beanDefinition) {

        Class clazz = beanDefinition.getBeanClass();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();


            //Aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = (Autowired) clazz.getDeclaredAnnotation(Autowired.class);
                    //现在只能根据对象名称来进行依赖注入，不太好，需要想办法改为允许类型注入，在名称注入
                    Object bean = getBean(declaredField.getName());
                    if (bean == null && autowired.require() == true) {
                        throw new NullPointerException(clazz.getName() + "未被正确注入");
                    }
                    declaredField.set(instance, bean);
                }
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            //初始化
            if (instance instanceof InitializingBean) {
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            //BeanPostProcessor
            //TODO Aop
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }


    }

    private void scan(Class configClass) {
        if (!configClass.isAnnotationPresent(ComponentScan.class)) {
            throw new RuntimeException("缺少必要的注解");
        }
        //解析配置类
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScanAnnotation.value();
        scanPackage(path);
    }

    private void scanPackage(String path) {
        path = path.replace(".", "/");
        //扫描
        ClassLoader classLoader = HuayuApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        scanDirectory(file, classLoader);
    }

    private void scanDirectory(File file, ClassLoader classLoader) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }

            for (File f : files) {
                String fileName = f.getAbsolutePath();
                //如果文件是目录，那么就扫描该目录下的所有文件
                if (f.isDirectory()) {
                    scanDirectory(f, classLoader);
                } else if (fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    className = className.replace("\\", ".");
                    dependencyInjection(classLoader, className);

                }

            }
        }
    }

    @Override
    public Object getBean(String beanName) {
        ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = beanFactory.getBeanDefinitionMap();
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            //TODO 对beanPostProcessor要进行特殊处理
            if ("singleton".equals(beanDefinition.getScope())) {
                Object o = beanFactory.getSingletonObjects().get(beanName);
                if (o == null) {
                    // 但是这里打破了单例模式，有点不太好，可以这时候put一下
                    Object bean = createBean(beanName, beanDefinition);
                    beanFactory.putSingletonObjects(beanName, bean);
                    return bean;
                }
                return o;
            } else {
                //创建bean对象
                Object bean = createBean(beanName, beanDefinition);
                return bean;
            }
        } else {
            return null;
        }
    }

    public BeanPostProcessor createBeanPostProcessor(Class beanPostProcessorClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        BeanPostProcessor instance = (BeanPostProcessor) beanPostProcessorClass.getDeclaredConstructor().newInstance();
        for (Field declaredField : beanPostProcessorClass.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Autowired.class)) {
                Autowired autowired = (Autowired) beanPostProcessorClass.getDeclaredAnnotation(Autowired.class);
                //现在只能根据对象名称来进行依赖注入，不太好，需要想办法改为允许类型注入，在名称注入
                Object bean = getBean(declaredField.getName());
                if (bean == null && autowired.require() == true) {
                    throw new NullPointerException(beanPostProcessorClass.getName() + "未被正确注入");
                }
                declaredField.set(instance, bean);
            }
        }
        return instance;
    }
}
