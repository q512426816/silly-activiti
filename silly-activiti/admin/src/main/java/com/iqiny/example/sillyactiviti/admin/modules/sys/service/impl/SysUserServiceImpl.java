/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iqiny.example.sillyactiviti.admin.common.annotation.DataFilter;
import com.iqiny.example.sillyactiviti.admin.common.base.BaseService;
import com.iqiny.example.sillyactiviti.admin.common.utils.Constant;
import com.iqiny.example.sillyactiviti.admin.common.utils.Query;
import com.iqiny.example.sillyactiviti.admin.modules.sys.dao.SysUserDao;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysDeptEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysUserEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysDeptService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysUserRoleService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysUserService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统用户
 *
 *
 */
@Service
public class SysUserServiceImpl extends BaseService<SysUserDao, SysUserEntity> implements SysUserService {

    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public List<String> queryAllMenuId(String userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");

        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );

        for (SysUserEntity sysUserEntity : page.getRecords()) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
            sysUserEntity.setDeptName(sysDeptEntity.getName());
        }

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        //String salt = RandomStringUtils.randomAlphanumeric(20);
        //user.setSalt(salt);
        //user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        this.save(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserEntity user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            SysUserEntity userEntity = this.getById(user.getId());
            //user.setPassword(ShiroUtils.sha256(user.getPassword(), userEntity.getSalt()));
        }
        this.updateById(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getId(), user.getRoleIdList());
    }


    @Override
    public boolean updatePassword(String userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

    @Override
    public SysUserEntity getByUsername(String username) {
        if(StringUtils.isEmpty(username)){
            return null;
        }
        QueryWrapper<SysUserEntity> qw = new QueryWrapper<>();
        final SysUserEntity userEntity = new SysUserEntity();
        userEntity.setUsername(username);
        qw.setEntity(userEntity);
        return baseMapper.selectOne(qw);
    }

    @Override
    public List<String> queryAllPerms(String userId) {
        if (Objects.equals(userId, Constant.SUPER_ADMIN)) {
            userId = null;
        }
        final List<String> perms = baseMapper.queryAllPerms(userId);
        List<String> allPerms = new ArrayList<>();
        for (String perm : perms) {
            allPerms.addAll(Arrays.asList(perm.split(SillyConstant.ARRAY_SPLIT_STR)));
        }
        return allPerms;
    }
}
