package org.springframework.context.support;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * 抽象的Xml上下文
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableConfigApplicationContext{

    private String[] configLocations;

    /**
     * 现在已经是一个xml容器了，可以确定加载方式
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws IOException {
        // 通过一个Xml的BeanDefinitionReader,通过一系列操作来加载DefaultListableBeanFactory，由于设计到忘记叫啥名了，是一个Xml解析器
        // 所以这里不是我们的重点，我将会演示设计理念，关于xml的读取只做概念，不做真正的实现
        //   DefaultListableBeanFactory的追踪如下，档案馆会先被XmlBeanDefinitionReader存储，然后放入到reader的上下文中，最后将解析器解析
        //的结果放入到从上下文中拿到的DefaultListableBeanFactory中，以此来完成档案馆的加载也就是刷新
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        // 该方法在抽象类中，每一个Reader都需要有一个loader来加载资源
        beanDefinitionReader.setResourceLoader(this);
        // 这是xml读取器特有的属性，我们暂时定义一下，因为我们不做具体的实现，所以里面什么都没有
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

        //初始化Reader就先跳过了
        //TODO initBeanDefinitionReader(beanDefinitionReader);
        loadBeanDefinitions(beanDefinitionReader);
    }


    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws IOException {
        Resource[] configResources = getConfigResources();
        if (configResources != null) {
            reader.loadBeanDefinitions(configResources);
        }
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            reader.loadBeanDefinitions(configLocations);
        }
    }

    protected Resource[] getConfigResources() {
        return null;
    }


    protected String[] getConfigLocations() {

        return (this.configLocations != null ? this.configLocations : getDefaultConfigLocations());
    }

    protected String[] getDefaultConfigLocations() {
        return null;
    }
}
