<title>专项审核计划</title>
<#include "../../component/silly-header.ftl">
<div id="index" class="row">
	<div class="col-xs-12 col-sm-12" @keyup.enter="query">
		<div class="widget-box widget-compact custom-collapsed">
			<div class="widget-header widget-header-blue widget-header-flat" @click="showSearch=!showSearch">
				<h5 class="widget-title lighter">
					<i class="ace-icon fa fa-search nav-search-icon"></i>查询条件
				</h5>
				<div class="widget-toolbar">
                    <span>
                        <i class="ace-icon fa fa-chevron-up"></i>
                    </span>
				</div>
			</div>
			<div class="widget-body">
				<div class="widget-main custom-collapsed-content" :style="{display: + showSearch?'block':'none'}">
					<el-form ref="searchForm" :model="form" label-width="100px" class="form-horizontal" size="mini">
						<el-row>
							<el-col :xs="24" :sm="12" :lg="8">
								<el-form-item label="计划时间" prop="planDate">
									<el-date-picker v-model="form.planDate" type="month" value-format="yyyy-MM"
									                placeholder="时间选择（月份）" style="width: 100%">
									</el-date-picker>
								</el-form-item>
							</el-col>
							<el-col :xs="24" :sm="12" :lg="8">
								<el-form-item label="报告编号" prop="reportCode">
									<el-input v-model="form.reportCode"
									          placeholder="请输入报告编号，按回车键查询">
									</el-input>
								</el-form-item>
							</el-col>
							<el-col :xs="24" :sm="12" :lg="8">
								<el-form-item label="" prop="options">
									<el-button type="primary" icon="el-icon-search" @click="query"
									           :loading="loading">查询
									</el-button>
									<el-button type="warning" icon="el-icon-refresh" @click="reset"
									           :loading="loading">重置
									</el-button>
								</el-form-item>
							</el-col>
						</el-row>
					</el-form>
				</div>
			</div>
		</div>
		<div class="widget-box widget-compact custom-collapsed">
			<el-tabs type="card" v-model="activeName" @tab-click="loadMyGrid">
				<el-tab-pane name="doing">
					<span slot="label"><i class="el-icon-s-claim"></i> 当前任务</span>
					<div class="widget-body">
						<div class="widget-main search_from_padding custom-collapsed-content" style="padding: 0px;">
							<div style="border: solid 0px red; height: 30px;line-height: 30px; padding-left: 5px;background-color: #EFF3F8;">
								&nbsp;&nbsp;
								<span @click="reloadGridTable()" class="pointer"><i
										class="ace-icon fa fa-refresh green bigger-130"></i>&nbsp;刷新</span>
								<shiro:hasPermission name="qmis:specialaudit:add">
									<span @click="formDialog()" class="pointer">
										<i class="ace-icon fa fa-plus-circle purple bigger-130"></i>&nbsp;新增</span>
								</shiro:hasPermission>
							</div>
						</div>
						<table id="doing-grid-table"></table>
						<div id="doing-grid-pager"></div>
					</div>
				</el-tab-pane>
				<el-tab-pane name="history">
					<span slot="label"><i class="el-icon-date"></i> 历史任务</span>
					<div class="widget-body">
						<table id="history-grid-table"></table>
						<div id="history-grid-pager"></div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>
	</div>
</div>
<div id="editDivId"></div>
<script type="text/javascript">

	var indexVue = new SillyIndexVue({
		data: function () {
			return {
				activeName: 'doing',
				newFormUrl: my.base + "/qmis/specialPlan/v1/form-0010.task",
				pageUrl: my.base + '/qmis/specialPlan/doingPage',
				delUrl: my.base + '/qmis/specialPlan/delete/',
				grid_selector: '#doing-grid-table',
				pager_selector: "#doing-grid-pager",
				form: {},
				doingColModel: [
					{name: 'id', key: true, hidden: true, label: ''},
					{name: 'taskId', hidden: true, label: ''},
					{name: 'status', hidden: true, label: ''},
					{name: 'processVersion', hidden: true, label: ''},
					{
						name: 'options', label: '操作', width: 80,
						formatter: function (cellvalue, options, rowObject) {
							var optionHtml = '<span class="pointer" title="查看详情" onclick="indexVue.detailForm(\'' + rowObject.id + '\')">' +
									'<i class="ace-icon fa fa-file-text-o blue bigger-130"></i>&nbsp;</span>' +
									'<span class="pointer" title="处置" onclick="indexVue.formDialog(\'' + rowObject.id + '\')">' +
									'<i class="ace-icon fa fa-edit green bigger-130"></i>&nbsp;</span>';
							if (rowObject.status === '20') {
								optionHtml += '<span class="pointer" title="删除" onclick="indexVue.del(\'' + rowObject.id + '\')">' +
										'<i class="ace-icon fa fa-trash-o red  bigger-130"></i>&nbsp;</span>';
							}
							return optionHtml

						}
					},
					{name: 'reportCode', index: '"reportCode"', label: '计划名称', width: '120'},
					{
						name: 'planDate', index: '"planDate"', label: '计划时间', width: '100',
						formatter: function (cellvalue, options, rowObject) {
							return '<span class="label label-info" style="border-radius: 4px">' + dateUtil.format(new Date(cellvalue), 'yyyy年 MM月') + '</span>';
						}
					},
					{name: 'auditCount', index: '"auditCount"', label: '策划数量', width: '95'}, {
						name: 'taskName', index: '"taskName"', label: '当前状态', width: '95',
						formatter: function (cellvalue, options, rowObject) {
							return '<span class="label label-info" style="border-radius: 4px">' + cellvalue + '</span>';
						}
					},
					{name: 'handleUserName', index: '"handleUserName"', label: '当前处置人', width: '95'}
				],
				historyColModel: [
					{name: 'id', key: true, hidden: true, label: ''},
					{name: 'processVersion', hidden: true, label: ''},
					{name: 'status', hidden: true, label: ''},
					{name: 'createUserId', hidden: true, label: ''},
					{
						name: 'options', label: '操作', width: 80,
						formatter: function (cellvalue, options, rowObject) {
							var optionsHtml = '<span class="pointer" title="查看详情" onclick="indexVue.detailForm(\'' + rowObject.id + '\')">' +
									'<i class="ace-icon fa fa-file-text-o blue bigger-130"></i>&nbsp;</span>';
							if (rowObject.status === '30' && (rowObject.createUserId === userUtil.getUserId() || userUtil.isAdmin())) {
								optionsHtml += '<span class="pointer" title="撤回" onclick="indexVue.reStartProcessForm(\'' + rowObject.id + '\')">' +
										'<i class="ace-icon fa fa-reply red bigger-130"></i>&nbsp;</span>';
							}
							return optionsHtml;
						}
					},
					{name: 'reportCode', index: '"reportCode"', label: '计划名称', width: '120'},
					{
						name: 'planDate', index: '"planDate"', label: '计划时间', width: '100',
						formatter: function (cellvalue, options, rowObject) {
							return '<span class="label label-info" style="border-radius: 4px">' + dateUtil.format(new Date(cellvalue), 'yyyy年 MM月') + '</span>';
						}
					},
					{name: 'auditCount', index: '"auditCount"', label: '策划数量', width: '95'},
					{
						name: 'taskName', index: '"taskName"', label: '当前状态', width: '95',
						formatter: function (cellvalue, options, rowObject) {
							return '<span class="label label-info" style="border-radius: 4px">' + cellvalue + '</span>';
						}
					},
					{name: 'handleUserName', index: '"handleUserName"', label: '当前处置人', width: '95'}
				],
				colModel: []
			};
		},
		methods: {
			init() {
				var that = this;
				setTimeout(function () {
					that.reSizeHeight();
					that.activeName = "doing";
					that.loadMyGrid();
				}, 50);
			},
			loadMyGrid: function (tab) {
				var tabName = this.activeName;
				this.grid_selector = "#" + tabName + "-grid-table";
				this.pager_selector = "#" + tabName + "-grid-pager";
				this.pageUrl = my.base + '/qmis/specialPlan/' + tabName + 'Page';
				this.colModel = this[tabName + 'ColModel'];
				this.form.findUnStart = tabName === 'doing' ? "1" : null;
				this.loadGridTable();
			},
			reStartProcessForm: function (rowId) {
				var that = this;
				my.http.get(my.base + "/qmis/specialPlan/reStartProcessForm.crrcdt").then(function (data) {
					$("#editDivId").html(data);
					setTimeout(function () {
						var rowData = that.getRowData(rowId);
						backFormVue.open({id: rowData.id});
						backFormVue.successFunc = function (data) {
							that.reloadGridTable();
						}
					}, 50);
				}).catch(function (err) {
					that.$notify({
						title: '失败',
						message: err,
						type: 'warning'
					});
				});
			},
			detailForm: function (rowId) {
				var that = this;
				var rowData = that.getRowData(rowId);
				my.http.get(my.base + "/qmis/specialPlan/" + rowData.processVersion + ".info").then(function (data) {
					$("#editDivId").html(data);
					setTimeout(function () {
						detailFormVue.open({id: rowData.id, taskId: rowData.taskId});
						detailFormVue.successFunc = function (data) {
							that.formDialog(rowId);
						}
					}, 50);
				}).catch(function (err) {
					that.$notify({
						title: '失败',
						message: err,
						type: 'warning'
					});
				});
			},
			formDialog: function (rowId) {
				var that = this;
				var rowData = that.getRowData(rowId);
				var formUrl = rowData.taskId ? my.base + "/qmis/specialPlan/" + rowData.taskId + ".task" : that.newFormUrl;
				my.http.get(formUrl).then(function (data) {
					$("#editDivId").html(data);
					setTimeout(function () {
						formVue.open({id: rowData.id, taskId: rowData.taskId});
						formVue.successFunc = function (data) {
							that.reloadGridTable();
						}
					}, 50);
				}).catch(function (err) {
					that.$notify({
						title: '失败',
						message: err,
						type: 'warning'
					});
				});
			}
		}
	});
</script>