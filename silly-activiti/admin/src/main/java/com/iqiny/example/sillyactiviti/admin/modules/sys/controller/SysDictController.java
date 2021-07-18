/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.sys.controller;

import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysDictEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysDictService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.common.utils.R;
import com.iqiny.example.sillyactiviti.common.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 数据字典表
 *
 * @author xy
 * @email sog_xy@163.com
 * @date 2020-10-26 23:17:14
 */
@RestController
@RequestMapping("sys/dict")
public class SysDictController {
    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('sys:dict:list')")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDictService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('sys:dict:info')")
    public R info(@PathVariable("id") Long id){
        SysDictEntity entity = sysDictService.getById(id);

        return R.ok().put("entity", entity);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @PreAuthorize("hasAnyAuthority('sys:dict:save')")
    public R save(@RequestBody SysDictEntity sysDict){
        ValidatorUtils.validateAddEntity(sysDict);
        sysDictService.save(sysDict);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAnyAuthority('sys:dict:update')")
    public R update(@RequestBody SysDictEntity sysDict){
        ValidatorUtils.validateUpdateEntity(sysDict);
        sysDictService.updateById(sysDict);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('sys:dict:delete')")
    public R delete(@RequestBody Long[] ids){
        sysDictService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
