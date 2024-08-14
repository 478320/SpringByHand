package org.springframework.core;

/**
 * 概念展示，别名注册中心接口
 */
public interface AliasRegistry {

    void registerAlias(String name, String alias);

    void removeAlias(String alias);

    boolean isAlias(String name);

    String[] getAliases(String name);

}
