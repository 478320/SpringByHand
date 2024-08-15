package org.huayu.web.handler;

import org.huayu.web.annotation.ConvertType;
import org.huayu.web.annotation.ExceptionHandler;
import org.huayu.web.annotation.RequestMapping;
import org.huayu.web.convert.ConvertHandler;
import org.huayu.web.intercpetor.HandlerInterceptor;
import org.huayu.web.intercpetor.MappedInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射器实现类，个人感觉重要程度仅次于DispatcherServlet
 */
public class RequestMappingHandlerMapping extends AbstractHandlerMapping {


    /**
     * 获得request对应的内部处理器方法
     */
    @Override
    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
        return lockUpPath(request);
    }

    /**
     * 检查处理器并注册处理器方法
     */
    @Override
    protected void detectHandlerMethod(String name) throws Exception {
        // 获取当前类
        final ApplicationContext context = obtainApplicationContext();
        final Class<?> type = context.getType(name);
        // 获取当前类下的所有方法
        final Method[] methods = type.getDeclaredMethods();
        List<HandlerMethod> handlerMethods = new ArrayList<>();
        // 获得类上的路径
        String path = "";
        if (AnnotatedElementUtils.hasAnnotation(type, RequestMapping.class)) {
            final RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(type, RequestMapping.class);
            final String value = requestMapping.value();
            path = value.equals("") ? "" : value;
        }

        // 收集局部异常解析器
        Map<Class,ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();
        final Object bean = context.getBean(name);

        // 收集局部类型转换器
        Map<Class, ConvertHandler> convertHandlerMap = new HashMap<>();
        for (Method method : methods) {
            // 获取方法上存在ExceptionHandler注解的注解
            if (AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class)){
                final ExceptionHandler exceptionHandler = AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
                exceptionHandlerMethodMap.put(exceptionHandler.value(),new ExceptionHandlerMethod(bean,method));
            }

            if (AnnotatedElementUtils.hasAnnotation(method, ConvertType.class)){
                final ConvertType convertType = AnnotatedElementUtils.findMergedAnnotation(method, ConvertType.class);
                convertHandlerMap.put(convertType.value(),new ConvertHandler(bean,method));
            }
            // 获取方法上是否存在RequestMapping注解
            if (AnnotatedElementUtils.hasAnnotation(method, RequestMapping.class)) {
                // 收集
                final HandlerMethod handlerMethod = new HandlerMethod(bean, method);
                // 获得方法上的路径

                final RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                final String value = requestMapping.value();
                String childPath = value.equals("") ? "" : value;
                handlerMethod.setRequestMethods(requestMapping.requestMethod());
                // 拼接路径
                handlerMethod.setPath(path + childPath);

                handlerMethods.add(handlerMethod);

            }
        }
        // 注册HandlerMethod
        if (!ObjectUtils.isEmpty(handlerMethods)) {
            for (HandlerMethod handlerMethod : handlerMethods) {
                handlerMethod.setExceptionHandlerMethodMap(exceptionHandlerMethodMap);
                handlerMethod.setConvertHandlerMap(convertHandlerMap);
                registerMapper(handlerMethod);
            }
        }
    }

    /**
     * 如果一个类标注了Controller就算handler，从当前类中找到所有标注了RequestMapping的方法，注册
     *
     * @param type
     * @return
     */
    @Override
    protected boolean isHandler(Class type) {
        //该工具类可以帮助我们递归的去找controller注解，否则RestController注解它就找不到了
        return AnnotatedElementUtils.hasAnnotation(type, Controller.class);
    }

    @Override
    public void setOrder(int order) {
        super.setOrder(order);
    }
}
