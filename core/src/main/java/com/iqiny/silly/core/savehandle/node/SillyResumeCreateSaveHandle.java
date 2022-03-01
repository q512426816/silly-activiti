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
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 流程 履历信息创建 处理器
 */
public class SillyResumeCreateSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_26_resumeCreate";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return (sourceData.isSubmit() || sourceData.isStartProcess()) && sourceData.getResume() == null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyNode node = sourceData.getNode();
        SillyAssert.notNull(node, "履历生成 节点node 数据不可为空");
        SillyTask nowTask = sourceData.getNowTask();
        List<? extends SillyTask> nextTaskList = sourceData.getNewNextTaskList();
        if (nextTaskList == null) {
            nextTaskList = sourceData.getNextTaskList();
        }
        SillyResume resume = createProcessResume(node, nowTask, nextTaskList, sillyConfig);
        sourceData.setResume(resume);
    }

    /**
     * 保存流程履历信息
     *
     * @param node         当前处置节点
     * @param oldTask      当前任务
     * @param nextTaskList 下一步任务
     */
    protected SillyResume createProcessResume(SillyNode node, SillyTask oldTask, List<? extends SillyTask> nextTaskList, SillyCategoryConfig sillyConfig) {

        SillyEngineService engineService = sillyConfig.getSillyEngineService();
        SillyResumeService sillyResumeService = sillyConfig.getSillyResumeService();
        SillyFactory sillyFactory = sillyConfig.getSillyFactory();

        String handleType = node.getHandleType();
        if (StringUtils.isEmpty(handleType)) {
            // 默认下一步
            handleType = SillyConstant.SillyResumeType.PROCESS_TYPE_NEXT;
        }
        String taskName = node.getNodeInfo();
        String taskKey = node.getNodeKey();
        Long dueTime = null;
        if (oldTask != null) {
            dueTime = engineService.getTaskDueTime(oldTask);
            taskName = oldTask.getName();
            taskKey = oldTask.getTaskDefinitionKey();
        }

        final Set<String> nextUserIds = nextProcess(nextTaskList, engineService);
        final String joinNextUserIds = StringUtils.myJoin(nextUserIds);
        // 履历内容
        String handleInfo = sillyResumeService.makeResumeHandleInfo(handleType, joinNextUserIds, taskName, node.getNodeInfo());

        final String masterId = node.getMasterId();
        return doSaveProcessResume(masterId, handleInfo, handleType, taskKey, taskName, joinNextUserIds, dueTime, sillyFactory);
    }

    protected SillyResume doSaveProcessResume(String masterId, String handleInfo, String handleType, String processNodeKey
            , String processNodeName, String nextUserIds, Long dueTime, SillyFactory sillyFactory) {
        SillyResume process = sillyFactory.newResume();
        SillyAssert.notEmpty(masterId, "履历保存业务主键不可为空");
        process.setProcessType(handleType);
        process.setBusinessId(masterId);
        process.setBusinessType(sillyFactory.usedCategory());
        process.setHandleInfo(handleInfo);
        process.setProcessNodeKey(processNodeKey);
        process.setProcessNodeName(processNodeName);
        process.setNextUserId(nextUserIds);
        process.setConsumeTime(dueTime);
        return process;
    }

    protected Set<String> nextProcess(List<? extends SillyTask> taskList, SillyEngineService engineService) {
        Set<String> userIds = new LinkedHashSet<>();
        if (taskList == null) {
            return userIds;
        }

        for (SillyTask task : taskList) {
            final List<String> taskUserIds = engineService.getTaskUserIds(task);
            if (taskUserIds != null && !taskUserIds.isEmpty()) {
                userIds.addAll(taskUserIds);
            }
        }
        return userIds;
    }

}
