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
 * Aop策略类（重要）
 */
public class AopProxy implements MethodInterceptor {

    // 事务管理器，用于处理事务这类特殊的Aop
    TransactionalManager transactionalManager;

    // 需要进行Aop的对象
    Object targetObject;

    // Aop的对象
    Object aopObject;

    Method beforeMethod;

    Method afterMethod;

    Method throwingMethod;

    Method aroundMethod;

    // 需要进行Aop的方法有哪些
    Map<Integer, Method> interceptorMethods = new HashMap<>();
    boolean isInterceptorAll = false;

    public AopProxy() {

    }

    /**
     * 通过Aop类来获取到Aop的增强逻辑
     *
     * @param aopObject Aop类
     */
    public void setEnhancerMethods(Object aopObject) {
        for (Method method : aopObject.getClass().getMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethod = method;
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethod = method;
            } else if (method.isAnnotationPresent(Around.class)) {
                aroundMethod = method;
            } else if (method.isAnnotationPresent(Throwing.class)) {
                throwingMethod = method;
            }
        }
    }

    /**
     * cglib代理逻辑
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        final int hashCode = method.hashCode();
        // 判断是否需要Aop
        if (isInterceptorAll || interceptorMethods.containsKey(hashCode)) {
            final JoinPoint joinPoint = new JoinPoint(method, objects);
            Object ret = null;
            //执行顺序：前 -》环绕-》后-》异常
            try {
                // 判断方法是否含有事务注解来决定是否使用事务
                if (method.isAnnotationPresent(Transactional.class)) {
                    // 下面是事务执行具体逻辑 事务+Aop
                    Connection connection = transactionalManager.getConnection();
                    connection.setAutoCommit(false);
                    try {
                        invokeMethod(joinPoint, beforeMethod);
                        if (aroundMethod != null) {
                            ret = invokeMethod(new ProceedingJoinPoint(method, objects, targetObject, methodProxy), aroundMethod);
                        } else {
                            ret = methodProxy.invokeSuper(o, objects);
                        }
                        connection.commit();
                        System.out.println("注册提交");
                        return ret;
                    } catch (Exception e) {
                        e.printStackTrace();
                        invokeMethod(e, throwingMethod);
                        System.out.println("注册回滚");
                        connection.rollback();
                    } finally {
                        invokeMethod(joinPoint, afterMethod);
                        connection.close();
                    }
                }
                // 如果不需要事务则直接使用Aop进行代理
                invokeMethod(joinPoint, beforeMethod);
                if (aroundMethod != null) {
                    ret = invokeMethod(new ProceedingJoinPoint(method, objects, targetObject, methodProxy), aroundMethod);
                } else {
                    ret = methodProxy.invokeSuper(o, objects);
                }
                return ret;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                invokeMethod(throwable, throwingMethod);
                return ret;
            } finally {
                invokeMethod(joinPoint, afterMethod);
            }
        }
        return methodProxy.invokeSuper(o, objects);
    }

    /**
     * 执行方法具体逻辑，用来给Around方法添加joinPoint参数
     */
    private Object invokeMethod(Object joinPoint, Method method) throws Throwable {
        Object ret = null;
        if (method != null) {
            //判断执行方法上面是否有目标参数
            if (method.getParameterTypes().length > 0) {
                if (!method.getParameterTypes()[0].equals(joinPoint.getClass())) {
                    throw new IllegalArgumentException("参数映射错误：非 IJoinPoint参数");
                }
                // 反射添加参数
                ret = method.invoke(aopObject, joinPoint);
                return ret;
            } else {
                ret = method.invoke(aopObject);
                return ret;
            }
        }
        return null;
    }


}
