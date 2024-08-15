package org.huayu.web;

import org.huayu.web.context.AbstractRefreshableWebApplicationContext;
import org.huayu.web.context.WebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * HttpServlet抽象接口
 */
public abstract class BaseHttpServlet extends HttpServlet {


    protected ApplicationContext webApplicationContext;

    public BaseHttpServlet(ApplicationContext webApplicationContext){
        this.webApplicationContext = webApplicationContext;
    }

    /**
     * webioc初始化以及配置，该方法会在实体类被创建后自动调用
     */
    @Override
    public void init() throws ServletException {
        //父子容器
        final ServletContext servletContext = getServletContext();

        ApplicationContext rootContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_NAME);


        if (!ObjectUtils.isEmpty(webApplicationContext)){
            if (!(this.webApplicationContext instanceof AnnotationConfigApplicationContext)){
                //需要转化
                AbstractRefreshableWebApplicationContext wac = (AbstractRefreshableWebApplicationContext) this.webApplicationContext;
                //设置父子容器
                if (wac.getParent() == null){
                    wac.setParent(rootContext);
                }
                //配置上下文
                wac.setServletContext(servletContext);
                wac.setServletConfig(getServletConfig());
                //容器刷新
                wac.refresh();
            }
        }
        // 我们定义的拓展点，我们可以在这里新增DispatcherServlet的初始化过程
        onRefresh(webApplicationContext);
    }

    /**
     * 自定义的DispatcherServlet拓展点，用于初始化组件
     */
    protected abstract void onRefresh(ApplicationContext webApplicationContext);
}
