package com.springboot.webServer;

import org.huayu.web.context.AbstractRefreshableWebApplicationContext;
import org.huayu.web.context.AnnotationConfigWebApplicationContext;

/**
 * Web服务顶层接口
 */
public interface WebServer {

    void start(AnnotationConfigWebApplicationContext applicationContext);
}
