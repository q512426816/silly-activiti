/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
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

        boolean flag = true;
        if (obj instanceof SillyMultipleCategory) {
            flag = ((SillyMultipleCategory) obj).isSupport(category);
        }

        if (!flag && obj instanceof SillyCategory) {
            String usedCategory = ((SillyCategory) obj).usedCategory();
            flag = SillyCategory.DEFAULT_CATEGORY.equals(usedCategory) || Objects.equals(usedCategory, category);
        }
        return flag;
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

        boolean flag = false;
        if (obj instanceof SillyMultipleCategory) {
            flag = ((SillyMultipleCategory) obj).isSupport(category);
        }
        if (!flag && obj instanceof SillyCategory) {
            String usedCategory = ((SillyCategory) obj).usedCategory();
            flag = Objects.equals(usedCategory, category);
        }

        return flag;
    }

    public static <T> T availableOrNull(String category, T t) {
        if (t == null) {
            return null;
        }

        boolean available = available(category, t);
        return available ? t : null;
    }

    public static <T> T availableOne(String category, Collection<T> list) {
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

    public static <T> T consistentOne(String category, Collection<T> list) {
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

    public static <T extends Collection> T availableList(String category, T t) {
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

    public static <T> void availableThen(String category, Collection<T> list, Consumer<T> consumer) {
        if (list == null) {
            return;
        }
        Collection<T> objects = availableList(category, list);
        for (T object : objects) {
            consumer.accept(object);
        }
    }

}
