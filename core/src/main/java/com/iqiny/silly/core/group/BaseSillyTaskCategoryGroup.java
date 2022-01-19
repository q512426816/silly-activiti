/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;


import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.SillyConfigUtil;
import com.iqiny.silly.core.config.SillyCurrentUserUtil;
import com.iqiny.silly.core.service.SillyReadService;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public abstract class BaseSillyTaskCategoryGroup implements SillyTaskCategoryGroup {

    @Override
    public String prefixName() {
        return usedCategory() + SPLIT_STR + name();
    }

    @Override
    public boolean belongGroup(String groupId) {
        return groupId.startsWith(prefixName());
    }

    @Override
    public String keyCovGroupId(String key) {
        return prefixName() + key;
    }

    @Override
    public String groupIdCovKey(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return null;
        }
        if (groupId.startsWith(prefixName())) {
            return groupId.substring(prefixName().length());
        }
        return null;
    }

    @Override
    public boolean hasGroup(String userId, String key) {
        throw new SillyException("未实现此方法 hasGroup");
    }

    @Override
    public List<String> getGroupIds(String userId) {
        List<String> groupIds = new ArrayList<>();
        SillyCategoryConfig sillyConfig = SillyConfigUtil.getSillyConfig(usedCategory());
        SillyVariable variable = sillyConfig.getSillyFactory().newVariable();
        variable.setVariableText(userId);
        variable.setVariableName(groupUserVariableName());
        variable.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        SillyReadService readService = sillyConfig.getSillyReadService();
        List<? extends SillyVariable> list = readService.findVariableList(variable);
        for (SillyVariable record : list) {
            groupIds.add(keyCovGroupId(record.getMasterId()));
        }
        return groupIds;
    }

    @Override
    public String getGroupName(String groupId) {
        String masterId = groupIdCovKey(groupId);
        SillyCategoryConfig sillyConfig = SillyConfigUtil.getSillyConfig(usedCategory());
        SillyVariable variable = sillyConfig.getSillyFactory().newVariable();
        variable.setMasterId(masterId);
        variable.setVariableName(groupUserVariableName());
        variable.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        SillyReadService readService = sillyConfig.getSillyReadService();
        List<? extends SillyVariable> list = readService.findVariableList(variable);
        SillyCurrentUserUtil userUtil = sillyConfig.getSillyCurrentUserUtil();
        StringJoiner sj = new StringJoiner(SillyConstant.ARRAY_SPLIT_STR);
        for (SillyVariable var : list) {
            sj.add(userUtil.userIdToName(var.getVariableText()));
        }
        return "[" + getGroupName() + ":" + sj + "]";
    }

    @Override
    public String name() {
        return groupUserVariableName();
    }

    /**
     * 获取 存储此 group 对应的用户信息 variable 的 name 值
     */
    protected abstract String groupUserVariableName();


}
