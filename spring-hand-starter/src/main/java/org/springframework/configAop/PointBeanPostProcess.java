package org.springframework.configAop;

import org.springframework.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.coreTransactional.TransactionalManager;
import org.springframework.stereotype.Component;
import org.springframework.annotationAop.Aop;
import org.springframework.coreAop.AopProxyFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Component("pointBeanPostProcess")
public class PointBeanPostProcess implements BeanPostProcessor {

    @Autowired
    public TransactionalManager transactionalManager;

    Map<String,Object> jointPointPathMap;

    Map<Class,Object> jointPointAnnotationMap;


    public PointBeanPostProcess() {
        this.jointPointPathMap = new HashMap<>();
        this.jointPointAnnotationMap = new HashMap<>();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        final Class<?> beanClass = bean.getClass();
        final Aop aop = beanClass.getAnnotation(Aop.class);
        if(aop!=null){
            final String jointPath = aop.jointPath();
            if (!jointPath.equals("")){
                jointPointPathMap.put(jointPath,bean);
            }else {
                jointPointAnnotationMap.put(aop.joinAnnotationClass(),bean);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        final Class<?> beanClass = bean.getClass();
        final String path = beanClass.getName();
        for (String s : jointPointPathMap.keySet()) {
            if (path.startsWith(s)){
                return AopProxyFactory.tryBuild(bean,jointPointPathMap.get(s), transactionalManager);
            }
        }
        for (Object aopObject : jointPointAnnotationMap.values()) {
            return AopProxyFactory.tryBuild(bean, aopObject,transactionalManager);
        }
        return bean;
    }
}
