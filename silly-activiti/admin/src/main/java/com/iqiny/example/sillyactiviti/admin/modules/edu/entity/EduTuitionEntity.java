/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.6-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.edu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iqiny.example.sillyactiviti.admin.common.base.DefaultBaseEntity;
import com.iqiny.example.sillyactiviti.admin.modules.res.entity.PersonEntity;
import com.iqiny.example.sillyactiviti.common.validator.group.AddGroup;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 缴费信息
 *
 * @author qiny
 * @email qy-1994-2008@163.com
 * @date 2020-10-25 20:05:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("xh_edu_tuition")
public class EduTuitionEntity extends DefaultBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 人员id
     */
    @NotNull(message = "推广等级不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String personId;
    /**
     * 推荐人 代理编号
     */
    private String agentCode;
    /**
     * 推荐人 ID
     */
    private String agentPersonId;
    /**
     * 了解渠道 字典 EDU_CHANNEL
     */
    private String channel;
    /**
     * 缴费金额
     */
    private String payMoney;
    /**
     * 应缴金额
     */
    private String needPayMoney;
    /**
     * 是否购买保险
     */
    private String hasInsurance;
    /**
     * 状态
     */
    private String status;
    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private PersonEntity person;

    @TableField(exist = false)
    private String channelName;
    
    @TableField(exist = false)
    private String hasInsuranceName;

    @TableField(exist = false)
    private String statusName;
}
