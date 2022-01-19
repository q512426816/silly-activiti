/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 根据任务ID设置当前任务信息
 */
public class SillyLoadNextTaskSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyProcessSubmitSaveHandle.ORDER + 100;

    public static final String NAME = "silly_23_loadNextTask";

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
        return (sourceData.isSubmit() || sourceData.isStartProcess())
                && sourceData.getNowTask() != null && sourceData.getNowTaskList() != null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {

        SillyEngineService engineService = sillyConfig.getSillyEngineService();
        SillyTask nowTask = sourceData.getNowTask();
        // 获取当前全部任务信息
        final String actProcessId = nowTask.getProcessInstanceId();
        List<? extends SillyTask> nextTaskList = engineService.findTaskByProcessInstanceId(actProcessId);
        sourceData.setNextTaskList(nextTaskList);

        List<? extends SillyTask> newNextTaskList = loadNewNextList(sourceData);
        sourceData.setNewNextTaskList(newNextTaskList);

    }

    protected <T extends SillyTask> List<T> loadNewNextList(SillyNodeSourceData sourceData) {
        List<T> nextTaskList = (List<T>) sourceData.getNextTaskList();
        List<T> nowTaskList = (List<T>) sourceData.getNowTaskList();

        List<T> newNextTaskList = new ArrayList<>();
        if (nextTaskList != null && !nextTaskList.isEmpty()) {
            for (T nextTask : nextTaskList) {
                boolean newTask = true;
                String nextTaskId = nextTask.getId();
                for (T nowTask : nowTaskList) {
                    String nowTaskId = nowTask.getId();
                    if (Objects.equals(nextTaskId, nowTaskId)) {
                        newTask = false;
                    }
                }
                if (newTask) {
                    newNextTaskList.add(nextTask);
                }
            }
        }
        return newNextTaskList;
    }

}
