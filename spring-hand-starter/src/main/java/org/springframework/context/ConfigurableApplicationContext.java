package org.springframework.context;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.metrics.ApplicationStartup;

/**
 * 可配置的容器上下文
 */
public interface ConfigurableApplicationContext extends ApplicationContext{

    String CONVERSION_SERVICE_BEAN_NAME = "conversionService";

    //ConfigurableListableBeanFactory是DefaultListableBeanFactory的接口,除了被淘汰的xmlBeanFactory，DefaultListableBeanFactory就是其唯一实现类
    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    ApplicationStartup getApplicationStartup();
}
