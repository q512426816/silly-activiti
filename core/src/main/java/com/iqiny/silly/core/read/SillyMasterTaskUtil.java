/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.read;

import com.iqiny.silly.core.base.SillyMasterTask;

import java.util.*;

public class SillyMasterTaskUtil<S extends SillyMasterTask> {

    private List<S> masterTaskList;
    /**
     * MASTER_ID  => MASTER_TASK
     */
    private Map<String, Set<S>> masterTaskMap;

    public static SillyMasterTaskUtil<? extends SillyMasterTask> create(List<? extends SillyMasterTask> masterTasks) {
        SillyMasterTaskUtil taskUtil = new SillyMasterTaskUtil();
        taskUtil.setMasterTaskList(masterTasks);
        return taskUtil;
    }

    public List<S> getMasterTaskList() {
        return masterTaskList;
    }

    public void setMasterTaskList(List<S> masterTaskList) {
        this.masterTaskList = masterTaskList;
    }

    public List<String> getMasterIds() {
        if (masterTaskList == null) {
            return new ArrayList<>();
        }
        Set<String> set = new LinkedHashSet<>();
        for (S masterTask : masterTaskList) {
            if (masterTask != null) {
                set.add(masterTask.getMasterId());
            }
        }
        return new ArrayList<>(set);
    }

    private void init() {
        masterTaskMap = new LinkedHashMap<>();
        for (S masterTask : masterTaskList) {
            if (masterTask == null) {
                continue;
            }
            final String masterId = masterTask.getMasterId();
            final Set<S> taskIds = masterTaskMap.getOrDefault(masterId, new LinkedHashSet<>());
            taskIds.add(masterTask);
            masterTaskMap.putIfAbsent(masterId, taskIds);
        }
    }

    public S getOneTask(String masterId) {
        if (masterTaskMap == null) {
            init();
        }
        final Set<S> tasks = masterTaskMap.get(masterId);
        if (tasks != null && !tasks.isEmpty()) {
            return tasks.iterator().next();
        }
        return null;
    }

    public Set<S> getTaskSet(String masterId) {
        if (masterTaskMap == null) {
            init();
        }
        return masterTaskMap.get(masterId);
    }
}
