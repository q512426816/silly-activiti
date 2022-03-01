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
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 内部方法 加载 SillyProcessNodeProperty
 */
public class SillyLoadNodePropertyByTaskSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_07_loadNodePropertyByTask";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return StringUtils.isNotEmpty(sourceData.taskId()) && sourceData.getNodeProperty() == null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyTask nowTask = sourceData.getNowTask();
        SillyMaster master = sourceData.getMaster();
        String processKey = master.processKey();
        String nodeKey = nowTask.getTaskDefinitionKey();
        SillyProcessNodeProperty<?> nodeProperty = sillyConfig.getNodeProperty(processKey, nodeKey);
        sourceData.setNodeProperty(nodeProperty);
    }

}
