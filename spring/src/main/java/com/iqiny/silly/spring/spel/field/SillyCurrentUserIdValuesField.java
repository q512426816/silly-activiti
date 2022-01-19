/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.spel.field;

import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.config.property.valuesfield.SillyPropertyValuesField;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import org.springframework.stereotype.Component;

/**
 * 当前用户ID 信息属性
 */
@Component
public class SillyCurrentUserIdValuesField implements SillyPropertyValuesField {

    public static final String NAME = "currentUserId";

    @Override
    public String fieldName() {
        return NAME;
    }

    @Override
    public Object value(SillyNodeSourceData data) {
        String category = data.getCategory();
        return SillyConfigUtil.getSillyConfig(category).getSillyCurrentUserUtil().currentUserId();
    }

}
