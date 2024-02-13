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
					
					<form action="/trans/approve.do" name="Form" id="Form" method="post">
						<input type="hidden" name="TRANS_ID" id="TRANS_ID" value="${pd.TRANS_ID}"/>
						<input type="hidden" name="TYPE" id="TYPE" value="${pd.TYPE}"/>
						<input type="hidden" name="COMMIT_FLAG" id="COMMIT_FLAG" value="${pd.COMMIT_FLAG}"/>
						<input type="hidden" name="AUDIT_LEVEL" id="AUDIT_LEVEL" value="${pd.AUDIT_LEVEL}"/>
						<input type="hidden" name="AUDIT_USER_LEVEL1" id="AUDIT_USER_LEVEL1" value="${pd.AUDIT_USER_LEVEL1}"/>
						<input type="hidden" name="AUDIT_USER_LEVEL2" id="AUDIT_USER_LEVEL2" value="${pd.AUDIT_USER_LEVEL2}"/>
						<input type="hidden" name="AUDIT_USER_LEVEL3" id="AUDIT_USER_LEVEL3" value="${pd.AUDIT_USER_LEVEL3}"/>
						<input type="hidden" name="APPROVE" id="APPROVE" value=""/>
						
						<div id="zhongxin" style="padding-top: 13px;">
						<div>
							<iframe name="daiban" id="daiban" frameborder="0" src="/template/list.do?TABLES_ID=${pd.TYPE}&DATA_DATE=${pd.DATA_DATE}&CORP_ID=${pd.CORP_ID}" style="margin:0 auto;width:100%;height:100%;"></iframe>
						</div>
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="text-align: center;" colspan="10">
									<c:if test="${APPROVE_FLAG == '1'}">
										<a class="btn btn-mini btn-primary" onclick="approve('ok');">审核通过</a>
										<a class="btn btn-mini btn-danger" onclick="approve('ng');">驳回</a>
									</c:if>
									<a class="btn btn-mini btn-primary" onclick="top.Dialog.close();">关闭</a>
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
		function approve(ret){
			$("#APPROVE").val(ret);
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