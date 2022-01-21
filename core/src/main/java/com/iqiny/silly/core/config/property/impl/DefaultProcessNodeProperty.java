/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.option.SillyProcessNodeOptionProperty;
import com.iqiny.silly.core.config.property.option.impl.*;

import java.util.*;


public class DefaultProcessNodeProperty implements SillyProcessNodeProperty<DefaultProcessVariableProperty> {


    private SillyProcessMasterProperty parent;
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
    @JSONField(parseFeatures = Feature.OrderedField, serialzeFeatures = SerializerFeature.MapSortField)
    private final Map<String, DefaultProcessVariableProperty> variable = new LinkedHashMap<>();

    /**
     * 操作KEY： 对应的配置参数
     */
    @JSONField(parseFeatures = Feature.OrderedField, serialzeFeatures = SerializerFeature.MapSortField)
    private final Map<String, Map<String, Object>> option = new LinkedHashMap<>();

    /**
     * 操作KEY： 对应的配置参数
     */
    @JSONField(serialize = false)
    private Map<String, SillyProcessNodeOptionProperty> sillyOption;

    /**
     * 操作KEY： className
     */
    @JSONField(parseFeatures = Feature.OrderedField, serialzeFeatures = SerializerFeature.MapSortField)
    private Map<String, String> optionClassNameMap = new HashMap<>();

    private String defaultOptionClassName = DefaultSillyProcessNodeOptionProperty.class.getName();

    {
        optionClassNameMap.putIfAbsent(SillyProcessNodeSaveOptionProperty.KEY, SillyProcessNodeSaveOptionProperty.class.getName());
        optionClassNameMap.putIfAbsent(SillyProcessNodeSumbitOptionProperty.KEY, SillyProcessNodeSumbitOptionProperty.class.getName());
        optionClassNameMap.putIfAbsent(SillyProcessNodeRejectOptionProperty.KEY, SillyProcessNodeRejectOptionProperty.class.getName());
        optionClassNameMap.putIfAbsent(SillyProcessNodeFlowOptionProperty.KEY, SillyProcessNodeFlowOptionProperty.class.getName());
    }

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

    @Override
    public SillyProcessMasterProperty getParent() {
        return parent;
    }

    @Override
    public void setParent(SillyProcessMasterProperty parent) {
        this.parent = parent;
    }

    @Override
    public Map<String, Map<String, Object>> getOption() {
        return option;
    }

    @Override
    public Map<String, SillyProcessNodeOptionProperty> getSillyOption() {
        if (sillyOption == null) {
            sillyOption = new LinkedHashMap<>();
            Set<Map.Entry<String, Map<String, Object>>> entries = option.entrySet();
            for (Map.Entry<String, Map<String, Object>> entry : entries) {
                String key = entry.getKey();
                Map<String, Object> value = entry.getValue();
                String className = SillyMapUtils.getString(value, "className");
                if (StringUtils.isEmpty(className)) {
                    className = optionClassNameMap.get(key);

                    if (StringUtils.isEmpty(className) && StringUtils.isNotEmpty(defaultOptionClassName)) {
                        value.putIfAbsent("key", key);
                        className = defaultOptionClassName;
                    }
                }

                SillyAssert.notEmpty(className, "操作配置ClassName 未设置" + key);
                String json = JSON.toJSONString(value);
                Class<SillyProcessNodeOptionProperty> nClass = SillyReflectUtil.classForName(className);
                SillyProcessNodeOptionProperty optionProperty = JSON.parseObject(json, nClass);
                sillyOption.put(optionProperty.getKey(), optionProperty);
            }
        }
        return sillyOption;
    }

}
