/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property;

import com.iqiny.silly.core.config.property.option.SillyProcessNodeOptionProperty;

import java.util.Map;


public interface SillyProcessNodeProperty<VP extends SillyProcessVariableProperty> {

    /**
     * 是否允许其他未定义的变量进行操作
     */
    boolean isAllowOtherVariable();

    /**
     * 若存在其他未定义变量，是否抛出异常， 否则 忽略
     */
    default boolean isOtherVariableThrowException() {
        return true;
    }

    /**
     * 此节点是否为并行节点 (是否覆盖当前节点数据)
     */
    boolean isParallel();

    /**
     * 节点 ID
     */
    String getNodeKey();

    /**
     * 节点名称
     */
    String getNodeName();


    /**
     * 属性名称： 对应的配置参数
     */
    Map<String, VP> getVariable();

    void setNodeKey(String nodeKey);

    /**
     * 忽略此参数，不存储，不校验
     */
    boolean ignoreField(String fieldName);

    /**
     * 设置父类
     */
    void setParent(SillyProcessMasterProperty property);

    SillyProcessMasterProperty getParent();

    /**
     * 设置操作
     */
    Map<String, Map<String, Object>> getOption();

    /**
     * 设置操作
     */
    Map<String, SillyProcessNodeOptionProperty> getSillyOption();
    
}
