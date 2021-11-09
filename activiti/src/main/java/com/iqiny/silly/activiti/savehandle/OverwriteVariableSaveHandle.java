/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti.savehandle;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;

/**
 * 重写处理器
 * 重写覆盖同名变量数据，将之前数据转为旧版本
 */
public class OverwriteVariableSaveHandle implements SillyVariableSaveHandle {

    public static final String NAME = "overwrite";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public boolean handle(SillyMaster master, SillyNode node, SillyVariable variable) {
        SillyConfig sillyConfig = SillyConfigUtil.getSillyConfig();

        // 直接更新同名数据 状态为 旧版本
        // （若有）更新之前的流程变量信息 为历史状态
        final SillyVariable whereVariable = sillyConfig.getSillyFactory(master.category()).newVariable();
        whereVariable.setMasterId(node.getMasterId());
        whereVariable.setVariableName(variable.getVariableName());
        final SillyVariable sillyVariable = sillyConfig.getSillyFactory(master.category()).newVariable();
        sillyVariable.setMasterId(node.getMasterId());
        sillyVariable.setVariableName(variable.getVariableName());
        sillyVariable.setStatus(SillyConstant.ActivitiNode.STATUS_HISTORY);
        sillyConfig.getSillyWriteService(master.category()).update(sillyVariable, whereVariable);
        return true;
    }
}
