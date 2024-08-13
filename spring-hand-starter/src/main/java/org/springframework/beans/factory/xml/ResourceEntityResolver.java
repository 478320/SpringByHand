package org.springframework.beans.factory.xml;

import org.springframework.core.io.ResourceLoader;

/**
 *
 */
public class ResourceEntityResolver extends DelegatingEntityResolver{

    private final ResourceLoader resourceLoader;


    /**
     * Create a ResourceEntityResolver for the specified ResourceLoader
     * (usually, an ApplicationContext).
     * @param resourceLoader the ResourceLoader (or ApplicationContext)
     * to load XML entity includes with
     */
    public ResourceEntityResolver(ResourceLoader resourceLoader) {
        //这个getClassLoader()我暂时设置为null了
        super(resourceLoader.getClassLoader());
        this.resourceLoader = resourceLoader;
    }
}
