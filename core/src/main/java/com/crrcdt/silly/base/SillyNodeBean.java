package com.crrcdt.silly.base;

import com.crrcdt.silly.base.core.SillyNode;
import com.crrcdt.silly.base.core.SillyVariable;

import java.util.List;

/**
 * 傻瓜工作流 流程节点 Bean （对应每个节点的操作人）
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyNodeBean<N extends SillyNode<V>, V extends SillyVariable> {

    N getNode();

    void setNode(N node);

    List<V> getVariableList();

    void setVariableList(List<V> variableList);
}
