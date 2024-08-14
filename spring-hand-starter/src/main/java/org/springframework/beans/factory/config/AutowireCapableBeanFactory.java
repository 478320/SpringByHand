package org.springframework.beans.factory.config;

/**
 * 可自动注入的Bean工厂顶层接口
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
    Object createBean(Class<?> beanClass);

    void autowireBean(Object existingBean);
}
