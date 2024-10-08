package org.huayu.web.convert;

import org.huayu.web.handler.HandlerMethod;
import org.springframework.util.ObjectUtils;


import java.lang.reflect.Method;

/**
 * 类型转换器封装对象
 */
public class ConvertHandler extends HandlerMethod {

    public ConvertHandler(Object bean, Method method) {
        super(bean,method);
    }

    public Object convert(Object arg) throws Exception {
        if (ObjectUtils.isEmpty(arg)){
            return null;
        }
        return this.getMethod().invoke(this.getBean(),arg);
    }

}
