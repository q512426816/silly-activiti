/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service.base;

import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.cache.SillyCache;
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.config.property.*;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.service.SillyService;

import java.util.*;

/**
 * 默认抽象方法
 *
 * @param <M> 主
 * @param <N> 节点
 * @param <V> 变量
 */
@SuppressWarnings("unchecked")
public abstract class AbstractSillyService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> implements SillyService, SillyCategory {

    protected SillyFactory<M, N, V, ? extends SillyResume> sillyFactory;

    protected SillyEngineService<? extends SillyTask> sillyEngineService;

    protected SillyCurrentUserUtil sillyCurrentUserUtil;

    protected SillyResumeService<? extends SillyResume> sillyResumeService;

    protected Map<String, SillyVariableConvertor> sillyConvertorMap;

    protected Map<String, SillyVariableSaveHandle> sillySaveHandleMap;

    protected SillyTaskGroupHandle sillyTaskGroupHandle;

    protected SillyCache sillyCache;

    private Class<M> masterClass;
    private Class<N> nodeClass;
    private Class<V> variableClass;


    public String processKeyMapKey() {
        return "processKey";
    }

    public String nodeKeyMapKey() {
        return "nodeKey";
    }

    public String masterIdMapKey() {
        return "id";
    }

    public String submitKey() {
        return "submit";
    }

    public String startProcessKey() {
        return "startProcess";
    }

    public String taskIdKey() {
        return "taskId";
    }

    @Override
    public void init() {
        SillyCategoryConfig sillyCategoryConfig = getSillyConfig();
        SillyAssert.notNull(sillyCategoryConfig);

        this.sillyFactory = sillyCategoryConfig.getSillyFactory();
        this.sillyEngineService = sillyCategoryConfig.getSillyEngineService();
        this.sillyCurrentUserUtil = sillyCategoryConfig.getSillyCurrentUserUtil();
        this.sillyConvertorMap = sillyCategoryConfig.getSillyConvertorMap();
        this.sillySaveHandleMap = sillyCategoryConfig.getSillyVariableSaveHandleMap();
        this.sillyResumeService = sillyCategoryConfig.getSillyResumeService();
        this.sillyTaskGroupHandle = sillyCategoryConfig.getSillyTaskGroupHandle();
        this.sillyCache = sillyCategoryConfig.getSillyCache();
        setEntityClazz(sillyCategoryConfig.getSillyFactory());

        otherInit();
    }

    private void setEntityClazz(SillyFactory sillyFactory) {
        masterClass = (Class<M>) sillyFactory.newMaster().getClass();
        nodeClass = (Class<N>) sillyFactory.newNode().getClass();
        variableClass = (Class<V>) sillyFactory.newVariable().getClass();
    }

    protected abstract void otherInit();

    public SillyCategoryConfig getSillyConfig() {
        return SillyConfigUtil.getSillyConfig(usedCategory());
    }

    protected SillyVariableConvertor<?> getSillyConvertor(String handleKey) {
        if (sillyConvertorMap == null) {
            throw new SillyException("Silly数据处理器未设置！");
        }

        return sillyConvertorMap.get(handleKey);
    }

    protected SillyVariableSaveHandle getSillyVariableSaveHandle(String saveHandleName) {
        if (sillySaveHandleMap == null) {
            throw new SillyException("Silly数据保存处理器未设置！");
        }
        SillyVariableSaveHandle sillyVariableSaveHandle = sillySaveHandleMap.get(saveHandleName);
        SillyAssert.notNull(sillyVariableSaveHandle, "saveHandleName：" + saveHandleName + " 未进行配置");
        return sillyVariableSaveHandle;
    }

    protected Object getPropertyHandleRoot(String masterId) {
        Object root = getPropertyHandleRootCache(masterId);
        if (root == null) {
            root = getPropertyHandleRootDB(masterId);
        }
        return root;
    }


    protected Object getPropertyHandleRootDB(String masterId) {
        List<V> nodeList = getSillyConfig().getSillyReadService().getVariableList(masterId, null);
        return getSillyConfig().getSillyReadService().variableList2Map(nodeList);
    }

    protected Object getPropertyHandleRootCache(String masterId) {
        return sillyCache.getPropertyHandleRootCache(usedCategory(), masterId);
    }

    protected SillyPropertyHandle getSillyPropertyHandle(String masterId, Map<String, Object> values) {
        SillyPropertyHandle sillyPropertyHandle = getSillyConfig().newSillyPropertyHandle();
        if (StringUtils.isNotEmpty(masterId)) {
            sillyPropertyHandle.setRoot(getPropertyHandleRoot(masterId));
        }
        sillyPropertyHandle.setValues(new HashMap<>(values));
        return sillyPropertyHandle;
    }

    public SillyProcessProperty<? extends SillyProcessMasterProperty> processProperty() {
        return getSillyConfig().getSillyProcessProperty();
    }

    public SillyProcessNodeProperty<?> getNodeProperty(String processKey, String nodeKey) {
        SillyProcessMasterProperty<?> masterProperty = getMasterProperty(processKey);
        SillyAssert.notNull(masterProperty, "配置未找到 processKey：" + processKey);
        SillyProcessNodeProperty<?> nodeProperty = masterProperty.getNode().get(nodeKey);
        SillyAssert.notNull(nodeProperty, "配置未找到 nodeKey：" + nodeKey);
        return nodeProperty;
    }

    public SillyProcessMasterProperty<?> getMasterProperty(String processKey) {
        SillyProcessProperty<?> property = processProperty();
        SillyAssert.notNull(property, "配置未找到 category：" + usedCategory());
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
    public SillyProcessNodeProperty<?> getLastNodeProperty(String processKey) {
        SillyAssert.notEmpty(processKey, "查询第一个节点KEY，流程KEY不可为空");
        Set<String> nodeKeySet = getMasterProperty(processKey).getNode().keySet();
        // 取node 中第一个定义的为首节点
        String nodeKey = nodeKeySet.iterator().next();
        return getNodeProperty(processKey, nodeKey);
    }

    /**
     * 获取最新流程的最新节点配置属性
     *
     * @return
     */
    public SillyProcessNodeProperty<?> getLastNodeProcessProperty() {
        SillyProcessProperty<? extends SillyProcessMasterProperty> property = processProperty();
        String processKey = property.getLastProcessKey();
        return getLastNodeProperty(processKey);
    }

    /**
     * 获取最新流程 KEY
     */
    public String getLastProcessKey() {
        return processProperty().getLastProcessKey();
    }


    protected Class<V> variableClass() {
        return variableClass;
    }

    protected Class<N> nodeClass() {
        return nodeClass;
    }

    protected Class<M> masterClass() {
        return masterClass;
    }

}
