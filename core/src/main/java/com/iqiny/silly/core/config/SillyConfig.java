/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;

import java.util.Map;
import java.util.Set;

/**
 * 配置类
 */
public interface SillyConfig {

    void init();
    /**
     * 支持的分类
     */
    Set<String> supportCategories();

    boolean isSupport(String category);

    CurrentUserUtil getCurrentUserUtil();

    SillyEngineService getSillyEngineService();

    Map<String, SillyVariableConvertor> getSillyConvertorMap();

    SillyResumeService getSillyResumeService();

    SillyFactory getSillyFactory(String category);

}
