/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.spel.field;

import com.iqiny.silly.core.config.property.valuesfield.SillyPropertyValuesField;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import org.springframework.stereotype.Component;

/**
 * 业务分类
 */
@Component
public class SillyCategoryValuesField implements SillyPropertyValuesField {

    public static final String NAME = "category";

    @Override
    public String fieldName() {
        return NAME;
    }

    @Override
    public Object value(SillyNodeSourceData data) {
        return data.getCategory();
    }
}
