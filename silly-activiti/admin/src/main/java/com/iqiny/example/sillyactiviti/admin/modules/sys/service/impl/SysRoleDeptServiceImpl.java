/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iqiny.example.sillyactiviti.admin.modules.sys.dao.SysRoleDeptDao;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysRoleDeptEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysRoleDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 角色与部门对应关系
 *
 *
 */
@Service
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptDao, SysRoleDeptEntity> implements SysRoleDeptService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(String roleId, List<String> deptIdList) {
		//先删除角色与部门关系
		deleteBatch(new String[]{roleId});

		if(deptIdList.size() == 0){
			return ;
		}

		//保存角色与菜单关系
		for(String deptId : deptIdList){
			SysRoleDeptEntity sysRoleDeptEntity = new SysRoleDeptEntity();
			sysRoleDeptEntity.setDeptId(deptId);
			sysRoleDeptEntity.setRoleId(roleId);

			this.save(sysRoleDeptEntity);
		}
	}

	@Override
	public List<String> queryDeptIdList(String[] roleIds) {
		return baseMapper.queryDeptIdList(roleIds);
	}

	@Override
	public int deleteBatch(String[] roleIds){
		return baseMapper.deleteBatch(roleIds);
	}
}
