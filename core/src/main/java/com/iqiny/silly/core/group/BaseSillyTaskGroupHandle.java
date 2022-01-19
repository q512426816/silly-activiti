/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;

import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseSillyTaskGroupHandle implements SillyTaskGroupHandle {

    public static final String GROUP_USER_ID_PREFIX = "group_user_id_prefix@@";
    
    protected final Map<String, List<SillyTaskCategoryGroup>> CATEGORY_GROUP_MAP = new ConcurrentHashMap<>();
    protected final Map<String, SillyTaskGroup> GROUP_MAP = new ConcurrentHashMap<>();

    @Override
    public String getNameById(String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return "";
        }
        SillyTaskGroup sillyTaskGroup = GROUP_MAP.get(groupId);
        if (sillyTaskGroup != null) {
            return sillyTaskGroup.getGroupName(groupId);
        }
        for (String key : CATEGORY_GROUP_MAP.keySet()) {
            List<SillyTaskCategoryGroup> list = CATEGORY_GROUP_MAP.get(key);
            for (SillyTaskCategoryGroup categoryGroup : list) {
                if (categoryGroup.belongGroup(groupId)) {
                    return categoryGroup.getGroupName(groupId);
                }
            }
        }
        return null;
    }

    @Override
    public void addSillyTaskGroup(SillyTaskGroup sillyTaskGroup) {
        GROUP_MAP.put(sillyTaskGroup.getGroupId(), sillyTaskGroup);
    }

    @Override
    public void addCategorySillyTaskGroup(SillyTaskCategoryGroup categoryGroup) {
        List<SillyTaskCategoryGroup> list = CATEGORY_GROUP_MAP.computeIfAbsent(categoryGroup.usedCategory(), k -> new ArrayList<>());
        list.add(categoryGroup);
    }

    @Override
    public SillyTaskCategoryGroup getCategoryGroup(String category, String key) {
        List<SillyTaskCategoryGroup> list = CATEGORY_GROUP_MAP.get(category);
        if (list != null) {
            for (SillyTaskCategoryGroup categoryGroup : list) {
                if (Objects.equals(categoryGroup.name(), key)) {
                    return categoryGroup;
                }
            }
        }
        throw new SillyException("业务类型任务分组未找到 " + category + " " + key);
    }

    /**
     * 判断用户是否拥有此groupId 权限
     *
     * @param userId
     * @param groupId
     * @return
     */
    @Override
    public boolean hasGroupId(String userId, String groupId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(groupId)) {
            return false;
        }
        SillyTaskGroup sillyTaskGroup = GROUP_MAP.get(groupId);
        return sillyTaskGroup != null && sillyTaskGroup.hasGroup(userId);
    }

    @Override
    public Map<String, List<SillyTaskCategoryGroup>> allCategoryGroup() {
        return CATEGORY_GROUP_MAP;
    }

    @Override
    public Map<String, SillyTaskGroup> allSillyTaskGroup() {
        return GROUP_MAP;
    }
}
