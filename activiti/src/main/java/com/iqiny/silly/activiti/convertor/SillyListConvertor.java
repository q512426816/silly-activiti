/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti.convertor;


import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    protected Log log = LogFactory.getLog(SillyListConvertor.class);

    protected String splitStr = SillyConstant.ARRAY_SPLIT_STR;

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
            String[] vtArr = value.split(SillyConstant.ARRAY_SPLIT_STR);
            varList.addAll(Arrays.asList(vtArr));
        }
        varMap.put(key, varList);
        return varList;
    }

    @Override
    public List<SillyVariable> makeSaveVariable(SillyVariable variable) {
        final String variableText = variable.getVariableText();
        List<SillyVariable> list = new ArrayList<>();
        if (StringUtils.isEmpty(variableText)) {
            return list;
        }

        try {
            final String[] split = variableText.split(splitStr);
            for (String val : split) {
                final SillyVariable copy = SillyReflectUtil.newInstance(variable.getClass());
                BeanUtils.copyProperties(variable, copy);
                copy.setVariableText(val);
                list.add(copy);
                beforeMakeAddOne(list, copy);
            }
        } catch (Exception e) {
            log.warn("数据处理转换异常："+ e.getMessage());
        }
        return list;
    }

    /**
     * 添加完成一个变量后执行的方法
     *
     * @param list
     * @param variable
     */
    protected void beforeMakeAddOne(List<SillyVariable> list, SillyVariable variable) {

    }

}
