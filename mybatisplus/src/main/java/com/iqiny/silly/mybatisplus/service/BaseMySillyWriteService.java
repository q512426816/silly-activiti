/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iqiny.silly.activiti.EnhanceSillyWriteService;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.mybatisplus.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.BaseMySillyVariable;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.MapUtils;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUser(String taskId, String userId, String reason) {
        super.changeUser(taskId, userId, reason);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public M saveData(String taskId, Map<String, Object> saveMap) {
        return super.saveData(taskId, saveMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public M submitData(String taskId, Map<String, Object> saveMap) {
        return super.submitData(taskId, saveMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public M saveData(boolean submit, String taskId, Map<String, Object> saveMap) {
        return super.saveData(submit, taskId, saveMap);
    }


    /**
     * 根据节点配置属性 进行数据保存/提交
     *
     * @param submit
     * @param processProperty
     * @param saveMap
     */
    protected M saveFirstData(boolean submit, SillyProcessProperty<?> processProperty, Map<String, Object> saveMap) {
        SillyProcessNodeProperty<?> nodeProperty = getLastNodeProcessProperty();
        List<V> vs = this.mapToVariables(saveMap, nodeProperty);
        M m = this.makeMasterByVariables(vs);
        m.setProcessKey(processProperty.getLastProcessKey());
        m.setProcessVersion(processProperty.getLastProcessVersion());

        N n = this.makeNodeByVariables(vs);
        if (StringUtils.isEmpty(n.getNodeKey())) {
            n.setNodeKey(processProperty.getFirstNodeKey());
        }
        n.setVariableList(vs);
        n.setParallelFlag(nodeProperty.isParallel() ? SillyConstant.YesOrNo.YES : SillyConstant.YesOrNo.NO);

        if (submit) {
            this.submit(m, n);
        } else {
            this.save(m, n);
        }

        return m;
    }

    @Transactional(rollbackFor = Exception.class)
    public M saveTaskMap(Map<String, Object> saveMap) {
        String taskId = MapUtils.getString(saveMap, "taskId");
        Boolean submit = MapUtils.getBoolean(saveMap, "submit");
        SillyAssert.notEmpty(taskId, "任务ID不可为空");
        SillyAssert.notNull(submit, "提交标记不可为空");
        saveMap.remove("taskId");
        saveMap.remove("submit");
        return saveData(submit, taskId, saveMap);
    }

    @Transactional(rollbackFor = Exception.class)
    public M saveOrNewMap(Map<String, Object> saveMap) {
        String taskId = MapUtils.getString(saveMap, "taskId");
        Boolean submit = MapUtils.getBoolean(saveMap, "submit");
        SillyAssert.notNull(submit, "提交标记不可为空");
        saveMap.remove("taskId");
        saveMap.remove("submit");

        if (StringUtils.isEmpty(taskId)) {
            return saveFirstData(submit, processProperty(), saveMap);
        } else {
            return saveData(submit, taskId, saveMap);
        }
    }

}
