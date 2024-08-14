package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.support.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 非常重要，这是Spring的档案馆，用来管理Bean的定义信息，它的其中一个父类的Spring真正的Ioc容器，为了方便获取
 * 我将这个Ioc容器设置为了protected，该子类可以轻松拿到并设置
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory, BeanDefinitionRegistry {

    public ConcurrentHashMap<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    // 该map用于保存BeanDefinition的信息
    private final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    // 该List用于保存bean的名字信息
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);


    private AutowireCandidateResolver autowireCandidateResolver = SimpleAutowireCandidateResolver.INSTANCE;

    /**
     * 返回自动注入处理器
     */
    public AutowireCandidateResolver getAutowireCandidateResolver() {
        return this.autowireCandidateResolver;
    }

    /**
     * 非关键
     */
    public void setAutowireCandidateResolver(AutowireCandidateResolver autowireCandidateResolver) {
        if (autowireCandidateResolver instanceof BeanFactoryAware) {
            // 这里将自动注入的候选解析器的BeanFactory成功设置，将在Autowire时有所帮助
            ((BeanFactoryAware) autowireCandidateResolver).setBeanFactory(this);
        }
        this.autowireCandidateResolver = autowireCandidateResolver;
    }


    /**
     * 根据BeanName获取Bean的定义信息
     *
     * @param beanName Bean名称
     * @return Bean定义信息
     */
    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            // TODO 抛出异常
            System.out.println(beanName + "bean定义不存在");
        }
        return beanDefinition;
    }

    //---------------------------------------------------------------------
    // 实现BeanDefinitionRegistry接口
    //---------------------------------------------------------------------

    /**
     * 这是将bean的定义信息放入档案馆的方法，源码很长，这边被简化
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
        // 简单判断Bean定义是否存在
        if (existingDefinition != null) {
            return;
        } else {
            //对档案馆和bean的名字赋值
            beanDefinitionMap.put(beanName, beanDefinition);
            List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
            updatedDefinitions.addAll(this.beanDefinitionNames);
            updatedDefinitions.add(beanName);
            this.beanDefinitionNames = updatedDefinitions;
        }
    }

    /**
     * 删除Bean定义信息
     * @param beanName Bean名称
     */
    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionMap.remove(beanName);
    }

    /**
     * 判断Bean定义信息是否包含的方法
     *
     * @param beanName Bean名称
     * @return 是否包含Bean定义信息的结果
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    @Override
    public boolean isConfigurationFrozen() {
        // Simplified logic
        return false;
    }

    @Override
    public void freezeConfiguration() {
        // Simplified logic
    }

    @Override
    public void preInstantiateSingletons() {

        List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
        for (String beanName : beanNames) {
            getBean(beanName);
        }

    }

    @Override
    protected Object doGetBean(String name) {
        BeanDefinition BeanDefinition = getBeanDefinition(name);
        return createBean(BeanDefinition.getBeanClass());
    }

    @Override
    protected Object createBean(String beanName, GenericBeanDefinition mbd, Object[] args) {
        return null;
    }

    @Override
    protected Object instantiateBean(Class<?> beanClass) {
        try {
            return beanClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // TODO 可能有问题
        return null;
    }

    //--------------------------------------------------------------------------------------------------
    //分割线之下暂时没用到
    @Override
    public void autowireBean(Object existingBean) {

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
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {

    }

    @Override
    public void destroySingletons() {

    }


}

