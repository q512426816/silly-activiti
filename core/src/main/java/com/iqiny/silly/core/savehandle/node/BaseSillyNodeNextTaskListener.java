/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

/**
 * 傻瓜 下一步任务 基础类
 */
public abstract class BaseSillyNodeNextTaskListener extends BaseSillyNodeSaveHandle {

    @Override
    public int order() {
        return SillyAfterCompleteSaveHandle.ORDER + AFTER;
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }


}
