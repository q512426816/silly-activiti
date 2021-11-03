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
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.convertor.SillyAutoConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;

import java.util.*;

/**
 * 默认抽象方法
 *
 * @param <M> 主
 * @param <N> 节点
 * @param <V> 变量
 */
public abstract class AbstractSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable, T>
        extends AbstractSillyService<M, N, V, T> implements SillyWriteService<M, N, V> {

    protected SillyReadService<M, N, V> sillyReadService;

    @Override
    protected void otherInit() {
        sillyReadService = getSillyConfig().getSillyReadService(usedCategory());
    }
    
    /**
     * 提交数据 流程流转
     *
     * @param master
     * @param node
     */
    protected void submit(M master, N node) {
        Map<String, Object> varMap = makeActVariableMap(node);
        // 保存数据
        save(master, node, varMap);

        final String nowTaskId = node.getTaskId();
        SillyAssert.notEmpty(nowTaskId, "提交时，当前任务ID不存在");
        // 当前任务
        final T nowTask = sillyEngineService.findTaskById(nowTaskId);
        SillyAssert.notNull(nowTask, "提交时，当前任务不存在");
        // 完成任务，返回下一步任务列表
        List<T> taskList = completeTask(master, node, varMap);

        afterSubmit(master, node, nowTask, taskList);
    }

    /**
     * 提交方法之后的回调
     *
     * @param master
     * @param node
     * @param oldTask
     * @param taskList
     */
    protected void afterSubmit(M master, N node, T oldTask, List<T> taskList) {
        // 保存流程履历信息
        saveProcessResume(node, oldTask, taskList);
    }

    /**
     * 保存流程履历信息
     *
     * @param node         当前处置节点
     * @param oldTask      当前任务
     * @param nextTaskList 下一步任务
     */
    protected abstract void saveProcessResume(N node, T oldTask, List<T> nextTaskList);

    /**
     * 保存流程履历
     *
     * @param node       当前处置节点
     * @param oldTask    当前任务
     * @param nextTaskId 下一步任务ID
     */
    protected abstract void saveProcessResume(N node, T oldTask, String nextTaskId);


    /**
     * 保存数据 不提交且不启动流程
     *
     * @param master
     * @param node
     */
    protected void save(M master, N node) {
        save(master, node, false, null);
    }


    /**
     * 保存数据 不提交 尝试启动流程
     *
     * @param master
     * @param node
     */
    protected void saveAndTryStartProcess(M master, N node) {
        Map<String, Object> varMap = makeActVariableMap(node);
        save(master, node, true, varMap);
    }

    /**
     * 保存数据 不提交 并启动流程
     *
     * @param master
     * @param node
     */
    protected void save(M master, N node, Map<String, Object> varMap) {
        SillyAssert.notNull(master, "启动流程主数据不可为null");
        SillyAssert.notNull(node, "启动流程节点数据不可为null");
        save(master, node, true, varMap);
    }

    /**
     * 保存数据 不提交且不启动流程
     *
     * @param master 可以为null， 为null 时不可启动流程
     * @param node
     */
    protected void save(M master, N node, boolean startProcess, Map<String, Object> varMap) {
        if (master != null) {
            saveMaster(master);
            node.setMasterId(master.getId());
            if (startProcess) {
                startProcess(master, node, varMap);
            }
        }

        saveNodeInfo(node);
    }

    /**
     * 根据业务ID启动流程， 仅启动流程
     *
     * @param master
     */
    protected void onlyStartProcess(M master, N node) {
        if (StringUtils.isEmpty(node.getMasterId())) {
            node.setMasterId(master.getId());
        }
        if (StringUtils.isEmpty(node.getHandleType())) {
            node.setHandleType(SillyConstant.SillyResumeType.PROCESS_TYPE_START);
        }
        if (StringUtils.isEmpty(node.getNodeKey())) {
            node.setNodeKey(SillyConstant.ActivitiNode.KEY_START);
        }
        if (StringUtils.isEmpty(node.getNodeInfo())) {
            node.setNodeInfo("流程启动");
        }

        // 获取流程变量
        final Map<String, Object> varMap = makeActVariableMap(node);
        // 启动流程
        startProcess(master, node, varMap);

        afterOnlyStartProcess(master, node);
    }


    protected void afterOnlyStartProcess(M master, N node) {
        // 保存流程履历信息
        saveProcessResume(node, null, node.getTaskId());
    }


    /**
     * 重启流程 ，撤销到 启动节点
     *
     * @param master
     */
    protected void reStartProcess(M master, N node) {
        if (StringUtils.isEmpty(node.getMasterId())) {
            node.setMasterId(master.getId());
        }
        // 结束之前存在的流程实例
        sillyEngineService.deleteProcessInstance(master.getProcessId(), "流程撤销");
        master.setProcessId("");
        // 获取流程变量
        final Map<String, Object> varMap = makeActVariableMap(node.getVariableList());
        // 启动新流程
        doStartProcess(master, node, varMap);

        afterReStartProcess(master, node);
    }

    /**
     * 流程重启之后的回调
     *
     * @param master
     * @param node
     */
    protected void afterReStartProcess(M master, N node) {
        // 保存履历
        node.setHandleType(SillyConstant.SillyResumeType.PROCESS_TYPE_START);
        saveProcessResume(node, null, node.getTaskId());
    }


    /**
     * 完成任务
     *
     * @param master 主对象
     * @param node   节点对象
     * @param varMap 流程参数
     */
    protected abstract List<T> completeTask(M master, N node, Map<String, Object> varMap);

    /**
     * 启动流程返回任务ID
     *
     * @param master
     * @param varMap
     * @return
     */
    protected void startProcess(M master, N node, Map<String, Object> varMap) {
        // 验证主表是否存在流程实例，若存在则不重复启动流程实例
        if (StringUtils.isNotEmpty(master.getProcessId())) {
            return;
        }

        final List<T> taskList = sillyEngineService.findTaskByMasterId(master.getId());
        if (taskList != null && !taskList.isEmpty()) {
            return;
        }
        doStartProcess(master, node, varMap);
    }

    protected void doStartProcess(M master, N node, Map<String, Object> varMap) {
        // 流程启动  返回任务ID
        String processInstanceId = sillyEngineService.start(master, varMap);
        final List<T> tasks = sillyEngineService.findTaskByProcessInstanceId(processInstanceId);
        final T t = tasks.get(0);
        node.setTaskId(sillyEngineService.getTaskId(t));

        master.setProcessId(processInstanceId);

        afterStartProcess(master, node, t);

    }

    protected void saveMaster(M master) {
        if (StringUtils.isEmpty(master.getId())) {
            // 插入主表
            boolean saveFlag = insert(master);
            if (!saveFlag) {
                throw new SillyException("主信息保存发生异常！");
            }
        } else {
            boolean saveFlag = updateById(master);
            if (!saveFlag) {
                throw new SillyException("主信息更新发生异常！");
            }
        }
    }

    protected abstract void afterStartProcess(M master, N node, T t);

    /**
     * 保存流程节点数据
     *
     * @param node
     * @return 工作流需要的数据 Map
     */
    protected void saveNodeInfo(N node) {
        if (node == null) {
            return;
        }
        boolean isParallel = SillyConstant.ActivitiParallel.IS_PARALLEL.equals(node.getParallelFlag());
        // （若有）更新之前的流程信息 为历史状态
        updateToHistory(node, isParallel);
        doInsertNode(node);
        doInsertVariable(node);
    }

    private void updateToHistory(N node, boolean isParallel) {
        // （若有）更新之前的流程信息 为历史状态
        final N whereNode = sillyFactory.newNode();
        SillyAssert.notEmpty(node.getMasterId(), "node.masterId 不可为空");
        SillyAssert.notEmpty(node.getNodeKey(), "node.nodeKey 不可为空");
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

        List<V> saveList = new ArrayList<>();
        for (V variable : variableList) {
            if (variable == null) {
                continue;
            }

            if (variable.getVariableName() == null) {
                throw new SillyException("流程参数名称不可为空！" + variable.getVariableText());
            }

            // 数据保存处置
            boolean saveFlag = batchSaveHandle(node, variable);
            if (!saveFlag) {
                continue;
            }

            String variableType = variable.getVariableType();
            SillyVariableConvertor<?> handler = getSillyConvertor(variableType);
            // 仅对 string、list 类型的数据进行自动转换
            if (variableType.equals(SillyConstant.ActivitiNode.CONVERTOR_STRING) || variableType.equals(SillyConstant.ActivitiNode.CONVERTOR_LIST)) {
                for (String name : sillyConvertorMap.keySet()) {
                    SillyVariableConvertor<?> convertor = sillyConvertorMap.get(name);
                    if (convertor instanceof SillyAutoConvertor) {
                        SillyAutoConvertor autoConvertor = (SillyAutoConvertor) convertor;
                        if (autoConvertor.auto() && autoConvertor.canConvertor(variable.getVariableName(), variable.getVariableText())) {
                            handler = convertor;
                            break;
                        }
                    }
                }
            }

            if (handler == null) {
                throw new SillyException("未配置相关数据处理器" + variable.getVariableType());
            }

            // 获取处理之后 真正需要保存的 variable 数据
            List<SillyVariable> saveVariableList = handler.makeSaveVariable(variable);
            if (saveVariableList != null) {
                for (SillyVariable v : saveVariableList) {
                    v.setId(null);
                    v.setTaskId(node.getTaskId());
                    v.setMasterId(node.getMasterId());
                    v.setNodeKey(node.getNodeKey());
                    v.setNodeId(node.getId());
                    v.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
                }

                saveList.addAll((Collection<? extends V>) saveVariableList);
            }
        }
        if (!saveList.isEmpty()) {
            boolean flag = batchInsert(saveList);
            if (!flag) {
                throw new SillyException("保存流程子表信息发生异常！");
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
        List<V> variableList = node.getVariableList();
        if (variableList != null) {
            for (SillyVariable variable : variableList) {
                final SillyVariableConvertor<?> handler = getSillyConvertor(variable.getActivitiHandler());
                if (handler != null) {
                    String vn = variable.getVariableName();
                    String vt = variable.getVariableText();
                    handler.convert(varMap, vn, vt);
                }
            }
        }
        return varMap;
    }

    protected Map<String, Object> makeActVariableMap(List<V> variableList) {
        if (variableList == null) {
            return null;
        }
        Map<String, Object> varMap = new LinkedHashMap<>();
        for (V variable : variableList) {
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
        if (StringUtils.isNotEmpty(processInstanceId)) {
            forceEndProcess(processInstanceId);
        }
    }

    /**
     * 强制结束流程
     *
     * @param processInstanceId
     */
    protected abstract void forceEndProcess(String processInstanceId);
}
