package com.springboot.config;


/**
 * <p>
 *  系统常量定义
 * </p>
 *
 * @author hai
 * @since 2018-03-10
 */
public interface Constant {
     /**
     * code定义
     */
    interface CodeConfig {
         /**
          * 失败
          */
         public static final Integer CODE_FAILURE = 0;
         /**
          * 成功
          */
         public static final Integer CODE_SUCCESS = 1;
         /**
          * 不允许为空
          */
         public static final Integer CODE_NOT_EMPTY = 2;
         /**
          * 系统异常
          */
         public static final Integer CODE_SYSTEM_EXCEPTION = 3;
         /**
          * 未查找到结果
          */
         public static final Integer CODE_NOT_FOUND_RESULT = 4;
         /**
          * 未登录
          */
         public static final Integer CODE_USER_NOT_LOGIN = 6;
     }
    /**
     * Message定义
     */
    interface MessageConfig{
        /**
         * 失败
         */
        public static final String MSG_FAILURE = "失败";
        /**
         * 成功
         */
        public static final String MSG_SUCCESS = "成功";
        /**
         * 不允许为空
         */
        public static final String MSG_NOT_EMPTY = "不允许为空";
        /**
         * 系统异常
         */
        public static final String MSG_SYSTEM_EXCEPTION = "系统异常";
        /**
         * 未查找到结果
         */
        public static final String MSG_NOT_FOUND_RESULT = "暂无订单数据";

        /**
         * 未登录
         */
        public static final String MSG_USER_NOT_LOGIN = "请先登录";
		 /**
         * 权限不足
         */
        public static final String PERMISSION_DENIED_ERROR = "权限不足";

        /**
         * 暂无数据
         */
        public static final String MSG_NO_DATA = "暂无数据";

        /**
         * 参数异常
         */
        public static final String PARAMETER_ABNORMITY = "参数异常";
    }

}
