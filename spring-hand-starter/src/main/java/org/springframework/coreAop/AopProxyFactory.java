package org.springframework.coreAop;


import org.springframework.annotationAop.Aop;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.coreTransactional.TransactionalManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 *
 */
public class AopProxyFactory {

    public static <T> T get(Object target,AopProxy aop){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(aop);
        return (T) enhancer.create();
    }

    public static Object tryBuild(Object targetObject, Object aopObject, TransactionalManager transactionalManager) {
        boolean isRetAopProxy = false;
        final AopProxy aopProxy = new AopProxy();
        final Aop aop = aopObject.getClass().getAnnotation(Aop.class);
        aopProxy.transactionalManager = transactionalManager;
        aopProxy.targetObject = targetObject;
        aopProxy.aopObject = aopObject;
        //设置aopObject中增强方法
        //如果是以路径进行拦截，因路径最小单元为类级别，因此直接对整个类中的方法进行增强
        aopProxy.setEnhancerMethods(aopObject);
        // TODO 这里有点问题这里只要我们没有写Path，不论是否含有对应的注解由于isInterceptorAll = true;所有方法都会被增强
        if (aop.jointPath()!=null && !aop.jointPath().isEmpty()){
            aopProxy.isInterceptorAll = true;
            isRetAopProxy = true;
        }else {
            final Class<? extends Annotation> aopAnnotation = aop.joinAnnotationClass();
            final Class<?> targetObjectClass = targetObject.getClass();
            // 如果是类级别
            if (targetObjectClass.isAnnotationPresent(aopAnnotation)){
                aopProxy.isInterceptorAll = true;
                isRetAopProxy = true;
            }else {
                //方法级别，遍历方法
                for (Method method : targetObjectClass.getMethods()) {
                    if (method.isAnnotationPresent(aopAnnotation)){
                        isRetAopProxy = true;
                        aopProxy.interceptorMethods.put(method.hashCode(),method);
                    }
                }
            }
        }
        if (isRetAopProxy){
            return get(targetObject,aopProxy);
        }else {
            return targetObject;
        }
    }
}
