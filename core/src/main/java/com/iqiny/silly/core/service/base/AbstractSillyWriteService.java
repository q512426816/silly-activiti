package com.iqiny.silly.core.service.base;

import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.service.SillyWriteService;
import com.iqiny.silly.common.Constant;
import com.iqiny.silly.common.util.CurrentUserUtil;
import com.iqiny.silly.common.util.StringUtils;

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
public abstract class AbstractSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> implements SillyWriteService<M, N, V> {

    protected SillyFactory<M, N, V> sillyFactory;

    protected CurrentUserUtil currentUserUtil;

    protected Map<String, SillyVariableConvertor<?>> sillyHandlerMap;

    @Override
    public void init() {
        this.sillyFactory = createSillyFactory();
        this.currentUserUtil = sillyFactory.currentUserUtil();
        this.sillyHandlerMap = initSillyHandlerMap();
    }

    protected abstract String startProcess(M master, Map<String, Object> varMap);

    protected abstract void forceEndProcess(String actProcessId);

    protected abstract SillyFactory<M, N, V> createSillyFactory();

    protected abstract Map<String, SillyVariableConvertor<?>> initSillyHandlerMap();

    private SillyVariableConvertor<?> getSillyHandler(String handleKey) {
        if (sillyHandlerMap == null) {
            synchronized (this) {
                if (sillyHandlerMap == null) {
                    sillyHandlerMap = initSillyHandlerMap();
                }
            }
            if (sillyHandlerMap == null) {
                throw new RuntimeException("Silly数据处理器未设置！");
            }
        }

        return sillyHandlerMap.get(handleKey);
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
            completeTask(node.getTaskId(), node.getProcessInfo(), varMap);
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
     * @return 任务启动返回 当前任务ID 否则返回 null
     */
    protected String saveMasterAndStart(M master, Map<String, Object> varMap, boolean complete) {
        if (StringUtils.isEmpty(master.getId())) {
            // 设置主表启动的状态
            if (StringUtils.isEmpty(master.getStatus())) {
                if (complete) {
                    master.setStatus(master.getDoingStatus());
                } else {
                    master.setStatus(master.getStartStatus());
                }
            }
            // 插入主表
            boolean saveFlag = insert(master);
            if (!saveFlag) {
                throw new RuntimeException("主信息保存发生异常！");
            }
            // 流程启动  返回任务ID
            return startProcess(master, varMap);
        } else {
            boolean saveFlag = updateById(master);
            if (!saveFlag) {
                throw new RuntimeException("主信息保存发生异常！");
            }
            return null;
        }
    }

    /**
     * 保存流程节点数据
     *
     * @param node
     * @return 工作流需要的数据 Map
     */
    protected Map<String, Object> saveNodeInfo(N node) {
        boolean isParallel = Constant.ActivitiParallel.IS_PARALLEL.equals(node.getParallelFlag());
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
            node.setParallelFlag(Constant.ActivitiParallel.NOT_PARALLEL);
        }
        final N sillyNode = sillyFactory.newNode();
        sillyNode.setStatus(Constant.ActivitiNode.STATUS_HISTORY);
        update(sillyNode, whereNode);

        // （若有）更新之前的流程变量信息 为历史状态
        final V whereVariable = sillyFactory.newVariable();
        whereVariable.setMasterId(node.getMasterId());
        whereVariable.setNodeKey(node.getMasterId());
        if (isParallel) {
            whereVariable.setTaskId(node.getTaskId());
        }
        V sillyVariable = sillyFactory.newVariable();
        sillyVariable.setMasterId(node.getMasterId());
        sillyVariable.setNodeKey(node.getNodeKey());
        sillyVariable.setTaskId(node.getTaskId());
        sillyVariable.setStatus(Constant.ActivitiNode.STATUS_HISTORY);
        update(sillyVariable, whereVariable);
    }

    private void doInsertNode(N node) {
        node.setStatus(Constant.ActivitiNode.STATUS_CURRENT);
        boolean flag = insert(node);
        if (!flag) {
            throw new RuntimeException("保存NCR流程主表信息发生异常！");
        }
    }

    private void doInsertVariable(N node) {
        // 保存变量
        List<V> variableList = node.getVariableList();
        for (V variable : variableList) {
            if (variable != null) {
                if (variable.getVariableName() == null) {
                    throw new RuntimeException("流程参数名称不可为空！" + variable.getVariableText());
                }
                variable.setId(null);
                variable.setTaskId(node.getTaskId());
                variable.setMasterId(node.getMasterId());
                variable.setNodeKey(node.getNodeKey());
                variable.setProcessId(node.getId());
                variable.setStatus(Constant.ActivitiNode.STATUS_CURRENT);
                boolean flag = insert(variable);
                if (!flag) {
                    throw new RuntimeException("保存流程子表信息发生异常！");
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
        for (SillyVariable variable : variableList) {
            final SillyVariableConvertor<?> handler = getSillyHandler(variable.getActivitiHandler());
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
     * @param actProcessId
     */
    protected void delete(String masterId, String actProcessId) {
        boolean delFlag = deleteByMasterId(masterId);
        if (!delFlag) {
            throw new RuntimeException("主信息删除发生异常！");
        }
        // 结束流程
        forceEndProcess(actProcessId);
    }

    /**
     * 关闭流程方法
     *
     * @param node
     */
    protected void close(N node) {
        saveNodeInfo(node);
        // 更新状态 关闭
        M master = sillyFactory.newMaster();
        master.setId(node.getMasterId());
        master.setStatus(master.getEndStatus());
        master.setCloseDate(new Date());
        master.setCloseUserId(currentUserUtil.currentUserId());
        if (!updateById(master)) {
            throw new RuntimeException("更新主表状态发生异常！");
        }
        // 强制流程结束
        forceEndProcess(master.getActProcessId());

    }

}
