package com.iqiny.silly.common.util;

/**
 * JWT 获取当前人工具方法 需要手动设定线程的用户
 */
public class JwtCurrentUserUtil implements CurrentUserUtil {

    private static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    @Override
    public String currentUserId() {
        return threadLocal.get();
    }

    /**
     * 设置当前线程的用户ID 方法执行前设置
     *
     * @param userId 用户ID
     */
    public static void currentUserId(String userId) {
        threadLocal.set(userId);
    }

    /**
     * 清空线程本地数据 方法执行结束后 只要设置过 setCurrentUserId(userId)【必须调用clear()】
     */
    public static void clear() {
        threadLocal.remove();
    }
}
