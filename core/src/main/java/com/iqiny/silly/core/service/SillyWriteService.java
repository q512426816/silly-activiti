/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service;

import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据写入的基本服务操作接口
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> extends SillyService {


    /**
     * 保存/新增数据
     *
     * @param saveMap
     * @return
     */
    M saveOrNewMap(Map<String, Object> saveMap);

    /**
     * 保存数据
     *
     * @param saveMap
     * @return
     */
    M saveTaskMap(Map<String, Object> saveMap);

    /**
     * 变更任务处置人
     * @param taskId
     * @param userId
     * @param reason
     */
    void changeUser(String taskId, String userId, String reason);

    /**
     * 任务节点驳回
     * @param taskId 当前任务 id
     * @param nodeKey 驳回到的节点 KEY
     * @param reason 原因
     */
    void reject(String taskId, String nodeKey, String reason, String userId);

    /**
     * 删除主表及关闭工作流
     *
     * @param masterId
     * @param processInstanceId
     */
    void delete(String masterId, String processInstanceId);

    /**
     * 强制结束流程
     *
     * @param processInstanceId
     */
    void forceEndProcess(String processInstanceId);

    /**
     * 新增主表数据
     *
     * @param master 主对象
     * @return 是否成功
     */
    boolean insert(M master);

    /**
     * 更新主表数据
     *
     * @param master 主对象
     * @return 是否成功
     */
    boolean updateById(M master);

    /**
     * 通过主表ID 删除数据
     *
     * @param masterId 主表ID
     * @return 是否成功
     */
    boolean deleteByMasterId(String masterId);

    /**
     * 新增节点表数据
     *
     * @param node 节点对象
     * @return 是否成功
     */
    boolean insert(N node);

    /**
     * 更新节点表数据
     *
     * @param node  节点对象（结果）
     * @param where 节点对象（范围）
     * @return 是否成功
     */
    boolean update(N node, N where);

    /**
     * 新增变量表数据
     *
     * @param variable 变量对象
     * @return 是否成功
     */
    boolean insert(V variable);

    boolean batchInsert(List<V> variable);

    /**
     * 更新变量表数据
     *
     * @param variable 变量对象（结果）
     * @param where    变量对象（范围）
     * @return 是否成功
     */
    boolean update(V variable, V where);

    boolean saveVariableBatch(Collection<V> variables, int batchSize);

}
