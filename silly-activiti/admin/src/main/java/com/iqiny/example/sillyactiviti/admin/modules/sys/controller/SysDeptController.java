/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.controller;

import com.iqiny.example.sillyactiviti.admin.common.utils.Constant;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysDeptEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysDeptService;
import com.iqiny.example.sillyactiviti.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 部门管理
 *
 *
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:dept:list')")
    public List<SysDeptEntity> list() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<>());

        return deptList;
    }

    /**
     * 选择部门(添加、修改菜单)
     */
    @RequestMapping("/select")
    @PreAuthorize("hasAnyAuthority('sys:dept:select')")
    public R select() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<>());

        //添加一级部门
        if (Objects.equals(getUserId(), Constant.SUPER_ADMIN)) {
            SysDeptEntity root = new SysDeptEntity();
            root.setDeptId("0");
            root.setName("一级部门");
            root.setParentId("-1");
            root.setOpen(true);
            deptList.add(root);
        }

        return R.ok().put("deptList", deptList);
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @RequestMapping("/info")
    @PreAuthorize("hasAnyAuthority('sys:dept:list')")
    public R info() {
        String deptId = "0";
        if (!getUserId().equals(Constant.SUPER_ADMIN)) {
            List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<>());
            String parentId = null;
            for (SysDeptEntity sysDeptEntity : deptList) {
                if (parentId == null) {
                    parentId = sysDeptEntity.getParentId();
                    continue;
                }

                if (Long.valueOf(parentId) > Long.valueOf(sysDeptEntity.getParentId())) {
                    parentId = sysDeptEntity.getParentId();
                }
            }
            deptId = parentId;
        }

        return R.ok().put("deptId", deptId);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{deptId}")
    @PreAuthorize("hasAnyAuthority('sys:dept:info')")
    public R info(@PathVariable("deptId") Long deptId) {
        SysDeptEntity dept = sysDeptService.getById(deptId);

        return R.ok().put("dept", dept);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @PreAuthorize("hasAnyAuthority('sys:dept:save')")
    public R save(@RequestBody SysDeptEntity dept) {
        sysDeptService.save(dept);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAnyAuthority('sys:dept:update')")
    public R update(@RequestBody SysDeptEntity dept) {
        sysDeptService.updateById(dept);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('sys:dept:delete')")
    public R delete(String deptId) {
        //判断是否有子部门
        List<String> deptList = sysDeptService.queryDetpIdList(deptId);
        if (deptList.size() > 0) {
            return R.error("请先删除子部门");
        }

        sysDeptService.removeById(deptId);

        return R.ok();
    }

}
