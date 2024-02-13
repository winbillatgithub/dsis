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
					
					<form action="/device/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ID" id="ID" value="${pd.ID}"/>
						<input type="hidden" name="STOP" id="STOP" value="${pd.STOP}" />
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">公司:</td>
								<%-- <td><input type="text" name="CORP_ID" id="CORP_ID" value="${pd.CORP_ID}" maxlength="100" placeholder="这里输入公司ID" title="公司ID" style="width:98%;"/></td> --%>
								<td id="juese">
									<select class="chosen-select form-control" name="CORP_ID" id="CORP_ID" data-placeholder="请选择公司" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${departmentList}" var="item">
											<option value="${item.CORP_ID }" <c:if test="${item.CORP_ID == pd.CORP_ID }">selected</c:if>>${item.NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">装置名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="100" placeholder="这里输入装置名称" title="装置名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">装置类型:</td>
								<td><input type="text" name="CATEGORY" id="CATEGORY" value="${pd.CATEGORY}" maxlength="100" placeholder="这里输入装置类型" title="装置类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">处理能力:</td>
								<td><input type="text" name="CAPACITY" id="CAPACITY" value="${pd.CAPACITY}" maxlength="100" placeholder="这里输入处理能力" title="处理能力" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">投产时间:</td>
								<td><input class="span10 date-picker" name="PRODUCT_TIME" id="PRODUCT_TIME" value="${pd.PRODUCT_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="投产时间" title="投产时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">工艺情况:</td>
								<td><input type="text" name="ART" id="ART" value="${pd.ART}" maxlength="150" placeholder="这里输入工艺情况" title="工艺情况" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">控制系统类型:</td>
								<td><input type="text" name="CS_KIND" id="CS_KIND" value="${pd.CS_KIND}" maxlength="100" placeholder="这里输入控制系统类型" title="控制系统类型" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">DCS_PLC厂商:</td>
								<td><input type="text" name="DCS_PRODUCER" id="DCS_PRODUCER" value="${pd.DCS_PRODUCER}" maxlength="100" placeholder="这里输入DCS_PLC厂商" title="DCS_PLC厂商" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">排序码:</td>
								<td><input type="number" name="SORT_NO" id="SORT_NO" value="${pd.SORT_NO}" maxlength="32" placeholder="这里输入排序码" title="排序码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">停用标志:</td>
								<%-- <td><input type="text" name="STOP" id="STOP" value="${pd.STOP}" maxlength="1" placeholder="这里输入停用标志" title="停用标志" style="width:98%;"/></td> --%>
								<td>
								<label style="float:left;padding-left: 12px;"><input class="ace" name="form-field-radio" id="form-field-radio1" onclick="setType('0');" <c:if test="${pd.STOP == '0' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">未停用</span></label>
								<label style="float:left;padding-left: 5px;"><input class="ace" name="form-field-radio" id="form-field-radio2" onclick="setType('1');" <c:if test="${pd.STOP == '1' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">已停用</span></label>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="REMARK" id="REMARK" value="${pd.REMARK}" maxlength="100" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
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
			if($("#CORP_ID").val()==""){
				$("#CORP_ID").tips({
					side:3,
		            msg:'请输入公司ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CORP_ID").focus();
			return false;
			}
			if($("#NAME").val()==""){
				$("#NAME").tips({
					side:3,
		            msg:'请输入装置名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NAME").focus();
			return false;
			}
			if($("#CATEGORY").val()==""){
				$("#CATEGORY").tips({
					side:3,
		            msg:'请输入装置类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CATEGORY").focus();
			return false;
			}
			if($("#CAPACITY").val()==""){
				$("#CAPACITY").tips({
					side:3,
		            msg:'请输入处理能力',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CAPACITY").focus();
			return false;
			}
			if($("#PRODUCT_TIME").val()==""){
				$("#PRODUCT_TIME").tips({
					side:3,
		            msg:'请输入投产时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#PRODUCT_TIME").focus();
			return false;
			}
			if($("#ART").val()==""){
				$("#ART").tips({
					side:3,
		            msg:'请输入工艺情况',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ART").focus();
			return false;
			}
			if($("#CS_KIND").val()==""){
				$("#CS_KIND").tips({
					side:3,
		            msg:'请输入控制系统类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CS_KIND").focus();
			return false;
			}
			if($("#DCS_PRODUCER").val()==""){
				$("#DCS_PRODUCER").tips({
					side:3,
		            msg:'请输入DCS_PLC厂商',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DCS_PRODUCER").focus();
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