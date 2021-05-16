/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.enroll.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iqiny.example.sillyactiviti.admin.common.base.DefaultBaseEntity;
import com.iqiny.example.sillyactiviti.admin.modules.res.entity.PersonEntity;
import com.iqiny.example.sillyactiviti.common.validator.group.AddGroup;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 招生员信息
 *
 * @author xy
 * @email sog_xy@163.com
 * @date 2020-10-25 13:01:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("xh_enroll_agent")
public class EnrollAgentEntity extends DefaultBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    @NotNull(message = "人员不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String personId;
    /**
     * 代理人编号 （唯一）
     */
    private String agentCode;
    /**
     * 推广等级 对应字典 GENERALIZE_GRADE
     */
    @NotBlank(message = "推广等级不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String grade;
    /**
     * 分红
     */
    private String dividend;
    /**
     * 返点
     */
    private String rebate;
    /**
     * 招收人数
     */
    private String enrollment;
    /**
     * 加入时间
     */
    private Date joinDate;
    /**
     * 年终福利
     */
    private String yearEndBonus;
    /**
     * 额外福利
     */
    private String extraBonus;
    /**
     * 相关供应商的优惠券
     */
    private String coupon;
    /**
     * 备注
     */
    @TableField(condition = SqlCondition.LIKE)
    private String remark;

    @TableField(exist = false)
    private String gradeName;

    @TableField(exist = false)
    private PersonEntity person;

}
