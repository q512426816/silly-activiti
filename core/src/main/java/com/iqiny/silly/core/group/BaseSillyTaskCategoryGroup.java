/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;


import com.iqiny.silly.common.util.StringUtils;

public abstract class BaseSillyTaskCategoryGroup implements SillyTaskCategoryGroup {

    @Override
    public String prefixName() {
        return category() + SPLIT_STR + key();
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

}
