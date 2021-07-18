/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti;

import com.alibaba.fastjson.JSON;
import com.iqiny.silly.activiti.convertor.SillyListConvertor;
import com.iqiny.silly.activiti.spring.SpringSillyContent;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.config.AbstractSillyConfig;
import com.iqiny.silly.core.config.CurrentUserUtil;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.impl.DefaultProcessProperty;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@SuppressWarnings("all")
public class ActivitiSillyConfig extends AbstractSillyConfig {

    private final static Log log = LogFactory.getLog(ActivitiSillyConfig.class);

    protected String processPattern = "classpath*:silly/*.json";

    protected Class<? extends SillyProcessProperty> processPropertyClazz = DefaultProcessProperty.class;

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
        if (resource == null || !resource.isFile()) {
            return;
        }
        try {
            File jsonFile = resource.getFile();
            String json = FileUtils.readFileToString(jsonFile);
            SillyProcessProperty processProperty = JSON.parseObject(json, getProcessPropertyClazz());
            String fileName = jsonFile.getName();
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
        log.info("转换器：" + sillyConvertorMap.size() + "个，对象工厂：" + sillyFactoryMap.size() + "个" +
                "读取服务：" + sillyReadServiceMap.size() + "个，写入服务：" + sillyWriteServiceMap.size() + "个" +
                "流程属性：" + sillyProcessPropertyMap.size() + "个");
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
}
