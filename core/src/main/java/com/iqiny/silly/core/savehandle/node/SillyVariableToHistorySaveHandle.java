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
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyWriteService;

/**
 * 节点变量 转历史数据处理
 */
public class SillyVariableToHistorySaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyNodeVariableHandleSaveHandle.ORDER + 100;

    public static final String NAME = "variableToHistory";

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
        return sourceData.getNode() != null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyNode node = sourceData.getNode();
        // 保存 节点变量表数据
        updateToHistory(node, sillyConfig.getSillyFactory(), sillyConfig.getSillyWriteService());
    }

    protected void updateToHistory(SillyNode node, SillyFactory sillyFactory, SillyWriteService writeService) {

        // （若有）更新之前的流程变量信息 为历史状态
        final SillyVariable whereVariable = sillyFactory.newVariable();
        whereVariable.setMasterId(node.getMasterId());
        whereVariable.setNodeKey(node.getNodeKey());

        boolean isParallel = SillyConstant.ActivitiParallel.IS_PARALLEL.equals(node.getParallelFlag());
        if (isParallel) {
            whereVariable.setTaskId(node.getTaskId());
        }
        SillyVariable sillyVariable = sillyFactory.newVariable();
        sillyVariable.setMasterId(node.getMasterId());
        sillyVariable.setNodeKey(node.getNodeKey());
        sillyVariable.setTaskId(node.getTaskId());
        sillyVariable.setStatus(SillyConstant.ActivitiNode.STATUS_HISTORY);
        writeService.update(sillyVariable, whereVariable);
    }

}
