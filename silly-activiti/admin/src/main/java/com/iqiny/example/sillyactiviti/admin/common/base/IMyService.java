package com.iqiny.example.sillyactiviti.admin.common.base;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface IMyService<T> extends IService<T> {

    PageUtils queryPage(Map<String, Object> params);
    
}
