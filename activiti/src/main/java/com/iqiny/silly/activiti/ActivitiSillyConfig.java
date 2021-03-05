package com.iqiny.silly.activiti;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.core.config.AbstractSillyConfig;
import com.iqiny.silly.activiti.convertor.SillyListConvertor;
import com.iqiny.silly.activiti.convertor.SillyListListConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;

import java.util.Map;

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
    protected void hookInitSillyConvertorMap(Map<String, SillyVariableConvertor> convertorMap) {
        convertorMap.put(SillyConstant.ActivitiNode.CONVERTOR_LIST, new SillyListConvertor());
        convertorMap.put(SillyConstant.ActivitiNode.CONVERTOR_LIST_LIST, new SillyListListConvertor());
    }
}
