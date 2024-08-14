package org.springframework.core.metrics;

/**
 * 概念展示，标记类接口
 */
public interface ApplicationStartup {


    ApplicationStartup DEFAULT = new DefaultApplicationStartup();

   
    StartupStep start(String name);
}
