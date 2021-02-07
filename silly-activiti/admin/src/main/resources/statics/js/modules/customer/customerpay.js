$(function () {
	$("#jqGrid").jqGrid({
		url: baseURL + 'customer/customerpay/list',
		datatype: "json",
		colModel: [
			{label: 'id', name: 'customerEntity.id', index: 't1.id', hidden: true, key: true},
			{label: '客户姓名', name: 'customerEntity.name', index: 't1.name', width: 80},
			{label: '客户编号', name: 'customerEntity.code', index: 't1.code', width: 80},
			{label: '缴费金额(元)', name: 'payMoney', index: 'pay_money', width: 80},
			{label: '应缴金额(元)', name: 'needPay', index: 'need_pay', width: 80},
			{label: '是否购买保险', name: 'hasInsurance', index: 'has_insurance', width: 80},
			{label: '状态', name: 'status', index: 'status', width: 80},
			{label: '备注', name: 'remark', index: 'remark', width: 80},
			{label: '创建时间', name: 'createDate', width: 80},
			{label: '创建人', name: 'createUserId', width: 80},
			{label: '更新时间', name: 'updateDate', width: 80},
			{label: '更新人', name: 'updateUserId', width: 80}
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
		showList: true,
		title: null,
		customerPayTypeList: [],
		customerPay: {customerEntity: {}}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		update: function (event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";

			vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			$('#btnSaveOrUpdate').button('loading').delay(1000).queue(function () {
				var url = vm.customerPay.id == null ? "customer/customerpay/save" : "customer/customerpay/update";
				$.ajax({
					type: "POST",
					url: baseURL + url,
					contentType: "application/json",
					data: JSON.stringify(vm.customerPay),
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
		getInfo: function (id) {
			$.get(baseURL + "customer/customerpay/info/" + id, function (r) {
				vm.customerPay = r.customerPay;
			});
			$.get(baseURL + "/sys/dict/list", {type: "CUSTOMER_PAY_STATUS"}, function (r) {
				vm.customerPayTypeList = r.page.list;
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