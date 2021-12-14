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
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 根据任务ID设置当前任务信息
 */
public class SillyLoadNowTaskSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyConstant.Order.BELONG_INTERNAL_VARIABLE_ORDER;

    public static final String NAME = "loadNowTask";

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
        return StringUtils.isNotEmpty(sourceData.taskId());
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        String taskId = sourceData.taskId();
        SillyEngineService sillyEngineService = sillyConfig.getSillyEngineService();
        SillyTask task = sillyEngineService.findTaskById(taskId);
        SillyAssert.notNull(task, "任务未找到" + taskId);
        sourceData.setNowTask(task);
    }

}
