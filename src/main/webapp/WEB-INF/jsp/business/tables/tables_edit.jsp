<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
	<script src="/static/js/common/common.js"></script>
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
					
					<form action="/tables/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="TABLES_ID" id="TABLES_ID" value="${pd.TABLES_ID}"/>
						<input type="hidden" name="TABLE_NAME_EN_OLD" id="TABLE_NAME_EN_OLD" value="${pd.TABLE_NAME_EN}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">模板名称:</td>
								<td><input type="text" name="TABLE_NAME" id="TABLE_NAME" value="${pd.TABLE_NAME}" maxlength="128" placeholder="这里输入模板名称" title="模板名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">模板ID:</td>
								<td><input type="text" name="TABLE_NAME_EN" id="TABLE_NAME_EN" value="${pd.TABLE_NAME_EN}" onblur="return hasBianma();" maxlength="100" placeholder="这里输入模板ID" title="模板ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">模板描述:</td>
								<td><input type="text" name="REMARK" id="REMARK" value="${pd.REMARK}" maxlength="100" placeholder="这里输入模板描述" title="模板描述" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">状态:</td>
								<td><input type="text" name="STATUS" id="STATUS" value="${pd.STATUS}" maxlength="50" placeholder="这里输入状态" title="状态" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="/static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
						
					</form>
	
					<div id="zhongxin2" class="center" style="display:none"><img src="/static/images/jzx.gif" style="width: 50px;" /><br/><h4 class="lighter block green"></h4></div>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
	</div>
	<!-- /.main-content -->
</div>
<!-- /.main-container -->


	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="/static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="/static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		if (typeof top.hangge === "function") {$(top.hangge());}
		//保存
		function save(){
			if($("#TABLE_NAME").val()==""){
				$("#TABLE_NAME").tips({
					side:3,
		            msg:'请输入模板名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TABLE_NAME").focus();
			return false;
			}
			if($("#TABLE_NAME_EN").val()==""){
				$("#TABLE_NAME_EN").tips({
					side:3,
		            msg:'请输入模板ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TABLE_NAME_EN").focus();
			return false;
			}
			// 小写字母，下划线
			var TABLE_NAME_EN = $.trim($("#TABLE_NAME_EN").val());
			var bRet = regExpCheck(TABLE_NAME_EN, '^[a-z_]{1}[a-z0-9_]{4,49}$');
			if (!bRet) {
				$("#TABLE_NAME_EN").tips({
					side:3,
		            msg:'模板ID只能包含小写字母、数字和下划线, 且不能以数字开头, 长度范围5-50',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TABLE_NAME_EN").focus();
				return false;
			}
//			if($("#REMARK").val()==""){
//				$("#REMARK").tips({
//					side:3,
//		            msg:'请输入模板描述',
//		            bg:'#AE81FF',
//		            time:2
//		        });
//				$("#REMARK").focus();
//			return false;
//			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		//判断是否存在
		function hasBianma(){
			var TABLE_NAME_EN = $.trim($("#TABLE_NAME_EN").val());
			if("" == TABLE_NAME_EN)return;
			// 小写字母，下划线
			var bRet = regExpCheck(TABLE_NAME_EN, '^[a-z_]{1}[a-z0-9_]{4,49}$');
			if (!bRet) {
				$("#TABLE_NAME_EN").tips({
					side:3,
		            msg:'模板ID只能包含小写字母、数字和下划线, 且不能以数字开头, 长度范围5-50',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TABLE_NAME_EN").focus();
				return false;
			}
			$.ajax({
				type: "POST",
				url: '/tables/hasBianma.do',
		    	data: {TABLE_NAME_EN:TABLE_NAME_EN,tm:new Date().getTime()},
				dataType:'json',
				cache: false,
				success: function(data){
					 if("success" == data.result){
					 }else{
						$("#TABLE_NAME_EN").tips({
							side:1,
				            msg:'模板ID:'+TABLE_NAME_EN+'已存在,重新输入',
				            bg:'#AE81FF',
				            time:2
				        });
						$("#TABLE_NAME_EN").focus();
					 }
				}
			});
		}
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>