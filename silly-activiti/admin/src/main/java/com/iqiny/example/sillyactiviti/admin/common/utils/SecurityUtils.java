/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.utils;

import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysUserEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysUserService;
import com.iqiny.example.sillyactiviti.common.utils.SpringContextUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 安全集成 工具
 */
public class SecurityUtils {

    private static SysUserService sysUserService = SpringContextUtils.getBean(SysUserService.class);

    private static final Map<String, SysUserEntity> USER_MAP = new ConcurrentHashMap<>();

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getUsername() {
        final Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        final Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getUsername();
        }
        return principal.toString();
    }

    public static String getUserId() {
        final SysUserEntity user = getUser();
        return user == null ? null : getUser().getId();
    }

    public static SysUserEntity getUser() {
        final String username = getUsername();
        SysUserEntity userEntity = USER_MAP.get(username);
        if (userEntity == null) {
            userEntity = sysUserService.getByUsername(username);
            USER_MAP.put(username, userEntity);
        }
        return userEntity;
    }
}
