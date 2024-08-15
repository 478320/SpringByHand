package org.huayu.web;

import org.huayu.web.adapter.HandlerMethodAdapter;
import org.huayu.web.context.WebApplicationContext;
import org.huayu.web.excpetion.NotFoundExcpetion;
import org.huayu.web.handler.HandlerExecutionChain;
import org.huayu.web.handler.HandlerMapping;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.resolver.HandlerExceptionResolver;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 自定义的Servlet在MVC中起到关键作用
 */
public class DispatcherServlet extends BaseHttpServlet {

    // 映射器
    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    // 适配器
    private List<HandlerMethodAdapter> handlerMethodAdapters = new ArrayList<>();

    private Properties defaultStrategies;

    // 异常处理器
    private List<HandlerExceptionResolver> handlerExceptionResolvers = new ArrayList<>();

    public static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";

    public DispatcherServlet(ApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    /**
     * 初始化DispatcherServlet的映射器，适配器，异常处理器
     */
    @Override
    protected void onRefresh(ApplicationContext webApplicationContext) {
        initHandlerMapping(webApplicationContext);
        initHandlerAdapter(webApplicationContext);
        initHandlerException(webApplicationContext);
    }

    /**
     * 初始化异常解析器
     */
    private void initHandlerException(ApplicationContext webApplicationContext) {
        //从容器中拿
        final Map<String, HandlerExceptionResolver> map = BeanFactoryUtils.beansOfTypeIncludingAncestors(webApplicationContext, HandlerExceptionResolver.class, true, false);
        if (!ObjectUtils.isEmpty(map)) {
            this.handlerExceptionResolvers = new ArrayList<>(map.values());
        } else {
            //没有则从默认配置文件中拿
            this.handlerExceptionResolvers.addAll(getDefaultStrategies(webApplicationContext, HandlerExceptionResolver.class));
        }
        this.handlerExceptionResolvers.sort(Comparator.comparingInt(Ordered::getOrder));
    }

    /**
     * 初始化适配器
     */
    private void initHandlerAdapter(ApplicationContext webApplicationContext) {
        //从容器中拿
        final Map<String, HandlerMethodAdapter> map = BeanFactoryUtils.beansOfTypeIncludingAncestors(webApplicationContext, HandlerMethodAdapter.class, true, false);
        if (!ObjectUtils.isEmpty(map)) {
            this.handlerMethodAdapters = new ArrayList<>(map.values());
        } else {
            //没有则从默认配置文件中拿
            this.handlerMethodAdapters.addAll(getDefaultStrategies(webApplicationContext, HandlerMethodAdapter.class));
        }
        this.handlerMethodAdapters.sort(Comparator.comparingInt(Ordered::getOrder));
    }

    /**
     * 初始话映射器
     */
    private void initHandlerMapping(ApplicationContext webApplicationContext) {
        //从容器中拿
        final Map<String, HandlerMapping> map = BeanFactoryUtils.beansOfTypeIncludingAncestors(webApplicationContext, HandlerMapping.class, true, false);
        if (!ObjectUtils.isEmpty(map)) {
            this.handlerMappings = new ArrayList<>(map.values());
        } else {
            //没有则从默认配置文件中拿
            this.handlerMappings.addAll(getDefaultStrategies(webApplicationContext, HandlerMapping.class));
        }
        this.handlerMappings.sort(Comparator.comparingInt(Ordered::getOrder));


    }

    protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
        if (defaultStrategies == null) {
            try {
                ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, DispatcherServlet.class);
                defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not load '" + DEFAULT_STRATEGIES_PATH + "': " + ex.getMessage());
            }
        }

        String key = strategyInterface.getName();
        String value = defaultStrategies.getProperty(key);
        if (value != null) {
            String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
            List<T> strategies = new ArrayList<>(classNames.length);
            for (String className : classNames) {
                try {
                    Class<?> clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
                    Object strategy = createDefaultStrategy(context, clazz);
                    strategies.add((T) strategy);
                } catch (ClassNotFoundException ex) {
                    throw new BeanInitializationException(
                            "Could not find DispatcherServlet's default strategy class [" + className +
                                    "] for interface [" + key + "]", ex);
                } catch (LinkageError err) {
                    throw new BeanInitializationException(
                            "Unresolvable class definition for DispatcherServlet's default strategy class [" +
                                    className + "] for interface [" + key + "]", err);
                }
            }
            return strategies;
        } else {
            return Collections.emptyList();
        }
    }

    protected Object createDefaultStrategy(ApplicationContext context, Class<?> clazz) {
        return context.getAutowireCapableBeanFactory().createBean(clazz);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Exception ex = null;
        HandlerExecutionChain handlerExecutionChain = null;
        try {
            handlerExecutionChain = getHandler(req);
            //handlerMethod找不到则返回404
            if (ObjectUtils.isEmpty(handlerExecutionChain)) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            handlerExecutionChain.afterCompletion(req, resp, handlerExecutionChain.getHandlerMethod(), ex);

            // 获得适配器
            HandlerMethodAdapter ha = getHandlerMethodAdapter(handlerExecutionChain.getHandlerMethod());
            if (!handlerExecutionChain.applyPreInterceptor(req, resp)) {
                return;
            }
            ha.handler(req, resp, handlerExecutionChain.getHandlerMethod());
            handlerExecutionChain.applyPostInterceptor(req, resp);
        } catch (Exception e) {
            ex = e;
        }
        try {
            processResult(req, resp, handlerExecutionChain, ex);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }


    private void processResult(HttpServletRequest req, HttpServletResponse resp, HandlerExecutionChain handlerExecutionChain, Exception ex) throws Exception {

        if (ex != null) {
            HandlerMethod handlerMethod = handlerExecutionChain == null ? null : handlerExecutionChain.getHandlerMethod();
            processResultException(req, resp, handlerMethod, ex);
        }
        // after todo 拦截器
    }

    private void processResultException(HttpServletRequest req, HttpServletResponse resp, HandlerMethod handlerMethod, Exception ex) throws Exception {

        for (HandlerExceptionResolver handlerExceptionResolver : this.handlerExceptionResolvers) {
            if (handlerExceptionResolver.resolveException(req, resp, handlerMethod, ex)) {
                return;
            }
        }
        throw new ServletException(ex.getMessage());
    }

    private HandlerMethodAdapter getHandlerMethodAdapter(HandlerMethod handlerMethod) throws Exception {
        for (HandlerMethodAdapter handlerMethodAdapter : this.handlerMethodAdapters) {
            if (handlerMethodAdapter.support(handlerMethod)) {
                return handlerMethodAdapter;
            }
        }
        throw new NotFoundExcpetion(handlerMethod + "没有支持的适配器");
    }

    //获取映射器
    private HandlerExecutionChain getHandler(HttpServletRequest req) throws Exception {
        // 拿到所有组件进行遍历
        for (HandlerMapping handlerMapping : handlerMappings) {
            final HandlerExecutionChain handler = handlerMapping.getHandler(req);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }


}
