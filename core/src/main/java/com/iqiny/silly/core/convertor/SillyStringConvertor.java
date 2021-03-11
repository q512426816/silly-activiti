package com.iqiny.silly.core.convertor;

import com.iqiny.silly.core.base.core.SillyVariable;

import java.util.Collections;
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
    public <V extends SillyVariable> List<V> saveVariable(V variable) {
        return Collections.singletonList(variable);
    }
}
