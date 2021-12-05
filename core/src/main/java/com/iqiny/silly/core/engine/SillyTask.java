/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.engine;

/**
 * 流程任务对象
 */
public interface SillyTask {

    String getId();

    String getExecutionId();
    
    String getProcessInstanceId();

    String getName();

    String getTaskDefinitionKey();

    String getAssignee();

    Object getTask();
    
}
