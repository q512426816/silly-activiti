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
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.*;

/**
 * 节点变量 数据保存处理
 */
public class SillyNodeVariableInsertSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyNodeVariableConvertorSaveHandle.ORDER + 100;

    public static final String NAME = "nodeVariableInsert";

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
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyNode node = sourceData.getNode();
        Map<String, SillyVariableConvertor> convertorMap = sillyConfig.getSillyConvertorMap();
        SillyWriteService writeService = sillyConfig.getSillyWriteService();
        // 保存 节点变量表数据
        doInsertVariable(node, convertorMap, writeService);
    }

    protected <V extends SillyVariable> void doInsertVariable(SillyNode node, Map<String, SillyVariableConvertor> convertorMap, SillyWriteService writeService) {
        // 保存变量
        List<V> variableList = node.getVariableList();
        if (variableList == null) {
            return;
        }

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
        if (!saveList.isEmpty()) {
            boolean flag = writeService.batchInsert(saveList);
            if (!flag) {
                throw new SillyException("保存流程节点表信息发生异常！");
            }
        }
    }

}
