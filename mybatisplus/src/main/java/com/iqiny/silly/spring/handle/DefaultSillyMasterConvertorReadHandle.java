/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.handle;

import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.mybatisplus.handle.SillyMasterConvertorReadHandle;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 傻瓜主数据转换读取处置类
 */
@Component
public class DefaultSillyMasterConvertorReadHandle implements SillyMasterConvertorReadHandle {

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    @Override
    public Map<String, Object> handle(SillyMaster master) {
        return SillyMapUtils.beanToMap(master);
    }
}
