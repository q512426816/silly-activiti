/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.utils;


import com.iqiny.silly.common.util.StringUtils;

import java.util.regex.Pattern;

/**
 * SQL过滤
 */
public class SQLFilter {

    private static final String REG = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|and|or|delete|insert|trancate|char|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
    private static final Pattern SQL_PATTERN = Pattern.compile(REG, Pattern.CASE_INSENSITIVE);


    /**
     * SQL注入过滤
     *
     * @param str 待验证的字符串
     */
    public static String sqlInject(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        if (SQL_PATTERN.matcher(str).find()) {
            return "";
        }

        return str;
    }
}
