/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.base.core;

import com.iqiny.silly.core.base.SillyCategory;

public interface SillyEntity {

    /**
     * 特殊种类 表示 兼容其他全部类型
     */
    String SUPPORT_ALL = SillyCategory.DEFAULT_CATEGORY;

    /**
     * 种类，需要保证唯一
     */
    String category();

}
