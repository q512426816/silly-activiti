/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.base;

import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.property.SillyProcessProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.engine.SillyEngineService;
import com.iqiny.silly.core.group.SillyTaskCategoryGroup;
import com.iqiny.silly.core.resume.SillyResume;
import com.iqiny.silly.core.service.SillyReadService;
import com.iqiny.silly.core.service.SillyWriteService;

/**
 * 傻瓜配置信息
 */
public interface SillyProperties {

    String getProcessPattern();

    String getEntityScanPackage();

    Class<? extends SillyMaster> getMasterSuperType();

    Class<? extends SillyNode> getNodeSuperType();

    Class<? extends SillyVariable> getVariableSuperType();

    Class<? extends SillyResume> getResumeSuperType();

    Class<? extends SillyProcessProperty> getProcessPropertyClazz();

    Class<? extends SillyPropertyHandle> getPropertyHandleClazz();

    String[] getCategories();

    Class<? extends SillyReadService> getDefaultReadServiceClazz();

    Class<? extends SillyWriteService> getDefaultWriteServiceClazz();

    Class<? extends SillyEngineService> getDefaultEngineServiceClazz();

    Class<? extends SillyTaskCategoryGroup> getDefaultTaskCategoryGroupClazz();

}
