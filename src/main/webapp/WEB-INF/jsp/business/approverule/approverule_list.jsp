<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>


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
							
						<!-- 检索  -->
						<form action="/approverule/list.do" method="post" name="Form" id="Form">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input" id="keywords" name="keywords" autocomplete="off" value="${page.pd.keywords }" />
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
								</td>
								<td>&nbsp;
									<select name="APPROVERULE_ID" id="APPROVERULE_ID">
										<option value="${APPROVERULE_ID}" <c:if test="${APPROVERULE_ID != ''}">selected</c:if>>本级</option>
										<option value="" <c:if test="${APPROVERULE_ID == ''}">selected</c:if>>全部</option>
									</select>
								</td>
								<c:if test="${QX.cha == 1 }">
								<td style="vertical-align:top;padding-left:2px"><a class="btn btn-light btn-xs" onclick="gsearch();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								</c:if>
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">	
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									
									<th class="center">
								  	<c:if test="${NUMBER == 0}">
									    规则名称
									  </c:if>
									  <c:if test="${NUMBER == 1}">
									    节点名称
									  </c:if>
									  <c:if test="${NUMBER == 2}">
									    角色名称
									  </c:if>
									</th>
									<c:if test="${NUMBER == 0}">
									<th class="center">模板</th>
									</c:if>
									<c:if test="${NUMBER == 1}">
									<th class="center">节点类型</th>
									</c:if>
									<c:if test="${NUMBER == 2}">
									<th class="center" style="display:none">角色编码</th>
									</c:if>

									<c:if test="${NUMBER == 4}">
									<th class="center">编码</th>
									</c:if>
									<th class="center">排序</th>
									<th class="center">操作</th>
								</tr>
							</thead>
													
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty varList}">
									<c:if test="${QX.cha == 1 }">
									<c:forEach items="${varList}" var="var" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											
											
											<c:if test="${NUMBER == 0 || NUMBER == 1}">
												<td class='center'><a href="javascript:goSondict('${var.APPROVERULE_ID }','${NUMBER}','ADD')"><i class="ace-icon fa fa-share bigger-100"></i>&nbsp;${var.NAME}</a></td>
											</c:if>
											<c:if test="${NUMBER == 2}">
												<td class='center'>
												${var.NAME}
												</td>
											</c:if>
											
											<c:if test="${NUMBER == 0}">
											<td class='center'><a href="javascript:goSondict('${var.APPROVERULE_ID }','${NUMBER}','ADD')">
												<c:forEach items="${tableList}" var="item">
													<c:if test="${item.TABLES_ID == var.NAME_EN }">${item.TABLE_NAME }</c:if>
												</c:forEach>
											</a></td>
											</c:if>
											<c:if test="${NUMBER == 1}">
											<td class='center'><a href="javascript:goSondict('${var.APPROVERULE_ID }','${NUMBER}','ADD')">
												<c:forEach items="${nodeTypeList}" var="item">
													<c:if test="${item.DICTIONARIES_ID == var.NAME_EN }">${item.NAME }</c:if>
												</c:forEach>
											</a></td>
											</c:if>
											<c:if test="${NUMBER == 2}">
											<td class='center' style="display:none"><a href="javascript:goSondict('${var.APPROVERULE_ID }','${NUMBER}','ADD')">
												<c:forEach items="${roleList}" var="item">
													<c:if test="${item.ROLE_ID == var.NAME_EN }">${item.ROLE_NAME }</c:if>
												</c:forEach>
											</a></td>
											</c:if>
											
											<c:if test="${NUMBER == 4}">
											<td class='center'>${var.BIANMA}</td>
											</c:if>
											
											<td class='center'>${var.ORDER_BY}</td>
											<td class="center">
												<c:if test="${QX.edit != 1 && QX.del != 1 }">
												<span class="label label-large label-grey arrowed-in-right arrowed-in"><i class="ace-icon fa fa-lock" title="无权限"></i></span>
												</c:if>
												<div class="hidden-sm hidden-xs btn-group">
													<c:if test="${QX.edit == 1 }">
													<a class="btn btn-xs btn-success" title="编辑" onclick="edit('${var.APPROVERULE_ID}','${NUMBER}');">
														<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
													</a>
													</c:if>
													<c:if test="${QX.del == 1 }">
													<a class="btn btn-xs btn-danger" onclick="del('${var.APPROVERULE_ID}','${NUMBER}');">
														<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
													</a>
													</c:if>
												</div>
												<div class="hidden-md hidden-lg">
													<div class="inline pos-rel">
														<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
															<i class="ace-icon fa fa-cog icon-only bigger-110"></i>
														</button>
			
														<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
															<c:if test="${QX.edit == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="edit('${var.APPROVERULE_ID}','${NUMBER}');" class="tooltip-success" data-rel="tooltip" title="修改">
																	<span class="green">
																		<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
															<c:if test="${QX.del == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="del('${var.APPROVERULE_ID}');" class="tooltip-error" data-rel="tooltip" title="删除">
																	<span class="red">
																		<i class="ace-icon fa fa-trash-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
														</ul>
													</div>
												</div>
											</td>
										</tr>
									
									</c:forEach>
									</c:if>
									<c:if test="${QX.cha == 0 }">
										<tr>
											<td colspan="100" class="center">您无权查看</td>
										</tr>
									</c:if>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="100" class="center" >没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						<div class="page-header position-relative">
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;">
									<c:if test="${QX.add == 1 }">
									<a class="btn btn-sm btn-success" onclick="add('${APPROVERULE_ID}','${NUMBER}');">新增</a>
									</c:if>
									<c:if test="${null != pd.APPROVERULE_ID && pd.APPROVERULE_ID != ''}">
									<a class="btn btn-sm btn-success" onclick="goSondict('${pd.PARENT_ID}','${NUMBER}','MINUS');">返回</a>
									</c:if>
								</td>
								<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
							</tr>
						</table>
						</div>
						</form>
					
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->


		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="/static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="/static/ace/js/ace/ace.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		if (typeof top.hangge === "function") {$(top.hangge());}//关闭加载状态
		//检索
		function gsearch(){
			if (typeof top.jzts === "function") {top.jzts();}
			$("#Form").submit();
		}
		
		//去此ID下子列表
		function goSondict(APPROVERULE_ID,NUMBER,ADD){
			if (typeof top.jzts === "function") {top.jzts();}
			var i = 0;
			NUMBER = parseInt(NUMBER);
			if (ADD == 'ADD') {
				i = i + NUMBER + 1;
			} else {
				i = i + NUMBER - 1;
			}
			window.location.href="/approverule/list.do?APPROVERULE_ID="+APPROVERULE_ID+"&NUMBER="+i;
		};
		
		//新增
		function add(APPROVERULE_ID,NUMBER){
			if (typeof top.jzts === "function") {top.jzts();}
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '/approverule/goAdd.do?APPROVERULE_ID='+APPROVERULE_ID+"&NUMBER="+NUMBER;
			 diag.Width = 500;
			 diag.Height = 500;
			 diag.CancelEvent = function(){ //关闭事件
				 if('none' == diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display){
					 parent.location.href="/approverule/listAllDict.do?APPROVERULE_ID=${APPROVERULE_ID}&NUMBER=${NUMBER}&dnowPage=${page.currentPage}";
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function del(Id,NUMBER){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					if (typeof top.jzts === "function") {top.jzts();}
					var url = "/approverule/delete.do?APPROVERULE_ID="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						
						if("success" == data.result){
							parent.location.href="/approverule/listAllDict.do?APPROVERULE_ID=${APPROVERULE_ID}&NUMBER=${NUMBER}&dnowPage=${page.currentPage}";
						}else if("false" == data.result){
							if (typeof top.hangge === "function") {$(top.hangge());}
							bootbox.dialog({
								message: "<span class='bigger-110'>删除失败！请先删除子级或删除占用资源.</span>",
								buttons: 			
								{
									"button" :
									{
										"label" : "确定",
										"className" : "btn-sm btn-success"
									}
								}
							});
						}else if("false2" == data.result){
							if (typeof top.hangge === "function") {$(top.hangge());}
							bootbox.dialog({
								message: "<span class='bigger-110'>删除失败！排查表不存在或其表中没有BIANMA字段.</span>",
								buttons: 			
								{
									"button" :
									{
										"label" : "确定",
										"className" : "btn-sm btn-success"
									}
								}
							});
						}
					});
				}
			});
		}
		
		//修改
		function edit(Id, NUMBER){
			if (typeof top.jzts === "function") {top.jzts();}
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '/approverule/goEdit.do?APPROVERULE_ID='+Id+"&NUMBER="+NUMBER;
			 diag.Width = 500;
			 diag.Height = 500;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 parent.location.href="/approverule/listAllDict.do?APPROVERULE_ID=${APPROVERULE_ID}&NUMBER=${NUMBER}&dnowPage=${page.currentPage}";
				}
				diag.close();
			 };
			 diag.show();
		}
		
	</script>


</body>
</html>