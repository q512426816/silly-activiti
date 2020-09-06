package com.iqiny.silly.activiti;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * 傻瓜工作流服务 结合流程引擎 Activiti
 */
public interface SillyActivitiService {

    /**
     * 完成任务
     *
     * @param taskId      任务ID
     * @param variableMap 流程变量
     */
    void complete(String taskId, Map<String, Object> variableMap);

    /**
     * 通过流程实例ID 获取任务列表
     *
     * @param processInstanceId 流程实例ID
     * @return 任务列表
     */
    List<Task> findTaskList(String processInstanceId);

    /**
     * 获取此任务ID对应的执行人
     *
     * @param taskId 任务ID
     * @return 执行人ID
     */
    List<String> getTaskUserIds(String taskId);

    /**
     * 获取任务执行时长
     *
     * @param task 任务
     * @return 执行时长（毫秒）
     */
    Long getTaskDueTime(Task task);

    /**
     * 通过流程实例ID 获取对应的业务Key
     *
     * @param processInstanceId 流程实例ID
     * @return 业务ID
     */
    String getBusinessKey(String processInstanceId);

    /**
     * 启动流程
     *
     * @param key         流程定义key
     * @param businessKey 业务Key
     * @param variableMap 流程变量Map
     * @return 流程实例
     */
    ProcessInstance start(String key, String businessKey, Map<String, Object> variableMap);

    /**
     * 通过流程实例ID 获取流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 流程实例
     */
    ProcessInstance getProcessInstance(String processInstanceId);

    /**
     * 通过流程实例ID 结束流程
     *
     * @param actProcessId 流程实例ID
     */
    void endProcessByActId(String actProcessId);

    /**
     * 通过流程实例ID+任务ID 结束流程下的某个任务
     *
     * @param actProcessId 流程实例ID
     * @param taskId       任务ID
     */
    void endProcessByActId(String actProcessId, String taskId);

    /**
     * 任务流程节点改变
     *
     * @param taskId     任务ID
     * @param activityId 流程节点定义ID
     */
    void changeProcessTask(String taskId, String activityId);

    /**
     * 任务流程节点改变
     *
     * @param taskId     任务ID
     * @param activityId 流程节点定义ID
     * @param userId     用户ID
     */
    void changeTask(String taskId, String activityId, String userId);


    Map<String, Object> makeUserVarMap(ActivityImpl pointActivity, Object userId);

    /**
     * 根据任务ID和节点ID获取活动节点 <br>
     *
     * @param taskId     任务ID
     * @param activityId 活动节点ID <br>
     *                   如果为null或""，则默认查询当前活动节点 <br>
     *                   如果为"end"，则查询结束节点 <br>
     * @return
     */
    ActivityImpl findActivitiImpl(String taskId, String activityId);


    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId 任务ID
     * @return 任务实例
     */
    TaskEntity findTaskById(String taskId);

    /**
     * 通过流程实例ID 获取当前的任务
     *
     * @param processInstanceId 流程实例ID
     * @return 任务ID
     */
    String getTaskIdByProcessId(String processInstanceId);

    /**
     * 通过流程实例ID 获取流程节点定义ID
     *
     * @param processInstanceId 流程实例ID
     * @return 流程节点定义ID
     */
    String getActKeyNameByActProcessId(String processInstanceId);

    String getProcessDefinitionIdByActProcessId(String executionId);

    String getTaskNodeKey(String taskId);

}
