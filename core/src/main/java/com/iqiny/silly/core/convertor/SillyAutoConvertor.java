/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.convertor;

/**
 * 傻瓜自动转换服务
 */
public interface SillyAutoConvertor {

    /**
     * 开启自动转换
     *
     * @return
     */
    default boolean auto() {
        return true;
    }

    /**
     * 是否可转换
     *
     * @param fieldName 字段名称
     * @param value     值
     * @return 是否可转换
     */
    boolean canConvertor(String fieldName, Object value);

}
