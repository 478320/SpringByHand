package org.springframework.context.annotation;

import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanFactory;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;

/**
 *
 */
public class AutowiredAnnotationBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor,
        MergedBeanDefinitionPostProcessor, BeanFactoryAware {

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
