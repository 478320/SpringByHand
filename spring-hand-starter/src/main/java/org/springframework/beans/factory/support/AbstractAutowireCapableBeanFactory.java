package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 *
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    @Override
    public Object createBean(Class<?> beanClass){
        // Simplified bean creation logic
        return instantiateBean(beanClass);
    }

    protected abstract Object createBean(String beanName, GenericBeanDefinition mbd, Object[] args);

    protected abstract Object instantiateBean(Class<?> beanClass);

    // Other methods...
}
