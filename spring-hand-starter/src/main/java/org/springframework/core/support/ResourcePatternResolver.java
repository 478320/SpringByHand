package org.springframework.core.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * 也是一种资源加载器loader
 */
public interface ResourcePatternResolver extends ResourceLoader {

    Resource[] getResources(String locationPattern) throws IOException;
}
