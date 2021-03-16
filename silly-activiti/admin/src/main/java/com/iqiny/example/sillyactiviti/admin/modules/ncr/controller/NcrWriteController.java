package com.iqiny.example.sillyactiviti.admin.modules.ncr.controller;

import com.iqiny.example.sillyactiviti.admin.modules.ncr.service.NcrSillyService;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.silly.NcrSillySaveData;
import com.iqiny.example.sillyactiviti.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/silly/ncr/write")
public class NcrWriteController {

    @Autowired
    private NcrSillyService service;

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody NcrSillySaveData dataMap) {
        service.submit(dataMap.getMaster(), dataMap.getNode());
        return R.ok();
    }

}