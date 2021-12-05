/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.engine;

import com.iqiny.silly.core.base.SillyMasterTask;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.service.SillyService;

import java.util.List;
import java.util.Map;

/**
 * 傻瓜流程引擎服务 （流程控制服务，一般都结合第三方框架实现，比如 Activiti）
 */
public interface SillyEngineService<T extends SillyTask> extends SillyService {

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
    void complete(SillyTask task, String userId, Map<String, Object> variableMap);

    /**
     * 任务流程任务改变
     *
     * @param task    要改变的 任务   （起始）
     * @param nodeKey 变更到的 节点Key（到哪）
     * @param userId  变更到的 用户ID （到谁）
     * @return 新的任务
     */
    List<T> changeTask(SillyTask task, String nodeKey, String userId);

    /**
     * 通过流程实例ID 获取当前的任务
     *
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    List<T> findTaskByProcessInstanceId(String processInstanceId);

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
     * 通过流程实例ID 获取主表ID
     *
     * @param taskId 任务ID
     * @return 主表ID
     */
    String getBusinessKeyByTaskId(String taskId);

    /**
     * 获取任务执行时长
     *
     * @param task 任务
     * @return 执行耗时（毫秒 ms）
     */
    Long getTaskDueTime(SillyTask task);

    /**
     * 获取任务的可执行人ID列表
     *
     * @param task 任务
     * @return 执行人ID列表
     */
    List<String> getTaskUserIds(SillyTask task);

    /**
     * 通过流程实例ID 结束流程
     *
     * @param processInstanceId 流程实例ID
     */
    void endProcessByProcessInstanceId(String processInstanceId, String userId);

    /**
     * 通过流程实例ID 删除流程
     *
     * @param processInstanceId 流程实例ID
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);

    /**
     * 通过流程实例ID 获取流程Key
     *
     * @param processInstanceId 流程实例ID
     * @return 流程Key
     */
    String getActKeyNameByProcessInstanceId(String processInstanceId);

    /**
     * 获取进行中的主表ID
     *
     * @param category
     * @param userId
     * @return
     */
    List<SillyMasterTask> getDoingMasterTask(String category, String userId);

    /**
     * 获取历史的主表ID
     *
     * @param category
     * @param userId
     * @return
     */
    List<SillyMasterTask> getHistoryMasterTask(String category, String userId);

    /**
     * 获取此分类下的单业务数据 用户的任务信息
     *
     * @param category
     * @param userId
     * @param masterId
     * @return
     */
    List<SillyMasterTask> getMyDoingMasterTaskId(String category, String userId, String masterId);

    /**
     * 获取某个用户下某业务的一个任务
     *
     * @param masterId
     * @return
     */
    default SillyMasterTask getOneTask(String category, String userId, String masterId) {
        List<SillyMasterTask> list = getMyDoingMasterTaskId(category, userId, masterId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 根据业务表ID 获取任务列表
     *
     * @param masterId
     * @return
     */
    List<T> findTaskByMasterId(String masterId);


    /**
     * 调整任务执行人
     *
     * @param taskId
     * @param userId
     */
    void changeUser(String taskId, String userId);

    /**
     * 添加任务参与者
     *
     * @param taskId
     * @param userId
     */
    void addUser(String taskId, String userId);
}
