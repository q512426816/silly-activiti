/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.core.config.html.SillyHtmlTagConfig;
import com.iqiny.silly.core.config.html.SillyHtmlTagTemplate;


public interface SillyProcessVariableProperty<C extends SillyHtmlTagConfig> {

    /**
     * 变量描述
     */
    String getDesc();


    /**
     * 是否必填表达式
     */
    String getRequestEl();

    /**
     * 是否必须填写(默认 true)
     */
    default boolean isRequest() {
        return true;
    }

    /**
     * 流程类型，为空则不加入流程引擎内
     */
    String getActivitiHandler();

    /**
     * 变量类型
     */
    String getVariableType();

    /**
     * 变量名称
     */
    String getVariableName();

    /**
     * 默认值（字符串）
     */
    String getDefaultText();

    /**
     * 变量归属对象 master / node / variable
     */
    String getBelong();

    void setVariableName(String variableName);

    void setVariableType(String convertorString);

    void setBelong(String belongVariable);

    String getHtmlType();

    void setHtmlType(String htmlType);

    C getHtmlConfig();

    void setHtmlConfig(C htmlConfig);

    SillyHtmlTagTemplate getHtmlTemplate();

    void setHtmlTemplate(SillyHtmlTagTemplate htmlTemplate);

    String getHtml();
}
