/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.variable;

import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;

import java.util.List;

/**
 * 删除除当前任务外 其余全部的任务
 */
public class DeleteOtherTaskVariableSaveHandle implements SillyVariableSaveHandle {

    public static final String NAME = "deleteOtherTask";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean handle(SillyMaster master, SillyNode node, SillyVariable variable) {
        String taskId = node.getTaskId();
        SillyCategoryConfig config = SillyConfigUtil.getSillyConfig(master.usedCategory());
        SillyEngineService engineService = config.getSillyEngineService();
        List<? extends SillyTask> sillyTasks = engineService.findTaskByMasterId(master.getId());
        for (SillyTask sillyTask : sillyTasks) {
            if (!sillyTask.getId().equals(taskId)) {
                engineService.deleteTask(sillyTask.getId());
            }
        }
        return true;
    }
}
