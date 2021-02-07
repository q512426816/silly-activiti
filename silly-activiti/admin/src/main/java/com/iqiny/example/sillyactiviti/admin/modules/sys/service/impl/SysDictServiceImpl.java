package com.iqiny.example.sillyactiviti.admin.modules.sys.service.impl;


import com.iqiny.example.sillyactiviti.admin.common.base.BaseService;
import com.iqiny.example.sillyactiviti.admin.modules.sys.dao.SysDictDao;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysDictEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysDictService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysDictServiceImpl extends BaseService<SysDictDao, SysDictEntity> implements SysDictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return queryPage(params, SysDictEntity.class);
    }
}
