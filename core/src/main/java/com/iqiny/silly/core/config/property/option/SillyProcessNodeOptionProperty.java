/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.option;

import com.iqiny.silly.core.config.property.SillyPropertyHandle;

/**
 * 傻瓜流程节点 操作配置
 */
public interface SillyProcessNodeOptionProperty {

    /**
     * 操作Key值
     */
    String getKey();

    /**
     * 操作名称
     */
    String getName();

    /**
     * 是否隐藏此操作
     */
    boolean isHide();

    /**
     * 是否隐藏此操作 EL表达式
     */
    String getHideEl();

    /**
     * 操作触发 url
     */
    String getUrl();

    /**
     * 解析器
     */
    void setHandle(SillyPropertyHandle handle);

}
