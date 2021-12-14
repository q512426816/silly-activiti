/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

/**
 * 内部方法 加载 SillyProcessNodeProperty
 */
public class SillyLoadNodePropertyByTaskSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyPropertyHandleSetRootSaveHandle.ORDER + 100;

    public static final String NAME = "loadNodePropertyByTask";

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
        return StringUtils.isNotEmpty(sourceData.taskId()) && sourceData.getNodeProperty() == null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyTask nowTask = sourceData.getNowTask();
        SillyMaster master = sourceData.getMaster();
        String processKey = master.processKey();
        String nodeKey = nowTask.getTaskDefinitionKey();
        SillyProcessNodeProperty<?> nodeProperty = getNodeProperty(processKey, nodeKey, sillyConfig);
        sourceData.setNodeProperty(nodeProperty);
    }

    protected SillyProcessNodeProperty<?> getNodeProperty(String processKey, String nodeKey, SillyCategoryConfig sillyConfig) {
        SillyProcessMasterProperty<?> masterProperty = getMasterProperty(processKey, sillyConfig);
        SillyAssert.notNull(masterProperty, "配置未找到 processKey：" + processKey);
        SillyProcessNodeProperty<?> nodeProperty = masterProperty.getNode().get(nodeKey);
        SillyAssert.notNull(nodeProperty, "配置未找到 nodeKey：" + nodeKey);
        return nodeProperty;
    }

    protected SillyProcessMasterProperty<?> getMasterProperty(String processKey, SillyCategoryConfig sillyConfig) {
        SillyProcessProperty<?> property = sillyConfig.getSillyProcessProperty();
        SillyAssert.notNull(property, "配置未找到 category：" + sillyConfig.usedCategory());
        SillyProcessMasterProperty<?> masterProperty = property.getMaster().get(processKey);
        SillyAssert.notNull(masterProperty, "配置未找到 processKey：" + processKey);
        return masterProperty;
    }

}
