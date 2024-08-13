package org.huayu.web.context;

import org.springframework.context.ApplicationContext;

/**
 *
 */
public interface WebApplicationContext extends ApplicationContext {
    String ROOT_NAME = WebApplicationContext.class.getName() + "ROOT";
}
