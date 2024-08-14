package org.springframework.context.annotation;

/**
 * 概念展示，注解配置的注册中心
 */
public interface AnnotationConfigRegistry {

    void register(Class<?>... componentClasses);

    void scan(String... basePackages);
}
