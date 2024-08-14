package org.springframework.beans.factory.config;

/**
 *可获得Bean属性的Bean工厂接口
 */
public interface ListableBeanFactory extends BeanFactory {
    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();
}
