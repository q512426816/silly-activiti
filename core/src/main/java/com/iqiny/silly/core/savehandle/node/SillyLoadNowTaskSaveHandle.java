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
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.List;

/**
 * 根据任务ID设置当前任务信息
 */
public class SillyLoadNowTaskSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_01_loadNowTask";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.getNowTask() == null && StringUtils.isNotEmpty(sourceData.taskId());
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        String taskId = sourceData.taskId();
        SillyEngineService sillyEngineService = sillyConfig.getSillyEngineService();
        SillyTask task = sillyEngineService.findTaskById(taskId);
        SillyAssert.notNull(task, "任务未找到" + taskId);
        sourceData.setNowTask(task);
        // 获取当前全部任务信息
        List<? extends SillyTask> nowTaskList = sillyEngineService.findTaskByProcessInstanceId(task.getProcessInstanceId());
        sourceData.setNowTaskList(nowTaskList);
    }

}
