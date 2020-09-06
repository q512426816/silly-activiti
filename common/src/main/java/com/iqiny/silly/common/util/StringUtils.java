package com.iqiny.silly.common.util;

import java.util.List;

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

    public static String join(List<String> list, String split) {
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
