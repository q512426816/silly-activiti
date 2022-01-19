/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.spel.field;

import com.iqiny.silly.common.util.SillyDateUtils;
import com.iqiny.silly.core.config.property.valuesfield.BaseSillyMethodPropertyValuesField;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 格式化 Date
 */
@Component
public class SillyFormatDateValuesField extends BaseSillyMethodPropertyValuesField {

    public static final String NAME = "formatDate";

    @Override
    public String fieldName() {
        return NAME;
    }

    @Override
    protected Class<?>[] getParameterTypes(SillyNodeSourceData data) {
        return new Class[]{Date.class, String.class};
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }

        return SillyDateUtils.formatDate(date, pattern);
    }
}
