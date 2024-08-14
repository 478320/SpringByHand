package org.springframework.beans.factory.config;

/**
 * 可配置的Bean工厂接口
 */
public interface ConfigurableBeanFactory extends SingletonBeanRegistry, HierarchicalBeanFactory {
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void destroySingletons();
}
