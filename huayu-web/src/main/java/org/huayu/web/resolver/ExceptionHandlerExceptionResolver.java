package org.huayu.web.resolver;

import org.huayu.web.annotation.ControllerAdvice;
import org.huayu.web.annotation.ExceptionHandler;
import org.huayu.web.handler.ExceptionHandlerMethod;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.handler.ServletInvocableMethod;
import org.huayu.web.support.WebServletRequest;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户自定义异常处理器
 */
public class ExceptionHandlerExceptionResolver extends ApplicationObjectSupport implements HandlerExceptionResolver, InitializingBean {


    private int order;

    // 异常处理器方法
    private Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();

    // 参数解析器组合器
    private HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();

    // 返回值处理器组合器
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();

    public void setOrder(int order) {
        this.order = order;
    }


    /**
     * 处理异常
     */
    @Override
    public Boolean resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws Exception {
        final ExceptionHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handler, ex);
        if (!ObjectUtils.isEmpty(exceptionHandlerMethod)) {

            final WebServletRequest webServletRequest = new WebServletRequest(request, response);
            final ServletInvocableMethod servletInvocableMethod = new ServletInvocableMethod();
            servletInvocableMethod.setExceptionHandlerMethodMap(exceptionHandlerMethodMap);
            servletInvocableMethod.setResolverComposite(resolverComposite);
            servletInvocableMethod.setReturnValueHandlerComposite(returnValueHandlerComposite);
            servletInvocableMethod.setHandlerMethod(exceptionHandlerMethod);
            servletInvocableMethod.invokeAndHandle(webServletRequest, exceptionHandlerMethod, ex);
            return true;
        }
        return false;
    }

    /**
     * 获取到异常方法
     */
    public ExceptionHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception ex) {
        Class aClass = ex.getClass();
        ExceptionHandlerMethod exceptionHandlerMethod = null;
        // 找局部
        if (handlerMethod != null && handlerMethod.getExceptionHandlerMethodMap().size() != 0) {
            Map<Class, ExceptionHandlerMethod> exMap = handlerMethod.getExceptionHandlerMethodMap();
            while (exceptionHandlerMethod == null) {
                exceptionHandlerMethod = exMap.get(aClass);
                aClass = aClass.getSuperclass();
                if (aClass == Throwable.class && exceptionHandlerMethod == null) {
                    break;
                }
            }
        }
        aClass = ex.getClass();
        // 找全局
        while (exceptionHandlerMethod == null) {
            exceptionHandlerMethod = this.exceptionHandlerMethodMap.get(aClass);
            aClass = aClass.getSuperclass();
            if (aClass == Throwable.class && exceptionHandlerMethod == null) {
                break;
            }
        }

        return exceptionHandlerMethod;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 初始化基础组件
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        exceptionHandlerMethodMap.putAll(initExceptionHandler());
        resolverComposite.addResolvers(getDefaultArgumentResolver());
        returnValueHandlerComposite.addMethodReturnValueHandlers(getDefaultMethodReturnValueHandler());
    }

    /**
     * 初始化参数解析器
     */
    public List<HandlerMethodArgumentResolver> getDefaultArgumentResolver() {
        final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        return resolvers;
    }

    /**
     * 初始化返回值处理器
     */
    public List<HandlerMethodReturnValueHandler> getDefaultMethodReturnValueHandler() {
        final ArrayList<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>();
        handlerMethodReturnValueHandlers.add(new RequestResponseBodyMethodReturnValueHandler());
        return handlerMethodReturnValueHandlers;

    }

    /**
     * 初始化异常解析器
     */
    public Map<Class, ExceptionHandlerMethod> initExceptionHandler() {
        final ApplicationContext context = obtainApplicationContext();
        Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();
        // 从容器当中拿带有ControllerAdvice Bean
        final String[] names = BeanFactoryUtils.beanNamesForAnnotationIncludingAncestors(context, ControllerAdvice.class);
        for (String name : names) {
            final Class<?> type = context.getType(name);
            final Method[] methods = type.getDeclaredMethods();
            for (Method method : methods) {
                if (AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class)) {
                    final ExceptionHandler exceptionHandler = AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
                    final Class<? extends Throwable> exType = exceptionHandler.value();
                    // 收集
                    exceptionHandlerMethodMap.put(exType, new ExceptionHandlerMethod(context.getBean(name), method));
                }
            }
        }
        return exceptionHandlerMethodMap;
    }
}
