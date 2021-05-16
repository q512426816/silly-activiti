/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iqiny.example.sillyactiviti.admin.modules.edu.entity.EduTuitionEntity;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;

import java.util.Map;

/**
 * 缴费信息
 *
 * @author qiny
 * @email qy-1994-2008@163.com
 * @date 2020-10-25 20:05:22
 */
public interface EduTuitionService extends IService<EduTuitionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

