<!DOCTYPE html>
<html>
<head>
<title>角色管理</title>
<#include "../../header.ftl">
</head>
<body>
<div id="rrapp" v-cloak>
	<div v-show="showList">
		<div class="grid-btn">
			<div class="form-group col-sm-2">
				<input type="text" class="form-control" v-model="q.roleName" @keyup.enter="query" placeholder="角色名称">
			</div>
			<a class="btn btn-default" @click="query">查询</a>

			<#if shiro.hasPermission("sys:role:save")>
			<a class="btn btn-primary" @click="add"><i class="fa fa-plus"></i>&nbsp;新增</a>
			</#if>
			<#if shiro.hasPermission("sys:role:update")>
			<a class="btn btn-primary" @click="update"><i class="fa fa-pencil-square-o"></i>&nbsp;修改</a>
			</#if>
			<#if shiro.hasPermission("sys:role:delete")>
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
				<div class="col-sm-2 control-label">角色名称</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="role.roleName" placeholder="角色名称"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label">所属部门</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" style="cursor:pointer;" v-model="role.deptName" @click="deptTree" readonly="readonly" placeholder="所属部门"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label">备注</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="role.remark" placeholder="备注"/>
				</div>
			</div>
			<div class="form-inline clearfix" style="margin-top:30px;margin-left:26px;">
				<div class="form-group col-md-6">
					<strong class="col-sm-5 control-label">功能权限</strong>
					<div class="col-sm-10">
						<ul id="menuTree" class="ztree"></ul>
					</div>
				</div>
				<div class="form-group col-md-6">
					<strong class="col-sm-5 control-label">数据权限</strong>
					<div class="col-sm-10">
						<ul id="dataTree" class="ztree"></ul>
					</div>
				</div>
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

<script src="${request.contextPath}/statics/js/modules/sys/role.js?_${.now?long}"></script>
</body>
</html>