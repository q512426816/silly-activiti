/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.common.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Jwt参数配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    public static final String DEFAULT_HEADER = "Authorization";
    public static final String DEFAULT_TOKEN_START = "Bearer ";
    public static final String DEFAULT_SECRET = "DEFAULT_SECRET[" + JwtProperties.class.getName();
    public static final Long DEFAULT_EXPIRATION_TIME = 60 * 60 * 1000L;
    public static final String DEFAULT_TOKEN_NAME = "token";
    public static final String DEFAULT_COOKIE_NAME = "crrcdt_token";

    /**
     * Request Headers ： Authorization
     */
    private String header = DEFAULT_HEADER;

    /**
     * 令牌前缀，get时 最后 添加个空格
     */
    private String tokenStartWith = DEFAULT_TOKEN_START;

    /**
     * 应用密钥
     */
    private String secret = DEFAULT_SECRET;

    /**
     * 令牌过期时间 此处单位/毫秒
     */
    private Long expirationTime = DEFAULT_EXPIRATION_TIME;

    /**
     * Token 返回给客户端的属性名称
     */
    private String tokenName = DEFAULT_TOKEN_NAME;

    /**
     * Cookie Name
     */
    private String cookieName = DEFAULT_COOKIE_NAME;

}