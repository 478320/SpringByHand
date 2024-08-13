package org.springframework.context;

import org.springframework.beans.factory.config.HierarchicalBeanFactory;
import org.springframework.beans.factory.config.ListableBeanFactory;

import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public interface ApplicationContext extends HierarchicalBeanFactory, ListableBeanFactory {

    void refresh() throws InvocationTargetException, IllegalAccessException;

}
