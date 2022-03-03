/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service.base;

import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.SillyMapUtils;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSaveHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;
import com.iqiny.silly.core.savehandle.node.*;
import com.iqiny.silly.core.service.SillyWriteService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 内部 傻瓜写入服务
 */
public abstract class InternalSillyWriteService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable>
        extends AbstractSillyService<M, N, V> implements SillyWriteService<M, N, V> {

    private final static Log log = LogFactory.getLog(InternalSillyWriteService.class);

    public static final List<String> INIT_SOURCE_DATA_HANDLE_NAMES = Arrays.asList(SillyLoadNowTaskSaveHandle.NAME,
            SillyLoadMasterByIdSaveHandle.NAME,
            SillyLoadMasterByTaskSaveHandle.NAME,
            SillyLoadMasterByNewSaveHandle.NAME,
            SillyPropertyHandleCreateSaveHandle.NAME,
            SillyLoadNodePropertyByTaskSaveHandle.NAME,
            SillyLoadNodePropertyByNotTaskSaveHandle.NAME);


    protected M saveData(boolean submit, String taskId, Map<String, Object> saveMap) {
        // 是否启动流程 （taskId存在 必定不启动流程，submit = true 启动流程）
        boolean isStartProcess = StringUtils.isEmpty(taskId) && SillyMapUtils.getBooleanValue(saveMap, startProcessKey(), submit);
        saveMap.put(submitKey(), submit);
        saveMap.put(taskIdKey(), taskId);
        saveMap.put(startProcessKey(), isStartProcess);

        long startTime = System.currentTimeMillis();

        SillyNodeSourceData sourceData = new SillyNodeSourceData(usedCategory(), saveMap);
        initSourceData(sourceData);
        saveData(sourceData);

        M master = (M) sourceData.getMaster();
        log.info("masterId:【" + master.getId() + "】 taskId:【" + sourceData.taskId()
                + "】 数据执行链信息 ==> " + sourceData.handleLinkNameLog() + " 总耗时：" + (System.currentTimeMillis() - startTime) + "ms");

        return master;
    }

    protected List<String> initSourceDataHandleNames() {
        return INIT_SOURCE_DATA_HANDLE_NAMES;
    }

    /**
     * 加载主数据，节点配置数据
     *
     * @param sourceData
     */
    protected void initSourceData(SillyNodeSourceData sourceData) {
        List<String> names = initSourceDataHandleNames();
        doNodeHandle(names, sourceData);

    }

    /**
     * 执行数据保存处理器
     *
     * @param sourceData
     */
    private void saveData(SillyNodeSourceData sourceData) {
        SillyProcessNodeProperty<?> nodeProperty = sourceData.getNodeProperty();
        SillyAssert.notNull(nodeProperty, "节点数据未配置" + sourceData.getCategory());

        List<String> beforeNames = nodeProperty.getBeforeSaveHandleNames();
        if (beforeNames != null) {
            doNodeHandle(beforeNames, sourceData);
        }

        List<String> names = nodeProperty.getSaveHandleNames();
        doNodeHandle(names, sourceData);

        List<String> afterNames = nodeProperty.getAfterSaveHandleNames();
        if (afterNames != null) {
            doNodeHandle(afterNames, sourceData);
        }
    }


    private void doNodeHandle(List<String> saveHandleNames, SillyNodeSourceData sourceData) {
        SillyAssert.notNull(saveHandleNames, "节点处置器名称不可为空");
        SillyCategoryConfig sillyConfig = getSillyConfig();
        Map<String, SillyNodeSaveHandle> nodeSaveHandleMap = sillyConfig.getSillyNodeSaveHandleMap();
        SillyPropertyHandle propertyHandle = sourceData.getPropertyHandle();
        for (String name : saveHandleNames) {
            if (propertyHandle != null) {
                name = propertyHandle.getStringValue(name);
            }
            if (StringUtils.isNotEmpty(name)) {
                SillyNodeSaveHandle nodeSaveHandle = nodeSaveHandleMap.get(name);
                SillyAssert.notNull(nodeSaveHandle, "节点处置器未配置:" + name);
                doNodeHandle(sourceData, nodeSaveHandle);
            }
        }
    }

    private void doNodeHandle(SillyNodeSourceData sourceData, SillyNodeSaveHandle nodeSaveHandle) {
        long startTime = System.currentTimeMillis();
        String handleName = nodeSaveHandle.name();
        nodeSaveHandle.handle(sourceData);
        if (log.isDebugEnabled()) {
            long useTime = System.currentTimeMillis() - startTime;
            log.debug("nodeSaveHandle【" + handleName + "】处置耗时：" + useTime + "ms");
        }
    }

}
