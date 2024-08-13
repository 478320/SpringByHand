package org.springframework.beans.factory.config;

/**
 *
 */
public interface ConfigurableBeanFactory extends SingletonBeanRegistry, HierarchicalBeanFactory {
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
    void destroySingletons();
}
