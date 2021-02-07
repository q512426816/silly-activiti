package com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.iqiny.example.sillyactiviti.admin.common.base.BaseEntity;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import com.iqiny.silly.core.base.core.SillyVariable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * SillyVariable 集成MybatisPlus
 *
 * @param <T>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class MySillyVariable<T extends Model<T>> extends Model<T> implements SillyVariable, BaseEntity {

    protected String nodeId;
    protected String masterId;

    protected String taskId;
    protected String nodeKey;
    protected String status;


    protected String activitiHandler;

    protected String variableType;
    protected String variableName;
    protected String variableText;

    /**
     * 主键
     */
    @TableId
    @NotNull(message = "基础ID不可为空", groups = {UpdateGroup.class})
    protected String id;
    /**
     * 删除标记  -1：已删除  0：正常
     */
    @TableLogic
    protected Integer delFlag;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createDate;
    /**
     * 创建人
     */
    protected String createUserId;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateDate;
    /**
     * 更新人
     */
    protected String updateUserId;

    /**
     * 更新人
     */
    @TableField(exist = false)
    protected String updateUserName;
    /**
     * 创建人
     */
    @TableField(exist = false)
    protected String createUserName;


    @Override
    public void preInsert() {

    }

    @Override
    public void preUpdate() {

    }

}
