package org.huayu.web.handler;

import javax.servlet.http.HttpServletRequest;

/**
 * 不提供实现只说明有这个场景
 */
public class BeanNameUrlHandlerMapping extends AbstractHandlerMapping{
    @Override
    protected HandlerMethod getHandlerInternal(HttpServletRequest request) {
        return null;
    }

    @Override
    protected void detectHandlerMethod(String name) {

    }

    @Override
    protected boolean isHandler(Class type) {
        return false;
    }

    @Override
    protected void setOrder(int order) {
        this.order = 2;
    }
}
