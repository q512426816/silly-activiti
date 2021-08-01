/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.impl;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.core.config.SillyConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.config.html.SillyHtmlTagTemplate;
import com.iqiny.silly.core.config.html.base.SillyBaseHtmlTagConfig;
import com.iqiny.silly.core.config.property.SillyProcessVariableProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;


public class DefaultProcessVariableProperty implements SillyProcessVariableProperty<SillyBaseHtmlTagConfig> {

    private String desc;
    private String requestEl = SillyConstant.YesOrNo.YES;
    private boolean request = true;
    private String activitiHandler;
    private String variableType;
    private String variableName;
    private String defaultText;
    private String belong;

    private String htmlType;
    private SillyBaseHtmlTagConfig htmlConfig;
    private SillyHtmlTagTemplate tagTemplate;


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

    @Override
    public String getHtmlType() {
        return htmlType;
    }

    @Override
    public void setHtmlType(String htmlType) {
        this.htmlType = htmlType;
    }

    @Override
    public SillyBaseHtmlTagConfig getHtmlConfig() {
        return htmlConfig;
    }

    @Override
    public void setHtmlConfig(SillyBaseHtmlTagConfig htmlConfig) {
        this.htmlConfig = htmlConfig;
    }

    @Override
    public SillyHtmlTagTemplate getHtmlTemplate() {
        return tagTemplate;
    }

    @Override
    public void setHtmlTemplate(SillyHtmlTagTemplate htmlTemplate) {
        this.tagTemplate = htmlTemplate;
    }

    @Override
    public String getHtml() {
        SillyHtmlTagTemplate htmlTemplate = getHtmlTemplate();
        return htmlTemplate == null ? null : htmlTemplate.getHtml(getHtmlConfig());
    }

    @Override
    public String getRequestEl() {
        return requestEl;
    }

    public void setRequestEl(String requestEl) {
        this.requestEl = requestEl;
    }
}
