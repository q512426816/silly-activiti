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
 * 主数据 更新
 */
public class SillyMasterUpdateSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyResumeRecordSaveHandle.ORDER + 100;

    public static final String NAME = "masterUpdate";

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
        return sourceData.getMaster() != null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyMaster master = sourceData.getMaster();
        // 保存 主表数据
        SillyWriteService writeService = sillyConfig.getSillyWriteService();
        boolean saveFlag = writeService.updateById(master);
        if (!saveFlag) {
            throw new SillyException("主信息更新发生异常！");
        }
    }

}
