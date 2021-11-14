/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle;

import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;

/**
 * 保存处理器
 * 此变量进行存储
 */
public class SaveVariableSaveHandle implements SillyVariableSaveHandle {

    public static final String NAME = "save";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean handle(SillyMaster master, SillyNode node, SillyVariable variable) {
        return true;
    }
}
