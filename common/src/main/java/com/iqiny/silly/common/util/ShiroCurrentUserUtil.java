package com.iqiny.silly.common.util;

/**
 * Shiro 获取当前人工具方法
 */
public class ShiroCurrentUserUtil implements CurrentUserUtil {

    @Override
    public String currentUserId() {
        /*Subject subject = SecurityUtils.getSubject();
        final Object principal = subject.getPrincipal();
        if (principal instanceof String) {
            return (String) principal;
        }*/
        return null;
    }

    @Override
    public String userIdToName(String userId) {
        return null;
    }
}
