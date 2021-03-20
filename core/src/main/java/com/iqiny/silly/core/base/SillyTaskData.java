/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.base;

import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;

import java.util.List;

/**
 * 傻瓜 流程任务数据 （对应每个节点任务的操作人）
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyTaskData<N extends SillyNode<V>, V extends SillyVariable> extends SillyEntity {

    N getNode();

    void setNode(N node);

    List<V> getVariableList();

    void setVariableList(List<V> variableList);
}
