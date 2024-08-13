package org.springframework.context.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.core.support.BeanDefinitionRegistry;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    protected final DefaultListableBeanFactory beanFactory;

    public final DefaultListableBeanFactory getDefaultListableBeanFactory() {
        return this.beanFactory;
    }


    public GenericApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    //TODO 为什么一定要构造方法
    public GenericApplicationContext(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerAlias(String name, String alias) {

    }

    @Override
    public void removeAlias(String alias) {

    }

    @Override
    public boolean isAlias(String name) {
        return false;
    }

    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {

    }

    @Override
    public void removeBeanDefinition(String beanName) {

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return null;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
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
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return null;
    }

    @Override
    public ApplicationStartup getApplicationStartup() {
        return null;
    }

    @Override
    protected void refreshBeanFactory() throws IllegalStateException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

    }


}
