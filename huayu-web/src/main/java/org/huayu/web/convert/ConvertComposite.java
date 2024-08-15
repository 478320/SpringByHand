package org.huayu.web.convert;

import org.huayu.web.excpetion.NotFoundExcpetion;
import org.huayu.web.handler.HandlerMethod;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类型转换器组合器
 */
public class ConvertComposite {


    private Map<Class,ConvertHandler> convertMap = new HashMap<>();

    public void addConvertMap(Map<Class, ConvertHandler> convertMap) {
        this.convertMap.putAll(convertMap);
    }

    public Object convert(HandlerMethod handlerMethod,Class<?> parameterType, Object result) throws Exception {
        // 先执行局部的，获取Controller中的类型转换器
        final Map<Class, ConvertHandler> convertHandlerMap = handlerMethod.getConvertHandlerMap();
        if (!ObjectUtils.isEmpty(convertHandlerMap)){
            final ConvertHandler convertHandler = convertHandlerMap.get(parameterType);
            if (convertHandler!=null){
                return convertHandler.convert(result);
            }
        }
        // 全局的，从初始化的类型转换器中拿
        if (convertMap.containsKey(parameterType)) {
            final ConvertHandler convertHandler = convertMap.get(parameterType);
            try {
                return convertHandler.convert(result);
            } catch (Exception e) {
                // 类型转换异常
                e.printStackTrace();
            }
        }
        // 没找到
        throw new NotFoundExcpetion(parameterType.getName() + "没有该参数类型的类型转换器");

    }
}
