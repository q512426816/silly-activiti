/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyWriteService;

/**
 * 主数据 创建 当任务ID 及 主表ID 都不存在时 创建新空白的主数据
 */
public class SillyLoadMasterByNewSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_04_loadMasterByNew";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.getMaster() == null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {

        SillyMaster master = sillyConfig.getSillyFactory().newMaster();
        master.setId(sourceData.masterId());
        // 保存 主表数据
        SillyWriteService writeService = sillyConfig.getSillyWriteService();
        // 插入主表
        boolean saveFlag = writeService.insert(master);
        if (!saveFlag) {
            throw new SillyException("主信息保存发生异常！");
        }

        sourceData.setMaster(master);
    }

}
