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
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * 内部方法 无当前任务模式 加载 SillyProcessNodeProperty
 */
public class SillyLoadNodePropertyByNotTaskSaveHandle extends BaseSillyNodeSaveHandle {

    public static final String NAME = "silly_08_loadNodePropertyByNotTask";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return StringUtils.isEmpty(sourceData.taskId()) && sourceData.getNodeProperty() == null;
    }

    @Override
    protected void handle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {

        Map<String, Object> map = sourceData.getMap();
        SillyPropertyHandle propertyHandle = sourceData.getPropertyHandle();

        String lastProcessKey = propertyHandle.getStringValue(sillyConfig.getSillyProcessProperty().getLastProcessKey());
        String processKey = MapUtils.getString(map, "processKey", lastProcessKey);
        String nodeKey = MapUtils.getString(map, "nodeKey");

        SillyProcessNodeProperty<?> nodeProperty = sillyConfig.getNodeProperty(processKey, nodeKey);
        sourceData.setNodeProperty(nodeProperty);
    }

}
