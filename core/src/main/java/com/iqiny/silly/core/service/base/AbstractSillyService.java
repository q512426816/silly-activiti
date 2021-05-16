/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service.base;

import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.CurrentUserUtil;
import com.iqiny.silly.core.config.SillyConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;
import com.iqiny.silly.core.service.SillyService;

import java.util.Map;

/**
 * 默认抽象方法
 *
 * @param <M> 主
 * @param <N> 节点
 * @param <V> 变量
 */
public abstract class AbstractSillyService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable, T> implements SillyService {

    protected SillyFactory<M, N, V, ? extends SillyResume> sillyFactory;

    protected SillyEngineService<T> sillyEngineService;

    protected CurrentUserUtil currentUserUtil;

    protected SillyResumeService sillyResumeService;

    protected Map<String, SillyVariableConvertor> sillyConvertorMap;

    @Override
    public void init() {
        SillyConfig sillyConfig = getSillyConfig();
        SillyAssert.notNull(sillyConfig);
        setSillyFactory(sillyConfig.getSillyFactory(usedCategory()));
        setSillyEngineService(sillyConfig.getSillyEngineService());
        setCurrentUserUtil(sillyConfig.getCurrentUserUtil());
        setSillyConvertorMap(sillyConfig.getSillyConvertorMap());
        setSillyResumeService(sillyConfig.getSillyResumeService());
    }

    @Override
    public SillyConfig getSillyConfig() {
        return SillyConfigUtil.getSillyConfig(usedCategory());
    }

    public void setSillyFactory(SillyFactory<M, N, V, ? extends SillyResume> sillyFactory) {
        this.sillyFactory = sillyFactory;
    }

    public void setSillyEngineService(SillyEngineService<T> sillyEngineService) {
        this.sillyEngineService = sillyEngineService;
    }

    public void setCurrentUserUtil(CurrentUserUtil currentUserUtil) {
        this.currentUserUtil = currentUserUtil;
    }

    public void setSillyConvertorMap(Map<String, SillyVariableConvertor> sillyConvertorMap) {
        this.sillyConvertorMap = sillyConvertorMap;
    }

    public void setSillyResumeService(SillyResumeService sillyResumeService) {
        this.sillyResumeService = sillyResumeService;
    }

    protected SillyVariableConvertor<?> getSillyConvertor(String handleKey) {
        if (sillyConvertorMap == null) {
            throw new SillyException("Silly数据处理器未设置！");
        }

        return sillyConvertorMap.get(handleKey);
    }


}
