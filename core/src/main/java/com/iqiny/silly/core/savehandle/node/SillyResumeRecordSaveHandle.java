/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 流程 履历记录 处理器
 */
public class SillyResumeRecordSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyResumeCreateSaveHandle.ORDER + 100;

    public static final String NAME = "resumeRecord";

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
        return sourceData.getResume() != null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyResume resume = sourceData.getResume();
        // 插入流程履历
        SillyResumeService sillyResumeService = sillyConfig.getSillyResumeService();
        sillyResumeService.insert(resume);
    }

}
