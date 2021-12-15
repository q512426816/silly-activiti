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
    public SillyNodeSaveHandle handle(SillyNodeSourceData sourceData) {
        String category = sourceData.getCategory();
        SillyCategoryConfig sillyConfig = SillyConfigUtil.getSillyConfig(category);
        boolean canHandleFlag = canHandle(sourceData);
        if (canHandleFlag) {
            saveHandle(sillyConfig, sourceData);
            sourceData.record(this);
        }
        return next(sillyConfig);
    }

    /**
     * 未执行过的 NAME  且 子类实现的判定通过
     */
    protected boolean canHandle(SillyNodeSourceData sourceData) {
        return !sourceData.executed(name()) && canDo(sourceData);
    }

    protected SillyNodeSaveHandle next(SillyCategoryConfig sillyConfig) {
        return sillyConfig.getSillyContext().getNextBean(this, sillyConfig.usedCategory(), SillyNodeSaveHandle.class);
    }

    protected abstract boolean canDo(SillyNodeSourceData sourceData);

    protected abstract void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData);

}
