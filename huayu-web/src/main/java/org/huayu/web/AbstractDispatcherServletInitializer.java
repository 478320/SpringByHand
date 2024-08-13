package org.huayu.web;

import org.huayu.web.context.WebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.util.ObjectUtils;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * 创建DispatcherServlet以及初始化Ioc
 */
public abstract class AbstractDispatcherServletInitializer implements WebApplicationInitializer{

    public static final String DEFAULT_SERVLET_NAME = "dispatcher";

    public static final String DEFAULT_FILTER_NAME = "filters";

    public static final int M = 1024*1024;
    @Override
    public void onStartUp(ServletContext servletContext) {
        //创建父容器
        final AnnotationConfigApplicationContext rootApplicationContext = createRootApplicationContext();
        //父容器放入servletContext当中
        servletContext.setAttribute(WebApplicationContext.ROOT_NAME,rootApplicationContext);
        //刷新父容器，在源码当中通过servlet事件进行刷新
        rootApplicationContext.refresh();
        //创建子容器
        final WebApplicationContext webAppContext = createWebApplicationContext();
        //创建DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webAppContext);
        ServletRegistration.Dynamic dynamic = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        //配置文件
        dynamic.setLoadOnStartup(1);//从配置信息中拿
        MultipartConfigElement configElement = new MultipartConfigElement(null,5*M,5*M,5);
        dynamic.setMultipartConfig(configElement);
        dynamic.addMapping(getMappings());
        Filter[] filters = getFilters();
        if (!ObjectUtils.isEmpty(filters)){
            for (Filter filter : filters) {
                servletContext.addFilter(DEFAULT_FILTER_NAME,filter);
            }
        }

    }

    // 过滤器
    protected abstract Filter[] getFilters();
    // 映射器
    protected  String[] getMappings(){
        return new String[]{"/"};
    };
    //创建父容器
    protected abstract AnnotationConfigApplicationContext createRootApplicationContext();

    //创建子容器
    protected abstract WebApplicationContext createWebApplicationContext();

    //获取包扫描配置类
    protected abstract Class<?>[] getRootConfigClasses();
    protected abstract Class<?>[] getWebConfigClasses();


}
