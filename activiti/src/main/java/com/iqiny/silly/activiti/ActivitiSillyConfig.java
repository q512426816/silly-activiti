package com.iqiny.silly.activiti;

import com.iqiny.silly.activiti.convertor.SillyListConvertor;
import com.iqiny.silly.activiti.convertor.SillyListListConvertor;
import com.iqiny.silly.core.config.AbstractSillyConfig;

public class ActivitiSillyConfig extends AbstractSillyConfig {

    @Override
    protected void preInit() {
        System.out.println("ActivitiSillyConfig 初始化开始");
    }

    @Override
    protected void initComplete() {
        System.out.println("ActivitiSillyConfig 初始化完成");
    }

    @Override
    protected void hookInitSillyConvertorMap() {
        addSillyVariableConvertor(new SillyListConvertor());
        addSillyVariableConvertor(new SillyListListConvertor());
    }
}
