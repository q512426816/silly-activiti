/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 任务组处理类
 */
public interface SillyTaskGroupHandle {

    /**
     * 根据组ID 获取组名称
     *
     * @param groupId
     * @return
     */
    String getNameById(String groupId);

    /**
     * 注册一个 任务组
     *
     * @param sillyTaskGroup
     */
    void addSillyTaskGroup(SillyTaskGroup sillyTaskGroup);

    /**
     * 注册一个 类型任务组
     *
     * @param categoryGroup
     */
    void addCategorySillyTaskGroup(SillyTaskCategoryGroup categoryGroup);

    /**
     * 获取类型任务组
     *
     * @param category 类型
     * @param key      (一个类型下不可重复) 组Key
     * @return
     */
    SillyTaskCategoryGroup getCategoryGroup(String category, String key);

    /**
     * 全部的 任务组
     * @return
     */
    Map<String, SillyTaskGroup> allSillyTaskGroup();

    /**
     * 全部的分类任务组
     * @return
     */
    Map<String, List<SillyTaskCategoryGroup>> allCategoryGroup();

    /**
     * 获取此用户拥有的全部 组ID
     *
     * @param category
     * @param userId
     * @return
     */
    Set<String> getAllGroupId(String category, String userId);

    /**
     * 判断此用户是否拥有此 groupId 权限
     *
     * @param userId
     * @param groupId
     * @return
     */
    boolean hasGroupId(String userId, String groupId);
    
}
