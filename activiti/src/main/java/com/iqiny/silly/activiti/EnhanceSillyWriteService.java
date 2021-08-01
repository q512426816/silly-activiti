/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-activiti 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.activiti;

import com.alibaba.fastjson.JSON;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyProcessVariableProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.service.base.AbstractSillyWriteService;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class EnhanceSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyWriteService<M, N, V, Task> {

    @Override
    protected void afterStartProcess(M master, Task task) {
        final M copyMaster = sillyFactory.newMaster();
        BeanUtils.copyProperties(master, copyMaster);
        if (StringUtils.isEmpty(master.getStatus())) {
            copyMaster.setStatus(master.startStatus());
        }
        if (StringUtils.isEmpty(master.getTaskName())) {
            copyMaster.setTaskName(task.getName());
        }

        copyMaster.setStartDate(new Date());
        copyMaster.setStartUserId(currentUserUtil.currentUserId());
        final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task);
        final String joinNextUserIds = StringUtils.join(taskUserIds);
        copyMaster.setHandleUserName(userIdsToName(joinNextUserIds));
        boolean saveFlag = updateById(copyMaster);
        if (!saveFlag) {
            throw new SillyException("主信息更新发生异常！");
        }
    }

    @Override
    protected List<Task> completeTask(M master, N node, Map<String, Object> varMap) {
        SillyAssert.notEmpty(node.getTaskId(), "完成任务操作，任务ID不可为空！");
        Task task = sillyEngineService.findTaskById(node.getTaskId());
        SillyAssert.notNull(task, "任务获取失败！");
        complete(varMap, task);
        return afterCompleteTask(master, node, task);
    }

    /**
     * 任务完成之后的回调
     *
     * @param master
     * @param node
     * @return
     */
    protected List<Task> afterCompleteTask(M master, N node, Task oldTask) {
        final String actProcessId = oldTask.getProcessInstanceId();
        // 判断是否结束任务了
        List<Task> taskList = sillyEngineService.findTaskByProcessInstanceId(actProcessId);
        if (taskList.isEmpty()) {
            afterCloseProcess(master, node);
        } else {
            // 下一步任务记录
            afterCompleteProcess(master, node, taskList);
        }
        return taskList;
    }

    @Override
    protected void saveProcessResume(N node, Task oldTask, String nextTaskId) {
        final Task task = sillyEngineService.findTaskById(nextTaskId);
        saveProcessResume(node, oldTask, Collections.singletonList(task));
    }

    @Override
    protected void saveProcessResume(N node, Task oldTask, List<Task> nextTaskList) {
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
        final String joinNextUserIds = StringUtils.join(nextUserIds);
        // 履历内容
        String handleInfo = sillyResumeService.makeResumeHandleInfo(handleType, joinNextUserIds, taskName, node.getNodeInfo());

        final String masterId = node.getMasterId();
        doSaveProcessResume(masterId, handleInfo, handleType, taskKey, taskName, joinNextUserIds, dueTime);
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
     *
     * @param masterId
     * @return
     */
    protected String processResumeBusinessId(String masterId) {
        // 默认使用本身作为流程履历业务ID
        return masterId;
    }


    /**
     * 完成流程任务  (主表状态只控制 新建（保存）/提交（提交）/完成（结束）  更多状态请使用 activiti事件控制)
     *
     * @param variableMap
     * @param task
     * @return
     */
    protected void complete(Map<String, Object> variableMap, Task task) {
        // 完成此步骤流程
        sillyEngineService.complete(task, currentUserUtil.currentUserId(), variableMap);
    }

    /**
     * 强制结束流程
     *
     * @param processInstanceId
     */
    @Override
    protected void forceEndProcess(String processInstanceId) {
        sillyEngineService.deleteProcessInstance(processInstanceId, "流程删除");
    }

    /**
     * 获取任务执行耗时
     *
     * @param task
     * @return
     */
    protected Long taskDueTime(Task task) {
        return sillyEngineService.getTaskDueTime(task);
    }

    /**
     * 流程结束之后的回调
     *
     * @param master
     * @param node
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
            master.setCloseUserId(currentUserUtil.currentUserId());
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
     *
     * @param master
     * @param node
     * @param taskList
     */
    protected void afterCompleteProcess(M master, N node, List<Task> taskList) {
        String masterId = master.getId();
        if (masterId == null) {
            throw new SillyException("通过任务流程实例ID获取主表数据ID异常！");
        }

        boolean updateFlag = false;
        if (StringUtils.isEmpty(master.getTaskName())) {
            Set<String> taskNames = new LinkedHashSet<>();
            for (Task task : taskList) {
                taskNames.add(task.getName());
            }
            master.setTaskName(StringUtils.join(taskNames, SillyConstant.ARRAY_SPLIT_STR));
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

    protected String userIdsToName(String userIds) {
        if (StringUtils.isEmpty(userIds)) {
            return null;
        }

        final String[] split = userIds.split(SillyConstant.ARRAY_SPLIT_STR);
        Set<String> userNames = new LinkedHashSet<>();
        for (String userId : split) {
            userNames.add(currentUserUtil.userIdToName(userId));
        }
        return StringUtils.join(userNames, SillyConstant.ARRAY_SPLIT_STR);
    }

    protected String userIdsToName(Set<String> userIds) {
        if (userIds == null) {
            return null;
        }

        Set<String> userNames = new LinkedHashSet<>();
        for (String userId : userIds) {
            userNames.add(currentUserUtil.userIdToName(userId));
        }
        return StringUtils.join(userNames, SillyConstant.ARRAY_SPLIT_STR);
    }

    protected Set<String> nextProcess(List<Task> taskList) {
        Set<String> userIds = new LinkedHashSet<>();
        for (Task task1 : taskList) {
            final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task1);
            if (taskUserIds == null || taskUserIds.isEmpty()) {
                throw new SillyException("下一步任务处置人不可为空！");
            }
            userIds.addAll(taskUserIds);
        }
        return userIds;
    }

    /**
     * 人员流转
     */
    protected void changeUser(String taskId, String userId, String reason) {
        final Task task = sillyEngineService.findTaskById(taskId);
        final String masterId = sillyEngineService.getBusinessKey(task.getProcessInstanceId());
        // 变更人员
        sillyEngineService.changeUser(taskId, userId);
        // 获取任务处置人员
        final List<String> userIds = sillyEngineService.getTaskUserIds(task);
        final String joinUserIds = StringUtils.join(userIds, SillyConstant.ARRAY_SPLIT_STR);
        // 记录履历
        String handleInfo = this.sillyResumeService.makeResumeHandleInfo(SillyConstant.SillyResumeType.PROCESS_TYPE_FLOW, joinUserIds, task.getName(), reason);
        doSaveProcessResume(masterId, handleInfo, SillyConstant.SillyResumeType.PROCESS_TYPE_FLOW, task.getTaskDefinitionKey(), task.getName(), joinUserIds, null);
        // 更新主表信息
        final M master = sillyFactory.newMaster();
        master.setId(masterId);
        master.setHandleUserName(userIdsToName(joinUserIds));
        updateById(master);
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
            doSetVariableList(list, key, variableText, SillyConstant.ActivitiNode.CONVERTOR_STRING, belong, activitiHandler);
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
            doSetVariableList(list, key, variableTextSj.toString(), SillyConstant.ActivitiNode.CONVERTOR_LIST, belong, activitiHandler);
        } else {
            throw new SillyException("不支持此数据类型进行变量转换" + object.getClass());
        }
    }

    private void doSetVariableList(List<V> list, String key, String variableText, String variableType, String belong, String activitiHandler) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(variableText)) {
            return;
        }

        paramToVariableList(list, key, variableText, variableType, belong, activitiHandler);
    }

    protected void paramToVariableList(List<V> list, String key, String variableText, String variableType, String belong, String activitiHandler) {
        V v = sillyFactory.newVariable();
        v.setBelong(belong);
        v.setActivitiHandler(activitiHandler);
        v.setVariableName(key);
        v.setVariableText(variableText);
        v.setVariableType(variableType);
        list.add(v);
    }

    protected void saveData(String taskId, Map<String, Object> saveMap) {
        saveData(false, taskId, saveMap);
    }

    protected void submitData(String taskId, Map<String, Object> saveMap) {
        saveData(true, taskId, saveMap);
    }

    protected void saveData(boolean submit, String taskId, Map<String, Object> saveMap) {
        Task task = sillyEngineService.findTaskById(taskId);
        SillyAssert.notNull(task, "任务未找到" + taskId);
        SillyProcessNodeProperty<?> nodeProperty = getNodeProperty(task);
        String masterId = sillyEngineService.getBusinessKey(task.getProcessInstanceId());
        List<V> vs = mapToVariables(saveMap, nodeProperty);
        M m = makeMasterByVariables(vs);
        m.setId(masterId);
        N n = makeNodeByVariables(vs);
        n.setVariableList(vs);
        n.setMasterId(masterId);
        n.setTaskId(taskId);
        n.setParallelFlag(nodeProperty.isParallel() ? SillyConstant.YesOrNo.YES : SillyConstant.YesOrNo.NO);


        if (submit) {
            submit(m, n);
        } else {
            save(m, n);
        }
    }

    /**
     * Map 根据配置对象转 Variable 对象集合
     *
     * @param map
     * @return
     */
    public List<V> mapToVariables(Map<String, Object> map, SillyProcessNodeProperty<?> nodeProperty) {
        SillyAssert.notNull(nodeProperty, "节点配置不可为空");

        SillyPropertyHandle sillyPropertyHandle = getSillyConfig().getSillyPropertyHandle();
        sillyPropertyHandle.setValues(map);

        List<V> list = new ArrayList<>();
        Map<String, ? extends SillyProcessVariableProperty> variableMap = nodeProperty.getVariable();
        for (String vKey : variableMap.keySet()) {
            SillyProcessVariableProperty variableProperty = variableMap.get(vKey);
            Object variableObj = map.remove(vKey);
            String variableText = sillyPropertyHandle.getStringValue(variableProperty.getDefaultText());
            if (variableObj != null) {
                variableText = variableObj.toString();
            }

            if (StringUtils.isEmpty(variableText)) {
                if (variableProperty.isRequest()) {
                    throw new SillyException("此参数值不可为空" + vKey);
                }
                continue;
            }

            paramToVariableList(list,
                    variableProperty.getVariableName(),
                    variableText,
                    variableProperty.getVariableType(),
                    variableProperty.getBelong(),
                    variableProperty.getActivitiHandler()
            );
        }

        for (String key : map.keySet()) {
            if (nodeProperty.ignoreField(key)) {
                continue;
            }

            if (!nodeProperty.isAllowOtherVariable()) {
                if (nodeProperty.isOtherVariableThrowException()) {
                    throw new SillyException("不允许保存此额外变量数据" + key);
                } else {
                    continue;
                }

            }

            doVariableList(map.get(key), list, key, SillyConstant.ActivitiVariable.BELONG_VARIABLE, null);
        }


        return list;
    }


    public SillyProcessNodeProperty<?> getNodeProperty(Task task) {
        String processKey = sillyEngineService.getActKeyNameByProcessInstanceId(task.getProcessInstanceId());
        String nodeKey = task.getTaskDefinitionKey();
        return getNodeProperty(processKey, nodeKey);
    }

    public M makeMasterByVariables(List<V> vList) {
        Map<String, Object> masterMap = new HashMap<>();
        for (V v : vList) {
            String name = v.getVariableName();
            String value = v.getVariableText();

            if (Objects.equals(v.getBelong(), SillyConstant.ActivitiVariable.BELONG_MASTER)) {
                masterMap.put(name, value);
            }
        }

        String masterJson = JSON.toJSONString(masterMap);
        return (M) JSON.parseObject(masterJson, sillyFactory.newMaster().getClass());
    }

    public N makeNodeByVariables(List<V> vList) {
        Map<String, Object> nodeMap = new HashMap<>();
        for (V v : vList) {
            String name = v.getVariableName();
            String value = v.getVariableText();

            if (Objects.equals(v.getBelong(), SillyConstant.ActivitiVariable.BELONG_NODE)) {
                nodeMap.put(name, value);
            }
        }

        String nodeJson = JSON.toJSONString(nodeMap);
        return (N) JSON.parseObject(nodeJson, sillyFactory.newNode().getClass());
    }

}
