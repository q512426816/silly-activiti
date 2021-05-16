/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.domain.controller;

import com.iqiny.example.sillyactiviti.admin.modules.domain.Server;
import com.iqiny.example.sillyactiviti.common.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("domain/service")
public class DomainController {

    /**
     * 信息
     */
    @RequestMapping("/info")
    public R info() {
        Server server = new Server();
        try {
            server.copyTo();
        } catch (Exception e) {
            return R.error().put("entity", server);
        }
        return R.ok().put("entity", server);
    }

}
