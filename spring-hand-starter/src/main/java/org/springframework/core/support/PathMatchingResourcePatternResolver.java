package org.springframework.core.support;

import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * 资源加载器的实现类
 */
public class PathMatchingResourcePatternResolver implements ResourcePatternResolver{
    public PathMatchingResourcePatternResolver() {
    }

    @Override
    public Resource getResource(String location) {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        return new Resource[0];
    }
}
