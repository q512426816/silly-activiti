/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring-boot-starter 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.starter;


import com.iqiny.silly.activiti.scan.ScanSillyConfig;
import com.iqiny.silly.activiti.spring.SpringSillyContent;
import com.iqiny.silly.core.config.SillyConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 傻瓜Spring自动配置
 */
@ConditionalOnProperty(value = "silly.enabled", matchIfMissing = true)
public class SillyAutoConfiguration {

    private static final String SCAN_PACKAGE_PROPERTY = "silly.scanEntityPackage";
    private ConfigurableEnvironment env;

    public SillyAutoConfiguration(ConfigurableEnvironment env) {
        this.env = env;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringSillyContent springSillyContent() {
        return new SpringSillyContent();
    }

    @Bean
    @ConditionalOnMissingBean
    public SillyConfig defaultSillyConfig() {
        final ScanSillyConfig scanSillyConfig = new ScanSillyConfig();
        scanSillyConfig.setEntityScanPackage(env.getProperty(SCAN_PACKAGE_PROPERTY));
        return scanSillyConfig;
    }

}
