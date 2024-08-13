package com.springboot.webServer;

import org.huayu.web.context.AnnotationConfigWebApplicationContext;

/**
 *
 */
public class JettyWebServer implements WebServer{
    @Override
    public void start(AnnotationConfigWebApplicationContext applicationContext) {
        System.out.println("启动jetty");
    }
}
