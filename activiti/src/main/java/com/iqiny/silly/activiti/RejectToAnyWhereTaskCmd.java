/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti;

import com.iqiny.silly.common.util.StringUtils;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * activiti 任务驳回任意节点 CMD
 */
public class RejectToAnyWhereTaskCmd implements Command<Void> {

    public static final String EVENT_NAME = "reject";

    /**
     * 当前执行ID
     */
    protected String executionId;
    /**
     * 目标节点ID (nodeKey)
     */
    protected ActivityImpl desActivity;
    /**
     * 流程变量
     */
    protected Map<String, Object> varMap;
    /**
     * 当前节点对象
     */
    protected ActivityImpl currentActivity;
    /**
     * 流程定义对象
     */
    protected ReadOnlyProcessDefinition processDefinitionEntity;

    public RejectToAnyWhereTaskCmd(String executionId, ActivityImpl desActivity,
                                   Map<String, Object> varMap, ActivityImpl currentActivity,
                                   ReadOnlyProcessDefinition processDefinitionEntity) {
        this.executionId = executionId;
        this.desActivity = desActivity;
        this.varMap = varMap;
        this.currentActivity = currentActivity;
        this.processDefinitionEntity = processDefinitionEntity;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = Context.getCommandContext().getExecutionEntityManager();
        ExecutionEntity executionEntity = executionEntityManager.findExecutionById(executionId);
        executionEntity.setVariables(varMap);
        executionEntity.setEventSource(this.currentActivity);
        executionEntity.setActivity(this.currentActivity);

        String parentId = executionEntity.getParentId();
        List<ExecutionEntity> executionEntitys = new ArrayList<>();
        if (StringUtils.isNotEmpty(parentId)) {
            executionEntitys = executionEntityManager.findChildExecutionsByParentExecutionId(parentId);
        }
        if (executionEntitys.isEmpty()) {
            // 非子流程、非多实例
            List<TaskEntity> tasksByExecutionId = Context.getCommandContext()
                    .getTaskEntityManager()
                    .findTasksByExecutionId(this.executionId);
            for (TaskEntity taskEntity : tasksByExecutionId) {
                // 触发任务监听
                taskEntity.fireEvent(TaskListener.EVENTNAME_COMPLETE);
                // 删除任务的原因
                Context.getCommandContext().getTaskEntityManager()
                        .deleteTask(taskEntity, TaskEntity.DELETE_REASON_COMPLETED, false);
            }
            executionEntity.executeActivity(this.desActivity);
        } else {
            // 子流程/多实例
            for (ExecutionEntity execution : executionEntitys) {
                List<TaskEntity> tasksByExecutionId = Context.getCommandContext()
                        .getTaskEntityManager()
                        .findTasksByExecutionId(execution.getId());
                // 子任务全部进行驳回操作（触发驳回事件 并删除任务）
                for (TaskEntity taskEntity : tasksByExecutionId) {
                    // 触发任务监听
                    taskEntity.fireEvent(EVENT_NAME);
                    // 删除任务的原因
                    Context.getCommandContext().getTaskEntityManager()
                            .deleteTask(taskEntity, "已自动驳回", false);
                }
                execution.remove();
            }
            String executionParentId = executionEntity.getParentId();
            ExecutionEntity parentExecutionEntity = executionEntityManager.findExecutionById(executionParentId);
            ActivityImpl pActivityImpl = (ActivityImpl) this.processDefinitionEntity.findActivity(parentExecutionEntity.getActivityId());
            executionEntity.setVariables(varMap);
            executionEntity.setEventSource(pActivityImpl);
            executionEntity.setActivity(pActivityImpl);
            parentExecutionEntity.executeActivity(this.desActivity);
        }
        return null;
    }

}