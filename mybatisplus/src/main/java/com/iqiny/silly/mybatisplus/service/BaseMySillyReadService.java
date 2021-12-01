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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.iqiny.silly.activiti.EnhanceSillyReadService;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyMasterTask;
import com.iqiny.silly.core.read.SillyMasterTaskUtil;
import com.iqiny.silly.mybatisplus.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.BaseMySillyVariable;
import com.iqiny.silly.mybatisplus.utils.QueryUtil;
import org.apache.commons.collections.MapUtils;

import java.util.*;

public abstract class BaseMySillyReadService<M extends BaseMySillyMaster<M>, N extends BaseMySillyNode<N, V>, V extends BaseMySillyVariable<V>>
        extends EnhanceSillyReadService<M, N, V> {

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


    @Override
    protected Object doQueryPage(Map<String, Object> params, Set<String> masterIds, SillyMasterTaskUtil<? extends SillyMasterTask> masterTaskUtil) {
        if (masterIds != null) {
            if (masterIds.isEmpty()) {
                return new Page<>();
            }
            final List<List<String>> partitionMasterIds = Lists.partition(new ArrayList<>(masterIds), 1000);
            params.put(queryIdsParamName(), partitionMasterIds);
        }
        IPage<Map<String, Object>> page = doQueryPage(makeNewPage(params), params);
        setRecordInfo(page.getRecords(), masterTaskUtil);
        return page;
    }

    /**
     * 分页查询进行中任务 （经过流程引擎筛选）
     *
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> queryDoingPage(Map<String, Object> params) {
        return (IPage<Map<String, Object>>) super.queryDoingPage(params);
    }


    /**
     * 分页查询历史任务 （经过流程引擎筛选）
     *
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> queryHistoryPage(Map<String, Object> params) {
        return (IPage<Map<String, Object>>) super.queryHistoryPage(params);
    }

    /**
     * 分页查询记录（不经过流程引擎）
     *
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> queryPage(Map<String, Object> params) {
        return (IPage<Map<String, Object>>) super.queryPage(params);
    }

    /**
     * 分页查询全部数据 （经过流程引擎 添加任务信息）
     *
     * @param params
     * @return
     */
    @Override
    public IPage<Map<String, Object>> sourcePage(Map<String, Object> params) {
        return (IPage<Map<String, Object>>) super.sourcePage(params);
    }

    @Override
    public Map<String, Object> queryOne(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        final Map<String, Object> params = new HashMap<>();
        Set<String> masterIds = new LinkedHashSet<>();
        masterIds.add(id);
        final List<List<String>> partitionMasterIds = Lists.partition(new ArrayList<>(masterIds), 1000);
        params.put(queryIdsParamName(), partitionMasterIds);
        IPage<Map<String, Object>> page = doQueryPage(makeNewPage(params), params);
        final List<Map<String, Object>> records = page.getRecords();
        setRecordInfo(records, null);
        if (records.isEmpty()) {
            return null;
        }

        return records.iterator().next();
    }

    protected IPage<Map<String, Object>> makeNewPage(Map<String, Object> params) {
        return new QueryUtil<Map<String, Object>>().getPage(params);
    }

    protected abstract IPage<Map<String, Object>> doQueryPage(IPage<Map<String, Object>> page, Map<String, Object> params);


    protected QueryWrapper<V> makeQueryWrapper(String key, Object value) {
        final String start = variableQueryStart();
        final String end = variableQueryEnd();

        String tempName = null;
        if (key.startsWith(start) && key.endsWith(end)) {
            tempName = key.substring(start.length(), key.lastIndexOf(end));
        } else if (key.equals(variableQueryKey())) {
            tempName = key;
        } else {
            return null;
        }

        QueryWrapper<V> qw = new QueryWrapper<>();
        qw.select("MASTER_ID");
        qw.eq("STATUS", SillyConstant.ActivitiNode.STATUS_CURRENT);

        final String name = tempName;
        qw.and(i -> i.eq("variable_Name", name));

        setQueryWrapperTextValue(qw, name, value);

        return qw;
    }

    protected void setQueryWrapperTextValue(QueryWrapper<V> qw, String name, Object value) {
        if (value == null) {
            return;
        }

        if (value instanceof Collection) {
            qw.in("variable_Text", (Collection<?>) value);
        } else if (name.endsWith("Ids") && value instanceof String) {
            String str = (String) value;
            qw.in("variable_Text", new ArrayList<>(Arrays.asList(str.split(SillyConstant.ARRAY_SPLIT_STR))));
        } else if (name.endsWith("Id")) {
            qw.eq("variable_Text", value);
        } else {
            qw.like("variable_Text", value);
        }
    }


    @Override
    public List<N> getAllNodeList(String masterId) {
        final N n = sillyFactory.newNode();
        n.setMasterId(masterId);
        QueryWrapper<N> qw = new QueryWrapper<>();
        qw.setEntity(n);
        qw.orderByAsc("CREATE_DATE");
        return sillyFactory.newNode().selectList(qw);
    }

    @Override
    public N getNode(String masterId, String nodeKey) {
        final N n = sillyFactory.newNode();
        n.setMasterId(masterId);
        n.setNodeKey(nodeKey);
        QueryWrapper<N> qw = new QueryWrapper<>();
        qw.setEntity(n);
        qw.orderByAsc("CREATE_DATE");
        return sillyFactory.newNode().selectOne(qw);
    }

    @Override
    protected List<V> findVariableListByKV(String key, Object value) {
        QueryWrapper<V> qw = makeQueryWrapper(key, value);
        List<V> vs = null;
        if (qw != null) {
            vs = sillyFactory.newVariable().selectList(qw);
        }
        return vs;
    }
}
