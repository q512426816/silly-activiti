/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.service;

import com.iqiny.silly.mybatisplus.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.BaseMySillyVariable;
import com.iqiny.silly.mybatisplus.service.BaseMySillyWriteService;

public abstract class MyWriteSillyService<M extends BaseMySillyMaster<M>, N extends BaseMySillyNode<N, V>, V extends BaseMySillyVariable<V>>
        extends BaseMySillyWriteService<M, N, V> {

    
}
