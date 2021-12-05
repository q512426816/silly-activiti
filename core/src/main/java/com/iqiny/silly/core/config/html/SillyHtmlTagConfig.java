/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.html;

import java.util.List;
import java.util.Map;

/**
 * 傻瓜变量 对应的 html 标签内属性 配置
 */
public interface SillyHtmlTagConfig {

    String getTagName();

    void setTagName(String tagName);

    String getFieldName();

    void setFieldName(String fieldName);

    String getLabel();

    void setLabel(String label);

    String getDesc();

    void setDesc(String desc);

    Boolean isRequest();

    void setRequest(Boolean request);

    String getRequestDesc();

    void setRequestDesc(String requestDesc);

    Map<String, String> getAttrConfigs();

    void setAttrConfigs(Map<String, String> attrConfigs);

    Map<String, Object> getParams();
}
