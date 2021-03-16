package com.iqiny.silly.core.config;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.SillyInitializable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 傻瓜配置工具类
 */
public class SillyConfigUtil {

    /**
     * 默认的配置对象，若找不到对应分类的配置对象则使用此配置对象
     */
    private static SillyConfig defaultSillyConfig;
    /**
     * 不同分类下的配置对象
     */
    private static final Map<String, SillyConfig> SILLY_CONFIG_MAP = new ConcurrentHashMap<>();

    public static void addSillyConfig(String category, SillyConfig sillyConfig) {
        SillyAssert.notNull(category);
        if (SillyInitializable.DEFAULT_CATEGORY.equals(category)) {
            defaultSillyConfig = sillyConfig;
        }
        SILLY_CONFIG_MAP.put(category, sillyConfig);
    }

    public static void addSillyConfig(SillyConfig sillyConfig) {
        addSillyConfig(sillyConfig.usedCategory(), sillyConfig);
    }

    public static SillyConfig getSillyConfig(String category) {
        SillyAssert.notNull(category);
        final SillyConfig sillyConfig = SILLY_CONFIG_MAP.get(category);
        return sillyConfig == null ? defaultSillyConfig : sillyConfig;
    }

    public static SillyConfig getSillyConfig() {
        return getSillyConfig(SillyInitializable.DEFAULT_CATEGORY);
    }
}
