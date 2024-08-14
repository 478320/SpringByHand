package org.springframework.beans.factory.config;

/**
 * 概念展示
 */
public interface BeanFactoryPostProcessor {

	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);

}