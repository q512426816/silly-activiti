/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.property.option.impl;

import com.iqiny.silly.core.config.property.option.BaseSillyProcessNodeOptionProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 驳回操作配置信息
 */
public class SillyProcessNodeRejectOptionProperty extends BaseSillyProcessNodeOptionProperty {

    public static final String KEY = "reject";

    private String key = KEY;

    private String name = "驳回";

    private String url = "${'silly/' + #category + '/reject'}";

    private Map<String, Object> data = new HashMap<>();

    private boolean hide = false;

    private String hideEl = null;

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getName() {
        return handleValue(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isHide() {
        return handleValue(hideEl, hide);
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    @Override
    public String getHideEl() {
        return handleValue(hideEl);
    }

    public void setHideEl(String hideEl) {
        this.hideEl = hideEl;
    }

    @Override
    public String getUrl() {
        return handleValue(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
