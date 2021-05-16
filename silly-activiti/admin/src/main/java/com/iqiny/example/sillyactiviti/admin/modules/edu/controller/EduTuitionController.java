/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.edu.controller;

import com.iqiny.example.sillyactiviti.admin.modules.edu.entity.EduTuitionEntity;
import com.iqiny.example.sillyactiviti.admin.modules.edu.service.EduTuitionService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.common.utils.R;
import com.iqiny.example.sillyactiviti.common.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 缴费信息
 *
 * @author qiny
 * @email qy-1994-2008@163.com
 * @date 2020-10-25 20:05:22
 */
@RestController
@RequestMapping("edu/tuition")
public class EduTuitionController {
    @Autowired
    private EduTuitionService eduTuitionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('edu:tuition:list')")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = eduTuitionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('edu:tuition:info')")
    public R info(@PathVariable("id") Long id){
        EduTuitionEntity entity = eduTuitionService.getById(id);
        return R.ok().put("entity", entity);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @PreAuthorize("hasAnyAuthority('edu:tuition:save')")
    public R save(@RequestBody EduTuitionEntity eduTuition){
        ValidatorUtils.validateAddEntity(eduTuition);
        eduTuitionService.save(eduTuition);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAnyAuthority('edu:tuition:update')")
    public R update(@RequestBody EduTuitionEntity eduTuition){
        ValidatorUtils.validateUpdateEntity(eduTuition);
        eduTuitionService.updateById(eduTuition);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('edu:tuition:delete')")
    public R delete(@RequestBody Long[] ids){
        eduTuitionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
