package com.crrcdt.silly.service;

import com.crrcdt.silly.base.Initializable;
import com.crrcdt.silly.base.core.SillyMaster;
import com.crrcdt.silly.base.core.SillyNode;
import com.crrcdt.silly.base.core.SillyVariable;

/**
 * 数据写入的基本服务操作接口
 *
 * @author QINY
 * @since 1.0
 */
public interface SillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> extends Initializable {

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

    /**
     * 更新变量表数据
     *
     * @param variable 变量对象（结果）
     * @param where    变量对象（范围）
     * @return 是否成功
     */
    boolean update(V variable, V where);

}
