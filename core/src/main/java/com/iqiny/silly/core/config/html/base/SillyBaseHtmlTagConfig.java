/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.html.base;

import com.iqiny.silly.core.config.html.SillyHtmlTagConfig;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SillyBaseHtmlTagConfig implements SillyHtmlTagConfig {

    protected String tagName;
    protected String fieldName;
    protected String label;
    protected String desc;
    protected Boolean request;
    protected String requestDesc;
    protected Map<String, Object> params = new LinkedHashMap<>();

    protected Map<String, String> attrConfigs = new LinkedHashMap<>();


    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public Boolean isRequest() {
        return request;
    }

    @Override
    public void setRequest(Boolean request) {
        this.request = request;
    }

    @Override
    public String getRequestDesc() {
        return requestDesc;
    }

    @Override
    public void setRequestDesc(String requestDesc) {
        this.requestDesc = requestDesc;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Map<String, String> getAttrConfigs() {
        return attrConfigs;
    }

    @Override
    public void setAttrConfigs(Map<String, String> attrConfigs) {
        this.attrConfigs = attrConfigs;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Boolean getRequest() {
        return request;
    }
}
