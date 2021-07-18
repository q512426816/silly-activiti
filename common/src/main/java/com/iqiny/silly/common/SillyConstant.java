/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-common 1.0.5-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.silly.common;

public class SillyConstant {

    public static final String ARRAY_SPLIT_STR = ",";

    private SillyConstant() {
    }

    public class ActivitiNode {

        public static final String KEY_START = "start";
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

    public class ActivitiVariable {

        /**
         * 流程变量 归属主表
         */
        public static final String BELONG_MASTER = "master";
        /**
         * 流程变量 归属节点表
         */
        public static final String BELONG_NODE = "node";
        /**
         * 流程变量 归属变量表
         */
        public static final String BELONG_VARIABLE = "variable";

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

    public class ActivitiDataMap {
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

    public class SillyResumeType {
        /**
         * 履历类型 启动
         */
        public static final String PROCESS_TYPE_START = "start";
        /**
         * 履历类型 提交
         */
        public static final String PROCESS_TYPE_NEXT = "next";
        /**
         * 履历类型 驳回
         */
        public static final String PROCESS_TYPE_BACK = "back";
        /**
         * 履历类型 流转
         */
        public static final String PROCESS_TYPE_FLOW = "flow";
        /**
         * 履历类型 关闭
         */
        public static final String PROCESS_TYPE_CLOSE = "close";
    }

    public class YesOrNo {
        public static final String YES = "1";
        public static final String NO = "0";
    }
}
