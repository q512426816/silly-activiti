/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.res.controller;

import com.iqiny.example.sillyactiviti.admin.modules.res.entity.PersonEntity;
import com.iqiny.example.sillyactiviti.admin.modules.res.service.PersonService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.common.utils.R;
import com.iqiny.example.sillyactiviti.common.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 客户信息
 *
 * @author qiny
 * @email qy-1994-2008@163.com
 * @date 2020-10-11 14:46:35
 */
@RestController
@RequestMapping("res/person")
public class PersonController {
    private final PersonService customerService;

    @Autowired
    public PersonController(PersonService customerService) {
        this.customerService = customerService;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('res:person:list')")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = customerService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('res:person:info')")
    public R info(@PathVariable("id") Long id) {
        PersonEntity entity = customerService.getById(id);

        return R.ok().put("entity", entity);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @PreAuthorize("hasAnyAuthority('res:person:save')")
    public R save(@RequestBody PersonEntity customer) {
        if (customer == null) {
            return R.error("参数为null");
        }
        if (StringUtils.isEmpty(customer.getId())) {
            ValidatorUtils.validateAddEntity(customer);
            customerService.save(customer);
        } else {
            ValidatorUtils.validateUpdateEntity(customer);
            customerService.updateById(customer);
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAnyAuthority('res:person:update')")
    public R update(@RequestBody PersonEntity customer) {
        ValidatorUtils.validateUpdateEntity(customer);
        customerService.updateById(customer);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('res:person:delete')")
    public R delete(@RequestBody Long[] ids) {
        customerService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
