/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.cache.SillyCache;
import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.config.property.valuesfield.SillyPropertyValuesField;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务配置类
 */
@SuppressWarnings("all")
public class DefaultSillyCategoryConfig<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> implements SillyCategoryConfig<M, N, V> {

    private final static Log log = LogFactory.getLog(DefaultSillyCategoryConfig.class);

    private boolean initialized = false;
    private boolean refresh = false;

    protected String category;

    protected Class<? extends SillyPropertyHandle> propertyHandleClazz;

    /**
     * 傻瓜上下文对象
     */
    protected SillyContext sillyContext;

    /**
     * 当前人获取工具
     */
    protected SillyCurrentUserUtil sillyCurrentUserUtil;

    /**
     * 流程变量 类型转换器
     */
    protected Map<String, SillyVariableConvertor> sillyConvertorMap;

    /**
     * 流程变量 类型转换器
     */
    protected Map<String, SillyVariableSaveHandle> sillyVariableSaveHandleMap;

    /**
     * 傻瓜工厂工厂
     */
    protected SillyFactory<M, N, V, SillyResume> sillyFactory;

    /**
     * 流程引擎服务，流程控制服务
     */
    protected SillyEngineService sillyEngineService;

    /**
     * 流程履历记录服务
     */
    protected SillyResumeService sillyResumeService;

    /**
     * 傻瓜读取服务类
     */
    protected SillyReadService<M, N, V> sillyReadService;

    /**
     * 傻瓜写入服务类
     */
    protected SillyWriteService<M, N, V> sillyWriteService;

    /**
     * 傻瓜流程参数属性配置
     */
    protected SillyProcessProperty sillyProcessProperty;

    /**
     * 傻瓜缓存属性配置
     */
    protected SillyCache sillyCache;

    /**
     * 傻瓜任务组处理类
     */
    protected SillyTaskGroupHandle sillyTaskGroupHandle;

    @Override
    public synchronized void init() {
        if (this.initialized) {
            return;
        }

        preInit();

        doInit();

        checkConfig();

        // 设置配置静态工具
        if (this.refresh) {
            SillyConfigUtil.refreshSillyConfig(this);
        } else {
            SillyConfigUtil.addSillyConfig(this);
        }

        this.initialized = true;
        this.refresh = false;
        initComplete();
    }

    @Override
    public void refresh() {
        log.info("开始刷新配置信息 " + usedCategory());
        this.initialized = false;
        this.refresh = true;
        init();

        log.info("完成配置信息刷新 " + usedCategory());
    }

    /**
     * 初始化
     */
    protected void doInit() {

    }

    /**
     * 初始化之前 的回调方法
     */
    protected void preInit() {
    }

    /**
     * 必要的属性配置校验
     */
    protected void checkConfig() {
        SillyAssert.notEmpty(usedCategory(), "silly Config usedCategory 未配置");

        SillyAssert.notNull(this.sillyContext, "sillyContext 未配置" + usedCategory());
        SillyAssert.notNull(this.sillyCurrentUserUtil, "currentUserUtil 未配置" + usedCategory());
        SillyAssert.notNull(this.sillyEngineService, "sillyEngineService 未配置" + usedCategory());
        SillyAssert.notNull(this.sillyResumeService, "sillyResumeService 未配置" + usedCategory());
        SillyAssert.notNull(this.sillyFactory, "sillyFactory 未配置" + usedCategory());
        SillyAssert.notNull(this.sillyReadService, "sillyReadService 未配置" + usedCategory());
        SillyAssert.notNull(this.sillyWriteService, "sillyWriteService 未配置" + usedCategory());
        SillyAssert.notNull(this.sillyConvertorMap, "sillyConvertorMap 未配置" + usedCategory());
        SillyAssert.notNull(this.sillyVariableSaveHandleMap, "sillyVariableSaveHandleMap 未配置" + usedCategory());
        if (this.sillyProcessProperty == null) {
            log.info("sillyProcessProperty 未配置" + usedCategory());
        }
        if (this.sillyCache == null) {
            log.info("sillyCache 未配置" + usedCategory());
        }
    }


    /**
     * 初始化完成后 的回调方法
     */
    protected void initComplete() {
    }


    @Override
    public SillyContext getSillyContext() {
        return sillyContext;
    }

    @Override
    public SillyCategoryConfig<M, N, V> setSillyContext(SillyContext sillyContext) {
        this.sillyContext = sillyContext;
        return this;
    }

    @Override
    public Class<? extends SillyPropertyHandle> getPropertyHandleClazz() {
        return propertyHandleClazz;
    }

    @Override
    public DefaultSillyCategoryConfig setPropertyHandleClazz(Class<? extends SillyPropertyHandle> propertyHandleClazz) {
        this.propertyHandleClazz = propertyHandleClazz;
        return this;
    }

    @Override
    public Map<String, SillyVariableConvertor> getSillyConvertorMap() {
        return sillyConvertorMap;
    }

    @Override
    public Map<String, SillyVariableSaveHandle> getSillyVariableSaveHandleMap() {
        return sillyVariableSaveHandleMap;
    }

    @Override
    public SillyFactory<M, N, V, SillyResume> getSillyFactory() {
        return sillyFactory;
    }

    @Override
    public SillyEngineService getSillyEngineService() {
        return sillyEngineService;
    }

    @Override
    public SillyResumeService getSillyResumeService() {
        return sillyResumeService;
    }

    @Override
    public SillyReadService<M, N, V> getSillyReadService() {
        return sillyReadService;
    }

    @Override
    public SillyWriteService<M, N, V> getSillyWriteService() {
        return sillyWriteService;
    }

    @Override
    public SillyProcessProperty getSillyProcessProperty() {
        return sillyProcessProperty;
    }

    @Override
    public SillyTaskGroupHandle getSillyTaskGroupHandle() {
        return sillyTaskGroupHandle;
    }

    @Override
    public SillyCategoryConfig<M, N, V> setSillyTaskGroupHandle(SillyTaskGroupHandle sillyTaskGroupHandle) {
        this.sillyTaskGroupHandle = sillyTaskGroupHandle;
        return this;
    }

    @Override
    public SillyCache getSillyCache() {
        return sillyCache;
    }

    @Override
    public SillyPropertyHandle newSillyPropertyHandle(SillyNodeSourceData sourceData) {
        SillyPropertyHandle propertyHandle = SillyReflectUtil.newInstance(getPropertyHandleClazz());
        SillyContext sillyContext = getSillyContext();
        propertyHandle.setSillyContext(sillyContext);
        List<SillyPropertyValuesField> beanList = sillyContext.getBeanList(getCategory(), SillyPropertyValuesField.class);
        HashMap<String, Object> valuesMap = new HashMap<>(256);
        for (SillyPropertyValuesField rootField : beanList) {
            valuesMap.put(rootField.fieldName(), rootField.value(sourceData));
        }
        valuesMap.putAll(sourceData.getMap());
        propertyHandle.setValues(valuesMap);

        String masterId = sourceData.masterId();
        if (StringUtils.isNotEmpty(masterId)) {
            SillyReadService sillyReadService = getSillyReadService();
            Map<String, Object> root = sillyReadService.queryRoot(masterId);
            propertyHandle.setRoot(root);
        }
        return propertyHandle;
    }

    @Override
    public String usedCategory() {
        return category;
    }

    public String getCategory() {
        return category;
    }

    public DefaultSillyCategoryConfig setCategory(String category) {
        this.category = category;
        return this;
    }

    public SillyCurrentUserUtil getSillyCurrentUserUtil() {
        return sillyCurrentUserUtil;
    }

    @Override
    public DefaultSillyCategoryConfig setSillyCurrentUserUtil(SillyCurrentUserUtil sillyCurrentUserUtil) {
        this.sillyCurrentUserUtil = sillyCurrentUserUtil;
        return this;
    }

    @Override
    public DefaultSillyCategoryConfig setSillyConvertorMap(Map<String, SillyVariableConvertor> sillyConvertorMap) {
        this.sillyConvertorMap = sillyConvertorMap;
        return this;
    }

    @Override
    public DefaultSillyCategoryConfig setSillyVariableSaveHandleMap(Map<String, SillyVariableSaveHandle> sillyVariableSaveHandleMap) {
        this.sillyVariableSaveHandleMap = sillyVariableSaveHandleMap;
        return this;
    }

    @Override
    public DefaultSillyCategoryConfig setSillyFactory(SillyFactory<M, N, V, SillyResume> sillyFactory) {
        this.sillyFactory = sillyFactory;
        return this;
    }

    @Override
    public DefaultSillyCategoryConfig setSillyEngineService(SillyEngineService sillyEngineService) {
        this.sillyEngineService = sillyEngineService;
        return this;
    }

    @Override
    public DefaultSillyCategoryConfig setSillyResumeService(SillyResumeService sillyResumeService) {
        this.sillyResumeService = sillyResumeService;
        return this;
    }

    public DefaultSillyCategoryConfig setSillyReadService(SillyReadService<M, N, V> sillyReadService) {
        this.sillyReadService = sillyReadService;
        return this;
    }

    @Override
    public DefaultSillyCategoryConfig setSillyWriteService(SillyWriteService<M, N, V> sillyWriteService) {
        this.sillyWriteService = sillyWriteService;
        return this;
    }

    @Override
    public DefaultSillyCategoryConfig setSillyProcessProperty(SillyProcessProperty sillyProcessProperty) {
        this.sillyProcessProperty = sillyProcessProperty;
        return this;
    }

    @Override
    public DefaultSillyCategoryConfig setSillyCache(SillyCache sillyCache) {
        this.sillyCache = sillyCache;
        return this;
    }

    @Override
    public SillyProcessMasterProperty<?> getMasterProperty(String processKey) {
        SillyProcessProperty<?> property = getSillyProcessProperty();
        SillyAssert.notNull(property, "配置未找到 category：" + usedCategory());
        if (StringUtils.isEmpty(processKey)) {
            processKey = property.getLastProcessKey();
        }
        SillyProcessMasterProperty<?> masterProperty = property.getMaster().get(processKey);
        SillyAssert.notNull(masterProperty, "配置未找到 processKey：" + processKey);
        return masterProperty;
    }

    @Override
    public SillyProcessNodeProperty<?> getNodeProperty(String processKey, String nodeKey) {
        SillyProcessMasterProperty<?> masterProperty = getMasterProperty(processKey);
        SillyAssert.notNull(masterProperty, "配置未找到 processKey：" + processKey);

        if (StringUtils.isEmpty(nodeKey)) {
            // 没设置 nodeKey 则获取第一个节点
            nodeKey = masterProperty.getNode().keySet().iterator().next();
        }

        SillyProcessNodeProperty<?> nodeProperty = masterProperty.getNode().get(nodeKey);
        SillyAssert.notNull(nodeProperty, "配置未找到 nodeKey：" + nodeKey);

        return nodeProperty;
    }

}
