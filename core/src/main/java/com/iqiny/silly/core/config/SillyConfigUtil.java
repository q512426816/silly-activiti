/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.SillyCategory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 傻瓜配置工具类
 *
 * @author iqiny
 */
public abstract class SillyConfigUtil {

    /**
     * 不同分类下的配置对象
     */
    private static final Map<String, SillyCategoryConfig> SILLY_CONFIG_MAP = new ConcurrentHashMap<>();

    public static void addSillyConfig(String category, SillyCategoryConfig sillyCategoryConfig) {
        addSillyConfig(false, category, sillyCategoryConfig);
    }

    public static void addSillyConfig(boolean refreshFlag, SillyCategoryConfig sillyCategoryConfig) {
        addSillyConfig(refreshFlag, sillyCategoryConfig.usedCategory(), sillyCategoryConfig);
    }

    public static void addSillyConfig(boolean refreshFlag, String category, SillyCategoryConfig sillyCategoryConfig) {
        SillyAssert.notNull(category);
        final SillyCategoryConfig put = SILLY_CONFIG_MAP.put(category, sillyCategoryConfig);
        if (!refreshFlag) {
            SillyAssert.isNull(put, "冲突的SillyConfig category:" + category);
        }
    }

    public static void addSillyConfig(Set<String> categories, SillyCategoryConfig sillyCategoryConfig) {
        SillyAssert.notNull(categories);

        for (String category : categories) {
            addSillyConfig(category, sillyCategoryConfig);
        }
    }

    public static void addSillyConfig(SillyCategoryConfig sillyCategoryConfig) {
        addSillyConfig(sillyCategoryConfig.usedCategory(), sillyCategoryConfig);
    }

    public static void refreshSillyConfig(SillyCategoryConfig sillyCategoryConfig) {
        addSillyConfig(true, sillyCategoryConfig.usedCategory(), sillyCategoryConfig);
    }

    public static SillyCategoryConfig getSillyConfig(String category) {
        return SILLY_CONFIG_MAP.get(category);
    }
    
}
