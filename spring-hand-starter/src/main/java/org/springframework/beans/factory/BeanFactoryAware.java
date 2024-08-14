package org.springframework.beans.factory;

import org.springframework.beans.factory.config.BeanFactory;

/**
 * Bean工厂Aware回调接口
 */
public interface BeanFactoryAware extends Aware {


	void setBeanFactory(BeanFactory beanFactory);

}