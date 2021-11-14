/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property;

/**
 * 表达式配置
 */
public interface SillyPropertyHandle {

    /**
     * 获取 ROOT
     */
    Object getRoot();

    /**
     * 设置ROOT
     */
    void setRoot(Object root);

    /**
     * 获取配置数据值
     */
    Object getValue(String expression);

    /**
     * 获取配置数据值
     */
    boolean getBooleanValue(String expression);

    /**
     * 获取配置数据值
     */
    default String getStringValue(String expression) {
        Object value = getValue(expression);
        return value == null ? null : value.toString();
    }

    /**
     * 设置配置内容体
     *
     * @param context
     */
    void setValues(Object context);

    Object getValues();

    void updateValue(String key, Object value);

}
