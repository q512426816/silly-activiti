/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.interceptor;

import com.iqiny.example.sillyactiviti.admin.common.utils.SecurityUtils;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysLogEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysLogService;
import com.iqiny.example.sillyactiviti.common.utils.IPUtils;
import com.iqiny.example.sillyactiviti.common.utils.SpringContextUtils;
import lombok.extern.java.Log;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log
public class LogInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> START_TIME_THREAD_LOCAL = new NamedThreadLocal<>(LogInterceptor.class.getName() + "_ThreadLocal_StartTime");

    private SysLogService sysLogService = SpringContextUtils.getBean(SysLogService.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {

        long beginTime = System.currentTimeMillis();
        START_TIME_THREAD_LOCAL.set(beginTime);
        log.info("开始计时 URI: " + request.getRequestURI());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView != null) {
            log.info("ViewName: " + modelAndView.getViewName());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 保存日志
        SysLogEntity logEntity = new SysLogEntity();
        if (ex == null) {
            logEntity.setType(SysLogEntity.TYPE_ACCESS);
        } else {
            logEntity.setType(SysLogEntity.TYPE_EXCEPTION);
            logEntity.setExceptionInfo(ex.getMessage());
        }

        logEntity.setUsername(SecurityUtils.getUsername());
        logEntity.setIp(IPUtils.getIpAddr(request));
        logEntity.setUserAgent(request.getHeader("user-agent"));
        logEntity.setUri(request.getRequestURI());
        logEntity.setParamsByMap(request.getParameterMap());
        logEntity.setMethod(request.getMethod());
        long beginTime = START_TIME_THREAD_LOCAL.get();
        long endTime = System.currentTimeMillis();
        logEntity.setTime(endTime - beginTime);
        sysLogService.asyncSaveLog(logEntity, handler, ex, null);

        // 打印JVM信息。
        String s = String.format("耗时：%sms URI: %s  最大内存: %sm  已分配内存: %sm  已分配内存中的剩余空间: %sm  最大可用内存: %sm",
                endTime - beginTime, request.getRequestURI(),
                Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime.getRuntime().totalMemory() / 1024 / 1024, Runtime.getRuntime().freeMemory() / 1024 / 1024,
                (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
        log.info(s);
        START_TIME_THREAD_LOCAL.remove();
    }

}