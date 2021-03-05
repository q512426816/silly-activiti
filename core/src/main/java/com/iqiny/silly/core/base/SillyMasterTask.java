package com.iqiny.silly.core.base;

/**
 * 主表与任务表关系对象
 */
public interface SillyMasterTask {

    String getMasterId();

    void setMasterId(String masterId);

    String getTaskId();

    void setTaskId(String taskId);
}
