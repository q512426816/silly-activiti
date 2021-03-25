/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-common 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.common.util;

import com.iqiny.silly.common.exception.SillyException;

import java.util.Collection;

public class SillyAssert {

    public static void isNull(Object o) {
        if (o != null) {
            throw SillyException.newInstance("数据验证异常：应该为null");
        }
    }


    public static void isNull(Object o, String msg) {
        if (o != null) {
            throw SillyException.newInstance(msg);
        }
    }

    public static void notNull(Object o) {
        if (o == null) {
            throw SillyException.newInstance("数据验证异常：不可为null");
        }
    }

    public static void notNull(Object o, String msg) {
        if (o == null) {
            throw SillyException.newInstance(msg);
        }
    }

    public static void notEmpty(String o, String msg) {
        if (StringUtils.isEmpty(o)) {
            throw SillyException.newInstance(msg);
        }
    }

    public static void notEmpty(Collection collection) {
        notEmpty(collection, "集合不可为空");
    }

    public static void notEmpty(Collection collection, String msg) {
        if (collection == null || collection.isEmpty()) {
            throw SillyException.newInstance(msg);
        }
    }

    public static void isTrue(boolean flag, String msg) {
        if (!flag) {
            throw SillyException.newInstance(msg);
        }
    }

    public static void isFalse(boolean flag, String msg) {
        if (flag) {
            throw SillyException.newInstance(msg);
        }
    }
}
