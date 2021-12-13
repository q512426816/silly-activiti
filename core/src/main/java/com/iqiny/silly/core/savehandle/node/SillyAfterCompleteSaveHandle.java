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
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 流程 任务处置完成后 处理器
 */
public class SillyAfterCompleteSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyProcessSubmitSaveHandle.ORDER + 100;

    public static final String NAME = "afterComplete";

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
        return sourceData.getNowTask() != null && sourceData.getNextTaskList() != null && !sourceData.getNextTaskList().isEmpty();
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyMaster master = sourceData.getMaster();
        SillyNode node = sourceData.getNode();
        List<? extends SillyTask> nextTaskList = sourceData.getNextTaskList();
        SillyEngineService engineService = sillyConfig.getSillyEngineService();
        SillyCurrentUserUtil currentUserUtil = sillyConfig.getSillyCurrentUserUtil();
        afterCompleteProcess(master, node, nextTaskList, engineService, currentUserUtil);
    }

    /**
     * 流程完成之后的回调
     */
    protected void afterCompleteProcess(SillyMaster master, SillyNode node, List<? extends SillyTask> taskList
            , SillyEngineService engineService, SillyCurrentUserUtil currentUserUtil) {
        String masterId = master.getId();
        if (masterId == null) {
            throw new SillyException("通过任务流程实例ID获取主表数据ID异常！");
        }

        if (StringUtils.isEmpty(master.getTaskName())) {
            master.setTaskName(makeTaskName(taskList));
        }
        if (StringUtils.isEmpty(master.getHandleUserName())) {
            master.setHandleUserName(userIdsToName(nextProcess(taskList, engineService), currentUserUtil));
        }
        if (StringUtils.isEmpty(master.getStatus())) {
            master.setStatus(master.doingStatus());
        }
        if (StringUtils.isEmpty(node.getHandleType())) {
            node.setHandleType(SillyConstant.SillyResumeType.PROCESS_TYPE_NEXT);
        }
    }

    protected String makeTaskName(List<? extends SillyTask> taskList) {
        Set<String> taskNames = new LinkedHashSet<>();
        for (SillyTask task : taskList) {
            taskNames.add(task.getName());
        }
        return StringUtils.myJoin(taskNames, SillyConstant.ARRAY_SPLIT_STR);
    }

    protected String userIdsToName(Set<String> userIds, SillyCurrentUserUtil currentUserUtil) {
        Set<String> userNames = new LinkedHashSet<>();
        for (String userId : userIds) {
            userNames.add(currentUserUtil.userIdToName(userId));
        }
        return StringUtils.myJoin(userNames, SillyConstant.ARRAY_SPLIT_STR);
    }

    protected Set<String> nextProcess(List<? extends SillyTask> taskList, SillyEngineService engineService) {
        Set<String> userIds = new LinkedHashSet<>();
        for (SillyTask task : taskList) {
            final List<String> taskUserIds = engineService.getTaskUserIds(task);
            if (taskUserIds != null && !taskUserIds.isEmpty()) {
                userIds.addAll(taskUserIds);
            }
        }
        return userIds;
    }

}
