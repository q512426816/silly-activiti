package com.iqiny.silly.core.service;

import com.iqiny.silly.core.base.Initializable;
import com.iqiny.silly.core.base.core.SillyMaster;

import java.util.List;
import java.util.Map;

/**
 * 傻瓜流程引擎服务 （流程控制服务，一般都结合第三方框架实现，比如 Activiti）
 */
public interface SillyEngineService<T> extends Initializable {

    /**
     * 启动流程
     *
     * @param master      业务主表对象
     * @param variableMap 流程变量Map
     * @return 流程实例 ID
     */
    String start(SillyMaster master, Map<String, Object> variableMap);

    /**
     * 完成任务
     *
     * @param task        任务
     * @param userId      用户ID
     * @param variableMap 流程变量
     */
    void complete(T task, String userId, Map<String, Object> variableMap);

    /**
     * 任务流程任务改变
     *
     * @param task    要改变的 任务   （起始）
     * @param nodeKey 变更到的 节点Key（到哪）
     * @param userId  变更到的 用户ID （到谁）
     * @return 新的任务
     */
    List<T> changeTask(T task, String nodeKey, String userId);

    /**
     * 通过流程实例ID 获取当前的任务
     *
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    List<T> findTaskByProcessInstanceId(String processInstanceId);

    /**
     * 获取任务ID
     *
     * @param task 任务对象
     * @return 任务ID
     */
    String getTaskId(T task);

    /**
     * 通过任务ID 获取任务
     *
     * @param taskId 任务ID
     * @return 任务
     */
    T findTaskById(String taskId);

    /**
     * 通过流程实例ID 获取主表ID
     *
     * @param processInstanceId 流程实例ID
     * @return 主表ID
     */
    String getBusinessKey(String processInstanceId);

    /**
     * 获取任务执行时长
     *
     * @param task 任务
     * @return 执行耗时（毫秒 ms）
     */
    Long getTaskDueTime(T task);

    /**
     * 获取任务的可执行人ID列表
     *
     * @param task 任务
     * @return 执行人ID列表
     */
    List<String> getTaskUserIds(T task);

    /**
     * 通过流程实例ID 结束流程
     *
     * @param processInstanceId 流程实例ID
     */
    void endProcessByProcessInstanceId(String processInstanceId, String userId);

    /**
     * 通过流程实例ID 获取流程Key
     *
     * @param processInstanceId 流程实例ID
     * @return 流程Key
     */
    String getActKeyNameByProcessInstanceId(String processInstanceId);

    /**
     * 获取任务节点定义ID
     *
     * @param taskId 任务ID
     * @return 节点定义ID
     */
    String getTaskNodeKey(String taskId);

}
