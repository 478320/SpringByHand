package org.springframework.beans.factory.config;

/**
 * Bean定义存储类
 */
public class BeanDefinitionHolder {

    private final BeanDefinition beanDefinition;

    private final String beanName;


    private final String[] aliases;

    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName) {
        this(beanDefinition, beanName, null);
    }

    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName,String[] aliases) {
        this.beanDefinition = beanDefinition;
        this.beanName = beanName;
        this.aliases = aliases;
    }
}
