package com.iqiny.silly.common;

public class Constant {
    private Constant() {
    }

    public static final String QMIS_SYSTEM_ID = "QMIS_SYSTEM_ID:20200408:A";

    public class NeedFlag {
        /**
         * 需要
         */
        public static final String YES = "1";
        /**
         * 不需要
         */
        public static final String NO = "0";
    }


    public class BusinessType {
        /**
         * NCR类型
         */
        public static final String NCR = "10";
        /**
         * 变更类型
         */
        public static final String ALTER = "20";
        /**
         * 例外转序类型
         */
        public static final String EXOD = "30";
        /**
         * 变更管理类型
         */
        public static final String CHANGE = "40";
        /**
         * 例行试验
         */
        public static final String ROUTINE_TSET = "50";
        /**
         * 质量门让步
         */
        public static final String QUALITY_GATE = "60";
        /**
         * 工位检验类型
         */
        public static final String INSPECT = "80";

    }

    public class DataSrc {
        /**
         * WMS
         */
        public static final String WMS = "1";
        /**
         * QMIS
         */
        public static final String QMIS = "2";
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

    public class ProjectCode {
        /**
         * 通用件项目编号
         */
        public static final String GENERAL_PARTS = "通用件";
    }

    /**
     * 默认值
     */
    public static class DefaultValue {

        private static final InnerDefaultValue DEFAULT_VALUE = new InnerDefaultValue();

        public static byte getByte() {
            return DEFAULT_VALUE.BYTE;
        }

        public static short getShort() {
            return DEFAULT_VALUE.SHORT;
        }

        public static int getInt() {
            return DEFAULT_VALUE.INT;
        }

        public static float getFloat() {
            return DEFAULT_VALUE.FLOAT;
        }

        public static long getLong() {
            return DEFAULT_VALUE.LONG;
        }

        public static double getDouble() {
            return DEFAULT_VALUE.DOUBLE;
        }

        public static char getChar() {
            return DEFAULT_VALUE.CHAR;
        }

        public static boolean getBoolean() {
            return DEFAULT_VALUE.BOOLEAN;
        }
    }

    /**
     * 默认值
     */
    private static class InnerDefaultValue {
        private byte BYTE;
        private short SHORT;
        private int INT;
        private float FLOAT;
        private long LONG;
        private double DOUBLE;
        private char CHAR;
        private boolean BOOLEAN;

        public byte getBYTE() {
            return BYTE;
        }

        public short getSHORT() {
            return SHORT;
        }

        public int getINT() {
            return INT;
        }

        public float getFLOAT() {
            return FLOAT;
        }

        public long getLONG() {
            return LONG;
        }

        public double getDOUBLE() {
            return DOUBLE;
        }

        public char getCHAR() {
            return CHAR;
        }

        public boolean getBOOLEAN() {
            return BOOLEAN;
        }
    }
}
