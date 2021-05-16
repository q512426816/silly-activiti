/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.res.service.impl;


import com.iqiny.example.sillyactiviti.admin.common.base.BaseService;
import com.iqiny.example.sillyactiviti.admin.common.utils.DictUtils;
import com.iqiny.example.sillyactiviti.admin.modules.res.dao.PersonDao;
import com.iqiny.example.sillyactiviti.admin.modules.res.entity.PersonEntity;
import com.iqiny.example.sillyactiviti.admin.modules.res.service.PersonService;
import com.iqiny.example.sillyactiviti.admin.common.base.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PersonServiceImpl extends BaseService<PersonDao, PersonEntity> implements PersonService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        return queryPage(params, PersonEntity.class);
    }

    @Override
    protected void setRecordInfo(List<PersonEntity> records) {
        for (PersonEntity entity : records) {
            entity.setTypeName(DictUtils.getName(entity.getType(), DictUtils.DictType.PERSON_TYPE));
            entity.setGenderName(DictUtils.getName(entity.getGender(), DictUtils.DictType.GENDER));
        }
    }
}
