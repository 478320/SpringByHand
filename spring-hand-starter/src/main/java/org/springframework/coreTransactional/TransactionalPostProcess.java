package org.springframework.coreTransactional;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.annotation.Autowired;
import org.springframework.annotationAop.Aop;
import org.springframework.annotationTransactional.Transactional;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.coreAop.AopProxy;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/** （弃用！！）
 *
 */
//@Component("transactionalPostProcess")
public class TransactionalPostProcess implements BeanPostProcessor {

    @Autowired
    public TransactionalManager transactionalManager;

    @Override
    public String toString() {
        return "TransactionalPostProcess{" +
                "transactionalManager=" + transactionalManager +
                '}';
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return TransactionalProxyFactory.tryBuild(bean, transactionalManager);
    }

}

