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
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.CurrentUserUtil;
import com.iqiny.silly.core.config.SillyConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.config.property.*;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;
import com.iqiny.silly.core.service.SillyService;
import org.apache.commons.collections.BeanMap;

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
public abstract class AbstractSillyService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable, T> implements SillyService {

    protected SillyFactory<M, N, V, ? extends SillyResume> sillyFactory;

    protected SillyEngineService<T> sillyEngineService;

    protected CurrentUserUtil currentUserUtil;

    protected SillyResumeService sillyResumeService;

    protected Map<String, SillyVariableConvertor> sillyConvertorMap;

    protected Map<String, SillyVariableSaveHandle> sillySaveHandleMap;

    protected SillyTaskGroupHandle sillyTaskGroupHandle;

    private Class<M> masterClass;
    private Class<N> nodeClass;
    private Class<V> variableClass;

    @Override
    public void init() {
        SillyConfig sillyConfig = getSillyConfig();
        SillyAssert.notNull(sillyConfig);
        setSillyFactory(sillyConfig.getSillyFactory(usedCategory()));
        setSillyEngineService(sillyConfig.getSillyEngineService());
        setCurrentUserUtil(sillyConfig.getCurrentUserUtil());
        setSillyConvertorMap(sillyConfig.getSillyConvertorMap());
        setSillySaveHandleMap(sillyConfig.getSillyVariableSaveHandleMap());
        setSillyResumeService(sillyConfig.getSillyResumeService());
        setSillyTaskGroupHandle(getSillyConfig().getSillyTaskGroupHandle());

        otherInit();
    }

    protected abstract void otherInit();

    @Override
    public SillyConfig getSillyConfig() {
        return SillyConfigUtil.getSillyConfig(usedCategory());
    }

    public void setSillyFactory(SillyFactory<M, N, V, ? extends SillyResume> sillyFactory) {
        this.sillyFactory = sillyFactory;
    }

    public void setSillyEngineService(SillyEngineService<T> sillyEngineService) {
        this.sillyEngineService = sillyEngineService;
    }

    public void setCurrentUserUtil(CurrentUserUtil currentUserUtil) {
        this.currentUserUtil = currentUserUtil;
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
    protected boolean batchSaveHandle(N node, V variables) {
        String saveHandleNames = variables.getSaveHandleName();
        SillyAssert.notEmpty(saveHandleNames, "批处理数据保存不可为空");
        String[] saveHandleNameArr = StringUtils.split(saveHandleNames, SillyConstant.ARRAY_SPLIT_STR);
        boolean lastFlag = true;
        for (String saveHandleName : saveHandleNameArr) {
            lastFlag = getSillyVariableSaveHandle(saveHandleName.trim()).handle(usedCategory(), node, variables);
        }
        return lastFlag;
    }

    protected SillyVariableSaveHandle getSillyVariableSaveHandle(String saveHandleName) {
        if (sillySaveHandleMap == null) {
            throw new SillyException("Silly数据保存处理器未设置！");
        }

        return sillySaveHandleMap.get(saveHandleName);
    }

    public SillyTaskGroupHandle getSillyTaskGroupHandle() {
        return sillyTaskGroupHandle;
    }

    public void setSillyTaskGroupHandle(SillyTaskGroupHandle sillyTaskGroupHandle) {
        this.sillyTaskGroupHandle = sillyTaskGroupHandle;
    }

    protected SillyPropertyHandle getSillyPropertyHandle(Map<String, Object> values) {
        SillyPropertyHandle sillyPropertyHandle = getSillyConfig().getSillyPropertyHandle();
        sillyPropertyHandle.setValues(new HashMap<>(values));
        return sillyPropertyHandle;
    }

    protected SillyPropertyHandle getSillyPropertyHandle() {
        SillyPropertyHandle sillyPropertyHandle = getSillyConfig().getSillyPropertyHandle();
        sillyPropertyHandle.setValues(new HashMap<>());
        return sillyPropertyHandle;
    }

    public SillyProcessProperty<?> processProperty() {
        return getSillyConfig().getSillyProcessProperty(usedCategory());
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
     * @param nodeKey
     * @return
     */
    public SillyProcessNodeProperty<?> getLastNodeProperty(String nodeKey) {
        SillyProcessProperty<?> property = processProperty();
        SillyAssert.notNull(property, "配置未找到 category：" + this.usedCategory());
        SillyPropertyHandle propertyHandle = getSillyPropertyHandle();
        String processKey = property.getLastProcessKey();
        if (StringUtils.isEmpty(nodeKey)) {
            nodeKey = property.getFirstNodeKey();
        }

        return getNodeProperty(propertyHandle.getStringValue(processKey), propertyHandle.getStringValue(nodeKey));
    }

    /**
     * 获取最新流程的最新节点配置属性
     *
     * @return
     */
    public SillyProcessNodeProperty<?> getLastNodeProcessProperty() {
        return getLastNodeProperty(null);
    }

    /**
     * 获取最新流程 KEY
     *
     * @return
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


}
