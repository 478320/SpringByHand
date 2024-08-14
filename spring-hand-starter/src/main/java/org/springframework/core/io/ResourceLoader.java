package org.springframework.core.io;

/**
 * 概念展示资源加载器接口
 */
public interface ResourceLoader {

    Resource getResource(String location);

    ClassLoader getClassLoader();
}
