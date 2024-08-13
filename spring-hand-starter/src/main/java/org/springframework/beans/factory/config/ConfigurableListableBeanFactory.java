package org.springframework.beans.factory.config;

/**
 * 该接口可获取bean名称，可配置，可自动注入，外还可以
 *
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory,AutowireCapableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName);
    void preInstantiateSingletons();
    void freezeConfiguration();
    boolean isConfigurationFrozen();

}
