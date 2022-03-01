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
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 内部方法 生成 SillyPropertyHandle
 */
public class SillyPropertyHandleCreateSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_05_propertyHandleCreate";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.getPropertyHandle() == null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyPropertyHandle propertyHandle = sillyConfig.newSillyPropertyHandle(sourceData);
        sourceData.setPropertyHandle(propertyHandle);
    }

}
