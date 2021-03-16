package com.iqiny.example.sillyactiviti.admin.common.silly.config;

import com.iqiny.silly.activiti.ActivitiSillyConfig;
import com.iqiny.silly.activiti.spring.SpringSillyContent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * silly activiti 配置文件
 */
@Configuration
public class MySillyConfig {

    @Bean
    public SpringSillyContent createSillyContent() {
        return new SpringSillyContent();
    }

    @Bean
    public ActivitiSillyConfig createSillyConfig() {
        return new ActivitiSillyConfig();
    }

}
