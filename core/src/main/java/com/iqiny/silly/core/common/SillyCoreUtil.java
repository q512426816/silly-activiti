/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.common;

import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.base.SillyMultipleCategory;

import java.util.*;
import java.util.function.Consumer;

/**
 * 傻瓜核心包工具类
 */
public abstract class SillyCoreUtil {

    /**
     * 判断此对象是否可用于此分类
     * 若 category eq SillyCategory.DEFAULT_CATEGORY 则为 true
     * 若 obj instanceof SillyMultipleCategory, 则判断 isSupport(category)是否为true
     * 若 obj instanceof SillyCategory, 则判断 usedCategory()是否等于 SillyCategory.DEFAULT_CATEGORY 或 category
     * 其他类型则 默认支持
     *
     * @param category
     * @param obj
     * @return
     */
    public static boolean available(String category, Object obj) {
        if (SillyCategory.DEFAULT_CATEGORY.equals(category)) {
            return true;
        }

        if (obj instanceof Class) {
            obj = SillyReflectUtil.newInstance((Class) obj);
        }

        if (obj instanceof SillyMultipleCategory) {
            return ((SillyMultipleCategory) obj).isSupport(category);
        } else if (obj instanceof SillyCategory) {
            String usedCategory = ((SillyCategory) obj).usedCategory();
            return SillyCategory.DEFAULT_CATEGORY.equals(usedCategory) || Objects.equals(usedCategory, category);
        }
        return true;
    }

    /**
     * obj与category 的分类一致的
     *
     * @param category
     * @param obj
     * @return
     */
    public static boolean consistent(String category, Object obj) {
        if (obj instanceof Class) {
            obj = SillyReflectUtil.newInstance((Class) obj);
        }

        if (obj instanceof SillyMultipleCategory) {
            return ((SillyMultipleCategory) obj).isSupport(category);
        } else if (obj instanceof SillyCategory) {
            String usedCategory = ((SillyCategory) obj).usedCategory();
            return Objects.equals(usedCategory, category);
        }
        return false;
    }


    public static <T> T availableOne(String category, List<T> list) {
        if (list == null) {
            return null;
        }

        // 优先取一致的
        for (T t : list) {
            boolean available = consistent(category, t);
            if (available) {
                return t;
            }
        }

        // 若没有匹配项 再取可用的
        for (T t : list) {
            boolean available = available(category, t);
            if (available) {
                return t;
            }
        }
        return null;
    }

    public static <T> T consistentOne(String category, List<T> list) {
        if (list == null) {
            return null;
        }

        // 取一致的
        for (T t : list) {
            boolean available = consistent(category, t);
            if (available) {
                return t;
            }
        }

        return null;
    }

    public static <T extends List> T availableList(String category, T t) {
        if (t == null) {
            return null;
        }

        final T categoryCollection = SillyReflectUtil.newInstance((Class<T>) t.getClass());
        t.forEach(e -> {
            boolean available = available(category, e);
            if (available) {
                categoryCollection.add(e);
            }
        });
        return categoryCollection;
    }

    public static <T> void availableThen(String category, List<T> list, Consumer<T> consumer) {
        if (list == null) {
            return;
        }
        List<T> objects = availableList(category, list);
        for (T object : objects) {
            consumer.accept(object);
        }
    }

    public static <T extends Map> T availableMap(String category, Map map, T categoryMap) {
        if (map == null || categoryMap == null) {
            return categoryMap;
        }

        map.forEach((k, v) -> {
            boolean available = available(category, v);
            if (available) {
                categoryMap.put(k, v);
            }
        });
        return categoryMap;
    }
}
