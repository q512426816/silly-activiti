<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>欢迎页</title>
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<#include "header.ftl">
</head>
<body>
<div class="panel panel-default">
	<div class="panel-heading">基本信息</div>
	
	<textarea id="mytextarea"></textarea>
</div>
<script type="text/javascript" src="${request.contextPath}/statics/plugins/tinymce/tinymce.min.js"></script>
<script>
	tinymce.init({
		selector: '#mytextarea',
		language: 'zh_CN',
		height: 650,
		min_height: 400
	});
</script>
</body>
</html>