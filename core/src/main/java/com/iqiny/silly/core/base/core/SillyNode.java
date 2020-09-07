package com.iqiny.silly.core.base.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 傻瓜工作流 节点接口 （对应流程图节点）
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyNode<V extends SillyVariable> {

    String getId();

    void setId(String id);
    
    String getParallelFlag();

    void setParallelFlag(String parallelFlag);

    String getNodeKey();

    void setNodeKey(String masterId);

    Date getProcessDate();

    String getTaskId();

    String getMasterId();

    void setTaskId(String taskId);

    void setMasterId(String id);

    void setStatus(String statusCurrent);

    List<V> getVariableList();

    Map<String,Object> getVariableMap();

    String getProcessInfo();
}
