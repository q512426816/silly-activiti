/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.res.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iqiny.example.sillyactiviti.admin.modules.res.entity.PersonEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户信息
 * 
 * @author qiny
 * @email qy-1994-2008@163.com
 * @date 2020-10-11 14:46:35
 */
@Mapper
public interface PersonDao extends BaseMapper<PersonEntity> {
	
}
