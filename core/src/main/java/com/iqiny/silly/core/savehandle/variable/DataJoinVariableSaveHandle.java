/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.variable;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;

import java.util.List;
import java.util.StringJoiner;

/**
 * 累加合并处理器
 * 累加合并同名变量数据
 */
public class DataJoinVariableSaveHandle implements SillyVariableSaveHandle {

    public static final String NAME = "dataJoin";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean handle(SillyMaster master, SillyNode node, SillyVariable variable) {
        SillyCategoryConfig sillyCategoryConfig = SillyConfigUtil.getSillyConfig(master.usedCategory());

        final SillyVariable whereVariable = sillyCategoryConfig.getSillyFactory().newVariable();
        whereVariable.setMasterId(node.getMasterId());
        whereVariable.setVariableName(variable.getVariableName());
        whereVariable.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        List<SillyVariable> variableList = sillyCategoryConfig.getSillyReadService().findVariableList(whereVariable);
      
        // 累计变量数据
        StringJoiner sj = new StringJoiner(SillyConstant.ARRAY_SPLIT_STR);
        for (SillyVariable sillyVariable : variableList) {
            sj.add(sillyVariable.getVariableText());
        }

        sj.add(variable.getVariableText());
        variable.setVariableText(sj.toString());

        return true;
    }
}
