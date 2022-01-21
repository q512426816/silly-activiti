/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-common
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.common.util;

import com.iqiny.silly.common.exception.SillyException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class SillyReflectUtil {

    private final static Log logger = LogFactory.getLog(SillyReflectUtil.class);

    public static <M> M newInstance(Class<M> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw SillyException.newInstance("实例化对象失败！" + e.getMessage(), e);
        }
    }

    public static <C extends Class<?>> C classForName(String className) {
        try {
            return (C) Class.forName(className);
        } catch (Exception e) {
            throw SillyException.newInstance("class 未能找到！" + e.getMessage(), e);
        }
    }

    public static <M> M newInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return (M) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw SillyException.newInstance("实例化对象失败！" + e.getMessage(), e);
        }
    }

    /**
     * copyright  com.baomidou.mybatisplus.core.toolkit.getSuperClassGenericType
     *
     * @param clazz
     * @param index
     * @return
     */
    public static <T extends Class<?>> T getSuperClassGenericType(final Class<?> clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            logger.warn(String.format("Warn: %s's superclass not ParameterizedType", clazz.getSimpleName()));
            return (T) Object.class;
        } else {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    logger.warn(String.format("Warn: %s not set the actual class on superclass generic parameter", clazz.getSimpleName()));
                    return (T) Object.class;
                } else {
                    return (T) params[index];
                }
            } else {
                logger.warn(String.format("Warn: Index: %s, Size of %s's Parameterized Type: %s .", index, clazz.getSimpleName(), params.length));
                return (T) Object.class;
            }
        }
    }
}
