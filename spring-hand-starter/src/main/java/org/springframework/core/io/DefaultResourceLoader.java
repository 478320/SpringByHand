package org.springframework.core.io;

/**
 *
 */
public class DefaultResourceLoader implements ResourceLoader{
    @Override
    public Resource getResource(String location) {
        if (location.startsWith("classpath:")) {
            return new ClassPathResource(location.substring("classpath:".length()));
        }
        // 可以扩展其他类型的资源加载，如文件系统资源、URL资源等
        throw new UnsupportedOperationException("Only classpath resources are supported in this demo");
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }
}
