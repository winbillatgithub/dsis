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
					
					<form action="/columns/${msg}.do" name="Form" id="Form" method="post">
						<input type="hidden" name="COLUMNS_ID" id="COLUMNS_ID" value="${pd.COLUMNS_ID}"/>
						<input type="hidden" name="SEARCH" id="SEARCH" value="${pd.SEARCH}" />
						<!-- disable table & index when editing -->
						<c:if test="${msg == 'edit'}">
							<input type="hidden" name="TABLES_ID" id="TABLES_ID" value="${pd.TABLES_ID}" />
							<input type="hidden" name="INDEXES_ID" id="INDEXES_ID" value="${pd.INDEXES_ID}" />
						</c:if>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">模板名称:</td>
								<%-- <td><input type="text" name="TABLES_ID" id="TABLES_ID" value="${pd.TABLES_ID}" maxlength="100" placeholder="这里输入模板名称" title="模板名称" style="width:98%;"/></td> --%>
								<td id="TD_TABLES_ID">
									<select <c:if test="${msg == 'edit'}">disabled='disabled' name='TABLES_ID_DISABLED' id='TABLES_ID_DISABLED'</c:if>
											<c:if test="${msg == 'save'}">name='TABLES_ID' id='TABLES_ID'</c:if>
										class="chosen-select form-control" data-placeholder="请选择表单元素类型" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${tableList}" var="item">
											<option value="${item.TABLES_ID }" <c:if test="${item.TABLES_ID == pd.TABLES_ID }">selected</c:if>>${item.TABLE_NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">表单元素:</td>
								<%-- <td><input type="text" name="INDEXES_ID" id="INDEXES_ID" value="${pd.INDEXES_ID}" maxlength="100" placeholder="这里输入表单元素ID" title="表单元素ID" style="width:98%;"/></td> --%>
								<td id="TD_INDEXES_ID">
									<select <c:if test="${msg == 'edit'}">disabled='disabled' name='INDEXES_ID_DISABLED' id='INDEXES_ID_DISABLED'</c:if>
											<c:if test="${msg == 'save'}">name='INDEXES_ID' id='INDEXES_ID'</c:if>
										class="chosen-select form-control" data-placeholder="请选择表单元素类型" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${indexesList}" var="item">
											<option value="${item.INDEXES_ID }" <c:if test="${item.INDEXES_ID == pd.INDEXES_ID }">selected</c:if>>${item.COLUMN_NAME_CN }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">检索项:</td>
								<td>
								<label style="float:left;padding-left: 12px;"><input class="ace" name="form-field-radio" id="form-field-radio1" onclick="setType('0');" <c:if test="${pd.SEARCH == '0' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">否</span></label>
								<label style="float:left;padding-left: 5px;"><input class="ace" name="form-field-radio" id="form-field-radio2" onclick="setType('1');" <c:if test="${pd.SEARCH == '1' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">是</span></label>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">排序号:</td>
								<td><input type="number" name="SORT_NO" id="SORT_NO" value="${pd.SORT_NO}" maxlength="32" placeholder="这里输入排序号" title="排序号" style="width:98%;"/></td>
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
			if($("#INDEXES_ID").val()==""){
				$("#INDEXES_ID").tips({
					side:3,
		            msg:'请输入表单元素ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INDEXES_ID").focus();
			return false;
			}
			if($("#SORT_NO").val()==""){
				$("#SORT_NO").tips({
					side:3,
		            msg:'请输入排序号',
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
			$("#SEARCH").val(value);
		}
		</script>
</body>
</html>