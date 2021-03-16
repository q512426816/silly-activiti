package com.iqiny.silly.activiti;

import com.iqiny.silly.activiti.convertor.SillyListConvertor;
import com.iqiny.silly.activiti.convertor.SillyListListConvertor;
import com.iqiny.silly.activiti.spring.SpringSillyContent;
import com.iqiny.silly.common.util.CurrentUserUtil;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.config.AbstractSillyConfig;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;

import java.util.Set;

public class ActivitiSillyConfig extends AbstractSillyConfig {

    protected SpringSillyContent springSillyContent;
    protected String category = DEFAULT_CATEGORY;

    public ActivitiSillyConfig() {

    }

    public ActivitiSillyConfig(String category) {
        this.category = category;
    }

    public void setSpringSillyContent(SpringSillyContent springSillyContent) {
        this.springSillyContent = springSillyContent;
    }

    @Override
    public String usedCategory() {
        return category;
    }

    @Override
    protected void preInit() {
        System.out.println(usedCategory() + "  ActivitiSillyConfig Aware 初始化开始");
    }

    @Override
    protected void initFiled() {
        this.currentUserUtil = springSillyContent.getBean(CurrentUserUtil.class);
        this.sillyEngineService = springSillyContent.getBean(SillyEngineService.class);
        this.sillyResumeService = springSillyContent.getBean(SillyResumeService.class);
    }

    @Override
    protected void hookInitSillyFactoryMap() {
        final Set<SillyFactory> beanSet = springSillyContent.getBeanSet(SillyFactory.class);
        for (SillyFactory sillyFactory : beanSet) {
            addSillyFactory(sillyFactory);
        }
    }

    @Override
    protected void hookInitSillyConvertorMap() {
        addSillyVariableConvertor(new SillyListConvertor());
        addSillyVariableConvertor(new SillyListListConvertor());
        final Set<SillyVariableConvertor> beanSet = springSillyContent.getBeanSet(SillyVariableConvertor.class);
        for (SillyVariableConvertor convertor : beanSet) {
            addSillyVariableConvertor(convertor);
        }
    }

    @Override
    protected void initComplete() {
        System.out.println(usedCategory() + "  ActivitiSillyConfig Aware 初始化完成! 傻瓜转换器：" + sillyConvertorMap.size() + "个，傻瓜工厂：" + sillyFactoryMap.size() + "个");
    }

}
