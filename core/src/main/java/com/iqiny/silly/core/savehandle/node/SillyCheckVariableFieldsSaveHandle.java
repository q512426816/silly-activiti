/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyProcessVariableProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 内部方法 数据验证 处理
 */
public class SillyCheckVariableFieldsSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyMapToVarSaveHandle.ORDER + 100;

    public static final String NAME = "checkVariableFields";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int order() {
        return ORDER;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.isSubmit();
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyPropertyHandle propertyHandle = sourceData.getPropertyHandle();
        SillyProcessNodeProperty<?> nodeProperty = sourceData.getNodeProperty();
        List<? extends SillyVariable> variables = sourceData.getVariables();
        check(propertyHandle, nodeProperty, variables);
    }

    protected void check(SillyPropertyHandle propertyHandle, SillyProcessNodeProperty<?> nodeProperty, List<? extends SillyVariable> variables) {
        Map<String, ? extends SillyProcessVariableProperty> variableMap = nodeProperty.getVariable();
        StringJoiner checkSj = new StringJoiner("\r\n");
        for (SillyVariable variable : variables) {
            SillyProcessVariableProperty variableProperty = variableMap.get(variable.getVariableName());
            if (StringUtils.isEmpty(variable.getVariableText())) {
                if (variableProperty.isRequest() && propertyHandle.getBooleanValue(variableProperty.getRequestEl())) {
                    checkSj.add(" 参数【" + variableProperty.getDesc() + "】 值不可为空 【" + variable.getVariableName() + "】");
                }
            }
        }

        SillyAssert.isEmpty(checkSj.toString(), checkSj.toString());
    }

}
