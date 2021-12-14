/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.common;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.base.SillyMultipleCategory;
import com.iqiny.silly.core.base.SillyOrdered;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 傻瓜核心包工具类
 */
public abstract class SillyCoreUtil {

    /**
     * 排序集合
     */
    public static <T extends List> T orderCollection(Collection<?> collection) {
        List<?> orderList = new ArrayList<>(collection);

        orderList.sort((o1, o2) -> {
            if (o1 instanceof SillyOrdered && o2 instanceof SillyOrdered) {
                SillyOrdered so1 = (SillyOrdered) o1;
                SillyOrdered so2 = (SillyOrdered) o2;
                int a = so1.order() - so2.order();
                if (a == 0) {
                    a = so1.hashCode() - so2.hashCode();
                    SillyAssert.isTrue(a != 0, "SillyOrdered 排序失败 " + so1.getClass());
                }
                return a > 0 ? 1 : -1;
            }

            if (o1 instanceof SillyOrdered) {
                // SillyOrdered 实现类 优先于 非SillyOrdered实现类
                return 1;
            }

            if (o2 instanceof SillyOrdered) {
                // SillyOrdered 实现类 优先于 非SillyOrdered实现类
                return -1;
            }

            return 0;
        });
        return (T) orderList;
    }

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
        if (obj == null) {
            return false;
        }

        if (SillyCategory.DEFAULT_CATEGORY.equals(category)) {
            return true;
        }

        if (obj instanceof Class) {
            obj = SillyReflectUtil.newInstance((Class) obj);
        }

        boolean flag = true;
        if (obj instanceof SillyMultipleCategory) {
            flag = ((SillyMultipleCategory) obj).isSupport(category);
            if (flag) {
                return true;
            }
        }

        if (obj instanceof SillyCategory) {
            String usedCategory = ((SillyCategory) obj).usedCategory();
            return SillyCategory.DEFAULT_CATEGORY.equals(usedCategory) || Objects.equals(usedCategory, category);
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
        
        if (obj instanceof SillyMultipleCategory) {
            boolean flag = ((SillyMultipleCategory) obj).isSupport(category);
            if (flag) {
                return true;
            }
        }

        if (obj instanceof SillyCategory) {
            String usedCategory = ((SillyCategory) obj).usedCategory();
            return Objects.equals(usedCategory, category);
        }

        return false;
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
