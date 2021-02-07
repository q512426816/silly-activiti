package com.iqiny.example.sillyactiviti.admin.common.silly.config;

import com.iqiny.silly.activiti.ActivitiSillyConfig;
import com.iqiny.silly.activiti.SillyActivitiEngineServiceImpl;
import com.iqiny.silly.core.config.SillyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * silly activiti 配置文件
 */
@Configuration
public class MySillyConfig {

    @Bean(initMethod = "init")
    public SillyConfig createSillyConfig(MyCurrentUserUtil currentUserUtil, SillyActivitiEngineServiceImpl sillyActivitiEngineService) {
        final SillyConfig sillyConfig = new ActivitiSillyConfig();
        sillyConfig.setCurrentUserUtil(currentUserUtil);
        sillyConfig.setSillyEngineService(sillyActivitiEngineService);
        return sillyConfig;
    }

}
