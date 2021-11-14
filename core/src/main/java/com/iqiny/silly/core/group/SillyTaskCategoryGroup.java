/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;

import com.iqiny.silly.core.base.SillyCategory;

import java.util.List;

public interface SillyTaskCategoryGroup extends SillyCategory {

    /**
     * groupid:  CATEGORY + SPLIT_STR + 自定义数据
     */
    String SPLIT_STR = "__CEY__";

    /**
     * 分组名称
     */
    String name();

    /**
     * 自己的数据名开头
     */
    String prefixName();

    /**
     * 根据组ID 判断此组ID 是否在此类中的生成
     */
    boolean belongGroup(String groupId);

    /**
     * 根据用户ID + 数据Key 判断此用户是否拥有此 GROUP_ID 权限
     */
    boolean hasGroup(String userId, String key);

    /**
     * 获取此 GROUP_ID
     */
    List<String> getGroupIds(String userId);

    /**
     * 获取此 分组名称
     */
    String getGroupName();

    /**
     * 获取此 分组名称
     */
    String getGroupName(String groupId);

    /**
     * KEY 转 GROUP_ID
     */
    String keyCovGroupId(String key);

    /**
     * GROUP_ID 转 KEY
     */
    String groupIdCovKey(String groupId);

}
