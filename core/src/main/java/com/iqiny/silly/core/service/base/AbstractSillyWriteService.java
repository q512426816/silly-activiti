package com.iqiny.silly.core.service.base;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.CurrentUserUtil;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyConfig;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.resume.SillyResumeService;
import com.iqiny.silly.core.service.SillyEngineService;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认抽象方法
 *
 * @param <M> 主
 * @param <N> 节点
 * @param <V> 变量
 */
public abstract class AbstractSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable, T> implements SillyWriteService<M, N, V> {

    protected SillyConfig sillyConfig;

    protected SillyFactory<M, N, V> sillyFactory;

    protected SillyEngineService<T> sillyEngineService;

    protected CurrentUserUtil currentUserUtil;

    protected SillyResumeService sillyResumeService;

    protected Map<String, SillyVariableConvertor> sillyConvertorMap;

    @Override
    public void init() {
        setSillyFactory(createSillyFactory());
        if (sillyConfig == null) {
            sillyConfig = initSillyConfig();
        }
        setSillyEngineService(sillyConfig.getSillyEngineService());
        setCurrentUserUtil(sillyConfig.getCurrentUserUtil());
        setSillyConvertorMap(sillyConfig.getSillyConvertorMap());
        setSillyResumeService(sillyConfig.getSillyResumeService());
    }

    public abstract SillyConfig initSillyConfig();

    public void setSillyConfig(SillyConfig sillyConfig) {
        this.sillyConfig = sillyConfig;
    }

    public void setSillyFactory(SillyFactory<M, N, V> sillyFactory) {
        this.sillyFactory = sillyFactory;
    }

    public void setSillyEngineService(SillyEngineService sillyEngineService) {
        this.sillyEngineService = sillyEngineService;
    }

    public void setCurrentUserUtil(CurrentUserUtil currentUserUtil) {
        this.currentUserUtil = currentUserUtil;
    }

    public void setSillyConvertorMap(Map<String, SillyVariableConvertor> sillyConvertorMap) {
        this.sillyConvertorMap = sillyConvertorMap;
    }

    public void setSillyResumeService(SillyResumeService sillyResumeService) {
        this.sillyResumeService = sillyResumeService;
    }

    protected abstract void forceEndProcess(String processInstanceId, String userId);

    protected abstract SillyFactory<M, N, V> createSillyFactory();

    private SillyVariableConvertor<?> getSillyConvertor(String handleKey) {
        if (sillyConvertorMap == null) {
            throw new SillyException("Silly数据处理器未设置！");
        }

        return sillyConvertorMap.get(handleKey);
    }

    /**
     * 提交主数据 流程开始流转
     *
     * @param master
     */
    protected void submit(M master, boolean complete) {
        submit(master, new HashMap<>(), complete);
    }

    /**
     * 提交主数据 流程开始流转
     *
     * @param master
     */
    protected void submit(M master, Map<String, Object> varMap) {
        submit(master, varMap, true);
    }

    /**
     * 提交主数据 流程开始流转
     *
     * @param master
     * @param node
     */
    protected void submit(M master, N node) {
        submit(master, node, true);
    }

    /**
     * 提交主数据 流程开始流转
     *
     * @param master
     * @param node
     */
    protected void submit(M master, N node, boolean complete) {
        final String taskId = saveMasterAndStart(master, node, complete);
        if (StringUtils.isNotEmpty(taskId)) {
            node.setTaskId(taskId);
        }
        node.setMasterId(master.getId());
        submitNode(node, complete);
    }

    /**
     * 提交主数据 流程开始流转
     *
     * @param master
     * @param varMap
     * @param complete
     */
    protected void submit(M master, Map<String, Object> varMap, boolean complete) {
        final String taskId = saveMasterAndStart(master, varMap, complete);
        if (complete) {
            completeTask(taskId, null, varMap);
        }
    }

    /**
     * 提交流程节点数据 并完成
     *
     * @param node
     */
    protected void submitNode(N node) {
        submitNode(node, true);
    }

    /**
     * 提交流程节点数据 完成/保存
     *
     * @param node
     */
    protected void submitNode(N node, boolean complete) {
        Map<String, Object> varMap = saveNodeInfo(node);
        // 完成任务
        if (complete) {
            completeTask(node.getTaskId(), node.getNodeInfo(), varMap);
        }
    }

    /**
     * 完成任务
     *
     * @param taskId      任务ID
     * @param processInfo 流程信息
     * @param varMap      流程参数
     */
    protected abstract void completeTask(String taskId, String processInfo, Map<String, Object> varMap);


    /**
     * 保存主表数据 (新增同时启动流程)
     *
     * @param master
     * @return 任务启动返回 当前任务ID 否则返回 null
     */
    protected String saveMasterAndStart(M master, N node, boolean complete) {
        // 根据 node 数据生成 启动的流程数据
        final Map<String, Object> varMap = makeActVariableMap(node);
        return saveMasterAndStart(master, varMap, complete);
    }

    /**
     * 保存主表数据 (新增同时启动流程)
     *
     * @param master
     * @param varMap
     * @param complete
     * @return 任务启动返回 当前任务ID (任务启动只能有一个任务) 否则返回 null
     */
    @SuppressWarnings("unchecked")
    protected String saveMasterAndStart(M master, Map<String, Object> varMap, boolean complete) {
        if (StringUtils.isEmpty(master.getId())) {
            // 设置主表启动的状态
            if (StringUtils.isEmpty(master.getStatus())) {
                if (complete) {
                    master.setStatus(master.doingStatus());
                } else {
                    master.setStatus(master.startStatus());
                }
            }
            // 插入主表
            boolean saveFlag = insert(master);
            if (!saveFlag) {
                throw new SillyException("主信息保存发生异常！");
            }
            // 流程启动  返回任务ID
            String processInstanceId = sillyEngineService.start(master, varMap);
            master.setProcessId(processInstanceId);
            final List<T> tasks = sillyEngineService.findTaskByProcessInstanceId(processInstanceId);
            if (tasks.size() != 1) {
                throw new SillyException("任务启动第一位节点任务不可为多个！");
            }
            final T t = tasks.get(0);
            onStartProcess(master, t);
            
            saveFlag = updateById(master);
            if (!saveFlag) {
                throw new SillyException("主信息更新发生异常！");
            }
            
            return sillyEngineService.getTaskId(tasks.get(0));
        } else {
            boolean saveFlag = updateById(master);
            if (!saveFlag) {
                throw new SillyException("主信息更新发生异常！");
            }
            return null;
        }
    }

    protected abstract void onStartProcess(M master, T t);

    /**
     * 保存流程节点数据
     *
     * @param node
     * @return 工作流需要的数据 Map
     */
    protected Map<String, Object> saveNodeInfo(N node) {
        boolean isParallel = SillyConstant.ActivitiParallel.IS_PARALLEL.equals(node.getParallelFlag());
        // （若有）更新之前的流程信息 为历史状态
        updateToHistory(node, isParallel);
        doInsertNode(node);
        doInsertVariable(node);
        return makeActVariableMap(node);
    }

    private void updateToHistory(N node, boolean isParallel) {
        // （若有）更新之前的流程信息 为历史状态
        final N whereNode = sillyFactory.newNode();
        whereNode.setMasterId(node.getMasterId());
        whereNode.setNodeKey(node.getNodeKey());
        if (isParallel) {
            whereNode.setTaskId(node.getTaskId());
        } else {
            node.setParallelFlag(SillyConstant.ActivitiParallel.NOT_PARALLEL);
        }
        final N sillyNode = sillyFactory.newNode();
        sillyNode.setStatus(SillyConstant.ActivitiNode.STATUS_HISTORY);
        update(sillyNode, whereNode);

        // （若有）更新之前的流程变量信息 为历史状态
        final V whereVariable = sillyFactory.newVariable();
        whereVariable.setMasterId(node.getMasterId());
        whereVariable.setNodeKey(node.getNodeKey());
        if (isParallel) {
            whereVariable.setTaskId(node.getTaskId());
        }
        V sillyVariable = sillyFactory.newVariable();
        sillyVariable.setMasterId(node.getMasterId());
        sillyVariable.setNodeKey(node.getNodeKey());
        sillyVariable.setTaskId(node.getTaskId());
        sillyVariable.setStatus(SillyConstant.ActivitiNode.STATUS_HISTORY);
        update(sillyVariable, whereVariable);
    }

    private void doInsertNode(N node) {
        node.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        node.setNodeDate(new Date());
        node.setNodeUserId(currentUserUtil.currentUserId());
        boolean flag = insert(node);
        if (!flag) {
            throw new SillyException("保存流程主表信息发生异常！");
        }
    }

    private void doInsertVariable(N node) {
        // 保存变量
        List<V> variableList = node.getVariableList();
        if (variableList == null) {
            return;
        }

        for (V variable : variableList) {
            if (variable != null) {
                if (variable.getVariableName() == null) {
                    throw new SillyException("流程参数名称不可为空！" + variable.getVariableText());
                }

                final SillyVariableConvertor<?> handler = getSillyConvertor(variable.getVariableType());
                if (handler == null) {
                    throw new SillyException("未配置相关数据处理器" + variable.getVariableType());
                }

                List<V> saveVariableList = handler.saveVariable(variable);
                for (V v : saveVariableList) {
                    v.setId(null);
                    v.setTaskId(node.getTaskId());
                    v.setMasterId(node.getMasterId());
                    v.setNodeKey(node.getNodeKey());
                    v.setNodeId(node.getId());
                    v.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
                    boolean flag = insert(v);
                    if (!flag) {
                        throw new SillyException("保存流程子表信息发生异常！");
                    }
                }

            }
        }
    }

    /**
     * 生成工作流所需流程变量 Map
     *
     * @param node
     * @return Map
     */
    private Map<String, Object> makeActVariableMap(N node) {
        if (node == null) {
            return null;
        }
        Map<String, Object> varMap = node.getVariableMap();
        List<? extends SillyVariable> variableList = node.getVariableList();
        if (variableList == null) {
            return null;
        }
        for (SillyVariable variable : variableList) {
            final SillyVariableConvertor<?> handler = getSillyConvertor(variable.getActivitiHandler());
            if (handler != null) {
                String vn = variable.getVariableName();
                String vt = variable.getVariableText();
                handler.convert(varMap, vn, vt);
            }
        }
        return varMap;
    }

    /**
     * 删除主表及关闭工作流
     *
     * @param masterId
     * @param processInstanceId
     */
    protected void delete(String masterId, String processInstanceId) {
        boolean delFlag = deleteByMasterId(masterId);
        if (!delFlag) {
            throw new SillyException("主信息删除发生异常！");
        }
        // 结束流程
        forceEndProcess(processInstanceId, currentUserUtil.currentUserId());
    }

}
