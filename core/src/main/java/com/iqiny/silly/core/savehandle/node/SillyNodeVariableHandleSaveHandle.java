/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 节点变量 执行变量 saveHandle 并设置 node 的 variables 数据
 */
public class SillyNodeVariableHandleSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyNodeInsertSaveHandle.ORDER + 100;

    public static final String NAME = "silly_19_nodeVariableHandle";

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
        SillyNode node = sourceData.getNode();
        return node != null && node.getVariableList() != null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {

        SillyMaster master = sourceData.getMaster();
        SillyNode node = sourceData.getNode();
        SillyPropertyHandle propertyHandle = sourceData.getPropertyHandle();

        Map<String, SillyVariableSaveHandle> handleMap = sillyConfig.getSillyVariableSaveHandleMap();
        List<? extends SillyVariable> sillyVariables = variableSaveHandle(master, node, propertyHandle, handleMap);
        node.setVariableList(sillyVariables);
    }

    protected <V extends SillyVariable> List<? extends SillyVariable> variableSaveHandle(
            SillyMaster master, SillyNode node, SillyPropertyHandle propertyHandle,
            Map<String, SillyVariableSaveHandle> handleMap) {

        List<V> vs = node.getVariableList();
        List<V> needSaveList = new ArrayList<>();
        for (V v : vs) {
            boolean needSaveFlag = batchSaveHandle(master, node, v, propertyHandle, handleMap);
            if (needSaveFlag) {
                needSaveList.add(v);
            }
        }
        return needSaveList;
    }


    /**
     * 批量保存处置类处理
     *
     * @param node
     * @param variables
     * @return
     */
    protected boolean batchSaveHandle(SillyMaster master, SillyNode node, SillyVariable variables
            , SillyPropertyHandle propertyHandle, Map<String, SillyVariableSaveHandle> handleMap) {
        String saveHandleNames = variables.getSaveHandleName();
        SillyAssert.notEmpty(saveHandleNames, "批处理数据保存不可为空");
        String[] saveHandleNameArr = StringUtils.split(saveHandleNames, SillyConstant.ARRAY_SPLIT_STR);
        boolean lastFlag = true;
        for (String saveHandleName : saveHandleNameArr) {
            String stringValue = propertyHandle.getStringValue(saveHandleName.trim());
            if (StringUtils.isNotEmpty(stringValue)) {
                SillyVariableSaveHandle saveHandle = handleMap.get(stringValue);
                SillyAssert.notNull(saveHandle, "此saveHandle未定义" + stringValue);
                lastFlag = saveHandle.handle(master, node, variables);
            }
        }
        return lastFlag;
    }

}