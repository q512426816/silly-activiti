package com.crrcdt.silly.convertor;

import java.util.Map;

/**
 * 数据Value: String
 * map: {key: "key", value: "1"}
 */
public class SillyStringConvertor implements SillyVariableConvertor<String> {

    @Override
    public String convert(Map<String, Object> varMap, String key, String value) {
        varMap.put(key, value);
        return value;
    }
}
