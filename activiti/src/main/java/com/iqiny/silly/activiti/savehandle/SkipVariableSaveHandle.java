/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti.savehandle;

import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
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
    public boolean handle(String category, SillyNode node, SillyVariable variable) {
        return false;
    }
}
