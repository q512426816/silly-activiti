package com.iqiny.silly.activiti;

import com.iqiny.silly.activiti.convertor.SillyListConvertor;
import com.iqiny.silly.activiti.convertor.SillyListListConvertor;
import com.iqiny.silly.activiti.spring.SillySpringContextHolder;
import com.iqiny.silly.common.util.CurrentUserUtil;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.config.AbstractSillyConfig;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ActivitiSillyConfig extends AbstractSillyConfig {

    @Autowired
    private SillySpringContextHolder sillySpringContextHolder;

    @Override
    protected void preInit() {
        System.out.println("ActivitiSillyConfig 初始化开始");
    }

    @Override
    protected void hookInitSillyFactoryMap() {
        final Set<SillyFactory> beanSet = sillySpringContextHolder.getBeanSet(SillyFactory.class);
        for (SillyFactory sillyFactory : beanSet) {
            addSillyFactory(sillyFactory);
        }
    }

    @Override
    protected void hookInitSillyConvertorMap() {
        addSillyVariableConvertor(new SillyListConvertor());
        addSillyVariableConvertor(new SillyListListConvertor());
        final Set<SillyVariableConvertor> beanSet = sillySpringContextHolder.getBeanSet(SillyVariableConvertor.class);
        for (SillyVariableConvertor convertor : beanSet) {
            addSillyVariableConvertor(convertor);
        }
    }

    @Override
    protected void initComplete() {
        System.out.println("ActivitiSillyConfig 初始化完成");
    }

    @Override
    @Autowired
    public void setCurrentUserUtil(CurrentUserUtil currentUserUtil) {
        this.currentUserUtil = currentUserUtil;
    }

    @Override
    @Autowired
    public void setSillyEngineService(SillyEngineService sillyEngineService) {
        this.sillyEngineService = sillyEngineService;
    }

    @Override
    @Autowired
    public void setSillyResumeService(SillyResumeService sillyResumeService) {
        this.sillyResumeService = sillyResumeService;
    }
}
