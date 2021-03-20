/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-common 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.common.util;

import java.util.Collection;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    public static String join(Collection<String> list) {
        return join(list, ",");
    }

    public static String join(Collection<String> list, String split) {
        if (list == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(split);
        }
        int lastIndex = sb.lastIndexOf(split);
        if (lastIndex >= 0) {
            sb.deleteCharAt(lastIndex);
        }
        return sb.toString();
    }
}
