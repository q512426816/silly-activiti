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
    protected void onStartProcess(M master, Task task) {
        master.setStatus(master.startStatus());
        master.setStartDate(new Date());
        master.setStartUserId(currentUserUtil.currentUserId());
        master.setTaskName(task.getName());
        final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task);
        master.setHandleUserName(userIdsToName(StringUtils.join(taskUserIds)));

        // 保存流程履历信息
        saveProcessResume(task, StringUtils.join(taskUserIds), "流程启动");
    }

    @Override
    protected List<Task> completeTask(String taskId, Map<String, Object> varMap) {
        Task task = sillyEngineService.findTaskById(taskId);
        SillyAssert.notNull(task, "任务获取失败！");
        complete(varMap, task);
        return afterCompleteTask(task.getProcessInstanceId());
    }

    protected List<Task> afterCompleteTask(String actProcessId) {
        // 判断是否结束任务了
        List<Task> taskList = sillyEngineService.findTaskByProcessInstanceId(actProcessId);
        if (taskList.isEmpty()) {
            // 任务完成
            final String masterId = sillyEngineService.getBusinessKey(actProcessId);
            preCloseProcess(masterId);
        } else {
            // 下一步任务记录
            afterCompleteProcess(taskList);
        }
        return taskList;
    }

    @Override
    protected void saveProcessResume(N node, List<Task> nextTaskList) {
        String handleType = node.getHandleType();
        if (StringUtils.isEmpty(handleType)) {
            // 默认下一步
            handleType = SillyConstant.SillyResumeType.PROCESS_TYPE_NEXT;
        }
        final Task task = sillyEngineService.findTaskById(node.getTaskId());
        SillyAssert.notNull(task);

        final Long dueTime = sillyEngineService.getTaskDueTime(task);
        final String masterId = node.getMasterId();

        final Set<String> nextUserIds = nextProcess(nextTaskList);
        final String joinNextUserIds = StringUtils.join(nextUserIds);
        // 履历内容
        String handleInfo = sillyResumeService.makeResumeHandleInfo(joinNextUserIds, task.getName(), node.getNodeInfo());

        doSaveProcessResume(masterId, handleInfo, handleType, task.getTaskDefinitionKey(), task.getName(), joinNextUserIds, dueTime);
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
     * 保存流程履历数据
     *
     * @param task
     * @param nextUserIds
     * @param orgHandleInfo
     */
    protected void saveProcessResume(Task task, String nextUserIds, String orgHandleInfo) {
        final Long dueTime = sillyEngineService.getTaskDueTime(task);
        final String masterId = sillyEngineService.getBusinessKey(task.getProcessInstanceId());
        // 履历内容
        String handleInfo = sillyResumeService.makeResumeHandleInfo(nextUserIds, task.getName(), orgHandleInfo);

        SillyResume process = sillyFactory.newResume();
        process.setProcessType(SillyConstant.SillyResumeType.PROCESS_TYPE_NEXT);
        process.setBusinessId(processResumeBusinessId(masterId));
        process.setBusinessType(processResumeBusinessType());
        process.setHandleInfo(handleInfo);
        process.setProcessNodeKey(task.getTaskDefinitionKey());
        process.setProcessNodeName(task.getName());
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
        complete(variableMap, task, currentUserUtil.currentUserId());
    }

    protected void complete(Map<String, Object> variableMap, Task task, String userId) {
        // 完成此步骤流程
        sillyEngineService.complete(task, userId, variableMap);
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

    protected String preCloseProcess(Task task) {
        String masterId = sillyEngineService.getBusinessKey(task.getProcessInstanceId());
        return preCloseProcess(masterId);
    }

    protected String preCloseProcess(String masterId) {
        if (masterId == null) {
            throw new SillyException("通过任务流程实例ID获取主表数据ID异常！");
        }
        M master = sillyFactory.newMaster();
        master.setStatus(master.endStatus());
        master.setCloseDate(new Date());
        master.setCloseUserId(currentUserUtil.currentUserId());
        master.setTaskName("流程结束");
        master.setHandleUserName("");
        master.setId(masterId);
        if (!updateById(master)) {
            throw new SillyException("更新主表状态发生异常！");
        }
        return masterId;
    }

    protected String afterCompleteProcess(List<Task> taskList) {
        Set<String> taskNames = new LinkedHashSet<>();
        for (Task task : taskList) {
            taskNames.add(task.getName());
        }
        String masterId = sillyEngineService.getBusinessKey(taskList.get(0).getProcessInstanceId());
        if (masterId == null) {
            throw new SillyException("通过任务流程实例ID获取主表数据ID异常！");
        }
        M master = sillyFactory.newMaster();
        master.setTaskName(StringUtils.join(taskNames, ","));
        master.setHandleUserName(userIdsToName(nextProcess(taskList)));
        master.setStatus(master.doingStatus());
        master.setId(masterId);
        if (!updateById(master)) {
            throw new SillyException("更新主表数据发生异常！");
        }
        return masterId;
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
