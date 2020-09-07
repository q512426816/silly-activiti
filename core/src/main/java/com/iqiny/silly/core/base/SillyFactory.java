package com.iqiny.silly.core.base;

import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;
import com.iqiny.silly.common.util.CurrentUserUtil;

/**
 * 傻瓜工厂 创建 相同一族的傻瓜
 *
 * @param <M>
 * @param <N>
 * @param <V>
 * @author QINY
 * @since 1.0
 */
public interface SillyFactory<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> {

    /**
     * 创建 主对象
     *
     * @return 主对象
     */
    M newMaster();

    /**
     * 创建 节点对象
     *
     * @return 节点对象
     */
    N newNode();

    /**
     * 创建 变量对象
     *
     * @return 变量对象
     */
    V newVariable();

    /**
     * 获取读取服务对象
     *
     * @return 读取服务对象
     */
    SillyReadService<M, N, V> readService();

    /**
     * 获取写入服务对象
     *
     * @return 写入服务对象
     */
    SillyWriteService<M, N, V> writeService();

    /**
     * 创建任务数据对象
     *
     * @return 任务数据对象
     */
    SillyTaskData<N, V> newSillyTaskData();

    /**
     * 当前人Id获取 工具
     * @return 当前人Id工具
     */
    CurrentUserUtil currentUserUtil();

}
