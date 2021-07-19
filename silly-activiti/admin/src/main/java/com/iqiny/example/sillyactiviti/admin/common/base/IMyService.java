/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.base;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface IMyService<T> extends IService<T> {

    PageUtils queryPage(Map<String, Object> params);
    
}
