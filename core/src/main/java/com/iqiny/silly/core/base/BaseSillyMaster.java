package com.iqiny.silly.core.base;

import com.iqiny.silly.core.base.core.SillyMaster;

import java.util.Date;

public abstract class BaseSillyMaster implements SillyMaster {

    protected String id;
    protected String processId;
    protected String status;
    protected String startUserId;
    protected Date startDate;
    protected String closeUserId;
    protected Date closeDate;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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
    public Date getCloseDate() {
        return closeDate;
    }

    @Override
    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }
}
