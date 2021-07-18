/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.enroll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iqiny.example.sillyactiviti.admin.modules.enroll.entity.EnrollAgentEntity;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;

import java.util.Map;

/**
 * 招生员信息
 *
 * @author xy
 * @email sog_xy@163.com
 * @date 2020-10-25 13:01:02
 */
public interface EnrollAgentService extends IService<EnrollAgentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

