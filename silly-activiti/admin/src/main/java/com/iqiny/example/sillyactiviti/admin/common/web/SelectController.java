/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.web;

import com.iqiny.example.sillyactiviti.admin.modules.res.service.PersonService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysDictService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 下拉列表查询接口
 */
@RestController
@RequestMapping("/common/select")
public class SelectController {

    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private PersonService personService;

    /**
     * 字典列表
     */
    @RequestMapping("/dict")
    public R dict(@RequestParam Map<String, Object> params) {
        PageUtils page = sysDictService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 字典列表
     */
    @RequestMapping("/person")
    public R person(@RequestParam Map<String, Object> params) {
        PageUtils page = personService.queryPage(params);
        return R.ok().put("page", page);
    }

}
