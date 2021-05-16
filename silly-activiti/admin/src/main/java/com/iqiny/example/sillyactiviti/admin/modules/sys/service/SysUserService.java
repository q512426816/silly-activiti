/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysUserEntity;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 *
 */
public interface SysUserService extends IService<SysUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询用户的所有菜单ID
     */
    List<String> queryAllMenuId(String userId);

    /**
     * 保存用户
     */
    void saveUser(SysUserEntity user);

    /**
     * 修改用户
     */
    void update(SysUserEntity user);

    /**
     * 修改密码
     * @param userId       用户ID
     * @param password     原密码
     * @param newPassword  新密码
     */
    boolean updatePassword(String userId, String password, String newPassword);

    /**
     * 通过 用户名获取用户信息
     */
    SysUserEntity getByUsername(String username);

    /**
     * 获取用户权限
     * @param userId
     * @return
     */
    List<String> queryAllPerms(String userId);
}
