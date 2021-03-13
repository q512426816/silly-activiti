package com.iqiny.silly.core.config;

import com.iqiny.silly.common.util.CurrentUserUtil;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.convertor.SillyStringConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置类
 */
public abstract class AbstractSillyConfig implements SillyConfig {

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
    protected Map<String, SillyVariableConvertor> sillyConvertorMap;


    /**
     * 傻瓜工厂工厂
     */
    private Map<String, SillyFactory> sillyFactoryMap;


    public void init() {
        preInit();
        // 1 初始化 傻瓜工厂
        initSillyFactory();
        // 2.初始化傻瓜转换器
        initSillyConvertorMap();

        checkConfig();

        initComplete();
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
        SillyAssert.notNull(this.currentUserUtil);
        SillyAssert.notNull(this.sillyEngineService);
        SillyAssert.notNull(this.sillyConvertorMap);
    }

    private void initSillyFactory() {
        if (sillyFactoryMap == null) {
            sillyFactoryMap = new LinkedHashMap<>();
        }
        hookInitSillyFactoryMap();
    }

    protected void addSillyFactory(SillyFactory sillyFactory) {
        if (sillyFactoryMap == null) {
            sillyFactoryMap = new LinkedHashMap<>();
        }
        sillyFactoryMap.put(sillyFactory.category(), sillyFactory);
    }

    /**
     * 初始 傻瓜工厂 回调方法
     */
    protected abstract void hookInitSillyFactoryMap();

    protected void initSillyConvertorMap() {
        addSillyVariableConvertor(new SillyStringConvertor());
        hookInitSillyConvertorMap();
    }

    protected void addSillyVariableConvertor(SillyVariableConvertor convertor) {
        if (sillyConvertorMap == null) {
            sillyConvertorMap = new LinkedHashMap<>();
        }
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
    public void setSillyConvertorMap(Map<String, SillyVariableConvertor> sillyConvertorMap) {
        this.sillyConvertorMap = sillyConvertorMap;
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
