/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.ncr.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.entity.MySillyMaster;

@TableName("silly_ncr_master")
public class NcrMaster extends MySillyMaster<NcrMaster> {

    /**
     * 流程分类
     */
    public static final String CATEGORY = "NCR";

    @TableField(exist = false)
    private String processKey;
    @TableField(exist = false)
    private String processVersion;

    @Override
    public String getTaskName() {
        return null;
    }

    @Override
    public void setTaskName(String status) {

    }

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

    @Override
    public String getHandleUserName() {
        return null;
    }

    @Override
    public void setHandleUserName(String userName) {

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

    @Override
    public String category() {
        return NcrMaster.CATEGORY;
    }
}
