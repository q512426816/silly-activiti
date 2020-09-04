package com.iqiny.silly.common.util;

public class StringUtils {
    
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return StringUtils.isEmpty(str);
    }
}
