/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.cache.SillyCache;
import com.iqiny.silly.core.config.html.SillyHtmlTagTemplate;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.savehandle.SillyVariableSaveHandle;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.group.SillyTaskGroupHandle;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.Map;

/**
 * 单业务分类 配置类
 */
@SuppressWarnings("all")
public interface SillyCategoryConfig<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> extends SillyCategory {

    /**
     * 设置分类
     * @return
     */
    String getCategory();
    SillyCategoryConfig<M, N, V> setCategory(String category);

    /**
     * 初始化方法
     */
    void init();
    
    void refresh();

    /**
     * 傻瓜上下文环境
     */
    SillyContext getSillyContext();

    SillyCategoryConfig<M, N, V> setSillyContext(SillyContext sillyContext);
    
    /**
     * 设置变量处置类类型
     */
    Class<? extends SillyPropertyHandle> getPropertyHandleClazz();

    SillyCategoryConfig<M, N, V> setPropertyHandleClazz(Class<? extends SillyPropertyHandle> clazz);

    /**
     * 当前用户获取工具
     */
    SillyCurrentUserUtil getSillyCurrentUserUtil();

    SillyCategoryConfig<M, N, V> setSillyCurrentUserUtil(SillyCurrentUserUtil sillyCurrentUserUtil);

    /**
     * 傻瓜变量转换工具
     */
    Map<String, SillyVariableConvertor> getSillyConvertorMap();

    SillyCategoryConfig<M, N, V> setSillyConvertorMap(Map<String, SillyVariableConvertor> sillyConvertorMap);

    /**
     * 傻瓜变量数据保存处置类
     */
    Map<String, SillyVariableSaveHandle> getSillyVariableSaveHandleMap();

    SillyCategoryConfig<M, N, V> setSillyVariableSaveHandleMap(Map<String, SillyVariableSaveHandle> sillyVariableSaveHandleMap);

    /**
     * 傻瓜履历服务接口
     */
    SillyResumeService getSillyResumeService();

    SillyCategoryConfig<M, N, V> setSillyResumeService(SillyResumeService sillyResumeService);

    /**
     * 傻瓜工厂
     */
    SillyFactory<M, N, V, SillyResume> getSillyFactory();

    SillyCategoryConfig<M, N, V> setSillyFactory(SillyFactory<M, N, V, SillyResume> sillyFactory);

    /**
     * 傻瓜流程引擎服务
     */
    SillyEngineService getSillyEngineService();

    SillyCategoryConfig<M, N, V> setSillyEngineService(SillyEngineService sillyEngineService);

    /**
     * 傻瓜读取服务
     */
    SillyReadService<M, N, V> getSillyReadService();

    SillyCategoryConfig<M, N, V> setSillyReadService(SillyReadService<M, N, V> sillyReadService);

    /**
     * 傻瓜写入服务
     */
    SillyWriteService<M, N, V> getSillyWriteService();

    SillyCategoryConfig<M, N, V> setSillyWriteService(SillyWriteService<M, N, V> sillyWriteService);

    /**
     * 傻瓜流程配置服务
     */
    SillyProcessProperty getSillyProcessProperty();

    SillyCategoryConfig<M, N, V> setSillyProcessProperty(SillyProcessProperty sillyProcessProperty);

    /**
     * 傻瓜任务组处理类
     */
    SillyTaskGroupHandle getSillyTaskGroupHandle();

    SillyCategoryConfig<M, N, V> setSillyTaskGroupHandle(SillyTaskGroupHandle sillyTaskGroupHandle);

    /**
     * 傻瓜缓存工具
     */
    SillyCache getSillyCache();

    SillyCategoryConfig<M, N, V> setSillyCache(SillyCache sillyCache);

    /**
     * 傻瓜页面模板生成工具
     */
    SillyHtmlTagTemplate getHtmlTemplate(String htmlType);

    SillyCategoryConfig<M, N, V> setHtmlTemplateMap(Map<String, SillyHtmlTagTemplate> sillyHtmlTagTemplateMap);

    SillyPropertyHandle newSillyPropertyHandle();

}
