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
