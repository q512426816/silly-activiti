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
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.Date;

/**
 * 节点 数据保存
 */
public class SillyNodeInsertSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_18_nodeInsert";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.getNode() != null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyNode node = sourceData.getNode();
        SillyWriteService writeService = sillyConfig.getSillyWriteService();
        SillyCurrentUserUtil currentUserUtil = sillyConfig.getSillyCurrentUserUtil();
        // 保存 节点变量表数据
        doInsertNode(node, currentUserUtil, writeService);
    }

    protected void doInsertNode(SillyNode node, SillyCurrentUserUtil currentUserUtil, SillyWriteService writeService) {
        node.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        node.setNodeDate(new Date());
        node.setNodeUserId(currentUserUtil.currentUserId());
        boolean flag = writeService.insert(node);
        if (!flag) {
            throw new SillyException("保存流程节点表信息发生异常！");
        }
    }

}
