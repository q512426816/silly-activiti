/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.web;

import com.iqiny.example.sillyactiviti.admin.common.silly.service.MySillyActivitiEngineService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 页面跳转 通用路由
 */
@Controller
public class RoutePageController {

    @Autowired
    private MySillyActivitiEngineService service;

    /**
     * 模块主页面跳转
     *
     * @param system
     * @param module
     * @param page
     * @return
     */
    @RequestMapping(value = "{system}/{module}/{page}.page", method = RequestMethod.GET)
    public String index(@PathVariable("system") String system, @PathVariable("module") String module, @PathVariable("page") String page) {
        return system + "/" + module + "/" + page;
    }

    /**
     * 模块表单处置页面跳转
     *
     * @param system
     * @param module
     * @param taskId
     * @return
     */
    @RequestMapping(value = "{system}/{module}/{taskId}.task", method = RequestMethod.GET)
    public String taskForm(@PathVariable("system") String system, @PathVariable("module") String module, @PathVariable("taskId") String taskId) {
        final Task task = service.findTaskById(taskId);
        final String version = service.getVersion(task.getProcessInstanceId());
        return system + "/" + module + "/" + version + "/form-" + task.getTaskDefinitionKey().substring(6);
    }

    /**
     * 模块表单处置页面跳转
     *
     * @param system
     * @param module
     * @return
     */
    @RequestMapping(value = "{system}/{module}/{version}/{page}.task", method = RequestMethod.GET)
    public String taskForm(@PathVariable("system") String system, @PathVariable("module") String module, @PathVariable("version") String version, @PathVariable("page") String page) {
        return system + "/" + module + "/" + version + "/" + page;
    }

    /**
     * 模块详情页面跳转
     *
     * @param system
     * @param module
     * @param version
     * @return
     */
    @RequestMapping(value = "{system}/{module}/{version}.info", method = RequestMethod.GET)
    public String detailForm(@PathVariable("system") String system, @PathVariable("module") String module, @PathVariable("version") String version) {
        return system + "/" + module + "/" + version + "/detail";
    }
    
}
