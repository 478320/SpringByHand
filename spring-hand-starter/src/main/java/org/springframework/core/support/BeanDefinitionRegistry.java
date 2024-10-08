package org.springframework.core.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.AliasRegistry;

/**
 * Bean定义注册中心接口
 */
public interface BeanDefinitionRegistry extends AliasRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
    void removeBeanDefinition(String beanName);
    BeanDefinition getBeanDefinition(String beanName);
    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();
}
