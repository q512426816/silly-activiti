package com.iqiny.silly.core.config;

import com.iqiny.silly.common.Constant;
import com.iqiny.silly.common.util.CurrentUserUtil;
import com.iqiny.silly.core.convertor.SillyListConvertor;
import com.iqiny.silly.core.convertor.SillyListListConvertor;
import com.iqiny.silly.core.convertor.SillyStringConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.service.SillyEngineService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;

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
     * 流程变量 类型转换器
     */
    protected Map<String, SillyVariableConvertor> sillyConvertorMap;

    @Override
    public void init() {
        setCurrentUserUtil(initCurrentUserUtil(null));
        setSillyEngineService(initSillyEngineService(null));
        setSillyConvertorMap(initSillyConvertorMap());
    }

    protected CurrentUserUtil initCurrentUserUtil(ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        ServiceLoader<CurrentUserUtil> currentUserUtils = ServiceLoader.load(CurrentUserUtil.class, classLoader);
        return currentUserUtils.iterator().next();
    }

    protected SillyEngineService initSillyEngineService(ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        ServiceLoader<SillyEngineService> currentUserUtils = ServiceLoader.load(SillyEngineService.class, classLoader);
        return currentUserUtils.iterator().next();
    }

    protected Map<String, SillyVariableConvertor> initSillyConvertorMap() {
        Map<String, SillyVariableConvertor> convertorMap = new LinkedHashMap<>();
        convertorMap.put(Constant.ActivitiNode.CONVERTOR_LIST, new SillyListConvertor());
        convertorMap.put(Constant.ActivitiNode.CONVERTOR_STRING, new SillyStringConvertor());
        convertorMap.put(Constant.ActivitiNode.CONVERTOR_LIST_LIST, new SillyListListConvertor());
        return convertorMap;
    }

    @Override
    public CurrentUserUtil getCurrentUserUtil() {
        return currentUserUtil;
    }

    @Override
    public void setCurrentUserUtil(CurrentUserUtil currentUserUtil) {
        this.currentUserUtil = currentUserUtil;
    }

    @Override
    public SillyEngineService getSillyEngineService() {
        return sillyEngineService;
    }

    @Override
    public void setSillyEngineService(SillyEngineService sillyEngineService) {
        this.sillyEngineService = sillyEngineService;
    }

    @Override
    public Map<String, SillyVariableConvertor> getSillyConvertorMap() {
        return sillyConvertorMap;
    }

    @Override
    public void setSillyConvertorMap(Map<String, SillyVariableConvertor> sillyConvertorMap) {
        this.sillyConvertorMap = sillyConvertorMap;
    }
}
