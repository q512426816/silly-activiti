/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.config.html;

/**
 * 傻瓜变量 对应的 html 标签模板
 */
public interface SillyHtmlTagTemplate {

    String getHtmlType();

    String getHtml(SillyHtmlTagConfig tagConfig);
    
}
