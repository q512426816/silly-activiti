package com.iqiny.example.sillyactiviti.admin.modules.ncr.service.impl;

import com.iqiny.example.sillyactiviti.admin.common.base.BaseService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.dao.NcrMasterDao;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrMaster;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.service.NcrMasterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NcrMasterServiceImpl extends BaseService<NcrMasterDao, NcrMaster> implements NcrMasterService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return queryPage(params, NcrMaster.class);
    }

    @Override
    protected void setRecordInfo(List<NcrMaster> records) {

    }
}