package org.springframework.beans.factory.config;

/**
 *
 */
public interface AutowireCapableBeanFactory extends BeanFactory{
    Object createBean(Class<?> beanClass) ;
    void autowireBean(Object existingBean);
}
