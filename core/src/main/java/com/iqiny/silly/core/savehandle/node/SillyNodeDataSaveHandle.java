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
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.Date;
import java.util.List;

/**
 * 节点数据 生成及保存
 */
public class SillyNodeDataSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyProcessStartSaveHandle.ORDER + 100;

    public static final String NAME = "nodeData";

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
        return sourceData.getNode() == null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        Class<? extends SillyNode> nodeClass = sillyConfig.getSillyFactory().nodeClass();
        List<? extends SillyVariable> vs = sourceData.getVariables();
        SillyProcessNodeProperty<?> nodeProperty = sourceData.getNodeProperty();

        SillyNode n = doMakeObjByVariable(vs, SillyConstant.ActivitiVariable.BELONG_NODE, nodeClass);
        n.setMasterId(sourceData.masterId());
        n.setTaskId(sourceData.taskId());
        n.setNodeKey(nodeProperty.getNodeKey());
        n.setNodeName(nodeProperty.getNodeName());
        n.setParallelFlag(nodeProperty.isParallel() ? SillyConstant.YesOrNo.YES : SillyConstant.YesOrNo.NO);
        n.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        n.setNodeDate(new Date());
        n.setNodeUserId(sillyConfig.getSillyCurrentUserUtil().currentUserId());

        // 保存 节点表数据
        saveNodeInfo(n, sillyConfig.getSillyFactory(), sillyConfig.getSillyWriteService());

        // 设置节点表信息
        sourceData.setNode(n);
    }

    /**
     * 保存流程节点数据
     *
     * @param node
     * @return 工作流需要的数据 Map
     */
    protected void saveNodeInfo(SillyNode node, SillyFactory sillyFactory, SillyWriteService writeService) {
        if (node == null) {
            return;
        }
        // （若有）更新之前的流程信息 为历史状态
        updateToHistory(node, sillyFactory, writeService);
        doInsertNode(node, writeService);
    }

    private void updateToHistory(SillyNode node, SillyFactory sillyFactory, SillyWriteService writeService) {

        // （若有）更新之前的流程信息 为历史状态
        final SillyNode whereNode = sillyFactory.newNode();
        SillyAssert.notEmpty(node.getMasterId(), "node.masterId 不可为空");
        SillyAssert.notEmpty(node.getNodeKey(), "node.nodeKey 不可为空");
        whereNode.setMasterId(node.getMasterId());
        whereNode.setNodeKey(node.getNodeKey());

        boolean isParallel = SillyConstant.ActivitiParallel.IS_PARALLEL.equals(node.getParallelFlag());
        if (isParallel) {
            whereNode.setTaskId(node.getTaskId());
        } else {
            node.setParallelFlag(SillyConstant.ActivitiParallel.NOT_PARALLEL);
        }
        final SillyNode sillyNode = sillyFactory.newNode();
        sillyNode.setStatus(SillyConstant.ActivitiNode.STATUS_HISTORY);
        writeService.update(sillyNode, whereNode);
    }

    private void doInsertNode(SillyNode node, SillyWriteService writeService) {
        boolean flag = writeService.insert(node);
        if (!flag) {
            throw new SillyException("保存流程主表信息发生异常！");
        }
    }

}
