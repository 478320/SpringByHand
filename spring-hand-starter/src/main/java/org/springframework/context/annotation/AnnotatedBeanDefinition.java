package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

/**
 * 概念展示，注解Bean定义
 */
public interface AnnotatedBeanDefinition extends BeanDefinition {

	/**
	 * 获取注解信息
	 */
	AnnotationMetadata getMetadata();

	/**
	 * 获取方法信息
	 */
	MethodMetadata getFactoryMethodMetadata();

}