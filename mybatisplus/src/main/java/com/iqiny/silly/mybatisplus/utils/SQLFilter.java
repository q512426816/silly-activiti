/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.utils;


import com.iqiny.silly.common.util.StringUtils;

/**
 * SQL过滤
 */
public class SQLFilter {

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

        //转换成小写
        //str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master ", "truncate ", "insert ", "select ", "delete ", "update ", "declare ", "alter ", "drop "};

        //判断是否包含非法字符
        for (String keyword : keywords) {
            if (str.contains(keyword)) {
                throw new RuntimeException("包含非法字符");
            }
        }

        return str;
    }
}
