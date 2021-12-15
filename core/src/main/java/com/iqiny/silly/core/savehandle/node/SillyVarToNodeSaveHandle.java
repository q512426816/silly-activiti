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
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.List;

/**
 * 节点数据 生成及保存
 */
public class SillyVarToNodeSaveHandle<T extends SillyNode> extends BaseSillyNodeBelongSaveHandle<T> {

    public static final int ORDER = SillyProcessStartSaveHandle.ORDER + 100;

    public static final String NAME = "varToNode";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int order() {
        return ORDER;
    }

    @Override
    protected String belongName(SillyNodeSourceData sourceData) {
        return SillyConstant.ActivitiVariable.BELONG_NODE;
    }

    @Override
    protected Class<T> belongClass(SillyNodeSourceData sourceData) {
        String category = sourceData.getCategory();
        return SillyConfigUtil.getSillyConfig(category).getSillyFactory().nodeClass();
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {

        SillyNode node = sourceData.getNode();
        final List<? extends SillyVariable> variableList = node.getVariableList();
        node.setVariableList(null);
        SillyNode n = doMakeObjByVariable(sourceData, node);
        n.setMasterId(sourceData.masterId());
        n.setTaskId(sourceData.taskId());
        n.setVariableList(variableList);
        // 设置节点表信息
        sourceData.setNode(n);
    }

}
