/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.silly.service;

import com.iqiny.silly.activiti.BaseSillyActivitiEngineService;
import com.iqiny.silly.core.base.SillyMasterTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Activiti 流程引擎服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MySillyActivitiEngineService extends BaseSillyActivitiEngineService {


    @Override
    public <S extends SillyMasterTask> List<S> getDoingMasterTask(String category, String userId) {
        return null;
    }

    @Override
    public <S extends SillyMasterTask> List<S> getHistoryMasterTask(String category, String userId) {
        return null;
    }
}