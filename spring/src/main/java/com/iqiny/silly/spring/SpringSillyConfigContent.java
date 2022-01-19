/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.core.base.SillyInitializable;
import com.iqiny.silly.core.base.SillyProperties;
import com.iqiny.silly.core.cache.SillyCache;
import com.iqiny.silly.core.common.SillyCoreUtil;
import com.iqiny.silly.core.config.BaseSillyConfigContent;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.config.html.SillyHtmlTagTemplate;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.config.property.impl.DefaultVariableSaveHandle;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.group.*;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.savehandle.variable.DataJoinVariableSaveHandle;
import com.iqiny.silly.core.savehandle.variable.OverwriteVariableSaveHandle;
import com.iqiny.silly.core.savehandle.variable.SaveVariableSaveHandle;
import com.iqiny.silly.core.savehandle.variable.SkipVariableSaveHandle;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SuppressWarnings("all")
@Order
public class SpringSillyConfigContent extends BaseSillyConfigContent implements InitializingBean {

    private final static Log log = LogFactory.getLog(SpringSillyConfigContent.class);

    protected String processPattern;

    Class<? extends SillyEngineService> defaultEngineServiceClazz;

    Class<? extends SillyWriteService> defaultWriteServiceClazz;

    Class<? extends SillyReadService> defaultReadServiceClazz;

    protected Class<? extends SillyProcessProperty> processPropertyClazz;

    protected Class<? extends SillyPropertyHandle> propertyHandleClazz;

    protected Class<? extends SillyTaskCategoryGroup> defaultTaskCategoryGroupClazz;

    private String[] categories;

    private boolean loadCategoryFlag = false;

    public SpringSillyConfigContent(SillyProperties sillyProperties, SillyContext sillyContext) {
        super(sillyProperties, sillyContext);
    }

    @Override
    protected void preInit() {
        log.info("SpringSillyConfigContent  " + this.getClass().getName() + " 初始化开始");
        SillyAssert.notNull(sillyProperties, "sillyProperties 配置属性不可为空");

        this.processPattern = sillyProperties.getProcessPattern();
        this.defaultEngineServiceClazz = sillyProperties.getDefaultEngineServiceClazz();
        this.defaultWriteServiceClazz = sillyProperties.getDefaultWriteServiceClazz();
        this.defaultReadServiceClazz = sillyProperties.getDefaultReadServiceClazz();
        this.processPropertyClazz = sillyProperties.getProcessPropertyClazz();
        this.propertyHandleClazz = sillyProperties.getPropertyHandleClazz();
        this.defaultTaskCategoryGroupClazz = sillyProperties.getDefaultTaskCategoryGroupClazz();
        this.categories = sillyProperties.getCategories();
    }

    @Override
    protected void initFiled() {
        this.loadCategoryFlag = false;
        super.initFiled();
    }

    @Override
    protected synchronized void loadCategorySet() {
        if (loadCategoryFlag) {
            return;
        }

        loadCategoryFlag = true;
        if (categories != null) {
            for (String category : categories) {
                addCategorySet(category);
            }
        }
    }

    @Override
    protected void hookSillyContextList() {
        final List<SillyContext> beanList = sillyContext.getBeanList(SillyContext.class);
        for (SillyContext sillyContext : beanList) {
            addSillyContext(sillyContext);
        }
    }

    @Override
    protected void initBaseSillyFactoryList() {

    }

    @Override
    protected void hookInitSillyConvertorList() {
        final List<SillyVariableConvertor> beanList = sillyContext.getBeanList(SillyVariableConvertor.class);
        for (SillyVariableConvertor convertor : beanList) {
            addSillyVariableConvertor(convertor);
        }
    }

    @Override
    protected void hookInitSillyVariableSaveHandleList() {
        addSillyVariableSaveHandle(new DefaultVariableSaveHandle());
        addSillyVariableSaveHandle(new OverwriteVariableSaveHandle());
        addSillyVariableSaveHandle(new DataJoinVariableSaveHandle());
        addSillyVariableSaveHandle(new SaveVariableSaveHandle());
        addSillyVariableSaveHandle(new SkipVariableSaveHandle());
        final List<SillyVariableSaveHandle> beanList = sillyContext.getBeanList(SillyVariableSaveHandle.class);
        for (SillyVariableSaveHandle saveHandle : beanList) {
            addSillyVariableSaveHandle(saveHandle);
        }
    }

    @Override
    protected void hookInitSillyHtmlTagTemplateList() {
        final List<SillyHtmlTagTemplate> beanList = sillyContext.getBeanList(SillyHtmlTagTemplate.class);
        for (SillyHtmlTagTemplate htmlTagTemplate : beanList) {
            addSillyHtmlTagTemplate(htmlTagTemplate);
        }
    }

    @Override
    protected void initEngineSillyService() {
        final List<SillyEngineService> engineServices = sillyContext.getBeanList(SillyEngineService.class);
        for (SillyEngineService engineService : engineServices) {
            addSillyEngineService(engineService);
        }
    }

    @Override
    protected void initResumeSillyService() {
        final List<SillyResumeService> resumeServices = sillyContext.getBeanList(SillyResumeService.class);
        for (SillyResumeService resumeService : resumeServices) {
            addSillyResumeService(resumeService);
        }
    }

    @Override
    protected void initReadSillyService() {
        final List<SillyReadService> readServices = sillyContext.getBeanList(SillyReadService.class);
        for (SillyReadService readService : readServices) {
            addSillyReadService(readService);
        }
    }

    @Override
    protected void initWriteSillyService() {
        final List<SillyWriteService> writeServices = sillyContext.getBeanList(SillyWriteService.class);
        for (SillyWriteService writeService : writeServices) {
            addSillyWriteService(writeService);
        }
    }

    @Override
    protected void hookSillyProcessPropertyList() {
        refreshProcessResource();
    }

    @Override
    protected SillyTaskGroupHandle loadSillyTaskGroupHandle() {
        SillyTaskGroupHandle handle = sillyContext.getBeanOrRegister(SillyTaskGroupHandle.class, DefaultSillyTaskGroupHandle.class);
        loadSillyTaskGroup(handle);
        loadSillyTaskCategoryGroup(handle);
        SillyAssert.notNull(handle, "任务组处理器未能正确加载");
        return handle;
    }

    @Override
    protected void hookSillyCacheList() {
        final List<SillyCache> sillyCaches = sillyContext.getBeanList(SillyCache.class);
        for (SillyCache sillyCache : sillyCaches) {
            addSillyCache(sillyCache);
        }
    }

    @Override
    protected void hookSillyCurrentUserUtilList() {
        final List<SillyCurrentUserUtil> sillyCurrentUserUtils = sillyContext.getBeanList(SillyCurrentUserUtil.class);
        for (SillyCurrentUserUtil currentUserUtil : sillyCurrentUserUtils) {
            addSillyCurrentUserUtil(currentUserUtil);
        }
    }

    protected void loadSillyTaskCategoryGroup(SillyTaskGroupHandle handle) {
        List<SillyTaskCategoryGroup> beanList = sillyContext.getBeanList(SillyTaskCategoryGroup.class);
        for (SillyTaskCategoryGroup sillyTaskCategoryGroup : beanList) {
            handle.addCategorySillyTaskGroup(sillyTaskCategoryGroup);
        }
    }

    protected void loadSillyTaskGroup(SillyTaskGroupHandle handle) {
        List<SillyTaskGroup> beanList = sillyContext.getBeanList(SillyTaskGroup.class);
        for (SillyTaskGroup sillyTaskGroup : beanList) {
            handle.addSillyTaskGroup(sillyTaskGroup);
        }
    }

    /**
     * 加载/刷新傻瓜流程配置资源
     */
    public void refreshProcessResource() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(processPattern);
            for (Resource resource : resources) {
                loadSillyProcessProperty(resource);
            }
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    protected void loadSillyProcessProperty(Resource resource) {
        if (resource == null || !resource.isReadable()) {
            log.warn(resource.getFilename() + "不可读取，将跳过加载");
            return;
        }
        try {
            // 生成jar后必须以流的形式读取
            InputStream inputStream = resource.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8.name());
            String json = writer.toString();
            String fileName = resource.getFilename();
            String category = fileName.substring(0, fileName.lastIndexOf("."));
            // 按定义顺序生成
            SillyProcessProperty processProperty = JSON.parseObject(json, getProcessPropertyClazz(category), Feature.OrderedField);
            addSillyProcessProperty(category, processProperty);
            log.info("成功更新流程配置文件:" + category);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    protected void initComplete() {
        log.info(this.getClass().getName() + " 初始化完成");
        log.info("转换器：" + sillyConvertorList.size() + "个，对象工厂：" + sillyFactoryList.size() + "个，" +
                "读取服务：" + sillyReadServiceList.size() + "个，写入服务：" + sillyWriteServiceList.size() + "个，" +
                "流程属性：" + sillyProcessPropertyList.size() + "个");
        log.info("成功注册" + sillyTaskGroupHandle.allSillyTaskGroup().size() + " 个任务组对象");
        log.info("成功注册" + sillyTaskGroupHandle.allCategoryGroup().size() + " 个类型任务组对象");
        log.info("已成功加载分类数量：" + allCategorySet().size() + " 种");
        log.info("已成功加载分类：" + allCategorySet() + "");
    }

    public Class<? extends SillyProcessProperty> getProcessPropertyClazz(String category) {
        return processPropertyClazz;
    }

    @Override
    public Class<? extends SillyPropertyHandle> getPropertyHandleClazz(String category) {
        return propertyHandleClazz;
    }

    @Override
    public SillyReadService getSillyReadService(String category) {
        SillyReadService readService = SillyCoreUtil.consistentOne(category, sillyReadServiceList);
        if (readService == null) {
            readService = getSillyContext(category).registerBean(
                    SpringSillyContext.getSillyReadServiceBeanName(category),
                    defaultReadServiceClazz,
                    bdb -> {
                        SillyAssert.isTrue(bdb instanceof BeanDefinitionBuilder, "bdb类型错误" + bdb.getClass());
                        ((BeanDefinitionBuilder) bdb).addPropertyValue("category", category);
                    });
            addSillyReadService(readService);
        }
        return readService;
    }

    @Override
    public SillyWriteService getSillyWriteService(String category) {
        SillyWriteService writeService = SillyCoreUtil.consistentOne(category, sillyWriteServiceList);
        if (writeService == null) {
            writeService = getSillyContext(category).registerBean(
                    SpringSillyContext.getSillyWriteServiceBeanName(category),
                    defaultWriteServiceClazz,
                    bdb -> {
                        SillyAssert.isTrue(bdb instanceof BeanDefinitionBuilder, "bdb类型错误" + bdb.getClass());
                        ((BeanDefinitionBuilder) bdb).addPropertyValue("category", category);
                    });
            addSillyWriteService(writeService);
        }
        return writeService;
    }

    @Override
    public SillyEngineService getSillyEngineService(String category) {
        SillyEngineService service = SillyCoreUtil.consistentOne(category, sillyEngineServiceList);
        if (service == null) {
            service = getSillyContext(category).registerBean(
                    SpringSillyContext.getSillyEngineServiceBeanName(category),
                    defaultEngineServiceClazz,
                    bdb -> {
                        SillyAssert.isTrue(bdb instanceof BeanDefinitionBuilder, "bdb类型错误" + bdb.getClass());
                        ((BeanDefinitionBuilder) bdb).addPropertyValue("category", category);
                    });
            addSillyEngineService(service);
        }
        return service;
    }

    @Override
    public SillyTaskCategoryGroup getSillyTaskCategoryGroup(String category
            , String variableName, String groupName, String userVariableName) {
        return getSillyContext(category).getBeanOrRegister(
                SpringSillyContext.getSillyTaskCategoryGroupBeanName(category, variableName, groupName),
                defaultTaskCategoryGroupClazz,
                bdb -> {
                    SillyAssert.isTrue(bdb instanceof BeanDefinitionBuilder, "bdb类型错误" + bdb.getClass());
                    ((BeanDefinitionBuilder) bdb).addPropertyValue("category", category);
                    ((BeanDefinitionBuilder) bdb).addPropertyValue("variableName", variableName);
                    ((BeanDefinitionBuilder) bdb).addPropertyValue("groupName", groupName);
                    ((BeanDefinitionBuilder) bdb).addPropertyValue("userVariableName", userVariableName);
                });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化 配置
        initSillyConfig();
        // 初始化其余
        initSillyInitializable();
    }

    protected void initSillyConfig() {
        // 开始初始化配置
        init();
        log.info("SillyConfig:" + getClass().getName() + " 初始化完成");
    }

    protected void initSillyInitializable() {
        // 开始初始化配置
        final List<SillyInitializable> beanList = sillyContext.getBeanList(SillyInitializable.class);
        for (SillyInitializable sillyInitializable : beanList) {
            // 不对sillyConfig重新初始化！
            if (!(sillyInitializable instanceof SillyCategoryConfig)) {
                sillyInitializable.init();
                if (log.isDebugEnabled()) {
                    log.debug("SillyInitializable:" + sillyInitializable.getClass().getName() + " 初始化完成");
                }
            }
        }
    }
}
