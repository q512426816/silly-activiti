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
import org.apache.commons.collections.MapUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * MapUtils
 */
public class SillyMapUtils extends MapUtils {

    public static void put(Map<String, Object> map, String key, Object value) {
        if (map != null) {
            map.put(key, value);
        }
    }

    public static Map<String, Object> beanToMap(Object beanObj, String... ignor) {
        return beanToMap(beanObj, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), ignor);
    }

    /**
     * 使用Introspector，对象转换为map集合
     *
     * @param beanObj javabean对象
     * @return map集合
     */
    public static Map<String, Object> beanToMap(Object beanObj, DateFormat df, String... ignor) {
        if (null == beanObj) {
            return null;
        }
        List<String> ignorList = new ArrayList<>();
        if (ignor != null) {
            ignorList = Arrays.asList(ignor);
        }
        HashMap<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (ignorList.contains(key)) {
                    continue;
                }
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(beanObj) : null;
                if (value instanceof Date && df != null) {
                    value = df.format(value);
                }
                map.put(key, value);
            }
            return map;
        } catch (Exception ex) {
            throw new SillyException(ex.getMessage());
        }
    }
}
