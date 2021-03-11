package com.iqiny.silly.core.base.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 傻瓜工作流 节点接口 （每个任务对应一个Node）
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyNode<V extends SillyVariable> {

    /**
     * 数据ID
     */
    String getId();

    void setId(String id);

    /**
     * Master ID
     */
    String getMasterId();

    void setMasterId(String masterId);

    /**
     * 节点执行的顺序 不可重复 从小到大排序
     */
    int getSeq();

    void setSeq(int seq);

    /**
     * 节点Key
     */
    String getNodeKey();

    void setNodeKey(String nodeKey);

    /**
     * 任务ID
     */
    String getTaskId();

    void setTaskId(String taskId);

    /**
     * 并行标记
     */
    String getParallelFlag();

    void setParallelFlag(String parallelFlag);

    /**
     * 节点执行时间
     */
    Date getNodeDate();

    void setNodeDate(Date nodeDate);

    /**
     * 节点执行人
     */
    String getNodeUserId();

    void setNodeUserId(String nodeDate);

    /**
     * 状态
     */
    String getStatus();

    void setStatus(String status);

    /**
     * 节点流程信息，一般用于履历记录
     */
    String getNodeInfo();

    void setNodeInfo(String nodeInfo);

    /**
     * 变量集合
     */
    List<V> getVariableList();

    void setVariableList(List<V> variableList);

    /**
     * 变量Map
     */
    Map<String, Object> getVariableMap();

    void setVariableMap(Map<String, Object> variableMap);

    /**
     * 处置类型 （启动/下一步/驳回/流转/完成）
     *
     * @return
     */
    String getHandleType();

}
