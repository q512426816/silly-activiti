/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.silly.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableConfigurationProperties(ActivitiConfig.class)
@ConfigurationProperties(
    prefix = ActivitiConfig.PREFIX
)
public class ActivitiConfig {

    public static final String PREFIX = "activiti";

    private DruidDataSource dataSource;

    public void setDataSource(DruidDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    @ConditionalOnMissingBean(ProcessEngineConfigurationImpl.class)
    public ProcessEngineConfigurationImpl processEngineConfiguration(PlatformTransactionManager transactionManager) {
        final SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setDataSource(dataSource);
        springProcessEngineConfiguration.setTransactionManager(transactionManager);
        return springProcessEngineConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean(ProcessEngineFactoryBean.class)
    public ProcessEngineFactoryBean processEngineFactoryBean(ProcessEngineConfigurationImpl configuration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(configuration);
        return processEngineFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean(RuntimeService.class)
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    @ConditionalOnMissingBean(HistoryService.class)
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    @ConditionalOnMissingBean(RepositoryService.class)
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    @ConditionalOnMissingBean(TaskService.class)
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    @ConditionalOnMissingBean(ManagementService.class)
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    @ConditionalOnMissingBean(DynamicBpmnService.class)
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService();
    }

}
