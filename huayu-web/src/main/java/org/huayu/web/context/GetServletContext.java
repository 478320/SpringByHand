package org.huayu.web.context;

import javax.servlet.ServletContext;

/**
 *
 */
public class GetServletContext implements ServletContextAware{

    ServletContext servletContext;

    //由于set注入该方法的Set方法会被调用两次，所以需要让BeanFactory排除掉实现ServletXXXAware的接口，来避免它被多次调用
    @Override
    public void setServletContext(ServletContext servletContext) {

    }
}
