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
					
					<form action="/indexes/${msg}.do" name="Form" id="Form" method="post">
						<input type="hidden" name="INDEXES_ID" id="INDEXES_ID" value="${pd.INDEXES_ID}"/>
						<input type="hidden" name="NULLABLE" id="NULLABLE" value="${pd.NULLABLE}" />
						<input type="hidden" name="RSV_STR2" id="RSV_STR2" value="${pd.RSV_STR2}" />

						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">指标标识:</td>
								<td><input type="text" name="COLUMN_NAME_EN" id="COLUMN_NAME_EN" value="${pd.COLUMN_NAME_EN}" maxlength="100" placeholder="这里输入指标标识" title="指标标识" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">指标标题:</td>
								<td><input type="text" name="COLUMN_NAME_CN" id="COLUMN_NAME_CN" value="${pd.COLUMN_NAME_CN}" maxlength="100" placeholder="这里输入指标标题" title="指标标题" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">指标类型:</td>
								<%-- <td><input type="text" name="DATA_TYPE" id="DATA_TYPE" value="${pd.DATA_TYPE}" maxlength="50" placeholder="这里输入指标类型" title="指标类型" style="width:98%;"/></td> --%>
								<td id="TD_DATA_TYPE">
									<select class="chosen-select form-control" name="DATA_TYPE" id="DATA_TYPE" data-placeholder="请选择指标类型" style="vertical-align:top;" style="width:98%;" >
										<!-- <option value=""></option> -->
										<c:forEach items="${indexesList}" var="item">
											<option value="${item.DICTIONARIES_ID }" <c:if test="${item.DICTIONARIES_ID == pd.DATA_TYPE }">selected</c:if>>${item.NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">指标精度:</td>
								<td><input type="number" name="LENG" id="LENG" value="${pd.LENG}" maxlength="32" placeholder="这里输入指标精度" title="指标精度" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">小数位:</td>
								<td><input type="number" name="DATA_PRECISION" id="DATA_PRECISION" value="${pd.DATA_PRECISION}" maxlength="32" placeholder="这里输入小数位" title="小数位" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">初始化类型:</td>
								<%-- <td><input type="text" name="INITIAL_TYPE" id="INITIAL_TYPE" value="${pd.INITIAL_TYPE}" maxlength="1" placeholder="这里输入初始化类型" title="初始化类型" style="width:98%;"/></td> --%>
								<td id="TD_INITIAL_TYPE">
									<select class="chosen-select form-control" name="INITIAL_TYPE" id="INITIAL_TYPE" data-placeholder="请选择初始化类型" style="vertical-align:top;" style="width:98%;" >
										<!-- <option value=""></option> -->
										<c:forEach items="${dataTypeList}" var="item">
											<option value="${item.DICTIONARIES_ID }" <c:if test="${item.DICTIONARIES_ID == pd.INITIAL_TYPE }">selected</c:if>>${item.NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<%-- <tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">默认值:</td>
								<td><input type="text" name="INITIAL_DATA" id="INITIAL_DATA" value="${pd.INITIAL_DATA}" maxlength="500" placeholder="这里输入默认值" title="默认值" style="width:98%;"/></td>
							</tr> --%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">校验规则:</td>
								<%-- <td><input type="text" name="VALIDATE_RULE" id="VALIDATE_RULE" value="${pd.VALIDATE_RULE}" maxlength="255" placeholder="这里输入校验规则" title="校验规则" style="width:98%;"/></td> --%>
								<td id="TD_VALIDATE_RULE">
									<select class="chosen-select form-control" name="VALIDATE_RULE" id="VALIDATE_RULE" data-placeholder="请选择校验规则" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${ruleList}" var="item">
											<option value="${item.CELLRULE_ID }" <c:if test="${item.CELLRULE_ID == pd.VALIDATE_RULE }">selected</c:if>>${item.NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">控件类型:</td>
								<%--<td><input type="text" name="CONTROL_TYPE" id="CONTROL_TYPE" value="${pd.CONTROL_TYPE}" maxlength="20" placeholder="这里输入显示控件类型" title="显示控件类型" style="width:98%;"/></td>--%>
								<td id="TD_CONTROL_TYPE">
									<select class="chosen-select form-control" name="CONTROL_TYPE" id="CONTROL_TYPE" data-placeholder="请选择控件类型" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${controlTypeList}" var="item">
											<option value="${item.key}" <c:if test="${item.key == pd.CONTROL_TYPE}">selected</c:if>>${item.value}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
                            <tr>
                                <td style="width:75px;text-align: right;padding-top: 13px;">控件子类型:</td>
                                <td id="TD_CONTROL_SUB_TYPE">
                                    <select class="chosen-select form-control" name="CONTROL_SUB_TYPE" id="CONTROL_SUB_TYPE" data-placeholder="请选择控件子类型" style="vertical-align:top;" style="width:98%;" >
                                        <option value=""></option>
                                        <c:forEach items="${controlSubTypeList}" var="item">
                                            <option value="${item.key}" <c:if test="${item.key == pd.CONTROL_SUB_TYPE}">selected</c:if>>${item.value}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td style="width:75px;text-align: right;padding-top: 13px;">控件格式:</td>
                                <td><input type="text" name="CONTROL_FORMAT" id="CONTROL_FORMAT" value="${pd.CONTROL_FORMAT}" maxlength="100" placeholder="这里输入控件格式" title="控件格式" style="width:98%;"/></td>
                            </tr>
                            <tr>
                                <td style="width:75px;text-align: right;padding-top: 13px;">数据来源:</td>
                                <td id="TD_DATA_SOURCE">
                                    <select class="chosen-select form-control" name="DATA_SOURCE" id="DATA_SOURCE" data-placeholder="请选择数据来源" style="vertical-align:top;" style="width:98%;" >
                                        <option value=""></option>
                                        <c:forEach items="${sourcelist}" var="item">
                                            <option value="${item.DICT_ID}" <c:if test="${item.DICT_ID == pd.DATA_SOURCE }">selected</c:if>>${item.NAME}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <%-- <tr>
                                <td style="width:75px;text-align: right;padding-top: 13px;">输入属性:</td>
                                <td><input type="text" name="READ_DISABLE" id="READ_DISABLE" value="${pd.READ_DISABLE}" maxlength="1" placeholder="这里输入输入属性" title="输入属性" style="width:98%;"/></td>
                            </tr>
                            <tr>
                                <td style="width:75px;text-align: right;padding-top: 13px;">指标说明:</td>
                                <td><input type="text" name="REMARK" id="REMARK" value="${pd.REMARK}" maxlength="255" placeholder="这里输入指标说明" title="指标说明" style="width:98%;"/></td>
                            </tr> --%>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">可空:</td>
								<%-- <td><input type="text" name="NULLABLE" id="NULLABLE" value="${pd.NULLABLE}" maxlength="1" placeholder="这里输入可空" title="可空" style="width:98%;"/></td> --%>
								<td>
								<label style="float:left;padding-left: 5px;"><input class="ace" name="form-field-radio" id="form-field-radio2" onclick="setType('1');" <c:if test="${pd.NULLABLE == '1' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">可空</span></label>
								<label style="float:left;padding-left: 12px;"><input class="ace" name="form-field-radio" id="form-field-radio1" onclick="setType('0');" <c:if test="${pd.NULLABLE == '0' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">不可空</span></label>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">加密存储:</td>
								<td>
								<label style="float:left;padding-left: 12px;"><input class="ace" name="form-encrypt-radio" id="form-encrypt-radio1" onclick="setEncryptType('0');" <c:if test="${pd.RSV_STR2 == '0' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">正常</span></label>
								<label style="float:left;padding-left: 5px;"><input class="ace" name="form-encrypt-radio" id="form-encrypt-radio2" onclick="setEncryptType('1');" <c:if test="${pd.RSV_STR2 == '1' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">加密</span></label>
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
			if($("#COLUMN_NAME_EN").val()==""){
				$("#COLUMN_NAME_EN").tips({
					side:3,
		            msg:'请输入指标标识',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COLUMN_NAME_EN").focus();
			return false;
			}
			if($("#COLUMN_NAME_CN").val()==""){
				$("#COLUMN_NAME_CN").tips({
					side:3,
		            msg:'请输入指标标题',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COLUMN_NAME_CN").focus();
			return false;
			}
			if($("#DATA_TYPE").val()==""){
				$("#DATA_TYPE").tips({
					side:3,
		            msg:'请输入指标类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DATA_TYPE").focus();
			return false;
			}
			var mode = '${msg}';
			// new item
			if (mode == 'save' && hasBianma() == true) {
				return false;
			}
			/* if($("#LENG").val()==""){
				$("#LENG").tips({
					side:3,
		            msg:'请输入指标精度',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#LENG").focus();
			return false;
			}
			if($("#DATA_PRECISION").val()==""){
				$("#DATA_PRECISION").tips({
					side:3,
		            msg:'请输入小数位',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#DATA_PRECISION").focus();
			return false;
			}
			if($("#INITIAL_TYPE").val()==""){
				$("#INITIAL_TYPE").tips({
					side:3,
		            msg:'请输入初始化类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INITIAL_TYPE").focus();
			return false;
			}
			if($("#INITIAL_DATA").val()==""){
				$("#INITIAL_DATA").tips({
					side:3,
		            msg:'请输入默认值',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INITIAL_DATA").focus();
			return false;
			}
			if($("#VALIDATE_RULE").val()==""){
				$("#VALIDATE_RULE").tips({
					side:3,
		            msg:'请输入校验规则',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#VALIDATE_RULE").focus();
			return false;
			}
			if($("#CONTROL_TYPE").val()==""){
				$("#CONTROL_TYPE").tips({
					side:3,
		            msg:'请输入显示控件类型',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONTROL_TYPE").focus();
			return false;
			}
			if($("#READ_DISABLE").val()==""){
				$("#READ_DISABLE").tips({
					side:3,
		            msg:'请输入输入属性',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#READ_DISABLE").focus();
			return false;
			}
			if($("#NULLABLE").val()==""){
				$("#NULLABLE").tips({
					side:3,
		            msg:'请输入可空',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NULLABLE").focus();
			return false;
			}
			if($("#CONTROL_FORMAT").val()==""){
				$("#CONTROL_FORMAT").tips({
					side:3,
		            msg:'请输入控件格式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONTROL_FORMAT").focus();
			return false;
			} */
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		//判断指标标识是否存在
		function hasBianma() {
			var COLUMN_NAME_EN = $.trim($("#COLUMN_NAME_EN").val());
			if("" == COLUMN_NAME_EN) return false;

			var url = '/indexes/hasBianma.do';
			var param = "{'COLUMN_NAME_EN':'" + COLUMN_NAME_EN + "','tm':'" + new Date().getTime() + "'}";
	    	var obj = eval('(' + param + ')');
			callAjax(url, false, obj, function(data) {
				if ("success" == data.result) {
					$("#Form").submit();
					$("#zhongxin").hide();
					$("#zhongxin2").show();
					 return false;
				} else if ("err_dup" == data.result) {
					$("#COLUMN_NAME_EN").tips({
						side:3,
			            msg:'指标标识'+COLUMN_NAME_EN+'已存在,重新输入',
			            bg:'#AE81FF',
			            time:5
			        });
					//$('#COLUMN_NAME_EN').val('');
				} else if ("err_sys" == data.result) {
					$("#COLUMN_NAME_EN").tips({
						side:3,
			            msg:'指标标识'+COLUMN_NAME_EN+'和系统预留标识冲突,重新输入',
			            bg:'#AE81FF',
			            time:5
			        });
					//$('#COLUMN_NAME_EN').val('');
				} else if ("err_excep" == data.result) {
					$("#COLUMN_NAME_EN").tips({
						side:3,
			            msg:'系统错误，请稍后再试',
			            bg:'#AE81FF',
			            time:5
			        });
					//$('#COLUMN_NAME_EN').val('');
				 }
				 return true;
			});
			return true;
		}
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		function setType(value){
			$("#NULLABLE").val(value);
		}
		function setEncryptType(value){
			$("#RSV_STR2").val(value);
		}
		</script>
</body>
</html>