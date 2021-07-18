/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.enroll.controller;

import com.iqiny.example.sillyactiviti.admin.modules.enroll.entity.EnrollAgentEntity;
import com.iqiny.example.sillyactiviti.admin.modules.enroll.service.EnrollAgentService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.common.utils.R;
import com.iqiny.example.sillyactiviti.common.validator.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 招生员信息
 *
 * @author xy
 * @email sog_xy@163.com
 * @date 2020-10-25 13:01:02
 */
@RestController
@RequestMapping("enroll/agent")
public class EnrollAgentController {
    @Autowired
    private EnrollAgentService agentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @PreAuthorize("hasAnyAuthority('enroll:agent:list')")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = agentService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('enroll:agent:info')")
    public R info(@PathVariable("id") Long id) {
        EnrollAgentEntity entity = agentService.getById(id);

        return R.ok().put("entity", entity);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @PreAuthorize("hasAnyAuthority('enroll:agent:save')")
    public R save(@RequestBody EnrollAgentEntity agent) {
        ValidatorUtils.validateAddEntity(agent);
        agentService.save(agent);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @PreAuthorize("hasAnyAuthority('enroll:agent:update')")
    public R update(@RequestBody EnrollAgentEntity agent) {
        ValidatorUtils.validateUpdateEntity(agent);
        agentService.updateById(agent);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('enroll:agent:delete')")
    public R delete(@RequestBody Long[] ids) {
        agentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
