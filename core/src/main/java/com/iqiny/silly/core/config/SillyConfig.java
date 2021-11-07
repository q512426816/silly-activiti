/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.config.html.SillyHtmlTagTemplate;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.Map;
import java.util.Set;

/**
 * 配置类
 */
@SuppressWarnings("all")
public interface SillyConfig {

    /**
     * 初始化方法
     */
    void init();

    /**
     * 支持的分类
     */
    Set<String> supportCategories();

    boolean isSupport(String category);

    CurrentUserUtil getCurrentUserUtil();

    SillyEngineService getSillyEngineService();

    Map<String, SillyVariableConvertor> getSillyConvertorMap();

    Map<String, SillyVariableSaveHandle> getSillyVariableSaveHandleMap();

    SillyResumeService getSillyResumeService();

    SillyFactory getSillyFactory(String category);

    SillyReadService getSillyReadService(String category);

    SillyWriteService getSillyWriteService(String category);

    SillyProcessProperty getSillyProcessProperty(String category);

    SillyPropertyHandle getSillyPropertyHandle();

    SillyTaskGroupHandle getSillyTaskGroupHandle();

    SillyHtmlTagTemplate getHtmlTemplate(String htmlType);

}
