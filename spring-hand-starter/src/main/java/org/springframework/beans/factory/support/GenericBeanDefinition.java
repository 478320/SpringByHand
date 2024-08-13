package org.springframework.beans.factory.support;

/**
 *
 */
public class GenericBeanDefinition extends AbstractBeanDefinition{

    private String parentName;

    private Object source;


    public void setSource(Object source) {
        this.source = source;
    }

    public GenericBeanDefinition(Class<?> beanClass) {
        super(beanClass);
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String getBeanClassName() {
        return null;
    }
}
