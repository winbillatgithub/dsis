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
<!-- LISTLISTRULE_ID&SLAVE_TABLES_ID&SOURCE_TABLES_ID -->
<input type="hidden" name="LISTLISTRULE_ID" id="LISTLISTRULE_ID" value="${pd.LISTLISTRULE_ID}"/>
<input type="hidden" name="SLAVE_TABLES_ID" id="SLAVE_TABLES_ID" value="${pd.SLAVE_TABLES_ID}"/>
<input type="hidden" name="SOURCE_TABLES_ID" id="SOURCE_TABLES_ID" value="${pd.SOURCE_TABLES_ID}"/>
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
					
					<form action="/listlistruledetail/${msg}.do" name="Form" id="Form" method="post">
						<input type="hidden" name="LISTLISTRULEDETAIL_ID" id="LISTLISTRULEDETAIL_ID" value="${pd.LISTLISTRULEDETAIL_ID}"/>
						<input type="hidden" name="LISTLISTRULE_ID" id="LISTLISTRULE_ID" value="${pd.LISTLISTRULE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">从表名称:</td>
								<td id="TD_TABLES_ID">
									<select class="chosen-select form-control" name="SLAVE_TABLES_ID" id="SLAVE_TABLES_ID" 
										onChange="changeSlaveTable()"
										data-placeholder="请选择从表" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${tableList}" var="item">
											<option value="${item.TABLES_ID }" <c:if test="${item.TABLES_ID == pd.SLAVE_TABLES_ID }">selected</c:if>>${item.TABLE_NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">从表字段:</td>
								<!-- TODO winbill 根据表名动态loading -->
								<td id="TD_INDEXES_ID">
									<select class="chosen-select form-control" name="SLAVE_COLUMNS_ID" id="SLAVE_COLUMNS_ID" data-placeholder="请选择字段" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${columnsList}" var="item">
											<option value="${item.COLUMNS_ID }" <c:if test="${item.COLUMNS_ID == pd.SLAVE_COLUMNS_ID }">selected</c:if>>${item.COLUMN_NAME_CN }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">源表名称:</td>
								<td id="TD_TABLES_ID">
									<select class="chosen-select form-control" name="SOURCE_TABLES_ID" id="SOURCE_TABLES_ID" 
										onChange="changeSourceTable()"
										data-placeholder="请选择源表" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${tableList}" var="item">
											<option value="${item.TABLES_ID }" <c:if test="${item.TABLES_ID == pd.SOURCE_TABLES_ID }">selected</c:if>>${item.TABLE_NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">源表字段:</td>
								<td id="TD_INDEXES_ID">
									<select class="chosen-select form-control" name="SOURCE_COLUMNS_ID" id="SOURCE_COLUMNS_ID" data-placeholder="请选择关联字段" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${columnsList}" var="item">
											<option value="${item.COLUMNS_ID }" <c:if test="${item.COLUMNS_ID == pd.SOURCE_COLUMNS_ID }">selected</c:if>>${item.COLUMN_NAME_CN }</option>
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
			if($("#SLAVE_TABLES_ID").val()==""){
				$("#SLAVE_TABLES_ID").tips({
					side:3,
		            msg:'请选择从表',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SLAVE_TABLES_ID").focus();
			return false;
			}
			if($("#SLAVE_COLUMNS_ID").val()==""){
				$("#SLAVE_COLUMNS_ID").tips({
					side:3,
		            msg:'请选择从表字段',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SLAVE_COLUMNS_ID").focus();
			return false;
			}
			if($("#SOURCE_TABLES_ID").val()==""){
				$("#SOURCE_TABLES_ID").tips({
					side:3,
		            msg:'请选择源表',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SOURCE_TABLES_ID").focus();
			return false;
			}
			if($("#SOURCE_COLUMNS_ID").val()==""){
				$("#SOURCE_COLUMNS_ID").tips({
					side:3,
		            msg:'请选择源表字段',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SOURCE_COLUMNS_ID").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		function changeSlaveTable() {
			// 清空列值
			var TABLES_ID = $('#SLAVE_TABLES_ID').val();
			if (TABLES_ID == '' || TABLES_ID == null) {
				$("#SLAVE_COLUMNS_ID").empty();
				$("#SLAVE_COLUMNS_ID").val('');
				return;
			}
			var url = '/columns/getListService.do?tm='+new Date().getTime();
			var param = "{'TABLES_ID':'" + TABLES_ID + "'}";
	    	var obj = eval('(' + param + ')');
			callAjax(url, true, obj, function(dt) {
				if (dt.success == true) {
					$("#SLAVE_COLUMNS_ID").empty();
					//$("#COLUMNS_ID").append("<option value=''></option>");
					var list = eval(dt.data);
					for (var i = 0; i < list.length; i++) {
						var value = list[i]['COLUMN_NAME_CN'];
						var key = list[i]['COLUMNS_ID'];
						$("#SLAVE_COLUMNS_ID").append("<option value='" + key +"'>" + value + "</option>");
					} 
				}
			});

		}
		function changeSourceTable() {
			// 清空列值
			var TABLES_ID = $('#SOURCE_TABLES_ID').val();
			if (TABLES_ID == '' || TABLES_ID == null) {
				$("#SOURCE_COLUMNS_ID").empty();
				$("#SOURCE_COLUMNS_ID").val('');
				return;
			}
			var url = '/columns/getListService.do?tm='+new Date().getTime();
			var param = "{'TABLES_ID':'" + TABLES_ID + "'}";
	    	var obj = eval('(' + param + ')');
			callAjax(url, true, obj, function(dt) {
				if (dt.success == true) {
					$("#SOURCE_COLUMNS_ID").empty();
					var list = eval(dt.data);
					for (var i = 0; i < list.length; i++) { 
						var value = list[i]['COLUMN_NAME_CN'];
						var key = list[i]['COLUMNS_ID'];
						$("#SOURCE_COLUMNS_ID").append("<option value='" + key +"'>" + value + "</option>");
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