package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

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