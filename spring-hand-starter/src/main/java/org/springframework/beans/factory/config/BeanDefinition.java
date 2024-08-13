package org.springframework.beans.factory.config;

/**
 *
 */
public interface BeanDefinition {

    String getBeanClassName();

    Class<?> getBeanClass();

    String getScope();


}
