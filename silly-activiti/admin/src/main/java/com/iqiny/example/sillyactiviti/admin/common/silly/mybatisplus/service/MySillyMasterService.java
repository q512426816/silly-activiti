package com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;

import java.util.Map;

public interface MySillyMasterService<T> extends IService<T> {

    PageUtils queryPage(Map<String, Object> params);
    
}
