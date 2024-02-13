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
					
					<form action="/singletablebusinessrule/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="SINGLETABLEBUSINESSRULE_ID" id="SINGLETABLEBUSINESSRULE_ID" value="${pd.SINGLETABLEBUSINESSRULE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">规则名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="100" placeholder="这里输入规则名称" title="规则名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">目标字段:</td>
								<%-- <td><input type="text" name="OUT_FIELD" id="OUT_FIELD" value="${pd.OUT_FIELD}" maxlength="100" placeholder="这里输入目标字段" title="目标字段" style="width:98%;"/></td> --%>
								<td id="TD_OUT_FIELD">
									<select class="chosen-select form-control" name="OUT_FIELD" id="OUT_FIELD" data-placeholder="请选择目标字段" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${indexesList}" var="item">
											<option value="${item.INDEXES_ID }" <c:if test="${item.DIRECTIONARIES_ID == pd.OUT_FIELD }">selected</c:if>>${item.COLUMN_NAME_CN }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">输入字段1:</td>
								<%-- <td><input type="text" name="IN_FIELD1" id="IN_FIELD1" value="${pd.IN_FIELD1}" maxlength="100" placeholder="这里输入输入字段1" title="输入字段1" style="width:98%;"/></td> --%>
								<td id="TD_IN_FIELD1">
									<select class="chosen-select form-control" name="IN_FIELD1" id="IN_FIELD1" data-placeholder="请选择输入字段1" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${indexesList}" var="item">
											<option value="${item.INDEXES_ID }" <c:if test="${item.DIRECTIONARIES_ID == pd.IN_FIELD1 }">selected</c:if>>${item.COLUMN_NAME_CN }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">运算符:</td>
								<%-- <td><input type="text" name="OPERATOR_FIELD" id="OPERATOR_FIELD" value="${pd.OPERATOR_FIELD}" maxlength="50" placeholder="这里输入运算符" title="运算符" style="width:98%;"/></td> --%>
								<td id="TD_OPERATOR_FIELD">
									<select class="chosen-select form-control" name="OPERATOR_FIELD" id="OPERATOR_FIELD" data-placeholder="请选择运算符" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${operatorList}" var="item">
											<option value="${item.DICTIONARIES_ID }" <c:if test="${item.DICTIONARIES_ID == pd.OPERATOR_FIELD }">selected</c:if>>${item.NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">输入字段2:</td>
								<%-- <td><input type="text" name="IN_FIELD2" id="IN_FIELD2" value="${pd.IN_FIELD2}" maxlength="100" placeholder="这里输入输入字段2" title="输入字段2" style="width:98%;"/></td> --%>
								<td id="TD_IN_FIELD2">
									<select class="chosen-select form-control" name="IN_FIELD2" id="IN_FIELD2" data-placeholder="请选择输入字段2" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${indexesList}" var="item">
											<option value="${item.INDEXES_ID }" <c:if test="${item.DIRECTIONARIES_ID == pd.IN_FIELD2 }">selected</c:if>>${item.COLUMN_NAME_CN }</option>
										</c:forEach>
									</select>
								</td>
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
			if($("#NAME").val()==""){
				$("#NAME").tips({
					side:3,
		            msg:'请输入规则名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NAME").focus();
			return false;
			}
			if($("#OUT_FIELD").val()==""){
				$("#OUT_FIELD").tips({
					side:3,
		            msg:'请输入目标字段',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OUT_FIELD").focus();
			return false;
			}
			if($("#IN_FIELD1").val()==""){
				$("#IN_FIELD1").tips({
					side:3,
		            msg:'请输入输入字段1',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IN_FIELD1").focus();
			return false;
			}
			if($("#OPERATOR_FIELD").val()==""){
				$("#OPERATOR_FIELD").tips({
					side:3,
		            msg:'请输入运算符',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OPERATOR_FIELD").focus();
			return false;
			}
			if($("#IN_FIELD2").val()==""){
				$("#IN_FIELD2").tips({
					side:3,
		            msg:'请输入输入字段2',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IN_FIELD2").focus();
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