package com.iqiny.silly.activiti;

import com.iqiny.silly.common.util.CurrentUserUtil;
import com.iqiny.silly.common.util.StringUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Activiti流程引擎服务
*/
public class ActivitiServiceImpl implements SillyActivitiService {

    @Autowired
    private CurrentUserUtil currentUserUtil;

    protected ProcessEngineFactoryBean processEngineFactoryBean;

    protected RuntimeService runtimeService;
    protected HistoryService historyService;
    protected TaskService taskService;
    protected RepositoryService repositoryService;


    public ProcessEngineFactoryBean getProcessEngineFactoryBean() {
        return processEngineFactoryBean;
    }

    @Autowired
    public void setProcessEngineFactoryBean(ProcessEngineFactoryBean processEngineFactoryBean) {
        this.processEngineFactoryBean = processEngineFactoryBean;
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
    public void complete(String taskId, Map<String, Object> variableMap) {
        if (StringUtils.isEmpty(taskId)) {
            throw new RuntimeException("当前执行的任务ID获取失败");
        }
        String currentUserId = currentUserUtil.currentUserId();
        taskService.claim(taskId, currentUserId);
        //完成任务
        taskService.complete(taskId, variableMap);
    }

    @Override
    public List<Task> findTaskList(String actProcessId) {
        if (StringUtils.isEmpty(actProcessId)) {
            throw new RuntimeException("查询任务列表，流程实例ID不可为空！");
        }
        return taskService.createTaskQuery().processInstanceId(actProcessId).list();
    }

    @Override
    public List<String> getTaskUserIds(String taskId) {
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
    public String getBusinessKey(String processInstanceId) {
        // TODO 此处可采用缓存提升效率
        final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        return historicProcessInstance.getBusinessKey();
    }

    @Override
    public ProcessInstance start(String key, String id, Map<String, Object> variableMap) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("流程启动时流程KEY 不可为空！");
        }
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("流程启动时 业务主键 不可为空！");
        }
        return runtimeService.startProcessInstanceByKey(key, id, variableMap);
    }

    @Override
    public ProcessInstance getProcessInstance(String actProcessId) {
        if (StringUtils.isEmpty(actProcessId)) {
            return null;
        }
        return runtimeService.createProcessInstanceQuery().processInstanceId(actProcessId).singleResult();
    }

    @Override
    public void endProcessByActId(String actProcessId) {
        List<Task> tasks = findTaskList(actProcessId);
        if (!tasks.isEmpty()) {
            endProcessByActId(actProcessId, tasks.get(0).getId());
        }
    }

    @Override
    public void endProcessByActId(String actProcessId, String taskId) {
        if (taskId != null) {
            changeProcessTask(taskId, "end");
        }
        if (actProcessId != null) {
            List<Task> tasks = findTaskList(actProcessId);
            if (!tasks.isEmpty()) {
                endProcessByActId(actProcessId, tasks.get(0).getId());
            }
        }
    }

    /**
     * @return
     * @Description 任务流程节点改变
     * @Author qinyi
     * @Date 2018/12/26 16:59
     * @Param
     **/
    @Override
    public void changeProcessTask(String taskId, String activityId) {
        changeTask(taskId, activityId, "");
    }

    /**
     * @return
     * @Description 任务流程节点改变
     * @Author qinyi
     * @Date 2018/12/26 16:59
     * @Param
     **/
    @Override
    public void changeTask(String taskId, String activityId, String userId) {
        if (taskId == null) {
            throw new RuntimeException("任务ID 不可为空！");
        }
        // 目标节点
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
        Map<String, Object> value = makeUserVarMap(pointActivity, userId);
        value.put("finish", "1");
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
        pointActivity.getIncomingTransitions().remove(newTransition);
        // 还原以前流向
        restoreTransition(currActivity, oriPvmTransitionList);
    }


    @Override
    public Map<String, Object> makeUserVarMap(ActivityImpl pointActivity, Object userId) {
        if (pointActivity != null && pointActivity.getActivityBehavior() instanceof UserTaskActivityBehavior) {
            // 设置节点处置人信息
            UserTaskActivityBehavior behavior = (UserTaskActivityBehavior) pointActivity.getActivityBehavior();
            Set<Expression> set = behavior.getTaskDefinition().getCandidateUserIdExpressions();
            Map<String, Object> value = new HashMap<>();
            for (Expression expression : set) {
                String et = expression.getExpressionText();
                if (et.length() > 3) {
                    String userKey = et.substring(2, et.length() - 1);
                    value.put(userKey, userId);
                }
            }
            return value;
        }
        return new HashMap<>();
    }

    /**
     * 根据任务ID和节点ID获取活动节点 <br>
     *
     * @param taskId     任务ID
     * @param activityId 活动节点ID <br>
     *                   如果为null或""，则默认查询当前活动节点 <br>
     *                   如果为"end"，则查询结束节点 <br>
     * @return
     */
    @Override
    public ActivityImpl findActivitiImpl(String taskId, String activityId) {
        // 取得流程定义 11
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

        // 获取当前活动节点ID
        if (activityId == null || "".equals(activityId)) {
            activityId = findTaskById(taskId).getTaskDefinitionKey();
        }
        // 根据节点ID，获取对应的活动节点
        return processDefinition.findActivity(activityId);
    }

    /**
     * 根据任务ID获取流程定义
     *
     * @param taskId 任务ID
     * @return
     */
    private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) {
        // 取得流程定义
        return (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(findTaskById(taskId)
                        .getProcessDefinitionId());
    }


    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId 任务ID
     * @return
     */
    @Override
    public TaskEntity findTaskById(String taskId) {
        return (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    /**
     * 清空指定活动节点流向
     *
     * @param activityImpl 活动节点
     * @return 节点流向集合
     */
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        List<PvmTransition> oriPvmTransitionList = new ArrayList<>(pvmTransitionList);
        pvmTransitionList.clear();
        return oriPvmTransitionList;
    }

    /**
     * 还原指定活动节点流向
     *
     * @param activityImpl         活动节点
     * @param oriPvmTransitionList 原有节点流向集合
     */
    private void restoreTransition(ActivityImpl activityImpl,
                                   List<PvmTransition> oriPvmTransitionList) {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        pvmTransitionList.addAll(oriPvmTransitionList);
    }

    @Override
    public String getTaskIdByProcessId(String actProcessId) {
        if (StringUtils.isEmpty(actProcessId)) {
            return null;
        }
        return taskService.createTaskQuery().processInstanceId(actProcessId).singleResult().getId();
    }

    @Override
    public String getActKeyNameByActProcessId(String actProcessId) {
        final String processDefinitionId = getProcessDefinitionIdByActProcessId(actProcessId);
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

    @Override
    public String getProcessDefinitionIdByActProcessId(String executionId) {
        if (StringUtils.isEmpty(executionId)) {
            return null;
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
        String processDefinitionId = "";
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(executionId).singleResult();
            processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        } else {
            processDefinitionId = processInstance.getProcessDefinitionId();
        }
        return processDefinitionId;
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
            return "end";
        }
    }
}
