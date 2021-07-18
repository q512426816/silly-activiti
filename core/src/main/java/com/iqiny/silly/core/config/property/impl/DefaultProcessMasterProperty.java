package com.iqiny.silly.core.config.property.impl;


import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultProcessMasterProperty implements SillyProcessMasterProperty<DefaultProcessNodeProperty> {

    /**
     * 最新流程Key
     */
    private String processKey;
    /**
     * 最新流程版本号
     */
    private String processVersion;
    /**
     * 流程描述
     */
    private String processDesc;

    /**
     * 属性名称： 对应的配置参数
     */
    private final Map<String, DefaultProcessNodeProperty> node = new LinkedHashMap<>();

    @Override
    public String getProcessKey() {
        return processKey;
    }

    @Override
    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    @Override
    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

    @Override
    public String getProcessDesc() {
        return processDesc;
    }

    public void setProcessDesc(String processDesc) {
        this.processDesc = processDesc;
    }

    @Override
    public Map<String, DefaultProcessNodeProperty> getNode() {
        return node;
    }
}
