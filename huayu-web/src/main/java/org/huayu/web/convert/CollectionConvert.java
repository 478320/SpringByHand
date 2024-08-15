package org.huayu.web.convert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Collection类型转换器
 */
public class CollectionConvert extends Convert<Collection>{

    public CollectionConvert(Class<Collection> type) {
        super(type);
    }

    @Override
    protected Object convert(Object arg) throws Exception {

        return ArrayList.class.getConstructor(Collection.class).newInstance(arg);
    }
}
