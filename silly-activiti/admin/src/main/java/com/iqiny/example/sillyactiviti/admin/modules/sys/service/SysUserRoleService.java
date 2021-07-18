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
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysUserRoleEntity;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 *
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {
	
	void saveOrUpdate(String userId, List<String> roleIdList);
	
	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<String> queryRoleIdList(String userId);

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(String[] roleIds);
}
