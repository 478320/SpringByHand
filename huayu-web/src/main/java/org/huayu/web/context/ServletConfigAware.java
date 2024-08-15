package org.huayu.web.context;

import org.springframework.beans.factory.Aware;

import javax.servlet.ServletConfig;

/**
 * 自定义设置容器配置注解
 */
public interface ServletConfigAware extends Aware {

    void setServletConfig(ServletConfig servletConfig);
}
