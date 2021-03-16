package com.iqiny.example.sillyactiviti.admin.common.silly.service;

import com.iqiny.silly.activiti.SillyActivitiEngineServiceImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Activiti 流程引擎服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MyActivitiEngineService extends SillyActivitiEngineServiceImpl {
    
    @Override
    @Autowired
    public void setProcessEngineFactoryBean(ProcessEngineFactoryBean processEngineFactoryBean) {
        this.processEngineFactoryBean = processEngineFactoryBean;
    }
    
}
