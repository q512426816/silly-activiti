/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti;

import com.iqiny.silly.activiti.spring.SpringSillyContent;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.config.CurrentUserUtil;
import com.iqiny.silly.core.config.SillyConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.service.SillyEngineService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import java.util.*;

/**
 * 工作流引擎服务
 */
public abstract class BaseSillyActivitiEngineService implements SillyEngineService<Task> {

    protected SillyConfig sillyConfig;
    protected CurrentUserUtil currentUserUtil;

    protected RuntimeService runtimeService;
    protected HistoryService historyService;
    protected TaskService taskService;
    protected RepositoryService repositoryService;

    @Override
    public void init() {
        this.sillyConfig = SillyConfigUtil.getSillyConfig(usedCategory());
        this.currentUserUtil = this.sillyConfig.getCurrentUserUtil();
        this.runtimeService = SpringSillyContent.getBean(RuntimeService.class);
        this.historyService = SpringSillyContent.getBean(HistoryService.class);
        this.taskService = SpringSillyContent.getBean(TaskService.class);
        this.repositoryService = SpringSillyContent.getBean(RepositoryService.class);
    }

    @Override
    public String usedCategory() {
        return DEFAULT_CATEGORY;
    }

    @Override
    public String start(SillyMaster master, Map<String, Object> variableMap) {
        if (StringUtils.isEmpty(master.processKey())) {
            throw new RuntimeException("流程启动时流程KEY 不可为空！");
        }
        if (StringUtils.isEmpty(master.getId())) {
            throw new RuntimeException("流程启动时 业务主键 不可为空！");
        }
        if (variableMap == null) {
            variableMap = new HashMap<>();
        }
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(master.processKey(), master.getId(), variableMap);
        return processInstance.getId();
    }

    @Override
    public void complete(Task task, String userId, Map<String, Object> variableMap) {
        String taskId = task.getId();
        if (StringUtils.isEmpty(task.getId())) {
            throw new RuntimeException("当前执行的任务ID获取失败");
        }
        if (task.getAssignee() == null) {
            // 认领任务
            taskService.claim(taskId, userId);
        } else {
            task.setAssignee(userId);
        }
        // 完成任务
        taskService.complete(taskId, variableMap);
    }

    @Override
    public synchronized List<Task> changeTask(Task task, String nodeKey, String userId) {
        String processInstanceId = task.getProcessInstanceId();
        String taskId = task.getId();
        if (StringUtils.isEmpty(taskId) || StringUtils.isEmpty(processInstanceId)) {
            throw new SillyException("任务ID/流程实例ID 不可为空！");
        }

        throw new SillyException("此功能暂未实现");
    }

    @Override
    public List<Task> findTaskByProcessInstanceId(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)) {
            throw new SillyException("查询任务列表，流程实例ID不可为空！");
        }
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }

    @Override
    public String getTaskId(Task task) {
        if (task == null) {
            return null;
        }
        return task.getId();
    }

    @Override
    public Task findTaskById(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            return null;
        }
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public String getBusinessKey(String processInstanceId) {
        final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        return historicProcessInstance.getBusinessKey();
    }

    @Override
    public Long getTaskDueTime(Task task) {
        if (task == null || StringUtils.isEmpty(task.getId()) || StringUtils.isEmpty(task.getExecutionId())) {
            return 0L;
        }
        final List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .executionId(task.getExecutionId()).orderByHistoricActivityInstanceEndTime().desc().list();
        for (HistoricActivityInstance instance : list) {
            if (Objects.equals(instance.getTaskId(), task.getId())) {
                return instance.getDurationInMillis();
            }
        }
        return null;
    }

    @Override
    public List<String> getTaskUserIds(Task task) {
        final String taskId = task.getId();
        List<String> ids = new ArrayList<>();
        if (StringUtils.isNotEmpty(taskId)) {
            List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
            for (IdentityLink link : identityLinkList) {
                ids.add(link.getUserId());
            }
        }
        return ids;
    }

    @Override
    public void endProcessByProcessInstanceId(String processInstanceId, String userId) {
        List<Task> tasks = findTaskByProcessInstanceId(processInstanceId);
        if (!tasks.isEmpty()) {
            endProcessByProcessInstanceId(processInstanceId, tasks.get(0), userId);
        }
    }

    public void endProcessByProcessInstanceId(String actProcessId, Task task, String userId) {
        if (task != null) {
            changeTask(task, SillyConstant.ActivitiNode.KEY_END, userId);
        }
        if (actProcessId != null) {
            List<Task> tasks = findTaskByProcessInstanceId(actProcessId);
            if (!tasks.isEmpty()) {
                endProcessByProcessInstanceId(actProcessId, tasks.get(0).getId());
            }
        }
    }

    @Override
    public String getActKeyNameByProcessInstanceId(String processInstanceId) {
        final String processDefinitionId = getProcessDefinitionIdByProcessInstanceId(processInstanceId);
        if (StringUtils.isEmpty(processDefinitionId)) {
            return null;
        }
        final ProcessDefinition deployedProcessDefinition = ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinitionId);
        if (deployedProcessDefinition != null) {
            return deployedProcessDefinition.getKey();
        }
        return null;
    }

    @Override
    public String getTaskNodeKey(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            return null;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            return task.getTaskDefinitionKey();
        } else {
            // 状态设置为已完成
            return SillyConstant.ActivitiNode.KEY_END;
        }
    }

    public String getVersion(String processId) {
        final String key = getActKeyNameByProcessInstanceId(processId);
        return getVersionByKey(key);
    }

    protected String getVersionByKey(String key) {
        if (key != null) {
            String[] arr = key.split("_");
            if (arr.length <= 1) {
                return "";
            }
            return StringUtils.lowerCase(arr[arr.length - 1]);
        }
        return "";
    }

    // ======================================= 工作流服务 内部方法 =============================================

    private String getProcessDefinitionIdByProcessInstanceId(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)) {
            return null;
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String processDefinitionId = "";
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        return processDefinitionId;
    }


    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    @Override
    public List<Task> findTaskByMasterId(String masterId) {
        final List<HistoricProcessInstance> processInstances = findProcessInstanceByMasterId(masterId);
        List<Task> taskList = new ArrayList<>();
        for (HistoricProcessInstance processInstance : processInstances) {
            taskList.addAll(findTaskByProcessInstanceId(processInstance.getId()));
        }
        return taskList;
    }

    public List<HistoricProcessInstance> findProcessInstanceByMasterId(String masterId) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(masterId).list();
    }

    @Override
    public void changeUser(String taskId, String userId) {
        taskService.setAssignee(taskId, userId);
    }

    @Override
    public void addUser(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
    }

}
