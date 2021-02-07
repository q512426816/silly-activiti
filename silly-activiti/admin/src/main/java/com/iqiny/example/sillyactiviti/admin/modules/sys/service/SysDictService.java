package com.iqiny.example.sillyactiviti.admin.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysDictEntity;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;

import java.util.Map;

/**
 * 数据字典表
 *
 * @author xy
 * @email sog_xy@163.com
 * @date 2020-10-26 23:17:14
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

