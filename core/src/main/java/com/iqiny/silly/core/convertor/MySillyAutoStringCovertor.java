/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.convertor;

import com.iqiny.silly.common.util.StringUtils;

public abstract class MySillyAutoStringCovertor extends SillyStringConvertor implements SillyAutoConvertor {

    @Override
    public boolean canConvertor(String fieldName, Object value) {
        if (StringUtils.isEmpty(fieldName)) {
            return false;
        }
        return fieldName.endsWith(fieldNameEndWith());
    }

    /**
     * 已特点字符结尾的 作为自动转换依据
     */
    protected abstract String fieldNameEndWith();
}
