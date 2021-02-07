package com.iqiny.example.sillyactiviti.admin.modules.ncr.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.MySillyMaster;

@TableName("silly_ncr_master")
public class NcrMaster extends MySillyMaster<NcrMaster> {

    @TableField(exist = false)
    private String processKey;
    @TableField(exist = false)
    private String processVersion;

    @Override
    public String processKey() {
        return processKey;
    }

    @Override
    public String processVersion() {
        return processVersion;
    }

    @Override
    public String startStatus() {
        return "10";
    }

    @Override
    public String doingStatus() {
        return "30";
    }

    @Override
    public String endStatus() {
        return "90";
    }

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
}
