package org.springframework.coreAop;


import org.springframework.annotationAop.Aop;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.coreTransactional.TransactionalManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Aop策略工厂类（重要）
 */
public class AopProxyFactory {

    /**
     * cglib代理增强
     */
    public static <T> T get(Object target, AopProxy aop) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(aop);
        return (T) enhancer.create();
    }

    /**
     * 尝试创建Aop代理对象
     */
    public static Object tryBuild(Object targetObject, Object aopObject, TransactionalManager transactionalManager) {
        boolean isRetAopProxy = false;
        final AopProxy aopProxy = new AopProxy();
        final Aop aop = aopObject.getClass().getAnnotation(Aop.class);
        // 设置事务管理器
        aopProxy.transactionalManager = transactionalManager;
        aopProxy.targetObject = targetObject;
        aopProxy.aopObject = aopObject;
        // 设置aopObject中增强方法
        // 如果是以路径进行拦截，因路径最小单元为类级别，因此直接对整个类中的方法进行增强
        aopProxy.setEnhancerMethods(aopObject);
        // 判断是路径Aop还是注解Aop
        if (aop.jointPath() != null && !aop.jointPath().isEmpty()) {
            aopProxy.isInterceptorAll = true;
            isRetAopProxy = true;
        } else {
            final Class<? extends Annotation> aopAnnotation = aop.joinAnnotationClass();
            final Class<?> targetObjectClass = targetObject.getClass();
            // 如果是类级别
            if (targetObjectClass.isAnnotationPresent(aopAnnotation)) {
                aopProxy.isInterceptorAll = true;
                isRetAopProxy = true;
            } else {
                //方法级别，遍历方法
                for (Method method : targetObjectClass.getMethods()) {
                    if (method.isAnnotationPresent(aopAnnotation)) {
                        isRetAopProxy = true;
                        // 将需要Aop的方法交给策略处理
                        aopProxy.interceptorMethods.put(method.hashCode(), method);
                    }
                }
            }
        }
        if (isRetAopProxy) {
            return get(targetObject, aopProxy);
        } else {
            return targetObject;
        }
    }
}
