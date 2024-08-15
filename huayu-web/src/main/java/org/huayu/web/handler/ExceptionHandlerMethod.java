package org.huayu.web.handler;

import java.lang.reflect.Method;

/**
 * 异常处理器方法的封装类
 */
public class ExceptionHandlerMethod extends HandlerMethod{
    public ExceptionHandlerMethod(Object bean, Method method) {
        super(bean, method);
    }
}
