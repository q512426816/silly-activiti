/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.core.base.SillyInitializable;

/**
 * 当前人获取工具
 */
public interface CurrentUserUtil extends SillyInitializable {

    String currentUserId();

    String userIdToName(String userId);

}
