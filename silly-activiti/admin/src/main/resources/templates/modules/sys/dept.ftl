<!DOCTYPE html>
<html>
<head>
<title>部门管理</title>
<#include "../../header.ftl">
<link rel="stylesheet" href="${request.contextPath}/statics/css/bootstrap-table.min.css">
<link rel="stylesheet" href="${request.contextPath}/statics/plugins/treegrid/jquery.treegrid.css">
<script src="${request.contextPath}/statics/libs/bootstrap-table.min.js"></script>
<script src="${request.contextPath}/statics/plugins/treegrid/jquery.treegrid.min.js"></script>
<script src="${request.contextPath}/statics/plugins/treegrid/jquery.treegrid.bootstrap3.js"></script>
<script src="${request.contextPath}/statics/plugins/treegrid/jquery.treegrid.extension.js"></script>
<script src="${request.contextPath}/statics/plugins/treegrid/tree.table.js"></script>
</head>
<body>
<div id="rrapp" v-cloak>
	<div v-show="showList">
		<div class="grid-btn">
			<#if shiro.hasPermission("sys:dept:save")>
			<a class="btn btn-primary" @click="add"><i class="fa fa-plus"></i>&nbsp;新增</a>
			</#if>
			<#if shiro.hasPermission("sys:dept:update")>
			<a class="btn btn-primary" @click="update"><i class="fa fa-pencil-square-o"></i>&nbsp;修改</a>
			</#if>
			<#if shiro.hasPermission("sys:dept:delete")>
			<a class="btn btn-primary" @click="del"><i class="fa fa-trash-o"></i>&nbsp;删除</a>
			</#if>
		</div>
		<table id="deptTable" data-mobile-responsive="true" data-click-to-select="true">
			<thead>
			<tr>
				<th data-field="selectItem" data-checkbox="true"></th>
			</tr>
			</thead>
		</table>
	</div>

	<div v-show="!showList" class="panel panel-default">
		<div class="panel-heading">{{title}}</div>
		<form class="form-horizontal">
			<div class="form-group">
				<div class="col-sm-2 control-label">部门名称</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" v-model="dept.name" placeholder="部门名称"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label">上级部门</div>
				<div class="col-sm-10">
					<input type="text" class="form-control" style="cursor:pointer;" v-model="dept.parentName" @click="deptTree" readonly="readonly" placeholder="一级部门"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-2 control-label">排序号</div>
				<div class="col-sm-10">
					<input type="number" class="form-control" v-model="dept.orderNum" placeholder="排序号"/>
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

<script src="${request.contextPath}/statics/js/modules/sys/dept.js?_${.now?long}"></script>
</body>
</html>