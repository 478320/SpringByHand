package org.springframework.coreTransactional;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.coreAop.JoinPoint;
import org.springframework.coreAop.ProceedingJoinPoint;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * （弃用！！）
 */
public class TransactionalProxy implements MethodInterceptor {

    Object transactionalObject;

    Map<Integer,Method> interceptorMethods = new HashMap<>();

    boolean isInterceptorAll = false;

    TransactionalManager transactionalManager;


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        final int hashCode = method.hashCode();
        if (isInterceptorAll || interceptorMethods.containsKey(hashCode)){
            Object ret = null;

            Connection connection = transactionalManager.getConnection();
            connection.setAutoCommit(false);
            try {
                ret = method.invoke(transactionalObject,objects);
                connection.commit();
                System.out.println("注册提交");
                return ret;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("注册回滚");
                connection.rollback();
            }finally {
                connection.close();
            }
        }
        return methodProxy.invokeSuper(o,objects);
    }

}
