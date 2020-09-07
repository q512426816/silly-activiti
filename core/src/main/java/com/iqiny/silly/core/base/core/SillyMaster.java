package com.iqiny.silly.core.base.core;

import java.util.Date;

/**
 * 傻瓜工作流 主表接口
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyMaster {

    /**
     * 主键
     */
    String getId();

    void setId(String id);

    /**
     * 流程实例ID
     */
    String getActProcessId();

    void setActProcessId(String actProcessId);

    /**
     * 主表状态
     */
    String getStatus();

    void setStatus(String status);

    /**
     * 关闭时间
     */
    void setCloseDate(Date date);

    /**
     * 关闭人
     */
    void setCloseUserId(String userId);

    /**
     * 插入前的操作
     */
    void preInsert();

    /**
     * 获取流程KEY
     */
    String actProcessKey();

    /**
     * 获取流程版本
     */
    String getActVersion();

    /**
     * 获取 主表启动的状态值
     */
    String getStartStatus();

    /**
     * 获取 主表进行中的状态值
     */
    String getDoingStatus();

    /**
     * 获取 主表结束的状态值
     */
    String getEndStatus();

}
