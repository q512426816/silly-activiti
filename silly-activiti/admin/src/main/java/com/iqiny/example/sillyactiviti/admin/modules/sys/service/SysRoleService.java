/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysRoleEntity;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;

import java.util.Map;

/**
 * 角色
 *
 *
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveRole(SysRoleEntity role);

	void update(SysRoleEntity role);
	
	void deleteBatch(String[] roleIds);

}
