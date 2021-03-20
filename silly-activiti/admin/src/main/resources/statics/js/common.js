//jqGrid的配置信息
if ($.jgrid) {
	$.jgrid.defaults.width = 1000;
	$.jgrid.defaults.responsive = true;
	$.jgrid.defaults.styleUI = 'Bootstrap';
}


var baseURL = "../../";

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function (name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]);
	return null;
};
T.p = url;

//全局配置
$.ajaxSetup({
	dataType: "json",
	cache: false
});

//重写alert
window.alert = function (msg, callback) {
	parent.layer.alert(msg, function (index) {
		parent.layer.close(index);
		if (typeof(callback) === "function") {
			callback("ok");
		}
	});
}

//重写confirm式样框
window.confirm = function (msg, callback) {
	parent.layer.confirm(msg, {btn: ['确定', '取消']},
		function () {//确定事件
			if (typeof(callback) === "function") {
				callback("ok");
			}
		});
}

//选择一条记录
function getSelectedRow() {
	var grid = $("#jqGrid");
	var rowKey = grid.getGridParam("selrow");
	if (!rowKey) {
		alert("请选择一条记录");
		return;
	}

	var selectedIDs = grid.getGridParam("selarrrow");
	if (selectedIDs.length > 1) {
		alert("只能选择一条记录");
		return;
	}

	return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
	var grid = $("#jqGrid");
	var rowKey = grid.getGridParam("selrow");
	if (!rowKey) {
		alert("请选择一条记录");
		return;
	}

	return grid.getGridParam("selarrrow");
}

//判断是否为空
function isBlank(value) {
	return !value || !/\S/.test(value)
}

function getArrOneAttr(arr, attr) {
	var returnArr = [];
	if (arr instanceof Array && attr) {
		for (var i = 0; i < arr.length; i++) {
			returnArr.push(arr[i][attr]);
		}
	}
	return returnArr;
}


(function () {

	function format(date, fmt) {
		if (!fmt) {
			fmt = 'yyyy-MM-dd hh:mm:ss';
		}
		if (/(y+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
		}

		let o = {
			'M+': date.getMonth() + 1,
			'd+': date.getDate(),
			'h+': date.getHours(),
			'm+': date.getMinutes(),
			's+': date.getSeconds()
		};
		for (let k in o) {
			if (new RegExp(`(${k})`).test(fmt)) {
				let str = o[k] + '';
				fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? str : padLeftZero(str))
			}
		}
		return fmt
	}

	function padLeftZero(str) {
		return ('00' + str).substr(str.length)
	}

	function timeShow(timer) {
		if (!timer) {
			return '';
		}
		timer = timer / 1000;
		let tips = '';
		switch (true) {
			case timer < 60:
				tips = parseInt(timer) + '秒';
				break;
			case timer >= 60 && timer < 3600:
				tips = parseInt(timer / 60) + '分钟';
				break;
			case timer >= 3600 && timer < 86400:
				tips = parseInt(timer / 3600) + '小时';
				break;
			case timer >= 86400 && timer < 2592000:
				tips = parseInt(timer / 86400) + '天';
				break;
			default:
				if (timer >= 2592000 && timer < 365 * 86400) {
					tips = parseInt(timer / (86400 * 30)) + '个月';
				} else {
					tips = parseInt(timer / (86400 * 365)) + '年';
				}
		}
		return tips;
	}


	if (!window.dateUtil) {
		window.dateUtil = {};
	}

	window.dateUtil.format = format;
	window.dateUtil.timeShow = timeShow;

}());

function getDiagWidth() {
	var val = document.body.clientWidth;
	var dialogWidth = "";
	if (val < 768) {
		dialogWidth = '100%'
	} else if (val < 1200) {
		dialogWidth = '80%'
	} else {
		dialogWidth = '70%'
	}
	return dialogWidth;
}

function isMobile() {
	var val = document.body.clientWidth;
	return val < 768;
}