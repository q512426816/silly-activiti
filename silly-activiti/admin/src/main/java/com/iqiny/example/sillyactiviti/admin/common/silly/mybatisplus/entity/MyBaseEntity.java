package com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.iqiny.example.sillyactiviti.admin.common.base.BaseEntity;
import com.iqiny.example.sillyactiviti.admin.common.utils.SecurityUtils;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import com.iqiny.silly.mybatisplus.BaseMyBaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public abstract class MyBaseEntity<T extends Model<?>> extends BaseMyBaseEntity<T> implements BaseEntity {

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
