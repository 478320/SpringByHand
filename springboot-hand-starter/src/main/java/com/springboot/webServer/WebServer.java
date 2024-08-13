package com.springboot.webServer;

import org.huayu.web.context.AbstractRefreshableWebApplicationContext;
import org.huayu.web.context.AnnotationConfigWebApplicationContext;

/**
 *
 */
public interface WebServer {

    void start(AnnotationConfigWebApplicationContext applicationContext);
}
