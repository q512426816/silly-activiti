/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.base.core;

import com.iqiny.silly.core.base.SillyEntity;

import java.util.Date;

/**
 * 傻瓜工作流 主表接口
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyMaster extends SillyEntity {

    /**
     * 主键
     */
    String getId();

    void setId(String id);

    /**
     * 流程实例ID
     */
    String getProcessId();

    void setProcessId(String processId);

    /**
     * 主表状态
     */
    String getStatus();

    void setStatus(String status);

    /**
     * 设置任务名称（多个去重 , 拼接），显示方便
     */
    String getTaskName();

    void setTaskName(String status);

    /**
     * 开启时间
     */
    Date getStartDate();

    void setStartDate(Date date);

    /**
     * 开启人
     */
    String getStartUserId();

    void setStartUserId(String userId);

    /**
     * 关闭时间
     */
    Date getCloseDate();

    void setCloseDate(Date date);

    /**
     * 关闭人
     */
    String getCloseUserId();

    void setCloseUserId(String userId);

    /**
     * 获取流程KEY
     */
    String processKey();

    /**
     * 获取流程版本
     */
    String processVersion();

    /**
     * 获取 主表启动的状态值
     */
    String startStatus();

    /**
     * 获取 主表进行中的状态值
     */
    String doingStatus();

    /**
     * 获取 主表结束的状态值
     */
    String endStatus();

    /**
     * 设置下一步处置人名称（多个去重 , 拼接），方便显示
     */
    String getHandleUserName();

    void setHandleUserName(String userName);
}
