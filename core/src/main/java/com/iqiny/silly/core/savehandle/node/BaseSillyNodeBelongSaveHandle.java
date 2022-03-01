/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.alibaba.fastjson.JSON;
import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 傻瓜 归属处置 基础类
 */
public abstract class BaseSillyNodeBelongSaveHandle<T> extends BaseSillyNodeSaveHandle {

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        List<? extends SillyVariable> variables = sourceData.getVariables();
        if (variables == null) {
            return false;
        }

        String belong = belongName(sourceData);
        Class<T> belongClass = belongClass(sourceData);
        if (belong == null || belongClass == null) {
            return false;
        }

        for (SillyVariable variable : variables) {
            if (Objects.equals(variable.getBelong(), belong)) {
                return true;
            }
        }
        return false;
    }

    protected T doMakeObjByVariable(SillyNodeSourceData sourceData, Object orgSource) {
        List<? extends SillyVariable> vList = sourceData.getVariables();
        Map<String, Object> objMap = new HashMap<>(64);
        if (orgSource != null) {
            objMap = SillyMapUtils.beanToMap(orgSource);
        }

        String belong = belongName(sourceData);
        Class<T> tClass = belongClass(sourceData);
        for (SillyVariable v : vList) {
            String name = v.getVariableName();
            String value = v.getVariableText();

            if (Objects.equals(v.getBelong(), belong)) {
                objMap.put(name, value);
            }
        }

        String objJson = JSON.toJSONString(objMap);
        return JSON.parseObject(objJson, tClass);
    }

    /**
     * 归属名称
     */
    protected abstract String belongName(SillyNodeSourceData sourceData);

    /**
     * 归属类型
     */
    protected abstract Class<T> belongClass(SillyNodeSourceData sourceData);

}
