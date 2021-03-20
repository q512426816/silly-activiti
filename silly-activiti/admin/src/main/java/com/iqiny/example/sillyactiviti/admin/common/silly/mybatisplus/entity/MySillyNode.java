/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.iqiny.example.sillyactiviti.admin.common.base.BaseEntity;
import com.iqiny.example.sillyactiviti.admin.common.utils.SecurityUtils;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import com.iqiny.silly.core.base.core.SillyVariable;
import com.iqiny.silly.mybatisplus.BaseMySillyNode;
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
public abstract class MySillyNode<T extends Model<T>, V extends SillyVariable> extends BaseMySillyNode<T, V> implements BaseEntity {

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

    @TableField(exist = false)
    protected List<V> variableList;
    @TableField(exist = false)
    protected Map<String, Object> variableMap = new LinkedHashMap<>();

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
