package org.springframework.context.support;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.io.IOException;

/**
 *
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{

    private volatile DefaultListableBeanFactory beanFactory;

    @Override
    protected final void refreshBeanFactory(){
        // TODO if判断另说
        //if (hasBeanFactory()) {
        //    destroyBeans();
        //    closeBeanFactory();
        //}
        try {
            DefaultListableBeanFactory beanFactory = createBeanFactory();

            //这三步目前不是我关注的内容所以暂时不做
            // TODO beanFactory.setSerializationId(getId());
            // TODO beanFactory.setApplicationStartup(getApplicationStartup());
            // TODO customizeBeanFactory(beanFactory);

            //加载Bean的定义信息，context也是一个资源加载器，不同的context应该由具体的实现类来决定资源的加载方式
            loadBeanDefinitions(beanFactory);
            this.beanFactory = beanFactory;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 这是刷新档案馆的步骤，我需要先得到一个档案馆来刷新，所以先创建，这里和源码不一样，源码会先去拿父容器的BeanFactory方便子容器查找
    // 但是我们写的简易Spring不考虑父子容器的情况下就直接创建一个档案馆了
    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
            throws IOException;

    /**
     * 将现在（已刷新）得到的档案馆返回，该方法的调用在刷新之后了
     */
    @Override
    public final ConfigurableListableBeanFactory getBeanFactory() {
        DefaultListableBeanFactory beanFactory = this.beanFactory;
        if (beanFactory == null) {
            throw new IllegalStateException("BeanFactory not initialized or already closed - " +
                    "call 'refresh' before accessing beans via the ApplicationContext");
        }
        return beanFactory;
    }
}
