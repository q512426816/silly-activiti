/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.3-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.common.silly.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.iqiny.example.sillyactiviti.admin.common.silly.mybatisplus.entity.MyBaseEntity;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import com.iqiny.silly.core.resume.SillyResume;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author QINY
 * @since 2019-07-30
 */
@TableName(value = "QMIS_SILLY_RESUME")
@EqualsAndHashCode(callSuper = true)
@Data
public class MySillyResume extends MyBaseEntity<MySillyResume> implements SillyResume {

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
     * 业务类型
     */
    @TableField("BUSINESS_TYPE")
    protected String businessType;
    /**
     * 业务表ID
     */
    @TableField("BUSINESS_ID")
    protected String businessId;
    /**
     * 序号
     */
    @TableField("SEQ")
    protected Integer seq;
    /**
     * 当前处置人
     */
    @TableField("HANDLE_USER_ID")
    protected String handleUserId;
    /**
     * 当前处置部门
     */
    @TableField("HANDLE_DEPT_ID")
    protected String handleDeptId;
    /**
     * 下一步处置人
     */
    @TableField("NEXT_USER_ID")
    protected String nextUserId;
    /**
     * 处置时间
     */
    @TableField(value = "HANDLE_DATE")
    protected Date handleDate;
    /**
     * 处置内容
     */
    @TableField("HANDLE_INFO")
    protected String handleInfo;
    /**
     * 处置耗时
     */
    @TableField("CONSUME_TIME")
    protected Long consumeTime;
    /**
     * 部门耗时
     */
    @TableField("CONSUME_DEPT_TIME")
    protected Long consumeDeptTime;
    /**
     * 流程类型
     */
    @TableField("PROCESS_TYPE")
    protected String processType;
    /**
     * 上一流程ID
     */
    @TableField("PROCESS_ID")
    protected String processId;
    /**
     * 流程等级
     */
    @TableField("PROCESS_LEVEL")
    protected Integer processLevel;
    /**
     * 流程等级
     */
    @TableField("PROCESS_NODE_KEY")
    protected String processNodeKey;
    /**
     * 当前任务流程节点
     */
    @TableField("PROCESS_NODE_NAME")
    protected String processNodeName;


    public Long getConsumeDeptTime() {
        return consumeDeptTime;
    }

    public MySillyResume() {
    }

    public MySillyResume(String id) {
        this.id = id;
    }


    public String getBusinessType() {
        return businessType;
    }

    @Override
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessId() {
        return businessId;
    }

    @Override
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getHandleUserId() {
        return handleUserId;
    }

    @Override
    public void setHandleUserId(String handleUserId) {
        this.handleUserId = handleUserId;
    }

    public String getHandleDeptId() {
        return handleDeptId;
    }

    @Override
    public void setHandleDeptId(String handleDeptId) {
        this.handleDeptId = handleDeptId;
    }

    public String getNextUserId() {
        return nextUserId;
    }

    @Override
    public void setNextUserId(String nextUserId) {
        this.nextUserId = nextUserId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getHandleDate() {
        return handleDate;
    }

    @Override
    public void setHandleDate(Date handleDate) {
        this.handleDate = handleDate;
    }

    public String getHandleInfo() {
        return handleInfo;
    }

    @Override
    public void setHandleInfo(String handleInfo) {
        this.handleInfo = handleInfo;
    }

    public Long getConsumeTime() {
        return consumeTime;
    }

    @Override
    public void setConsumeTime(Long consumTime) {
        this.consumeTime = consumTime;
    }

    public Long getConsumDeptTime() {
        return consumeDeptTime;
    }

    @Override
    public void setConsumeDeptTime(Long consumDeptTime) {
        this.consumeDeptTime = consumDeptTime;
    }

    public String getProcessType() {
        return processType;
    }

    @Override
    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public Integer getProcessLevel() {
        return processLevel;
    }

    public void setProcessLevel(Integer processLevel) {
        this.processLevel = processLevel;
    }

    public String getProcessNodeKey() {
        return processNodeKey;
    }

    @Override
    public void setProcessNodeKey(String processNodeKey) {
        this.processNodeKey = processNodeKey;
    }

    public String getProcessNodeName() {
        return processNodeName;
    }

    @Override
    public void setProcessNodeName(String processNodeName) {
        this.processNodeName = processNodeName;
    }

}
