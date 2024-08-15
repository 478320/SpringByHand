package org.huayu.web.context;

import org.springframework.context.ApplicationContext;

/**
 * 自定义Web容器接口
 */
public interface WebApplicationContext extends ApplicationContext {
    String ROOT_NAME = WebApplicationContext.class.getName() + "ROOT";
}
