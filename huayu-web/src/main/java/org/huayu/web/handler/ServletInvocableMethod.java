package org.huayu.web.handler;

import org.huayu.web.convert.ConvertComposite;
import org.huayu.web.excpetion.NotFoundExcpetion;
import org.huayu.web.resolver.HandlerMethodArgumentResolverComposite;
import org.huayu.web.resolver.HandlerMethodReturnValueHandlerComposite;
import org.huayu.web.support.WebServletRequest;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 用于执行HandlerMethod， 也会保存执行HandlerMethod基础组件
 */
public class ServletInvocableMethod extends HandlerMethod{

    private HandlerMethod handlerMethod;

    // 参数解析器组合器
    private HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();

    // 类型转换器组合器
    private ConvertComposite convertComposite = new ConvertComposite();

    // 参数名称注册发现
    private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    // 返回值处理器
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();

    public void setReturnValueHandlerComposite(HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite) {
        this.returnValueHandlerComposite = returnValueHandlerComposite;
    }

    public void setResolverComposite(HandlerMethodArgumentResolverComposite resolverComposite) {
        this.resolverComposite = resolverComposite;
    }

    public void setConvertComposite(ConvertComposite convertComposite) {
        this.convertComposite = convertComposite;
    }

    public ServletInvocableMethod(Object bean, Method method) {
        super(bean, method);
    }

    public ServletInvocableMethod() {
    }

    public void setHandlerMethod(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    /**
     * 执行方法逻辑
     */
    public void invokeAndHandle(WebServletRequest webServletRequest, HandlerMethod handler,Object... providerArgs) throws Exception {

        // 1.获取参数
        final Object[] methodArguments = getMethodArguments(webServletRequest, handlerMethod,providerArgs);
        // 2.执行
        final Object returnValue = doInvoke(methodArguments);
        // 3.选择返回值处理器，处理执行后的返回值
        this.returnValueHandlerComposite.doInvoke(returnValue,handler.getMethod(),webServletRequest);
    }

    /**
     * 获得方法的参数
     */
    public Object[] getMethodArguments(WebServletRequest webServletRequest,HandlerMethod handlerMethod,Object... providerArgs) throws Exception {
        final MethodParameter[] parameters = handlerMethod.getParameters();
        Object args[] = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {

            final MethodParameter parameter = parameters[i];
            //parameter要拿到name需要初始化填充
            parameter.initParameterNameDiscovery(nameDiscoverer);
            args[i] = findProviderArgs(parameter,providerArgs);
            if (args[i] != null){
                continue;
            }
            // 参数解析器
            if (!this.resolverComposite.supportsParameter(parameter)) {
                throw new NotFoundExcpetion("没有参数解析器解析参数:" + parameter.getParameterType());
            }
            args[i] = this.resolverComposite.resolveArgument(parameter,handlerMethod,webServletRequest,convertComposite);
        }
        return args;
    }

    /**
     * 发现参数
     */
    private Object findProviderArgs(MethodParameter parameter, Object[] providerArgs) {

        final Class<?> parameterType = parameter.getParameterType();
        // 遍历参数，参数的
        for (Object providerArg : providerArgs ) {
            if (parameterType == providerArg.getClass() || parameterType.isAssignableFrom(providerArg.getClass())){
                return providerArg;
            }
        }
        return null;
    }

    /**
     * 执行方法
     */
    public Object doInvoke(Object args[]) throws Exception {
        final Object returnValue = this.handlerMethod.getMethod().invoke(this.handlerMethod.getBean(), args);
        return returnValue;
    }
}
