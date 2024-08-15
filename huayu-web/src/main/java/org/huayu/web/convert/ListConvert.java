package org.huayu.web.convert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * List类型转换器
 */
public class ListConvert extends Convert<ArrayList>{

    public ListConvert(Class<ArrayList> type) {
        super(type);
    }

    @Override
    protected Object convert(Object arg) throws Exception {
        return this.type.getConstructor(Collection.class).newInstance(arg);

    }
}
