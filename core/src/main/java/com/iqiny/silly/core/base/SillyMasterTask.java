/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.base;

import java.util.Map;

/**
 * 主表与任务表关系对象
 */
public interface SillyMasterTask {

    String getCategory();

    void setCategory(String category);

    String getMasterId();

    void setMasterId(String masterId);

    String getTaskId();

    void setTaskId(String taskId);

    Map<String, Object> getParams();

    void setParams(Map<String, Object> params);
    
}
