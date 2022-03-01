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
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.Date;

/**
 * 节点数据 对象设置
 */
public class SillyLoadNodeInfoSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_09_loadNodeInfo";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.getNode() == null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyProcessNodeProperty<?> nodeProperty = sourceData.getNodeProperty();
        SillyAssert.notNull(nodeProperty, "创建node对象，节点配置信息获取失败");
        SillyNode n = sillyConfig.getSillyFactory().newNode();
        SillyAssert.notEmpty(sourceData.masterId(), "node创建 主数据ID不可为空");
        n.setMasterId(sourceData.masterId());
        n.setTaskId(sourceData.taskId());
        n.setNodeKey(nodeProperty.getNodeKey());
        n.setNodeName(nodeProperty.getNodeName());
        n.setParallelFlag(nodeProperty.isParallel() ? SillyConstant.YesOrNo.YES : SillyConstant.YesOrNo.NO);
        if (n.getStatus() == null) {
            n.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        }
        n.setNodeDate(new Date());
        n.setNodeUserId(sillyConfig.getSillyCurrentUserUtil().currentUserId());
        sourceData.setNode(n);
    }

}
