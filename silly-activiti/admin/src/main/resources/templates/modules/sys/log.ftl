<!DOCTYPE html>
<html>
<head>
	<title>日志查询</title>
<#include "../../header.ftl">
</head>
<body>
<div id="index" class="ms-index" v-cloak>
	<el-header class="ms-header" height="50px">
		<el-col :span="24">
			<el-button type="primary" icon="el-icon-search" size="mini" @click="showSearch=!showSearch">列表查询</el-button>
		</el-col>
	</el-header>
	<el-collapse-transition>
		<div v-show="showSearch" class="ms-search" style="padding: 20px 10px 0 10px;">
			<el-row>
				<el-form :model="form" ref="searchForm" label-width="120px" size="mini">
					<el-row>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="账号名称" prop="username">
								<el-input v-model="form.username"
								          :disabled="false"
								          :clearable="true"
								          placeholder="请输入账号名称">
								</el-input>
							</el-form-item>
						</el-col>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="IP地址" prop="ip">
								<el-input v-model="form.ip"
								          :disabled="false"
								          :clearable="true"
								          placeholder="请输入IP地址">
								</el-input>
							</el-form-item>
						</el-col>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="记录类型" prop="type">
								<xh-select v-model="form.type"
								           dict-type="LOG_TYPE"
								           placeholder="请选择记录类型">
								</xh-select>
							</el-form-item>
						</el-col>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="请求参数" prop="params">
								<el-input v-model="form.params"
								          :disabled="false"
								          :clearable="true"
								          placeholder="请输入请求参数">
								</el-input>
							</el-form-item>
						</el-col>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="异常信息" prop="exceptionInfo">
								<el-input v-model="form.exceptionInfo"
								          :disabled="false"
								          :clearable="true"
								          placeholder="请输入身份证号码">
								</el-input>
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
			<template slot="empty">
				{{emptyText}}
			</template>
			<el-table-column label="序号" type="index" align="center" width="50"></el-table-column>
			<el-table-column label="操作账号" min-width="150" align="left" prop="username"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="IP地址" min-width="150" align="left" prop="ip"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="记录类型" min-width="80" align="left" prop="typeName"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="uri" min-width="250" align="left" prop="uri"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="执行时长(毫秒)" min-width="150" align="left" prop="time"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="请求方法" min-width="150" align="left" prop="method"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="请求参数" min-width="150" align="left" prop="params"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="用户代理信息" min-width="150" align="left" prop="userAgent"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="异常信息" min-width="150" align="left" prop="exceptionInfo"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="发生时间" min-width="150" align="left" prop="createDate"
			                 show-overflow-tooltip></el-table-column>
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
</div>

<script>
	var indexVue = new Vue({
		el: '#index',
		data: {
			//是否显示查询
			showSearch: false,
			//数据列表
			dataList: [],
			//管理员管理列表
			selectionList: [],
			//管理员管理列表选中
			total: 0,
			//总记录数量
			pageSize: 10,
			//页面数量
			currentPage: 1,
			loading: true,
			//加载状态
			emptyText: '',
			//提示文字
			optionFixed: true,
			//搜索表单
			form: {}
		},
		methods: {
			//查询列表
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
					xh.http.get(xh.base + "/sys/log/list", Object.assign({}, that.form, page)).then(function (data) {
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
			//管理员管理列表选中
			handleSelectionChange: function (val) {
				this.selectionList = val;
			},
			//表格数据转换
			//pageSize改变时会触发
			sizeChange: function (pagesize) {
				this.loading = true;
				this.pageSize = pagesize;
				this.list();
			},
			//currentPage改变时会触发
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