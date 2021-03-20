/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.base;

public interface SillyEntity {

    /**
     * 特殊种类 表示 兼容其他全部类型
     */
    String SUPPORT_ALL = "SILLY_SUPPORT_ALL";

    /**
     * 种类，需要保证唯一
     */
    String category();

}