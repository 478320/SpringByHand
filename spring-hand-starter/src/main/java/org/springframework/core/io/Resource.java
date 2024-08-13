package org.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public interface Resource {

    //检查资源是否存在
    boolean exists();

    //检查资源是否可读
    boolean isReadable();

    //获取资源的输入流
    InputStream getInputStream() throws IOException;

    //返回资源的描述信息
    String getDescription();
}
