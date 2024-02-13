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
					
					<form action="/logs/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="LOGS_ID" id="LOGS_ID" value="${pd.LOGS_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">类型:</td>
								<td><input type="text" name="OPT_TYPE" id="OPT_TYPE" value="${pd.OPT_TYPE}" maxlength="1" placeholder="这里输入类型" title="类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">日志等级:</td>
								<td><input type="text" name="LOG_LEVEL" id="LOG_LEVEL" value="${pd.LOG_LEVEL}" maxlength="1" placeholder="这里输入日志等级" title="日志等级" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">单位:</td>
								<td><input type="text" name="CORP_ID" id="CORP_ID" value="${pd.CORP_ID}" maxlength="100" placeholder="这里输入单位" title="单位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">服务名称:</td>
								<td><input type="text" name="SERVICE_NAME" id="SERVICE_NAME" value="${pd.SERVICE_NAME}" maxlength="100" placeholder="这里输入服务名称" title="服务名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">模块名称:</td>
								<td><input type="text" name="MODULE_NAME" id="MODULE_NAME" value="${pd.MODULE_NAME}" maxlength="100" placeholder="这里输入模块名称" title="模块名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="REMARK" id="REMARK" value="${pd.REMARK}" maxlength="100" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">操作日期:</td>
								<td><input class="span10 date-picker" name="OPT_TIME" id="OPT_TIME" value="${pd.OPT_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="操作日期" title="操作日期" style="width:98%;"/></td>
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
			if($("#OPT_TYPE").val()==""){
				$("#OPT_TYPE").tips({
					side:3,
		            msg:'请输入类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OPT_TYPE").focus();
			return false;
			}
			if($("#LOG_LEVEL").val()==""){
				$("#LOG_LEVEL").tips({
					side:3,
		            msg:'请输入日志等级',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LOG_LEVEL").focus();
			return false;
			}
			if($("#CORP_ID").val()==""){
				$("#CORP_ID").tips({
					side:3,
		            msg:'请输入单位',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CORP_ID").focus();
			return false;
			}
			if($("#SERVICE_NAME").val()==""){
				$("#SERVICE_NAME").tips({
					side:3,
		            msg:'请输入服务名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SERVICE_NAME").focus();
			return false;
			}
			if($("#MODULE_NAME").val()==""){
				$("#MODULE_NAME").tips({
					side:3,
		            msg:'请输入模块名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MODULE_NAME").focus();
			return false;
			}
			if($("#REMARK").val()==""){
				$("#REMARK").tips({
					side:3,
		            msg:'请输入备注',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REMARK").focus();
			return false;
			}
			if($("#OPT_TIME").val()==""){
				$("#OPT_TIME").tips({
					side:3,
		            msg:'请输入操作日期',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OPT_TIME").focus();
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