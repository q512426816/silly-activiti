/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.group;

import java.util.List;

public interface SillyTaskGroupFactory {

    SillyTaskGroup getSillyTaskGroup(String groupId);

    List<SillyTaskGroup> getAllSillyTaskGroup();

}
