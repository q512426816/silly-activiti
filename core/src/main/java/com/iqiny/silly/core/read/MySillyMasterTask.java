/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.read;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyMasterTask;
import com.iqiny.silly.core.config.SillyConfigUtil;
import org.apache.commons.collections.MapUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class MySillyMasterTask implements SillyMasterTask {

    public static final String TASK_TYPE_ASSIGNEE = SillyConstant.UserTaskType.TASK_TYPE_ASSIGNEE;
    public static final String TASK_TYPE_USER = SillyConstant.UserTaskType.TASK_TYPE_USER;
    public static final String TASK_TYPE_GROUP = SillyConstant.UserTaskType.TASK_TYPE_GROUP;

    /**
     * 业务分类
     */
    private String category;

    /**
     * 业务主表ID
     */
    private String masterId;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务节点ID
     */
    private String nodeKey;

    /**
     * 任务类型 assignee / user / group
     */
    private String taskType;

    /**
     * 其他参数
     */
    private Map<String, Object> params = new LinkedHashMap<>();

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getNodeKey() {
        return nodeKey;
    }

    @Override
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    @Override
    public String getTaskType() {
        return taskType;
    }

    @Override
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public String getMasterId() {
        return masterId;
    }

    @Override
    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }


    public String getUserId() {
        return MapUtils.getString(getParams(), "userId");
    }

    public void setUserId(String userId) {
        getParams().put("userId", userId);
    }

    public String getAssignee() {
        return MapUtils.getString(getParams(), TASK_TYPE_ASSIGNEE);
    }

    public void setAssignee(String assignee) {
        getParams().put(TASK_TYPE_ASSIGNEE, assignee);
    }

    public String getGroupId() {
        return MapUtils.getString(getParams(), "groupId");
    }

    public void setGroupId(String groupId) {
        getParams().put("groupId", groupId);
    }

    public String getChangeFlag() {
        if (StringUtils.isEmpty(MapUtils.getString(getParams(), "changeFlag"))) {
            String userId = getUserId();
            String assignee = getAssignee();
            String currUserId = SillyConfigUtil.getSillyConfig(category).getSillyCurrentUserUtil().currentUserId();
            if (Objects.equals(currUserId, userId) || Objects.equals(currUserId, assignee)) {
                setChangeFlag(SillyConstant.YesOrNo.YES);
            } else {
                setChangeFlag(SillyConstant.YesOrNo.NO);
            }
        }
        return MapUtils.getString(getParams(), "changeFlag");
    }

    public void setChangeFlag(String changeFlag) {
        getParams().put("changeFlag", changeFlag);
    }
}
