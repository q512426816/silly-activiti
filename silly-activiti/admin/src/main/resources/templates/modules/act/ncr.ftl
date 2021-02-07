<!DOCTYPE html>
<html>
<head>
	<title>质量异常</title>
<#include "../../header.ftl">
</head>
<body>
<div id="index" class="ms-index" v-cloak>
	<el-header class="ms-header" height="50px">
		<el-col :span="24">
			<el-button type="primary" icon="el-icon-search" size="mini" @click="showSearch=!showSearch">列表查询</el-button>
		<#if shiro.hasPermission("res:person:save")>
			<el-button type="primary" icon="el-icon-plus" size="mini" @click="save()">新增</el-button>
        </#if>
		<#if shiro.hasPermission("res:person:delete")>
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
							<el-form-item label="人员姓名" prop="name">
								<el-input v-model="form.name"
								          :disabled="false"
								          :clearable="true"
								          placeholder="请输入客户姓名">
								</el-input>
							</el-form-item>
						</el-col>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="人员编号" prop="code">
								<el-input v-model="form.code"
								          :disabled="false"
								          :clearable="true"
								          placeholder="请输入客户编号">
								</el-input>
							</el-form-item>
						</el-col>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="人员类型" prop="type">
								<xh-select v-model="form.type"
								           dict-type="PERSON_TYPE"
								           placeholder="请选择人员类型">
								</xh-select>
							</el-form-item>
						</el-col>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="人员性别" prop="gender">
								<xh-select v-model="form.gender"
								           dict-type="GENDER"
								           placeholder="请选择客户性别">
								</xh-select>
							</el-form-item>
						</el-col>
						<el-col :xs="24" :sm="12" :lg="8">
							<el-form-item label="身份证号码" prop="idCardNo">
								<el-input v-model="form.idCardNo"
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
			<el-table-column label="人员姓名" min-width="150" align="left" prop="name"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="人员编号" min-width="150" align="left" prop="code"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="人员类型" min-width="150" align="left" prop="typeName"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="性别" min-width="150" align="left" prop="genderName"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="身份证号码" min-width="150" align="left" prop="idCardNo"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="居住地" min-width="150" align="left" prop="residence"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="生日" min-width="150" align="left" prop="birthday"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="备注" min-width="150" align="left" prop="remark"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="创建时间" min-width="150" align="left" prop="createDate"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="创建人" min-width="150" align="left" prop="createUserId"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="更新时间" min-width="150" align="left" prop="updateDate"
			                 show-overflow-tooltip></el-table-column>
			<el-table-column label="更新人" min-width="150" align="left" prop="updateUserId"
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
</div>
<#include "ncr-form.ftl">
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
					xh.http.get(xh.base + "/silly/ncr/read/list", Object.assign({}, that.form, page)).then(function (data) {
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
			//不能删除自己
			isChecked: function (row, index) {
				return true;
			},
			//删除
			del: function (row) {
				var that = this;
				that.$confirm('此操作将永久删除所选内容, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					xh.http.post(xh.base + "/res/person/delete", getArrOneAttr(row, "id"), {
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
			//新增
			save: function (id) {
				formVue.open(id);
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