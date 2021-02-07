package com.iqiny.example.sillyactiviti.admin.modules.res.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iqiny.example.sillyactiviti.admin.modules.res.entity.PersonEntity;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;

import java.util.Map;

/**
 * 客户信息
 *
 * @author qiny
 * @email qy-1994-2008@163.com
 * @date 2020-10-11 14:46:35
 */
public interface PersonService extends IService<PersonEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

