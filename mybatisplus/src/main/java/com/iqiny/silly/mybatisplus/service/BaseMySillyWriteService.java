/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.iqiny.silly.spring.service.EnhanceSillyWriteService;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.baseentity.BaseMySillyVariable;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;
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
        String taskId = node.getTaskId();
        if (StringUtils.isNotEmpty(taskId)) {
            SillyTask task = sillyEngineService.findTaskById(taskId);
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
        for (V v : list) {
            v.preInsert();
        }
        return saveVariableBatch(list, 100);
    }

    protected String variableSqlStatement(String method) {
        return SqlHelper.table(this.variableClass()).getSqlStatement(method);
    }

    protected SqlSession variableSqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(this.variableClass());
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveVariableBatch(Collection<V> entityList, int batchSize) {
        String sqlStatement = this.variableSqlStatement(SqlMethod.INSERT_ONE.getMethod());
        SqlSession batchSqlSession = this.variableSqlSessionBatch();
        Throwable var5 = null;

        try {
            int i = 0;

            for (Iterator var7 = entityList.iterator(); var7.hasNext(); ++i) {
                V anEntityList = (V) var7.next();
                batchSqlSession.insert(sqlStatement, anEntityList);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }

            batchSqlSession.flushStatements();
            return true;
        } catch (Throwable var16) {
            var5 = var16;
            throw var16;
        } finally {
            if (batchSqlSession != null) {
                if (var5 != null) {
                    try {
                        batchSqlSession.close();
                    } catch (Throwable var15) {
                        var5.addSuppressed(var15);
                    }
                } else {
                    batchSqlSession.close();
                }
            }
        }
    }


    @Override
    public boolean update(V variable, V where) {
        variable.preUpdate();
        QueryWrapper<V> qw = new QueryWrapper<>();
        qw.setEntity(where);
        return variable.update(qw);
    }

}
