<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- ico 小图标 -->
<link rel="shortcut icon" href="${request.contextPath}/statics/images/favicon.ico"/>
<link rel="Bookmark" href="${request.contextPath}/statics/images/favicon.ico">
<!-- 图标 -->
<link rel="stylesheet" type="text/css" href="${request.contextPath}/statics/plugins/iconfont/1.0.0/iconfont.css"/>
<script src="${request.contextPath}/statics/plugins/iconfont/1.0.0/iconfont.js"></script>

<link rel="stylesheet" href="${request.contextPath}/statics/css/font-awesome.min.css">
<link rel="stylesheet" href="${request.contextPath}/statics/plugins/element-ui/2.13.0/index.css">

<!-- 引入组件库 -->
<script src="${request.contextPath}/statics/libs/jquery.min.js"></script>
<script src="${request.contextPath}/statics/libs/vue.min.js"></script>
<script src="${request.contextPath}/statics/plugins/vue-cookies/vue-cookies.js"></script>
<script src="${request.contextPath}/statics/plugins/axios-0.18.0/axios.min.js"></script>
<script src="${request.contextPath}/statics/plugins/qs/6.6.0/qs.min.js"></script>
<script src="${request.contextPath}/statics/plugins/jqgrid/grid.locale-cn.js"></script>
<script src="${request.contextPath}/statics/plugins/jqgrid/jquery.jqGrid.min.js"></script>
<script src="${request.contextPath}/statics/plugins/element-ui/2.13.0/index.js"></script>

<!-- 引入本项目库 -->
<script src="${request.contextPath}/statics/silly/my-http.js"></script>
<script src="${request.contextPath}/statics/silly/silly-common.js?t=1000000"></script>

<script>
	/**
	 * F5 仅刷新 iframe 内容
	 * @param e
	 */
	document.onkeydown = function (e) {
		e = window.event || e;
		var keycode = e.keyCode || e.which;
		if (keycode === 116) {
			if (window.event) {
				try {
					e.keyCode = 0;
				} catch (e) {
				}
				e.returnValue = false;
			} else {
				e.preventDefault();
			}
			if (self !== top) {
				location.reload();
			} else {
				// 是否存在刷新按钮
				var refreshIcon = document.querySelector('.el-icon-refresh');
				if (refreshIcon) {
					refreshIcon.click();
				} else {
					location.reload();
				}
			}
		}
	}
</script>

<#--添加自定义组件-->
<#include "my-select.ftl">
<#include "my-upload.ftl">
<#include "my-resume.ftl">