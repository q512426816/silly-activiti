package com.iqiny.example.sillyactiviti.admin.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.iqiny.example.sillyactiviti.admin.common.base.DefaultBaseEntity;
import com.iqiny.example.sillyactiviti.common.validator.group.AddGroup;
import com.iqiny.example.sillyactiviti.common.validator.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 数据字典表
 * 
 * @author xy
 * @email sog_xy@163.com
 * @date 2020-10-26 23:17:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dict")
public class SysDictEntity extends DefaultBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 字典名称
	 */
	@NotNull(message = "字典名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 字典类型
	 */
	@NotNull(message = "字典类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String type;
	/**
	 * 字典码
	 */
	@NotNull(message = "字典码不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String code;
	/**
	 * 字典值
	 */
	@NotNull(message = "字典值不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String value;
	/**
	 * 排序
	 */
	private Integer orderNum;
	/**
	 * 备注
	 */
	private String remark;

}
