package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.core.support.BeanDefinitionRegistry;

/**
 * 非主要内容不做介绍
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {


    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);
}
