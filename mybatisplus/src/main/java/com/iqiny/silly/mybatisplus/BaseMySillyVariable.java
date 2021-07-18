/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.iqiny.silly.core.base.core.SillyVariable;

/**
 * SillyVariable 集成MybatisPlus
 *
 * @param <T>
 */
public abstract class BaseMySillyVariable<T extends Model<T>> extends BaseMyBaseEntity<T> implements SillyVariable {

    protected String nodeId;
    protected String masterId;

    protected String taskId;
    protected String nodeKey;
    protected String status;

    protected String activitiHandler;

    protected String variableType;
    protected String variableName;
    protected String variableText;
    protected String belong;

    @Override
    public String getNodeId() {
        return nodeId;
    }

    @Override
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String getMasterId() {
        return masterId;
    }

    @Override
    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getNodeKey() {
        return nodeKey;
    }

    @Override
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getActivitiHandler() {
        return activitiHandler;
    }

    @Override
    public void setActivitiHandler(String activitiHandler) {
        this.activitiHandler = activitiHandler;
    }

    @Override
    public String getVariableType() {
        return variableType;
    }

    @Override
    public void setVariableType(String variableType) {
        this.variableType = variableType;
    }

    @Override
    public String getVariableName() {
        return variableName;
    }

    @Override
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public String getVariableText() {
        return variableText;
    }

    @Override
    public void setVariableText(String variableText) {
        this.variableText = variableText;
    }

    @Override
    public String getBelong() {
        return belong;
    }

    @Override
    public void setBelong(String belong) {
        this.belong = belong;
    }
    
}
