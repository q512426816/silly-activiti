/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.controller;


import com.iqiny.example.sillyactiviti.admin.common.utils.SecurityUtils;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysUserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller公共组件
 *
 *
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return SecurityUtils.getUser();
	}

	protected String getUserId() {
		return SecurityUtils.getUserId();
	}
}
