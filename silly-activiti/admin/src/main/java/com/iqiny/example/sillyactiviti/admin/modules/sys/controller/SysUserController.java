/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.controller;


import com.iqiny.example.sillyactiviti.admin.common.annotation.SysLog;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.admin.common.utils.SecurityUtils;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysUserEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysUserRoleService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysUserService;
import com.iqiny.example.sillyactiviti.common.utils.R;
import com.iqiny.example.sillyactiviti.common.validator.Assert;
import com.iqiny.example.sillyactiviti.common.validator.ValidatorUtils;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 *
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        return R.ok().put("user", sysUserService.getByUsername(SecurityUtils.getUsername()));
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @RequestMapping("/password")
    public R password(String password, String newPassword) {
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
        //password = ShiroUtils.sha256(password, getUser().getSalt());
        //新密码
        //newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @PreAuthorize("hasAnyAuthority('sys:user:info')")
    public R info(@PathVariable("userId") String userId) {
        SysUserEntity user = sysUserService.getById(userId);
        user.setPassword(null);
        //获取用户所属的角色列表
        List<String> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @RequestMapping("/save")
    @PreAuthorize("hasAnyAuthority('sys:user:save')")
    public R save(@RequestBody SysUserEntity user) {
        if (user.getId() == null) {
            ValidatorUtils.validateAddEntity(user);
            sysUserService.saveUser(user);
        } else {
            ValidatorUtils.validateUpdateEntity(user);
            sysUserService.update(user);
        }
        return R.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @RequestMapping("/update")
    @PreAuthorize("hasAnyAuthority('sys:user:update')")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        sysUserService.update(user);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('sys:user:delete')")
    public R delete(@RequestBody Long[] userIds) {
        /*if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }*/

        sysUserService.removeByIds(Arrays.asList(userIds));

        return R.ok();
    }
}
