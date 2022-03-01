/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.resume.SillyResume;

import java.util.*;

/**
 * 傻瓜资源数据
 */
public class SillyNodeSourceData {

    /**
     * 执行过的 handle Name
     */
    private final List<String> handleLinkName = new ArrayList<>();
    /**
     * 执行过的 handle
     */
    private final List<SillyNodeSaveHandle> handleLink = new ArrayList<>();

    /**
     * 忽略的处置器名称
     */
    private final Set<String> ignoreHandleName = new LinkedHashSet<>();

    /**
     * 创建参数
     */
    private final String category;
    private final Map<String, Object> map;

    /**
     * 协助处理工具
     */
    private SillyProcessNodeProperty<?> nodeProperty;
    private SillyPropertyHandle propertyHandle;

    /**
     * 数据参数  master / node / variables
     */
    private SillyMaster master;
    private SillyNode node;
    private List<? extends SillyVariable> variables;

    /**
     * 履历信息
     */
    private SillyResume resume;

    /**
     * 流程引擎变量
     */
    private Map<String, Object> actMap;

    /**
     * 当前任务信息
     */
    private SillyTask nowTask;

    /**
     * 当前未完成的 全部任务信息
     */
    private List<? extends SillyTask> nowTaskList;

    /**
     * 全部的下一步任务信息
     */
    private List<? extends SillyTask> nextTaskList;

    /**
     * 新增的 下一步任务信息
     */
    private List<? extends SillyTask> newNextTaskList;


    public SillyNodeSourceData(String category, Map<String, Object> map) {
        this.category = category;
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
        if (nowTask != null) {
            return nowTask.getId();
        }
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

    public void setNodeProperty(SillyProcessNodeProperty<?> nodeProperty) {
        this.nodeProperty = nodeProperty;
    }

    public void setPropertyHandle(SillyPropertyHandle propertyHandle) {
        this.propertyHandle = propertyHandle;
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

    public boolean ignoreHandle(String name) {
        return ignoreHandleName.contains(name);
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

    public List<? extends SillyTask> getNowTaskList() {
        return nowTaskList;
    }

    public void setNowTaskList(List<? extends SillyTask> nowTaskList) {
        this.nowTaskList = nowTaskList;
    }

    public List<? extends SillyTask> getNewNextTaskList() {
        return newNextTaskList;
    }

    public void setNewNextTaskList(List<? extends SillyTask> newNextTaskList) {
        this.newNextTaskList = newNextTaskList;
    }

    public SillyResume getResume() {
        return resume;
    }

    public void setResume(SillyResume resume) {
        this.resume = resume;
    }

    public void addIgnoreHandleName(String name) {
        ignoreHandleName.add(name);
    }

    public String handleLinkNameLog() {
        StringJoiner sj = new StringJoiner(" ->");
        for (int i = 0; i < handleLinkName.size(); i++) {
            sj.add("【(" + (i + 1) + ")" + handleLinkName.get(i) + "】");
        }
        return sj.toString();
    }

    public String getVariableText(String variableName) {
        if (variables == null) {
            return null;
        }

        StringJoiner sj = new StringJoiner(SillyConstant.ARRAY_SPLIT_STR);
        for (SillyVariable variable : variables) {
            if (Objects.equals(variable.getVariableName(), variableName)) {
                sj.add(variable.getVariableText());
            }
        }
        return sj.toString();
    }

    public List<String> getVariableTexts(String variableName) {
        List<String> list = new ArrayList<>();
        if (variables == null) {
            return list;
        }

        for (SillyVariable variable : variables) {
            if (Objects.equals(variable.getVariableName(), variableName)) {
                list.add(variable.getVariableText());
            }
        }
        return list;
    }

    public <V extends SillyVariable> List<V> getVariables(String variableName) {
        List<V> list = new ArrayList<>();
        if (variables == null) {
            return list;
        }

        for (SillyVariable variable : variables) {
            if (Objects.equals(variable.getVariableName(), variableName)) {
                list.add((V)variable);
            }
        }
        return list;
    }
}
