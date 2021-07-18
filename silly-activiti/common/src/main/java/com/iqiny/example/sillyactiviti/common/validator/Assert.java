/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.common.validator;

import com.iqiny.example.sillyactiviti.common.exception.MyException;
import com.iqiny.example.sillyactiviti.common.utils.StringUtils;

/**
 * 数据校验
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new MyException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new MyException(message);
        }
    }
}
