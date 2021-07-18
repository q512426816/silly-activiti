package com.iqiny.silly.core.config.property;

public interface SillyProcessVariableProperty {

    /**
     * 变量描述
     */
    String getDesc();

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
}
