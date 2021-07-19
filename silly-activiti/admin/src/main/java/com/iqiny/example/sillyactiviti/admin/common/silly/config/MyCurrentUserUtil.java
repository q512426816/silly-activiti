/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.silly.config;

import com.iqiny.example.sillyactiviti.admin.common.utils.SecurityUtils;
import com.iqiny.silly.core.config.CurrentUserUtil;
import org.springframework.stereotype.Component;

@Component
public class MyCurrentUserUtil implements CurrentUserUtil {

    @Override
    public String currentUserId() {
        return SecurityUtils.getUserId();
    }

    @Override
    public String userIdToName(String userId) {
        return userId;
    }

    @Override
    public void init() {
        
    }

    @Override
    public String usedCategory() {
        return null;
    }
}
