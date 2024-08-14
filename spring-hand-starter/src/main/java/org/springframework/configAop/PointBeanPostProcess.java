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
 * Aop的关键处理器
 */
@Component("pointBeanPostProcess")
public class PointBeanPostProcess implements BeanPostProcessor {

    /**
     * 事务管理器，用于获取数据库连接
     */
    @Autowired
    public TransactionalManager transactionalManager;

    Map<String,Object> jointPointPathMap;

    Map<Class,Object> jointPointAnnotationMap;


    public PointBeanPostProcess() {
        this.jointPointPathMap = new HashMap<>();
        this.jointPointAnnotationMap = new HashMap<>();
    }

    /**
     * 初始化操作，将所有的切面类存储起来
     */
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

    /**
     * 尝试进行Aop
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        final Class<?> beanClass = bean.getClass();
        final String path = beanClass.getName();
        // 路径匹配，进行Aop
        for (String s : jointPointPathMap.keySet()) {
            if (path.startsWith(s)){
                return AopProxyFactory.tryBuild(bean,jointPointPathMap.get(s), transactionalManager);
            }
        }
        // 含有Aop自定义的自动Aop注解，进行Aop
        for (Object aopObject : jointPointAnnotationMap.values()) {
            return AopProxyFactory.tryBuild(bean, aopObject,transactionalManager);
        }
        return bean;
    }
}
