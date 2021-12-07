/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.core.base.SillyProperties;

import java.util.List;

/**
 * 傻瓜配置 资源内容配置
 */
public interface SillyConfigContent {

    /**
     * 初始化
     */
    void init();

    /**
     * 刷新配置
     */
    void refresh();

    /**
     * 将资源内容转换为配置对象
     */
    List<SillyCategoryConfig> convertSillyConfig();


}
