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
 * 默认执行顺序：
 * 1. SillyMapToVarSaveHandle.class
 * 2. SillyFieldCheckSaveHandle.class
 * 3. SillyMasterSaveHandle.class
 * 4. SillyNodeDataSaveHandle.class
 * 5. SillyNodeVariableDataSaveHandle.class
 * 6. SillyNodeVariableExecuteSaveHandle.class
 * 7. SillyProcessMapSaveHandle.class
 * 8. SillyProcessStartSaveHandle.class
 * 9. SillyProcessSubmitSaveHandle.class
 * 10. SillyAfterCompleteSaveHandle.class
 * 11. SillyAfterCloseSaveHandle.class
 * 12. SillyResumeCreateSaveHandle.class
 * 13. SillyResumeRecordSaveHandle.class
 * 14. SillyMasterUpdateSaveHandle.class
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
