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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class BaseSillyProcessNodeOptionProperty implements SillyProcessNodeOptionProperty {

    @JSONField(serialize = false)
    protected SillyPropertyHandle handle;

    protected boolean handleValue(String value, boolean defaultValue) {
        if (handle == null) {
            return defaultValue;
        }
        return handle.getBooleanValue(value);
    }

    protected <T> T handleValue(Object t) {
        if (t == null || handle == null) {
            return (T) t;
        }

        // 处理字符串
        if (t instanceof String) {
            return (T) handle.getValue((String) t);
        }

        // 处理Map
        if (t instanceof Map) {
            Map<Object, Object> map = (Map) t;
            Set<Object> keySet = map.keySet();
            for (final Object key : keySet) {
                Object value = map.get(key);
                map.put(handleValue(key), handleValue(value));
            }
        }

        // 处理集合
        if (t instanceof Collection) {
            Collection<Object> col = (Collection<Object>) t;
            Collection<Object> ncol = new ArrayList<>();
            for (Object object : col) {
                Object n = handleValue(object);
                ncol.add(n);
            }
            return (T) ncol;
        }

        return (T) t;
    }

    @Override
    public void setHandle(SillyPropertyHandle handle) {
        this.handle = handle;
    }
}
