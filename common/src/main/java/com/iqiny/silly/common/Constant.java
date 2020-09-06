package com.iqiny.silly.common;

public class Constant {
    private Constant() {
    }

    public class ActivitiNode {

        // ===================================== STATUS ==============================================
        /**
         * 当前状态数据
         */
        public static final String STATUS_CURRENT = "10";
        /**
         * 历史状态数据
         */
        public static final String STATUS_HISTORY = "20";

        // ===================================== ACTIVITI_HANDLER ==============================================
        /**
         * 流程变量描述 nextUserId 流程下一步人ID信息存储 前端 act-var="nextUserId" （使用 list 类型）
         */
        public static final String ACTIVITI_HANDLER_NEXT_USER_ID = "nextUserId";
        /**
         * 流程变量描述 nextUserId 流程下一步人ID信息存储 前端 act-var="nextUserId" （使用 list 类型）
         * 此类型后续升级可能会去除，建议使用 ACTIVITI_HANDLER_LIST_LIST 类型代替！！！
         */
        public static final String ACTIVITI_HANDLER_NEXT_OFFICE_ID = "nextOfficeId";
        /**
         * 流程变量描述 使用 list 类型
         */
        public static final String ACTIVITI_HANDLER_LIST = "list";
        /**
         * 流程变量描述 使用 List<List<Object>> 类型
         */
        public static final String ACTIVITI_HANDLER_LIST_LIST = "list&list";
        /**
         * 流程变量描述 使用 string 类型
         */
        public static final String ACTIVITI_HANDLER_STRING = "string";

    }


    public class ActivitiLabel {
        /**
         * 标签-字符串  数据使用String存储 前端 my-var="string" (默认值) 空及string 使用String 操作数据 其他 使用list 操作
         */
        public static final String LABEL_STRING = "string";

        public static final String LABEL_LIST = "list";
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

}
