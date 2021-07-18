/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iqiny.silly.activiti.EnhanceSillyWriteService;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.mybatisplus.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.BaseMySillyVariable;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

public abstract class BaseMySillyWriteService<M extends BaseMySillyMaster<M>, N extends BaseMySillyNode<N, V>, V extends BaseMySillyVariable<V>>
        extends EnhanceSillyWriteService<M, N, V> {

    protected SillyReadService<M, N, V> sillyReadService;

    @Override
    protected void otherInit() {
        sillyReadService = getSillyConfig().getSillyReadService(usedCategory());
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
        return sillyFactory.newMaster().deleteById(masterId);
    }

    @Override
    public boolean insert(N node) {
        node.preInsert();
        String taskId = node.getTaskId();
        if (StringUtils.isNotEmpty(taskId)) {
            Task task = sillyEngineService.findTaskById(taskId);
            node.setNodeName(task.getName());
            if (StringUtils.isEmpty(node.getNodeKey())) {
                node.setNodeKey(task.getTaskDefinitionKey());
            }
        }
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
    public boolean batchInsert(List<V> list) {
        boolean flag = false;
        for (V v : list) {
            v.preInsert();
            flag = v.insert();
            if (!flag) {
                return false;
            }
        }
        return flag;
    }

    @Override
    public boolean update(V variable, V where) {
        variable.preUpdate();
        QueryWrapper<V> qw = new QueryWrapper<>();
        qw.setEntity(where);
        return variable.update(qw);
    }

}
