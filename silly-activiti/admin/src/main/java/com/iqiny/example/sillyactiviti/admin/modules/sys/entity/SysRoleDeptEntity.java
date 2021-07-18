/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与部门对应关系
 *
 *
 */
@Data
@TableName("sys_role_dept")
public class SysRoleDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private String id;

	/**
	 * 角色ID
	 */
	private String roleId;

	/**
	 * 部门ID
	 */
	private String deptId;

	
}
