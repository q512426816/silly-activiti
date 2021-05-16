/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.read;

import java.util.*;

public class SillyMasterTaskUtil {

    public static SillyMasterTaskUtil create(List<MySillyMasterTask> masterTaskList) {
        final SillyMasterTaskUtil sillyMasterTaskUtil = new SillyMasterTaskUtil();
        sillyMasterTaskUtil.setMasterTaskList(masterTaskList);
        return sillyMasterTaskUtil;
    }

    private List<MySillyMasterTask> masterTaskList;
    private Map<String, Set<String>> masterTaskMap;

    public List<MySillyMasterTask> getMasterTaskList() {
        return masterTaskList;
    }

    public void setMasterTaskList(List<MySillyMasterTask> masterTaskList) {
        this.masterTaskList = masterTaskList;
    }

    public List<String> getMasterIds() {
        if (masterTaskList == null) {
            return new ArrayList<>();
        }
        Set<String> set = new LinkedHashSet<>();
        for (MySillyMasterTask masterTask : masterTaskList) {
            if (masterTask != null) {
                set.add(masterTask.getMasterId());
            }
        }
        return new ArrayList<>(set);
    }

    private void init() {
        masterTaskMap = new LinkedHashMap<>();
        for (MySillyMasterTask masterTask : masterTaskList) {
            if (masterTask == null) {
                continue;
            }
            final String masterId = masterTask.getMasterId();
            final Set<String> taskIds = masterTaskMap.getOrDefault(masterId, new LinkedHashSet<>());
            taskIds.add(masterTask.getTaskId());
            masterTaskMap.putIfAbsent(masterId, taskIds);
        }
    }

    public String getOneTaskId(String masterId) {
        if (masterTaskMap == null) {
            init();
        }
        final Set<String> taskIds = masterTaskMap.get(masterId);
        if (taskIds != null && !taskIds.isEmpty()) {
            return taskIds.iterator().next();
        }
        return null;
    }
}
