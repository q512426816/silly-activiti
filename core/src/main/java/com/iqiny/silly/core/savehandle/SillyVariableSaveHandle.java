/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle;

import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;


/**
 * 傻瓜变量数据保存处置类
 */
public interface SillyVariableSaveHandle {

    /**
     * 保存处置类的名称 （全局唯一）
     */
    String name();

    /**
     * 处置执行具体内容
     * @param variables 当前保存数据集合
     * @return 是否保存
     */
    boolean handle(SillyCategoryConfig sillyConfig,  SillyNodeSourceData sourceData, SillyVariable variables);

}
