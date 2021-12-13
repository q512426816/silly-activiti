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
import com.iqiny.silly.core.base.SillyMasterTask;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 流程启动处理器
 */
public class SillyProcessStartSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyProcessMapSaveHandle.ORDER + 100;

    public static final String NAME = "processStart";

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
        return sourceData.isStartProcess();
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        String category = sourceData.getCategory();
        SillyMaster master = sourceData.getMaster();
        SillyEngineService engineService = sillyConfig.getSillyEngineService();
        SillyCurrentUserUtil currentUserUtil = sillyConfig.getSillyCurrentUserUtil();

        boolean startFlag = startProcess(master, sourceData.getActMap(),
                engineService, currentUserUtil);

        if (startFlag && sourceData.isSubmit()) {
            // 设置当前任务ID
            SillyMasterTask task = engineService.getOneTask(category, currentUserUtil.currentUserId(), master.getId());
            SillyAssert.notNull(task, "未找到您需要处置的任务" + category);
            sourceData.setTaskId(task.getTaskId());
        }
    }

    protected boolean startProcess(SillyMaster master, Map<String, Object> varMap, SillyEngineService engineService, SillyCurrentUserUtil currentUserUtil) {
        // 验证主表是否存在流程实例，若存在则不重复启动流程实例
        if (StringUtils.isNotEmpty(master.getProcessId())) {
            return false;
        }

        final List<? extends SillyTask> taskList = engineService.findTaskByMasterId(master.getId());
        if (taskList != null && !taskList.isEmpty()) {
            return false;
        }
        return doStartProcess(master, varMap, engineService, currentUserUtil);
    }

    protected boolean doStartProcess(SillyMaster master, Map<String, Object> varMap, SillyEngineService engineService, SillyCurrentUserUtil currentUserUtil) {
        // 流程启动  返回任务ID
        String processInstanceId = engineService.start(master, varMap);
        master.setProcessId(processInstanceId);
        master.setStartDate(new Date());
        master.setStartUserId(currentUserUtil.currentUserId());
        return StringUtils.isNotEmpty(processInstanceId);
    }

}
