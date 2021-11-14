/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.SillyMultipleCategory;
import com.iqiny.silly.core.cache.SillyCache;
import com.iqiny.silly.core.config.html.SillyHtmlTagConfig;
import com.iqiny.silly.core.config.html.SillyHtmlTagTemplate;
import com.iqiny.silly.core.config.property.SillyProcessMasterProperty;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyProcessVariableProperty;
import com.iqiny.silly.core.convertor.SillyStringConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.service.SillyEngineService;
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

    private boolean initialized = false;
    private boolean refresh = false;

    protected Class<? extends SillyCategoryConfig> sillyCategoryConfigClazz = DefaultSillyCategoryConfig.class;

    /**
     * 业务配置信息集合
     */
    protected List<SillyCategoryConfig> sillyCategoryConfigList = new ArrayList<>();

    /**
     * 当前人获取工具
     */
    protected final Map<String, SillyCurrentUserUtil> sillyCurrentUserUtilMap = new SillyConfigHashMap<>();

    /**
     * 流程引擎服务，流程控制服务
     */
    protected final Map<String, SillyEngineService> sillyEngineServiceMap = new SillyConfigHashMap<>();

    /**
     * 流程履历记录服务
     */
    protected final Map<String, SillyResumeService> sillyResumeServiceMap = new SillyConfigHashMap<>();

    /**
     * 流程变量 类型转换器
     */
    protected final Map<String, SillyVariableConvertor> sillyConvertorMap = new HashMap<>();

    /**
     * 流程变量 类型转换器
     */
    protected final Map<String, SillyVariableSaveHandle> sillyVariableSaveHandleMap = new HashMap<>();

    /**
     * 傻瓜工厂工厂
     */
    protected final Map<String, SillyFactory> sillyFactoryMap = new SillyConfigHashMap<>();

    /**
     * 傻瓜读取服务类
     */
    protected final Map<String, SillyReadService> sillyReadServiceMap = new SillyConfigHashMap<>();

    /**
     * 傻瓜写入服务类
     */
    protected final Map<String, SillyWriteService> sillyWriteServiceMap = new SillyConfigHashMap<>();

    /**
     * 傻瓜流程参数属性配置
     */
    protected final Map<String, SillyProcessProperty> sillyProcessPropertyMap = new SillyConfigHashMap<>();

    /**
     * 傻瓜缓存属性配置
     */
    protected final Map<String, SillyCache> sillyCacheMap = new SillyConfigHashMap<>();

    /**
     * 傻瓜任务组处理类
     */
    protected SillyTaskGroupHandle sillyTaskGroupHandle;

    /**
     * 傻瓜HTML 标签模板配置
     */
    protected final Map<String, SillyHtmlTagTemplate> sillyHtmlTagTemplateMap = new HashMap<>();

    @Override
    public Class<? extends SillyCategoryConfig> getSillyCategoryConfigClazz() {
        return sillyCategoryConfigClazz;
    }

    public void setSillyCategoryConfigClazz(Class<? extends SillyCategoryConfig> sillyCategoryConfigClazz) {
        this.sillyCategoryConfigClazz = sillyCategoryConfigClazz;
    }

    @Override
    public synchronized void init() {
        if (this.initialized) {
            return;
        }

        preInit();
        // 1 初始化 基本属性
        initFiled();
        initCurrentUserUtilMap();
        // 2 初始化 傻瓜工厂
        initSillyFactory();
        // 3 初始化 傻瓜转换器
        initSillyConvertorMap();
        // 4 初始化 傻瓜数据保存器
        initSillyVariableSaveHandleMap();
        // 5 初始化 傻瓜页面代码生成模板
        initSillyHtmlTagTemplateMap();
        // 6 初始化 傻瓜服务
        initSillyService();
        // 7 初始化 傻瓜流程参数
        initSillyProcessProperty();
        // 8 初始化 傻瓜任务组处理类
        initSillyTaskGroupHandle();
        // 9 初始化 傻瓜缓存配置
        initSillyCacheMap();

        checkConfig();

        // 将配置资源转换为业务配置
        sillyCategoryConfigList = convertSillyConfig();
        for (SillyCategoryConfig sillyCategoryConfig : sillyCategoryConfigList) {
            if (this.refresh) {
                sillyCategoryConfig.refresh();
            } else {
                sillyCategoryConfig.init();
            }
        }

        this.initialized = true;
        this.refresh = false;

        initComplete();
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

    private void initSillyFactory() {
        initBaseSillyFactoryMap();
        hookInitSillyFactoryMap();
    }


    protected void addSillyFactory(SillyFactory sillyFactory) {
        sillyFactoryMap.put(sillyFactory.category(), sillyFactory);
    }

    /**
     * 初始化基本傻瓜工厂，可能会被相同种类的工厂覆盖
     */
    protected abstract void initBaseSillyFactoryMap();

    /**
     * 初始 傻瓜工厂 回调方法
     */
    protected abstract void hookInitSillyFactoryMap();

    protected void initSillyConvertorMap() {
        addSillyVariableConvertor(new SillyStringConvertor());
        hookInitSillyConvertorMap();
    }

    protected void initSillyVariableSaveHandleMap() {
        hookInitSillyVariableSaveHandleMap();
    }

    protected void addSillyVariableConvertor(SillyVariableConvertor convertor) {
        sillyConvertorMap.put(convertor.name(), convertor);
    }

    protected void addSillyVariableSaveHandle(SillyVariableSaveHandle saveHandle) {
        sillyVariableSaveHandleMap.put(saveHandle.name(), saveHandle);
    }

    /**
     * 初始完成 傻瓜转换器 回调方法
     */
    protected abstract void hookInitSillyConvertorMap();


    protected abstract void hookInitSillyVariableSaveHandleMap();


    protected void initSillyHtmlTagTemplateMap() {
        hookInitSillyHtmlTagTemplateMap();
    }

    protected void addSillyHtmlTagTemplateMap(SillyHtmlTagTemplate htmlTagTemplate) {
        sillyHtmlTagTemplateMap.put(htmlTagTemplate.getHtmlType(), htmlTagTemplate);
    }


    /**
     * 初始完成 傻瓜转换器 回调方法
     */
    protected abstract void hookInitSillyHtmlTagTemplateMap();

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
        Set<String> categories = engineService.supportCategories();
        for (String category : categories) {
            sillyEngineServiceMap.put(category, engineService);
        }
    }

    protected void addSillyResumeService(SillyResumeService resumeService) {
        Set<String> categories = resumeService.supportCategories();
        for (String category : categories) {
            sillyResumeServiceMap.put(category, resumeService);
        }
    }

    protected void addSillyReadService(SillyReadService readService) {
        sillyReadServiceMap.put(readService.usedCategory(), readService);
    }

    protected void addSillyWriteService(SillyWriteService writeService) {
        sillyWriteServiceMap.put(writeService.usedCategory(), writeService);
    }

    private void initSillyProcessProperty() {
        hookSillyProcessPropertyMap();
    }

    protected void addSillyProcessProperty(String category, SillyProcessProperty sillyProcessProperty) {
        initSillyProcessProperty(category, sillyProcessProperty);
        if (StringUtils.isEmpty(category)) {
            category = sillyProcessProperty.getCategory();
        }
        SillyAssert.notEmpty(category, "category不可为空");
        sillyProcessPropertyMap.put(category, sillyProcessProperty);
    }


    protected abstract void hookSillyProcessPropertyMap();

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
                        setVariablePropertyHtmlConfig(variableProperty);
                    }
                }

            }
        }
    }

    protected void setVariablePropertyHtmlConfig(SillyProcessVariableProperty variableProperty) {
        SillyHtmlTagTemplate htmlTemplate = getHtmlTemplate(variableProperty.getHtmlType());
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

    protected void initSillyCacheMap() {
        hookSillyCacheMap();
    }

    protected abstract void hookSillyCacheMap();

    protected void addSillyCache(SillyCache sillyCache) {
        Set<String> categories = sillyCache.supportCategories();
        for (String category : categories) {
            sillyCacheMap.put(category, sillyCache);
        }
    }


    protected void initCurrentUserUtilMap() {
        hookSillyCurrentUserUtilMap();
    }

    protected abstract void hookSillyCurrentUserUtilMap();

    protected void addSillyCurrentUserUtil(SillyCurrentUserUtil currentUserUtil) {
        Set<String> categories = currentUserUtil.supportCategories();
        for (String category : categories) {
            sillyCurrentUserUtilMap.put(category, currentUserUtil);
        }
    }

    public SillyCurrentUserUtil getCurrentUserUtil(String category) {
        return sillyCurrentUserUtilMap.get(category);
    }

    public SillyEngineService getSillyEngineService(String category) {
        return sillyEngineServiceMap.get(category);
    }

    public Map<String, SillyVariableConvertor> getSillyConvertorMap() {
        return sillyConvertorMap;
    }

    public Map<String, SillyVariableSaveHandle> getSillyVariableSaveHandleMap() {
        return sillyVariableSaveHandleMap;
    }

    public SillyResumeService getSillyResumeService(String category) {
        return sillyResumeServiceMap.get(category);
    }


    public SillyFactory getSillyFactory(String category) {
        return sillyFactoryMap.get(category);
    }

    public SillyReadService getSillyReadService(String category) {
        return sillyReadServiceMap.get(category);
    }

    public SillyWriteService getSillyWriteService(String category) {
        return sillyWriteServiceMap.get(category);
    }

    public SillyProcessProperty getSillyProcessProperty(String category) {
        return sillyProcessPropertyMap.get(category);
    }

    public SillyTaskGroupHandle getSillyTaskGroupHandle() {
        return sillyTaskGroupHandle;
    }

    public SillyCache getSillyCache(String category) {
        return sillyCacheMap.get(category);
    }

    public SillyHtmlTagTemplate getHtmlTemplate(String htmlType) {
        return sillyHtmlTagTemplateMap.get(htmlType);
    }

    @Override
    public List<SillyCategoryConfig> convertSillyConfig() {
        List<SillyCategoryConfig> converList = new ArrayList<>();
        Set<String> categorySet = SillyConfigHashMap.categorySet;
        for (String category : categorySet) {
            if (category.equals(SillyCategory.DEFAULT_CATEGORY)) {
                continue;
            }

            SillyCategoryConfig sillyCategoryConfig = convertSillyConfig(category);
            converList.add(sillyCategoryConfig);
        }
        return converList;
    }

    public SillyCategoryConfig convertSillyConfig(String category) {
        SillyCategoryConfig sillyCategoryConfig = SillyReflectUtil.newInstance(getSillyCategoryConfigClazz());
        sillyCategoryConfig
                .setCategory(category)
                .setPropertyHandleClazz(getPropertyHandleClazz())
                .setSillyTaskGroupHandle(getSillyTaskGroupHandle())
                .setSillyConvertorMap(getSillyConvertorMap())
                .setSillyVariableSaveHandleMap(getSillyVariableSaveHandleMap())
                .setSillyCurrentUserUtil(getCurrentUserUtil(category))
                .setSillyEngineService(getSillyEngineService(category))
                .setSillyFactory(getSillyFactory(category))
                .setSillyProcessProperty(getSillyProcessProperty(category))
                .setSillyResumeService(getSillyResumeService(category))
                .setSillyReadService(getSillyReadService(category))
                .setSillyWriteService(getSillyWriteService(category))
                .setSillyCache(getSillyCache(category))
                .setHtmlTemplateMap(sillyHtmlTagTemplateMap);
        return sillyCategoryConfig;
    }

    static class SillyConfigHashMap<K, V> extends HashMap<K, V> {

        /**
         * 全部已识别的业务分类
         */
        private static final Set<String> categorySet = new LinkedHashSet<>();

        public static Set<String> allCategorySet() {
            return categorySet;
        }

        @Override
        public V get(Object key) {
            V v = super.get(key);
            if (v == null) {
                // 获取通用类型
                v = super.get(SillyCategory.DEFAULT_CATEGORY);
                if (v instanceof SillyMultipleCategory) {
                    // 判断是否支持此类型
                    boolean support = ((SillyMultipleCategory) v).isSupport((String) key);
                    if (support) {
                        put((K) key, v);
                    } else {
                        log.info(v.getClass().getName() + " 不支持此类型" + key);
                    }
                }
            }
            return v;
        }

        @Override
        public V put(K key, V value) {
            V put = super.put(key, value);
            if (key instanceof String) {
                categorySet.add((String) key);
            }
            if (put != null) {
                log.warn("存在重复KEY被覆盖！" + key);
            }
            return put;
        }
    }
}
