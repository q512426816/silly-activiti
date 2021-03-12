package com.iqiny.silly.activiti;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyConfig;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.service.base.AbstractSillyWriteService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class EnhanceSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyWriteService<M, N, V, Task> implements InitializingBean {

    @Autowired
    private SillyConfig sillyConfig;

    @Override
    public SillyConfig initSillyConfig() {
        return sillyConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.init();
    }

    @Override
    protected void afterStartProcess(M master, Task task) {
        if (StringUtils.isEmpty(master.getStatus())) {
            master.setStatus(master.startStatus());
        }
        if (StringUtils.isEmpty(master.getTaskName())) {
            master.setTaskName(task.getName());
        }

        master.setStartDate(new Date());
        master.setStartUserId(currentUserUtil.currentUserId());
        final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task);
        final String joinNextUserIds = StringUtils.join(taskUserIds);
        master.setHandleUserName(userIdsToName(joinNextUserIds));
    }

    @Override
    protected List<Task> completeTask(M master, N node, Map<String, Object> varMap) {
        SillyAssert.notEmpty(node.getTaskId(), "完成任务操作，任务ID不可为空！");
        Task task = sillyEngineService.findTaskById(node.getTaskId());
        SillyAssert.notNull(task, "任务获取失败！");
        complete(varMap, task);
        return afterCompleteTask(master, node, task);
    }

    /**
     * 任务完成之后的回调
     *
     * @param master
     * @param node
     * @return
     */
    protected List<Task> afterCompleteTask(M master, N node, Task oldTask) {
        final String actProcessId = oldTask.getProcessInstanceId();
        // 判断是否结束任务了
        List<Task> taskList = sillyEngineService.findTaskByProcessInstanceId(actProcessId);
        if (taskList.isEmpty()) {
            afterCloseProcess(master, node);
        } else {
            // 下一步任务记录
            afterCompleteProcess(master, node, taskList);
        }
        return taskList;
    }

    @Override
    protected void saveProcessResume(N node, Task oldTask, String nextTaskId) {
        final Task task = sillyEngineService.findTaskById(nextTaskId);
        saveProcessResume(node, oldTask, Collections.singletonList(task));
    }

    @Override
    protected void saveProcessResume(N node, Task oldTask, List<Task> nextTaskList) {
        String handleType = node.getHandleType();
        if (StringUtils.isEmpty(handleType)) {
            // 默认下一步
            handleType = SillyConstant.SillyResumeType.PROCESS_TYPE_NEXT;
        }
        String taskName = node.getNodeInfo();
        String taskKey = node.getNodeKey();
        Long dueTime = null;
        if (oldTask != null) {
            dueTime = sillyEngineService.getTaskDueTime(oldTask);
            taskName = oldTask.getName();
            taskKey = oldTask.getTaskDefinitionKey();
        }

        final Set<String> nextUserIds = nextProcess(nextTaskList);
        final String joinNextUserIds = StringUtils.join(nextUserIds);
        // 履历内容
        String handleInfo = sillyResumeService.makeResumeHandleInfo(handleType, joinNextUserIds, taskName, node.getNodeInfo());

        final String masterId = node.getMasterId();
        doSaveProcessResume(masterId, handleInfo, handleType, taskKey, taskName, joinNextUserIds, dueTime);
    }

    protected void doSaveProcessResume(String masterId, String handleInfo, String handleType, String processNodeKey, String processNodeName, String nextUserIds, Long dueTime) {
        SillyResume process = sillyFactory.newResume();
        process.setProcessType(handleType);
        process.setBusinessId(processResumeBusinessId(masterId));
        process.setBusinessType(processResumeBusinessType());
        process.setHandleInfo(handleInfo);
        process.setProcessNodeKey(processNodeKey);
        process.setProcessNodeName(processNodeName);
        process.setNextUserId(nextUserIds);
        process.setConsumeTime(dueTime);
        // 插入流程履历
        sillyResumeService.insert(process);
    }


    /**
     * 通过主表ID 获取流程履历业务ID
     *
     * @param masterId
     * @return
     */
    protected String processResumeBusinessId(String masterId) {
        // 默认使用本身作为流程履历业务ID
        return masterId;
    }

    /**
     * 获取流程履历的业务类型
     *
     * @return
     */
    protected abstract String processResumeBusinessType();


    /**
     * 完成流程任务  (主表状态只控制 新建（保存）/提交（提交）/完成（结束）  更多状态请使用 activiti事件控制)
     *
     * @param variableMap
     * @param task
     * @return
     */
    protected void complete(Map<String, Object> variableMap, Task task) {
        // 完成此步骤流程
        sillyEngineService.complete(task, currentUserUtil.currentUserId(), variableMap);
    }

    /**
     * 强制结束流程
     *
     * @param processInstanceId
     */
    @Override
    protected void forceEndProcess(String processInstanceId) {
        sillyEngineService.deleteProcessInstance(processInstanceId, "流程删除");
    }

    /**
     * 获取任务执行耗时
     *
     * @param task
     * @return
     */
    protected Long taskDueTime(Task task) {
        return sillyEngineService.getTaskDueTime(task);
    }

    /**
     * 流程结束之后的回调
     *
     * @param master
     * @param node
     */
    protected void afterCloseProcess(M master, N node) {
        final String masterId = master.getId();
        if (masterId == null) {
            throw new SillyException("通过任务流程实例ID获取主表数据ID异常！");
        }
        boolean updateFlag = false;
        if (StringUtils.isEmpty(master.getStatus())) {
            master.setStatus(master.endStatus());
            master.setCloseDate(new Date());
            master.setCloseUserId(currentUserUtil.currentUserId());
            master.setHandleUserName("");
            updateFlag = true;
        }
        if (StringUtils.isEmpty(master.getTaskName())) {
            master.setTaskName("流程结束");
            updateFlag = true;
        }
        node.setHandleType(SillyConstant.SillyResumeType.PROCESS_TYPE_CLOSE);

        if (updateFlag && !updateById(master)) {
            throw new SillyException("更新主表状态发生异常！");
        }
    }

    /**
     * 流程完成之后的回调
     *
     * @param master
     * @param node
     * @param taskList
     */
    protected void afterCompleteProcess(M master, N node, List<Task> taskList) {
        String masterId = master.getId();
        if (masterId == null) {
            throw new SillyException("通过任务流程实例ID获取主表数据ID异常！");
        }

        boolean updateFlag = false;
        if (StringUtils.isEmpty(master.getTaskName())) {
            Set<String> taskNames = new LinkedHashSet<>();
            for (Task task : taskList) {
                taskNames.add(task.getName());
            }
            master.setTaskName(StringUtils.join(taskNames, ","));
            updateFlag = true;
        }
        if (StringUtils.isEmpty(master.getHandleUserName())) {
            master.setHandleUserName(userIdsToName(nextProcess(taskList)));
            updateFlag = true;
        }
        if (StringUtils.isEmpty(master.getStatus())) {
            master.setStatus(master.doingStatus());
            updateFlag = true;
        }
        if (StringUtils.isEmpty(node.getHandleType())) {
            node.setHandleType(SillyConstant.SillyResumeType.PROCESS_TYPE_NEXT);
            updateFlag = true;
        }

        if (updateFlag && !updateById(master)) {
            throw new SillyException("更新主表数据发生异常！");
        }

    }

    protected String userIdsToName(String userIds) {
        if (StringUtils.isEmpty(userIds)) {
            return null;
        }

        final String[] split = userIds.split(",");
        Set<String> userNames = new LinkedHashSet<>();
        for (String userId : split) {
            userNames.add(currentUserUtil.userIdToName(userId));
        }
        return StringUtils.join(userNames, ",");
    }

    protected String userIdsToName(Set<String> userIds) {
        if (userIds == null) {
            return null;
        }

        Set<String> userNames = new LinkedHashSet<>();
        for (String userId : userIds) {
            userNames.add(currentUserUtil.userIdToName(userId));
        }
        return StringUtils.join(userNames, ",");
    }

    protected Set<String> nextProcess(List<Task> taskList) {
        Set<String> userIds = new LinkedHashSet<>();
        for (Task task1 : taskList) {
            final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task1);
            if (taskUserIds == null || taskUserIds.isEmpty()) {
                throw new SillyException("下一步任务处置人不可为空！");
            }
            userIds.addAll(taskUserIds);
        }
        return userIds;
    }

}
