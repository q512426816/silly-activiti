/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.readhandle;

import java.util.Map;

/**
 * 傻瓜主数据读取处置类
 */
public interface SillyMasterReadHandle {

    /**
     * 处理器名称
     */
    String name();

    /**
     * 处置执行具体内容
     *
     * @param record 单个主数据Map
     */
    void handle(Map<String, Object> record);

}
