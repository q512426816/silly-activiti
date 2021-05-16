/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.edu.service.impl;


import com.iqiny.example.sillyactiviti.admin.common.base.BaseService;
import com.iqiny.example.sillyactiviti.admin.common.utils.DictUtils;
import com.iqiny.example.sillyactiviti.admin.modules.edu.dao.EduTuitionDao;
import com.iqiny.example.sillyactiviti.admin.modules.edu.entity.EduTuitionEntity;
import com.iqiny.example.sillyactiviti.admin.modules.edu.service.EduTuitionService;
import com.iqiny.example.sillyactiviti.admin.modules.res.service.PersonService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EduTuitionServiceImpl extends BaseService<EduTuitionDao, EduTuitionEntity> implements EduTuitionService {

    @Autowired
    private PersonService personService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return queryPage(params, EduTuitionEntity.class);
    }

    @Override
    protected void setRecordInfo(List<EduTuitionEntity> records) {
        for (EduTuitionEntity entity : records) {
            entity.setPerson(personService.getById(entity.getPersonId()));
            entity.setChannelName(DictUtils.getName(entity.getChannel(), DictUtils.DictType.EDU_CHANNEL));
            entity.setHasInsuranceName(DictUtils.getName(entity.getHasInsurance(), DictUtils.DictType.YES_NO));
            entity.setStatusName(DictUtils.getName(entity.getStatus(), DictUtils.DictType.PAY_STATUS));
        }
    }
}
