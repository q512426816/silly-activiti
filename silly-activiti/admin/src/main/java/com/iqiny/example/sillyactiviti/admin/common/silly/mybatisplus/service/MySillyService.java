package com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.MySillyMaster;
import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.MySillyNode;
import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.MySillyVariable;
import com.iqiny.silly.activiti.EnhanceSillyWriteService;
import com.iqiny.silly.core.config.SillyConfig;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class MySillyService<M extends MySillyMaster<M>, N extends MySillyNode<N, V>, V extends MySillyVariable<V>>
        extends EnhanceSillyWriteService<M, N, V> {

    @Autowired
    private SillyConfig sillyConfig;

    @Override
    public SillyConfig initSillyConfig() {
        return sillyConfig;
    }

    @Override
    public boolean insert(M master) {
        master.preInsert();
        return master.insert();
    }

    @Override
    public boolean updateById(M master) {
        master.preUpdate();
        return master.updateById();
    }

    @Override
    @SuppressWarnings("all")
    public boolean deleteByMasterId(String masterId) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("master_id", masterId);
        // 删除节点表数据
        sillyFactory.newNode().delete(qw);
        // 删除数据表数据
        sillyFactory.newVariable().delete(qw);
        // 删除主表数据
        return sillyFactory.newMaster().deleteById();
    }

    @Override
    public boolean insert(N node) {
        node.preInsert();
        return node.insert();
    }

    @Override
    public boolean update(N node, N where) {
        node.preUpdate();
        QueryWrapper<N> qw = new QueryWrapper<>();
        qw.setEntity(where);
        return node.update(qw);
    }

    @Override
    public boolean insert(V variable) {
        variable.preInsert();
        return variable.insert();
    }

    @Override
    public boolean update(V variable, V where) {
        variable.preUpdate();
        QueryWrapper<V> qw = new QueryWrapper<>();
        qw.setEntity(where);
        return variable.update(qw);
    }
}
