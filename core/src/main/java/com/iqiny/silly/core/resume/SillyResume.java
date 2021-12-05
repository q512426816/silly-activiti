/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.resume;

import com.iqiny.silly.core.base.core.SillyEntity;

import java.util.Date;

/**
 * 流程履历对象接口
 */
public interface SillyResume extends SillyEntity {

    void setBusinessId(String businessKey);

    void setProcessType(String processTypeNext);

    void setBusinessType(String category);

    void setHandleInfo(String handleInfo);

    void setProcessNodeKey(String taskDefinitionKey);

    void setProcessNodeName(String name);

    void setNextUserId(String nextUserIds);

    void setConsumeTime(Long dueTime);

    void setConsumeDeptTime(Long dueTime);

    void setHandleUserId(String handleUserId);

    void setHandleDeptId(String handleDeptId);

    void setHandleDate(Date handleDate);

}
