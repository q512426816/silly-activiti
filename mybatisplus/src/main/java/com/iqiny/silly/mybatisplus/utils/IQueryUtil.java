/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

public interface IQueryUtil {

    <T> IPage<T> getPage(Map<String, Object> params);
}
