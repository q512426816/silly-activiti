/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.List;

/**
 * 内部方法 生成 SillyPropertyHandle
 */
public class SillyUpdateCachePropertyHandleRootSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyMasterUpdateSaveHandle.ORDER + 100;

    public static final String NAME = "updateCachePropertyHandleRoot";

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
        SillyCategoryConfig sillyConfig = SillyConfigUtil.getSillyConfig(sourceData.getCategory());
        String masterId = sourceData.masterId();
        return StringUtils.isNotEmpty(masterId) && sillyConfig.getSillyCache() != null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        String masterId = sourceData.masterId();
        updatePropertyHandleRoot(masterId, sillyConfig);
    }

    protected void updatePropertyHandleRoot(String masterId, SillyCategoryConfig sillyConfig) {
        Object propertyHandleRoot = getPropertyHandleRootDB(masterId, sillyConfig);
        updatePropertyHandleRootCache(masterId, propertyHandleRoot, sillyConfig);
    }

    protected Object getPropertyHandleRootDB(String masterId, SillyCategoryConfig sillyConfig) {
        List<? extends SillyVariable> nodeList = sillyConfig.getSillyReadService().getVariableList(masterId, null);
        return sillyConfig.getSillyReadService().variableList2Map(nodeList);
    }


    protected void updatePropertyHandleRootCache(String masterId, Object updateValue, SillyCategoryConfig sillyConfig) {
        if (updateValue == null) {
            return;
        }

        sillyConfig.getSillyCache().updatePropertyHandleRootCache(sillyConfig.usedCategory(), masterId, updateValue);
    }

}
