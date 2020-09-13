package com.iqiny.silly.common;

public class Constant {
    private Constant() {
    }

    public class ActivitiNode {

        public static final String KEY_END = "end";

        // ===================================== STATUS ==============================================
        /**
         * 当前状态数据
         */
        public static final String STATUS_CURRENT = "10";
        /**
         * 历史状态数据
         */
        public static final String STATUS_HISTORY = "20";

        // ===================================== ACTIVITI_CONVERTOR ==============================================
        /**
         * 流程变量描述 使用 list 类型
         */
        public static final String CONVERTOR_LIST = "list";
        /**
         * 流程变量描述 使用 List<List<Object>> 类型
         */
        public static final String CONVERTOR_LIST_LIST = "list_list";
        /**
         * 流程变量描述 使用 string 类型
         */
        public static final String CONVERTOR_STRING = "string";

    }

    public class ActivitiParallel {
        /**
         * 是并行
         */
        public static final String IS_PARALLEL = "1";
        /**
         * 非并行
         */
        public static final String NOT_PARALLEL = "0";
    }

    public class ActivitiDataMap{
        /**
         * 存储全部扁平化数据
         */
        public static final String KEY_ALL_MAP = "all";
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
    }

}
