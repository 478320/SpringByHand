package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.io.ResourceLoader;

/**
 * 概念展示
 */
public interface ResourceLoaderAware extends Aware {

    void setResourceLoader(ResourceLoader resourceLoader);
}
