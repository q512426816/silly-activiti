/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.edu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iqiny.example.sillyactiviti.admin.modules.edu.entity.EduTuitionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 缴费信息
 * 
 * @author qiny
 * @email qy-1994-2008@163.com
 * @date 2020-10-25 20:05:22
 */
@Mapper
public interface EduTuitionDao extends BaseMapper<EduTuitionEntity> {
	
}
