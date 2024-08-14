package org.springframework.beans.factory.support;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.support.BeanDefinitionRegistry;
import org.springframework.core.support.PathMatchingResourcePatternResolver;
import org.springframework.core.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * 概念展示，抽象的资源读取器，负责获取到加载的资源同时给档案馆赋值
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    private BeanDefinitionRegistry registry;

    private ResourceLoader resourceLoader;


    @Override
    public final BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 获取到档案馆并进行一些处理
     * @param registry 档案馆实例
     */
    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        // 设置档案馆
        this.registry = registry;

        // 现在来看还不是一个ResourceLoader，现在是一个档案馆
        if (this.registry instanceof ResourceLoader) {
            this.resourceLoader = (ResourceLoader) this.registry;
        }
        else {
            // 给资源加载器赋值
            this.resourceLoader = new PathMatchingResourcePatternResolver();
        }

        // TODO 源码还涉及一些环境的内容不多做介绍
    }

    /**
     * 加载Bean定义信息的具体方法
     *
     * @param resources 资源
     */
    @Override
    public int loadBeanDefinitions(Resource... resources){
        int count = 0;
        for (Resource resource : resources) {
            // 调用子类的加载Bean定义信息
            count += loadBeanDefinitions(resource);
        }
        return count;
    }

    // 加载bean的定义信息，下面都是一些源码主要都是在加载Bean的定义信息
    @Override
    public int loadBeanDefinitions(String location){
        return loadBeanDefinitions(location, null);
    }

    @Override
    public int loadBeanDefinitions(String... locations){
        int count = 0;
        for (String location : locations) {
            count += loadBeanDefinitions(location);
        }
        return count;
    }

    public int loadBeanDefinitions(String location, Set<Resource> actualResources){
        ResourceLoader resourceLoader = getResourceLoader();
        if (resourceLoader == null) {
            System.out.println("AbstractBeanDefinitionReader的loadBeanDefinitions的resourceLoader为空");
            return 0;
        }

        if (resourceLoader instanceof ResourcePatternResolver) {
            // Resource pattern matching available.
            try {
                //利用Reader获取资源没有具体实现
                Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources(location);
                int count = loadBeanDefinitions(resources);
                if (actualResources != null) {
                    Collections.addAll(actualResources, resources);
                }
                return count;
            }
            catch (IOException ex) {
                ex.printStackTrace();
                return 0;
            }
        }
        else {
            // Can only load single resources by absolute URL.
            Resource resource = resourceLoader.getResource(location);
            int count = loadBeanDefinitions(resource);
            if (actualResources != null) {
                actualResources.add(resource);
            }

            return count;
        }
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
}
