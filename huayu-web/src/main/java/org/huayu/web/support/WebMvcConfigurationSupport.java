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
 * @EnableWebMVC 导入类的抽象类，用于初始化组件，允许拦截器操作
 */
public abstract class WebMvcConfigurationSupport {

    /**
     * 扫描拦截器并注册到映射器中
     */
    @Bean
    public HandlerMapping handlerMapping() {
        final RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setOrder(0);
        InterceptorRegistry registry = new InterceptorRegistry();
        // 注册中心扫描并获取拦截器
        getIntercept(registry);
        // 获取拦截器
        final List<MappedInterceptor> interceptors = registry.getInterceptors();
        //添加拦截器
        requestMappingHandlerMapping.addHandlerInterceptors(interceptors);
        return requestMappingHandlerMapping;
    }

    protected abstract void getIntercept(InterceptorRegistry registry);

    /**
     * 初始化适配器
     */
    @Bean
    public HandlerMethodAdapter handlerMethodAdapter() {
        final RequestMappingHandlerMethodAdapter requestMappingHandlerMethodAdapter = new RequestMappingHandlerMethodAdapter();
        requestMappingHandlerMethodAdapter.setOrder(0);
        return requestMappingHandlerMethodAdapter;
    }

    /**
     * 添加默认异常处理器
     */
    @Bean
    public HandlerExceptionResolver defaulthandlerExceptionResolver() {
        final DefaultHandlerExceptionResolver defaultHandlerExceptionResolver = new DefaultHandlerExceptionResolver();
        defaultHandlerExceptionResolver.setOrder(1);
        return defaultHandlerExceptionResolver;
    }

    /**
     * 添加用户自定义异常处理器，初始化会扫描并获取到用户的自定义异常拦截器类
     */
    @Bean
    public HandlerExceptionResolver handlerExceptionResolver() {
        final ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionHandlerExceptionResolver.setOrder(0);
        return exceptionHandlerExceptionResolver;
    }
}

