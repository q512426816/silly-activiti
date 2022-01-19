/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.valuesfield;

import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 傻瓜解析器  values 属性对象
 */
public interface SillyPropertyValuesField {

    String fieldName();

    Object value(SillyNodeSourceData data);

}
