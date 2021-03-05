package com.iqiny.silly.activiti;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.service.base.AbstractSillyWriteService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class EnhanceSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyWriteService<M, N, V, Task> implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        super.init();
    }

    @Override
    protected void onStartProcess(M master, Task task) {
        master.setTaskName(task.getName());
        final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task);
        master.setHandleUserName(userIdsToName(StringUtils.join(taskUserIds)));
    }

    @Override
    protected void completeTask(String taskId, String orgHandleInfo, Map<String, Object> varMap) {
        Task task = sillyEngineService.findTaskById(taskId);
        SillyAssert.notNull(task, "任务获取失败！");
        String nextUserIds = complete(varMap, task);
        // 保存流程履历信息
        saveProcessResume(task, nextUserIds, orgHandleInfo);
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
    protected String complete(Map<String, Object> variableMap, Task task) {
        String actProcessId = task.getProcessInstanceId();
        // 完成此步骤流程
        sillyEngineService.complete(task, currentUserUtil.currentUserId(), variableMap);
        // 判断是否结束任务了
        List<Task> taskList = sillyEngineService.findTaskByProcessInstanceId(actProcessId);
        String nextUserIds = null;
        if (taskList.isEmpty()) {
            preCloseProcess(task);
        } else {
            nextUserIds = nextProcess(taskList);
            afterCompleteProcess(taskList, nextUserIds);
        }
        return nextUserIds;
    }

    /**
     * 强制结束流程
     *
     * @param processInstanceId
     */
    @Override
    protected void forceEndProcess(String processInstanceId, String userId) {
        sillyEngineService.endProcessByProcessInstanceId(processInstanceId, userId);
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
        if (masterId == null) {
            throw new SillyException("通过任务流程实例ID获取主表数据ID异常！");
        }
        M master = sillyFactory.newMaster();
        master.setStatus(master.endStatus());
        master.setCloseDate(new Date());
        master.setCloseUserId(currentUserUtil.currentUserId());
        master.setId(masterId);
        if (!updateById(master)) {
            throw new SillyException("更新主表状态发生异常！");
        }
        return masterId;
    }

    protected String afterCompleteProcess(List<Task> taskList, String userIds) {
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
        master.setHandleUserName(userIdsToName(userIds));
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

    protected String nextProcess(List<Task> taskList) {
        List<String> userIds = new ArrayList<>();
        for (Task task1 : taskList) {
            final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task1);
            if (taskUserIds == null || taskUserIds.isEmpty()) {
                throw new SillyException("下一步任务处置人不可为空！");
            }
            userIds.addAll(taskUserIds);
        }
        String nextUserIds = StringUtils.join(userIds, ",");
        if (StringUtils.isEmpty(nextUserIds)) {
            throw new SillyException("下一步处置人不可为空！");
        }
        return nextUserIds;
    }

}
