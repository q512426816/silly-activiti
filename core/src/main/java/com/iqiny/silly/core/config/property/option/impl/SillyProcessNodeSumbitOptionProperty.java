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

/**
 * 保存操作配置信息
 */
public class SillyProcessNodeSumbitOptionProperty extends BaseSillyProcessNodeOptionProperty {

    public static final String KEY = "submit";

    private String key = KEY;

    private String name = "提交";

    private String url = "${'silly/' + #category + '/saveMap'}";

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

}
