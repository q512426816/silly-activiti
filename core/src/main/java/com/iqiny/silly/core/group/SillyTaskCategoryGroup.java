/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;

import java.util.List;

public interface SillyTaskCategoryGroup {

    /**
     * groupid:  CATEGORY + SPLIT_STR + 自定义数据
     */
    String SPLIT_STR = "__CEY__";

    String category();
    
    String key();

    /**
     * 自己的数据名开头
     * @return
     */
    String prefixName();

    /**
     * 根据组ID 判断此组ID 是否在此类中的生成
     *
     * @param groupId
     * @return
     */
    boolean belongGroup(String groupId) ;

    /**
     * 根据用户ID + 数据Key 判断此用户是否拥有此 GROUP_ID 权限
     *
     * @param userId
     * @return
     */
    boolean hasGroup(String userId, String key);

    /**
     * 获取此 GROUP_ID
     *
     * @return
     */
    List<String> getGroupIds(String userId);

    /**
     * 获取此 分组名称
     *
     * @return
     */
    String getGroupName();


    String keyCovGroupId(String key);

    String groupIdCovKey(String groupId);

}
