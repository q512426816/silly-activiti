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
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.List;
import java.util.Map;

/**
 * 流程提交处理器
 */
public class SillyProcessSubmitSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyNodeVariableDataSaveHandle.ORDER + 100;

    public static final String NAME = "processSubmit";

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
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {

        SillyNode node = sourceData.getNode();
        final String nowTaskId = node.getTaskId();
        SillyAssert.notEmpty(nowTaskId, "提交时，当前任务ID不存在");
        SillyEngineService engineService = sillyConfig.getSillyEngineService();
        SillyCurrentUserUtil currentUserUtil = sillyConfig.getSillyCurrentUserUtil();
        // 当前任务
        final SillyTask nowTask = engineService.findTaskById(nowTaskId);
        SillyAssert.notNull(nowTask, "提交时，当前任务不存在");
        sourceData.setNowTask(nowTask);

        Map<String, Object> actMap = sourceData.getActMap();
        engineService.complete(nowTask, currentUserUtil.currentUserId(), actMap);

        final String actProcessId = nowTask.getProcessInstanceId();
        List<? extends SillyTask> taskList = engineService.findTaskByProcessInstanceId(actProcessId);
        sourceData.setNextTaskList(taskList);
    }

}
