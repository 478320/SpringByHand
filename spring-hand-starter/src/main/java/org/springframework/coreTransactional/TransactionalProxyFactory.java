package org.springframework.coreTransactional;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.annotationTransactional.Transactional;

import java.lang.reflect.Method;

/**
 * （弃用！！）
 */
public class TransactionalProxyFactory {

    public static <T> T get(Object target, TransactionalProxy transactionalProxy) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(transactionalProxy);
        return (T) enhancer.create();
    }

    public static Object tryBuild(Object targetObject, TransactionalManager transactionalManager) {
        boolean isRetAopProxy = false;
        TransactionalProxy transactionalProxy = new TransactionalProxy();
        transactionalProxy.transactionalObject = targetObject;
        transactionalProxy.transactionalManager = transactionalManager;
        Class<?> targetObjectClass = targetObject.getClass();
        // 如果是类级别
        if (targetObjectClass.isAnnotationPresent(Transactional.class)) {
            transactionalProxy.isInterceptorAll = true;
            isRetAopProxy = true;
        } else {
            //方法级别，遍历方法
            for (Method method : targetObjectClass.getMethods()) {
                if (method.isAnnotationPresent(Transactional.class)) {
                    isRetAopProxy = true;
                    transactionalProxy.interceptorMethods.put(method.hashCode(), method);
                }
            }
        }
        if (isRetAopProxy) {
            return get(targetObject, transactionalProxy);
        } else {
            return targetObject;
        }
    }
}
