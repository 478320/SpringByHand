package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 概念展示，类路径资源类
 */
public class ClassPathResource extends AbstractFileResolvingResource{
    private final String path;

    public ClassPathResource(String path) {
        this.path = path;
    }

    @Override
    public boolean exists() {
        return getClass().getClassLoader().getResource(path) != null;
    }

    @Override
    public boolean isReadable() {
        return exists();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new IOException("Resource not found: " + path);
        }
        return is;
    }

    @Override
    public String getDescription() {
        return "ClassPathResource [path=" + path + "]";
    }
}
