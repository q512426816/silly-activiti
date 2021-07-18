/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-common 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.common.util;

import com.iqiny.silly.common.exception.SillyException;

public class SillyReflectUtil {

    public static <M> M newInstance(Class<M> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw SillyException.newInstance(e.getMessage(), e);
        }
    }
}
