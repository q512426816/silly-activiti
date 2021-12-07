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
import com.iqiny.silly.core.config.html.SillyHtmlTagConfig;
import com.iqiny.silly.core.config.html.SillyHtmlTagTemplate;
import com.iqiny.silly.core.config.property.*;
import com.iqiny.silly.core.convertor.SillyStringConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.engine.SillyEngineService;
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
     * Name 流程变量 类型转换器
     */
    protected final List<SillyVariableSaveHandle> sillyVariableSaveHandleList = new ArrayList<>();

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
        initSillyVariableSaveHandleList();
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
        hookInitSillyConvertorList();
    }

    protected void initSillyVariableSaveHandleList() {
        hookInitSillyVariableSaveHandleList();
    }

    protected void addSillyVariableConvertor(SillyVariableConvertor convertor) {
        sillyConvertorList.add(convertor);
    }

    protected void addSillyVariableSaveHandle(SillyVariableSaveHandle saveHandle) {
        sillyVariableSaveHandleList.add(saveHandle);
    }

    /**
     * 初始完成 傻瓜转换器 回调方法
     */
    protected abstract void hookInitSillyConvertorList();


    protected abstract void hookInitSillyVariableSaveHandleList();


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
            if (StringUtils.isEmpty(masterProperty.getProcessKey())) {
                masterProperty.setProcessKey(key);
            }

            Map<String, SillyProcessNodeProperty> nodeMap = masterProperty.getNode();
            for (String nodeKey : nodeMap.keySet()) {
                SillyProcessNodeProperty nodeProperty = nodeMap.get(nodeKey);
                if (StringUtils.isEmpty(nodeProperty.getNodeKey())) {
                    nodeProperty.setNodeKey(nodeKey);
                }

                Map<String, SillyProcessVariableProperty> variableMap = nodeProperty.getVariable();
                for (String variableName : variableMap.keySet()) {
                    SillyProcessVariableProperty variableProperty = variableMap.get(variableName);
                    if (StringUtils.isEmpty(variableProperty.getVariableName())) {
                        variableProperty.setVariableName(variableName);
                    }
                    if (StringUtils.isEmpty(variableProperty.getVariableType())) {
                        variableProperty.setVariableType(SillyConstant.ActivitiNode.CONVERTOR_STRING);
                    }
                    if (StringUtils.isEmpty(variableProperty.getBelong())) {
                        variableProperty.setBelong(SillyConstant.ActivitiVariable.BELONG_VARIABLE);
                    }

                    if (StringUtils.isNotEmpty(variableProperty.getHtmlType())) {
                        setVariablePropertyHtmlConfig(category, variableProperty);
                    }
                }

            }
        }
    }

    protected void setVariablePropertyHtmlConfig(String category, SillyProcessVariableProperty variableProperty) {
        SillyHtmlTagTemplate htmlTemplate = getHtmlTemplate(category, variableProperty.getHtmlType());
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
            htmlConfig.setLabel(htmlTemplate.getHtmlType());
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

    public Map<String, SillyHtmlTagTemplate> getSillyHtmlTagTemplateMap(String category) {
        Map<String, SillyHtmlTagTemplate> map = sillyCategoryHtmlTagTemplateMap.get(category);
        if (map != null) {
            return map;
        }

        sillyCategoryHtmlTagTemplateMap.put(category, new HashMap<>());
        SillyCoreUtil.availableThen(category, sillyHtmlTagTemplateList, (v) -> {
            sillyCategoryHtmlTagTemplateMap.get(category).put(v.getHtmlType(), v);
        });
        return sillyCategoryHtmlTagTemplateMap.get(category);
    }

    public SillyCache getSillyCache(String category) {
        return SillyCoreUtil.availableOne(category, sillyCacheList);
    }

    public SillyHtmlTagTemplate getHtmlTemplate(String category, String htmlType) {
        Map<String, SillyHtmlTagTemplate> sillyHtmlTagTemplateMap = getSillyHtmlTagTemplateMap(category);
        return sillyHtmlTagTemplateMap.get(htmlType);
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
                .setSillyCurrentUserUtil(getCurrentUserUtil(category))
                .setSillyEngineService(getSillyEngineService(category))
                .setSillyFactory(getSillyFactory(category))
                .setSillyProcessProperty(getSillyProcessProperty(category))
                .setSillyResumeService(getSillyResumeService(category))
                .setSillyReadService(getSillyReadService(category))
                .setSillyWriteService(getSillyWriteService(category))
                .setSillyCache(getSillyCache(category))
                .setHtmlTemplateMap(getSillyHtmlTagTemplateMap(category));
        return sillyCategoryConfig;
    }

    protected abstract Class<? extends SillyPropertyHandle> getPropertyHandleClazz(String category);

}
