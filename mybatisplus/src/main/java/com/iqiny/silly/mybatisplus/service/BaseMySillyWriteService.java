package com.iqiny.silly.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iqiny.silly.mybatisplus.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.BaseMySillyVariable;
import com.iqiny.silly.activiti.EnhanceSillyWriteService;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.StringUtils;
import org.activiti.engine.task.Task;

import java.util.List;

public abstract class BaseMySillyWriteService<M extends BaseMySillyMaster<M>, N extends BaseMySillyNode<N, V>, V extends BaseMySillyVariable<V>>
        extends EnhanceSillyWriteService<M, N, V> {

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

    /**
     * 人员流转
     */
    public void changeUser(String taskId, String userId, String reason) {
        final Task task = sillyEngineService.findTaskById(taskId);
        final String masterId = sillyEngineService.getBusinessKey(task.getProcessInstanceId());
        // 变更人员
        sillyEngineService.changeUser(taskId, userId);
        // 获取任务处置人员
        final List<String> userIds = sillyEngineService.getTaskUserIds(task);
        final String joinUserIds = StringUtils.join(userIds, ",");
        // 记录履历
        String handleInfo = this.sillyResumeService.makeResumeHandleInfo(SillyConstant.SillyResumeType.PROCESS_TYPE_FLOW, joinUserIds, task.getName(), reason);
        super.doSaveProcessResume(masterId, handleInfo, SillyConstant.SillyResumeType.PROCESS_TYPE_FLOW, task.getTaskDefinitionKey(), task.getName(), joinUserIds, null);
        // 更新主表信息
        final M master = sillyFactory.newMaster();
        master.setId(masterId);
        master.setHandleUserName(userIdsToName(joinUserIds));
        updateById(master);
    }

}
