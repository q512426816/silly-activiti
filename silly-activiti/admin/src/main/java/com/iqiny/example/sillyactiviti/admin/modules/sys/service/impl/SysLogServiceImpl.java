package com.iqiny.example.sillyactiviti.admin.modules.sys.service.impl;


import com.iqiny.example.sillyactiviti.admin.common.annotation.XhAsync;
import com.iqiny.example.sillyactiviti.admin.common.base.BaseService;
import com.iqiny.example.sillyactiviti.admin.common.utils.DictUtils;
import com.iqiny.example.sillyactiviti.admin.modules.sys.dao.SysLogDao;
import com.iqiny.example.sillyactiviti.admin.modules.sys.entity.SysLogEntity;
import com.iqiny.example.sillyactiviti.admin.modules.sys.service.SysLogService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysLogServiceImpl extends BaseService<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return queryPage(params, SysLogEntity.class);
    }

    @Override
    protected void setRecordInfo(List<SysLogEntity> records) {
        for (SysLogEntity entity : records) {
            entity.setTypeName(DictUtils.getName(entity.getType(), DictUtils.DictType.LOG_TYPE));
        }
    }

    @Override
    @XhAsync
    public void asyncSaveLog(SysLogEntity logEntity, Object handler, Exception ex, Object o) {
        save(logEntity);
    }
}
