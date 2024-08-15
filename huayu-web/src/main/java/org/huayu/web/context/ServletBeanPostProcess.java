package org.huayu.web.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * 自定义Servlet的增强器
 */
public class ServletBeanPostProcess implements BeanPostProcessor {

    private ServletContext servletContext;

    private ServletConfig servletConfig;

    public ServletBeanPostProcess(ServletContext servletContext, ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
        this.servletContext = servletContext;
    }

    /**
     * 用户想拿到servletContext/servletConfig，需要自行实现XXAware。通过接口获取属性
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean != null && bean instanceof ServletConfigAware){
            ((ServletConfigAware)bean).setServletConfig(this.servletConfig);
        }
        if (bean != null && bean instanceof ServletContextAware){
            ((ServletContextAware)bean).setServletContext(this.servletContext);
        }
        return bean;
    }
}
