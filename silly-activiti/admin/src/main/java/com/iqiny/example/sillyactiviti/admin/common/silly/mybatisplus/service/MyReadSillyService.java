package com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.service;

import com.iqiny.silly.mybatisplus.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.BaseMySillyVariable;
import com.iqiny.silly.mybatisplus.service.BaseMySillyReadService;

/**
 * 本业务模块读取服务抽象类
 *
 * @param <M>
 * @param <N>
 * @param <V>
 */
public abstract class MyReadSillyService<M extends BaseMySillyMaster<M>, N extends BaseMySillyNode<N, V>, V extends BaseMySillyVariable<V>>
        extends BaseMySillyReadService<M, N, V> {


}
