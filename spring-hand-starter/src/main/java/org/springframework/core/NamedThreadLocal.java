package org.springframework.core;

/**
 *
 */
public class NamedThreadLocal<T> extends ThreadLocal<T> {

    private final String name;


    /**
     * Create a new NamedThreadLocal with the given name.
     * @param name a descriptive name for this ThreadLocal
     */
    public NamedThreadLocal(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
