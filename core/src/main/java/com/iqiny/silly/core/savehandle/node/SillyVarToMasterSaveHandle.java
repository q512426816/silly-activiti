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
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 主数据 创建及处理（id 为空则进行新增）
 */
public class SillyVarToMasterSaveHandle<T extends SillyMaster> extends BaseSillyNodeBelongSaveHandle<T> {

    public static final String NAME = "silly_14_varToMaster";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected String belongName(SillyNodeSourceData sourceData) {
        return SillyConstant.ActivitiVariable.BELONG_MASTER;
    }

    @Override
    protected Class<T> belongClass(SillyNodeSourceData sourceData) {
        String category = sourceData.getCategory();
        return SillyConfigUtil.getSillyConfig(category).getSillyFactory().masterClass();
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyMaster master = sourceData.getMaster();
        SillyMaster m = doMakeObjByVariable(sourceData, master);

        // 设置主表信息
        sourceData.setMaster(m);
    }

}
