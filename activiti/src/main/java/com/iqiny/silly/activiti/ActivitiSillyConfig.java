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
import com.iqiny.silly.activiti.convertor.SillyListConvertor;
import com.iqiny.silly.activiti.savehandle.DataJoinVariableSaveHandle;
import com.iqiny.silly.activiti.savehandle.OverwriteVariableSaveHandle;
import com.iqiny.silly.activiti.savehandle.SaveVariableSaveHandle;
import com.iqiny.silly.activiti.savehandle.SkipVariableSaveHandle;
import com.iqiny.silly.activiti.spring.SillySpelPropertyHandle;
import com.iqiny.silly.activiti.spring.SpringSillyContent;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyReflectUtil;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.config.AbstractSillyConfig;
import com.iqiny.silly.core.config.CurrentUserUtil;
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
public class ActivitiSillyConfig extends AbstractSillyConfig {

    private final static Log log = LogFactory.getLog(ActivitiSillyConfig.class);

    protected String processPattern = "classpath*:/silly/*.json";

    protected Class<? extends SillyProcessProperty> processPropertyClazz = DefaultProcessProperty.class;

    protected Class<? extends SillyPropertyHandle> propertyHandleClazz = SillySpelPropertyHandle.class;

    public ActivitiSillyConfig() {
    }

    @Override
    protected void preInit() {
        log.info("Category:" + supportCategories() + "  " + this.getClass().getName() + " 初始化开始");
    }

    @Override
    protected void initFiled() {
        this.currentUserUtil = SpringSillyContent.getBean(CurrentUserUtil.class);
        this.sillyEngineService = SpringSillyContent.getBean(SillyEngineService.class);
        this.sillyResumeService = SpringSillyContent.getBean(SillyResumeService.class);
    }

    @Override
    protected void initBaseSillyFactoryMap() {

    }

    @Override
    protected void hookInitSillyFactoryMap() {
        final Set<SillyFactory> beanSet = SpringSillyContent.getBeanSet(SillyFactory.class);
        for (SillyFactory sillyFactory : beanSet) {
            addSillyFactory(sillyFactory);
        }
    }

    @Override
    protected void hookInitSillyConvertorMap() {
        addSillyVariableConvertor(new SillyListConvertor());
        final Set<SillyVariableConvertor> beanSet = SpringSillyContent.getBeanSet(SillyVariableConvertor.class);
        for (SillyVariableConvertor convertor : beanSet) {
            addSillyVariableConvertor(convertor);
        }
    }

    @Override
    protected void hookInitSillyVariableSaveHandleMap() {
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
    protected void hookInitSillyHtmlTagTemplateMap() {
        final Set<SillyHtmlTagTemplate> beanSet = SpringSillyContent.getBeanSet(SillyHtmlTagTemplate.class);
        for (SillyHtmlTagTemplate htmlTagTemplate : beanSet) {
            addSillyHtmlTagTemplateMap(htmlTagTemplate);
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
    protected void hookSillyProcessPropertyMap() {
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
            SillyProcessProperty processProperty = JSON.parseObject(json, getProcessPropertyClazz());
            String fileName = resource.getFilename();
            String category = fileName.substring(0, fileName.lastIndexOf("."));
            addSillyProcessProperty(category, processProperty);
            log.info("成功更新流程配置文件:" + category);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    protected void initComplete() {
        log.info(this.getClass().getName() + " 初始化完成");
        log.info("转换器：" + sillyConvertorMap.size() + "个，对象工厂：" + sillyFactoryMap.size() + "个，" +
                "读取服务：" + sillyReadServiceMap.size() + "个，写入服务：" + sillyWriteServiceMap.size() + "个，" +
                "流程属性：" + sillyProcessPropertyMap.size() + "个");
        log.info("成功注册" + sillyTaskGroupHandle.allSillyTaskGroup().size() + " 个任务组对象");
        log.info("成功注册" + sillyTaskGroupHandle.allCategoryGroup().size() + " 个类型任务组对象");
    }

    public String getProcessPattern() {
        return processPattern;
    }

    public void setProcessPattern(String processPattern) {
        this.processPattern = processPattern;
    }

    public Class<? extends SillyProcessProperty> getProcessPropertyClazz() {
        return processPropertyClazz;
    }

    public void setProcessPropertyClazz(Class<? extends SillyProcessProperty> processPropertyClazz) {
        this.processPropertyClazz = processPropertyClazz;
    }

    public Class<? extends SillyPropertyHandle> getPropertyHandleClazz() {
        return propertyHandleClazz;
    }

    public void setPropertyHandleClazz(Class<? extends SillyPropertyHandle> propertyHandleClazz) {
        this.propertyHandleClazz = propertyHandleClazz;
    }

    @Override
    public SillyPropertyHandle getSillyPropertyHandle() {
        return SillyReflectUtil.newInstance(propertyHandleClazz);
    }
}
