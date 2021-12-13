/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property;

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
     * 数据处理保存的处理方法 （方法名称）
     */
    String[] getSaveHandleNames();

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

    /**
     * 是否更新此字段信息到处理器上下文中
     */
    boolean isUpdatePropertyHandleValue();

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

    /**
     * 设置父类
     *
     * @param property
     */
    void setParent(SillyProcessNodeProperty property);

    SillyProcessNodeProperty getParent();

}
