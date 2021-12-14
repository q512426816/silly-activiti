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
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.List;

/**
 * 内部方法 设置 SillyPropertyHandle root 对象
 */
public class SillyPropertyHandleSetRootSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyPropertyHandleCreateSaveHandle.ORDER + 100;

    public static final String NAME = "propertyHandleSetRoot";

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
        SillyPropertyHandle propertyHandle = sourceData.getPropertyHandle();
        String masterId = sourceData.masterId();
        return StringUtils.isNotEmpty(masterId) && propertyHandle != null && propertyHandle.getRoot() == null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyPropertyHandle propertyHandle = sourceData.getPropertyHandle();
        String masterId = sourceData.masterId();
        Object root = getPropertyHandleRoot(masterId, sillyConfig);
        propertyHandle.setRoot(root);
    }

    protected Object getPropertyHandleRoot(String masterId, SillyCategoryConfig sillyConfig) {
        Object root = getPropertyHandleRootCache(masterId, sillyConfig);
        if (root == null) {
            root = getPropertyHandleRootDB(masterId, sillyConfig);
        }
        return root;
    }

    protected Object getPropertyHandleRootCache(String masterId, SillyCategoryConfig sillyConfig) {
        return sillyConfig.getSillyCache().getPropertyHandleRootCache(sillyConfig.usedCategory(), masterId);
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
