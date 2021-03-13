package com.iqiny.silly.core.service;

import com.iqiny.silly.core.base.Initializable;
import com.iqiny.silly.core.config.SillyConfig;

/**
 * 傻瓜基本服务操作接口
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyService extends Initializable {

    /**
     * 使用的类型
     */
    String usedCategory();

    /**
     * 初始化傻瓜配置
     */
    SillyConfig getSillyConfig();

}
