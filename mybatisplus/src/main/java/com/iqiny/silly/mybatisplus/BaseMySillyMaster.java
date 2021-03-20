/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.iqiny.silly.core.base.core.SillyMaster;

import java.util.Date;

/**
 * SillyMaster 集成MybatisPlus
 *
 * @param <T>
 */
public abstract class BaseMySillyMaster<T extends Model<T>> extends BaseMyBaseEntity<T> implements SillyMaster {

    protected String processKey;
    protected String processVersion;
    protected String processId;
    protected String status;
    protected String startUserId;
    protected Date startDate;
    protected String closeUserId;
    protected Date closeDate;
    protected String taskName;
    protected String handleUserName;


    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

    @Override
    public String getProcessId() {
        return processId;
    }

    @Override
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStartUserId() {
        return startUserId;
    }

    @Override
    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getCloseUserId() {
        return closeUserId;
    }

    @Override
    public void setCloseUserId(String closeUserId) {
        this.closeUserId = closeUserId;
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCloseDate() {
        return closeDate;
    }

    @Override
    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }

    @Override
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String getHandleUserName() {
        return handleUserName;
    }

    @Override
    public void setHandleUserName(String handleUserName) {
        this.handleUserName = handleUserName;
    }
}
