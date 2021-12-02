/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service;

import com.iqiny.silly.core.base.SillyCategory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;

import java.util.List;
import java.util.Map;

/**
 * 数据读取的基本服务支持接口
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyReadService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> extends SillyService {

    /**
     * 获取主表数据
     *
     * @param masterId 主表ID
     * @return 主表数据
     */
    M getMaster(String masterId);

    /**
     * 查询节点表数据
     *
     * @param masterId 主表ID
     * @return 节点表数据
     */
    List<N> getNodeList(String masterId);

    /**
     * 查询变量表数据
     *
     * @param masterId 主表ID
     * @param nodeId   节点表ID
     * @return 变量表数据
     */
    List<V> getVariableList(String masterId, String nodeId);

    /**
     * 查询变量表数据
     *
     * @return 变量表数据
     */
    List<V> findVariableList(V where);

    /**
     * 获取当前任务下的变量数据
     *
     * @param taskId
     */
    Map<String, Object> findVariableByTaskId(String taskId);

    /**
     * 查询主表数据 Map
     *
     * @param masterId 主表ID
     * @return 主表数据Map
     */
    Map<String, Object> getMasterMap(String masterId);

    /**
     * 获取节点集合数据
     *
     * @param masterId
     */
    List<N> getAllNodeList(String masterId);

    /**
     * 获取单个节点数据
     *
     * @param masterId
     * @param nodeKey
     */
    N getNode(String masterId, String nodeKey);

    /**
     * 获取进行中的任务列表分页数据
     *
     * @param params
     */
    Object queryDoingPage(Map<String, Object> params);

    /**
     * 获取历史任务分页数据
     *
     * @param params
     */
    Object queryHistoryPage(Map<String, Object> params);

    /**
     * 获取分页数据（不经过流程引擎）
     *
     * @param params
     */
    Object queryPage(Map<String, Object> params);

    /**
     * 获取分页数据（经过流程引擎, 获取对应任务信息）
     *
     * @param params
     */
    Object sourcePage(Map<String, Object> params);

    /**
     * 查询一条数据
     *
     * @param id
     * @return
     */
    Object queryOne(String id);


    /**
     * variableList2Map
     *
     * @param variables
     * @return
     */
    Map<String, Object> variableList2Map(List<V> variables);

}
