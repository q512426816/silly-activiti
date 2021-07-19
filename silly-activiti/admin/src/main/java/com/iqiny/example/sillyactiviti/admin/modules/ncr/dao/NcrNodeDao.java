/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.ncr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NcrNodeDao extends BaseMapper<NcrNode> {
	
}
