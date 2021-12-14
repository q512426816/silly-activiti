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
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程变量处理
 */
public class SillyProcessMapSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyCheckVariableFieldsSaveHandle.ORDER + 100;

    public static final String NAME = "processMap";

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
        return sourceData.getActMap() == null && (sourceData.isSubmit() || sourceData.isStartProcess());
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        Map<String, SillyVariableConvertor> convertorMap = sillyConfig.getSillyConvertorMap();
        Map<String, Object> actMap = makeActVariableMap(sourceData.getVariables(), convertorMap);
        sourceData.setActMap(actMap);
    }

    protected Map<String, Object> makeActVariableMap(List<? extends SillyVariable> variableList, Map<String, SillyVariableConvertor> convertorMap) {
        Map<String, Object> varMap = new LinkedHashMap<>();
        for (SillyVariable variable : variableList) {
            String activitiHandler = variable.getActivitiHandler();
            if (StringUtils.isEmpty(activitiHandler)) {
                continue;
            }

            final SillyVariableConvertor<?> handler = convertorMap.get(activitiHandler);
            SillyAssert.notNull(handler, "转换器未定义" + activitiHandler);
            String vn = variable.getVariableName();
            String vt = variable.getVariableText();
            handler.convert(varMap, vn, vt);
        }
        return varMap;
    }

}
