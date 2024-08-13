package org.huayu.web;

import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 */
@HandlesTypes(WebApplicationInitializer.class)
public class WebServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> webApplications, ServletContext ctx) throws ServletException {
        if (webApplications!=null){
            final ArrayList<WebApplicationInitializer> initializers = new ArrayList<>(webApplications.size());

            for (Class<?> webApplication : webApplications) {
                if (!webApplication.isInterface() && !Modifier.isAbstract(webApplication.getModifiers())
                        && WebApplicationInitializer.class.isAssignableFrom(webApplication)){
                    try {
                        initializers.add((WebApplicationInitializer) ReflectionUtils.accessibleConstructor(webApplication).newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            //调用onStart方法
            if(!ObjectUtils.isEmpty(initializers)){
                for (WebApplicationInitializer initializer : initializers) {
                    initializer.onStartUp(ctx);
                }
            }
        }
    }
}
