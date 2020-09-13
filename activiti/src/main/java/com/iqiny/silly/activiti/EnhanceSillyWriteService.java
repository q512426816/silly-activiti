package com.iqiny.silly.activiti;

import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.service.base.AbstractSillyWriteService;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class EnhanceSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> extends AbstractSillyWriteService<M, N, V> {

    @Override
    protected void completeTask(String taskId, String orgHandleInfo, Map<String, Object> varMap) {
        Task task = (Task) sillyEngineService.findTaskById(taskId);
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
        //final Long dueTime = sillyActivitiService.getTaskDueTime(task);
        //final String masterId = sillyActivitiService.getBusinessKey(task.getProcessInstanceId());
        // 履历内容
       /* String handleInfo = getSillyResumeService().makeResumeHandleInfo(nextUserIds, task.getName(), orgHandleInfo);

        SillyResume process = getSillyFactory().newResume();
        process.setProcessType(MbpProcessResume.PROCESS_TYPE_NEXT);
        process.setBusinessId(processResumeBusinessId(masterId));
        process.setBusinessType(processResumeBusinessType());
        process.setHandleInfo(handleInfo);
        process.setProcessNodeKey(task.getTaskDefinitionKey());
        process.setProcessNodeName(task.getName());
        process.setNextUserId(nextUserIds);
        process.setConsumeTime(dueTime);
        process.setHandleDate(new Date());
        process.setHandleUserId(ThreadUtil.currentUserId());
        // 插入流程履历
        getSillyResumeService().insert(process);*/
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
            throw new RuntimeException("通过任务流程实例ID获取主表数据ID异常！");
        }
        M master = sillyFactory.newMaster();
        master.setStatus(master.getEndStatus());
        master.setCloseDate(new Date());
        master.setCloseUserId(currentUserUtil.currentUserId());
        master.setId(masterId);
        if (!updateById(master)) {
            throw new RuntimeException("更新主表状态发生异常！");
        }
        return masterId;
    }

    protected String nextProcess(List<Task> taskList) {
        List<String> userIds = new ArrayList<>();
        for (Task task1 : taskList) {
            final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task1.getId());
            if (taskUserIds == null || taskUserIds.isEmpty()) {
                throw new RuntimeException("下一步任务处置人不可为空！");
            }
            userIds.addAll(taskUserIds);
        }
        String nextUserIds = StringUtils.join(userIds, ",");
        if (StringUtils.isEmpty(nextUserIds)) {
            throw new RuntimeException("下一步处置人不可为空！");
        }
        return nextUserIds;
    }

}
