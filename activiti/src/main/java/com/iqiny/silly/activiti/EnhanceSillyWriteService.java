package com.iqiny.silly.activiti;

import com.crrcdt.silly.base.core.SillyMaster;
import com.crrcdt.silly.base.core.SillyNode;
import com.crrcdt.silly.base.core.SillyVariable;
import com.crrcdt.silly.convertor.SillyListConvertor;
import com.crrcdt.silly.convertor.SillyListListConvertor;
import com.crrcdt.silly.convertor.SillyStringConvertor;
import com.crrcdt.silly.convertor.SillyVariableConvertor;
import com.crrcdt.silly.service.base.AbstractSillyWriteService;
import com.iqiny.silly.common.Constant;
import com.iqiny.silly.common.util.StringUtils;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public abstract class EnhanceSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> extends AbstractSillyWriteService<M, N, V> {

    @Autowired
    protected SillyActivitiService sillyActivitiService;

    /**
     * 启动流程 同时主表设置流程实例ID
     *
     * @param master
     * @param varMap
     * @return
     */
    @Override
    protected String startProcess(M master, Map<String, Object> varMap) {
        // 流程启动
        ProcessInstance pi = sillyActivitiService.start(master.actProcessKey(), master.getId(), varMap);
        final String instanceId = pi.getProcessInstanceId();
        // 设置主表的流程实例ID
        master.setActProcessId(instanceId);
        return sillyActivitiService.getTaskIdByProcessId(instanceId);
    }

    @Override
    protected void completeTask(String taskId, String orgHandleInfo, Map<String, Object> varMap) {
        Task task = sillyActivitiService.findTaskById(taskId);
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
        final Long dueTime = sillyActivitiService.getTaskDueTime(task);
        final String masterId = sillyActivitiService.getBusinessKey(task.getProcessInstanceId());
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
        sillyActivitiService.complete(task.getId(), variableMap);
        // 判断是否结束任务了
        List<Task> taskList = sillyActivitiService.findTaskList(actProcessId);
        String nextUserIds = null;
        if (taskList.isEmpty()) {
            preCloseProcess(task);
        } else {
            nextUserIds = nextProcess(taskList);
        }
        return nextUserIds;
    }

    @Override
    protected Map<String, SillyVariableConvertor<?>> initSillyHandlerMap() {
        Map<String, SillyVariableConvertor<?>> sillyHandlerMap = new LinkedHashMap<>();
        sillyHandlerMap.put(Constant.ActivitiNode.ACTIVITI_HANDLER_NEXT_USER_ID, new SillyListConvertor());
        sillyHandlerMap.put(Constant.ActivitiNode.ACTIVITI_HANDLER_LIST_LIST, new SillyListListConvertor());
        sillyHandlerMap.put(Constant.ActivitiNode.ACTIVITI_HANDLER_LIST, new SillyListConvertor());
        sillyHandlerMap.put(Constant.ActivitiNode.ACTIVITI_HANDLER_STRING, new SillyStringConvertor());
        return sillyHandlerMap;
    }

    /**
     * 强制结束流程
     *
     * @param actProcessId
     */
    @Override
    protected void forceEndProcess(String actProcessId) {
        sillyActivitiService.endProcessByActId(actProcessId);
    }

    /**
     * 获取任务执行耗时
     *
     * @param task
     * @return
     */
    protected Long taskDueTime(Task task) {
        return sillyActivitiService.getTaskDueTime(task);
    }

    protected String preCloseProcess(Task task) {
        String masterId = sillyActivitiService.getBusinessKey(task.getProcessInstanceId());
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
            final List<String> taskUserIds = sillyActivitiService.getTaskUserIds(task1.getId());
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
