/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service.base;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyReflectUtil;
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
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.service.SillyEngineService;
import com.iqiny.silly.core.service.SillyService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 默认抽象方法
 *
 * @param <M> 主
 * @param <N> 节点
 * @param <V> 变量
 */
public abstract class AbstractSillyService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable, T> implements SillyService, SillyCategory {

    protected SillyFactory<M, N, V, ? extends SillyResume> sillyFactory;

    protected SillyEngineService<T> sillyEngineService;

    protected SillyCurrentUserUtil sillyCurrentUserUtil;

    protected SillyResumeService sillyResumeService;

    protected Map<String, SillyVariableConvertor> sillyConvertorMap;

    protected Map<String, SillyVariableSaveHandle> sillySaveHandleMap;

    protected SillyTaskGroupHandle sillyTaskGroupHandle;

    protected SillyCache sillyCache;

    private Class<M> masterClass;
    private Class<N> nodeClass;
    private Class<V> variableClass;

    @Override
    public void init() {
        SillyCategoryConfig sillyCategoryConfig = getSillyConfig();
        SillyAssert.notNull(sillyCategoryConfig);
        setSillyFactory(sillyCategoryConfig.getSillyFactory());
        setSillyEngineService(sillyCategoryConfig.getSillyEngineService());
        setCurrentUserUtil(sillyCategoryConfig.getSillyCurrentUserUtil());
        setSillyConvertorMap(sillyCategoryConfig.getSillyConvertorMap());
        setSillySaveHandleMap(sillyCategoryConfig.getSillyVariableSaveHandleMap());
        setSillyResumeService(sillyCategoryConfig.getSillyResumeService());
        setSillyTaskGroupHandle(sillyCategoryConfig.getSillyTaskGroupHandle());
        setSillyCache(sillyCategoryConfig.getSillyCache());

        otherInit();
    }

    protected abstract void otherInit();

    @Override
    public SillyCategoryConfig getSillyConfig(String category) {
        return SillyConfigUtil.getSillyConfig(category);
    }

    public SillyCategoryConfig getSillyConfig() {
        return getSillyConfig(usedCategory());
    }

    public void setSillyFactory(SillyFactory<M, N, V, ? extends SillyResume> sillyFactory) {
        this.sillyFactory = sillyFactory;
    }

    public void setSillyEngineService(SillyEngineService<T> sillyEngineService) {
        this.sillyEngineService = sillyEngineService;
    }

    public void setCurrentUserUtil(SillyCurrentUserUtil sillyCurrentUserUtil) {
        this.sillyCurrentUserUtil = sillyCurrentUserUtil;
    }

    public void setSillyConvertorMap(Map<String, SillyVariableConvertor> sillyConvertorMap) {
        this.sillyConvertorMap = sillyConvertorMap;
    }

    public void setSillyResumeService(SillyResumeService sillyResumeService) {
        this.sillyResumeService = sillyResumeService;
    }

    public void setSillySaveHandleMap(Map<String, SillyVariableSaveHandle> sillySaveHandleMap) {
        this.sillySaveHandleMap = sillySaveHandleMap;
    }

    protected SillyVariableConvertor<?> getSillyConvertor(String handleKey) {
        if (sillyConvertorMap == null) {
            throw new SillyException("Silly数据处理器未设置！");
        }

        return sillyConvertorMap.get(handleKey);
    }

    /**
     * 批量保存处置类处理
     *
     * @param node
     * @param variables
     * @return
     */
    protected boolean batchSaveHandle(M master, N node, V variables, SillyPropertyHandle propertyHandle) {
        String saveHandleNames = variables.getSaveHandleName();
        SillyAssert.notEmpty(saveHandleNames, "批处理数据保存不可为空");
        String[] saveHandleNameArr = StringUtils.split(saveHandleNames, SillyConstant.ARRAY_SPLIT_STR);
        boolean lastFlag = true;
        for (String saveHandleName : saveHandleNameArr) {
            String stringValue = propertyHandle.getStringValue(saveHandleName.trim());
            if (StringUtils.isNotEmpty(stringValue)) {
                lastFlag = getSillyVariableSaveHandle(stringValue).handle(master, node, variables);
            }
        }
        return lastFlag;
    }

    protected SillyVariableSaveHandle getSillyVariableSaveHandle(String saveHandleName) {
        if (sillySaveHandleMap == null) {
            throw new SillyException("Silly数据保存处理器未设置！");
        }
        SillyVariableSaveHandle sillyVariableSaveHandle = sillySaveHandleMap.get(saveHandleName);
        SillyAssert.notNull(sillyVariableSaveHandle, "saveHandleName：" + saveHandleName + " 未进行配置");
        return sillyVariableSaveHandle;
    }

    public SillyTaskGroupHandle getSillyTaskGroupHandle() {
        return sillyTaskGroupHandle;
    }

    public void setSillyTaskGroupHandle(SillyTaskGroupHandle sillyTaskGroupHandle) {
        this.sillyTaskGroupHandle = sillyTaskGroupHandle;
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
        if (variableClass == null) {
            Type[] actualTypeArgument = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
            variableClass = (Class<V>) SillyReflectUtil.getSuperClassGenericType((Class<?>) actualTypeArgument[1], 1);
        }
        return variableClass;
    }

    protected Class<N> nodeClass() {
        if (nodeClass == null) {
            Type[] actualTypeArgument = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
            nodeClass = (Class<N>) SillyReflectUtil.getSuperClassGenericType((Class<?>) actualTypeArgument[1], 0);
        }
        return nodeClass;
    }

    protected Class<M> masterClass() {
        if (masterClass == null) {
            Type[] actualTypeArgument = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
            masterClass = (Class<M>) SillyReflectUtil.getSuperClassGenericType((Class<?>) actualTypeArgument[0], 0);
        }
        return masterClass;
    }

    public SillyCache getSillyCache() {
        return sillyCache;
    }

    public void setSillyCache(SillyCache sillyCache) {
        this.sillyCache = sillyCache;
    }
}
