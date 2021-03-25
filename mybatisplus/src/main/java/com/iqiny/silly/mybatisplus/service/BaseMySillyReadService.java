/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-mybatisplus 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.iqiny.silly.activiti.EnhanceSillyReadService;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.read.MySillyMasterTask;
import com.iqiny.silly.core.read.SillyMasterTaskUtil;
import com.iqiny.silly.mybatisplus.BaseMySillyMaster;
import com.iqiny.silly.mybatisplus.BaseMySillyNode;
import com.iqiny.silly.mybatisplus.BaseMySillyVariable;
import com.iqiny.silly.mybatisplus.utils.PageUtils;
import com.iqiny.silly.mybatisplus.utils.QueryUtil;

import java.util.*;

public abstract class BaseMySillyReadService<M extends BaseMySillyMaster<M>, N extends BaseMySillyNode<N, V>, V extends BaseMySillyVariable<V>>
        extends EnhanceSillyReadService<M, N, V> {

    public static final String DEFAULT_QUERY_IDS_PARAM_NAME = "partitionMasterIds";

    protected String queryIdsParamName() {
        return DEFAULT_QUERY_IDS_PARAM_NAME;
    }

    protected String variableQueryStart() {
        return "variableQuery[";
    }

    protected String variableQueryEnd() {
        return "]";
    }

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


    /**
     * 分页查询进行中任务 （经过流程引擎筛选）
     *
     * @param params
     * @return
     */
    public PageUtils queryDoingPage(Map<String, Object> params) {
        final List<MySillyMasterTask> masterTasks = sillyEngineService.getDoingMasterTask(usedCategory(), currentUserUtil.currentUserId());
        final SillyMasterTaskUtil masterTaskUtil = SillyMasterTaskUtil.create(masterTasks);
        final Set<String> masterIds = findMasterIdByMap(masterTaskUtil.getMasterIds(), params);

        return doQuery(params, masterIds, masterTaskUtil);
    }

    /**
     * 分页查询历史任务 （经过流程引擎筛选）
     *
     * @param params
     * @return
     */
    public PageUtils queryHistoryPage(Map<String, Object> params) {
        final List<MySillyMasterTask> masterTasks = sillyEngineService.getHistoryMasterTask(usedCategory(), currentUserUtil.currentUserId());
        final SillyMasterTaskUtil masterTaskUtil = SillyMasterTaskUtil.create(masterTasks);
        final Set<String> masterIds = findMasterIdByMap(masterTaskUtil.getMasterIds(), params);
        return doQuery(params, masterIds, masterTaskUtil);
    }

    /**
     * 分页查询记录（不经过流程引擎）
     *
     * @param params
     * @return
     */
    public PageUtils queryPage(Map<String, Object> params) {
        final Set<String> masterIds = findMasterIdByMap(params);
        // masterIds == null 时表示没有变量表查询操作，!=null 表示有变量表操作
        return doQuery(params, masterIds, null);
    }

    /**
     * 真正执行查询的方法
     *
     * @param params
     * @param masterIds
     * @param masterTaskUtil
     * @return
     */
    protected PageUtils doQuery(Map<String, Object> params, Set<String> masterIds, SillyMasterTaskUtil masterTaskUtil) {
        if (masterIds != null) {
            if (masterIds.isEmpty()) {
                return new PageUtils(makeNewPage(params));
            }
            final List<List<String>> partitionMasterIds = Lists.partition(new ArrayList<>(masterIds), 1000);
            params.put(queryIdsParamName(), partitionMasterIds);
        }

        IPage<Map<String, Object>> page = doQueryPage(makeNewPage(params), params);
        setRecordInfo(page.getRecords(), masterTaskUtil);
        return new PageUtils(page);
    }

    protected IPage<Map<String, Object>> makeNewPage(Map<String, Object> params) {
        return new QueryUtil<Map<String, Object>>().getPage(params);
    }

    protected abstract IPage<Map<String, Object>> doQueryPage(IPage<Map<String, Object>> page, Map<String, Object> params);

    /**
     * 子类重写此方法设置分页数据信息
     *
     * @param records
     */
    protected void setRecordInfo(List<Map<String, Object>> records, SillyMasterTaskUtil masterTaskUtil) {
        for (Map<String, Object> record : records) {
            setOneRecordInfo(record, masterTaskUtil);
        }
    }

    /**
     * 子类重写此方法设置分页数据信息
     *
     * @param record
     */
    protected void setOneRecordInfo(Map<String, Object> record, SillyMasterTaskUtil masterTaskUtil) {
        if (masterTaskUtil != null) {
            final String masterId = SillyMapUtils.getString(record, "id");
            String taskId = masterTaskUtil.getOneTaskId(masterId);
            SillyMapUtils.put(record, "taskId", taskId);
        }

    }


    protected Set<String> findMasterIdByMap(Map<String, Object> params) {
        return findMasterIdByMap(null, params);
    }

    protected Set<String> findMasterIdByMap(List<String> masterIdList, Map<String, Object> params) {
        // 已加载数据标识
        boolean loadData = (masterIdList != null);
        // 变量表查询返回的主表ID集合
        Set<String> masterIds = new LinkedHashSet<>();
        if (masterIdList != null) {
            masterIds.addAll(masterIdList);
        }
        if (params == null) {
            params = new HashMap<>();
        }

        final String start = variableQueryStart();
        final String end = variableQueryEnd();

        final Set<String> keys = params.keySet();
        for (String key : keys) {
            // 默认获取以 variableQuery[ 开头， ] 结尾的参数 查询 变量表数据
            if (StringUtils.isEmpty(key) || !key.startsWith(start) || !key.endsWith(end)) {
                continue;
            }
            final String name = key.substring(start.length(), key.lastIndexOf(end));
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
