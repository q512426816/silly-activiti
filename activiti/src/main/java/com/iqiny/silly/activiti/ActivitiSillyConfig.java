/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti;

import com.iqiny.silly.activiti.spring.SpringSillyContent;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.config.AbstractSillyConfig;
import com.iqiny.silly.core.config.CurrentUserUtil;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;

public class ActivitiSillyConfig extends AbstractSillyConfig {

    private final static Log log = LogFactory.getLog(ActivitiSillyConfig.class);


    public ActivitiSillyConfig() {
    }

    @Override
    protected void preInit() {
        log.info("Category:" + supportCategories() + "  " + this.getClass().getName() + " 初始化开始");
    }

    @Override
    protected void initFiled() {
        this.currentUserUtil = SpringSillyContent.getBean(CurrentUserUtil.class);
        this.sillyEngineService = SpringSillyContent.getBean(SillyEngineService.class);
        this.sillyResumeService = SpringSillyContent.getBean(SillyResumeService.class);
    }

    @Override
    protected void initBaseSillyFactoryMap() {
       
    }

    @Override
    protected void hookInitSillyFactoryMap() {
        final Set<SillyFactory> beanSet = SpringSillyContent.getBeanSet(SillyFactory.class);
        for (SillyFactory sillyFactory : beanSet) {
            addSillyFactory(sillyFactory);
        }
    }

    @Override
    protected void hookInitSillyConvertorMap() {
        final Set<SillyVariableConvertor> beanSet = SpringSillyContent.getBeanSet(SillyVariableConvertor.class);
        for (SillyVariableConvertor convertor : beanSet) {
            addSillyVariableConvertor(convertor);
        }
    }

    @Override
    protected void initComplete() {
        log.info(this.getClass().getName() + " 初始化完成");
        log.info("傻瓜转换器：" + sillyConvertorMap.size() + "个，傻瓜工厂：" + sillyFactoryMap.size() + "个");
    }

}
