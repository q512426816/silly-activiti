/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.SillyProperties;
import com.iqiny.silly.core.cache.SillyCache;
import com.iqiny.silly.core.common.SillyCoreUtil;
import com.iqiny.silly.core.config.html.SillyHtmlAutoTag;
import com.iqiny.silly.core.config.html.SillyHtmlTagConfig;
import com.iqiny.silly.core.config.html.SillyHtmlTagTemplate;
import com.iqiny.silly.core.config.property.*;
import com.iqiny.silly.core.convertor.*;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.group.SillyTaskCategoryGroup;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.savehandle.SillyNodeSaveHandle;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.savehandle.node.*;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * 配置类 资源
 */
@SuppressWarnings("all")
public abstract class BaseSillyConfigContent implements SillyConfigContent {

    private final static Log log = LogFactory.getLog(BaseSillyConfigContent.class);

    protected final SillyProperties sillyProperties;
    protected final SillyContext sillyContext;

    private boolean initialized = false;
    private boolean refresh = false;

    /**
     * 全部已识别的业务分类
     */
    protected final Set<String> categorySet = new LinkedHashSet<>();

    public BaseSillyConfigContent(SillyProperties sillyProperties, SillyContext sillyContext) {
        SillyAssert.notNull(sillyProperties, "sillyProperties 不可为空");
        SillyAssert.notNull(sillyContext, "sillyContext 不可为空");

        this.sillyProperties = sillyProperties;
        this.sillyContext = sillyContext;
    }


    public Set<String> allCategorySet() {
        loadCategorySet();
        return categorySet;
    }

    protected abstract void loadCategorySet();

    protected Class<? extends SillyCategoryConfig> sillyCategoryConfigClazz = DefaultSillyCategoryConfig.class;

    /**
     * 傻瓜上下文对象
     */
    protected List<SillyContext> sillyContextList = new ArrayList<>();

    /**
     * 业务配置信息集合
     */
    protected List<SillyCategoryConfig> sillyCategoryConfigList = new ArrayList<>();

    /**
     * Category 当前人获取工具
     */
    protected final List<SillyCurrentUserUtil> sillyCurrentUserUtilList = new ArrayList<>();

    /**
     * Category 流程引擎服务，流程控制服务
     */
    protected final List<SillyEngineService> sillyEngineServiceList = new ArrayList<>();

    /**
     * Category 流程履历记录服务
     */
    protected final List<SillyResumeService> sillyResumeServiceList = new ArrayList<>();

    /**
     * Category 傻瓜工厂工厂
     */
    protected final List<SillyFactory> sillyFactoryList = new ArrayList<>();

    /**
     * Category 傻瓜读取服务类
     */
    protected final List<SillyReadService> sillyReadServiceList = new ArrayList<>();

    /**
     * Category 傻瓜写入服务类
     */
    protected final List<SillyWriteService> sillyWriteServiceList = new ArrayList<>();

    /**
     * Category 傻瓜流程参数属性配置
     */
    protected final List<SillyProcessProperty> sillyProcessPropertyList = new ArrayList<>();

    /**
     * Category 傻瓜缓存属性配置
     */
    protected final List<SillyCache> sillyCacheList = new ArrayList<>();

    /**
     * Name 流程变量 类型转换器
     */
    protected final List<SillyVariableConvertor> sillyConvertorList = new ArrayList<>();

    /**
     * 变量保存处理器
     */
    protected final List<SillyVariableSaveHandle> sillyVariableSaveHandleList = new ArrayList<>();

    /**
     * 节点保存处理器
     */
    protected final List<SillyNodeSaveHandle> sillyNodeSaveHandleList = new ArrayList<>();

    /**
     * 傻瓜任务组处理类
     */
    protected SillyTaskGroupHandle sillyTaskGroupHandle;

    /**
     * Type 傻瓜HTML 标签模板配置
     */
    protected final List<SillyHtmlTagTemplate> sillyHtmlTagTemplateList = new ArrayList<>();

    protected final Map<String, Map<String, SillyHtmlTagTemplate>> sillyCategoryHtmlTagTemplateMap = new HashMap<>();

    public Class<? extends SillyCategoryConfig> getSillyCategoryConfigClazz(String category) {
        return sillyCategoryConfigClazz;
    }

    @Override
    public synchronized void init() {
        if (this.initialized) {
            return;
        }

        // 0 预初始化
        preInit();
        // 1 初始化 基本属性
        initFiled();
        // 2 初始化上下文
        initSillyContextList();
        // 3 初始化 业务分类
        initCategorySet();
        // 4 初始化 当前人工具
        initCurrentUserUtilList();
        // 5 初始化 傻瓜工厂
        initSillyFactory();
        // 6 初始化 傻瓜转换器
        initSillyConvertorList();
        // 7 初始化 傻瓜数据保存器
        initSillySaveHandleList();
        // 8 初始化 傻瓜页面代码生成模板
        initSillyHtmlTagTemplateList();
        // 9 初始化 傻瓜服务
        initSillyService();
        // 10 初始化 傻瓜流程参数
        initSillyProcessProperty();
        // 11 初始化 傻瓜任务组处理类
        initSillyTaskGroupHandle();
        // 12 初始化 傻瓜缓存配置
        initSillyCacheList();

        // 13 校验配置
        checkConfig();

        // 14 将配置资源转换为业务配置
        initSillyCategoryConfigList();

        this.initialized = true;
        this.refresh = false;

        // 15 初始化完成回调
        initComplete();
    }

    private void initSillyCategoryConfigList() {
        sillyCategoryConfigList = convertSillyConfig();
        for (SillyCategoryConfig sillyCategoryConfig : sillyCategoryConfigList) {
            if (this.refresh) {
                sillyCategoryConfig.refresh();
            } else {
                sillyCategoryConfig.init();
            }
        }
    }

    private void initSillyContextList() {
        hookSillyContextList();
    }

    protected abstract void hookSillyContextList();


    protected void addSillyContext(SillyContext sillyContext) {
        sillyContextList.add(sillyContext);
    }

    @Override
    public void refresh() {
        this.refresh = true;
        this.initialized = false;
        init();
    }

    /**
     * 初始化配置属性
     */
    protected void initFiled() {
        categorySet.clear();
        sillyContextList.clear();
        sillyCategoryConfigList.clear();
        sillyCurrentUserUtilList.clear();
        sillyEngineServiceList.clear();
        sillyResumeServiceList.clear();
        sillyFactoryList.clear();
        sillyReadServiceList.clear();
        sillyWriteServiceList.clear();
        sillyProcessPropertyList.clear();
        sillyCacheList.clear();
        sillyConvertorList.clear();
        sillyVariableSaveHandleList.clear();
        sillyTaskGroupHandle = null;
        sillyHtmlTagTemplateList.clear();
        sillyCategoryHtmlTagTemplateMap.clear();
    }

    /**
     * 初始化之前 的回调方法
     */
    protected abstract void preInit();

    /**
     * 初始化完成后 的回调方法
     */
    protected abstract void initComplete();

    protected void checkConfig() {

    }

    protected void initCategorySet() {
        loadCategorySet();
    }

    protected void addCategorySet(String category) {
        categorySet.add(category);
    }

    private void initSillyFactory() {
        initBaseSillyFactoryList();
    }


    protected void addSillyFactory(SillyFactory sillyFactory) {
        sillyFactoryList.add(sillyFactory);
    }

    /**
     * 初始化基本傻瓜工厂，可能会被相同种类的工厂覆盖
     */
    protected abstract void initBaseSillyFactoryList();

    protected void initSillyConvertorList() {
        addSillyVariableConvertor(new SillyStringConvertor());
        addSillyVariableConvertor(new SillyListConvertor());
        addSillyVariableConvertor(new SillyDateConvertor());
        addSillyVariableConvertor(new SillyDateTimeConvertor());
        hookInitSillyConvertorList();
    }

    protected void initSillySaveHandleList() {
        hookInitSillyNodeSaveHandleList();
        hookInitSillyVariableSaveHandleList();
    }

    private void registerSillyNodeSaveHandle() {
        // 加载 任务及主表数据
        sillyContext.registerBean(SillyLoadNowTaskSaveHandle.NAME, SillyLoadNowTaskSaveHandle.class);
        sillyContext.registerBean(SillyLoadMasterByNewSaveHandle.NAME, SillyLoadMasterByNewSaveHandle.class);
        sillyContext.registerBean(SillyLoadMasterByIdSaveHandle.NAME, SillyLoadMasterByIdSaveHandle.class);
        sillyContext.registerBean(SillyLoadMasterByTaskSaveHandle.NAME, SillyLoadMasterByTaskSaveHandle.class);

        // 生成参数读取处理器
        sillyContext.registerBean(SillyPropertyHandleCreateSaveHandle.NAME, SillyPropertyHandleCreateSaveHandle.class);

        // 获取节点配置信息
        sillyContext.registerBean(SillyLoadNodePropertyByTaskSaveHandle.NAME, SillyLoadNodePropertyByTaskSaveHandle.class);
        sillyContext.registerBean(SillyLoadNodePropertyByNotTaskSaveHandle.NAME, SillyLoadNodePropertyByNotTaskSaveHandle.class);

        // 创建节点对象信息
        sillyContext.registerBean(SillyLoadNodeInfoSaveHandle.NAME, SillyLoadNodeInfoSaveHandle.class);

        // 提交对象Map ->转-> 数据集合varList
        sillyContext.registerBean(SillyMapToVarSaveHandle.NAME, SillyMapToVarSaveHandle.class);
        sillyContext.registerBean(SillyCheckVariableFieldsSaveHandle.NAME, SillyCheckVariableFieldsSaveHandle.class);

        // 生成流程变量数据
        sillyContext.registerBean(SillyVarToProcessMapSaveHandle.NAME, SillyVarToProcessMapSaveHandle.class);

        // 启动流程
        sillyContext.registerBean(SillyProcessStartSaveHandle.NAME, SillyProcessStartSaveHandle.class);

        // 根据varList 生成 主对象属性  节点对象属性
        sillyContext.registerBean(SillyVarToMasterSaveHandle.NAME, SillyVarToMasterSaveHandle.class);
        sillyContext.registerBean(SillyVarToNodeSaveHandle.NAME, SillyVarToNodeSaveHandle.class);

        // 节点数据 及 变量数据 转 历史数据
        sillyContext.registerBean(SillyVariableToHistorySaveHandle.NAME, SillyVariableToHistorySaveHandle.class);
        sillyContext.registerBean(SillyNodeToHistorySaveHandle.NAME, SillyNodeToHistorySaveHandle.class);

        // 节点数据 保存
        sillyContext.registerBean(SillyNodeInsertSaveHandle.NAME, SillyNodeInsertSaveHandle.class);

        // 节点变量数据设置 及 变量 variableSaveHandle 执行 (变量数量 可能变少)
        sillyContext.registerBean(SillyNodeVariableHandleSaveHandle.NAME, SillyNodeVariableHandleSaveHandle.class);

        // 节点变量数据设置 及 变量 variableConvertor 执行  (变量数量 可能变多)
        sillyContext.registerBean(SillyNodeVariableConvertorSaveHandle.NAME, SillyNodeVariableConvertorSaveHandle.class);

        // 节点变量数据 保存
        sillyContext.registerBean(SillyNodeVariableInsertSaveHandle.NAME, SillyNodeVariableInsertSaveHandle.class);

        // 流程提交
        sillyContext.registerBean(SillyProcessSubmitSaveHandle.NAME, SillyProcessSubmitSaveHandle.class);

        // 提交之后的 处置
        sillyContext.registerBean(SillyLoadNextTaskSaveHandle.NAME, SillyLoadNextTaskSaveHandle.class);
        sillyContext.registerBean(SillyAfterCompleteSaveHandle.NAME, SillyAfterCompleteSaveHandle.class);
        sillyContext.registerBean(SillyAfterCloseSaveHandle.NAME, SillyAfterCloseSaveHandle.class);

        // 履历记录
        sillyContext.registerBean(SillyResumeCreateSaveHandle.NAME, SillyResumeCreateSaveHandle.class);
        sillyContext.registerBean(SillyResumeRecordSaveHandle.NAME, SillyResumeRecordSaveHandle.class);

        // 更新主表 及 root 信息
        sillyContext.registerBean(SillyMasterUpdateSaveHandle.NAME, SillyMasterUpdateSaveHandle.class);
        sillyContext.registerBean(SillyUpdateCachePropertyHandleRootSaveHandle.NAME, SillyUpdateCachePropertyHandleRootSaveHandle.class);

    }

    protected void addSillyVariableConvertor(SillyVariableConvertor convertor) {
        sillyConvertorList.add(convertor);
    }

    protected void addSillyVariableSaveHandle(SillyVariableSaveHandle saveHandle) {
        sillyVariableSaveHandleList.add(saveHandle);
    }

    protected void addSillyNodeSaveHandle(SillyNodeSaveHandle saveHandle) {
        sillyNodeSaveHandleList.add(saveHandle);
    }

    /**
     * 初始完成 傻瓜转换器 回调方法
     */
    protected abstract void hookInitSillyConvertorList();


    protected abstract void hookInitSillyVariableSaveHandleList();

    protected abstract void hookInitSillyNodeSaveHandleList();

    protected void initSillyHtmlTagTemplateList() {
        hookInitSillyHtmlTagTemplateList();
    }

    protected void addSillyHtmlTagTemplate(SillyHtmlTagTemplate htmlTagTemplate) {
        sillyHtmlTagTemplateList.add(htmlTagTemplate);
    }


    /**
     * 初始完成 傻瓜转换器 回调方法
     */
    protected abstract void hookInitSillyHtmlTagTemplateList();

    protected void initSillyService() {
        initEngineSillyService();
        initResumeSillyService();
        initReadSillyService();
        initWriteSillyService();
    }

    protected abstract void initEngineSillyService();

    protected abstract void initResumeSillyService();

    protected abstract void initReadSillyService();

    protected abstract void initWriteSillyService();

    protected void addSillyEngineService(SillyEngineService engineService) {
        sillyEngineServiceList.add(engineService);
    }

    protected void addSillyResumeService(SillyResumeService resumeService) {
        sillyResumeServiceList.add(resumeService);
    }

    protected void addSillyReadService(SillyReadService readService) {
        sillyReadServiceList.add(readService);
    }

    protected void addSillyWriteService(SillyWriteService writeService) {
        sillyWriteServiceList.add(writeService);
    }

    private void initSillyProcessProperty() {
        hookSillyProcessPropertyList();
    }

    protected void addSillyProcessProperty(String category, SillyProcessProperty sillyProcessProperty) {
        initSillyProcessProperty(category, sillyProcessProperty);
        SillyAssert.notEmpty(sillyProcessProperty.getCategory(), "category不可为空");
        sillyProcessPropertyList.add(sillyProcessProperty);
    }


    protected abstract void hookSillyProcessPropertyList();

    protected void initSillyProcessProperty(String category, SillyProcessProperty property) {
        if (StringUtils.isEmpty(property.getCategory())) {
            property.setCategory(category);
        }
        Map<String, SillyProcessMasterProperty> masterMap = property.getMaster();
        for (String key : masterMap.keySet()) {
            SillyProcessMasterProperty masterProperty = masterMap.get(key);
            masterProperty.setParent(property);
            if (StringUtils.isEmpty(masterProperty.getProcessKey())) {
                masterProperty.setProcessKey(key);
            }

            Map<String, SillyProcessNodeProperty> nodeMap = masterProperty.getNode();
            for (String nodeKey : nodeMap.keySet()) {
                SillyProcessNodeProperty nodeProperty = nodeMap.get(nodeKey);
                nodeProperty.setParent(masterProperty);
                if (StringUtils.isEmpty(nodeProperty.getNodeKey())) {
                    nodeProperty.setNodeKey(nodeKey);
                }

                Map<String, SillyProcessVariableProperty> variableMap = nodeProperty.getVariable();
                for (String variableName : variableMap.keySet()) {
                    SillyProcessVariableProperty variableProperty = variableMap.get(variableName);
                    variableProperty.setParent(nodeProperty);
                    if (StringUtils.isEmpty(variableProperty.getVariableName())) {
                        variableProperty.setVariableName(variableName);
                    }
                    if (StringUtils.isEmpty(variableProperty.getVariableType())) {
                        variableProperty.setVariableType(SillyConstant.ActivitiNode.CONVERTOR_STRING);
                    }
                    if (StringUtils.isEmpty(variableProperty.getBelong())) {
                        variableProperty.setBelong(SillyConstant.ActivitiVariable.BELONG_VARIABLE);
                    }

                    registerSillyTaskCategoryGroup(category, variableProperty);
                    setVariablePropertyHtmlConfig(category, variableProperty);
                }

            }
        }
    }

    /**
     * 注册 变量 任务业务分组工具
     *
     * @param category
     * @param variableProperty
     */
    protected void registerSillyTaskCategoryGroup(String category, SillyProcessVariableProperty variableProperty) {
        String userGroupVariableName = variableProperty.getUserGroupVariableName();
        if (StringUtils.isEmpty(userGroupVariableName)) {
            return;
        }

        String variableName = variableProperty.getVariableName();
        if (StringUtils.isEmpty(variableProperty.getDefaultText())) {
            variableProperty.setDefaultText("${#" + variableName + "}");
        }

        getSillyTaskCategoryGroup(category, variableName, variableProperty.getDesc(), userGroupVariableName);
    }

    /**
     * 配置HTML信息
     *
     * @param category
     * @param variableProperty
     */
    protected void setVariablePropertyHtmlConfig(String category, SillyProcessVariableProperty variableProperty) {

        SillyHtmlTagTemplate htmlTemplate = getHtmlTemplate(category, variableProperty);
        if (htmlTemplate == null && variableProperty.getHtmlType() == null) {
            return;
        }

        SillyAssert.notNull(htmlTemplate, "htmlConfig 未找到 " + variableProperty.getHtmlType());
        variableProperty.setHtmlTemplate(htmlTemplate);

        SillyHtmlTagConfig htmlConfig = variableProperty.getHtmlConfig();

        if (htmlConfig.getFieldName() == null) {
            htmlConfig.setFieldName(variableProperty.getVariableName());
        }
        if (htmlConfig.getDesc() == null) {
            htmlConfig.setDesc(variableProperty.getDesc());
        }
        if (htmlConfig.getLabel() == null) {
            htmlConfig.setLabel(variableProperty.getDesc());
        }
        if (htmlConfig.getTagName() == null) {
            htmlConfig.setTagName(htmlTemplate.getHtmlType());
        }
        if (htmlConfig.isRequest() == null) {
            htmlConfig.setRequest(variableProperty.isRequest());
        }
    }

    protected void initSillyTaskGroupHandle() {
        sillyTaskGroupHandle = loadSillyTaskGroupHandle();
    }

    protected abstract SillyTaskGroupHandle loadSillyTaskGroupHandle();

    protected void initSillyCacheList() {
        hookSillyCacheList();
    }

    protected abstract void hookSillyCacheList();

    protected void addSillyCache(SillyCache sillyCache) {
        sillyCacheList.add(sillyCache);
    }


    protected void initCurrentUserUtilList() {
        hookSillyCurrentUserUtilList();
    }

    protected abstract void hookSillyCurrentUserUtilList();

    protected void addSillyCurrentUserUtil(SillyCurrentUserUtil currentUserUtil) {
        sillyCurrentUserUtilList.add(currentUserUtil);
    }


    public SillyContext getSillyContext(String category) {
        return SillyCoreUtil.availableOne(category, sillyContextList);
    }

    public SillyCurrentUserUtil getCurrentUserUtil(String category) {
        return SillyCoreUtil.availableOne(category, sillyCurrentUserUtilList);
    }

    public SillyEngineService getSillyEngineService(String category) {
        return SillyCoreUtil.availableOne(category, sillyEngineServiceList);
    }

    public Map<String, SillyVariableConvertor> getSillyConvertorMap(String category) {
        Map<String, SillyVariableConvertor> map = new HashMap<>();
        SillyCoreUtil.availableThen(category, sillyConvertorList, (v) -> {
            map.put(v.name(), v);
        });
        return map;
    }

    public Map<String, SillyVariableSaveHandle> getSillyVariableSaveHandleMap(String category) {
        Map<String, SillyVariableSaveHandle> map = new HashMap<>();
        SillyCoreUtil.availableThen(category, sillyVariableSaveHandleList, (v) -> {
            map.put(v.name(), v);
        });
        return map;
    }

    public Map<String, SillyNodeSaveHandle> getSillyNodeSaveHandleMap(String category) {
        Map<String, SillyNodeSaveHandle> map = new HashMap<>();
        SillyCoreUtil.availableThen(category, sillyNodeSaveHandleList, (v) -> {
            map.put(v.name(), v);
        });
        return map;
    }

    public SillyResumeService getSillyResumeService(String category) {
        return SillyCoreUtil.availableOne(category, sillyResumeServiceList);
    }

    public SillyFactory getSillyFactory(String category) {
        return SillyCoreUtil.availableOne(category, sillyFactoryList);
    }

    public SillyReadService getSillyReadService(String category) {
        return SillyCoreUtil.availableOne(category, sillyReadServiceList);
    }

    public SillyWriteService getSillyWriteService(String category) {
        return SillyCoreUtil.availableOne(category, sillyWriteServiceList);
    }

    public SillyProcessProperty getSillyProcessProperty(String category) {
        return SillyCoreUtil.consistentOne(category, sillyProcessPropertyList);
    }

    public SillyTaskGroupHandle getSillyTaskGroupHandle(String category) {
        boolean available = SillyCoreUtil.available(category, sillyTaskGroupHandle);
        return available ? sillyTaskGroupHandle : null;
    }

    public abstract SillyTaskCategoryGroup getSillyTaskCategoryGroup(String category, String variableName, String groupName, String userVariableName);

    public SillyCache getSillyCache(String category) {
        return SillyCoreUtil.availableOne(category, sillyCacheList);
    }

    public SillyHtmlTagTemplate getHtmlTemplate(String category, SillyProcessVariableProperty variableProperty) {
        String htmlType = variableProperty.getHtmlType();
        List<SillyHtmlTagTemplate> beanList = sillyContext.getBeanList(category, SillyHtmlTagTemplate.class);

        for (SillyHtmlTagTemplate tagTemplate : beanList) {
            if (tagTemplate.getHtmlType().equals(htmlType)) {
                return tagTemplate;
            }

            if (tagTemplate instanceof SillyHtmlAutoTag) {
                SillyHtmlAutoTag autoTag = (SillyHtmlAutoTag) tagTemplate;
                if (autoTag.support(variableProperty)) {
                    return tagTemplate;
                }
            }
        }
        return null;
    }

    @Override
    public List<SillyCategoryConfig> convertSillyConfig() {
        List<SillyCategoryConfig> converList = new ArrayList<>();
        for (String category : allCategorySet()) {
            if (category.equals(SillyCategory.DEFAULT_CATEGORY)) {
                continue;
            }

            SillyCategoryConfig sillyCategoryConfig = convertSillyConfig(category);
            converList.add(sillyCategoryConfig);
        }
        return converList;
    }

    public SillyCategoryConfig convertSillyConfig(String category) {
        SillyCategoryConfig sillyCategoryConfig = SillyReflectUtil.newInstance(getSillyCategoryConfigClazz(category));
        sillyCategoryConfig
                .setCategory(category)
                .setSillyContext(getSillyContext(category))
                .setPropertyHandleClazz(getPropertyHandleClazz(category))
                .setSillyTaskGroupHandle(getSillyTaskGroupHandle(category))
                .setSillyConvertorMap(getSillyConvertorMap(category))
                .setSillyVariableSaveHandleMap(getSillyVariableSaveHandleMap(category))
                .setSillyNodeSaveHandleMap(getSillyNodeSaveHandleMap(category))
                .setSillyCurrentUserUtil(getCurrentUserUtil(category))
                .setSillyEngineService(getSillyEngineService(category))
                .setSillyFactory(getSillyFactory(category))
                .setSillyProcessProperty(getSillyProcessProperty(category))
                .setSillyResumeService(getSillyResumeService(category))
                .setSillyReadService(getSillyReadService(category))
                .setSillyWriteService(getSillyWriteService(category))
                .setSillyCache(getSillyCache(category));
        return sillyCategoryConfig;
    }

    protected abstract Class<? extends SillyPropertyHandle> getPropertyHandleClazz(String category);

}
