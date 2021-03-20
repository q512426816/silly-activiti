/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service;

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
     * 查询主表数据 Map
     *
     * @param masterId 主表ID
     * @return 主表数据Map
     */
    Map<String, Object> getMasterMap(String masterId);

}
