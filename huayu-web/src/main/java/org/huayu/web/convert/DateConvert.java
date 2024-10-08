package org.huayu.web.convert;

import java.util.Date;

/**
 * Date类型转换器
 */
public class DateConvert extends Convert<Date>{


    public DateConvert(Class<Date> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
