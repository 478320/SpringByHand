package org.huayu.web.context;

import org.springframework.beans.factory.Aware;

import javax.servlet.ServletContext;

/**
 * 自定义设置容器上下文注解
 */
public interface ServletContextAware extends Aware {

    void setServletContext(ServletContext servletContext);
}
