/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring-boot-starter 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.starter;

import com.iqiny.silly.activiti.scan.AutoScanSillyConfigContent;
import com.iqiny.silly.activiti.spring.SpringSillyContent;
import com.iqiny.silly.core.base.SillyProperties;
import com.iqiny.silly.core.config.SillyConfigContent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 傻瓜Spring自动配置
 */
@Configuration
@EnableConfigurationProperties({StarterSillyProperties.class})
@ComponentScan(
        basePackages = {
                "com.iqiny.silly.spring",
        }
)
@ConditionalOnProperty(value = "silly.enabled", matchIfMissing = true)
public class SillyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringSillyContent springSillyContent() {
        return new SpringSillyContent();
    }

    @Bean
    @ConditionalOnMissingBean
    public SillyConfigContent sillyConfigContent(SillyProperties properties) {
        SillyConfigContent content = new AutoScanSillyConfigContent();
        content.setSillyProperties(properties);
        return content;
    }

}
