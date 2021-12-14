/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service.base;

import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.savehandle.SillyNodeSaveHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.service.SillyWriteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * 内部 傻瓜写入服务
 */
public abstract class InternalSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyService<M, N, V> implements SillyWriteService<M, N, V> {

    private final static Log log = LogFactory.getLog(InternalSillyWriteService.class);

    protected M saveData(boolean submit, String taskId, Map<String, Object> saveMap) {
        // 是否启动流程 （taskId存在 必定不启动流程，submit = true 启动流程）
        boolean isStartProcess = StringUtils.isEmpty(taskId) && SillyMapUtils.getBooleanValue(saveMap, startProcessKey(), submit);
        saveMap.put(submitKey(), submit);
        saveMap.put(taskIdKey(), taskId);
        saveMap.put(startProcessKey(), isStartProcess);

        // 其他类型归属处置
        SillyNodeSourceData sourceData = new SillyNodeSourceData(usedCategory(), saveMap);
        return saveData(sourceData);
    }

    protected M saveData(SillyNodeSourceData sourceData) {
        SillyNodeSaveHandle nodeSaveHandle = sillyContext.getNextBean(null, usedCategory(), SillyNodeSaveHandle.class);
        while (nodeSaveHandle != null) {
            nodeSaveHandle = nodeSaveHandle.handle(sourceData);
        }

        M master = (M) sourceData.getMaster();
        log.info("masterId:" + master.getId() + " taskId:" + sourceData.taskId()
                + " 数据执行链信息 ==>" + sourceData.handleLinkNameLog());

        return master;
    }

}
