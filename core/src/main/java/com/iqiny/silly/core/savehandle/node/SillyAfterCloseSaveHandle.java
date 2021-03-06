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
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.Date;
import java.util.Objects;

/**
 * 流程 任务处置完成后(流程结束) 处理器
 */
public class SillyAfterCloseSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_25_afterClose";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.isSubmit() && sourceData.getNowTask() != null &&
                (sourceData.getNextTaskList() == null || sourceData.getNextTaskList().isEmpty());
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyMaster master = sourceData.getMaster();
        SillyNode node = sourceData.getNode();
        SillyCurrentUserUtil currentUserUtil = sillyConfig.getSillyCurrentUserUtil();
        afterCloseProcess(master, node, currentUserUtil);
    }

    /**
     * 流程结束之后的回调
     */
    protected void afterCloseProcess(SillyMaster master, SillyNode node, SillyCurrentUserUtil currentUserUtil) {

        String status = master.getStatus();
        if (StringUtils.isEmpty(status) || Objects.equals(master.doingStatus(), status)) {
            master.setStatus(master.endStatus());
            master.setTaskName("流程结束");
        }

        master.setCloseDate(new Date());
        master.setCloseUserId(currentUserUtil.currentUserId());
        master.setHandleUserName("");

        node.setHandleType(SillyConstant.SillyResumeType.PROCESS_TYPE_CLOSE);
    }
    
}
