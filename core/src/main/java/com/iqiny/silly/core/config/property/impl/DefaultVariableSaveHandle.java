/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.impl;

import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;

/**
 * 默认变量保存处置类 （不进行任何处置）
 */
public class DefaultVariableSaveHandle implements SillyVariableSaveHandle {

    public static final String NAME = "default";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData, SillyVariable variables) {
        return variables != null;
    }

}
