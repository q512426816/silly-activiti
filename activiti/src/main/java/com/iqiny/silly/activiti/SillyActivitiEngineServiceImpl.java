package com.iqiny.silly.activiti;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyMasterTask;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.service.SillyEngineService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.ProcessEngineFactoryBean;

import java.util.*;

public class SillyActivitiEngineServiceImpl implements SillyEngineService<Task> {

    protected ProcessEngineFactoryBean processEngineFactoryBean;

    protected RuntimeService runtimeService;
    protected HistoryService historyService;
    protected TaskService taskService;
    protected RepositoryService repositoryService;

    public void setProcessEngineFactoryBean(ProcessEngineFactoryBean processEngineFactoryBean) {
        this.processEngineFactoryBean = processEngineFactoryBean;
    }

    protected void doInit() {
        if (processEngineFactoryBean == null) {
            return;
        }

        final ProcessEngineConfigurationImpl processEngineConfiguration = processEngineFactoryBean.getProcessEngineConfiguration();
        if (processEngineConfiguration == null) {
            return;
        }

        this.runtimeService = processEngineConfiguration.getRuntimeService();
        this.historyService = processEngineConfiguration.getHistoryService();
        this.taskService = processEngineConfiguration.getTaskService();
        this.repositoryService = processEngineConfiguration.getRepositoryService();
    }

    @Override
    public void init() {
        doInit();
    }

    @Override
    public String start(SillyMaster master, Map<String, Object> variableMap) {
        if (StringUtils.isEmpty(master.processKey())) {
            throw new SillyException("流程启动时流程KEY 不可为空！");
        }
        if (StringUtils.isEmpty(master.getId())) {
            throw new SillyException("流程启动时 业务主键 不可为空！");
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
            throw new SillyException("当前执行的任务ID获取失败");
        }
        // 认领任务
        taskService.claim(taskId, userId);
        // 完成任务
        taskService.complete(taskId, variableMap);
    }

    @Override
    public List<Task> changeTask(Task task, String nodeKey, String userId) {
        String processInstanceId = task.getProcessInstanceId();
        String taskId = task.getId();
        if (StringUtils.isEmpty(taskId) || StringUtils.isEmpty(processInstanceId)) {
            throw new SillyException("任务ID/流程实例ID 不可为空！");
        }
        /*// 目标节点
        ActivityImpl pointActivity = findActivitiImpl(taskId, nodeKey);
        Map<String, Object> value = makeUserVarMap(pointActivity, userId);
        // 当前节点
        ActivityImpl currActivity = findActivitiImpl(taskId, null);
        // 清空当前流向
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);
        // 创建新流向
        TransitionImpl newTransition = currActivity.createOutgoingTransition();
        // 设置新流向的目标节点
        newTransition.setDestination(pointActivity);
        // 执行转向任务
        taskService.complete(taskId, value);
        // 删除目标节点新流入
        pointActivity.getIncomingTransitions().remove(newTransition);*/
        // 还原以前流向
        //restoreTransition(currActivity, oriPvmTransitionList);
        return findTaskByProcessInstanceId(processInstanceId);
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
        final TaskQuery taskQuery = taskService.createTaskQuery();
        return taskQuery.taskId(taskId).singleResult();
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

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
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
       /* final ReadOnlyProcessDefinition deployedProcessDefinition = ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);
        if (deployedProcessDefinition != null) {
            return deployedProcessDefinition.getKey();
        }*/
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

    /**
     * @param category 流程类型
     * @param userId   用户ID
     * @return 业务MasterId
     */
    @Override
    public List<? extends SillyMasterTask> getDoingMasterTask(String category, String userId) {
        throw new SillyException("请自行实现getDoingMasterId");
    }

    @Override
    public List<Task> findTaskByMasterId(String masterId) {
        final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(masterId).singleResult();
        if (processInstance == null) {
            return new ArrayList<>();
        }
        return findTaskByProcessInstanceId(processInstance.getProcessInstanceId());
    }


    // ======================================= 工作流服务 内部方法 =============================================

    /*private ActivityImpl findActivitiImpl(String taskId, String nodeKey) {
        // 取得流程定义 11
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

        // 获取当前活动节点ID
        if (nodeKey == null || "".equals(nodeKey)) {
            nodeKey = findTaskById(taskId).getTaskDefinitionKey();
        }
        // 根据节点ID，获取对应的活动节点
        return processDefinition.findActivity(nodeKey);
    }*/

    private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) {
        // 取得流程定义
        return (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(findTaskById(taskId)
                        .getProcessDefinitionId());
    }

   /* private Map<String, Object> makeUserVarMap(ActivityImpl pointActivity, String userId) {
        if (pointActivity != null && pointActivity.getActivityBehavior() instanceof UserTaskActivityBehavior) {
            // 设置节点处置人信息
            UserTaskActivityBehavior behavior = (UserTaskActivityBehavior) pointActivity.getActivityBehavior();
            Set<Expression> set = behavior.getTaskDefinition().getCandidateUserIdExpressions();
            Map<String, Object> value = new HashMap<>();
            for (Expression expression : set) {
                String et = expression.getExpressionText();
                if (et.length() > 3) {
                    // 设置下一步操作人
                    String userKey = et.substring(2, et.length() - 1);
                    value.put(userKey, userId);
                }
            }
            return value;
        }
        return new HashMap<>();
    }*/

    /*private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        List<PvmTransition> oriPvmTransitionList = new ArrayList<>(pvmTransitionList);
        pvmTransitionList.clear();
        return oriPvmTransitionList;
    }*/

    /*private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        pvmTransitionList.addAll(oriPvmTransitionList);
    }*/

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
    public void changeUser(String taskId, String userId) {
        taskService.unclaim(taskId);
        taskService.claim(taskId, userId);
    }

    @Override
    public void addUser(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
    }
}
