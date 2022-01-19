/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;

import com.iqiny.silly.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 傻瓜任务组接口，任务处置人
 */
public class DefaultSillyTaskGroupHandle extends BaseSillyTaskGroupHandle {

    private static final Log log = LogFactory.getLog(DefaultSillyTaskGroupHandle.class);

    /**
     * 根据 用户ID 获取用户拥有的全部 groupId
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> getAllGroupId(String category, String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }

        Set<String> set = new LinkedHashSet<>();

        Set<String> keys = GROUP_MAP.keySet();
        for (String key : keys) {
            loadGroupKey(userId, set, GROUP_MAP.get(key));
        }

        loadCategoryGroupKey(userId, set, CATEGORY_GROUP_MAP.get(category));
        return set;
    }

    private void loadGroupKey(String userId, Set<String> set, SillyTaskGroup sillyTaskGroup) {
        if (sillyTaskGroup != null) {
            boolean hasFlag = sillyTaskGroup.hasGroup(userId);
            if (hasFlag) {
                int size = set.size();
                set.add(sillyTaskGroup.getGroupId());
                if (size != set.size() - 1) {
                    log.warn("存在重复的 groupId 值！！！" + sillyTaskGroup.getGroupId());
                }
            }
        }
    }

    private void loadCategoryGroupKey(String userId, Set<String> set, List<SillyTaskCategoryGroup> categoryGroups) {
        if (categoryGroups == null) {
            return;
        }

        for (SillyTaskCategoryGroup categoryGroup : categoryGroups) {
            List<String> groupIds = categoryGroup.getGroupIds(userId);
            if (groupIds != null && !groupIds.isEmpty()) {
                set.addAll(groupIds);
            }
        }
    }

}
