package org.huayu.web.convert;

import java.util.Collection;
import java.util.HashSet;

/**
 * Set类型转换器
 */
public class SetConvert extends Convert<HashSet>{

    public SetConvert(Class<HashSet> type) {
        super(type);
    }

    @Override
    protected Object convert(Object arg) throws Exception {
        return this.type.getConstructor(Collection.class).newInstance(arg);
    }
}
