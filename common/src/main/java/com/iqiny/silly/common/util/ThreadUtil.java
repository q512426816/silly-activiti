package com.iqiny.silly.common.util;

public class ThreadUtil {

    private static CurrentUserUtil userUtil = new ShiroCurrentUserUtil();

    public static String currentUserId(){
        return userUtil.currentUserId();
    }
}
