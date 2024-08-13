package org.springframework.context.annotation;

import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanFactory;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;

/**
 *
 */
public class GenericTypeAwareAutowireCandidateResolver extends SimpleAutowireCandidateResolver implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
