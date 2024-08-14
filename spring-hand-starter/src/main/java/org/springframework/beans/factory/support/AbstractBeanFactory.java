package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象Bean工厂（关键）
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    //用来存储Bean的定义信息，但是这里的Bean定义信息是什么时候拿到的呢
    private final Map<String, GenericBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);


    @Override
    public Object getBean(String name){
        // Simplified logic to get bean
        return doGetBean(name);
    }




    protected abstract Object doGetBean(String name);

        // 检查ioc容器有没有，如果已经有了，比如已经在别的bean创建的时候通过依赖创建了，那就不用再创建了

        // 看当前bean有没有依赖其他bean，所以beanDefinition里还要存dependOn,如果有就递归挨个创建

        // 拿到bean定义信息，判断是否是单例

        // 直接创建对象，具体的方法在ObjectFactory中

        // 创建bean的实例


        // 初始化bean,在创建对象的DoCreateBean后增强

    protected abstract Object createBean(String beanName, GenericBeanDefinition mbd,  Object[] args);

    private String transformedBeanName(String name) {
        return null;
    }


}
