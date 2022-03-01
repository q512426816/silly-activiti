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
import com.iqiny.silly.core.service.SillyReadService;

import java.util.List;
import java.util.Map;

/**
 * 内部方法 更新ROOT
 */
public class SillyUpdateCachePropertyHandleRootSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_29_updateCachePropertyHandleRoot";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        SillyCategoryConfig sillyConfig = SillyConfigUtil.getSillyConfig(sourceData.getCategory());
        String masterId = sourceData.masterId();
        return StringUtils.isNotEmpty(masterId) && sillyConfig.getSillyCache() != null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        String masterId = sourceData.masterId();
        updatePropertyHandleRoot(masterId, sillyConfig);
    }

    protected void updatePropertyHandleRoot(String masterId, SillyCategoryConfig sillyConfig) {
        Map<String, Object> propertyHandleRoot = getPropertyHandleRootDB(masterId, sillyConfig);
        updatePropertyHandleRootCache(masterId, propertyHandleRoot, sillyConfig);
    }

    protected Map<String, Object> getPropertyHandleRootDB(String masterId, SillyCategoryConfig sillyConfig) {
        SillyReadService sillyReadService = sillyConfig.getSillyReadService();
        List<? extends SillyVariable> nodeList = sillyReadService.getVariableList(masterId, null);
        return sillyReadService.variableList2Map(nodeList);
    }


    protected void updatePropertyHandleRootCache(String masterId, Map<String, Object> updateValue, SillyCategoryConfig sillyConfig) {
        if (updateValue == null) {
            return;
        }
        sillyConfig.getSillyCache().updatePropertyHandleRootCache(sillyConfig.usedCategory(), masterId, updateValue);
    }

}
