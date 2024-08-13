package org.springframework.beans.factory.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.support.BeanDefinitionRegistry;

/**
 *
 */
public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    int loadBeanDefinitions(Resource... resources);

    int loadBeanDefinitions(String location);

    int loadBeanDefinitions(String... locations);

    ResourceLoader getResourceLoader();

    int loadBeanDefinitions(Resource resource);
}
