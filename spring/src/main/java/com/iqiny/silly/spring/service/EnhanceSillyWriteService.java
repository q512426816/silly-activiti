/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.service;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.service.base.AbstractSillyWriteService;
import org.apache.commons.collections.MapUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class EnhanceSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyWriteService<M, N, V> {

    @Transactional(rollbackFor = Exception.class)
    public M saveFirstData(boolean submit, Map<String, Object> saveMap) {
        return saveData(submit, null, saveMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public M saveTaskMap(Map<String, Object> saveMap) {
        String taskId = MapUtils.getString(saveMap, taskIdKey());
        Boolean submit = MapUtils.getBoolean(saveMap, submitKey(), false);
        SillyAssert.notEmpty(taskId, "任务ID不可为空");
        saveMap.remove(taskIdKey());
        saveMap.remove(submitKey());
        return saveData(submit, taskId, saveMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public M saveOrNewMap(Map<String, Object> saveMap) {
        String taskId = MapUtils.getString(saveMap, taskIdKey());
        Boolean submit = MapUtils.getBoolean(saveMap, submitKey(), false);
        saveMap.remove(taskIdKey());
        saveMap.remove(submitKey());
        return saveData(submit, taskId, saveMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUser(String taskId, String userId, String reason) {
        super.changeUser(taskId, userId, reason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String masterId, String processInstanceId) {
        super.delete(masterId, processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forceEndProcess(String processInstanceId) {
        super.forceEndProcess(processInstanceId);
    }

    @Transactional(rollbackFor = Exception.class)
    public M saveData(String taskId, Map<String, Object> saveMap) {
        return saveData(false, taskId, saveMap);
    }

    @Transactional(rollbackFor = Exception.class)
    public M submitData(String taskId, Map<String, Object> saveMap) {
        return saveData(true, taskId, saveMap);
    }

}
