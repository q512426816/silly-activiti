package com.iqiny.example.sillyactiviti.admin.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysDictEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典表
 * 
 * @author xy
 * @email sog_xy@163.com
 * @date 2020-10-26 23:17:14
 */
@Mapper
public interface SysDictDao extends BaseMapper<SysDictEntity> {
	
}
