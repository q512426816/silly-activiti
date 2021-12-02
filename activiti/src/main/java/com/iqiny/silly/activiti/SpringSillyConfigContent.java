/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.iqiny.silly.activiti.convertor.SillyListConvertor;
import com.iqiny.silly.core.savehandle.DataJoinVariableSaveHandle;
import com.iqiny.silly.core.savehandle.OverwriteVariableSaveHandle;
import com.iqiny.silly.core.savehandle.SaveVariableSaveHandle;
import com.iqiny.silly.core.savehandle.SkipVariableSaveHandle;
import com.iqiny.silly.activiti.spring.SillySpelPropertyHandle;
import com.iqiny.silly.activiti.spring.SpringSillyContent;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.cache.SillyCache;
import com.iqiny.silly.core.config.BaseSillyConfigContent;
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.config.html.SillyHtmlTagTemplate;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.config.property.impl.DefaultProcessProperty;
import com.iqiny.silly.core.config.property.impl.DefaultVariableSaveHandle;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.group.SillyTaskCategoryGroup;
import com.iqiny.silly.core.group.SillyTaskGroup;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@SuppressWarnings("all")
public class SpringSillyConfigContent extends BaseSillyConfigContent {

    private final static Log log = LogFactory.getLog(SpringSillyConfigContent.class);

    protected String processPattern = "classpath*:/silly/*.json";

    protected Class<? extends SillyProcessProperty> processPropertyClazz = DefaultProcessProperty.class;

    protected Class<? extends SillyPropertyHandle> propertyHandleClazz = SillySpelPropertyHandle.class;

    protected String[] categorys;

    public SpringSillyConfigContent() {
    }

    @Override
    protected void loadCategorySet() {
        if (categorys != null) {
            for (String category : categorys) {
                addCategorySet(category);
            }
        }
    }

    @Override
    protected void preInit() {
        log.info("SpringSillyConfigContent  " + this.getClass().getName() + " 初始化开始");
    }

    @Override
    protected void initBaseSillyFactoryList() {

    }

    @Override
    protected void hookInitSillyConvertorList() {
        addSillyVariableConvertor(new SillyListConvertor());
        final Set<SillyVariableConvertor> beanSet = SpringSillyContent.getBeanSet(SillyVariableConvertor.class);
        for (SillyVariableConvertor convertor : beanSet) {
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
        final Set<SillyVariableSaveHandle> beanSet = SpringSillyContent.getBeanSet(SillyVariableSaveHandle.class);
        for (SillyVariableSaveHandle saveHandle : beanSet) {
            addSillyVariableSaveHandle(saveHandle);
        }
    }

    @Override
    protected void hookInitSillyHtmlTagTemplateList() {
        final Set<SillyHtmlTagTemplate> beanSet = SpringSillyContent.getBeanSet(SillyHtmlTagTemplate.class);
        for (SillyHtmlTagTemplate htmlTagTemplate : beanSet) {
            addSillyHtmlTagTemplate(htmlTagTemplate);
        }
    }

    @Override
    protected void initEngineSillyService() {
        final Set<SillyEngineService> engineServices = SpringSillyContent.getBeanSet(SillyEngineService.class);
        for (SillyEngineService engineService : engineServices) {
            addSillyEngineService(engineService);
        }
    }

    @Override
    protected void initResumeSillyService() {
        final Set<SillyResumeService> resumeServices = SpringSillyContent.getBeanSet(SillyResumeService.class);
        for (SillyResumeService resumeService : resumeServices) {
            addSillyResumeService(resumeService);
        }
    }

    @Override
    protected void initReadSillyService() {
        final Set<SillyReadService> readServices = SpringSillyContent.getBeanSet(SillyReadService.class);
        for (SillyReadService readService : readServices) {
            addSillyReadService(readService);
        }
    }

    @Override
    protected void initWriteSillyService() {
        final Set<SillyWriteService> writeServices = SpringSillyContent.getBeanSet(SillyWriteService.class);
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
        SillyTaskGroupHandle handle = SpringSillyContent.getBean(SillyTaskGroupHandle.class);
        loadSillyTaskGroup(handle);
        loadSillyTaskCategoryGroup(handle);
        SillyAssert.notNull(handle, "任务组处理器未能正确加载");
        return handle;
    }

    @Override
    protected void hookSillyCacheList() {
        final Set<SillyCache> sillyCaches = SpringSillyContent.getBeanSet(SillyCache.class);
        for (SillyCache sillyCache : sillyCaches) {
            addSillyCache(sillyCache);
        }
    }

    @Override
    protected void hookSillyCurrentUserUtilList() {
        final Set<SillyCurrentUserUtil> sillyCurrentUserUtils = SpringSillyContent.getBeanSet(SillyCurrentUserUtil.class);
        for (SillyCurrentUserUtil currentUserUtil : sillyCurrentUserUtils) {
            addSillyCurrentUserUtil(currentUserUtil);
        }
    }

    protected void loadSillyTaskCategoryGroup(SillyTaskGroupHandle handle) {
        Set<SillyTaskCategoryGroup> beanSet = SpringSillyContent.getBeanSet(SillyTaskCategoryGroup.class);
        for (SillyTaskCategoryGroup sillyTaskCategoryGroup : beanSet) {
            handle.addCategorySillyTaskGroup(sillyTaskCategoryGroup);
        }
    }

    protected void loadSillyTaskGroup(SillyTaskGroupHandle handle) {
        Set<SillyTaskGroup> beanSet = SpringSillyContent.getBeanSet(SillyTaskGroup.class);
        for (SillyTaskGroup sillyTaskGroup : beanSet) {
            handle.addSillyTaskGroup(sillyTaskGroup);
        }
    }

    /**
     * 加载/刷新傻瓜流程配置资源
     */
    public void refreshProcessResource() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(getProcessPattern());
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

    @Override
    public String getProcessPattern() {
        return processPattern;
    }

    public void setProcessPattern(String processPattern) {
        this.processPattern = processPattern;
    }

    public Class<? extends SillyProcessProperty> getProcessPropertyClazz(String category) {
        return processPropertyClazz;
    }

    public void setProcessPropertyClazz(Class<? extends SillyProcessProperty> processPropertyClazz) {
        this.processPropertyClazz = processPropertyClazz;
    }

    @Override
    public Class<? extends SillyPropertyHandle> getPropertyHandleClazz(String category) {
        return propertyHandleClazz;
    }

    public void setPropertyHandleClazz(Class<? extends SillyPropertyHandle> propertyHandleClazz) {
        this.propertyHandleClazz = propertyHandleClazz;
    }

    public SillyPropertyHandle getSillyPropertyHandle() {
        return SillyReflectUtil.newInstance(propertyHandleClazz);
    }

}