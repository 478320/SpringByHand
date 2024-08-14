package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.support.BeanDefinitionRegistry;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;

/**
 * 概念展示，注解Bean定义的读取器
 */
public class AnnotatedBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
        AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
    }

    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registerBean(componentClass);
        }
    }

    public void registerBean(Class<?> beanClass) {
        doRegisterBean(beanClass, null, null, null);
    }

    private <T> void doRegisterBean(Class<T> beanClass, String name,
                                    Class<? extends Annotation>[] qualifiers,Supplier<T> supplier
                                    ) {
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
        String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(abd, this.registry));
        registry.registerBeanDefinition("",new GenericBeanDefinition(beanClass));
    }


}
