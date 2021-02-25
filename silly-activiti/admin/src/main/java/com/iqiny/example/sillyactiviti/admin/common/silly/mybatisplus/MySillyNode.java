package com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.iqiny.example.sillyactiviti.admin.common.base.BaseEntity;
import com.iqiny.example.sillyactiviti.admin.common.utils.SecurityUtils;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import com.iqiny.silly.core.base.core.SillyNode;
import com.iqiny.silly.core.base.core.SillyVariable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * SillyNode 集成MybatisPlus
 *
 * @param <T>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class MySillyNode<T extends Model<T>, V extends SillyVariable> extends Model<T> implements SillyNode<V>, BaseEntity {

    protected String masterId;

    protected int seq;

    protected String nodeKey;
    protected String taskId;

    protected String parallelFlag;

    protected Date nodeDate;
    protected String nodeUserId;

    protected String status;

    protected String nodeInfo;

    @TableField(exist = false)
    protected List<V> variableList;
    @TableField(exist = false)
    protected Map<String, Object> variableMap = new LinkedHashMap<>();


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
        this.createDate = new Date();
        this.createUserId = SecurityUtils.getUserId();
        this.updateDate = new Date();
        this.updateUserId = SecurityUtils.getUserId();
        this.delFlag = 0;
    }

    @Override
    public void preUpdate() {
        this.updateDate = new Date();
        this.updateUserId = SecurityUtils.getUserId();
    }

}
