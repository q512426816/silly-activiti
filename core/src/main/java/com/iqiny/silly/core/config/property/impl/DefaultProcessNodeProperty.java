/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.impl;

import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DefaultProcessNodeProperty implements SillyProcessNodeProperty<DefaultProcessVariableProperty> {

    /**
     * 是否允许其他未定义的变量进行操作
     */
    private boolean allowOtherVariable;

    /**
     * 若存在其他未定义变量，是否抛出异常， 否则 忽略
     */
    private boolean otherVariableThrowException = true;

    /**
     * 此节点是否为并行节点
     */
    private boolean isParallel;

    /**
     * 节点 ID
     */
    private String nodeKey;

    /**
     * 节点名称
     */
    private String nodeName;

    private List<String> ignoreFieldNames;


    /**
     * 属性名称： 对应的配置参数
     */
    private final Map<String, DefaultProcessVariableProperty> variable = new LinkedHashMap<>();


    @Override
    public boolean isAllowOtherVariable() {
        return allowOtherVariable;
    }

    public void setAllowOtherVariable(boolean allowOtherVariable) {
        this.allowOtherVariable = allowOtherVariable;
    }

    @Override
    public boolean isOtherVariableThrowException() {
        return otherVariableThrowException;
    }

    public void setOtherVariableThrowException(boolean otherVariableThrowException) {
        this.otherVariableThrowException = otherVariableThrowException;
    }

    @Override
    public boolean isParallel() {
        return isParallel;
    }

    public void setParallel(boolean parallel) {
        isParallel = parallel;
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
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public Map<String, DefaultProcessVariableProperty> getVariable() {
        return variable;
    }

    public List<String> getIgnoreFieldNames() {
        return ignoreFieldNames;
    }

    public void setIgnoreFieldNames(List<String> ignoreFieldNames) {
        this.ignoreFieldNames = ignoreFieldNames;
    }

    @Override
    public boolean ignoreField(String fieldName) {
        List<String> ignoreFieldNames = getIgnoreFieldNames();
        return ignoreFieldNames != null && ignoreFieldNames.contains(fieldName);
    }
}
