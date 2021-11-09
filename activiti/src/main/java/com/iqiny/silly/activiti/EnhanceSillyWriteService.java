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
import com.iqiny.silly.core.config.property.*;
import com.iqiny.silly.core.config.property.impl.DefaultVariableSaveHandle;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.service.base.AbstractSillyWriteService;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.config.CacheManagementConfigUtils;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class EnhanceSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyWriteService<M, N, V, Task> {

    @Override
    protected void afterStartProcess(M master, N node, Task task) {
        final M copyMaster = sillyFactory.newMaster();
        BeanUtils.copyProperties(master, copyMaster);
        if (StringUtils.isEmpty(master.getStatus())) {
            copyMaster.setStatus(master.startStatus());
        }
        if (StringUtils.isEmpty(master.getTaskName())) {
            copyMaster.setTaskName(task.getName());
        }

        if (StringUtils.isEmpty(node.getTaskId())) {
            node.setTaskId(task.getId());
        }
        if (StringUtils.isEmpty(node.getNodeKey())) {
            node.setNodeKey(task.getTaskDefinitionKey());
        }
        if (StringUtils.isEmpty(node.getNodeName())) {
            node.setNodeName(task.getName());
        }

        copyMaster.setStartDate(new Date());
        copyMaster.setStartUserId(currentUserUtil.currentUserId());
        updateHandleUserName(copyMaster);
    }

    public void updateHandleUserName(M master) {
        String processId = master.getProcessId();
        List<Task> taskList = sillyEngineService.findTaskByProcessInstanceId(processId);
        master.setHandleUserName(this.userIdsToName(this.nextProcess(taskList)));
        boolean saveFlag = updateById(master);
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
        final String joinNextUserIds = StringUtils.myJoin(nextUserIds);
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
            master.setTaskName(StringUtils.myJoin(taskNames, SillyConstant.ARRAY_SPLIT_STR));
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
        return StringUtils.myJoin(userNames, SillyConstant.ARRAY_SPLIT_STR);
    }

    protected String userIdsToName(Set<String> userIds) {
        if (userIds == null) {
            return null;
        }

        Set<String> userNames = new LinkedHashSet<>();
        for (String userId : userIds) {
            userNames.add(currentUserUtil.userIdToName(userId));
        }
        return StringUtils.myJoin(userNames, SillyConstant.ARRAY_SPLIT_STR);
    }

    protected Set<String> nextProcess(List<Task> taskList) {
        Set<String> userIds = new LinkedHashSet<>();
        for (Task task1 : taskList) {
            final List<String> taskUserIds = sillyEngineService.getTaskUserIds(task1);
            if (taskUserIds != null && !taskUserIds.isEmpty()) {
                userIds.addAll(taskUserIds);
            }
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
        final String joinUserIds = StringUtils.myJoin(userIds, SillyConstant.ARRAY_SPLIT_STR);
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

    protected M saveData(String taskId, Map<String, Object> saveMap) {
        return saveData(false, taskId, saveMap);
    }

    protected M submitData(String taskId, Map<String, Object> saveMap) {
        return saveData(true, taskId, saveMap);
    }

    public String processKeyMapKey() {
        return "processKey";
    }

    public String nodeKeyMapKey() {
        return "nodeKey";
    }

    public String masterIdMapKey() {
        return "id";
    }

    public String submitKey() {
        return "submit";
    }

    public String taskIdKey() {
        return "taskId";
    }

    protected M saveData(boolean submit, String taskId, Map<String, Object> saveMap) {
        String masterIdMapKey = masterIdMapKey();
        String masterId = MapUtils.getString(saveMap, masterIdMapKey);
        saveMap.remove(masterIdMapKey);
        String processKey = null;
        String nodeKey = null;
        if (StringUtils.isEmpty(taskId)) {
            // 从Map 中获取 processKey ， nodeKey
            SillyPropertyHandle propertyHandle = getSillyPropertyHandle(masterId, saveMap);
            String processKeyMapKey = propertyHandle.getStringValue(processKeyMapKey());
            String nodeKeyMapKey = propertyHandle.getStringValue(nodeKeyMapKey());
            String lastProcessKey = propertyHandle.getStringValue(processProperty().getLastProcessKey());
            String firstNodeKey = propertyHandle.getStringValue(processProperty().getFirstNodeKey());
            processKey = MapUtils.getString(saveMap, processKeyMapKey, lastProcessKey);
            nodeKey = MapUtils.getString(saveMap, nodeKeyMapKey, firstNodeKey);
            saveMap.remove(processKeyMapKey);
            saveMap.remove(nodeKeyMapKey);
        } else {
            Task task = sillyEngineService.findTaskById(taskId);
            SillyAssert.notNull(task, "任务未找到" + taskId);
            if (StringUtils.isEmpty(masterId)) {
                masterId = sillyEngineService.getBusinessKey(task.getProcessInstanceId());
                SillyAssert.notEmpty(masterId, "根据任务获取 masterId 失败 " + taskId);
            }
            M master = sillyReadService.getMaster(masterId);
            SillyAssert.notNull(master, "根据 masterId 获取数据失败 " + masterId);
            processKey = master.processKey();
            nodeKey = task.getTaskDefinitionKey();
        }

        return saveData(submit, taskId, masterId, processKey, nodeKey, saveMap);
    }

    protected M saveData(boolean submit, String taskId, String masterId, String processKey, String nodeKey, Map<String, Object> saveMap) {
        SillyProcessMasterProperty<?> masterProperty = getMasterProperty(processKey);
        SillyProcessNodeProperty<?> nodeProperty = getNodeProperty(processKey, nodeKey);

        SillyAssert.notNull(masterProperty, "masterProperty 获取失败" + processKey);
        SillyAssert.notNull(nodeProperty, "nodeProperty 获取失败" + nodeKey);

        saveMap.put(submitKey(), submit);
        saveMap.put(masterIdMapKey(), masterId);
        SillyPropertyHandle propertyHandle = getSillyPropertyHandle(masterId, saveMap);
        saveMap.remove(submitKey());
        saveMap.remove(masterIdMapKey());

        List<V> vs = mapToVariables(propertyHandle, saveMap, nodeProperty);
        M m = makeMasterByVariables(vs);
        m.setProcessKey(masterProperty.getProcessKey());
        m.setProcessVersion(masterProperty.getProcessVersion());
        m.setId(masterId);
        N n = makeNodeByVariables(vs);
        n.setMasterId(masterId);
        n.setTaskId(taskId);
        n.setNodeKey(nodeProperty.getNodeKey());
        n.setNodeName(nodeProperty.getNodeName());
        n.setParallelFlag(nodeProperty.isParallel() ? SillyConstant.YesOrNo.YES : SillyConstant.YesOrNo.NO);

        List<V> saveV = variableSaveHandle(m, n, vs, propertyHandle);
        n.setVariableList(saveV);

        if (submit) {
            submit(m, n);
        } else {
            save(m, n);
        }
        // 更新 ROOT 数据
        updatePropertyHandleRoot(m.getId(), propertyHandle.getValues());
        return m;
    }

    protected List<V> variableSaveHandle(M m, N n, List<V> vs, SillyPropertyHandle propertyHandle) {
        List<V> needSaveList = new ArrayList<>();
        for (V v : vs) {
            boolean needSaveFlag = batchSaveHandle(m, n, v, propertyHandle);
            if (needSaveFlag) {
                needSaveList.add(v);
            }
        }
        return needSaveList;
    }

    @Override
    protected Object getPropertyHandleRoot(String masterId) {
        Object root = getPropertyHandleRootCache(masterId);
        if (root == null) {
            root = getPropertyHandleRootDB(masterId);
        }
        return root;
    }

    protected Object getPropertyHandleRootDB(String masterId) {
        List<V> nodeList = sillyReadService.getVariableList(masterId, null);
        return sillyReadService.variableList2Map(nodeList);
    }

    protected abstract Object getPropertyHandleRootCache(String masterId);

    protected void updatePropertyHandleRoot(String masterId, Object updateValue) {
        updatePropertyHandleRootCache(masterId, updateValue);
    }

    protected void updatePropertyHandleRootCache(String masterId, Object updateValue) {
        if (updateValue == null) {
            return;
        }

        Object propertyHandleRoot = getPropertyHandleRoot(masterId);
        if (propertyHandleRoot == null) {
            propertyHandleRoot = new LinkedHashMap<>();
        }
        if (propertyHandleRoot instanceof Map && updateValue instanceof Map) {
            Map rootMap = (Map) propertyHandleRoot;
            rootMap.putAll((Map) updateValue);
            doUpdatePropertyHandleRootCache(masterId, rootMap);
        } else {
            throw new SillyException("不支持此数据类型的ROOT值更新" + propertyHandleRoot.getClass());
        }
    }

    protected abstract void doUpdatePropertyHandleRootCache(String masterId, Map<String, Object> saveMap);

    /**
     * Map 根据配置对象转 Variable 对象集合
     *
     * @param map
     * @return
     */
    public List<V> mapToVariables(SillyPropertyHandle sillyPropertyHandle, Map<String, Object> map, SillyProcessNodeProperty<?> nodeProperty) {
        SillyAssert.notNull(nodeProperty, "节点配置不可为空");

        List<V> list = new ArrayList<>();
        Map<String, ? extends SillyProcessVariableProperty> variableMap = nodeProperty.getVariable();
        Set<String> keySet = variableMap.keySet();
        for (String vKey : keySet) {
            SillyProcessVariableProperty variableProperty = variableMap.get(vKey);
            Object variableObj = map.remove(vKey);
            Object defaultObject = sillyPropertyHandle.getValue(variableProperty.getDefaultText());
            String defaultText = object2String(defaultObject, null);
            String variableText = object2String(variableObj, defaultText);
            if (StringUtils.isEmpty(variableText)) {
                if (variableProperty.isRequest() && sillyPropertyHandle.getBooleanValue(variableProperty.getRequestEl())) {
                    throw new SillyException("此参数值不可为空" + vKey);
                }
                continue;
            }

            if (variableProperty.isUpdatePropertyHandleValue()) {
                sillyPropertyHandle.updateValue(vKey, variableText);
            }

            String variableName = sillyPropertyHandle.getStringValue(variableProperty.getVariableName());
            String variableType = sillyPropertyHandle.getStringValue(variableProperty.getVariableType());
            String belong = sillyPropertyHandle.getStringValue(variableProperty.getBelong());
            String activitiHandler = sillyPropertyHandle.getStringValue(variableProperty.getActivitiHandler());
            String saveHandleName = StringUtils.join(variableProperty.getSaveHandleNames(), SillyConstant.ARRAY_SPLIT_STR);
            paramToVariableList(list,
                    variableName,
                    variableText,
                    variableType,
                    belong,
                    activitiHandler,
                    saveHandleName
            );
        }

        keySet = map.keySet();
        for (String key : keySet) {
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

    protected String object2String(Object variableObj, String defaultStr) {
        String variableText = defaultStr;
        if (variableObj != null) {
            if (variableObj instanceof Collection) {
                Collection<Object> c = (Collection<Object>) variableObj;
                if (c.isEmpty()) {
                    return variableText;
                }

                Object next = c.iterator().next();
                if (next instanceof String) {
                    variableText = StringUtils.myJoin((Collection<String>) variableObj);
                } else {
                    variableText = JSON.toJSONString(variableObj);
                }
            } else {
                if (variableObj instanceof String) {
                    variableText = (String) variableObj;
                } else {
                    variableText = JSON.toJSONString(variableObj);
                }
            }
        }

        return variableText;
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
        return JSON.parseObject(masterJson, masterClass());
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
        return JSON.parseObject(nodeJson, nodeClass());
    }

}
