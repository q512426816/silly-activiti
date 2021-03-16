package com.iqiny.silly.core.base;

/**
 * 可初始化的对象
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyInitializable {

    /**
     * 默认的类型
     */
    String DEFAULT_CATEGORY = "SILLY_DEFAULT_CATEGORY";

    /**
     * 初始化方法
     */
    void init();

    /**
     * 使用的类型
     */
    String usedCategory();

}