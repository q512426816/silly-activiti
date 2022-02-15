/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service.base;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyMasterTask;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.config.property.impl.DefaultVariableSaveHandle;
import com.iqiny.silly.core.convertor.SillyAutoConvertor;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.engine.SillyTask;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.service.SillyReadService;

import java.util.*;

/**
 * 默认抽象方法
 *
 * @param <M> 主
 * @param <N> 节点
 * @param <V> 变量
 */
public abstract class AbstractSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends InternalSillyWriteService<M, N, V> {

    protected SillyReadService<M, N, V> sillyReadService;

    @Override
    protected void otherInit() {
        this.sillyReadService = getSillyConfig().getSillyReadService();
    }

    /**
     * 批量保存处置类处理
     *
     * @param node
     * @param variables
     * @return
     */
    protected boolean batchSaveHandle(M master, N node, V variables, SillyPropertyHandle propertyHandle) {
        String saveHandleNames = variables.getSaveHandleName();
        SillyAssert.notEmpty(saveHandleNames, "批处理数据保存不可为空");
        String[] saveHandleNameArr = StringUtils.split(saveHandleNames, SillyConstant.ARRAY_SPLIT_STR);
        boolean lastFlag = true;
        for (String saveHandleName : saveHandleNameArr) {
            String stringValue = propertyHandle.getStringValue(saveHandleName.trim());
            if (StringUtils.isNotEmpty(stringValue)) {
                lastFlag = getSillyVariableSaveHandle(stringValue).handle(master, node, variables);
            }
        }
        return lastFlag;
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

        doSubmit(master, node, varMap);
    }

    protected void doSubmit(M master, N node, Map<String, Object> varMap) {
        final String nowTaskId = node.getTaskId();
        SillyAssert.notEmpty(nowTaskId, "提交时，当前任务ID不存在");
        // 当前任务
        final SillyTask nowTask = sillyEngineService.findTaskById(nowTaskId);
        SillyAssert.notNull(nowTask, "提交时，当前任务不存在");
        // 完成任务，返回下一步任务列表
        List<? extends SillyTask> taskList = completeTask(master, node, nowTask, varMap);

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
    protected void afterSubmit(M master, N node, SillyTask oldTask, List<? extends SillyTask> taskList) {
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
    protected void saveProcessResume(N node, SillyTask oldTask, List<? extends SillyTask> nextTaskList) {
        String handleType = node.getHandleType();
        if (StringUtils.isEmpty(handleType)) {
            // 默认下一步
            handleType = SillyConstant.SillyResumeType.PROCESS_TYPE_NEXT;
        }
        String taskName = node.getNodeInfo();
        String taskKey = node.getNodeKey();
        Long dueTime = null;
        if (oldTask != null) {
            dueTime = sillyEngineService.getTaskDueTime(oldTask);
            taskName = oldTask.getName();
            taskKey = oldTask.getTaskDefinitionKey();
        }

        final Set<String> nextUserIds = nextProcess(nextTaskList);
        final String joinNextUserIds = StringUtils.myJoin(nextUserIds);
        // 履历内容
        String handleInfo = sillyResumeService.makeResumeHandleInfo(handleType, joinNextUserIds, taskName, node.getNodeInfo());

        final String masterId = node.getMasterId();
        doSaveProcessResume(masterId, handleInfo, handleType, taskKey, taskName, joinNextUserIds, dueTime);
    }


    /**
     * 保存流程履历
     *
     * @param node       当前处置节点
     * @param oldTask    当前任务
     * @param nextTaskId 下一步任务ID
     */
    protected void saveProcessResume(N node, SillyTask oldTask, String nextTaskId) {
        final SillyTask task = sillyEngineService.findTaskById(nextTaskId);
        SillyAssert.notNull(task, "任务信息获取失败" + nextTaskId);
        saveProcessResume(node, oldTask, Collections.singletonList(task));
    }


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
            if (startProcess && startProcess(master, varMap)) {
                setNodeMyTaskId(master.getId(), node);
            }
        }

        saveNodeInfo(node);
    }

    /**
     * 设置节点的 我的任务ID
     *
     * @param masterId
     * @param node
     * @return
     */
    protected SillyMasterTask setNodeMyTaskId(String masterId, N node) {
        SillyMasterTask task = sillyEngineService.getOneTask(usedCategory(), sillyCurrentUserUtil.currentUserId(), masterId);
        SillyAssert.notNull(task, "未找到您需要处置的任务" + usedCategory());

        node.setTaskId(task.getTaskId());
        if (StringUtils.isEmpty(node.getNodeKey())) {
            node.setNodeKey(task.getNodeKey());
        }
        return task;
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
        startProcess(master, varMap);
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
        doStartProcess(master, varMap);
        // 设置节点任务ID
        setNodeMyTaskId(master.getId(), node);

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
    protected List<? extends SillyTask> completeTask(M master, N node, SillyTask task, Map<String, Object> varMap) {
        complete(varMap, task);
        return afterCompleteTask(master, node, task);
    }

    /**
     * 完成流程任务  (主表状态只控制 新建（保存）/提交（提交）/完成（结束））
     */
    protected void complete(Map<String, Object> variableMap, SillyTask task) {
        // 完成此步骤流程
        sillyEngineService.complete(task, sillyCurrentUserUtil.currentUserId(), variableMap);
    }

    /**
     * 任务完成之后的回调
     */
    protected List<? extends SillyTask> afterCompleteTask(M master, N node, SillyTask oldTask) {
        final String actProcessId = oldTask.getProcessInstanceId();
        // 判断是否结束任务了
        List<? extends SillyTask> taskList = sillyEngineService.findTaskByProcessInstanceId(actProcessId);
        if (taskList.isEmpty()) {
            afterCloseProcess(master, node);
        } else {
            // 下一步任务记录
            afterCompleteProcess(master, node, taskList);
        }
        return taskList;
    }

    /**
     * 流程结束之后的回调
     */
    protected void afterCloseProcess(M master, N node) {
        final String masterId = master.getId();
        if (masterId == null) {
            throw new SillyException("通过任务流程实例ID获取主表数据ID异常！");
        }
        boolean updateFlag = false;
        if (StringUtils.isEmpty(master.getStatus())) {
            master.setStatus(master.endStatus());
            master.setCloseDate(new Date());
            master.setCloseUserId(sillyCurrentUserUtil.currentUserId());
            master.setHandleUserName("");
            updateFlag = true;
        }
        if (StringUtils.isEmpty(master.getTaskName())) {
            master.setTaskName("流程结束");
            updateFlag = true;
        }
        node.setHandleType(SillyConstant.SillyResumeType.PROCESS_TYPE_CLOSE);

        if (updateFlag && !updateById(master)) {
            throw new SillyException("更新主表状态发生异常！");
        }
    }

    /**
     * 流程完成之后的回调
     */
    protected void afterCompleteProcess(M master, N node, List<? extends SillyTask> taskList) {
        String masterId = master.getId();
        if (masterId == null) {
            throw new SillyException("通过任务流程实例ID获取主表数据ID异常！");
        }

        boolean updateFlag = false;
        if (StringUtils.isEmpty(master.getTaskName())) {
            master.setTaskName(makeTaskName(taskList));
            updateFlag = true;
        }
        if (StringUtils.isEmpty(master.getHandleUserName())) {
            master.setHandleUserName(userIdsToName(nextProcess(taskList)));
            updateFlag = true;
        }
        if (StringUtils.isEmpty(master.getStatus())) {
            master.setStatus(master.doingStatus());
            updateFlag = true;
        }
        if (StringUtils.isEmpty(node.getHandleType())) {
            node.setHandleType(SillyConstant.SillyResumeType.PROCESS_TYPE_NEXT);
            updateFlag = true;
        }

        if (updateFlag && !updateById(master)) {
            throw new SillyException("更新主表数据发生异常！");
        }
    }


    protected String makeTaskName(List<? extends SillyTask> taskList) {
        Set<String> taskNames = new LinkedHashSet<>();
        for (SillyTask task : taskList) {
            taskNames.add(task.getName());
        }
        return StringUtils.myJoin(taskNames, SillyConstant.ARRAY_SPLIT_STR);
    }

    protected String userIdsToName(String userIds) {
        if (StringUtils.isEmpty(userIds)) {
            return null;
        }

        final String[] split = userIds.split(SillyConstant.ARRAY_SPLIT_STR);
        Set<String> userNames = new LinkedHashSet<>();
        for (String userId : split) {
            userNames.add(sillyCurrentUserUtil.userIdToName(userId));
        }
        return StringUtils.myJoin(userNames, SillyConstant.ARRAY_SPLIT_STR);
    }

    protected String userIdsToName(Set<String> userIds) {
        if (userIds == null) {
            return null;
        }

        Set<String> userNames = new LinkedHashSet<>();
        for (String userId : userIds) {
            userNames.add(sillyCurrentUserUtil.userIdToName(userId));
        }
        return StringUtils.myJoin(userNames, SillyConstant.ARRAY_SPLIT_STR);
    }

    protected Set<String> nextProcess(List<? extends SillyTask> taskList) {
        Set<String> userIds = new LinkedHashSet<>();
        for (SillyTask task : taskList) {
            final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task);
            if (taskUserIds != null && !taskUserIds.isEmpty()) {
                userIds.addAll(taskUserIds);
            }
        }
        return userIds;
    }


    protected void doSaveProcessResume(String masterId, String handleInfo, String handleType, String processNodeKey, String processNodeName, String nextUserIds, Long dueTime) {
        SillyResume process = sillyFactory.newResume();
        final String businessId = processResumeBusinessId(masterId);
        SillyAssert.notEmpty(businessId, "履历保存业务主键不可为空");
        process.setProcessType(handleType);
        process.setBusinessId(businessId);
        process.setBusinessType(usedCategory());
        process.setHandleInfo(handleInfo);
        process.setProcessNodeKey(processNodeKey);
        process.setProcessNodeName(processNodeName);
        process.setNextUserId(nextUserIds);
        process.setConsumeTime(dueTime);
        // 插入流程履历
        sillyResumeService.insert(process);
    }


    /**
     * 通过主表ID 获取流程履历业务ID
     */
    protected String processResumeBusinessId(String masterId) {
        // 默认使用本身作为流程履历业务ID
        return masterId;
    }


    /**
     * 启动流程返回任务ID
     *
     * @param master
     * @param varMap
     * @return 是否启动
     */
    protected boolean startProcess(M master, Map<String, Object> varMap) {
        // 验证主表是否存在流程实例，若存在则不重复启动流程实例
        if (StringUtils.isNotEmpty(master.getProcessId())) {
            return false;
        }

        final List<? extends SillyTask> taskList = sillyEngineService.findTaskByMasterId(master.getId());
        if (taskList != null && !taskList.isEmpty()) {
            return false;
        }
        return doStartProcess(master, varMap);
    }

    protected boolean doStartProcess(M master, Map<String, Object> varMap) {
        // 流程启动  返回任务ID
        String processInstanceId = sillyEngineService.start(master, varMap);
        master.setProcessId(processInstanceId);

        afterStartProcess(master);

        return StringUtils.isNotEmpty(processInstanceId);
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

    protected void afterStartProcess(M master) {
        final M copyMaster = sillyFactory.newMaster();
        copyMaster.setId(master.getId());
        copyMaster.setProcessId(master.getProcessId());
        if (StringUtils.isEmpty(master.getStatus())) {
            copyMaster.setStatus(master.startStatus());
        }

        copyMaster.setStartDate(new Date());
        copyMaster.setStartUserId(sillyCurrentUserUtil.currentUserId());
        updateHandleUserNameAndTaskName(copyMaster);
    }

    public void updateHandleUserNameAndTaskName(M master) {
        List<? extends SillyTask> taskList = sillyEngineService.findTaskByProcessInstanceId(master.getProcessId());
        updateHandleUserNameAndTaskName(master, taskList);
    }

    public void updateHandleUserNameAndTaskName(M master, List<? extends SillyTask> taskList) {
        master.setHandleUserName(this.userIdsToName(this.nextProcess(taskList)));
        master.setTaskName(makeTaskName(taskList));
        boolean saveFlag = updateById(master);
        if (!saveFlag) {
            throw new SillyException("主信息更新发生异常！");
        }
    }

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
        node.setNodeUserId(sillyCurrentUserUtil.currentUserId());
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

            String variableType = variable.getVariableType();
            SillyVariableConvertor<?> handler = getSillyConvertor(variableType);
            // 仅对 string、list 类型的数据进行自动转换
            if (variableType.equals(SillyConstant.ActivitiNode.CONVERTOR_STRING) || variableType.equals(SillyConstant.ActivitiNode.CONVERTOR_LIST)) {
                for (Map.Entry<String, SillyVariableConvertor> convertorEntry : sillyConvertorMap.entrySet()) {
                    SillyVariableConvertor<?> convertor = convertorEntry.getValue();
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
                throw new SillyException("保存流程节点表信息发生异常！");
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
            String activitiHandler = variable.getActivitiHandler();
            if (StringUtils.isEmpty(activitiHandler)) {
                continue;
            }

            final SillyVariableConvertor<?> handler = getSillyConvertor(activitiHandler);
            if (handler != null) {
                String vn = variable.getVariableName();
                String vt = variable.getVariableText();
                handler.convert(varMap, vn, vt);
            }
        }
        return varMap;
    }

    @Override
    public void changeUser(String taskId, String userId, String reason) {
        final SillyTask task = sillyEngineService.findTaskById(taskId);
        String assignee = task.getAssignee();
        String currentUserId = sillyCurrentUserUtil.currentUserId();
        SillyAssert.isTrue(Objects.equals(assignee, currentUserId) || sillyCurrentUserUtil.isAdmin(currentUserId),
                "您非当前任务责任人，任务不可流转");
        final String masterId = sillyEngineService.getBusinessKey(task.getProcessInstanceId());
        // 变更人员
        sillyEngineService.changeUser(taskId, userId);
        // 记录履历
        String handleInfo = this.sillyResumeService.makeResumeHandleInfo(SillyConstant.SillyResumeType.PROCESS_TYPE_FLOW, userId, task.getName(), reason);
        doSaveProcessResume(masterId, handleInfo, SillyConstant.SillyResumeType.PROCESS_TYPE_FLOW, task.getTaskDefinitionKey(), task.getName(), userId, null);
        // 获取任务处置人员
        final List<String> userIds = sillyEngineService.getTaskUserIds(task);
        final String joinUserIds = StringUtils.myJoin(userIds, SillyConstant.ARRAY_SPLIT_STR);
        // 更新主表信息
        final M master = sillyFactory.newMaster();
        master.setId(masterId);
        master.setHandleUserName(userIdsToName(joinUserIds));
        updateById(master);
    }


    @Override
    public void reject(String taskId, String nodeKey, String reason, String userId) {
        // 获取当前任务信息
        SillyTask task = sillyEngineService.findTaskById(taskId);
        // 获取主对象信息
        final String masterId = sillyEngineService.getBusinessKey(task.getProcessInstanceId());
        M master = sillyReadService.getMaster(masterId);
        SillyAssert.notNull(master, "主数据对象获取失败，驳回异常");
        // 流程引擎驳回操作
        List<? extends SillyTask> taskList = sillyEngineService.changeTask(task, nodeKey, userId);

        master.setTaskName(makeTaskName(taskList));
        // 下一步处置人 ID集合
        Set<String> nextUserIds = nextProcess(taskList);
        master.setHandleUserName(userIdsToName(nextUserIds));

        String userStr = StringUtils.myJoin(nextUserIds);
        // 记录履历
        String handleInfo = sillyResumeService.makeResumeHandleInfo(SillyConstant.SillyResumeType.PROCESS_TYPE_BACK, userStr, task.getName(), reason);
        SillyResume process = sillyFactory.newResume();
        process.setProcessType(SillyConstant.SillyResumeType.PROCESS_TYPE_BACK);
        process.setBusinessId(masterId);
        process.setBusinessType(usedCategory());
        process.setHandleInfo(handleInfo);
        process.setProcessNodeKey(task.getTaskDefinitionKey());
        process.setProcessNodeName(task.getName());
        process.setNextUserId(userStr);
        Long taskDueTime = sillyEngineService.getTaskDueTime(task);
        process.setConsumeTime(taskDueTime);
        // 插入流程履历
        sillyResumeService.insert(process);
        this.updateById(master);
    }

    /**
     * 删除主表及关闭工作流
     *
     * @param masterId
     * @param processInstanceId
     */
    @Override
    public void delete(String masterId, String processInstanceId) {
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
    @Override
    public void forceEndProcess(String processInstanceId) {
        sillyEngineService.deleteProcessInstance(processInstanceId, "流程删除");
    }


    public List<V> toVariableList(Map<String, Object> map) {
        return toVariableList(map, SillyConstant.ActivitiVariable.BELONG_VARIABLE, null);
    }

    public List<V> toVariableList(Map<String, Object> map, String belong) {
        return toVariableList(map, belong, null);
    }

    public List<V> toVariableList(Map<String, Object> map, String belong, String activitiHandler) {
        List<V> list = new ArrayList<>();
        for (String key : map.keySet()) {
            Object object = map.get(key);
            doVariableList(object, list, key, belong, activitiHandler);
        }
        return list;
    }

    protected void doVariableList(Object object, List<V> list, String key, String belong, String activitiHandler) {
        if (object == null || StringUtils.isEmpty(key)) {
            return;
        }

        if (object instanceof String) {
            String variableText = (String) object;
            doSetVariableList(list, key, variableText, SillyConstant.ActivitiNode.CONVERTOR_STRING, belong, activitiHandler, DefaultVariableSaveHandle.NAME);
        } else if (object instanceof Collection<?>) {
            Collection<?> objectList = (Collection<?>) object;
            StringJoiner variableTextSj = new StringJoiner(SillyConstant.ARRAY_SPLIT_STR);
            for (Object o : objectList) {
                if (o instanceof String) {
                    variableTextSj.add((String) o);
                } else {
                    throw new SillyException("不支持此数据集合内类型进行变量转换" + o.getClass());
                }
            }
            doSetVariableList(list, key, variableTextSj.toString(), SillyConstant.ActivitiNode.CONVERTOR_LIST, belong, activitiHandler, DefaultVariableSaveHandle.NAME);
        } else {
            throw new SillyException("不支持此数据类型进行变量转换" + object.getClass());
        }
    }

    private void doSetVariableList(List<V> list, String key, String variableText, String variableType, String belong, String activitiHandler, String saveHandleName) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(variableText)) {
            return;
        }

        paramToVariableList(list, key, variableText, variableType, belong, activitiHandler, saveHandleName);
    }

    protected void paramToVariableList(List<V> list, String key, String variableText, String variableType, String belong, String activitiHandler, String saveHandleName) {
        V v = sillyFactory.newVariable();
        v.setBelong(belong);
        v.setSaveHandleName(saveHandleName);
        v.setActivitiHandler(activitiHandler);
        v.setVariableName(key);
        v.setVariableText(variableText);
        v.setVariableType(variableType);
        list.add(v);
    }

}
