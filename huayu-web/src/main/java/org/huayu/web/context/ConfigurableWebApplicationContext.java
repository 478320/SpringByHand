package org.huayu.web.context;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * 可配置的Web容器接口
 */
public interface ConfigurableWebApplicationContext extends WebApplicationContext{

    void setServletContext(ServletContext servletContext);

    void setServletConfig(ServletConfig servletConfig);

    ServletContext getServletContext();

    ServletConfig getServletConfig();
}
