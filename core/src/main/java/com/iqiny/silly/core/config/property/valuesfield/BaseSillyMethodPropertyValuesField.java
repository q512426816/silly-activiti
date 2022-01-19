/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.valuesfield;

import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 返回方法属性 方法必须使用 static 修饰
 */
public abstract class BaseSillyMethodPropertyValuesField implements SillyPropertyValuesField {

    @Override
    public Object value(SillyNodeSourceData data) {
        String methodName = getMethodName(data);
        Class<?>[] parameterTypes = getParameterTypes(data);
        return loadMethod(methodName, parameterTypes);
    }

    /**
     * 方法参数
     *
     * @param data
     * @return
     */
    protected abstract Class<?>[] getParameterTypes(SillyNodeSourceData data);

    /**
     * 方法名称 (默认与属性名称一致)
     *
     * @param data
     * @return
     */
    protected String getMethodName(SillyNodeSourceData data) {
        return fieldName();
    }


    protected Object loadMethod(String methodName, Class<?>[] parameterTypes) {
        try {
            return this.getClass().getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new SillyException("方法获取失败" + methodName + e.getMessage(), e);
        }
    }

}
