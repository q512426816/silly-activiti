/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-spring
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.spring.service;

import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.service.base.AbstractSillyReadService;

/**
 * 结合工作流Activiti的傻瓜读取服务
 *
 * @param <M>
 * @param <N>
 * @param <V>
 */
public abstract class EnhanceSillyReadService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyReadService<M, N, V> {

}
