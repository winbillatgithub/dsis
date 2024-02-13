<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>

<!-- 下拉框 -->
<link rel="stylesheet" href="/static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="/static/ace/css/datepicker.css" />
<link rel="stylesheet" href="/static/jquery.drag/drag.css" />
<style type="text/css">
.card-container .item {
	border: 2px solid black;
	margin:10px 0 0;
	height:80px;
}

.card-container .current {
	border: 2px dashed red;
}

.tool-container{
	
}

.tool-container .item {
	border: 3px solid;
	margin:5px 0 0;
	clear:both;
}
</style>
<script src="/static/js/common/common.js"></script>
</head>
<body>
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<div class="row">
			<!-- 左侧card面板区域 -->
			<div class="container card-container col-md-8">
				<div class="row item current">
					<label>表名:sys_logs</label> 
					<input id="hiddenTableName" type="hidden" value="sys_logs" />
				</div>
				<div id="LOGS_ID" class="row item">主键</div>
				<div id="OPT_TYPE" class="row item">类型</div>
				<div id="LOG_LEVEL" class="row item">日志等级</div>
				<div id="CORP_ID" class="row item">单位</div>

			</div>
			<!-- 右侧工具栏区域 -->
			<div class="tool-container col-md-3">
				 <div class="sidebar">
					<div class="container tool-container">
						<div class="btn btn-default item" data-field-type="Fields::TextField" >单行文字</div>
						<div class="btn btn-default item" data-field-type="Fields::TextField" >多行文字</div>
						<div class="btn btn-default item" data-field-type="Fields::TextField" >数字</div>
						<div class="btn btn-default item" data-field-type="Fields::TextField" >日期</div>
					</div>
				 </div>
			</div>
		</div>

	</div>

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="/static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="/static/ace/js/ace/ace.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
	<!-- <script type="text/javascript" src="/static/jquery.drag/drag.js?ver=1"></script> -->
	<script type="text/javascript">
		if (typeof top.hangge === "function") {$(top.hangge());}//关闭加载状态
		(function($){
			
			$.prototype.drag = funciton(opts){
					var me,defaults,options;
				    me = this;
				    defaults = {
				        target: '.card-container .item', //默认的可以放置的目标class
				        draggable: false,
				        placeholder: 'drop here',
				        callback: false,
				        containerClass: 'dad-container',
				        childrenClass: 'dads-children',
				        cloneClass: 'dads-children-clone',
				        active: true
				    };
				    options = $.extend({}, defaults, opts);
				    $(this).each(function() {
			            var mouse,
			                target,
			                dragClass,
			                active,
			                callback,
			                placeholder,
			                daddy,
			                childrenClass,
			                jQclass,
			                cloneClass;
			            
				    });
				    return this;
				}
		})(jQuery);
		
		
		
		$(function(){ 
			var d = $('.jq22').dad();
			var target = $('.dropzone');

			d.addDropzone(target, function(e){
				e.find('span').appendTo(target);
				e.remove();
			});
		});
		
		
		
		
		
	</script>


</body>
</html>