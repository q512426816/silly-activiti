/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.convertor;

import com.iqiny.silly.core.base.core.SillyVariable;

import java.util.List;
import java.util.Map;

/**
 * 变量转换工具
 */
public interface SillyVariableConvertor<T> {

    /**
     * 转换器名称
     *
     * @return
     */
    String name();

    /**
     * 转换变量数据结构
     *
     * @param map   原始数据Map
     * @param key   取出 Key（若从 Map 中取此Key为 null，则直接 put(key,value)）
     * @param value 放入的 value字符串
     * @return 转换后的数据
     */
    T convert(Map<String, Object> map, String key, String value);

    /**
     * 生成真正需要保存的数据
     *
     * @param variable
     * @return
     */
    List<SillyVariable> makeSaveVariable(SillyVariable variable);

}
