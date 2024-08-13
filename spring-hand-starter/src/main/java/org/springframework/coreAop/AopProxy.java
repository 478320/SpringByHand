package org.springframework.coreAop;

import org.springframework.annotationAop.After;
import org.springframework.annotationAop.Around;
import org.springframework.annotationAop.Before;
import org.springframework.annotationAop.Throwing;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.annotationTransactional.Transactional;
import org.springframework.coreTransactional.TransactionalManager;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AopProxy implements MethodInterceptor {

    TransactionalManager transactionalManager;

    Object targetObject;

    Object aopObject;
    
    Method beforeMethod;
    
    Method afterMethod;
    
    Method throwingMethod;
    
    Method aroundMethod;
    
    Map<Integer,Method> interceptorMethods = new HashMap<>();
    boolean isInterceptorAll = false;

    public AopProxy(){

    }
    public void setEnhancerMethods(Object aopObject) {
        for (Method method : aopObject.getClass().getMethods()) {
            if (method.isAnnotationPresent(Before.class)){
                beforeMethod = method;
            }else if (method.isAnnotationPresent(After.class)){
                afterMethod = method;
            }else if (method.isAnnotationPresent(Around.class)){
                aroundMethod = method;
            }else if (method.isAnnotationPresent(Throwing.class)){
                throwingMethod = method;
            }
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        final int hashCode = method.hashCode();
        //前后置拦截器入参分别为JoinPoint
        //环绕为 ProceedingJoinPoint 因为环绕有invoke行为
        if (isInterceptorAll || interceptorMethods.containsKey(hashCode)){
            final JoinPoint joinPoint = new JoinPoint(method,objects);
            Object ret = null;
            //执行顺序：前 -》环绕-》后-》异常
            try {
                if (method.isAnnotationPresent(Transactional.class)){
                    Connection connection = transactionalManager.getConnection();
                    connection.setAutoCommit(false);
                    try {
                        invokeMethod(joinPoint,beforeMethod);
                        if (aroundMethod != null){
                            ret = invokeMethod(new ProceedingJoinPoint(method,objects,targetObject,methodProxy),aroundMethod);
                        }else {
                            ret = methodProxy.invokeSuper(o,objects);
                        }
                        connection.commit();
                        System.out.println("注册提交");
                        return ret;
                    } catch (Exception e) {
                        e.printStackTrace();
                        invokeMethod(e,throwingMethod);
                        System.out.println("注册回滚");
                        connection.rollback();
                    }finally {
                        invokeMethod(joinPoint,afterMethod);
                        connection.close();
                    }
                }
                invokeMethod(joinPoint,beforeMethod);
                if (aroundMethod != null){
                    ret = invokeMethod(new ProceedingJoinPoint(method,objects,targetObject,methodProxy),aroundMethod);
                }else {
                    ret = methodProxy.invokeSuper(o,objects);
                }
                return ret;
            }catch (Throwable throwable){
                throwable.printStackTrace();
                invokeMethod(throwable,throwingMethod);
                return ret;
            }finally {
                invokeMethod(joinPoint,afterMethod);
            }
        }
        return methodProxy.invokeSuper(o,objects);
    }

    private Object invokeMethod(Object joinPoint, Method method) throws Throwable{
        Object ret = null;
        if (method!=null){
            //判断执行方法上面是否有目标参数
            if (method.getParameterTypes().length>0){
                if (!method.getParameterTypes()[0].equals(joinPoint.getClass())){
                    throw new IllegalArgumentException("参数映射错误：非 IJoinPoint参数");
                }
                ret = method.invoke(aopObject,joinPoint);
                return ret;
            }else {
                ret = method.invoke(aopObject);
                return ret;
            }
        }
        return null;
    }
    
    
    
    
}
