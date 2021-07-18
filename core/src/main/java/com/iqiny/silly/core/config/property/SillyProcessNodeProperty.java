package com.iqiny.silly.core.config.property;

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
     * 此节点是否为并行节点
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
}
