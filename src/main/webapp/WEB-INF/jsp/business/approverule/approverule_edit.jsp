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
		<base href="<%=basePath%>">
		<!-- jsp文件头和头部 -->
		<%@ include file="../../system/index/top.jsp"%>
		
	
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
					
					<form action="/approverule/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="APPROVERULE_ID" id="APPROVERULE_ID" value="${pd.APPROVERULE_ID}"/>
						<input type="hidden" name="PARENT_ID" id="PARENT_ID" value="${null == pd.PARENT_ID ? APPROVERULE_ID:pd.PARENT_ID}"/>
						<input type="hidden" name="NUMBER" id="NUMBER" value="${NUMBER}"/>
						<div id="zhongxin">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">上级:</td>
								<td>
									<div class="col-xs-4 label label-lg label-light arrowed-in arrowed-right">
										<b>${null == pds.NAME ?'(无) 此项为顶级':pds.NAME}</b>
									</div>
								</td>
							</tr>

						    <c:if test="${NUMBER == 0}">
							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">规则名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="50" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							</c:if>
							<c:if test="${NUMBER == 1}">
							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">节点名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="50" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							</c:if>
							<c:if test="${NUMBER == 2}">
							<tr style="display:none">
								<td style="width:70px;text-align: right;padding-top: 13px;">角色名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="50" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							</c:if>

							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">
								  <c:if test="${NUMBER == 0}">
								    模板:
								  </c:if>
								  <c:if test="${NUMBER == 1}">
								    节点类型:
								  </c:if>
								  <c:if test="${NUMBER == 2}">
								    角色编码:
								  </c:if>
								
								</td>
								<td>
								  <c:if test="${NUMBER == 0}">
							        <%-- <input type="text" name="NAME_EN" id="NAME_EN" value="${pd.NAME_EN}" maxlength="50" placeholder="这里输入英文" title="英文" style="width:98%;"/> --%>
								    <select class="chosen-select form-control" name="NAME_EN" id="NAME_EN" onchange="changeTable(this)" data-placeholder="请选择模板" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${tableList}" var="item">
											<option value="${item.TABLES_ID }" <c:if test="${item.TABLES_ID == pd.NAME_EN }">selected</c:if>>${item.TABLE_NAME }</option>
										</c:forEach>
									</select>
								  </c:if>
								  <c:if test="${NUMBER == 1}">
								    <select class="chosen-select form-control" name="NAME_EN" id="NAME_EN" data-placeholder="请选择节点类型" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${nodeTypeList}" var="item">
											<option value="${item.DICTIONARIES_ID }" <c:if test="${item.DICTIONARIES_ID == pd.NAME_EN }">selected</c:if>>${item.NAME }</option>
										</c:forEach>
									</select>
								  </c:if>
								  <c:if test="${NUMBER == 2}">
							        <%-- <input type="text" name="NAME_EN" id="NAME_EN" value="${pd.NAME_EN}" maxlength="50" placeholder="这里输入英文" title="英文" style="width:98%;"/> --%>
							        <select class="chosen-select form-control" name="NAME_EN" id="NAME_EN" onchange="changeRole(this)" data-placeholder="请选择角色" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${roleList}" var="item">
											<option value="${item.ROLE_ID }" <c:if test="${item.ROLE_ID == pd.NAME_EN }">selected</c:if>>${item.ROLE_NAME }</option>
										</c:forEach>
									</select>
								  </c:if>
								</td>
							</tr>

							<tr style="display:none">
								<td style="width:70px;text-align: right;padding-top: 13px;">编码:</td>
								<td><input type="text" name="BIANMA" id="BIANMA" value="${pd.BIANMA}" maxlength="32" placeholder="这里输入编码 (不重复, 禁止修改)" title="编码" style="width:76%;" <c:if test="${null != pd.BIANMA}">readonly="readonly"</c:if> <c:if test="${null == pd.BIANMA}">onblur="hasBianma();"</c:if> /></td>
							</tr>

							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">排序:</td>
								<td><input type="number" name="ORDER_BY" id="ORDER_BY" value="${pd.ORDER_BY}" maxlength="32" placeholder="这里输入排序" title="排序"/></td>
							</tr>
							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">备注:</td>
								<td>
									<textarea rows="3" cols="46" name="BZ" id="BZ" placeholder="这里输入备注" title="备注"  style="width:98%;">${pd.BZ}</textarea>
								</td>
							</tr>
							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">排查表:</td>
								<td><input type="text" name="TBSNAME" id="TBSNAME" value="${pd.TBSNAME}" maxlength="100" placeholder="这里输入表名, 多个用逗号分隔(非必录)" title="排查表" style="width:98%;"/></td>
							</tr>
							<tr>
								<td colspan="10" class="center"><p class="text-warning bigger-110 orange">排查表：删除此条数据时会去此表查询是否被占用(是:禁止删除)</p></td>
							</tr>
							<tr>
								<td class="center" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save('${NUMBER}');">保存</a>
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
	<!--提示框-->
	<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		if (typeof top.hangge === "function") {$(top.hangge());}
		//保存
		function save(NUMBER){
			if($("#NAME").val()==""){
				$("#NAME").tips({
					side:3,
		            msg:'请输入名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NAME").focus();
			return false;
		}
		if (NUMBER != '2') {
			if($("#NAME_EN").val()==""){
				$("#NAME_EN").tips({
					side:3,
		            msg:'请输入英文',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NAME_EN").focus();
				return false;
			}
		}
			/* if($("#BIANMA").val()==""){
				$("#BIANMA").tips({
					side:3,
		            msg:'请输入编码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BIANMA").focus();
			return false;
		} */
			if($("#ORDER_BY").val()==""){
				$("#ORDER_BY").tips({
					side:3,
		            msg:'请输入数字',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORDER_BY").focus();
			return false;
		}
			
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
			
		}
		
		//判断编码是否存在
		function hasBianma(){
			return true;
			var BIANMA = $.trim($("#BIANMA").val());
			if("" == BIANMA)return;
			$.ajax({
				type: "POST",
				url: '/approverule/hasBianma.do',
		    	data: {BIANMA:BIANMA,tm:new Date().getTime()},
				dataType:'json',
				cache: false,
				success: function(data){
					 if("success" == data.result){
					 }else{
						$("#BIANMA").tips({
							side:1,
				            msg:'编码'+BIANMA+'已存在,重新输入',
				            bg:'#AE81FF',
				            time:5
				        });
						$('#BIANMA').val('');
					 }
				}
			});
		}

		function changeRole(obj) {
			var txt = $(obj).find("option:selected").text();
			$("#NAME").val(txt);
		}

		function changeTable(obj) {
			var val = $(obj).find("option:selected").val();
			$("#BIANMA").val(val);
		}
		</script>
</body>
</html>