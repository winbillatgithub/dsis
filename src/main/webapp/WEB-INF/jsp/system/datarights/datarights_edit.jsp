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
					
					<form action="/datarights/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">角色:</td>
								<%-- <td><input type="text" name="ROLE_ID" id="ROLE_ID" value="${pd.ROLE_ID}" maxlength="100" placeholder="这里输入角色ID" title="角色ID" style="width:98%;"/></td> --%>
								<td id="juese">
									<select class="chosen-select form-control" name="ROLE_ID" id="ROLE_ID" data-placeholder="请选择角色" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${roleList}" var="role">
											<option value="${role.ROLE_ID }" <c:if test="${role.ROLE_ID == pd.ROLE_ID }">selected</c:if>>${role.ROLE_NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">表名:</td>
								<td><input type="text" name="TABLE_NAME" id="TABLE_NAME" value="${pd.TABLE_NAME}" maxlength="160" placeholder="这里输入表名" title="表名" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">权限:</td>
								<td><input type="text" name="RIGHTS" id="RIGHTS" value="${pd.RIGHTS}" maxlength="255" placeholder="这里输入权限" title="权限" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">操作符:</td>
								<td><input type="text" name="OPERATOR" id="OPERATOR" value="${pd.OPERATOR}" maxlength="50" placeholder="这里输入操作符" title="操作符" style="width:98%;"/></td>
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
			if($("#ROLE_ID").val()==""){
				$("#juese").tips({
					side:3,
		            msg:'选择角色',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ROLE_ID").focus();
				return false;
			}
			if($("#TABLE_NAME").val()==""){
				$("#TABLE_NAME").tips({
					side:3,
		            msg:'请输入表名',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TABLE_NAME").focus();
				return false;
			}
			if($("#RIGHTS").val()==""){
				$("#RIGHTS").tips({
					side:3,
		            msg:'请输入权限',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#RIGHTS").focus();
				return false;
			}
			if($("#OPERATOR").val()==""){
				$("#OPERATOR").tips({
					side:3,
		            msg:'请输入操作符',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OPERATOR").focus();
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