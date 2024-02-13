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
					
					<form action="/multitablerule/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="MULTITABLERULE_ID" id="MULTITABLERULE_ID" value="${pd.MULTITABLERULE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">规则名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="100" placeholder="这里输入规则名称" title="规则名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">模板名称:</td>
								<%-- <td><input type="text" name="TABLES_ID" id="TABLES_ID" value="${pd.TABLES_ID}" maxlength="100" placeholder="这里输入模板名称" title="模板名称" style="width:98%;"/></td> --%>
								<td id="TD_TABLES_ID">
									<select class="chosen-select form-control" name="TABLES_ID" id="TABLES_ID" 
										onChange="changeTable()"
										data-placeholder="请选择模板" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${tableList}" var="item">
											<option value="${item.TABLES_ID }" <c:if test="${item.TABLES_ID == pd.TABLES_ID }">selected</c:if>>${item.TABLE_NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">字段:</td>
								<!-- TODO winbill 根据表名动态loading -->
								<%-- <td><input type="text" name="COLUMNS_ID" id="COLUMNS_ID" value="${pd.COLUMNS_ID}" maxlength="100" placeholder="这里输入字段" title="字段" style="width:98%;"/></td> --%>
								<td id="TD_INDEXES_ID">
									<select class="chosen-select form-control" name="COLUMNS_ID" id="COLUMNS_ID" data-placeholder="请选择字段" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${columnsList}" var="item">
											<option value="${item.COLUMNS_ID }" <c:if test="${item.COLUMNS_ID == pd.COLUMNS_ID }">selected</c:if>>${item.COLUMN_NAME_CN }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关联模板名称:</td>
								<%-- <td><input type="text" name="JOIN_TABLES_ID" id="JOIN_TABLES_ID" value="${pd.JOIN_TABLES_ID}" maxlength="100" placeholder="这里输入关联模板名称" title="关联模板名称" style="width:98%;"/></td> --%>
								<td id="TD_TABLES_ID">
									<select class="chosen-select form-control" name="JOIN_TABLES_ID" id="JOIN_TABLES_ID" 
										onChange="changeJoinTable()"
										data-placeholder="请选择关联模板" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${tableList}" var="item">
											<option value="${item.TABLES_ID }" <c:if test="${item.TABLES_ID == pd.JOIN_TABLES_ID }">selected</c:if>>${item.TABLE_NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关联字段:</td>
								<%-- <td><input type="text" name="JOIN_COLUMNS_ID" id="JOIN_COLUMNS_ID" value="${pd.JOIN_COLUMNS_ID}" maxlength="100" placeholder="这里输入关联字段" title="关联字段" style="width:98%;"/></td> --%>
								<td id="TD_INDEXES_ID">
									<select class="chosen-select form-control" name="JOIN_COLUMNS_ID" id="JOIN_COLUMNS_ID" data-placeholder="请选择关联字段" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${columnsList}" var="item">
											<option value="${item.COLUMNS_ID }" <c:if test="${item.COLUMNS_ID == pd.COLUMNS_ID }">selected</c:if>>${item.COLUMN_NAME_CN }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关联条件:</td>
								<td><input type="text" name="JOIN_CONDITION" id="JOIN_CONDITION" value="${pd.JOIN_CONDITION}" maxlength="100" placeholder="这里输入关联条件" title="关联条件" style="width:98%;"/></td>
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
			if($("#TABLES_ID").val()==""){
				$("#TABLES_ID").tips({
					side:3,
		            msg:'请输入模板名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TABLES_ID").focus();
			return false;
			}
			if($("#COLUMNS_ID").val()==""){
				$("#COLUMNS_ID").tips({
					side:3,
		            msg:'请输入字段',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COLUMNS_ID").focus();
			return false;
			}
			if($("#JOIN_TABLES_ID").val()==""){
				$("#JOIN_TABLES_ID").tips({
					side:3,
		            msg:'请输入关联模板名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#JOIN_TABLES_ID").focus();
			return false;
			}
			if($("#JOIN_COLUMNS_ID").val()==""){
				$("#JOIN_COLUMNS_ID").tips({
					side:3,
		            msg:'请输入关联字段',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#JOIN_COLUMNS_ID").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		function changeTable() {
			// 清空列值
			var TABLES_ID = $('#TABLES_ID').val();
			if (TABLES_ID == '' || TABLES_ID == null) {
				$("#COLUMNS_ID").empty();
				$("#COLUMNS_ID").val('');
				return;
			}
			var url = '/columns/getListService.do?tm='+new Date().getTime();
			var param = "{'TABLES_ID':'" + TABLES_ID + "'}";
	    	var obj = eval('(' + param + ')');
			callAjax(url, true, obj, function(dt) {
				if (dt.success == true) {
					$("#COLUMNS_ID").empty();
					//$("#COLUMNS_ID").append("<option value=''></option>");
					var list = eval(dt.data);
					for (var i = 0; i < list.length; i++) {
						var value = list[i]['COLUMN_NAME_CN'];
						var key = list[i]['COLUMNS_ID'];
						$("#COLUMNS_ID").append("<option value='" + key +"'>" + value + "</option>");
					} 
				}
			});

		}
		function changeJoinTable() {
			// 清空列值
			var TABLES_ID = $('#JOIN_TABLES_ID').val();
			if (TABLES_ID == '' || TABLES_ID == null) {
				$("#JOIN_COLUMNS_ID").empty();
				$("#JOIN_COLUMNS_ID").val('');
				return;
			}
			var url = '/columns/getListService.do?tm='+new Date().getTime();
			var param = "{'TABLES_ID':'" + TABLES_ID + "'}";
	    	var obj = eval('(' + param + ')');
			callAjax(url, true, obj, function(dt) {
				if (dt.success == true) {
					$("#JOIN_COLUMNS_ID").empty();
					//$("#JOIN_COLUMNS_ID").append("<option value=''></option>");
					var list = eval(dt.data);
					for (var i = 0; i < list.length; i++) { 
						var value = list[i]['COLUMN_NAME_CN'];
						var key = list[i]['COLUMNS_ID'];
						$("#JOIN_COLUMNS_ID").append("<option value='" + key +"'>" + value + "</option>");
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