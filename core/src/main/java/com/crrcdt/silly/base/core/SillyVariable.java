package com.crrcdt.silly.base.core;

/**
 * 傻瓜工作流 流程变量接口 （对应每个操作节点的具体内容）
 *
 * @author QINY
 */
public interface SillyVariable {

    String getId();

    void setId(String id);

    String getVariableLabel();

    String getVariableName();

    String getVariableText();

    void setTaskId(String taskId);

    void setMasterId(String masterId);

    void setNodeKey(String nodeKey);

    void setProcessId(String processId);

    void setStatus(String statusCurrent);

    String getActivitiHandler();
}
