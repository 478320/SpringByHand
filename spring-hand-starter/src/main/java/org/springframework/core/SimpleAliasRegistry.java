package org.springframework.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class SimpleAliasRegistry implements AliasRegistry{

    private final Map<String, String> aliasMap = new ConcurrentHashMap<>();

    @Override
    public void registerAlias(String name, String alias) {
        aliasMap.put(alias, name);
    }

    @Override
    public void removeAlias(String alias) {
        aliasMap.remove(alias);
    }

    @Override
    public boolean isAlias(String name) {
        return aliasMap.containsKey(name);
    }

    @Override
    public String[] getAliases(String name) {
        return aliasMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(name))
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }
}
