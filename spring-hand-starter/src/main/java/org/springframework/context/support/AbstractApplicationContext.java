package org.springframework.context.support;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {




    @Override
    public void refresh() throws InvocationTargetException, IllegalAccessException {
        // Template method to refresh the context
        // TODO StartupStep contextRefresh = this.applicationStartup.start("spring.context.refresh");

        // Prepare this context for refreshing.
        // TODO prepareRefresh();

        // 我需要获得到一个档案馆 做什么？忘记了再说，总之我要获得一个,在这里所有bean的定义信息就有了
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // 完成bean工厂初始化
        // finishBeanFactoryInitialization(beanFactory);
    }

    @Override
    public Object getBean(String name) {
        // Retrieve bean by name
        return null;
    }

    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() throws InvocationTargetException, IllegalAccessException {
        //获得这个档案馆我需要加载档案馆里的资源，所以我需要刷新这个档案馆，加载后再把这个刷新好的档案馆返回
        try {
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
