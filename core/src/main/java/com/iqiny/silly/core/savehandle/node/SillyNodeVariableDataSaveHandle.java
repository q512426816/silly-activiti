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
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.convertor.SillyAutoConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 节点变量 保存处理
 */
public class SillyNodeVariableDataSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyNodeVariableExecuteSaveHandle.ORDER + 100;

    public static final String NAME = "nodeVariableData";

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
        return sourceData.getNode() != null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyNode node = sourceData.getNode();
        Map<String, SillyVariableConvertor> convertorMap = sillyConfig.getSillyConvertorMap();

        // 保存 节点变量表数据
        saveNodeInfo(node, convertorMap, sillyConfig.getSillyFactory(), sillyConfig.getSillyWriteService());
    }

    protected void saveNodeInfo(SillyNode node, Map<String, SillyVariableConvertor> convertorMap, SillyFactory sillyFactory, SillyWriteService writeService) {
        if (node == null) {
            return;
        }
        // （若有）更新之前的流程信息 为历史状态
        updateToHistory(node, sillyFactory, writeService);
        doInsertVariable(node, convertorMap, writeService);
    }

    private void updateToHistory(SillyNode node, SillyFactory sillyFactory, SillyWriteService writeService) {

        // （若有）更新之前的流程变量信息 为历史状态
        final SillyVariable whereVariable = sillyFactory.newVariable();
        whereVariable.setMasterId(node.getMasterId());
        whereVariable.setNodeKey(node.getNodeKey());

        boolean isParallel = SillyConstant.ActivitiParallel.IS_PARALLEL.equals(node.getParallelFlag());
        if (isParallel) {
            whereVariable.setTaskId(node.getTaskId());
        }
        SillyVariable sillyVariable = sillyFactory.newVariable();
        sillyVariable.setMasterId(node.getMasterId());
        sillyVariable.setNodeKey(node.getNodeKey());
        sillyVariable.setTaskId(node.getTaskId());
        sillyVariable.setStatus(SillyConstant.ActivitiNode.STATUS_HISTORY);
        writeService.update(sillyVariable, whereVariable);
    }

    private <V extends SillyVariable> void doInsertVariable(SillyNode node, Map<String, SillyVariableConvertor> convertorMap, SillyWriteService writeService) {
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
