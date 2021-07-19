/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.exception;


import com.iqiny.example.sillyactiviti.common.exception.MyException;
import com.iqiny.example.sillyactiviti.common.utils.R;
import com.iqiny.silly.common.exception.SillyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 *
 */
@RestControllerAdvice
public class MyRestExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(MyException.class)
    public R handleMyException(MyException e) {
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
        logger.info(e.getMsg(), e);
        return r;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e) {
        logger.error(e.getMessage(), e);
        return R.error("数据库中已存在该记录");
    }

    /**
     * 处理傻瓜异常
     */
    @ExceptionHandler(SillyException.class)
    public R handleRuntimeException(SillyException e) {
        logger.warn(e.getMessage(), e);
        return R.error("流程处置异常！");
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return R.error("服务器运行异常！");
    }

    /**
     * 处理异常
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return R.error("服务器异常！");
    }
}
