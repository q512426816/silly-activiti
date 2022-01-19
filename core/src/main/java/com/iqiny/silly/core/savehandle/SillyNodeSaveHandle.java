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
 * <p>
 * 默认执行顺序： （内置 29 个）
 * <p>
 * <p>
 * // 加载 任务及主表数据
 *
 * @see com.iqiny.silly.core.savehandle.node.SillyLoadNowTaskSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyLoadMasterByNewSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyLoadMasterByIdSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyLoadMasterByTaskSaveHandle
 * <p>
 * // 生成参数读取处理器
 * @see com.iqiny.silly.core.savehandle.node.SillyPropertyHandleCreateSaveHandle
 * <p>
 * // 获取节点配置信息
 * @see com.iqiny.silly.core.savehandle.node.SillyLoadNodePropertyByTaskSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyLoadNodePropertyByNotTaskSaveHandle
 * <p>
 * // 创建节点对象信息
 * @see com.iqiny.silly.core.savehandle.node.SillyLoadNodeInfoSaveHandle
 * <p>
 * // 提交对象Map ->转-> 数据集合varList
 * @see com.iqiny.silly.core.savehandle.node.SillyMapToVarSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyCheckVariableFieldsSaveHandle
 * <p>
 * // 生成流程变量数据
 * @see com.iqiny.silly.core.savehandle.node.SillyVarToProcessMapSaveHandle
 * <p>
 * // 启动流程
 * @see com.iqiny.silly.core.savehandle.node.SillyProcessStartSaveHandle
 * <p>
 * // 根据varList 生成 主对象属性  节点对象属性
 * @see com.iqiny.silly.core.savehandle.node.SillyVarToMasterSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyVarToNodeSaveHandle
 * <p>
 * // 节点数据 及 变量数据 转 历史数据
 * @see com.iqiny.silly.core.savehandle.node.SillyVariableToHistorySaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyNodeToHistorySaveHandle
 * <p>
 * // 节点数据 保存
 * @see com.iqiny.silly.core.savehandle.node.SillyNodeInsertSaveHandle
 * <p>
 * // 节点变量数据设置 及 变量 variableSaveHandle 执行 (变量数量 可能变少)
 * @see com.iqiny.silly.core.savehandle.node.SillyNodeVariableHandleSaveHandle
 * <p>
 * // 节点变量数据设置 及 变量 variableConvertor 执行  (变量数量 可能变多)
 * @see com.iqiny.silly.core.savehandle.node.SillyNodeVariableConvertorSaveHandle
 * <p>
 * // 节点变量数据 保存
 * @see com.iqiny.silly.core.savehandle.node.SillyNodeVariableInsertSaveHandle
 * <p>
 * // 流程提交
 * @see com.iqiny.silly.core.savehandle.node.SillyProcessSubmitSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyAfterCompleteSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyAfterCloseSaveHandle
 * <p>
 * // 履历记录
 * @see com.iqiny.silly.core.savehandle.node.SillyResumeCreateSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyResumeRecordSaveHandle
 * <p>
 * // 更新主表 及 root 信息
 * @see com.iqiny.silly.core.savehandle.node.SillyMasterUpdateSaveHandle
 * @see com.iqiny.silly.core.savehandle.node.SillyUpdateCachePropertyHandleRootSaveHandle
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
