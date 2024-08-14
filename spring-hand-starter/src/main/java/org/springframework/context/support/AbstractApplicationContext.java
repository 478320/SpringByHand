package org.springframework.context.support;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.lang.reflect.InvocationTargetException;

/**
 * 抽象上下文，刷新方法具体逻辑在这里执行，非常关键
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {


    /**
     * 刷新容器
     */
    @Override
    public void refresh() throws InvocationTargetException, IllegalAccessException {

        //获取一个档案馆，由于我的档案馆实际上也成为了一个Ioc容器，所以获取的时候就注册了所有Bean，这里被我简化了
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // 完成bean工厂初始化

        // 在这里创建Bean
        // finishBeanFactoryInitialization(beanFactory);
    }

    @Override
    public Object getBean(String name) {
        // Retrieve bean by name
        return null;
    }

    /**
     * 获取一个刷新好的档案馆
     */
    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() throws InvocationTargetException, IllegalAccessException {
        //获得这个档案馆我需要加载档案馆里的资源，所以我需要刷新这个档案馆，加载后再把这个刷新好的档案馆返回
        try {
            // 刷新档案馆，简单化，交给子类实现
            refreshBeanFactory();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        return getBeanFactory();
    }

    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    protected abstract void refreshBeanFactory() throws IllegalStateException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException;


    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {


        // Allow for caching all bean definition metadata, not expecting further changes.
        beanFactory.freezeConfiguration();

        // 完成所有非懒加载bean的初始化
        beanFactory.preInstantiateSingletons();
    }
}
