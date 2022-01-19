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
 * 主表ID
 */
@Component
public class SillyMasterIdValuesField implements SillyPropertyValuesField {

    public static final String NAME = "masterId";

    @Override
    public String fieldName() {
        return NAME;
    }

    @Override
    public Object value(SillyNodeSourceData data) {
        return data.masterId();
    }
}
