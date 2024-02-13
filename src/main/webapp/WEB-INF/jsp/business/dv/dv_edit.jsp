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
					
					<form action="/dv/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="STOP" id="STOP" value="${pd.STOP}" />
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">控制器ID:</td>
								<td><input type="text" name="CONTROLLER_ID" id="CONTROLLER_ID" value="${pd.CONTROLLER_ID}" maxlength="50" placeholder="这里输入控制器ID" title="控制器ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">变量名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="100" placeholder="这里输入变量名称" title="变量名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">变量描述:</td>
								<td><input type="text" name="DESCRIPTION" id="DESCRIPTION" value="${pd.DESCRIPTION}" maxlength="150" placeholder="这里输入变量描述" title="变量描述" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">APC位号:</td>
								<td><input type="text" name="APC_POSITION" id="APC_POSITION" value="${pd.APC_POSITION}" maxlength="40" placeholder="这里输入APC位号" title="APC位号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">DCS位号:</td>
								<td><input type="text" name="DCS_POSITION" id="DCS_POSITION" value="${pd.DCS_POSITION}" maxlength="40" placeholder="这里输入DCS位号" title="DCS位号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">停用标志:</td>
								<td>
								<label style="float:left;padding-left: 12px;"><input class="ace" name="form-field-radio" id="form-field-radio1" onclick="setType('0');" <c:if test="${pd.STOP == '0' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">未停用</span></label>
								<label style="float:left;padding-left: 5px;"><input class="ace" name="form-field-radio" id="form-field-radio2" onclick="setType('1');" <c:if test="${pd.STOP == '1' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">已停用</span></label>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">排序码:</td>
								<td><input type="number" name="SORT_NO" id="SORT_NO" value="${pd.SORT_NO}" maxlength="11" placeholder="这里输入排序码" title="排序码" style="width:98%;"/></td>
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
			if($("#CONTROLLER_ID").val()==""){
				$("#CONTROLLER_ID").tips({
					side:3,
		            msg:'请输入控制器ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONTROLLER_ID").focus();
			return false;
			}
			if($("#NAME").val()==""){
				$("#NAME").tips({
					side:3,
		            msg:'请输入变量名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NAME").focus();
			return false;
			}
			if($("#DESCRIPTION").val()==""){
				$("#DESCRIPTION").tips({
					side:3,
		            msg:'请输入变量描述',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DESCRIPTION").focus();
			return false;
			}
			if($("#APC_POSITION").val()==""){
				$("#APC_POSITION").tips({
					side:3,
		            msg:'请输入APC位号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#APC_POSITION").focus();
			return false;
			}
			if($("#DCS_POSITION").val()==""){
				$("#DCS_POSITION").tips({
					side:3,
		            msg:'请输入DCS位号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DCS_POSITION").focus();
			return false;
			}
			if($("#STOP").val()==""){
				$("#STOP").tips({
					side:3,
		            msg:'请输入停用标志',
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
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		function setType(value){
			$("#STOP").val(value);
		}
		</script>
</body>
</html>