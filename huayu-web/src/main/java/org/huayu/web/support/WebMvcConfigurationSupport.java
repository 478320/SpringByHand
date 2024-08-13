package org.huayu.web.support;

import org.huayu.web.adapter.HandlerMethodAdapter;
import org.huayu.web.adapter.RequestMappingHandlerMethodAdapter;
import org.huayu.web.handler.HandlerMapping;
import org.huayu.web.handler.RequestMappingHandlerMapping;
import org.huayu.web.intercpetor.InterceptorRegistry;
import org.huayu.web.intercpetor.MappedInterceptor;
import org.huayu.web.resolver.DefaultHandlerExceptionResolver;
import org.huayu.web.resolver.ExceptionHandlerExceptionResolver;
import org.huayu.web.resolver.HandlerExceptionResolver;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 初始化组件
 */
public abstract class WebMvcConfigurationSupport {

    // 初始化组件

    @Bean
    public HandlerMapping handlerMapping(){
        final RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setOrder(0);
        InterceptorRegistry registry = new InterceptorRegistry();
        getIntercept(registry);
        // todo 通过 registry 获取 MappedInterceptor
        // 获取拦截器
        final List<MappedInterceptor> interceptors = registry.getInterceptors();
        requestMappingHandlerMapping.addHandlerInterceptors(interceptors);
        //添加拦截器
        return requestMappingHandlerMapping;
    }

    protected abstract void getIntercept(InterceptorRegistry registry);

    @Bean
    public HandlerMethodAdapter handlerMethodAdapter(){
        final RequestMappingHandlerMethodAdapter requestMappingHandlerMethodAdapter = new RequestMappingHandlerMethodAdapter();
        requestMappingHandlerMethodAdapter.setOrder(0);
        return requestMappingHandlerMethodAdapter;
    }

    @Bean
    public HandlerExceptionResolver defaulthandlerExceptionResolver(){
        final DefaultHandlerExceptionResolver defaultHandlerExceptionResolver = new DefaultHandlerExceptionResolver();
        defaultHandlerExceptionResolver.setOrder(1);
        return defaultHandlerExceptionResolver;
    }

    @Bean
    public HandlerExceptionResolver handlerExceptionResolver(){
        final ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionHandlerExceptionResolver.setOrder(0);
        return exceptionHandlerExceptionResolver;
    }
}

