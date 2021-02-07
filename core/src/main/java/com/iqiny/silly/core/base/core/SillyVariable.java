package com.iqiny.silly.core.base.core;

/**
 * 傻瓜工作流 流程变量接口 （每个数据key：value对应一个Variable）
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyVariable {

    /**
     * 主键
     */
    String getId();

    void setId(String id);

    /**
     * 节点ID
     */
    String getNodeId();

    void setNodeId(String nodeId);

    /**
     * 主表ID
     */
    String getMasterId();

    void setMasterId(String masterId);

    /**
     * 任务ID
     */
    String getTaskId();

    void setTaskId(String taskId);

    /**
     * 节点Key
     */
    String getNodeKey();

    void setNodeKey(String nodeKey);

    /**
     * 数据状态
     */
    String getStatus();

    void setStatus(String status);

    /**
     * 变量类型
     */
    String getVariableType();

    void setVariableType(String variableType);

    /**
     * 变量属性名称
     */
    String getVariableName();

    void setVariableName(String variableName);

    /**
     * 变量值字符串
     */
    String getVariableText();

    void setVariableText(String variableText);

    /**
     * 流程引擎数据处置类型
     */
    String getActivitiHandler();

    void setActivitiHandler(String variableText);
}
