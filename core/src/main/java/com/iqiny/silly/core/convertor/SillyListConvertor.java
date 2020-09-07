package com.iqiny.silly.core.convertor;


import com.iqiny.silly.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据Value: List<String>
 * map: {key: "key", value: ["1","2","3"]}
 */
public class SillyListConvertor implements SillyVariableConvertor<List<String>> {

    @Override
    public List<String> convert(Map<String, Object> varMap, String key, String value) {
        List<String> varList = null;
        Object obj = varMap.get(key);
        if (obj instanceof List) {
            varList = (List<String>) obj;
        } else {
            varList = new ArrayList<>();
        }
        if (StringUtils.isNotEmpty(value)) {
            String[] vtArr = value.split(",");
            varList.addAll(Arrays.asList(vtArr));
        }
        varMap.put(key, varList);
        return varList;
    }
}
