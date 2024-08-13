package org.springframework.context.support;

import org.springframework.core.metrics.ApplicationStartup;

/**
 *
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {


    public ClassPathXmlApplicationContext(String configLocation){
        this(new String[] {configLocation}, true);
    }
    public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh) {
        setConfigLocations(configLocations);
        if (refresh) {
            refresh();
        }
    }

    //关键方法，刷新容器
    @Override
    public void refresh() {
        // Load bean definitions from the XML configuration file
        // and initialize the container
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
