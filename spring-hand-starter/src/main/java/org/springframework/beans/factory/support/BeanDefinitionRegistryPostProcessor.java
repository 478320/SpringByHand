package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.core.support.BeanDefinitionRegistry;

/**
 *
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {


    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);
}
