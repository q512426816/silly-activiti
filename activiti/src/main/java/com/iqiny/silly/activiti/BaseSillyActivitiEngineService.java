/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.group.BaseSillyTaskGroupHandle;
import com.iqiny.silly.core.read.MySillyMasterTask;
import org.activiti.engine.*;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import java.util.*;

/**
 * 集成activiti 工作流引擎服务
 */
public abstract class BaseSillyActivitiEngineService
        implements SillyEngineService<SillyActivitiTask, MySillyMasterTask> {

    protected ManagementService managementService;
    protected RuntimeService runtimeService;
    protected HistoryService historyService;
    protected TaskService taskService;
    protected RepositoryService repositoryService;

  
    public void otherInit(SillyContext sillyContext) {
        this.managementService = sillyContext.getBean(ManagementService.class);
        this.runtimeService = sillyContext.getBean(RuntimeService.class);
        this.historyService = sillyContext.getBean(HistoryService.class);
        this.taskService = sillyContext.getBean(TaskService.class);
        this.repositoryService = sillyContext.getBean(RepositoryService.class);
    }

    public SillyActivitiTask convertor(Task task) {
        if (task == null) {
            return null;
        }

        return new SillyActivitiTask(task);
    }

    public List<SillyActivitiTask> convertor(List<Task> tasks) {
        if (tasks == null) {
            return null;
        }

        List<SillyActivitiTask> taskList = new ArrayList<>();
        for (Task task : tasks) {
            taskList.add(new SillyActivitiTask(task));
        }
        return taskList;
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
    public void complete(SillyTask task, String userId, Map<String, Object> variableMap) {
        String taskId = task.getId();
        if (StringUtils.isEmpty(task.getId())) {
            throw new RuntimeException("当前执行的任务ID获取失败");
        }
        if (task.getAssignee() == null) {
            // 认领任务
            taskService.claim(taskId, userId);
        }
        // 完成任务
        taskService.complete(taskId, variableMap);
    }

    @Override
    public List<SillyActivitiTask> changeTask(SillyTask task, String nodeKey, String userId) {
        String processInstanceId = task.getProcessInstanceId();
        String taskId = task.getId();
        if (StringUtils.isEmpty(taskId) || StringUtils.isEmpty(processInstanceId)) {
            throw new RuntimeException("任务ID/流程实例ID 不可为空！");
        }
        // 目标节点
        ActivityImpl pointActivity = findActivitiImpl(taskId, nodeKey);

        Map<String, Object> value = new HashMap<>();
        if (StringUtils.isEmpty(userId)) {
            value = makeUserVarMap(pointActivity, userId);
        }

        // 当前节点
        ActivityImpl currActivity = findActivitiImpl(taskId, null);

        managementService.executeCommand(
                new RejectToAnyWhereTaskCmd(task.getExecutionId(), pointActivity, value, currActivity, findProcessDefinitionEntityByTaskId(taskId))
        );

        return findTaskByProcessInstanceId(processInstanceId);
    }

    @Override
    public List<SillyActivitiTask> findTaskByProcessInstanceId(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)) {
            throw new SillyException("查询任务列表，流程实例ID不可为空！");
        }
        return convertor(taskService.createTaskQuery().processInstanceId(processInstanceId).list());
    }

    @Override
    public SillyActivitiTask findTaskById(String taskId) {
        if (StringUtils.isEmpty(taskId)) {
            return null;
        }
        return convertor(taskService.createTaskQuery().taskId(taskId).singleResult());
    }

    @Override
    public String getBusinessKey(String processInstanceId) {
        final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        return historicProcessInstance.getBusinessKey();
    }

    @Override
    public String getBusinessKeyByTaskId(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return getBusinessKey(task.getProcessInstanceId());
    }

    @Override
    public Long getTaskDueTime(SillyTask task) {
        if (task == null || StringUtils.isEmpty(task.getId())) {
            return 0L;
        }

        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).singleResult();
        return historicTaskInstance.getDurationInMillis();
    }

    @Override
    public List<String> getTaskUserIds(SillyTask task) {
        final String taskId = task.getId();
        return getTaskUserIds(taskId);
    }

    public List<String> getTaskUserIds(String taskId) {
        List<String> ids = new ArrayList<>();
        if (StringUtils.isNotEmpty(taskId)) {
            List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(taskId);
            for (IdentityLink link : identityLinkList) {
                String groupId = link.getGroupId();
                String userId = link.getUserId();
                if (StringUtils.isNotEmpty(groupId)) {
                    ids.add(BaseSillyTaskGroupHandle.GROUP_USER_ID_PREFIX + groupId);
                } else if (StringUtils.isNotEmpty(userId)) {
                    ids.add(userId);
                }
            }
        }
        return ids;
    }

    @Override
    public void endProcessByProcessInstanceId(String processInstanceId, String userId) {
        List<SillyActivitiTask> tasks = findTaskByProcessInstanceId(processInstanceId);
        if (!tasks.isEmpty()) {
            endProcessByProcessInstanceId(processInstanceId, tasks.get(0), userId);
        }
    }

    public void endProcessByProcessInstanceId(String actProcessId, SillyActivitiTask task, String userId) {
        if (task != null) {
            changeTask(task, SillyConstant.ActivitiNode.KEY_END, userId);
        }
        if (actProcessId != null) {
            List<SillyActivitiTask> tasks = findTaskByProcessInstanceId(actProcessId);
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
        final ReadOnlyProcessDefinition deployedProcessDefinition = ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);
        if (deployedProcessDefinition != null) {
            return deployedProcessDefinition.getKey();
        }
        return null;
    }

    // ======================================= 工作流服务 内部方法 =============================================

    private ActivityImpl findActivitiImpl(String taskId, String nodeKey) {
        // 取得流程定义 11
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

        // 获取当前活动节点ID
        if (StringUtils.isEmpty(nodeKey)) {
            nodeKey = findTaskById(taskId).getTaskDefinitionKey();
        }
        // 根据节点ID，获取对应的活动节点
        return processDefinition.findActivity(nodeKey);
    }

    private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) {
        // 取得流程定义
        return (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(taskService.createTaskQuery().taskId(taskId).singleResult()
                        .getProcessDefinitionId());
    }

    private Map<String, Object> makeUserVarMap(ActivityImpl pointActivity, String userId) {
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
    }

    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        List<PvmTransition> oriPvmTransitionList = new ArrayList<>(pvmTransitionList);
        pvmTransitionList.clear();
        return oriPvmTransitionList;
    }

    private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        pvmTransitionList.addAll(oriPvmTransitionList);
    }

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


    public List<HistoricProcessInstance> findProcessInstanceByMasterId(String masterId) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(masterId).list();
    }

    @Override
    public List<MySillyMasterTask> findMyTaskByMasterId(String category, String userId, String masterId, Set<String> allGroupId) {
        return getMyDoingMasterTaskId(category, userId, masterId, allGroupId);
    }

    @Override
    public List<SillyActivitiTask> findTaskByMasterId(String masterId) {
        final List<HistoricProcessInstance> processInstances = findProcessInstanceByMasterId(masterId);
        List<SillyActivitiTask> taskList = new ArrayList<>();
        for (HistoricProcessInstance processInstance : processInstances) {
            taskList.addAll(findTaskByProcessInstanceId(processInstance.getId()));
        }
        return taskList;
    }


    @Override
    public void changeUser(String taskId, String userId) {
        taskService.setAssignee(taskId, userId);
    }

    @Override
    public void addUser(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
    }

    @Override
    public void deleteUser(String taskId, String userId) {
        taskService.deleteCandidateUser(taskId, userId);
    }

    @Override
    public void deleteTask(String taskId) {
        taskService.deleteTask(taskId);
    }
}
