package com.iqiny.silly.activiti;

import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.service.base.AbstractSillyReadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 结合工作流Activiti的傻瓜读取服务
 *
 * @param <M>
 * @param <N>
 * @param <V>
 */
public abstract class EnhanceSillyReadService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> extends AbstractSillyReadService<M, N, V> {

    @Autowired
    protected SillyActivitiService sillyActivitiService;

    /**
     * 获取此主业务的版本号
     *
     * @param masterId 主表ID
     * @return 版本号
     */
    public String getVersion(String masterId) {
        return getVersion(getMaster(masterId));
    }

    public String getVersion(M master) {
        final String processId = master.getActProcessId();
        if (processId == null) {
            return StringUtils.lowerCase(master.getActVersion());
        }
        final String key = sillyActivitiService.getActKeyNameByActProcessId(processId);
        return getVersionByKey(key);
    }

    protected String getVersionByKey(String key) {
        if (key != null) {
            String[] arr = key.split("_");
            if (arr.length <= 1) {
                return "";
            }
            return StringUtils.lowerCase(arr[arr.length - 1]);
        }
        return "";
    }

    public String getTaskNodeKey(String taskId) {
        return sillyActivitiService.getTaskNodeKey(taskId);
    }
}
