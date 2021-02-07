package com.iqiny.example.sillyactiviti.admin.modules.ncr.silly;

import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrMaster;
import com.iqiny.example.sillyactiviti.admin.modules.ncr.entity.NcrNode;
import com.iqiny.example.sillyactiviti.common.utils.BeanUtils;

import java.util.*;

public class NcrSillyDataMap extends LinkedHashMap<String, Object> {

    private Map<String, Set<String>> actMap = new HashMap<>();

    public NcrSillyDataMap() {
        init();
    }

    protected void init() {
        // v1 版本 流程变量属性名称
        Set<String> v1Set = new LinkedHashSet<>();
        v1Set.add("approveUserId");
        v1Set.add("approveUserId111");
        actMap.put("PBTS_DELIVERY_VEHICLE_V1", v1Set);
    }

    /**
     * 判断是否为流程变量属性
     *
     * @param processKey
     * @param fieldName
     * @return
     */
    protected boolean isActField(String processKey, String fieldName) {
        final Set<String> fields = actMap.get(processKey);
        if (fields != null) {
            return fields.contains(fieldName);
        }
        return false;
    }

    private NcrMaster master;

    private NcrNode node;

    public NcrMaster getMaster() {
        if (master == null) {
            final Object o = this.get("master");
            if (o instanceof Map) {
                master = BeanUtils.mapToBean((Map) o, NcrMaster.class);
            }
        }
        return master;
    }

    public void setMaster(NcrMaster master) {
        this.master = master;
    }

    public NcrNode getNode() {
        if (node == null) {
            final Object o = this.get("node");
            if (o instanceof Map) {
                node = BeanUtils.mapToBean((Map) o, NcrNode.class);
            }
        }
        return node;
    }

    public void setNode(NcrNode node) {
        this.node = node;
    }
}
