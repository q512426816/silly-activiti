package com.iqiny.silly.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iqiny.silly.activiti.EnhanceSillyReadService;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.mybatisplus.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.BaseMySillyVariable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseMySillyReadService<M extends BaseMySillyMaster<M>, N extends BaseMySillyNode<N, V>, V extends BaseMySillyVariable<V>>
        extends EnhanceSillyReadService<M, N, V> {

    public static final String VARIABLE_QUERY_START = "variableQuery[";
    public static final String VARIABLE_QUERY_END = "]";

    @Override
    public M getMaster(String masterId) {
        return sillyFactory.newMaster().selectById(masterId);
    }

    @Override
    public List<N> getNodeList(String masterId) {
        final N n = sillyFactory.newNode();
        n.setMasterId(masterId);
        n.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        QueryWrapper<N> qw = new QueryWrapper<>();
        qw.setEntity(n);
        qw.orderByAsc("CREATE_DATE");
        return sillyFactory.newNode().selectList(qw);
    }

    @Override
    public List<V> getVariableList(String masterId, String nodeId) {
        final V v = sillyFactory.newVariable();
        v.setMasterId(masterId);
        v.setNodeId(nodeId);
        v.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        QueryWrapper<V> qw = new QueryWrapper<>();
        qw.setEntity(v);
        qw.orderByAsc("CREATE_DATE");
        return sillyFactory.newVariable().selectList(qw);
    }

    @Override
    public List<V> findVariableList(V variable) {
        QueryWrapper<V> qw = new QueryWrapper<>();
        qw.setEntity(variable);
        qw.orderByAsc("CREATE_DATE");
        return sillyFactory.newVariable().selectList(qw);
    }

    protected Set<String> findMasterIdByMap(Map<String, Object> params) {
        return findMasterIdByMap(null, params);
    }

    protected Set<String> findMasterIdByMap(List<String> masterIdList, Map<String, Object> params) {
        if (params == null) {
            return new LinkedHashSet<>();
        }

        final Set<String> keys = params.keySet();
        // 已加载数据标识
        boolean loadData = (masterIdList != null);
        // 变量表查询返回的主表ID集合
        Set<String> masterIds = new LinkedHashSet<>();
        if (masterIdList != null) {
            masterIds.addAll(masterIdList);
        }
        for (String key : keys) {
            // 获取以 variableQuery[ 开头， ] 结尾的参数 查询 变量表数据
            if (StringUtils.isEmpty(key) || !key.startsWith(VARIABLE_QUERY_START) || !key.endsWith(VARIABLE_QUERY_END)) {
                continue;
            }
            final String name = key.substring(VARIABLE_QUERY_START.length(), key.lastIndexOf(VARIABLE_QUERY_END));
            final String value = (String) params.get(key);
            if (StringUtils.isNotEmpty(value)) {
                QueryWrapper<V> qw = new QueryWrapper<>();
                qw.select("MASTER_ID");
                qw.eq("STATUS", SillyConstant.ActivitiNode.STATUS_CURRENT);
                qw.and(i -> i.eq("variable_Name", name).like("variable_Text", value));
                final List<V> vs = sillyFactory.newVariable().selectList(qw);
                Set<String> oneMasterIds = new LinkedHashSet<>();
                for (V v : vs) {
                    oneMasterIds.add(v.getMasterId());
                }
                if (loadData) {
                    masterIds.retainAll(oneMasterIds);
                } else {
                    // 仅第一次查询返回值加入，后续参数返回数据都将进行删除操作
                    masterIds.addAll(oneMasterIds);
                    loadData = true;
                }
                if (masterIds.isEmpty()) {
                    // 结束循环
                    break;
                }
            }
        }

        return loadData ? masterIds : null;
    }
    
}
