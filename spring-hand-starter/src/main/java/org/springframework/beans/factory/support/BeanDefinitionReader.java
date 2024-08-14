package org.springframework.beans.factory.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.support.BeanDefinitionRegistry;

/**
 * 概念展示，Bean定义的读取器接口
 */
public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    int loadBeanDefinitions(Resource... resources);

    int loadBeanDefinitions(String location);

    int loadBeanDefinitions(String... locations);

    ResourceLoader getResourceLoader();

    int loadBeanDefinitions(Resource resource);
}
