package com.iqiny.example.sillyactiviti.admin.common.silly.web;

import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.admin.common.silly.service.ActProcessService;
import com.iqiny.example.sillyactiviti.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * silly
 */
@Controller
@RequestMapping("/silly")
public class SillyActivitiController {

    @Autowired
    private ActProcessService actProcessService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('silly:deploy:deploy')")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = actProcessService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 流程部署
     */
    @RequestMapping("/deploy")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('silly:deploy:deploy')")
    public R deploy(String category, MultipartFile file) {
        actProcessService.deploy(category, file);
        String fileName = file.getOriginalFilename();
        return R.ok(fileName + " 部署成功！");
    }

    @RequestMapping(value = "/resource/xml/{processDefinitionId}")
    public void resourceXml(@PathVariable("processDefinitionId") String processDefinitionId, HttpServletResponse response) {
        InputStream resourceAsStream = actProcessService.resourceRead(processDefinitionId, ActProcessService.XML);
        if (resourceAsStream == null) {
            return;
        }

        byte[] b = new byte[1024];
        int len;
        try (OutputStream out = response.getOutputStream()) {
            while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/resource/image/{processDefinitionId}")
    public void resourceImage(@PathVariable("processDefinitionId") String processDefinitionId, HttpServletResponse response) {
        InputStream resourceAsStream = actProcessService.resourceRead(processDefinitionId, ActProcessService.IMAGE);
        if (resourceAsStream == null) {
            return;
        }
        response.setContentType("image/svg+xml");
        byte[] b = new byte[1024];
        int len;
        try (OutputStream out = response.getOutputStream()) {
            while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
