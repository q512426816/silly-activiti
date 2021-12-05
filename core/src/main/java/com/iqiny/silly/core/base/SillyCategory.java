/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.base;

/**
 * 支持单个业务分类
 */
public interface SillyCategory {

    /**
     * 默认通用的类型
     */
    String DEFAULT_CATEGORY = "SILLY_DEFAULT_CATEGORY";
    
    /**
     * 正在使用的类型
     */
    String usedCategory();

}
