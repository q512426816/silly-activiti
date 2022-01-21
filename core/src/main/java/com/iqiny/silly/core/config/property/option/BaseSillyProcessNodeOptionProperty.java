/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.option;

import com.alibaba.fastjson.annotation.JSONField;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;

public abstract class BaseSillyProcessNodeOptionProperty implements SillyProcessNodeOptionProperty {

    @JSONField(serialize = false)
    protected SillyPropertyHandle handle;

    protected boolean handleValue(String value, boolean defaultValue) {
        if (handle == null) {
            return defaultValue;
        }
        return handle.getBooleanValue(value);
    }

    protected String handleValue(String value) {
        if (handle == null) {
            return value;
        }
        return handle.getStringValue(value);
    }

    @Override
    public void setHandle(SillyPropertyHandle handle) {
        this.handle = handle;
    }
}
