package org.springframework.context;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.metrics.ApplicationStartup;

/**
 *
 */
public interface ConfigurableApplicationContext extends ApplicationContext{

    String CONVERSION_SERVICE_BEAN_NAME = "conversionService";

    //ConfigurableListableBeanFactory是DefaultListableBeanFactory的接口,除了被淘汰的xmlBeanFactory，DefaultListableBeanFactory就是其唯一实现类
    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    ApplicationStartup getApplicationStartup();
}
