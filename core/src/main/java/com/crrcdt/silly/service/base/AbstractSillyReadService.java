package com.crrcdt.silly.service.base;

import com.crrcdt.silly.base.SillyFactory;
import com.crrcdt.silly.base.SillyTaskData;
import com.crrcdt.silly.base.core.SillyMaster;
import com.crrcdt.silly.base.core.SillyNode;
import com.crrcdt.silly.base.core.SillyVariable;
import com.crrcdt.silly.convertor.SillyListConvertor;
import com.crrcdt.silly.convertor.SillyStringConvertor;
import com.crrcdt.silly.convertor.SillyVariableConvertor;
import com.crrcdt.silly.service.SillyReadService;
import com.iqiny.silly.common.Constant;
import com.iqiny.silly.common.util.StringUtils;

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
public abstract class AbstractSillyReadService<M extends SillyMaster, N extends SillyNode<V>, V extends SillyVariable> implements SillyReadService<M, N, V> {

    protected SillyFactory<M, N, V> sillyFactory;
    
    private Map<String, SillyVariableConvertor<?>> sillyHandlerMap;

    /**
     * 存储全部扁平化数据
     */
    public static final String KEY_ALL_MAP = "all";

    /**
     * 节点处置人ID
     */
    public static final String KEY_NODE_USER_ID = "userId";
    /**
     * 节点处置人名称
     */
    public static final String KEY_NODE_USER_NAME = "userName";
    /**
     * 节点处置附件组ID
     */
    public static final String KEY_NODE_ATTACH_GROUP_ID = "attachGroupId";
    /**
     * 节点处置时间字符串
     */
    public static final String KEY_NODE_HANDLE_DATE = "nodeTime";
    /**
     * 节点处置的工作流任务ID
     */
    public static final String KEY_NODE_TASK_ID = "taskId";

    /**
     * 集合数据存储地方
     */
    public static final String KEY_GROUP = "group";

    private SillyVariableConvertor<?> getSillyHandler(String handleKey) {
        if (sillyHandlerMap == null) {
            synchronized (this) {
                if (sillyHandlerMap == null) {
                    sillyHandlerMap = initSillyHandlerMap();
                }
            }
            if (sillyHandlerMap == null) {
                throw new RuntimeException("Silly处理类初始化为null！");
            }
        }
        final SillyVariableConvertor<?> handler = sillyHandlerMap.get(handleKey);
        return handler == null ? sillyHandlerMap.get(Constant.ActivitiLabel.LABEL_STRING) : handler;
    }


    private Map<String, SillyVariableConvertor<?>> initSillyHandlerMap() {
        Map<String, SillyVariableConvertor<?>> sillyHandlerMap = new LinkedHashMap<>();
        sillyHandlerMap.put(Constant.ActivitiLabel.LABEL_STRING, new SillyStringConvertor());
        sillyHandlerMap.put(Constant.ActivitiLabel.LABEL_LIST, new SillyListConvertor());
        return sillyHandlerMap;
    }

    protected abstract SillyFactory<M, N, V> createSillyFactory();
    
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
     * 查询 节点信息 包装Bean 集合
     *
     * @param masterId
     * @return
     */
    public List<? extends SillyTaskData<N, V>> getProcessBeanList(String masterId) {
        List<SillyTaskData<N, V>> list = new ArrayList<>();
        final List<N> nodeList = getNodeList(masterId);
        for (N node : nodeList) {
            SillyTaskData<N, V> sillyNodeBean = sillyFactory.newSillyTaskData();
            sillyNodeBean.setVariableList(getVariableList(masterId, node.getId()));
            sillyNodeBean.setNode(node);
            list.add(sillyNodeBean);
        }
        return list;
    }

    /**
     * 获取流程信息节点 Map
     * 节点 key.name
     * 若为list 则放入  key.group 属性中
     *
     * @return
     */
    public Map<String, Object> processVariableMap(String masterId) {
        final List<? extends SillyTaskData<N, V>> processBeanList = getProcessBeanList(masterId);
        Map<String, Object> bigMap = new LinkedHashMap<>();
        if (processBeanList != null && !processBeanList.isEmpty()) {
            for (SillyTaskData<N, V> processBean : processBeanList) {
                if (processBean != null) {
                    Map<String, Object> nodeMap = createSillyNodeProcess(processBean.getNode(), bigMap);
                    final List<V> variableList = processBean.getVariableList();
                    Map<String, Map<String, Object>> variableMap = createSillyVariableMap(nodeMap, variableList);
                    mergeMap(variableMap, nodeMap);
                }
            }
        }
        bigMap.put(KEY_ALL_MAP, mergeAllMap(bigMap, new LinkedHashMap<>(128)));
        return bigMap;
    }

    /**
     * 将Map 中的 K--V 扁平化存储 将其中的 V 是 Map 的全部拆分
     *
     * @param bigMap
     * @param toMap
     * @return
     */
    protected Map<String, Object> mergeAllMap(final Map<String, Object> bigMap, final Map<String, Object> toMap) {
        bigMap.forEach((k, v) -> {
            if (v instanceof List) {
                List list = (List) v;
                boolean isMap = false;
                for (Object o : list) {
                    if (o instanceof Map) {
                        isMap = true;
                        mergeAllMap((Map) o, toMap);
                    }
                }
                if (!isMap) {
                    toMap.put(k, v);
                }
            } else if (v instanceof Map) {
                mergeAllMap((Map) v, toMap);
            } else {
                toMap.put(k, v);
            }
        });
        return toMap;
    }

    /**
     * 创建节点流程变量Map
     *
     * @param np
     * @param map
     * @return
     */
    protected Map<String, Object> createSillyNodeProcess(N np, Map<String, Object> map) {
        Map<String, Object> nodeMap = new LinkedHashMap<>();
        boolean isParallel = !Constant.ActivitiParallel.NOT_PARALLEL.equals(np.getParallelFlag());
        if (isParallel) {
            List<Map<String, Object>> list = null;
            if (map.get(np.getNodeKey()) instanceof List) {
                list = (List<Map<String, Object>>) map.get(np.getNodeKey());
            }
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(nodeMap);
            map.put(np.getNodeKey(), list);
        } else {
            map.put(np.getNodeKey(), nodeMap);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (np.getProcessDate() != null) {
            nodeMap.put(KEY_NODE_HANDLE_DATE, sdf.format(np.getProcessDate()));
        }
        nodeMap.put(KEY_NODE_TASK_ID, np.getTaskId());
        return nodeMap;
    }

    /**
     * 生成流程变量Map
     *
     * @param nodeMap
     * @param pvList
     * @return
     */
    protected Map<String, Map<String, Object>> createSillyVariableMap(Map<String, Object> nodeMap, List<V> pvList) {
        Map<String, Map<String, Object>> variableMap = new LinkedHashMap<>();
        if (pvList == null || pvList.isEmpty()) {
            return variableMap;
        }

        for (SillyVariable variable : pvList) {
            final String label = variable.getVariableLabel();
            final String key = variable.getVariableName();
            final String value = variable.getVariableText();
            if (StringUtils.isEmpty(label) || Constant.ActivitiLabel.LABEL_STRING.equals(label)) {
                final SillyVariableConvertor<?> handle = getSillyHandler(Constant.ActivitiLabel.LABEL_STRING);
                handle.convert(nodeMap, key, value);
            } else {
                Map<String, Object> myMap = variableMap.computeIfAbsent(label, k -> new HashMap<>());
                final SillyVariableConvertor<?> handle = getSillyHandler(label);
                handle.convert(myMap, key, value);
            }
        }
        return variableMap;
    }

    /**
     * 合并节点map 与 变量Map
     *
     * @param variableMap
     * @param nodeMap
     */
    protected void mergeMap(Map<String, Map<String, Object>> variableMap, Map<String, Object> nodeMap) {
        if (!variableMap.isEmpty()) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (String key : variableMap.keySet()) {
                list.add(variableMap.get(key));
            }
            nodeMap.put(KEY_GROUP, list);
        }
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
            throw new RuntimeException();
        }
    }
}
