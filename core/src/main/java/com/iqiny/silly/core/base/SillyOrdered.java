/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.base;

/**
 * 傻瓜排序  越小越先执行
 */
public interface SillyOrdered {

    /**
     * 之前
     */
    int BEFORE = -1;

    /**
     * 之后
     */
    int AFTER = 1;

    /**
     * 排序号 越小越先执行 （不可冲突）
     */
    int order();

}
