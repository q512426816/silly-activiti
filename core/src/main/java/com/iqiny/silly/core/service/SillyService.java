package com.iqiny.silly.core.service;

import com.iqiny.silly.core.base.SillyInitializable;
import com.iqiny.silly.core.config.SillyConfig;

/**
 * 傻瓜基本服务操作接口
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyService extends SillyInitializable {

    /**
     * 初始化傻瓜配置
     */
    SillyConfig getSillyConfig();

}