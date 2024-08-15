package org.huayu.web.resolver;

import org.huayu.web.convert.ConvertComposite;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.support.WebServletRequest;
import org.springframework.core.MethodParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参数解析器组合器
 */
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver{

    final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

    //避免参数解析器被不断遍历
    Map<MethodParameter,HandlerMethodArgumentResolver> argumentResolverCache = new HashMap<>();

    /**
     * 判断是否支持该参数
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        for (HandlerMethodArgumentResolver resolver : this.resolvers) {
            if (resolver.supportsParameter(parameter)) {
                argumentResolverCache.put(parameter,resolver);
                return true;
            }
        }
        //如果返回false，说明我们当前没有参数解析器可以应对当前的场景
        return false;
    }

    protected HandlerMethodArgumentResolver getReSolverArgument(MethodParameter parameter){
        return argumentResolverCache.get(parameter);
    }

    /**
     * 获取参数解析器解析参数
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, WebServletRequest webServletRequest, ConvertComposite convertComposite) throws Exception {

        //1.获取对应的参数解析器
        final HandlerMethodArgumentResolver reSolverArgument = getReSolverArgument(parameter);
        return reSolverArgument.resolveArgument(parameter,handlerMethod,webServletRequest,convertComposite);
    }

    public void addResolvers(List<HandlerMethodArgumentResolver> resolvers){
        this.resolvers.addAll(resolvers);
    }
}
