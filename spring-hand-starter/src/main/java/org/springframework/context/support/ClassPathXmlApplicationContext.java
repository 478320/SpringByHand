package org.springframework.context.support;

import org.springframework.core.metrics.ApplicationStartup;

import java.lang.reflect.InvocationTargetException;

/**
 * 概念展示，Xml上下文
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    // ----------------------------------------------------------------------------------------------------------
    // 飞书文档中说的重点标注关键开始就是下面的构造方法
    // ----------------------------------------------------------------------------------------------------------

    public ClassPathXmlApplicationContext(String configLocation) throws InvocationTargetException, IllegalAccessException {
        this(new String[] {configLocation}, true);
    }
    public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh) throws InvocationTargetException, IllegalAccessException {
        setConfigLocations(configLocations);
        if (refresh) {
            // 调用刷新方法
            refresh();
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return new String[0];
    }

    @Override
    public int getBeanDefinitionCount() {
        return 0;
    }

    @Override
    public void setBeanName(String name) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public ApplicationStartup getApplicationStartup() {
        return null;
    }
}
