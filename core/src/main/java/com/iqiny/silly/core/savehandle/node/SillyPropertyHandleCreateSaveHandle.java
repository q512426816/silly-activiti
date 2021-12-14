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

import java.util.HashMap;
import java.util.Map;

/**
 * 内部方法 生成 SillyPropertyHandle
 */
public class SillyPropertyHandleCreateSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyLoadMasterByTaskSaveHandle.ORDER + 100;

    public static final String NAME = "propertyHandleCreate";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int order() {
        return ORDER;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.getPropertyHandle() == null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyPropertyHandle propertyHandle = newSillyPropertyHandle(sourceData.getMap(), sillyConfig);
        sourceData.setPropertyHandle(propertyHandle);
    }

    protected SillyPropertyHandle newSillyPropertyHandle(Map<String, Object> values, SillyCategoryConfig sillyConfig) {
        SillyPropertyHandle sillyPropertyHandle = sillyConfig.newSillyPropertyHandle();
        sillyPropertyHandle.setSillyContext(sillyConfig.getSillyContext());
        sillyPropertyHandle.setValues(new HashMap<>(values));
        return sillyPropertyHandle;
    }

}
