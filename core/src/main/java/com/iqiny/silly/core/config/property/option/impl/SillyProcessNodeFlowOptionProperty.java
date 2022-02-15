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
 * 保存操作配置信息
 */
public class SillyProcessNodeFlowOptionProperty extends BaseSillyProcessNodeOptionProperty {

    public static final String KEY = "flow";

    private String key = KEY;

    private String name = "流转";

    private String url = "${'silly/' + #category + '/flow'}";

    private boolean hide = false;

    private String hideEl = null;

    private Map<String, Object> searchData = new HashMap<>();

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

    public Map<String, Object> getSearchData() {
        return searchData;
    }

    public void setSearchData(Map<String, Object> searchData) {
        this.searchData = searchData;
    }
}
