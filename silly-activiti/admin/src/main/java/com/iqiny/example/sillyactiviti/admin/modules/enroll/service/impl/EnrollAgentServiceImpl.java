/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.enroll.service.impl;


import com.iqiny.example.sillyactiviti.admin.common.base.BaseService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import com.iqiny.example.sillyactiviti.admin.common.utils.DictUtils;
import com.iqiny.example.sillyactiviti.admin.modules.enroll.dao.EnrollAgentDao;
import com.iqiny.example.sillyactiviti.admin.modules.enroll.entity.EnrollAgentEntity;
import com.iqiny.example.sillyactiviti.admin.modules.enroll.service.EnrollAgentService;
import com.iqiny.example.sillyactiviti.admin.modules.res.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("agentService")
public class EnrollAgentServiceImpl extends BaseService<EnrollAgentDao, EnrollAgentEntity> implements EnrollAgentService {

    @Autowired
    private PersonService personService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return queryPage(params, EnrollAgentEntity.class);
    }

    @Override
    protected void setRecordInfo(List<EnrollAgentEntity> records) {
        for (EnrollAgentEntity entity : records) {
            entity.setPerson(personService.getById(entity.getPersonId()));
            entity.setGradeName(DictUtils.getName(entity.getGrade(), DictUtils.DictType.GENERALIZE_GRADE));
        }
    }

}
