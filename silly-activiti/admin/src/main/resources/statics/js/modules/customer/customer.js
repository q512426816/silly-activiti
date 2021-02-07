$(function () {

	$("#jqGrid").jqGrid({
		url: baseURL + 'customer/customer/list',
		datatype: "json",
		colModel: [
			{label: 'id', name: 'id', index: 'id', hidden: true, key: true},
			{label: '客户姓名', name: 'name', index: 'name', width: 80},
			{label: '客户编号', name: 'code', index: 'code', width: 80},
			{label: '客户类型', name: 'type', index: 'type', width: 80},
			{label: '性别', name: 'gender', index: 'gender', width: 80},
			{label: '身份证号码', name: 'idCardNo', index: 'id_card_no', width: 80},
			{label: '居住地', name: 'residence', index: 'residence', width: 80},
			{label: '生日', name: 'birthday', index: 'birthday', width: 80},
			{label: '备注', name: 'remark', index: 'remark', width: 80},
			{label: '创建时间', name: 'createDate', index: 'create_date', width: 80},
			{label: '创建人', name: 'createUserId', index: 'create_user_id', width: 80},
			{label: '更新时间', name: 'updateDate', index: 'update_date', width: 80},
			{label: '更新人', name: 'updateUserId', index: 'update_user_id', width: 80}
		],
		viewrecords: true,
		height: 385,
		rowNum: 10,
		rowList: [10, 30, 50],
		rownumbers: true,
		rownumWidth: 25,
		autowidth: true,
		multiselect: true,
		pager: "#jqGridPager",
		jsonReader: {
			root: "page.list",
			page: "page.currPage",
			total: "page.totalPage",
			records: "page.totalCount"
		},
		prmNames: {
			page: "page",
			rows: "limit",
			order: "order"
		},
		gridComplete: function () {
			//隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
		}
	});
});

var vm = new Vue({
	el: '#rrapp',
	data: {
		q: {
			name: null
		},
		showList: true,
		title: null,
		customer: {},
		customerTypeList: [],
		genderList: []
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.customer = {};

			vm.init();
		},
		update: function (event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";

			vm.getInfo(id);

			vm.init();
		},
		init: function () {
			laydate.render({
				elem: "#birthday"
				, format: 'yyyy-MM-dd HH:mm:ss'
				, type: 'datetime'
				, trigger: 'click'
				, done: function (value) {
					this.date = value;
				}
			});
		},
		saveOrUpdate: function (event) {
			$("#btnSaveOrUpdate").button('loading').delay(1000).queue(function () {
				var url = vm.customer.id == null ? "customer/customer/save" : "customer/customer/update";
				$.ajax({
					type: "POST",
					url: baseURL + url,
					contentType: "application/json",
					data: JSON.stringify(vm.customer),
					success: function (r) {
						if (r.code === 0) {
							layer.msg("操作成功", {icon: 1});
							vm.reload();
							$('#btnSaveOrUpdate').button('reset');
							$('#btnSaveOrUpdate').dequeue();
						} else {
							layer.alert(r.msg);
							$('#btnSaveOrUpdate').button('reset');
							$('#btnSaveOrUpdate').dequeue();
						}
					}
				});
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if (ids == null) {
				return;
			}
			var lock = false;
			layer.confirm('确定要删除选中的记录？', {
				btn: ['确定', '取消'] //按钮
			}, function () {
				if (!lock) {
					lock = true;
					$.ajax({
						type: "POST",
						url: baseURL + "customer/customer/delete",
						contentType: "application/json",
						data: JSON.stringify(ids),
						success: function (r) {
							if (r.code == 0) {
								layer.msg("操作成功", {icon: 1});
								$("#jqGrid").trigger("reloadGrid");
							} else {
								layer.alert(r.msg);
							}
						}
					});
				}
			}, function () {
			});
		},
		getInfo: function (id) {
			$.get(baseURL + "customer/customer/info/" + id, function (r) {
				vm.customer = r.customer;
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				page: page
			}).trigger("reloadGrid");
		}
	}
});