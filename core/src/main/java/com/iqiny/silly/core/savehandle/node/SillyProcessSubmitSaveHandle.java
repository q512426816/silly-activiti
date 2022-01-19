/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.Map;

/**
 * 流程提交处理器
 */
public class SillyProcessSubmitSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyNodeVariableInsertSaveHandle.ORDER + 100;

    public static final String NAME = "silly_22_processSubmit";

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
        return sourceData.isSubmit();
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {

        SillyEngineService engineService = sillyConfig.getSillyEngineService();
        // 当前任务
        final SillyTask nowTask = sourceData.getNowTask();
        SillyAssert.notNull(nowTask, "提交时，当前任务不存在");

        SillyNode node = sourceData.getNode();
        SillyAssert.notNull(node, "节点数据未能获取");
        node.setTaskId(nowTask.getId());

        Map<String, Object> actMap = sourceData.getActMap();
        engineService.complete(nowTask, node.getNodeUserId(), actMap);

    }

}
