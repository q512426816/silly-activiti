/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.SillyInitializable;
import com.iqiny.silly.core.convertor.SillyStringConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;

import java.util.*;

/**
 * 配置类
 */
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


    @Override
    public void init() {
        preInit();
        // 1 初始化 基本属性
        initFiled();
        // 2 初始化 傻瓜工厂
        initSillyFactory();
        // 3 初始化傻瓜转换器
        initSillyConvertorMap();

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
}
