/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle;

import com.iqiny.silly.core.base.SillyOrdered;

/**
 * 傻瓜节点保存处理器
 */
public interface SillyNodeSaveHandle extends SillyOrdered {

    /**
     * 处理器名称
     */
    String name();

    /**
     * 处置执行具体内容
     *
     * @param sourceData 当前节点提交的数据
     * @return 下一个处置器
     */
    SillyNodeSaveHandle handle(SillyNodeSourceData sourceData);

}
