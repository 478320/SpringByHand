package com.springboot;

import com.springboot.webServer.WebServer;
import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.huayu.web.DispatcherServlet;
import org.huayu.web.context.AnnotationConfigWebApplicationContext;
import org.huayu.web.context.WebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;


/**
 * 自定义boot程序核心类
 */
public class HuayuSpringApplication {


    //java -jar xxx.jar --k1=v1 配了没传args就没有用，失效，先不关心

    /**
     * 用于启动Web程序，包含配置类注册Web程序启动等功能
     *
     * @param configClazz 配置类
     */
    public static void run(Class<?> configClazz) {
        //创建Spring容器
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(configClazz);
        applicationContext.refresh();
        WebServer webServer = getWebServer(applicationContext);
        webServer.start(applicationContext);
    }

    /**
     * 从程序中获取到Web，并选择合适的Web进行返回
     *
     * @param applicationContext 具体容器容器
     * @return Web的Bean实例
     */
    private static WebServer getWebServer(WebApplicationContext applicationContext) {
        Map<String, WebServer> beansOfType = applicationContext.getBeansOfType(WebServer.class);
        if (beansOfType.size() == 0) {
            throw new NullPointerException();
        }
        if (beansOfType.size() > 1) {
            throw new IllegalStateException();
        }
        return beansOfType.values().stream().findFirst().get();
    }

}
