/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service.base;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyMasterTask;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.read.SillyMasterTaskUtil;
import com.iqiny.silly.core.service.SillyReadService;

import java.util.*;

/**
 * 默认抽象方法
 *
 * @param <M> 主
 * @param <N> 节点
 * @param <V> 变量
 */
@SuppressWarnings("unchecked")
public abstract class AbstractSillyReadService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyService<M, N, V> implements SillyReadService<M, N, V> {

    public static final String DEFAULT_QUERY_IDS_PARAM_NAME = "partitionMasterIds";

    public String queryIdsParamName() {
        return DEFAULT_QUERY_IDS_PARAM_NAME;
    }

    public String variableQueryStart() {
        return "variableQuery[";
    }

    public String variableQueryEnd() {
        return "]";
    }

    public String variableQueryKey() {
        return "variableQuery";
    }

    @Override
    protected void otherInit() {

    }

    /**
     * 查询主表数据
     *
     * @param masterId
     * @return
     */
    @Override
    public Map<String, Object> getMasterMap(String masterId) {
        final M master = getMaster(masterId);
        return SillyMapUtils.beanToMap(master);
    }

    /**
     * 获取当前任务下的变量数据
     *
     * @param taskId
     * @return
     */
    @Override
    public Map<String, Object> findVariableByTaskId(String taskId) {
        final V variable = sillyFactory.newVariable();
        variable.setTaskId(taskId);
        variable.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        return findVariableByVariable(variable);
    }

    /**
     * 获取数据 Map
     *
     * @param masterId
     * @return
     */
    public Map<String, Object> findVariableByMasterId(String masterId) {
        return findVariableByMasterIdNodeKey(masterId, null);
    }

    /**
     * 获取数据 Map
     *
     * @param masterId
     * @param nodeKey
     * @return
     */
    public Map<String, Object> findVariableByMasterIdNodeKey(String masterId, String nodeKey) {
        final V variable = sillyFactory.newVariable();
        variable.setMasterId(masterId);
        variable.setNodeKey(nodeKey);
        variable.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        return findVariableByVariable(variable);
    }

    /**
     * 获取数据 Map
     *
     * @param variable
     * @return
     */
    public Map<String, Object> findVariableByVariable(V variable) {
        final List<V> variables = findVariableList(variable);
        return variableList2Map(variables);
    }


    @Override
    public Map<String, Object> variableList2Map(List<V> variables) {
        Map<String, Object> variableMap = new LinkedHashMap<>();
        Map<String, Map<String, Map<String, Object>>> parallelMap = new LinkedHashMap<>();
        M master = null;
        for (V variable : variables) {
            if (master == null) {
                String masterId = variable.getMasterId();
                master = getMaster(masterId);
                SillyAssert.notNull(master, "变量数据获取主表数据失败" + masterId);
            }
            final SillyVariableConvertor<?> sillyHandler = getSillyConvertor(variable.getVariableType());
            try {
                SillyProcessNodeProperty<?> nodeProperty = getNodeProperty(master.processKey(), variable.getNodeKey());
                if (nodeProperty.isParallel()) {
                    Map<String, Map<String, Object>> nodeMap = parallelMap.putIfAbsent(variable.getNodeKey(), new LinkedHashMap<>());
                    Map<String, Object> taskMap = nodeMap.putIfAbsent(variable.getTaskId(), new LinkedHashMap<>());
                    sillyHandler.convert(taskMap, variable.getVariableName(), variable.getVariableText());
                }
            } catch (SillyException ignore) {
            }
            sillyHandler.convert(variableMap, variable.getVariableName(), variable.getVariableText());
        }

        if (!parallelMap.isEmpty()) {
            variableMap.put(parallelMapKey(), parallelMap);
        }
        return variableMap;
    }

    public String parallelMapKey() {
        return "parallelMap";
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

        final Set<String> keys = params.keySet();
        for (String key : keys) {
            // 默认获取以 variableQuery[ 开头， ] 结尾的参数 查询 变量表数据
            if (StringUtils.isEmpty(key)) {
                continue;
            }

            final Object value = params.get(key);
            if (value != null) {
                final List<V> vs = findVariableListByKV(key, value);
                if (vs != null) {
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
        }

        return loadData ? masterIds : null;
    }

    protected abstract List<V> findVariableListByKV(String key, Object value);

    protected List<String> findBusinessDoingMasterIds(Map<String, Object> params, String userId) {
        return null;
    }


    @Override
    public Object queryDoingPage(Map<String, Object> params) {
        final List<SillyMasterTask> masterTasks = sillyEngineService.getDoingMasterTask(usedCategory(), sillyCurrentUserUtil.currentUserId());
        final SillyMasterTaskUtil<? extends SillyMasterTask> masterTaskUtil = SillyMasterTaskUtil.create(masterTasks);
        List<String> masterIdList = masterTaskUtil.getMasterIds();
        List<String> businessDoingMasterIds = findBusinessDoingMasterIds(params, sillyCurrentUserUtil.currentUserId());
        if (businessDoingMasterIds != null && !businessDoingMasterIds.isEmpty()) {
            masterIdList.addAll(businessDoingMasterIds);
        }
        final Set<String> masterIds = findMasterIdByMap(masterIdList, params);
        return doQueryPage(params, masterIds, masterTaskUtil);
    }

    @Override
    public Object queryHistoryPage(Map<String, Object> params) {
        final List<SillyMasterTask> masterTasks = sillyEngineService.getHistoryMasterTask(usedCategory(), sillyCurrentUserUtil.currentUserId());
        final SillyMasterTaskUtil<? extends SillyMasterTask> masterTaskUtil = SillyMasterTaskUtil.create(masterTasks);
        List<String> masterIdList = masterTaskUtil.getMasterIds();
        final Set<String> masterIds = findMasterIdByMap(masterIdList, params);
        return doQueryPage(params, masterIds, masterTaskUtil);
    }

    @Override
    public Object queryPage(Map<String, Object> params) {
        final Set<String> masterIds = findMasterIdByMap(params);
        // masterIds == null 时表示没有变量表查询操作，!=null 表示有变量表操作
        return doQueryPage(params, masterIds);
    }

    @Override
    public Object sourcePage(Map<String, Object> params) {
        List<SillyMasterTask> masterTasks = this.sillyEngineService.getDoingMasterTask(this.usedCategory(), this.sillyCurrentUserUtil.currentUserId());
        SillyMasterTaskUtil<? extends SillyMasterTask> masterTaskUtil = SillyMasterTaskUtil.create(masterTasks);
        Set<String> masterIds = this.findMasterIdByMap(null, params);
        if (masterIds != null && masterIds.isEmpty()) {
            masterIds = null;
        }
        return this.doQueryPage(params, masterIds, masterTaskUtil);
    }

    /**
     * 子类重写此方法设置分页数据信息
     *
     * @param records
     */
    protected void setRecordInfo(List<Map<String, Object>> records, SillyMasterTaskUtil<? extends SillyMasterTask> masterTaskUtil) {
        for (Map<String, Object> record : records) {
            setOneRecordInfo(record, masterTaskUtil);
        }
    }

    /**
     * 子类重写此方法设置分页数据信息
     *
     * @param record
     */
    protected void setOneRecordInfo(Map<String, Object> record, SillyMasterTaskUtil<? extends SillyMasterTask> masterTaskUtil) {
        if (masterTaskUtil != null) {
            final String masterId = SillyMapUtils.getString(record, "id");
            SillyMasterTask one = masterTaskUtil.getOneTask(masterId);
            if (one == null) {
                SillyMapUtils.put(record, "taskId", null);
            } else {
                SillyMapUtils.put(record, "taskId", one.getTaskId());
                SillyMapUtils.put(record, "taskObj", one);
                SillyMapUtils.put(record, "taskList", masterTaskUtil.getTaskSet(masterId));
            }
            otherSetTaskRecord(record, masterTaskUtil);
        }
        setOneRecordInfo(record);
    }

    protected void setOneRecordInfo(Map<String, Object> record) {
    }

    protected void convertorRecordValue(String convertorName, Map<String, Object> record, String field) {
        getSillyConvertor(convertorName).convert(record, field, SillyMapUtils.getString(record, field));
    }


    protected void otherSetTaskRecord(Map<String, Object> record, SillyMasterTaskUtil<? extends SillyMasterTask> masterTaskUtil) {
    }


    protected Object doQueryPage(Map<String, Object> params, Set<String> masterIds) {
        return doQueryPage(params, masterIds, null);
    }

    protected abstract Object doQueryPage(Map<String, Object> params, Set<String> masterIds, SillyMasterTaskUtil<? extends SillyMasterTask> masterTaskUtil);
}
