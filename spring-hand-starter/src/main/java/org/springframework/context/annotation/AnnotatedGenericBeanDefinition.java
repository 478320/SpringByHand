package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

/**
 * 概念展示，注解Bean定义的普通定义
 */
public class AnnotatedGenericBeanDefinition implements AnnotatedBeanDefinition{
    public <T> AnnotatedGenericBeanDefinition(Class<T> beanClass) {
    }

    @Override
    public String getBeanClassName() {
        return null;
    }

    @Override
    public Class<?> getBeanClass() {
        return null;
    }

    @Override
    public String getScope() {
        return null;
    }

    @Override
    public AnnotationMetadata getMetadata() {
        return null;
    }

    @Override
    public MethodMetadata getFactoryMethodMetadata() {
        return null;
    }
}
