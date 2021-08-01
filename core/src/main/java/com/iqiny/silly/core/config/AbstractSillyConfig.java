/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.alibaba.fastjson.JSON;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.SillyInitializable;
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
import com.iqiny.silly.core.service.SillyEngineService;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.*;

/**
 * 配置类
 */

@SuppressWarnings("all")
public abstract class AbstractSillyConfig implements SillyConfig {

    /**
     * 支持的业务种类
     */
    protected final Set<String> supportCategories = new LinkedHashSet<>();

    /**
     * 当前人获取工具
     */
    protected CurrentUserUtil currentUserUtil;

    /**
     * 流程引擎服务，流程控制服务
     */
    protected SillyEngineService sillyEngineService;

    /**
     * 流程履历记录服务
     */
    protected SillyResumeService sillyResumeService;

    /**
     * 流程变量 类型转换器
     */
    protected final Map<String, SillyVariableConvertor> sillyConvertorMap = new HashMap<>();

    /**
     * 傻瓜工厂工厂
     */
    protected final Map<String, SillyFactory> sillyFactoryMap = new HashMap<>();

    /**
     * 傻瓜读取服务类
     */
    protected final Map<String, SillyReadService> sillyReadServiceMap = new HashMap<>();

    /**
     * 傻瓜写入服务类
     */
    protected final Map<String, SillyWriteService> sillyWriteServiceMap = new HashMap<>();

    /**
     * 傻瓜流程参数属性配置
     */
    protected final Map<String, SillyProcessProperty> sillyProcessPropertyMap = new HashMap<>();

    /**
     * 傻瓜任务组处理类
     */
    protected SillyTaskGroupHandle sillyTaskGroupHandle;

    /**
     * 傻瓜HTML 标签模板配置
     */
    protected final Map<String, SillyHtmlTagTemplate> sillyHtmlTagTemplateMap = new HashMap<>();

    @Override
    public void init() {
        preInit();
        // 1 初始化 基本属性
        initFiled();
        // 2 初始化 傻瓜工厂
        initSillyFactory();
        // 3 初始化傻瓜转换器
        initSillyConvertorMap();
        // 4 初始化傻瓜标签模板
        initSillyHtmlTagTemplateMap();
        // 4 初始化傻瓜服务
        initSillyService();
        // 5 初始化傻瓜流程参数
        initSillyProcessProperty();
        // 6 初始化傻瓜任务组处理类
        initSillyTaskGroupHandle();

        checkConfig();

        // 设置配置静态工具
        SillyConfigUtil.addSillyConfig(this);

        initComplete();
    }


    @Override
    public Set<String> supportCategories() {
        return supportCategories;
    }

    /**
     * 此配置器是否支持此种类, 若支持默认类型，则全部支持
     *
     * @param category
     * @return
     */
    @Override
    public boolean isSupport(String category) {
        SillyAssert.notNull(category);

        return supportCategories.contains(SillyInitializable.DEFAULT_CATEGORY) || supportCategories.contains(category);
    }

    /**
     * 初始化配置属性
     */
    protected abstract void initFiled();

    /**
     * 初始化之前 的回调方法
     */
    protected abstract void preInit();

    /**
     * 初始化完成后 的回调方法
     */
    protected abstract void initComplete();

    protected void checkConfig() {
        SillyAssert.notEmpty(this.supportCategories);
        SillyAssert.notNull(this.currentUserUtil);
        SillyAssert.notNull(this.sillyEngineService);
        SillyAssert.notNull(this.sillyConvertorMap);
    }

    private void initSillyFactory() {
        initBaseSillyFactoryMap();
        hookInitSillyFactoryMap();
    }


    protected void addSillyFactory(SillyFactory sillyFactory) {
        boolean support = isSupport(sillyFactory.category());
        // 仅适配支持的类型
        if (support) {
            sillyFactoryMap.put(sillyFactory.category(), sillyFactory);
        }
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

    protected void addSillyVariableConvertor(SillyVariableConvertor convertor) {
        sillyConvertorMap.put(convertor.name(), convertor);
    }

    /**
     * 初始完成 傻瓜转换器 回调方法
     */
    protected abstract void hookInitSillyConvertorMap();


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
        initReadSillyService();
        initWriteSillyService();
    }

    protected abstract void initReadSillyService();

    protected abstract void initWriteSillyService();

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
        SillyHtmlTagTemplate htmlTemplate = SillyConfigUtil.getSillyConfig().getHtmlTemplate(variableProperty.getHtmlType());
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

    @Override
    public CurrentUserUtil getCurrentUserUtil() {
        return currentUserUtil;
    }

    @Override
    public SillyEngineService getSillyEngineService() {
        return sillyEngineService;
    }

    @Override
    public Map<String, SillyVariableConvertor> getSillyConvertorMap() {
        return sillyConvertorMap;
    }

    @Override
    public SillyResumeService getSillyResumeService() {
        return sillyResumeService;
    }


    @Override
    public SillyFactory getSillyFactory(String category) {
        return sillyFactoryMap.get(category);
    }

    @Override
    public SillyReadService getSillyReadService(String category) {
        return sillyReadServiceMap.get(category);
    }

    @Override
    public SillyWriteService getSillyWriteService(String category) {
        return sillyWriteServiceMap.get(category);
    }

    @Override
    public SillyProcessProperty getSillyProcessProperty(String category) {
        return sillyProcessPropertyMap.get(category);
    }

    @Override
    public SillyTaskGroupHandle getSillyTaskGroupHandle() {
        return sillyTaskGroupHandle;
    }

    @Override
    public SillyHtmlTagTemplate getHtmlTemplate(String htmlType) {
        return sillyHtmlTagTemplateMap.get(htmlType);
    }
}
