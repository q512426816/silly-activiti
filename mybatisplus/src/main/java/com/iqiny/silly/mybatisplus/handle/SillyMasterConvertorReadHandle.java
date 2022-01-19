/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.handle;

import com.iqiny.silly.core.base.core.SillyMaster;

import java.util.Map;

/**
 * 傻瓜主数据读取处置类
 */
public interface SillyMasterConvertorReadHandle {

    /**
     * 处理器名称
     */
    String name();

    /**
     * 处置执行具体内容
     *
     * @param master 单个主数据Map
     */
    Map<String, Object> handle(SillyMaster master);

}
