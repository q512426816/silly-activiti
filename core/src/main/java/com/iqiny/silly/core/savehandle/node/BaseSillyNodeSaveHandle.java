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
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.savehandle.SillyNodeSaveHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 傻瓜归属 基础类
 */
public abstract class BaseSillyNodeSaveHandle implements SillyNodeSaveHandle {

    @Override
    public SillyNodeSaveHandle handle(SillyNodeSourceData sourceData) {
        String category = sourceData.getCategory();
        SillyCategoryConfig sillyConfig = SillyConfigUtil.getSillyConfig(category);
        boolean canHandleFlag = canHandle(sourceData);
        if (canHandleFlag) {
            saveHandle(sillyConfig, sourceData);
            sourceData.record(this);
        }
        return next(sillyConfig);
    }

    /**
     * 未执行过的 NAME  且 子类实现的判定通过
     */
    protected boolean canHandle(SillyNodeSourceData sourceData) {
        return !sourceData.executed(name()) && canDo(sourceData);
    }

    protected SillyNodeSaveHandle next(SillyCategoryConfig sillyConfig) {
        return sillyConfig.getSillyContext().getNextBean(this, sillyConfig.usedCategory(), SillyNodeSaveHandle.class);
    }

    protected abstract boolean canDo(SillyNodeSourceData sourceData);

    protected abstract void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData);

    protected boolean canDoBelong(List<? extends SillyVariable> variables, String belong) {
        if (variables == null) {
            return false;
        }

        for (SillyVariable variable : variables) {
            if (Objects.equals(variable.getBelong(), belong)) {
                return true;
            }
        }
        return false;
    }

    protected <T, V extends SillyVariable> T doMakeObjByVariable(List<V> vList, String belong, Object org, Class<T> clazz) {

        Map<String, Object> objMap = new HashMap<>();
        if (org != null) {
            objMap = SillyMapUtils.beanToMap(org);
        }

        for (V v : vList) {
            String name = v.getVariableName();
            String value = v.getVariableText();

            if (Objects.equals(v.getBelong(), belong)) {
                objMap.put(name, value);
            }
        }

        String objJson = JSON.toJSONString(objMap);
        return JSON.parseObject(objJson, clazz);
    }
}
