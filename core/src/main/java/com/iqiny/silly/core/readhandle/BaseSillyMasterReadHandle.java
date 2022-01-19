/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.readhandle;

import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;

import java.util.Map;

public abstract class BaseSillyMasterReadHandle implements SillyMasterReadHandle, SillyCategory {

    @Override
    public String name() {
        return usedCategory() + getClass().getSimpleName();
    }

    @Override
    public void handle(Map<String, Object> record) {
        SillyCategoryConfig sillyConfig = SillyConfigUtil.getSillyConfig(usedCategory());
        Map<String, SillyVariableConvertor> sillyConvertorMap = sillyConfig.getSillyConvertorMap();
        convertorRecord(sillyConvertorMap, record);
    }

    /**
     * 数据转换方法
     *
     * @param sillyConvertorMap
     * @param record
     */
    public abstract void convertorRecord(Map<String, SillyVariableConvertor> sillyConvertorMap, Map<String, Object> record);

}
