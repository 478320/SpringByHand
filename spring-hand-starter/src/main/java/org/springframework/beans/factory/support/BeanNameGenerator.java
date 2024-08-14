package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.support.BeanDefinitionRegistry;

/**
 * BeanName生成器抽象接口（未实现）
 */
public interface BeanNameGenerator {

    String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry);
}
