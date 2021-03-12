package com.iqiny.silly.common.util;

import com.iqiny.silly.common.exception.SillyException;

public class SillyAssert {

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

}
