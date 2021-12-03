/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.service.impl;

import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyVariable;
import com.iqiny.silly.mybatisplus.service.BaseMySillyWriteService;

public class DefaultMySillyWriteService<M extends BaseMySillyMaster<M>, N extends BaseMySillyNode<N, V>, V extends BaseMySillyVariable<V>>
        extends BaseMySillyWriteService<M, N, V> {

    private String category;

    @Override
    public String usedCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
