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
import com.iqiny.silly.core.resume.SillyResume;

/**
 * 傻瓜工厂 创建 相同一族的傻瓜
 *
 * @param <M>
 * @param <N>
 * @param <V>
 * @author QINY
 * @since 1.0
 */
public interface SillyFactory<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable, R extends SillyResume> extends SillyCategory {

    /**
     * 创建 主对象
     *
     * @return 主对象
     */
    M newMaster();

    Class<M> masterClass();

    /**
     * 创建 节点对象
     *
     * @return 节点对象
     */
    N newNode();

    Class<N> nodeClass();

    /**
     * 创建 变量对象
     *
     * @return 变量对象
     */
    V newVariable();

    Class<V> variableClass();

    /**
     * 创建 流程履历对象
     *
     * @return 流程履历对象
     */
    R newResume();

    Class<R> resumeClass();

}
