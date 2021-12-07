/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;


/**
 * 当前人获取工具
 */
public interface SillyCurrentUserUtil {

    boolean isAdmin();

    boolean isAdmin(String userId);
    
    String currentUserId();

    String userIdToName(String userId);

}
