/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;

/**
 * 傻瓜任务组接口，任务处置人
 */
public interface SillyTaskGroup {

    /**
     * 根据用户ID 判断此用户是否拥有此 GROUP_ID 权限
     *
     * @param userId
     * @return
     */
    boolean hasGroup(String userId);

    /**
     * 获取此 GROUP_ID
     *
     * @return
     */
    String getGroupId();

    /**
     * 获取此 分组名称
     *
     * @return
     */
    String getGroupName();

    /**
     * 获取此 分组名称
     *
     * @return
     */
    String getGroupName(String groupId);

}
