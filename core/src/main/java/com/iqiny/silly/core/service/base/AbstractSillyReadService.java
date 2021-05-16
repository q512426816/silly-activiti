/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-core 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.core.service.base;

import com.iqiny.silly.common.SillyConstant;
import com.iqiny.silly.common.exception.SillyException;
import com.iqiny.silly.core.base.core.SillyMaster;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.core.convertor.SillyVariableConvertor;
import com.iqiny.silly.core.service.SillyReadService;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 默认抽象方法
 *
 * @param <M> 主
 * @param <N> 节点
 * @param <V> 变量
 */
@SuppressWarnings("unchecked")
public abstract class AbstractSillyReadService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable, T> extends AbstractSillyService<M, N, V, T> implements SillyReadService<M, N, V> {


    /**
     * 查询主表数据
     *
     * @param masterId
     * @return
     */
    @Override
    public Map<String, Object> getMasterMap(String masterId) {
        final M master = getMaster(masterId);
        return getMasterMap(master);
    }

    public Map<String, Object> getMasterMap(M master) {
        return beanToMap(master);
    }
    
    
    /**
     * 使用Introspector，对象转换为map集合
     *
     * @param beanObj javabean对象
     * @return map集合
     */
    private Map<String, Object> beanToMap(Object beanObj, String... ignor) {
        if (null == beanObj) {
            return null;
        }
        List<String> ignorList = new ArrayList<>();
        if (ignor != null) {
            ignorList = Arrays.asList(ignor);
        }
        HashMap<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (ignorList.contains(key)) {
                    continue;
                }
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(beanObj) : null;
                if (value instanceof Date) {
                    value = df.format(value);
                }
                map.put(key, value);
            }
            return map;
        } catch (Exception ex) {
            throw new SillyException(ex.getMessage());
        }
    }

    /**
     * 获取当前任务下的变量数据
     *
     * @param taskId
     * @return
     */
    public Map<String, Object> findVariableByTaskId(String taskId) {
        final V variable = sillyFactory.newVariable();
        variable.setTaskId(taskId);
        variable.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        return findVariableByVariable(variable);
    }

    /**
     * 获取数据 Map
     *
     * @param masterId
     * @return
     */
    public Map<String, Object> findVariableByMasterId(String masterId) {
        return findVariableByMasterIdNodeKey(masterId, null);
    }

    /**
     * 获取数据 Map
     *
     * @param masterId
     * @param nodeKey
     * @return
     */
    public Map<String, Object> findVariableByMasterIdNodeKey(String masterId, String nodeKey) {
        final V variable = sillyFactory.newVariable();
        variable.setMasterId(masterId);
        variable.setNodeKey(nodeKey);
        variable.setStatus(SillyConstant.ActivitiNode.STATUS_CURRENT);
        return findVariableByVariable(variable);
    }

    /**
     * 获取数据 Map
     *
     * @param variable
     * @return
     */
    public Map<String, Object> findVariableByVariable(V variable) {
        final List<V> variables = findVariableList(variable);
        Map<String, Object> map = new LinkedHashMap<>();
        for (V auditVariable : variables) {
            final SillyVariableConvertor<?> sillyHandler = getSillyConvertor(auditVariable.getVariableType());
            sillyHandler.convert(map, auditVariable.getVariableName(), auditVariable.getVariableText());
        }

        return map;
    }

    protected abstract List<V> findVariableList(V where);

}
