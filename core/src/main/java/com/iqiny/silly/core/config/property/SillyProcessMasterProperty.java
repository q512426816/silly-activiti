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

public interface SillyProcessMasterProperty<NP extends SillyProcessNodeProperty> {

    /**
     * 最新流程Key
     */
    String getProcessKey();

    /**
     * 最新流程版本号
     */
    String getProcessVersion();

    /**
     * 流程描述
     */
    String getProcessDesc();

    /**
     * 属性名称： 对应的配置参数
     */
    Map<String, NP> getNode();

    void setProcessKey(String key);
}
