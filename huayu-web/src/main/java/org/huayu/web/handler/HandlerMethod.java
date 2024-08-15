package org.huayu.web.handler;

import org.huayu.web.annotation.RequestMethod;
import org.huayu.web.convert.ConvertHandler;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 处理器方法类
 */
public class HandlerMethod {

    protected Object bean;

    protected Class type;

    protected Method method;

    protected String path;

    protected RequestMethod[] requestMethods = new RequestMethod[0];

    // spring提供的增强的参数
    protected MethodParameter[] parameters = new MethodParameter[0];

    // 异常处理器
    private Map<Class,ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();

    // 类型转换器
    private Map<Class, ConvertHandler> convertHandlerMap = new HashMap<>();

    public Map<Class, ConvertHandler> getConvertHandlerMap() {
        return convertHandlerMap;
    }

    public void setConvertHandlerMap(Map<Class, ConvertHandler> convertHandlerMap) {
        this.convertHandlerMap = convertHandlerMap;
    }

    public Map<Class, ExceptionHandlerMethod> getExceptionHandlerMethodMap() {
        return exceptionHandlerMethodMap;
    }

    public void setExceptionHandlerMethodMap(Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap) {
        this.exceptionHandlerMethodMap = exceptionHandlerMethodMap;
    }

    public HandlerMethod() {
    }

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.type = bean.getClass();
        this.method = method;
        final Parameter[] parameters = method.getParameters();
        MethodParameter[] methodParameters = new MethodParameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            methodParameters[i] = new MethodParameter(method,i);
        }
        this.parameters = methodParameters;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }


    //如果为空则设置所有请求类型
    public void setRequestMethods(RequestMethod[] requestMethods) {
        if (ObjectUtils.isEmpty(requestMethods)){
            requestMethods = RequestMethod.values();
        }
        this.requestMethods = requestMethods;
    }

    public MethodParameter[] getParameters() {
        return parameters;
    }

    public RequestMethod[] getRequestMethods() {
        return requestMethods;
    }

    public Method getMethod() {
        return method;
    }

    public Object getBean() {
        return bean;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {return true;}
        if (object == null || getClass() != object.getClass()) {return false;}
        HandlerMethod that = (HandlerMethod) object;
        return Objects.equals(path, that.path) && Arrays.equals(requestMethods, that.requestMethods);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(path);
        result = 31 * result + Arrays.hashCode(requestMethods);
        return result;
    }

    @Override
    public String toString() {
        return "HandlerMethod{" +
                "bean=" + bean +
                ", type=" + type +
                ", method=" + method +
                ", path='" + path + '\'' +
                ", requestMethods=" + Arrays.toString(requestMethods) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
