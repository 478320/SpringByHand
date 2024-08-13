package org.springframework.beans.factory.config;

/**
 *
 */
public interface ListableBeanFactory extends BeanFactory {
    String[] getBeanDefinitionNames();
    int getBeanDefinitionCount();
}
