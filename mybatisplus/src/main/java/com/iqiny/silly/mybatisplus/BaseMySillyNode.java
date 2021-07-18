/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SillyNode 集成MybatisPlus
 *
 * @param <T>
 */
public abstract class BaseMySillyNode<T extends Model<T>, V extends SillyVariable> extends BaseMyBaseEntity<T> implements SillyNode<V> {

    protected String masterId;

    protected int seq;

    protected String nodeKey;
    protected String nodeName;
    protected String taskId;

    protected String parallelFlag;

    protected Date nodeDate;
    protected String nodeUserId;

    protected String status;

    protected String nodeInfo;

    @TableField(exist = false)
    protected String handleType;
    
    @TableField(exist = false)
    protected Map<String, Object> variableMap = new LinkedHashMap<>();

    @Override
    public String getMasterId() {
        return masterId;
    }

    @Override
    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    @Override
    public int getSeq() {
        return seq;
    }

    @Override
    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String getNodeKey() {
        return nodeKey;
    }

    @Override
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getParallelFlag() {
        return parallelFlag;
    }

    @Override
    public void setParallelFlag(String parallelFlag) {
        this.parallelFlag = parallelFlag;
    }

    @Override
    public Date getNodeDate() {
        return nodeDate;
    }

    @Override
    public void setNodeDate(Date nodeDate) {
        this.nodeDate = nodeDate;
    }

    @Override
    public String getNodeUserId() {
        return nodeUserId;
    }

    @Override
    public void setNodeUserId(String nodeUserId) {
        this.nodeUserId = nodeUserId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getNodeInfo() {
        return nodeInfo;
    }

    @Override
    public void setNodeInfo(String nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    @Override
    public String getHandleType() {
        return handleType;
    }

    @Override
    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    @Override
    public Map<String, Object> getVariableMap() {
        return variableMap;
    }

    @Override
    public void setVariableMap(Map<String, Object> variableMap) {
        this.variableMap = variableMap;
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
