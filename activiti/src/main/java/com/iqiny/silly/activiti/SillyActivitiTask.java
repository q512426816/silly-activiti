/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.engine.SillyTask;
import org.activiti.engine.task.Task;

public class SillyActivitiTask implements SillyTask {

    private String id;
    private String executionId;
    private String processInstanceId;
    private String name;
    private String taskDefinitionKey;
    private String assignee;
    private Task task;


    public SillyActivitiTask(Task task) {
        SillyAssert.notNull(task, "任务对象不可为空");

        this.id = task.getId();
        this.executionId = task.getExecutionId();
        this.processInstanceId = task.getProcessInstanceId();
        this.name = task.getName();
        this.taskDefinitionKey = task.getTaskDefinitionKey();
        this.assignee = task.getAssignee();
        this.task = task;

    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    @Override
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
