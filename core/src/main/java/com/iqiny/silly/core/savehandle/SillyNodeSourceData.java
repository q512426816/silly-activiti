/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle;

import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.engine.SillyTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 傻瓜资源数据
 */
public class SillyNodeSourceData {

    /**
     * 执行过的 handle
     */
    private final List<String> handleLinkName = new ArrayList<>();
    /**
     * 执行过的 handle
     */
    private final List<SillyNodeSaveHandle> handleLink = new ArrayList<>();

    /**
     * 创建参数
     */
    private final String category;
    private final Map<String, Object> map;
    private final SillyProcessNodeProperty<?> nodeProperty;
    private final SillyPropertyHandle propertyHandle;

    /**
     * 数据参数  master / node / variables
     */
    private SillyMaster master;
    private SillyNode node;
    private List<? extends SillyVariable> variables;

    /**
     * 流程引擎变量
     */
    private Map<String, Object> actMap;

    /**
     * 当前任务信息
     */
    private SillyTask nowTask;

    /**
     * 下一步任务信息
     */
    private List<? extends SillyTask> nextTaskList;


    public SillyNodeSourceData(String category, SillyProcessNodeProperty<?> nodeProperty, SillyPropertyHandle propertyHandle, Map<String, Object> map) {
        this.category = category;
        this.nodeProperty = nodeProperty;
        this.propertyHandle = propertyHandle;
        this.map = map;
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public void record(SillyNodeSaveHandle nodeSaveHandle) {
        handleLink.add(nodeSaveHandle);
        handleLinkName.add(nodeSaveHandle.name());
    }

    public boolean isSubmit() {
        return SillyMapUtils.getBooleanValue(map, "submit");
    }

    public boolean isStartProcess() {
        return SillyMapUtils.getBooleanValue(map, "startProcess");
    }

    public String masterId() {
        if (master != null) {
            return master.getId();
        }
        return SillyMapUtils.getString(map, "id");
    }

    public String taskId() {
        return SillyMapUtils.getString(map, "taskId");
    }

    public void setTaskId(String taskId) {
        map.put("taskId", taskId);
    }

    public String getCategory() {
        return category;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public SillyProcessNodeProperty<?> getNodeProperty() {
        return nodeProperty;
    }

    public SillyPropertyHandle getPropertyHandle() {
        return propertyHandle;
    }

    public SillyMaster getMaster() {
        return master;
    }

    public void setMaster(SillyMaster master) {
        this.master = master;
    }

    public SillyNode getNode() {
        return node;
    }

    public void setNode(SillyNode node) {
        this.node = node;
    }

    public List<? extends SillyVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<? extends SillyVariable> variables) {
        this.variables = variables;
    }

    public Map<String, Object> getActMap() {
        return actMap;
    }

    public void setActMap(Map<String, Object> actMap) {
        this.actMap = actMap;
    }

    public boolean executed(String name) {
        return handleLinkName.contains(name);
    }

    public List<SillyNodeSaveHandle> getHandleLink() {
        return handleLink;
    }

    public SillyTask getNowTask() {
        return nowTask;
    }

    public void setNowTask(SillyTask nowTask) {
        this.nowTask = nowTask;
    }

    public List<? extends SillyTask> getNextTaskList() {
        return nextTaskList;
    }

    public void setNextTaskList(List<? extends SillyTask> nextTaskList) {
        this.nextTaskList = nextTaskList;
    }
}
