﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
							
						<!-- 检索  -->
						<form action="/trans/listDaily.do?FROM=${FROM}&TABLES_ID=${TABLES_ID}" method="post" name="Form" id="Form">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入提交人姓名" class="nav-search-input" id="nav-search-input" autocomplete="off" name="keywords" value="${pd.keywords}" placeholder="这里输入提交人姓名"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="startDate" id="startDate"  value="${pd.startDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="开始日期"/></td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="dueDate" name="dueDate"  value="${pd.dueDate}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="结束日期"/></td>
								<!-- <td style="vertical-align:top;padding-left:2px;">
								 	<select class="chosen-select form-control" name="name" id="id" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
									<option value=""></option>
									<option value="">全部</option>
									<option value="">1</option>
									<option value="">2</option>
								  	</select>
								</td> -->
								<c:if test="${QX.cha == 1 }">
								<td style="vertical-align:top;padding-left:2px"><a class="btn btn-light btn-xs" onclick="tosearch();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								</c:if>
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">	
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">项目名称</th>
									<th class="center">状态</th>
									<th class="center">提交人</th>
									<th class="center">提交时间</th>
									<th class="center">一级审核人</th>
									<th class="center">一审时间</th>
									<th class="center">二级审核人</th>
									<th class="center">二审时间</th>
									<th class="center">三级审核人</th>
									<th class="center">三审时间</th>
									<th class="center">意见</th>
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
											<td class='center'>
												<label class="pos-rel"><input type='checkbox' name='ids' value="${var.TRANS_ID}" class="ace" /><span class="lbl"></span></label>
											</td>
											<c:set var="data_date" value="${fn:substring(var.DATA_DATE, 0, 10)}" />
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class='center'>
											  <a href="javascript:goSondict('${FROM}','${var.TRANS_ID}','${var.TYPE}','${data_date}','${var.CORP_ID}','${var.APPROVE_FLAG}','${var.COMMIT_FLAG}','${var.AUDIT_LEVEL}','${var.AUDIT_USER_LEVEL1}','${var.AUDIT_USER_LEVEL2}','${var.AUDIT_USER_LEVEL3}','${var.COMMIT_USER}')">
												<c:forEach items="${deptList}" var="item">
													<c:if test="${item.CORP_ID == var.CORP_ID }">${data_date}-${item.NAME}-${var.TYPE}</c:if>
												</c:forEach>
											  </a>
											</td>
											<td class='center'>
												<c:if test="${var.COMMIT_FLAG == '0'}"><font color="red">驳回</font></c:if>
												<c:if test="${var.COMMIT_FLAG == '1'}">已保存</c:if>
												<c:if test="${var.COMMIT_FLAG == '2'}">待审核</c:if>
												<c:if test="${var.COMMIT_FLAG == '3'}">一审通过</c:if>
												<c:if test="${var.COMMIT_FLAG == '4'}">二审通过</c:if>
												<c:if test="${var.COMMIT_FLAG == '5'}">三审通过</c:if>
												<c:if test="${var.COMMIT_FLAG == '9'}"><font color="blue">审批完成</font></c:if>
											</td>
											<td class='center'>
												<c:forEach items="${userList}" var="item">
													<c:if test="${item.USER_ID == var.COMMIT_USER }">${item.NAME}</c:if>
												</c:forEach>
											</td>
											<td class='center'>${var.COMMIT_TIME}</td>
											<td class='center'>
												<c:set value="${var.USER_LIST1}" var="list1"></c:set>
												<c:forEach items="${list1}" var="item" varStatus="status">
													<c:if test="${item.AUDIT_STATUS == '0' }"><p><font size="1" color="red">${item.NAME}</font></p></c:if>
													<c:if test="${item.AUDIT_STATUS == '1' }"><p><font size="1" color="blue">${item.NAME}</font></p></c:if>
												</c:forEach>
											</td>
											<td class='center'>${var.AUDIT_TIME_LEVEL1}</td>
											<td class='center'>
												<c:set value="${var.USER_LIST2}" var="list2"></c:set>
												<c:forEach items="${list2}" var="item" varStatus="status">
													<c:if test="${item.AUDIT_STATUS == '0' }"><p><font size="1" color="red">${item.NAME}</font></p></c:if>
													<c:if test="${item.AUDIT_STATUS == '1' }"><p><font size="1" color="blue">${item.NAME}</font></p></c:if>
												</c:forEach>
											</td>
											<td class='center'>${var.AUDIT_TIME_LEVEL2}</td>
											<td class='center'>
												<c:set value="${var.USER_LIST3}" var="list3"></c:set>
												<c:forEach items="${list3}" var="item" varStatus="status">
													<c:if test="${item.AUDIT_STATUS == '0' }"><p><font size="1" color="red">${item.NAME}</font></p></c:if>
													<c:if test="${item.AUDIT_STATUS == '1' }"><p><font size="1" color="blue">${item.NAME}</font></p></c:if>
												</c:forEach>
											</td>
											<td class='center'>${var.AUDIT_TIME_LEVEL3}</td>
											<td class='center'>${var.REJECT_REASON}</td>
											<td class="center">
												<c:if test="${QX.edit != 1 && QX.del != 1 }">
												<span class="label label-large label-grey arrowed-in-right arrowed-in"><i class="ace-icon fa fa-lock" title="无权限"></i></span>
												</c:if>
												<div class="hidden-sm hidden-xs btn-group">
													<c:if test="${QX.del == 1 && (var.COMMIT_FLAG == '0' || var.COMMIT_FLAG == '1')}">
													<a class="btn btn-xs btn-danger" onclick="del('${var.TRANS_ID}','${var.TYPE}');">
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
															<c:if test="${QX.del == 1 && (var.COMMIT_FLAG == '0' || var.COMMIT_FLAG == '1')}">
															<li>
																<a style="cursor:pointer;" onclick="del('${var.TRANS_ID}','${var.TYPE}');" class="tooltip-error" data-rel="tooltip" title="删除">
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
								  <c:if test="${TABLES_ID != null && TABLES_ID != ''}">
									<c:if test="${QX.add == 1 }">
									<a class="btn btn-sm btn-success" onclick="add('${TABLES_ID}');">新增</a>
									</c:if>
									<c:if test="${QX.del == 1 }">
									<%-- <a class="btn btn-sm btn-danger" onclick="makeAll('${TABLES_ID}','确定要删除选中的数据吗?');" title="批量删除" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a> --%>
									</c:if>
								  </c:if>
								  <a class="btn btn-sm btn-success" onclick="toExcel('${FROM}','${TABLES_ID}');">导出</a>
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
	<!-- 下拉框 -->
	<script src="/static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="/static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
	<script type="text/javascript">
        if (typeof top.hangge === "function") {$(top.hangge());}
		//检索
		function tosearch(){
            if (typeof top.jzts === "function") {top.jzts();}
			$("#Form").submit();
		}
		$(function() {
			if (typeof top.hangge === "function") {$(top.hangge());}
			//日期框
			$('.date-picker').datepicker({
				autoclose: true,
				todayHighlight: true
			});
			
			//下拉框
			if(!ace.vars['touch']) {
				$('.chosen-select').chosen({allow_single_deselect:true}); 
				$(window)
				.off('resize.chosen')
				.on('resize.chosen', function() {
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				}).trigger('resize.chosen');
				$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
					if(event_name != 'sidebar_collapsed') return;
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				});
				$('#chosen-multiple-style .btn').on('click', function(e){
					var target = $(this).find('input[type=radio]');
					var which = parseInt(target.val());
					if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
					 else $('#form-field-select-4').removeClass('tag-input-style');
				});
			}
			
			
			//复选框全选控制
			var active_class = 'active';
			$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
				var th_checked = this.checked;//checkbox inside "TH" table header
				$(this).closest('table').find('tbody > tr').each(function(){
					var row = this;
					if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
					else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
				});
			});
		});
		
		//新增
		function add(TABLES_ID){
			 if (typeof top.jzts === "function") {top.jzts();}
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '/trans/goNew.do?TABLES_ID=' + TABLES_ID;
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 if (typeof top.jzts === "function") {top.jzts();}
						 setTimeout("self.location=self.location",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function del(TRANS_ID, TABLES_ID){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					if (typeof top.jzts === "function") {top.jzts();}
					var url = "/trans/delete.do?TRANS_ID="+TRANS_ID+"&TABLES_ID="+TABLES_ID+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		
		//修改
		function edit(TRANS_ID, APPROVE_FLAG){
			 if (typeof top.jzts === "function") {top.jzts();}
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '/trans/goEdit.do?TRANS_ID='+TRANS_ID+'&APPROVE_FLAG='+APPROVE_FLAG;
			 diag.Width = 800;
			 diag.Height = 600;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//批量操作
		function makeAll(TABLES_ID, msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
					  if(document.getElementsByName('ids')[i].checked){
					  	if(str=='') str += document.getElementsByName('ids')[i].value;
					  	else str += ',' + document.getElementsByName('ids')[i].value;
					  }
					}
					if(str==''){
						bootbox.dialog({
							message: "<span class='bigger-110'>您没有选择任何内容!</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
						$("#zcheckbox").tips({
							side:1,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}else{
						if(msg == '确定要删除选中的数据吗?'){
							if (typeof top.jzts === "function") {top.jzts();}
							$.ajax({
								type: "POST",
								url: '/trans/deleteAll.do?TABLES_ID='+TABLES_ID+'&tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									 $.each(data.list, function(i, list){
											nextPage(${page.currentPage});
									 });
								}
							});
						}
					}
				}
			});
		};
		//跳转到每日填报页面
		// TRANS_ID,TYPE,COMMIT_FLAG,AUDIT_LEVEL,AUDIT_USER_LEVEL1,AUDIT_USER_LEVEL2,AUDIT_USER_LEVEL3
		function goSondict(FROM,TRANS_ID, TABLES_ID, DATA_DATE, CORP_ID, APPROVE_FLAG,COMMIT_FLAG,AUDIT_LEVEL,AUDIT_USER_LEVEL1,AUDIT_USER_LEVEL2,AUDIT_USER_LEVEL3,COMMIT_USER) {
			if (typeof top.jzts === "function") {top.jzts();}
			var url = "/list/list.do?TRANS_ID="+TRANS_ID+"&TABLES_ID="+TABLES_ID+"&DATA_DATE="+DATA_DATE+"&CORP_ID="+CORP_ID+"&APPROVE_FLAG="+APPROVE_FLAG+"&COMMIT_USER="+COMMIT_USER;
			var url2 = "&COMMIT_FLAG="+COMMIT_FLAG+"&AUDIT_LEVEL="+AUDIT_LEVEL+"&AUDIT_USER_LEVEL1="+AUDIT_USER_LEVEL1+"&AUDIT_USER_LEVEL2="+AUDIT_USER_LEVEL2+"&AUDIT_USER_LEVEL3="+AUDIT_USER_LEVEL3;
			url = url + url2;
			if (FROM != null && FROM != '' && FROM != 'undefined') {
				url += ("&FROM=" + FROM);
			}
			window.location.href=url;
		};
		//导出excel
		function toExcel(FROM,TABLES_ID){
 			window.location.href='/trans/listDailyExcel.do?FROM='+FROM+'&TABLES_ID='+TABLES_ID;
			return;
		}
	</script>


</body>
</html>