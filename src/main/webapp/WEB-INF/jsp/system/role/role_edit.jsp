<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>

<!-- jsp文件头和头部 -->
<%@ include file="../index/top.jsp"%>
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
						<form action="/role/${msg}.do" name="form1" id="form1"  method="post">
						<input type="hidden" name="ROLE_ID" id="id" value="${pd.ROLE_ID}"/>
						<input name="PARENT_ID" id="parent_id" value="${pd.parent_id }" type="hidden">
							<div id="zhongxin" style="padding-top:13px;">
							<table class="center" style="width:100%;">
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">角色名:</td>
									<td><input type="text" name="ROLE_NAME" id="roleName" placeholder="这里输入角色名称" value="${pd.ROLE_NAME}"  title="名称" style="width:99%;"/></td>
								</tr>
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">公司:</td>
									<td id="juese">
									<%-- <c:if test="${pd.CORP_ID == null || pd.CORP_ID == ''}"> --%>
										<select class="chosen-select form-control" name="CORP_ID" id="corpId" data-placeholder="请选择公司" style="vertical-align:top;" style="width:98%;" >
										<option value=""></option>
										<c:forEach items="${departmentList}" var="department">
											<option value="${department.CORP_ID}" <c:if test="${pd.CORP_ID == department.CORP_ID }">selected</c:if>>${department.NAME }</option>
										</c:forEach>
										</select>
									<%-- </c:if> --%>
									<%-- <c:if test="${pd.CORP_ID != null && pd.CORP_ID != ''}">
										<input name="CORP_ID" id="corpId" value="${pd.CORP_ID}" type="hidden" />
										<c:set value="${pd.CORP_ID}" var="selectedCorpId"></c:set><!-- 临时变量，选中的分类id -->
										<c:forEach items ="${departmentList}" var = "department" varStatus="status">
								          <c:choose>
								            <c:when test="${department.CORP_ID == selectedCorpId}">
								              <input name="CORP_NAME" id="corp_name" value="${department.NAME}" readonly="readonly" style="width:99%;" />
								            </c:when>
								          </c:choose>
								        </c:forEach>
									</c:if> --%>
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
	<%@ include file="../index/foot.jsp"%>
	<!--提示框-->
	<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		if (typeof top.hangge === "function") {$(top.hangge());}
        //保存
        function save(){
            if($("#corpId").val()==""){
                $("#juese").tips({
                    side:3,
                    msg:'选择组织',
                    bg:'#AE81FF',
                    time:2
                });
                $("#corpId").focus();
                return false;
            }
            if($("#roleName").val()==""){
                $("#roleName").tips({
                    side:3,
                    msg:'请输入',
                    bg:'#AE81FF',
                    time:2
                });
                $("#roleName").focus();
                return false;
            }
            $("#form1").submit();
            $("#zhongxin").hide();
            $("#zhongxin2").show();
        }
	</script>
</body>
</html>

