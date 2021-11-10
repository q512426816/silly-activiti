/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property;

import java.util.Map;

/**
 * 傻瓜流程信息属性配置
 */
public interface SillyProcessProperty<MP extends SillyProcessMasterProperty> {

    /**
     * 流程业务分类
     */
    String getCategory();

    void setCategory(String category);
    /**
     * 最新流程Key
     */
    String getLastProcessKey();

    void setLastProcessKey(String lastProcessKey);
    /**
     * 最新流程版本号
     */
    String getLastProcessVersion();

    void setLastProcessVersion(String lastProcessVersion);
    /**
     * 流程描述
     */
    String getProcessDesc();

    void setProcessDesc(String processDesc);

    /**
     * 流程KEY： 对应的配置参数
     */
    Map<String, MP> getMaster();
}
