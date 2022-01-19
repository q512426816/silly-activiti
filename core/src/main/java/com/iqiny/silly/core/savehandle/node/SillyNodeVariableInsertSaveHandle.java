/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.List;

/**
 * 节点变量 数据保存处理
 */
public class SillyNodeVariableInsertSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyNodeVariableConvertorSaveHandle.ORDER + 100;

    public static final String NAME = "silly_21_nodeVariableInsert";

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
        SillyWriteService writeService = sillyConfig.getSillyWriteService();
        // 保存 节点变量表数据
        doInsertVariable(node, writeService);
    }

    protected <V extends SillyVariable> void doInsertVariable(SillyNode node, SillyWriteService writeService) {
        // 保存变量
        List<V> variableList = node.getVariableList();
        if (!variableList.isEmpty()) {
            boolean flag = writeService.batchInsert(variableList);
            if (!flag) {
                throw new SillyException("保存流程节点表信息发生异常！");
            }
        }
    }

}
