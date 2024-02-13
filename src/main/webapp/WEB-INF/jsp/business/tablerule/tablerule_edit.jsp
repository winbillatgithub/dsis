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
					
					<form action="/tablerule/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="TABLERULE_ID" id="TABLERULE_ID" value="${pd.TABLERULE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">模板名称:</td>
								<%-- <td><input type="text" name="TABLE_ID" id="TABLE_ID" value="${pd.TABLE_ID}" maxlength="100" placeholder="这里输入模板名称" title="模板名称" style="width:98%;"/></td> --%>
								<td id="TD_TABLES_ID">
									<select class="chosen-select form-control" name="TABLES_ID" id="TABLES_ID" data-placeholder="请选择模板名称" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${tableList}" var="item">
											<option value="${item.TABLES_ID }" <c:if test="${item.TABLES_ID == pd.TABLES_ID }">selected</c:if>>${item.TABLE_NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">规则名称:</td>
								<%-- <td><input type="text" name="RULE_ID" id="RULE_ID" value="${pd.RULE_ID}" maxlength="100" placeholder="这里输入指标ID" title="指标ID" style="width:98%;"/></td> --%>
								<td id="TD_RULE_ID">
									<select class="chosen-select form-control" name="RULE_ID" id="RULE_ID" data-placeholder="请选择指标类型" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${rulesList}" var="item">
											<option value="${item.SINGLETABLEBUSINESSRULE_ID}" <c:if test="${item.SINGLETABLEBUSINESSRULE_ID == pd.RULE_ID }">selected</c:if>>${item.NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">规则内容:</td>
								<td><input type="text" readonly="readonly" name="RULE_CONTENT" id="RULE_CONTENT" value="${pd.RULE_CONTENT}" maxlength="255" placeholder="这里输入规则内容" title="规则内容" style="width:98%;"/></td>
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
			if($("#TABLE_ID").val()==""){
				$("#TABLE_ID").tips({
					side:3,
		            msg:'请输入模板名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TABLE_ID").focus();
			return false;
			}
			if($("#RULE_ID").val()==""){
				$("#RULE_ID").tips({
					side:3,
		            msg:'请输入指标ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#RULE_ID").focus();
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
		</script>
</body>
</html>