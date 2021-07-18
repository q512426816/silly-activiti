package com.iqiny.silly.core.config.property.impl;

import com.iqiny.silly.core.config.property.SillyProcessVariableProperty;

public class DefaultProcessVariableProperty implements SillyProcessVariableProperty {

    private String desc;
    private boolean request = true;
    private String activitiHandler;
    private String variableType;
    private String variableName;
    private String defaultText;
    private String belong;

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    @Override
    public String getActivitiHandler() {
        return activitiHandler;
    }

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
    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
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
