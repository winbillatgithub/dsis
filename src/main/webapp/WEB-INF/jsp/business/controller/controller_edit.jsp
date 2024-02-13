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
					
					<form action="/controller/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">系统ID:</td>
								<td><input type="text" name="SYSTEM_ID" id="SYSTEM_ID" value="${pd.SYSTEM_ID}" maxlength="50" placeholder="这里输入系统ID" title="系统ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">父控制器ID:</td>
								<td><input type="text" name="PARENT_ID" id="PARENT_ID" value="${pd.PARENT_ID}" maxlength="50" placeholder="这里输入父控制器ID" title="父控制器ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="100" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">功能描述:</td>
								<td><input type="text" name="FUNCTION_DESC" id="FUNCTION_DESC" value="${pd.FUNCTION_DESC}" maxlength="100" placeholder="这里输入功能描述" title="功能描述" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">投用状态位号:</td>
								<td><input type="text" name="POSITIONS" id="POSITIONS" value="${pd.POSITIONS}" maxlength="100" placeholder="这里输入投用状态位号" title="投用状态位号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">投用状态位号数值:</td>
								<td><input type="text" name="POSITIONS_VALUE" id="POSITIONS_VALUE" value="${pd.POSITIONS_VALUE}" maxlength="100" placeholder="这里输入投用状态位号数值" title="投用状态位号数值" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">子控制器标志:</td>
								<td><input type="text" name="HAS_CHILDREN" id="HAS_CHILDREN" value="${pd.HAS_CHILDREN}" maxlength="1" placeholder="这里输入子控制器标志" title="子控制器标志" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">停用原因类型:</td>
								<td><input type="text" name="STOP_TYPE" id="STOP_TYPE" value="${pd.STOP_TYPE}" maxlength="100" placeholder="这里输入停用原因类型" title="停用原因类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">停用详情:</td>
								<td><input type="text" name="STOP_DETAIL" id="STOP_DETAIL" value="${pd.STOP_DETAIL}" maxlength="500" placeholder="这里输入停用详情" title="停用详情" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">记录停用标志:</td>
								<td><input type="text" name="STOP" id="STOP" value="${pd.STOP}" maxlength="1" placeholder="这里输入记录停用标志" title="记录停用标志" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">排序码:</td>
								<td><input type="number" name="SORT_NO" id="SORT_NO" value="${pd.SORT_NO}" maxlength="32" placeholder="这里输入排序码" title="排序码" style="width:98%;"/></td>
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
			if($("#SYSTEM_ID").val()==""){
				$("#SYSTEM_ID").tips({
					side:3,
		            msg:'请输入系统ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SYSTEM_ID").focus();
			return false;
			}
			if($("#PARENT_ID").val()==""){
				$("#PARENT_ID").tips({
					side:3,
		            msg:'请输入父控制器ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PARENT_ID").focus();
			return false;
			}
			if($("#NAME").val()==""){
				$("#NAME").tips({
					side:3,
		            msg:'请输入名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NAME").focus();
			return false;
			}
			/* if($("#POSITIONS").val()==""){
				$("#POSITIONS").tips({
					side:3,
		            msg:'请输入投用状态位号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#POSITIONS").focus();
			return false;
			}
			if($("#POSITIONS_VALUE").val()==""){
				$("#POSITIONS_VALUE").tips({
					side:3,
		            msg:'请输入投用状态位号数值',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#POSITIONS_VALUE").focus();
			return false;
			}
			if($("#HAS_CHILDREN").val()==""){
				$("#HAS_CHILDREN").tips({
					side:3,
		            msg:'请输入子控制器标志',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#HAS_CHILDREN").focus();
			return false;
			}
			if($("#STOP_TYPE").val()==""){
				$("#STOP_TYPE").tips({
					side:3,
		            msg:'请输入停用原因类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOP_TYPE").focus();
			return false;
			}
			if($("#STOP_DETAIL").val()==""){
				$("#STOP_DETAIL").tips({
					side:3,
		            msg:'请输入停用详情',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOP_DETAIL").focus();
			return false;
			}
			if($("#STOP").val()==""){
				$("#STOP").tips({
					side:3,
		            msg:'请输入记录停用标志',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STOP").focus();
			return false;
			}
			if($("#SORT_NO").val()==""){
				$("#SORT_NO").tips({
					side:3,
		            msg:'请输入排序码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SORT_NO").focus();
			return false;
			} */
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