package org.springframework.beans.factory;

import org.springframework.beans.factory.config.BeanFactory;

public interface BeanFactoryAware extends Aware {


	void setBeanFactory(BeanFactory beanFactory);

}