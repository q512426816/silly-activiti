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
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyReadService;

/**
 * 主数据 创建及处理（id 为空则进行新增）
 */
public class SillyLoadMasterByTaskSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyLoadMasterByIdSaveHandle.ORDER + 100;

    public static final String NAME = "silly_04_loadMasterByTask";

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
        return sourceData.getMaster() == null && sourceData.getNowTask() != null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyTask nowTask = sourceData.getNowTask();
        SillyEngineService sillyEngineService = sillyConfig.getSillyEngineService();
        SillyReadService sillyReadService = sillyConfig.getSillyReadService();
        String masterId = sillyEngineService.getBusinessKey(nowTask.getProcessInstanceId());
        SillyAssert.notEmpty(masterId, "根据任务获取 masterId 失败 " + nowTask.getId());
        SillyMaster master = sillyReadService.getMaster(masterId);
        SillyAssert.notNull(master, "根据 masterId 获取数据失败 " + masterId);
        sourceData.setMaster(master);
    }

}
