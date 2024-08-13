package org.springframework.beans.factory.config;

/**
 *
 */
public interface BeanFactory {

    Object getBean(String name);
    <T> T getBean(String name, Class<T> requiredType);
    boolean containsBean(String name);
    boolean isSingleton(String name);
    boolean isPrototype(String name);

}
