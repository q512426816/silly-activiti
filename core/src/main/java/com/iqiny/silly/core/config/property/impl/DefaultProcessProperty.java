/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.impl;

import com.iqiny.silly.core.config.property.SillyProcessProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 傻瓜流程信息属性配置
 */
public class DefaultProcessProperty implements SillyProcessProperty<DefaultProcessMasterProperty> {

    /**
     * 流程业务分类
     */
    private String category;
    /**
     * 最新流程Key
     */
    private String lastProcessKey;
    /**
     * 最新流程版本号
     */
    private String lastProcessVersion;
    /**
     * 第一个节点Key
     */
    private String firstNodeKey;
    /**
     * 流程描述
     */
    private String processDesc;

    /**
     * 流程KEY： 对应的配置参数
     */
    private final Map<String, DefaultProcessMasterProperty> master = new LinkedHashMap<>();

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getLastProcessKey() {
        return lastProcessKey;
    }

    @Override
    public void setLastProcessKey(String lastProcessKey) {
        this.lastProcessKey = lastProcessKey;
    }

    @Override
    public String getLastProcessVersion() {
        return lastProcessVersion;
    }

    @Override
    public void setLastProcessVersion(String lastProcessVersion) {
        this.lastProcessVersion = lastProcessVersion;
    }

    @Override
    public String getProcessDesc() {
        return processDesc;
    }

    @Override
    public void setProcessDesc(String processDesc) {
        this.processDesc = processDesc;
    }

    @Override
    public Map<String, DefaultProcessMasterProperty> getMaster() {
        return master;
    }

    @Override
    public String getFirstNodeKey() {
        return firstNodeKey;
    }

    @Override
    public void setFirstNodeKey(String firstNodeKey) {
        this.firstNodeKey = firstNodeKey;
    }
}
