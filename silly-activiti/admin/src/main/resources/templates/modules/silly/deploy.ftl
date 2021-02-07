<!DOCTYPE html>
<html>
<head>
	<title>流程部署</title>
    <#include "../../header.ftl">
	<style>
		.svg-container {
			display: inline-block;
			position: relative;
			width: 100%;
			padding-bottom: 100%;
			vertical-align: middle;
			overflow: hidden;
		}
	</style>
</head>
<body>
<div id="index" class="ms-index" v-cloak>
	<el-header class="ms-header" height="50px">
		<el-col :span="24">
			<el-button type="primary" icon="el-icon-search" size="mini" @click="showSearch=!showSearch">列表查询</el-button>
            <#if shiro.hasPermission("enroll:agent:save")>
                <el-button type="primary" icon="el-icon-plus" size="mini" @click="save()">部署流程</el-button>
            </#if>
            <#if shiro.hasPermission("enroll:agent:delete")>
                <el-button type="danger" icon="el-icon-delete" size="mini" @click="del(selectionList)"
                           :disabled="!selectionList.length">删除
                </el-button>
            </#if>
		</el-col>
	</el-header>
	<el-collapse-transition>
		<div v-show="showSearch" class="ms-search" style="padding: 20px 10px 0 10px;">
			<el-row>
				<el-form :model="form" ref="searchForm" label-width="120px" size="mini">
					<el-row>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="流程KEY" prop="key">
								<el-input v-model="form.key" placeholder="请输入流程KEY">
							</el-form-item>
						</el-col>

						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="分类" prop="category">
								<xh-select v-model="form.category"
								           dict-type="GENERALIZE_GRADE"
								           placeholder="请选择分类">
								</xh-select>
							</el-form-item>
						</el-col>

						<el-col :xs="24" :sm="12" :lg="8" style="text-align: right">
							<el-button type="primary" icon="el-icon-search" size="mini" @click="currentPage=1;list()">查询
							</el-button>
							<el-button @click="rest" icon="el-icon-refresh" size="mini">重置</el-button>
						</el-col>
					</el-row>
				</el-form>
			</el-row>
		</div>
	</el-collapse-transition>
	<el-main class="ms-container">
		<el-table v-loading="loading" height="calc(100vh - 68px)" ref="multipleTable" border
		          :data="dataList" tooltip-effect="dark" @selection-change="handleSelectionChange">
			<template slot="empty">{{emptyText}}</template>
			<el-table-column label="序号" type="index" align="center" width="50"></el-table-column>
			<el-table-column label="分类" min-width="150" align="left" prop="category"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="部署ID" min-width="150" align="left" prop="deploymentId"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="KEY" min-width="250" align="left" prop="key"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="流程名称" min-width="250" align="left" prop="name"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="流程图" min-width="130">
				<template slot-scope="scope">
					<el-link type="primary" :underline="false" @click="processImage(scope.row.id)">
						<img :src="xh.base + '/silly/resource/image/' + scope.row.id" width="100">
					</el-link>
				</template>
			</el-table-column>
			<el-table-column label="当前版本" min-width="100" align="left" prop="version"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="部署资源名称" min-width="250" align="left" prop="resourceName"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="流程描述" min-width="250" align="left" prop="description"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="部署时间" min-width="250" align="left" prop="deployTimeStr"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column v-if="optionFixed" type="selection" :selectable="isChecked"
			                 width="40"></el-table-column>
			<el-table-column v-if="optionFixed" label="操作" align="center" width="80">
				<template slot-scope="scope">
					<#if shiro.hasPermission("edu:tuition:update")>
					<el-link type="primary" :underline="false" @click="save(scope.row.id)">
						<i class="iconfont icon-zidingyiguanli" title="编辑"></i>
					</el-link>
					&nbsp;
                    </#if>
					<#if shiro.hasPermission("edu:tuition:delete")>
					<el-link type="danger" :underline="false" @click="del([scope.row])">
						<i class="iconfont icon-lajitong" title="删除"></i>
					</el-link>
                    </#if>
				</template>
			</el-table-column>
			<el-table-column v-if="!optionFixed" type="selection" fixed="right" :selectable="isChecked"
			                 width="40"></el-table-column>
			<el-table-column v-if="!optionFixed" label="操作" fixed="right" align="center" width="160">
				<template slot-scope="scope">
					<#if shiro.hasPermission("edu:tuition:update")>
					<el-link type="primary" :underline="false" @click="save(scope.row.id)">
						<i class="iconfont icon-zidingyiguanli" title="编辑">编辑</i>
					</el-link>
					&nbsp;&nbsp;
                    </#if>
					<#if shiro.hasPermission("edu:tuition:delete")>
					<el-link type="danger" :underline="false" @click="del([scope.row])">
						<i class="iconfont icon-lajitong" title="删除">删除</i>
					</el-link>
                    </#if>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
				background
				:page-sizes="[5, 10, 20, 50, 500]"
				layout="total, sizes, prev, pager, next, jumper"
				:current-page="currentPage"
				:page-size="pageSize"
				:total="total"
				class="ms-pagination"
				@current-change='currentChange'
				@size-change="sizeChange">
		</el-pagination>
	</el-main>
	<el-dialog title="流程图查看" fullscreen="true" :visible.sync="processImageDialogVisible">
		<div>
			<img :src="xh.base + '/silly/resource/image/' + id" width="100%" height="100%">
		</div>
		<span slot="footer" class="dialog-footer">
	    <el-button @click="processImageDialogVisible = false">关闭</el-button>
	  </span>
	</el-dialog>
</div>
<#include "deploy-form.ftl">
<script>
	var indexVue = new Vue({
		el: '#index',
		data: {
			showSearch: false,
			dataList: [],
			selectionList: [],
			total: 0,
			pageSize: 10,
			currentPage: 1,
			loading: true,
			emptyText: '',
			optionFixed: true,
			processImageDialogVisible: false,
			processImageSvg: "",
			id: "",
			form: {}
		},
		methods: {
			list: function () {
				var that = this;
				var page = {
					page: that.currentPage,
					limit: that.pageSize
				};
				var form = JSON.parse(JSON.stringify(that.form));
				for (var key in form) {
					if (!form[key]) {
						delete form[key];
					}
				}
				history.replaceState({
					form: form,
					page: page,
					total: that.total
				}, "");
				that.loading = true;
				setTimeout(function () {
					xh.http.get(xh.base + "/silly/list", Object.assign({}, that.form, page)).then(function (data) {
						that.loading = false;
						if (data.page.totalCount <= 0) {
							that.emptyText = '暂无数据';
							that.dataList = [];
						} else {
							that.emptyText = '';
							that.total = data.page.totalCount;
							that.dataList = data.page.list;
						}
					}).catch(function (err) {
						console.log(err);
					});
				}, 100);
			},
			handleSelectionChange: function (val) {
				this.selectionList = val;
			},
			isChecked: function (row, index) {
				return true;
			},
			del: function (row) {
				var that = this;
				that.$confirm('此操作将永久删除所选内容, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					xh.http.post(xh.base + "/enroll/agent/delete", getArrOneAttr(row, "id"), {
						headers: {
							'Content-Type': 'application/json'
						}
					}).then(function (data) {
						if (data.code === 0) {
							that.$notify({
								type: 'success',
								message: '删除成功!'
							}); //删除成功，刷新列表

							that.list();
						} else {
							that.$notify({
								title: '失败',
								message: data.msg,
								type: 'warning'
							});
						}
					});
				}).catch(function () {
					that.$notify({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			save: function (id) {
				formVue.open(id);
			},
			sizeChange: function (pagesize) {
				this.loading = true;
				this.pageSize = pagesize;
				this.list();
			},
			currentChange: function (currentPage) {
				this.loading = true;
				this.currentPage = currentPage;
				this.list();
			},
			//重置表单
			rest: function () {
				this.currentPage = 1;
				this.$refs.searchForm.resetFields();

				if (history.hasOwnProperty("state") && history.state) {
					this.form = history.state.form;
					this.total = history.state.total;
					this.currentPage = history.state.page.page;
					this.pageSize = history.state.page.limit;
				}

				this.list();
			},
			setOptionFixed: function () {
				this.optionFixed = isMobile();
			},
			processImage: function (id) {
				var that = this;
				that.id = id;
				that.processImageDialogVisible = true;
			}
		},
		created: function () {
			this.setOptionFixed();
			this.list();
		}
	});
</script>
</body>
</html>