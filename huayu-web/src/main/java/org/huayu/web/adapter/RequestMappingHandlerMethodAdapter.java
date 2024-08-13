package org.huayu.web.adapter;

import org.huayu.web.annotation.ControllerAdvice;
import org.huayu.web.annotation.ConvertType;
import org.huayu.web.annotation.RequestMapping;
import org.huayu.web.convert.*;
import org.huayu.web.handler.HandlerMethod;
import org.huayu.web.handler.ServletInvocableMethod;
import org.huayu.web.resolver.*;
import org.huayu.web.support.WebServletRequest;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 处理RequestMapping注解
 */
public class RequestMappingHandlerMethodAdapter extends ApplicationObjectSupport implements HandlerMethodAdapter, InitializingBean {

    //参数解析器组合器
    private HandlerMethodArgumentResolverComposite resolverComposite = new HandlerMethodArgumentResolverComposite();

    private ConvertComposite convertComposite = new ConvertComposite();

    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();

    private int order;

    /**
     *
     * @param handlerMethod
     * @return
     */
    @Override
    public boolean support(HandlerMethod handlerMethod) {
        return AnnotatedElementUtils.hasAnnotation(handlerMethod.getMethod(), RequestMapping.class);
    }

    // 具体执行的逻辑
    @Override
    public void handler(HttpServletRequest req, HttpServletResponse res, HandlerMethod handler) throws Exception {
        final WebServletRequest webServletRequest = new WebServletRequest(req, res);
        // 需要将 HandlerMethod执行，创建一个类来包装
        final ServletInvocableMethod invocableMethod = new ServletInvocableMethod();
        invocableMethod.setHandlerMethod(handler);
        invocableMethod.setConvertComposite(convertComposite);
        invocableMethod.setResolverComposite(resolverComposite);
        invocableMethod.setReturnValueHandlerComposite(returnValueHandlerComposite);
        invocableMethod.invokeAndHandle(webServletRequest,handler);
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    // 初始化基础组件
    @Override
    public void afterPropertiesSet() throws Exception {
        resolverComposite.addResolvers(getDefaultArgumentResolver());
        convertComposite.addConvertMap(getDefaultConvert());
        returnValueHandlerComposite.addMethodReturnValueHandlers(getDefaultMethodReturnValueHandler());

        // 获取用户的全局类型转换器
        final Map<Class, ConvertHandler> diyConvertMap = getDiyConvertMap();
        convertComposite.addConvertMap(diyConvertMap);
    }

    public Map<Class, ConvertHandler> getDiyConvertMap(){
        final HashMap<Class, ConvertHandler> convertHandlerHashMap = new HashMap<>();
        final ApplicationContext context = obtainApplicationContext();
        final String[] names = BeanFactoryUtils.beanNamesForAnnotationIncludingAncestors(context,ControllerAdvice.class);
        for (String name : names) {
            final Class<?> type = context.getType(name);
            final Method[] methods = type.getMethods();
            for (Method method : methods) {
                if (AnnotatedElementUtils.hasAnnotation(method, ConvertType.class)){
                    final ConvertType convertType = AnnotatedElementUtils.findMergedAnnotation(method, ConvertType.class);
                    convertHandlerHashMap.put(convertType.value(),new ConvertHandler(context.getBean(name),method));

                }
            }
        }
        return convertHandlerHashMap;
    }

    //初始化返回值处理器
    public List<HandlerMethodReturnValueHandler> getDefaultMethodReturnValueHandler(){
        final ArrayList<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>();
        handlerMethodReturnValueHandlers.add(new RequestResponseBodyMethodReturnValueHandler());
        return handlerMethodReturnValueHandlers;

    }

    // 初始化类型转换器
    public Map<Class, ConvertHandler> getDefaultConvert(){
        final Map<Class, ConvertHandler> convertMap = new HashMap<>();
        convertMap.put(Integer.class,getConvertHandler(new IntegerConvert(Integer.class)));
        convertMap.put(int.class,getConvertHandler(new IntegerConvert(Integer.class)));
        convertMap.put(String.class,getConvertHandler(new StringConvert(String.class)));
        convertMap.put(Long.class,getConvertHandler(new LongConvert(Long.class)));
        convertMap.put(long.class,getConvertHandler(new LongConvert(Long.class)));
        convertMap.put(Float.class,getConvertHandler(new FloatConvert(Float.class)));
        convertMap.put(float.class,getConvertHandler(new FloatConvert(Float.class)));
        convertMap.put(Boolean.class,getConvertHandler(new BooleanConvert(Boolean.class)));
        convertMap.put(boolean.class,getConvertHandler(new BooleanConvert(Boolean.class)));
        convertMap.put(Byte.class,getConvertHandler(new ByteConvert(Byte.class)));
        convertMap.put(byte.class,getConvertHandler(new ByteConvert(Byte.class)));
        convertMap.put(Short.class,getConvertHandler(new ShortConvert(Short.class)));
        convertMap.put(short.class,getConvertHandler(new ShortConvert(Short.class)));
        convertMap.put(Date.class,getConvertHandler(new DateConvert(Date.class)));
        convertMap.put(Map.class,getConvertHandler(new MapConvert(HashMap.class)));
        convertMap.put(Collection.class,getConvertHandler(new CollectionConvert(Collection.class)));
        convertMap.put(List.class,getConvertHandler(new ListConvert(ArrayList.class)));
        convertMap.put(Set.class,getConvertHandler(new SetConvert(HashSet.class)));
        return convertMap;
    }


    protected ConvertHandler getConvertHandler(Convert convert){
        try {
            final Method method = convert.getClass().getDeclaredMethod("convert", Object.class);
            return new ConvertHandler(convert,method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    //初始化参数解析器
    public List<HandlerMethodArgumentResolver> getDefaultArgumentResolver(){
        final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        resolvers.add(new PathVariableMethodArgumentResolver());
        resolvers.add(new PathVariableMapMethodArgumentResolver());
        resolvers.add(new RequestCookieMethodArgumentResolver());
        resolvers.add(new RequestHeaderMethodArgumentResolver());
        resolvers.add(new RequestHeaderMapMethodArgumentResolver());
        resolvers.add(new RequestPartMethodArgumentResolver());
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new RequestParamMapMethodArgumentResolver());
        resolvers.add(new RequestRequestBodyMethodArgumentResolver());
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());
        return resolvers;
    }
}
