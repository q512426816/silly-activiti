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
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.*;

/**
 * 流程启动处理器
 */
public class SillyProcessStartSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_13_processStart";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        boolean flag = sourceData.getNowTask() == null && sourceData.isStartProcess()
                && sourceData.getMaster() != null && StringUtils.isNotEmpty(sourceData.masterId())
                && StringUtils.isEmpty(sourceData.getMaster().getProcessId());
        if (!flag) {
            return false;
        }
        String category = sourceData.getCategory();
        SillyCategoryConfig sillyConfig = SillyConfigUtil.getSillyConfig(category);
        SillyEngineService engineService = sillyConfig.getSillyEngineService();
        final List<? extends SillyTask> taskList = engineService.findTaskByMasterId(sourceData.masterId());
        if (taskList != null && !taskList.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        String category = sourceData.getCategory();
        SillyMaster master = sourceData.getMaster();
        SillyNode node = sourceData.getNode();
        SillyAssert.notNull(node, "节点数据未能获取");
        SillyProcessNodeProperty<?> nodeProperty = sourceData.getNodeProperty();
        SillyProcessMasterProperty masterProperty = nodeProperty.getParent();
        master.setProcessKey(masterProperty.getProcessKey());
        master.setProcessVersion(masterProperty.getProcessVersion());
        SillyEngineService engineService = sillyConfig.getSillyEngineService();

        boolean startFlag = doStartProcess(master, sourceData.getActMap(), engineService, node.getNodeUserId());

        if (startFlag) {
            // 设置当前任务ID
            String nodeUserId = node.getNodeUserId();
            Set<String> allGroupId = null;
            SillyCurrentUserUtil sillyCurrentUserUtil = sillyConfig.getSillyCurrentUserUtil();
            if (sillyCurrentUserUtil.isAdmin(nodeUserId)) {
                nodeUserId = null;
            } else {
                allGroupId = sillyConfig.getSillyTaskGroupHandle().getAllGroupId(category, nodeUserId);
            }
            SillyMasterTask masterTask = engineService.getOneTask(category, nodeUserId, master.getId(), allGroupId);
            SillyAssert.notNull(masterTask, "未找到您需要处置的任务" + category);
            SillyTask nowTask = engineService.findTaskById(masterTask.getTaskId());
            SillyAssert.notNull(nowTask, "未找到任务" + masterTask.getTaskId());
            sourceData.setNowTask(nowTask);
            sourceData.setNowTaskList(new ArrayList<>());
            node.setTaskId(nowTask.getId());
        }
    }

    protected boolean doStartProcess(SillyMaster master, Map<String, Object> varMap, SillyEngineService engineService, String currentUserId) {
        // 流程启动  返回任务ID
        String processInstanceId = engineService.start(master, varMap);
        master.setProcessId(processInstanceId);
        master.setStartDate(new Date());
        master.setStartUserId(currentUserId);
        master.setStatus(master.startStatus());
        return StringUtils.isNotEmpty(processInstanceId);
    }

}
