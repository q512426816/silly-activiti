<!DOCTYPE html>
<html>
<head>
	<title>管理员列表</title>
<#include "../../header.ftl">
</head>
<body>
<div id="rrapp" v-cloak>
	<div v-show="showList">
		<div class="grid-btn">
			<div class="form-group col-sm-2">
				<input type="text" class="form-control" v-model="q.username" @keyup.enter="query" placeholder="用户名">
			</div>
			<a class="btn btn-default" @click="query">查询</a>
			<#if shiro.hasPermission("sys:user:save")>
			<a class="btn btn-primary" @click="add"><i class="fa fa-plus"></i>&nbsp;新增</a>
            </#if>
			<#if shiro.hasPermission("sys:user:update")>
			<a class="btn btn-primary" @click="update"><i class="fa fa-pencil-square-o"></i>&nbsp;修改</a>
            </#if>
			<#if shiro.hasPermission("sys:user:delete")>
			<a class="btn btn-primary" @click="del"><i class="fa fa-trash-o"></i>&nbsp;删除</a>
            </#if>
		</div>
		<table id="jqGrid"></table>
		<div id="jqGridPager"></div>
	</div>

	<div v-show="!showList" class="panel panel-default">
		<div class="panel-heading">{{title}}</div>
		<form class="form-horizontal">
			<div class="form-group">
				<div class="col-sm-2 control-label required">用户名</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="user.username" placeholder="登录账号"/>
				</div>
			</div>
			<div class="form-group" v-if="e.isNewData">
				<div class="col-sm-2 control-label required">密码</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="user.password" placeholder="密码"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label required">昵称</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="user.nickName" placeholder="昵称"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label required">所属部门</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" style="cursor:pointer;" v-model="user.deptName"
					       @click="deptTree" readonly="readonly" placeholder="所属部门"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label required">邮箱</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="user.email" placeholder="邮箱"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label">手机号</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="user.mobile" placeholder="手机号"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label">角色</div>
				<div class="col-sm-10">
					<label v-for="role in roleList" class="checkbox-inline">
						<input type="checkbox" :value="role.roleId" v-model="user.roleIdList">{{role.roleName}}
					</label>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label required">状态</div>
				<label class="radio-inline">
					<input type="radio" name="status" value="0" v-model="user.status"/> 禁用
				</label>
				<label class="radio-inline">
					<input type="radio" name="status" value="1" v-model="user.status"/> 正常
				</label>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label"></div>
				<input type="button" class="btn btn-primary" @click="saveOrUpdate" value="确定"/>
				&nbsp;&nbsp;<input type="button" class="btn btn-warning" @click="reload" value="返回"/>
			</div>
		</form>
	</div>
</div>

<!-- 选择部门 -->
<div id="deptLayer" style="display: none;padding:10px;">
	<ul id="deptTree" class="ztree"></ul>
</div>

<script src="${request.contextPath}/statics/js/modules/sys/user.js?_${.now?long}"></script>
</body>
</html>