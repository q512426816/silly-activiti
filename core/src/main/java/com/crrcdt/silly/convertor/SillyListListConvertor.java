package com.crrcdt.silly.convertor;

import com.iqiny.silly.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数据Value: List<List<String>>
 * map: {key: "key", value: [["1"],["2"],["3"]]}
 */
public class SillyListListConvertor implements SillyVariableConvertor<List<List<String>>> {

    @Override
    public List<List<String>> convert(Map<String, Object> varMap, String key, String value) {
        List<List<String>> varList = null;
        Object obj = varMap.get(key);
        if (obj instanceof List) {
            varList = (List<List<String>>) obj;
        } else {
            varList = new ArrayList<>();
        }
        if (StringUtils.isNotEmpty(value)) {
            String[] vtArr = value.split(",");
            varList.add(Arrays.asList(vtArr));
        }
        varMap.put(key, varList);
        return varList;
    }
}
