/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.html;

import com.iqiny.silly.core.config.property.SillyProcessVariableProperty;

/**
 * 傻瓜标签自动
 */
public interface SillyHtmlAutoTag {

    /**
     * 此Tag 是否支持此 variable
     *
     * @param variableProperty
     */
    boolean support(SillyProcessVariableProperty variableProperty);

}
