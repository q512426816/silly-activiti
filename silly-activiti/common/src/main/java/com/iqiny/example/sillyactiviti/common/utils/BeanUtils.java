/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.common.utils;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class BeanUtils {

    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
        try {
            T t = beanClass.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                Method setter = property.getWriteMethod();
                if (setter != null) {
                    String name = property.getName();
                    if (map.containsKey(name)) {
                        Class<?> claz = setter.getParameterTypes()[0];
                        setter.invoke(t, get(claz, map.get(property.getName()), name));
                    }
                }
            }
            return t;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T get(Class<T> clz, Object o, String paramName) {
        if (clz.isInstance(o)) {
            return clz.cast(o);
        } else {
            if (o == null) {
                return null;
            }
            String ser = o.toString();
            if (StringUtils.isBlank(ser)) {
                return null;
            }
            if (StringUtils.isNumeric(ser)) {
                if (clz.isInstance(new Integer(ser))) {
                    return clz.cast(new Integer(ser));
                } else if (clz.isInstance(new Long(ser))) {
                    return clz.cast(new Long(ser));
                } else if (clz.isInstance(new Double(ser))) {
                    return clz.cast(new Double(ser));
                }
            } else {
                if (clz.isInstance(new Date())) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(ser);
                        return clz.cast(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(ser + "日期格式错误：应使用 【yyyy-MM-dd HH:mm:ss】");
                    }
                } else if (clz.isInstance(new Double(ser))) {
                    return clz.cast(new Double(ser));
                }
            }
            throw new RuntimeException(paramName + " 参数值：" + ser + ",转换为-》参数类型：" + clz.getName() + " 解析错误！");
        }
    }
}
