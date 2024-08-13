package org.springframework.beans.factory.support;


import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.core.SimpleAliasRegistry;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 真正的ioc容器
 */
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {

    // 缓存所有单实力对象，也就是单例对象池
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    public Map<String, Object> getSingletonObjects() {
        return singletonObjects;
    }

    public void putSingletonObjects(String beanName,Object bean){
        if (singletonObjects.containsKey(beanName)){
            return;
        }
        singletonObjects.put(beanName,bean);
    }

    public Object getSingleton(String beanName) {
        return null;
    }
}
