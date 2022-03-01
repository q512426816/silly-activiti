/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.savehandle.SillyNodeSaveHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 傻瓜节点保存处理 基础类
 */
@SuppressWarnings("all")
public abstract class BaseSillyNodeSaveHandle implements SillyNodeSaveHandle {

    @Override
    public void handle(SillyNodeSourceData sourceData) {
        String category = sourceData.getCategory();
        SillyCategoryConfig sillyConfig = SillyConfigUtil.getSillyConfig(category);
        boolean canHandleFlag = canHandle(sourceData);
        if (canHandleFlag) {
            doHandle(sillyConfig, sourceData);
        }
    }

    protected void doHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        handle(sillyConfig, sourceData);
        sourceData.record(this);
    }

    /**
     * 未执行过的 NAME  且 子类实现的判定通过
     */
    protected boolean canHandle(SillyNodeSourceData sourceData) {
        return !sourceData.ignoreHandle(name()) && canDo(sourceData);
    }

    protected abstract boolean canDo(SillyNodeSourceData sourceData);

    protected abstract void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData);

}
