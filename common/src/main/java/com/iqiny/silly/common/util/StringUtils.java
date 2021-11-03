/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-common 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.common.util;

import com.iqiny.silly.common.SillyConstant;

import java.util.Collection;
import java.util.StringJoiner;

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

    public static String myJoin(Collection<String> list) {
        return myJoin(list, SillyConstant.ARRAY_SPLIT_STR);
    }

    public static String myJoin(Collection<String> list, String split) {
        if (list == null) {
            return null;
        }

        StringJoiner sj = new StringJoiner(split);
        for (String s : list) {
            sj.add(s);
        }
        return sj.toString();
    }
}
