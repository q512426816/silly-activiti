/*
 *  Copyright  iqiny.com
 *
 *  https://gitee.com/iqiny/silly
 *
 *  project name：silly-parent 1.0.4-RELEASE
 *  project description：top silly project pom.xml file
 */
package com.iqiny.example.sillyactiviti.admin.modules.res.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iqiny.example.sillyactiviti.admin.common.base.DefaultBaseEntity;
import com.iqiny.example.sillyactiviti.common.validator.group.AddGroup;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 人员信息
 * 
 * @author qiny
 * @email qy-1994-2008@163.com
 * @date 2020-10-11 14:46:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("xh_person")
public class PersonEntity extends DefaultBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 人员姓名
	 */
	@NotBlank(message="人员姓名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@TableField(condition = SqlCondition.LIKE)
	private String name;
	/**
	 * 人员编号
	 */
	@NotBlank(message="人员编号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@TableField(condition = SqlCondition.LIKE)
	private String code;
	/**
	 * 人员类型 对应字典 PERSON_TYPE
	 */
	@NotBlank(message="人员类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String type;
	/**
	 * 是否是招生员 对应字典 YES_NO
	 */
	private String isAgent;
	/**
	 * 性别 对应字典 GENDER
	 */
	private String gender;
	/**
	 * 客户身份证号码
	 */
	@TableField(condition = SqlCondition.LIKE)
	private String idCardNo;
	/**
	 * 居住地
	 */
	@TableField(condition = SqlCondition.LIKE)
	private String residence;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 备注
	 */
	@TableField(condition = SqlCondition.LIKE)
	private String remark;

	@TableField(exist = false)
	private String typeName;

	@TableField(exist = false)
	private String genderName;
}
