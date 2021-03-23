/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-common 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.common.util;

import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * MapUtils
 */
public class SillyMapUtils extends MapUtils {

    public static void put(Map<String, Object> map, String key, Object value) {
        if (map != null) {
            map.put(key, value);
        }
    }
}
