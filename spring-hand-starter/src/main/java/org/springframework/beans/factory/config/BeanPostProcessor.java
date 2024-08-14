package org.springframework.beans.factory.config;

/**
 * Bean的拓展处理器
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean,String beanName);

    Object postProcessAfterInitialization(Object bean,String beanName);

}
