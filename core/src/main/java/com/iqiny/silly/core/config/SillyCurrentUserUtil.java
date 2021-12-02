/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.core.base.SillyInitializable;
import com.iqiny.silly.core.base.SillyMultipleCategory;

/**
 * 当前人获取工具
 */
public interface SillyCurrentUserUtil {

    String currentUserId();

    String userIdToName(String userId);

}