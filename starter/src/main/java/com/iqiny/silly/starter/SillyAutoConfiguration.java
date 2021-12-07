/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring-boot-starter
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.starter;

import com.iqiny.silly.core.base.SillyContext;
import com.iqiny.silly.spring.scan.AutoScanSillyConfigContent;
import com.iqiny.silly.spring.SpringSillyContext;
import com.iqiny.silly.core.base.SillyProperties;
import com.iqiny.silly.core.config.SillyConfigContent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
    public SillyContext sillyContext() {
        return new SpringSillyContext();
    }

    @Bean
    @ConditionalOnMissingBean
    public SillyConfigContent sillyConfigContent(SillyProperties sillyProperties, SillyContext sillyContext) {
        return new AutoScanSillyConfigContent(sillyProperties, sillyContext);
    }

}
