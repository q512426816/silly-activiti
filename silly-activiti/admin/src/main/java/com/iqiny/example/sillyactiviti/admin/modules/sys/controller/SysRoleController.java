/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.controller;

import com.iqiny.example.sillyactiviti.admin.common.annotation.SysLog;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysRoleEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysRoleDeptService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysRoleMenuService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysRoleService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.common.utils.R;
import com.iqiny.example.sillyactiviti.common.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 *
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	@PreAuthorize("hasAnyAuthority('sys:role:list')")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysRoleService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@PreAuthorize("hasAnyAuthority('sys:menu:select')")
	public R select(){
		List<SysRoleEntity> list = sysRoleService.list();
		
		return R.ok().put("list", list);
	}
	
	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@PreAuthorize("hasAnyAuthority('sys:menu:info')")
	public R info(@PathVariable("roleId") String roleId){
		SysRoleEntity role = sysRoleService.getById(roleId);
		
		//查询角色对应的菜单
		List<String> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		//查询角色对应的部门
		List<String> deptIdList = sysRoleDeptService.queryDeptIdList(new String[]{roleId});
		role.setDeptIdList(deptIdList);
		
		return R.ok().put("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@RequestMapping("/save")
	@PreAuthorize("hasAnyAuthority('sys:menu:save')")
	public R save(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		sysRoleService.saveRole(role);
		
		return R.ok();
	}
	
	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@RequestMapping("/update")
	@PreAuthorize("hasAnyAuthority('sys:menu:update')")
	public R update(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		sysRoleService.update(role);
		
		return R.ok();
	}
	
	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@RequestMapping("/delete")
	@PreAuthorize("hasAnyAuthority('sys:menu:delete')")
	public R delete(@RequestBody String[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		
		return R.ok();
	}
}
