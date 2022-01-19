/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.convertor;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.core.base.core.SillyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据Value: String
 * map: {key: "key", value: "1"}
 */
public class SillyStringConvertor implements SillyVariableConvertor<String> {

    public static final String NAME = SillyConstant.ActivitiNode.CONVERTOR_STRING;

    @Override
    public String name() {
        return NAME;
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
