package org.springframework.coreTransactional;

import org.springframework.annotation.Autowired;
import org.springframework.annotationAop.Around;
import org.springframework.annotationTransactional.Transactional;
import org.springframework.coreAop.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 *
 */

@org.springframework.annotationAop.Aop(joinAnnotationClass = Transactional.class)
@Component("transactionalAop")
public class TransactionalAop {

    @Autowired
    public TransactionalManager transactionalManager;

    @Around
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Connection connection = transactionalManager.getConnection();
        connection.setAutoCommit(false);
        try {
            Object invoke = joinPoint.invoke();
            connection.commit();
            System.out.println("注册提交");
            return invoke;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("注册回滚");
            connection.rollback();
        }finally {
            connection.close();
        }
        return null;
    }
}
