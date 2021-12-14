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
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.List;

/**
 * 主数据 创建及处理（id 为空则进行新增）
 */
public class SillyVarToMasterSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyProcessMapSaveHandle.ORDER + 100;

    public static final String NAME = "varToMaster";

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
        return canDoBelong(sourceData.getVariables(), SillyConstant.ActivitiVariable.BELONG_MASTER);
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        Class<? extends SillyMaster> masterClass = sillyConfig.getSillyFactory().masterClass();
        List<? extends SillyVariable> vs = sourceData.getVariables();
        SillyProcessNodeProperty<?> nodeProperty = sourceData.getNodeProperty();
        SillyProcessMasterProperty masterProperty = nodeProperty.getParent();
        SillyMaster master = sourceData.getMaster();
        SillyMaster m = doMakeObjByVariable(vs, SillyConstant.ActivitiVariable.BELONG_MASTER, master, masterClass);
        m.setProcessKey(masterProperty.getProcessKey());
        m.setProcessVersion(masterProperty.getProcessVersion());

        // 设置主表信息
        sourceData.setMaster(m);
    }

}
