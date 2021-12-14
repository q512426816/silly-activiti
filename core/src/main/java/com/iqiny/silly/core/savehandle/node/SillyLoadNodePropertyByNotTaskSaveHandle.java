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
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import org.apache.commons.collections.MapUtils;

import java.util.Map;
import java.util.Set;

/**
 * 内部方法 无当前任务模式 加载 SillyProcessNodeProperty
 */
public class SillyLoadNodePropertyByNotTaskSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyLoadNodePropertyByTaskSaveHandle.ORDER + 100;

    public static final String NAME = "loadNodePropertyByNotTask";

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
        return StringUtils.isEmpty(sourceData.taskId()) && sourceData.getNodeProperty() == null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {

        Map<String, Object> map = sourceData.getMap();
        SillyPropertyHandle propertyHandle = sourceData.getPropertyHandle();

        String lastProcessKey = propertyHandle.getStringValue(sillyConfig.getSillyProcessProperty().getLastProcessKey());
        String processKey = MapUtils.getString(map, "processKey", lastProcessKey);
        String firstNodeKey = propertyHandle.getStringValue(getLastNodeProperty(processKey, sillyConfig).getNodeKey());
        String nodeKey = MapUtils.getString(map, "nodeKey", firstNodeKey);

        SillyProcessNodeProperty<?> nodeProperty = getNodeProperty(processKey, nodeKey, sillyConfig);
        sourceData.setNodeProperty(nodeProperty);
    }

    public SillyProcessNodeProperty<?> getNodeProperty(String processKey, String nodeKey, SillyCategoryConfig sillyConfig) {
        SillyProcessMasterProperty<?> masterProperty = getMasterProperty(processKey, sillyConfig);
        SillyAssert.notNull(masterProperty, "配置未找到 processKey：" + processKey);
        SillyProcessNodeProperty<?> nodeProperty = masterProperty.getNode().get(nodeKey);
        SillyAssert.notNull(nodeProperty, "配置未找到 nodeKey：" + nodeKey);
        return nodeProperty;
    }

    public SillyProcessMasterProperty<?> getMasterProperty(String processKey, SillyCategoryConfig sillyConfig) {
        SillyProcessProperty<?> property = sillyConfig.getSillyProcessProperty();
        SillyAssert.notNull(property, "配置未找到 category：" + sillyConfig.usedCategory());
        SillyProcessMasterProperty<?> masterProperty = property.getMaster().get(processKey);
        SillyAssert.notNull(masterProperty, "配置未找到 processKey：" + processKey);
        return masterProperty;
    }

    /**
     * 获取最新版本下的节点数据
     *
     * @param processKey
     * @return
     */
    public SillyProcessNodeProperty<?> getLastNodeProperty(String processKey, SillyCategoryConfig sillyConfig) {
        SillyAssert.notEmpty(processKey, "查询第一个节点KEY，流程KEY不可为空");
        Set<String> nodeKeySet = getMasterProperty(processKey, sillyConfig).getNode().keySet();
        // 取node 中第一个定义的为首节点
        String nodeKey = nodeKeySet.iterator().next();
        return getNodeProperty(processKey, nodeKey, sillyConfig);
    }

}
