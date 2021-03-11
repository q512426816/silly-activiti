package com.iqiny.silly.activiti.convertor;


import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import org.springframework.beans.BeanUtils;

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
    public String name() {
        return "list";
    }

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

    @Override
    public <V extends SillyVariable> List<V> saveVariable(V variable) {
        final String variableText = variable.getVariableText();
        List<V> list = new ArrayList<>();
        try {
            final String[] split = variableText.split(",");
            for (String val : split) {
                final V copy = (V) variable.getClass().newInstance();
                BeanUtils.copyProperties(variable, copy);
                copy.setVariableText(val);
                list.add(copy);
            }
        } catch (Exception e) {
        }
        return list;
    }

}
