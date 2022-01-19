package com.iqiny.silly.core.config.property.option;

/**
 * 傻瓜流程节点 操作配置 
 */
public interface SillyProcessNodeOptionProperty {

    /**
     * 操作Key值
     */
    String key();

    /**
     * 操作名称
     */
    String name();

    /**
     * 是否隐藏此操作
     */
    boolean hide();

    /**
     * 是否隐藏此操作 EL表达式
     */
    String hideEl();

    /**
     * 操作触发 url
     */
    String url();

}
