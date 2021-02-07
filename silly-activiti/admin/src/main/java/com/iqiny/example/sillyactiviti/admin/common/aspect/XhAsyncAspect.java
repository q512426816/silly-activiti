package com.iqiny.example.sillyactiviti.admin.common.aspect;


import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 方法异步操作
 *
 * @author xh_admin
 */
@Aspect
@Component
@Log
public class XhAsyncAspect {

    private final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(4, 4, 0, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>());


    @Pointcut("@annotation(com.iqiny.example.sillyactiviti.admin.common.annotation.SysLog)")
    public void asyncPointCut() {
    }

    @Around("asyncPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        final Class<?> returnType = method.getReturnType();
        Object result = null;
        if (returnType.equals(Void.TYPE)) {
            //执行方法
            poolExecutor.execute(() -> {
                try {
                    point.proceed();
                } catch (Throwable e) {
                    log.warning(e.getMessage());
                    e.printStackTrace();
                }
            });
        } else {
            log.warning("不支持此方法返回值操作异步！");
            result = point.proceed();
        }
        return result;
    }
}
