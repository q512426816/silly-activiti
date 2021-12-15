/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.savehandle.node;

import com.alibaba.fastjson.JSON;
import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.common.util.SillyAssert;
import com.iqiny.silly.common.util.StringUtils;
import com.iqiny.silly.core.base.SillyFactory;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.config.SillyCategoryConfig;
import com.iqiny.silly.core.config.property.SillyProcessNodeProperty;
import com.iqiny.silly.core.config.property.SillyProcessVariableProperty;
import com.iqiny.silly.core.config.property.SillyPropertyHandle;
import com.iqiny.silly.core.config.property.impl.DefaultVariableSaveHandle;
import com.iqiny.silly.core.savehandle.SillyNodeSourceData;

import java.util.*;

/**
 * 内部方法处理  map 转 variableList
 */
public class SillyMapToVarSaveHandle extends BaseSillyNodeSaveHandle {

    public static final int ORDER = SillyLoadNodeInfoSaveHandle.ORDER + 100;

    public static final String NAME = "mapToVar";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public int order() {
        return ORDER;
    }

    @Override
    protected boolean canDo(SillyNodeSourceData sourceData) {
        return sourceData.getVariables() == null;
    }

    @Override
    protected void saveHandle(SillyCategoryConfig sillyConfig, SillyNodeSourceData sourceData) {
        SillyNode node = sourceData.getNode();
        SillyAssert.notNull(node, "当前处置节点数据未能获取");

        SillyPropertyHandle propertyHandle = sourceData.getPropertyHandle();
        SillyProcessNodeProperty<?> nodeProperty = sourceData.getNodeProperty();
        Map<String, Object> map = sourceData.getMap();
        SillyFactory sillyFactory = sillyConfig.getSillyFactory();
        // 设置变量集合数据 （map 转 variableList）
        List<? extends SillyVariable> sillyVariables = mapToVariables(propertyHandle, map, nodeProperty, sillyFactory);
        sourceData.setVariables(sillyVariables);
        node.setVariableList(sillyVariables);
    }

    protected <V extends SillyVariable> List<V> mapToVariables(
            SillyPropertyHandle sillyPropertyHandle
            , Map<String, Object> saveMap
            , SillyProcessNodeProperty<?> nodeProperty
            , SillyFactory sillyFactory) {
        SillyAssert.notNull(nodeProperty, "节点配置不可为空");

        Map<String, Object> map = new HashMap<>(saveMap);

        // 忽略这些变量值
        map.remove("id");
        map.remove("taskId");
        map.remove("submit");
        map.remove("startProcess");
        map.remove("processKey");
        map.remove("nodeKey");

        List<V> list = new ArrayList<>();
        Map<String, ? extends SillyProcessVariableProperty> variableMap = nodeProperty.getVariable();
        Set<String> keySet = variableMap.keySet();

        StringJoiner checkSj = new StringJoiner("\r\n");
        for (String vKey : keySet) {
            SillyProcessVariableProperty variableProperty = variableMap.get(vKey);
            Object variableObj = map.remove(vKey);
            String variableText = object2String(variableObj, null);
            if (StringUtils.isEmpty(variableText)) {
                Object defaultObject = sillyPropertyHandle.getValue(variableProperty.getDefaultText());
                variableText = object2String(defaultObject, null);
            }

            if (variableProperty.isUpdatePropertyHandleValue()) {
                sillyPropertyHandle.updateValue(vKey, variableText);
            }
            String variableName = sillyPropertyHandle.getStringValue(variableProperty.getVariableName());
            String variableType = sillyPropertyHandle.getStringValue(variableProperty.getVariableType());
            String belong = sillyPropertyHandle.getStringValue(variableProperty.getBelong());
            String activitiHandler = sillyPropertyHandle.getStringValue(variableProperty.getActivitiHandler());
            String saveHandleName = StringUtils.join(variableProperty.getSaveHandleNames(), SillyConstant.ARRAY_SPLIT_STR);
            paramToVariableList(list,
                    variableName,
                    variableText,
                    variableType,
                    belong,
                    activitiHandler,
                    saveHandleName,
                    sillyFactory
            );
        }

        keySet = map.keySet();
        for (String key : keySet) {
            if (nodeProperty.ignoreField(key)) {
                continue;
            }

            if (!nodeProperty.isAllowOtherVariable()) {
                if (nodeProperty.isOtherVariableThrowException()) {
                    checkSj.add(" 不允许保存此未定义的变量数据【" + key + "】");
                }
                continue;
            }

            doVariableList(map.get(key), list, key, SillyConstant.ActivitiVariable.BELONG_VARIABLE, null, sillyFactory);
        }

        String checkInfo = checkSj.toString();
        SillyAssert.isEmpty(checkInfo, checkInfo);

        return list;
    }

    protected String object2String(Object variableObj, String defaultStr) {
        String variableText = defaultStr;
        if (variableObj == null) {
            return variableText;
        }

        if (variableObj instanceof Collection) {
            Collection<Object> c = (Collection<Object>) variableObj;
            if (c.isEmpty()) {
                return variableText;
            }

            Object next = c.iterator().next();
            if (next instanceof String) {
                variableText = StringUtils.myJoin((Collection<String>) variableObj);
            } else {
                variableText = JSON.toJSONString(variableObj);
            }
        } else {
            if (variableObj instanceof String) {
                variableText = (String) variableObj;
            } else {
                variableText = JSON.toJSONString(variableObj);
            }
        }

        return variableText;
    }

    protected <V extends SillyVariable> void doSetVariableList(List<V> list
            , String key, String variableText, String variableType, String belong
            , String activitiHandler, String saveHandleName, SillyFactory sillyFactory) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(variableText)) {
            return;
        }

        paramToVariableList(list, key, variableText, variableType, belong, activitiHandler, saveHandleName, sillyFactory);
    }

    protected <V extends SillyVariable> void paramToVariableList(List<V> list
            , String key, String variableText, String variableType, String belong, String activitiHandler
            , String saveHandleName, SillyFactory sillyFactory) {
        V v = (V) sillyFactory.newVariable();
        v.setBelong(belong);
        v.setSaveHandleName(saveHandleName);
        v.setActivitiHandler(activitiHandler);
        v.setVariableName(key);
        v.setVariableText(variableText);
        v.setVariableType(variableType);
        list.add(v);
    }

    protected <V extends SillyVariable> void doVariableList(Object object, List<V> list, String key
            , String belong, String activitiHandler, SillyFactory sillyFactory) {
        if (object == null || StringUtils.isEmpty(key)) {
            return;
        }

        if (object instanceof String) {
            String variableText = (String) object;
            doSetVariableList(list, key, variableText, SillyConstant.ActivitiNode.CONVERTOR_STRING,
                    belong, activitiHandler, DefaultVariableSaveHandle.NAME, sillyFactory);
        } else if (object instanceof Collection<?>) {
            Collection<?> objectList = (Collection<?>) object;
            StringJoiner variableTextSj = new StringJoiner(SillyConstant.ARRAY_SPLIT_STR);
            for (Object o : objectList) {
                if (o instanceof String) {
                    variableTextSj.add((String) o);
                } else {
                    throw new SillyException("不支持此数据集合内类型进行变量转换" + o.getClass());
                }
            }
            doSetVariableList(list, key, variableTextSj.toString(), SillyConstant.ActivitiNode.CONVERTOR_LIST,
                    belong, activitiHandler, DefaultVariableSaveHandle.NAME, sillyFactory);
        } else {
            throw new SillyException("不支持此数据类型进行变量转换" + object.getClass());
        }
    }

}
