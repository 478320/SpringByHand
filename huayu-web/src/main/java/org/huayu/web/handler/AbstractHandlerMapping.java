package org.huayu.web.handler;

import org.huayu.web.annotation.RequestMethod;
import org.huayu.web.excpetion.HttpRequestMethodNotSupport;
import org.huayu.web.intercpetor.HandlerInterceptor;
import org.huayu.web.intercpetor.MappedInterceptor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 */
public abstract class AbstractHandlerMapping extends ApplicationObjectSupport implements HandlerMapping, InitializingBean {

    protected int order;

    protected MapperRegister mapperRegister = new MapperRegister();

    private List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();

    public void addHandlerInterceptors(List<MappedInterceptor> handlerInterceptors) {
        this.handlerInterceptors.addAll(handlerInterceptors);
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        final HandlerMethod handlerMethod = getHandlerInternal(request);
        if(ObjectUtils.isEmpty(handlerMethod)) {return null;}
        final HandlerExecutionChain executionChain = new HandlerExecutionChain(handlerMethod);

        executionChain.setHandlerInterceptors(handlerInterceptors);

        return executionChain;
    }

    protected HandlerMethod lockUpPath(HttpServletRequest request) throws Exception {
        // 1.获取请求路径 请求类型
        HandlerMethod handlerMethod = null;
        final String requestPath = request.getRequestURI();
        final Map<String, Set<HandlerMethod>> fuzzyMatchingPath = mapperRegister.getFuzzyMatchingPath();
        final Map<String, Set<HandlerMethod>> accurateMatchingPath = mapperRegister.getAccurateMatchingPath();
        boolean flag = false;
        // 2.精确匹配中如果没有则说明在模糊匹配，需要遍历模糊匹配的Key来进行正则表达式的查找
        if (!accurateMatchingPath.containsKey(requestPath)) {
            //遍历模糊匹配
            Set<String> paths = fuzzyMatchingPath.keySet();
            // 对路径进行排序
            paths = paths.stream().sorted((o1, o2) -> -(o1.compareTo(o2))).collect(Collectors.toCollection(LinkedHashSet::new));
            for (String path : paths) {
                //能匹配成功则还需要匹配请求类型
                if (Pattern.compile(path).matcher(requestPath).matches()) {
                    flag = true;
                    Set<HandlerMethod> handlerMethods = fuzzyMatchingPath.get(path);
                    handlerMethod = getHandlerMethod(handlerMethods, request);
                    if (!ObjectUtils.isEmpty(handlerMethod)){
                        return handlerMethod;
                    }
                }
            }
        }

        // 3.精确匹配直接查找
        if (accurateMatchingPath.containsKey(requestPath)) {
            flag = true;
            handlerMethod = getHandlerMethod(accurateMatchingPath.get(requestPath),request);
            if (!ObjectUtils.isEmpty(handlerMethod)){
                return handlerMethod;
            }
        }
        if (flag){
            // 请求类型不匹配
            throw new HttpRequestMethodNotSupport(requestPath + "请求类型不匹配");
        }
        // 404
        return null;
    }

    protected HandlerMethod getHandlerMethod(Set<HandlerMethod> handlerMethods, HttpServletRequest request) throws Exception {
        final String requestPath = request.getRequestURI();
        final String requestMethod = request.getMethod();
        for (HandlerMethod handlerMethod : handlerMethods) {
            // RequestMapping 接受任意请求

            // GetMapping 接受get
            // DeleteMapping 接受delete
            for (RequestMethod method : handlerMethod.getRequestMethods()) {
                if (method.name().equals(requestMethod)) {
                    return handlerMethod;
                }
            }
        }
        return null;
    }

    protected abstract HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception;

    protected void setOrder(int order) {
        this.order = order;
    }


    @Override
    public int getOrder() {
        return 0;
    }

    //找到所有的HandlerMethod
    @Override
    public void afterPropertiesSet() throws Exception {
        initHandlerMethod();
    }

    private void initHandlerMethod() throws Exception {
        //获取所有bean
        final ApplicationContext context = obtainApplicationContext();
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context, Object.class);
        for (String name : names) {
            //拿到当前class
            Class type = null;
            try {
                type = context.getType(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (type != null && isHandler(type)) {
                detectHandlerMethod(name);
            }
        }
        //判断是否是一个handler -> 交给子类

        //找到bean当中的HandlerMethod -> 交给子类
    }

    protected abstract void detectHandlerMethod(String name) throws Exception;

    protected abstract boolean isHandler(Class type);

    protected void registerMappers(List<HandlerMethod> handlerMethods) throws Exception {
        for (HandlerMethod handlerMethod : handlerMethods) {
            mapperRegister.register(handlerMethod);
        }
    }

    protected void registerMapper(HandlerMethod handlerMethod) throws Exception {
        mapperRegister.register(handlerMethod);
    }


    // 保存HandlerMethod
    class MapperRegister {

        //精确路径,因为不同的路径请求的方式可能不一样，为了方便未来判断路径是否重复，选择使用Set集合报错比较好判断
        Map<String, Set<HandlerMethod>> accurateMatchingPath = new HashMap<>();

        //模糊匹配,TODO 需要倒序，否则出现多个正则表达式匹配的优先级未知，可能出现匹配错误的情况
        Map<String, Set<HandlerMethod>> fuzzyMatchingPath = new HashMap<>();

        public void register(HandlerMethod handlerMethod) throws Exception {
            // 获取请求路径
            String path = handlerMethod.getPath();
            if (path.contains("{") && path.contains("}")) {
                // 路径替换
                path = path.replaceAll("\\{\\w+\\}", "(\\\\w+)");
                // 存在，可能请求类型一样
                register(fuzzyMatchingPath, path, handlerMethod);
            } else {
                // 根据请求路径的不同分别保存HandlerMethod
                register(accurateMatchingPath, path, handlerMethod);
            }


        }

        private void register(Map<String, Set<HandlerMethod>> mapPath, String path, HandlerMethod handlerMethod) throws Exception {
            // 存在，可能请求类型一样 重复了
            if (mapPath.containsKey(path) && mapPath.get(path).contains(handlerMethod)) {
                throw new HttpRequestMethodNotSupport(Arrays.toString(handlerMethod.getRequestMethods()) + handlerMethod.getPath() + "HandlerMethod相同");
            }
            if (!mapPath.containsKey(path)) {
                mapPath.put(path, new HashSet<>());
            }
            mapPath.get(path).add(handlerMethod);
        }

        public Map<String, Set<HandlerMethod>> getAccurateMatchingPath() {
            return accurateMatchingPath;
        }

        public Map<String, Set<HandlerMethod>> getFuzzyMatchingPath() {
            return fuzzyMatchingPath;
        }
    }
}
