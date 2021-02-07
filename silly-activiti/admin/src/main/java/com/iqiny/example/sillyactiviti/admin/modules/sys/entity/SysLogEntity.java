package com.iqiny.example.sillyactiviti.admin.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iqiny.example.sillyactiviti.admin.common.base.DefaultBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * 系统日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_log")
public class SysLogEntity extends DefaultBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String TYPE_ACCESS = "1";
    public static final String TYPE_EXCEPTION = "2";

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户操作
     */
    private String operation;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求参数
     */
    @TableField(condition = SqlCondition.LIKE)
    private String params;
    /**
     * 执行时长(毫秒)
     */
    private Long time;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 标题
     */
    private String title;
    /**
     * 日志类型 1：正常  2：异常
     */
    private String type;
    /**
     * 请求 uri
     */
    private String uri;
    /**
     * 用户代理信息
     */
    private String userAgent;
    /**
     * 异常信息
     */
    @TableField(condition = SqlCondition.LIKE)
    private String exceptionInfo;

    @TableField(exist = false)
    private String typeName;

    public void setParamsByMap(Map<String, String[]> paramMap) {
        if (paramMap == null) {
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
            params.append("".equals(params.toString()) ? "" : "&").append(param.getKey()).append("=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            //params.append(StringUtils.abbreviate(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        this.params = params.toString();
    }
}
