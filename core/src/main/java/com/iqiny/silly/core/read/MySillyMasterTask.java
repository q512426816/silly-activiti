/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.read;

import com.iqiny.silly.core.base.SillyMasterTask;

import java.util.Map;

public class MySillyMasterTask implements SillyMasterTask {

    private String category;
    private String masterId;
    private String taskId;
    private Map<String, Object> params;

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getMasterId() {
        return masterId;
    }

    @Override
    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
