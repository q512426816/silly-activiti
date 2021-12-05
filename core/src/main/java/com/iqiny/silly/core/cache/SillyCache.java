/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.cache;

/**
 * 傻瓜缓存对象
 */
public interface SillyCache {

    /**
     * 获取值
     */
    <T> T getValue(String category, Object key);

    /**
     * 设置缓存值
     */
    <T> T setValue(String category, Object key, Object value);

    /**
     * 更新参数处理器ROOT对象缓存
     */
    void updatePropertyHandleRootCache(String usedCategory, String masterId, Object updateValue);

    /**
     * 获取参数处理器ROOT对象缓存
     */
    <T> T getPropertyHandleRootCache(String usedCategory, String masterId);

}
