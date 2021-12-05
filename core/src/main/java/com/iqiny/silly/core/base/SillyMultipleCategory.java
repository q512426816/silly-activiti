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

/**
 * 支持多种业务分类
 */
public interface SillyMultipleCategory {

    /**
     * 已支持的分类集合，不一定包含全部支持的分类，具体是否支持此分类请使用 isSupport(category) 进行判断
     */
    Set<String> supportCategories();

    /**
     * 是否支持此类型
     */
    boolean isSupport(String category);

}
