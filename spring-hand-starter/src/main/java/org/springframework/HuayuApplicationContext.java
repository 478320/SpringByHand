package org.springframework;


import org.springframework.annotation.*;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.configAop.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是以前写的最初版的容器，已经被我整合到了AnnotationConfigApplicationContext中,非重点
 */
public class HuayuApplicationContext {

    private Class configClass;

    //key的值是我们自己定义的，目前还不可以没有
    private ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Class,Object> singletonClassObjects = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, HuayuBeanDefinition>  beanDefinitionMap= new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();
    public HuayuApplicationContext(Class configClass) {
        this.configClass = configClass;

        enableAop();
        //扫描路径-BeanDefinition->放入beanDefinitionMap
        scan(configClass);

        for (Map.Entry<String, HuayuBeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            HuayuBeanDefinition huayuBeanDefinition = entry.getValue();
            if (huayuBeanDefinition.getScope().equals("singleton")){
                Object bean = createBean(beanName, huayuBeanDefinition);
                //将单例bean放到单例池中
                singletonObjects.put(beanName,bean);
            }
        }
    }

    private void enableAop() {
        if (!configClass.isAnnotationPresent(EnableAspectJAutoProxy.class)){
            return;
        }
        //解析配置类
        EnableAspectJAutoProxy enableAspectJAutoProxyAnnotation = (EnableAspectJAutoProxy) configClass.getDeclaredAnnotation(EnableAspectJAutoProxy.class);
        Import importAnnotation = enableAspectJAutoProxyAnnotation.getClass().getDeclaredAnnotation(Import.class);
        Class<?>[] value = importAnnotation.value();
        importBean(value);

    }

    /**
     * 目前Import默认要有component有点怪
     * @param value
     */
    private void importBean(Class<?>[] value) {
        for (Class<?> aClass : value) {
            ClassLoader classLoader = HuayuApplicationContext.class.getClassLoader();
            dependencyInjection(classLoader, aClass.getName());
        }
    }

    private void dependencyInjection(ClassLoader classLoader,String className) {
        try {
            //根据类路径加载类
            Class<?> clazz = classLoader.loadClass(className);
            if (clazz.isAnnotationPresent(Component.class)){
                // 进一步处理
                //解析类--->BeanDefinition
                if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                    try {
                        BeanPostProcessor instance = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
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

                HuayuBeanDefinition huayuBeanDefinition =new HuayuBeanDefinition();
                huayuBeanDefinition.setClazz(clazz);
                if (clazz.isAnnotationPresent(Scope.class)){
                    Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                    huayuBeanDefinition.setScope(scopeAnnotation.value());
                } else {
                    huayuBeanDefinition.setScope("singleton");
                }
                beanDefinitionMap.put(beanName, huayuBeanDefinition);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Object createBean(String beanName, HuayuBeanDefinition huayuBeanDefinition){

        Class clazz = huayuBeanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)){
                    Autowired autowired = (Autowired) clazz.getDeclaredAnnotation(Autowired.class);
                    //现在只能根据对象名称来进行依赖注入，不太好，需要想办法改为允许类型注入，在名称注入
                    Object bean = getBean(declaredField.getName());
                    if (bean == null && autowired.require()==true){
                        throw new NullPointerException(clazz.getName()+"未被正确注入");
                    }
                    declaredField.set(instance,bean);
                }
            }
            //Aware回调
            if (instance instanceof BeanNameAware){
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList){
                instance = beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            }

            //初始化
            if (instance instanceof InitializingBean){
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            //BeanPostProcessor
            //TODO Aop
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList){
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
        if (!configClass.isAnnotationPresent(ComponentScan.class)){
            throw new RuntimeException("缺少必要的注解");
        }
        //解析配置类
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScanAnnotation.value();
        scanPackage(path);


    }

    private void scanPackage(String path) {
        path = path.replace(".","/");
        //扫描
        ClassLoader classLoader = HuayuApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        scanDirectory(file,classLoader);
    }

    private void scanDirectory(File file,ClassLoader classLoader) {
        if (file.isDirectory()){
            File[] files = file.listFiles();
            if (files==null){
                return;
            }

            for (File f : files) {
                String fileName = f.getAbsolutePath();
                //如果文件是目录，那么就扫描该目录下的所有文件
                if (f.isDirectory()) {
                    scanDirectory(f,classLoader);
                } else if (fileName.endsWith(".class")){
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    className = className.replace("\\",".");
                    dependencyInjection(classLoader,className);

                }

            }
        }
    }

    public Object getBean(String beanName){
        if (beanDefinitionMap.containsKey(beanName)){
            HuayuBeanDefinition huayuBeanDefinition = beanDefinitionMap.get(beanName);
            //TODO 对beanPostProcessor要进行特殊处理
            if (huayuBeanDefinition.getScope().equals("singleton")){
                Object o = singletonObjects.get(beanName);
                return o;
            } else {
                //创建bean对象
                Object bean = createBean(beanName, huayuBeanDefinition);
                return bean;
            }
        } else {
            return null;
        }
    }
}
