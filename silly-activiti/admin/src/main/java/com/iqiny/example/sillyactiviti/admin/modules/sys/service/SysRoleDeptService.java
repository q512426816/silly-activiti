package com.iqiny.example.sillyactiviti.admin.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysRoleDeptEntity;

import java.util.List;

/**
 * 角色与部门对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleDeptService extends IService<SysRoleDeptEntity> {
	
	void saveOrUpdate(String roleId, List<String> deptIdList);
	
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	List<String> queryDeptIdList(String[] roleIds) ;

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(String[] roleIds);
}