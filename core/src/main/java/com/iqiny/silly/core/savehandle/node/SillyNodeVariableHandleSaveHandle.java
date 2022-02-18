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
        SillyNode node = sourceData.getNode();
        List<SillyVariable> vs = node.getVariableList();
        List<SillyVariable> needSaveList = new ArrayList<>();
        for (SillyVariable v : vs) {
            boolean needSaveFlag = batchSaveHandle(sillyConfig, sourceData, v);
            if (needSaveFlag) {
                needSaveList.add(v);
            }
        }
        node.setVariableList(needSaveList);
    }


    /**
     * 批量保存处置类处理
     */
    protected boolean batchSaveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData, SillyVariable variables) {
        SillyPropertyHandle propertyHandle = sourceData.getPropertyHandle();
        Map<String, SillyVariableSaveHandle> handleMap = sillyConfig.getSillyVariableSaveHandleMap();

        String saveHandleNames = variables.getSaveHandleName();
        SillyAssert.notEmpty(saveHandleNames, "批处理数据保存不可为空");
        String[] saveHandleNameArr = StringUtils.split(saveHandleNames, SillyConstant.ARRAY_SPLIT_STR);
        boolean lastFlag = true;
        for (String saveHandleName : saveHandleNameArr) {
            String stringValue = propertyHandle.getStringValue(saveHandleName.trim());
            if (StringUtils.isNotEmpty(stringValue)) {
                SillyVariableSaveHandle saveHandle = handleMap.get(stringValue);
                SillyAssert.notNull(saveHandle, "此saveHandle未定义" + stringValue);
                lastFlag = saveHandle.handle(sillyConfig, sourceData, variables);
            }
        }
        return lastFlag;
    }

}
