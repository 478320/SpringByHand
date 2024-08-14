package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.support.BeanDefinitionRegistry;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 概念展示，注解配置的工具类
 */
public abstract class AnnotationConfigUtils {

    public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalConfigurationAnnotationProcessor";

    public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";

    public static void registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {
        registerAnnotationConfigProcessors(registry, null);
    }

    public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(
            BeanDefinitionRegistry registry, Object source) {

        DefaultListableBeanFactory beanFactory = unwrapDefaultListableBeanFactory(registry);
        // 对档案馆的自动注入组件进行了一些初始化
        if (beanFactory != null) {
            // 这里删掉了初始化顺序
            if (!(beanFactory.getAutowireCandidateResolver() instanceof ContextAnnotationAutowireCandidateResolver)) {
                //除了设置档案馆的处理器还会设置处理器的Bean工厂
                beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
            }
        }

        Set<BeanDefinitionHolder> beanDefs = new LinkedHashSet<>(8);

        // 如果不存在config，那就自动创建一个
        if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            GenericBeanDefinition def = new GenericBeanDefinition(ConfigurationClassPostProcessor.class);
            def.setSource(source);
            //registerPostProcessor是将configuration注册到档案馆的方法
            beanDefs.add(registerPostProcessor(registry, def, CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME));
        }

        if (!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            GenericBeanDefinition def = new GenericBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
            def.setSource(source);
            beanDefs.add(registerPostProcessor(registry, def, AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME));
        }

        return beanDefs;
    }

    private static DefaultListableBeanFactory unwrapDefaultListableBeanFactory(BeanDefinitionRegistry registry) {
        if (registry instanceof DefaultListableBeanFactory) {
            return (DefaultListableBeanFactory) registry;
        }
        else if (registry instanceof GenericApplicationContext) {
            //现在是个容器，所以会拿到我们容器的档案馆
            return ((GenericApplicationContext) registry).getDefaultListableBeanFactory();
        }
        else {
            return null;
        }
    }

    private static BeanDefinitionHolder registerPostProcessor(
            BeanDefinitionRegistry registry, GenericBeanDefinition definition, String beanName) {

        registry.registerBeanDefinition(beanName, definition);
        return new BeanDefinitionHolder(definition, beanName);
    }
}
