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
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.List;

/**
 * 主数据 创建及处理（id 为空则进行新增）
 */
public class SillyMasterSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyInternalSaveHandle.ORDER + 100;

    public static final String NAME = "master";

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
        return sourceData.getMaster() == null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        Class<? extends SillyMaster> masterClass = sillyConfig.getSillyFactory().masterClass();
        List<? extends SillyVariable> vs = sourceData.getVariables();
        SillyProcessNodeProperty<?> nodeProperty = sourceData.getNodeProperty();
        SillyProcessMasterProperty masterProperty = nodeProperty.getParent();
        SillyMaster m = doMakeObjByVariable(vs, SillyConstant.ActivitiVariable.BELONG_MASTER, masterClass);
        m.setProcessKey(masterProperty.getProcessKey());
        m.setProcessVersion(masterProperty.getProcessVersion());
        m.setId(sourceData.masterId());
        // 保存 主表数据
        SillyWriteService writeService = sillyConfig.getSillyWriteService();
        if (StringUtils.isEmpty(m.getId())) {
            // 插入主表
            boolean saveFlag = writeService.insert(m);
            if (!saveFlag) {
                throw new SillyException("主信息保存发生异常！");
            }
        }
        // 设置主表信息
        sourceData.setMaster(m);
    }

}
