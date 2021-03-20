/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.silly.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iqiny.example.sillyactiviti.admin.common.base.BaseService;
import com.iqiny.example.sillyactiviti.admin.common.silly.dao.MySillyResumeDao;
import com.iqiny.example.sillyactiviti.admin.common.silly.entity.MySillyResume;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.resume.SillyResumeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 流程履历服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MySillyResumeService extends BaseService<MySillyResumeDao, MySillyResume> implements SillyResumeService {

    private final Map<String, String> handleMap = new HashMap<>();


    @Override
    public String makeResumeHandleInfo(String handleType, String nextUserIds, String taskName, String content) {
        if (StringUtils.isNotEmpty(content)) {
            taskName += ("，" + content);
        }
        final String handleName = handleMap.getOrDefault(handleType, "");
        String nowUserName = "当前人A";
        String nowUserOfficeName = "当前人部门";
        StringBuilder nextUserName = new StringBuilder();
        StringBuilder nextUserOfficeName = new StringBuilder();
        if (StringUtils.isEmpty(nextUserIds)) {
            return String.format("【%s】 (%s) %s-%s。", nowUserOfficeName, nowUserName, handleName, taskName);
        }

        String spStr = "、";
        int index = nextUserName.lastIndexOf(spStr);
        if (index > 0) {
            nextUserName.deleteCharAt(index);
        }
        index = nextUserOfficeName.lastIndexOf(spStr);
        if (index > 0) {
            nextUserOfficeName.deleteCharAt(index);
        }

        Object[] arr = new String[]{nowUserOfficeName, nowUserName, handleName, taskName, nextUserOfficeName.toString(), nextUserName.toString()};
        return String.format("【%s】 (%s) %s-%s，下一步由【%s】 (%s)进行处置。", arr);
    }


    @Override
    public void insert(SillyResume sillyResume) {
        if (sillyResume instanceof MySillyResume) {
            MySillyResume resume = (MySillyResume) sillyResume;
            resume.setHandleUserId("1");
            resume.setHandleDeptId("1");
            resume.setHandleDate(new Date());
            resume.preInsert();
            resume.insert();
        } else {
            throw new RuntimeException("流程履历对象类型错误");
        }
    }

    @Override
    public List<? extends SillyResume> selectList(String masterId, String businessType) {
        MySillyResume resume = new MySillyResume();
        resume.setBusinessId(masterId);
        resume.setBusinessType(businessType);
        QueryWrapper<MySillyResume> qw = new QueryWrapper<>();
        qw.setEntity(resume);
        qw.orderByAsc("HANDLE_DATE");
        return new MySillyResume().selectList(qw);
    }

    @Override
    public void init() {
        handleMap.put(SillyConstant.SillyResumeType.PROCESS_TYPE_NEXT, "完成任务");
        handleMap.put(SillyConstant.SillyResumeType.PROCESS_TYPE_BACK, "执行任务驳回");
        handleMap.put(SillyConstant.SillyResumeType.PROCESS_TYPE_CLOSE, "执行流程关闭");
        handleMap.put(SillyConstant.SillyResumeType.PROCESS_TYPE_FLOW, "执行任务流转");
        handleMap.put(SillyConstant.SillyResumeType.PROCESS_TYPE_START, "执行流程启动");
    }

    @Override
    public String usedCategory() {
        return DEFAULT_CATEGORY;
    }
}
