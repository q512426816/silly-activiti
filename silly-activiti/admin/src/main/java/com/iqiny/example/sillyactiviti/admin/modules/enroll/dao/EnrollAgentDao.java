/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.enroll.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iqiny.example.sillyactiviti.admin.modules.enroll.entity.EnrollAgentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 招生员信息
 * 
 * @author xy
 * @email sog_xy@163.com
 * @date 2020-10-25 13:01:02
 */
@Mapper
public interface EnrollAgentDao extends BaseMapper<EnrollAgentEntity> {
	
}
