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
					
					<form action="/node/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="NODE_ID" id="NODE_ID" value="${pd.NODE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">审批组ID:</td>
								<td><input type="text" name="AUDIT_USER_IDS" id="AUDIT_USER_IDS" value="${pd.AUDIT_USER_IDS}" maxlength="50" placeholder="这里输入审批组ID" title="审批组ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">审核人:</td>
								<td><input type="text" name="AUDIT_USER" id="AUDIT_USER" value="${pd.AUDIT_USER}" maxlength="50" placeholder="这里输入审核人" title="审核人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">审核时间:</td>
								<td><input class="span10 date-picker" name="AUDIT_TIME" id="AUDIT_TIME" value="${pd.AUDIT_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="审核时间" title="审核时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">审核状态:</td>
								<td><input type="text" name="AUDIT_STATUS" id="AUDIT_STATUS" value="${pd.AUDIT_STATUS}" maxlength="1" placeholder="这里输入审核状态" title="审核状态" style="width:98%;"/></td>
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
			if($("#AUDIT_USER_IDS").val()==""){
				$("#AUDIT_USER_IDS").tips({
					side:3,
		            msg:'请输入审批组ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AUDIT_USER_IDS").focus();
			return false;
			}
			if($("#AUDIT_USER").val()==""){
				$("#AUDIT_USER").tips({
					side:3,
		            msg:'请输入审核人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AUDIT_USER").focus();
			return false;
			}
			if($("#AUDIT_TIME").val()==""){
				$("#AUDIT_TIME").tips({
					side:3,
		            msg:'请输入审核时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AUDIT_TIME").focus();
			return false;
			}
			if($("#AUDIT_STATUS").val()==""){
				$("#AUDIT_STATUS").tips({
					side:3,
		            msg:'请输入审核状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#AUDIT_STATUS").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>