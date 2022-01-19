/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.convertor.SillyAutoConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 节点变量 转换器 处理
 */
public class SillyNodeVariableConvertorSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyNodeVariableHandleSaveHandle.ORDER + 100;

    public static final String NAME = "silly_20_nodeVariableConvertor";

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
        SillyNode node = sourceData.getNode();
        return node != null && node.getVariableList() != null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyNode node = sourceData.getNode();
        Map<String, SillyVariableConvertor> convertorMap = sillyConfig.getSillyConvertorMap();
        List<? extends SillyVariable> variables = convertorVariable(node, convertorMap);
        node.setVariableList(variables);
    }

    protected <V extends SillyVariable> List<V> convertorVariable(
            SillyNode node, Map<String, SillyVariableConvertor> convertorMap) {

        List<V> variableList = node.getVariableList();
        // 保存变量
        List<V> saveList = new ArrayList<>();
        for (V variable : variableList) {
            if (variable == null) {
                continue;
            }

            if (variable.getVariableName() == null) {
                throw new SillyException("流程参数名称不可为空！" + variable.getVariableText());
            }

            String variableType = variable.getVariableType();
            SillyVariableConvertor<?> handler = convertorMap.get(variableType);
            // 仅对 string、list 类型的数据进行自动转换
            if (variableType.equals(SillyConstant.ActivitiNode.CONVERTOR_STRING) || variableType.equals(SillyConstant.ActivitiNode.CONVERTOR_LIST)) {
                for (Map.Entry<String, SillyVariableConvertor> convertorEntry : convertorMap.entrySet()) {
                    SillyVariableConvertor<?> convertor = convertorEntry.getValue();
                    if (convertor instanceof SillyAutoConvertor) {
                        SillyAutoConvertor autoConvertor = (SillyAutoConvertor) convertor;
                        if (autoConvertor.auto() && autoConvertor.canConvertor(variable.getVariableName(), variable.getVariableText())) {
                            handler = convertor;
                            break;
                        }
                    }
                }
            }

            if (handler == null) {
                throw new SillyException("未配置相关数据处理器" + variable.getVariableType());
            }

            // 获取处理之后 真正需要保存的 variable 数据
            List<? extends SillyVariable> saveVariableList = handler.makeSaveVariable(variable);
            if (saveVariableList != null) {
                for (SillyVariable v : saveVariableList) {
                    v.setId(null);
                    v.setTaskId(node.getTaskId());
                    v.setMasterId(node.getMasterId());
                    v.setNodeKey(node.getNodeKey());
                    v.setNodeId(node.getId());
                    v.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
                }

                saveList.addAll((Collection<? extends V>) saveVariableList);
            }
        }

        return saveList;
    }

}
