package org.springframework.beans.factory.config;

/**
 * Bean定义顶层接口
 */
public interface BeanDefinition {

    String getBeanClassName();

    Class<?> getBeanClass();

    String getScope();


}
