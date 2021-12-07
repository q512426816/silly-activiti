/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.base;

import java.util.Set;
import java.util.function.Consumer;

/**
 * 傻瓜空间
 */
public interface SillyContext {

    default <T> T getBean(String beanName) {
        return getBean(SillyCategory.DEFAULT_CATEGORY, beanName);
    }

    default <T> T getBean(Class<T> clazz) {
        return getBean(SillyCategory.DEFAULT_CATEGORY, clazz);
    }

    default <T> Set<T> getBeanSet(Class<T> clazz) {
        return getBeanSet(SillyCategory.DEFAULT_CATEGORY, clazz);
    }

    default <T> T registerBean(String beanName, Class<T> clazz, Consumer<Object> consumer) {
        return registerBean(SillyCategory.DEFAULT_CATEGORY, beanName, clazz, consumer);
    }

    <T> T getBean(String category, String beanName);

    <T> T getBean(String category, Class<T> clazz);

    <T> Set<T> getBeanSet(String category, Class<T> clazz);

    <T> T registerBean(String category, String beanName, Class<T> clazz, Consumer<Object> consumer);

}
