/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyReadService;

/**
 * 主数据 创建及处理（id 为空则进行新增）
 */
public class SillyLoadMasterByIdSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_02_loadMasterById";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.getMaster() == null && StringUtils.isNotEmpty(sourceData.masterId());
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyReadService sillyReadService = sillyConfig.getSillyReadService();
        String masterId = sourceData.masterId();
        SillyMaster master = sillyReadService.getMaster(masterId);
        sourceData.setMaster(master);
    }

}
