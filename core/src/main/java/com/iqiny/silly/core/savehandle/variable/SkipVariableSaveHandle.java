/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.variable;

import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;

/**
 * 跳过保存处理器
 * 此变量不进行存储
 */
public class SkipVariableSaveHandle implements SillyVariableSaveHandle {

    public static final String NAME = "skip";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData, SillyVariable variable) {
        return false;
    }
}
