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
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.CurrentUserUtil;
import com.iqiny.silly.core.config.SillyConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyProcessVariableProperty;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;
import com.iqiny.silly.core.service.SillyService;
import org.apache.commons.collections.BeanMap;

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

    protected SillyTaskGroupHandle sillyTaskGroupHandle;

    @Override
    public void init() {
        SillyConfig sillyConfig = getSillyConfig();
        SillyAssert.notNull(sillyConfig);
        setSillyFactory(sillyConfig.getSillyFactory(usedCategory()));
        setSillyEngineService(sillyConfig.getSillyEngineService());
        setCurrentUserUtil(sillyConfig.getCurrentUserUtil());
        setSillyConvertorMap(sillyConfig.getSillyConvertorMap());
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

    protected SillyVariableConvertor<?> getSillyConvertor(String handleKey) {
        if (sillyConvertorMap == null) {
            throw new SillyException("Silly数据处理器未设置！");
        }

        return sillyConvertorMap.get(handleKey);
    }

    public SillyTaskGroupHandle getSillyTaskGroupHandle() {
        return sillyTaskGroupHandle;
    }

    public void setSillyTaskGroupHandle(SillyTaskGroupHandle sillyTaskGroupHandle) {
        this.sillyTaskGroupHandle = sillyTaskGroupHandle;
    }

    public SillyProcessProperty<?> processProperty() {
        return getSillyConfig().getSillyProcessProperty(usedCategory());
    }

    public SillyProcessNodeProperty<?> getNodeProperty(String processKey, String nodeKey) {
        SillyProcessProperty<?> property = processProperty();
        SillyAssert.notNull(property, "配置未找到 category：" + usedCategory());
        SillyProcessMasterProperty<?> masterProperty = property.getMaster().get(processKey);
        SillyAssert.notNull(masterProperty, "配置未找到 processKey：" + processKey);
        SillyProcessNodeProperty<?> nodeProperty = masterProperty.getNode().get(nodeKey);
        SillyAssert.notNull(nodeProperty, "配置未找到 nodeKey：" + nodeKey);
        return nodeProperty;
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
        String processKey = property.getLastProcessKey();
        if (StringUtils.isEmpty(nodeKey)) {
            nodeKey = property.getFirstNodeKey();
        }

        return getNodeProperty(processKey, nodeKey);
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

}
