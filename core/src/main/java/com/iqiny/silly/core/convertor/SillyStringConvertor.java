/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.convertor;

import com.iqiny.silly.core.base.core.SillyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据Value: String
 * map: {key: "key", value: "1"}
 */
public class SillyStringConvertor implements SillyVariableConvertor<String> {

    @Override
    public String name() {
        return "string";
    }

    @Override
    public String convert(Map<String, Object> varMap, String key, String value) {
        varMap.put(key, value);
        return value;
    }

    @Override
    public List<SillyVariable> makeSaveVariable(SillyVariable variable) {
        final List<SillyVariable> objects = new ArrayList<>();
        objects.add(variable);
        return objects;
    }
}
