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

<!-- 下拉框 -->
<link rel="stylesheet" href="/static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="/static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
    <%--弹出选择隐藏项--%>
    <input type="hidden" name="DICT_KEY" id="DICT_KEY" value="${DICT_KEY}" />
    <input type="hidden" name="DICT_VALUE" id="DICT_VALUE" value="${DICT_VALUE}" />
    <input type="hidden" name="DBL_CLICK" id="DBL_CLICK" value="" />
    <input type="hidden" name="DICT_KEY" value="${DICT_KEY}" />
    <input type="hidden" name="DICT_VALUE" value="${DICT_VALUE}" />

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">

                        <form action="/card/goPopupDict.do" method="post" name="Form" id="Form">
                            <input type="hidden" id="param" name="param" value="" />
                        <!-- 检索  -->
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="" id="keywords" autocomplete="off" name="keywords" value="${pd.keywords }" />
											<i class="ace-icon fa fa-search"></i>
										</span>
									</div>
								</td>
								<td style="vertical-align:top;padding-left:2px">
                                    <a class="btn btn-light btn-xs" onclick="tosearch();"  title="检索">
                                        <i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i>
                                    </a>
                                    <a class="btn btn-light btn-xs" onclick="clearSearch();"  title="清空">
                                        <i id="nav-search-icon1" class="ace-icon fa fa-refresh bigger-110 green"></i>
                                    </a>
                                </td>
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">	
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><span class="lbl"></span></label>
									</th>
                                    <th class="center">${label}</th>
								</tr>
							</thead>

							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty varList}">
									<c:forEach items="${varList}" var="var" varStatus="vs">
										<tr id="${var._KEY_}">
                                            <td class='center'>
                                                <label class="pos-rel"><input type='radio' name='itemids'
                                                                              data-key="${var._KEY_}"
                                                                              data-value="${var._VALUE_}"
                                                                              value="${var._KEY_}"
                                                                              onclick="tdKeyClick(this)"
                                                                              class="ace" />
                                                    <span class="lbl"></span></label>
                                            </td>
                                            <td class='center' data-key="${var._KEY_}" data-value="${var._VALUE_}"
                                                onclick="tdValueClick(this)">${var._VALUE_}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="100" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						<div class="page-header position-relative">
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;">
									<c:if test="${QX.del == 1 }">
									    <a class="btn btn-sm btn-danger" onclick="makeAll('sdf?');" title="保存" >批量删除</a>
									</c:if>
								</td>
								<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${pageStr}</div></td>
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
		if (typeof top.hangge === "function") {$(top.hangge());}//关闭加载状态
		//检索
		function tosearch(){
            var keywords = $("#keywords").val().trim();
            if (keywords == '') {
                $("#keywords").focus();
                return;
            }
            doSearch(10, 1);
		}
        // Clear search
        function clearSearch() {
            $("#keywords").val('');
            // 考虑那种方式, form还是ajax
            // form 需要将where传回
            doSearch(10, 1);
        }
        function doSearch(showCount, currentPage) {
            if (typeof top.jzts === "function") {top.jzts();}
            var keywords = $("#keywords").val().trim();

            var param = {
                'tablesId':'${pd.tablesId}',
                'corpId':'${pd.corpId}',
                'joinColumnsId':'${pd.joinColumnsId}',
                'selectColumnsId':'${pd.selectColumnsId}',
                'label':'${label}',
                'DICT_KEY':$("#DICT_KEY").val(),
                'DICT_VALUE':$("#DICT_VALUE").val(),
                'showCount':showCount,
                'currentPage':currentPage,
                'keywords':keywords
            };
            $("#param").val(encodeURIComponent(JSON.stringify(param)));
            $("#Form").submit();
        }
		$(function() {
            var defaultKey = $("#DICT_KEY").val();
            if (defaultKey != null && defaultKey != '' && defaultKey != 'undefined') {
                $("input[name='itemids'][value='" + defaultKey + "']").prop("checked", "checked");
            }
            $("#simple-table tr td").dblclick(function() {
                if ($(this).index() == 0) {
                    return;
                }
                var key = $(this).attr('data-key');
                var value = $(this).attr('data-value');
                setSelectValues(key, value);
                $("#DBL_CLICK").val('true');
                // 调用父窗口回调
                top.Dialog.close();
            });
		});
        function tdKeyClick(tdobject) {
            var td = $(tdobject);
            var key = td.attr('data-key');
            var value = td.attr('data-value');
            setSelectValues(key, value);
        }
        function tdValueClick(tdobject) {
            var td = $(tdobject);
            var key = td.attr('data-key');
            var value = td.attr('data-value');
            $("input[name='itemids'][value='" + key + "']").prop("checked", "checked");
            setSelectValues(key, value);
        }
        function setSelectValues(key, value) {
            $("#DICT_KEY").val(key);
            $("#DICT_VALUE").val(value);
        }
        function nextPage(page) {
            if (typeof top.jzts === "function") {top.jzts();}
            if (true && document.forms[0]) {
//                var url = document.forms[0].getAttribute("action");
//                if (url.indexOf('?') > -1) {
//                    url += "&page.currentPage=";
//                }
//                else {
//                    url += "?page.currentPage=";
//                }
//                url = url + page + "&page.showCount=10";
//                document.forms[0].action = url;
//                document.forms[0].submit();
                doSearch(10, page);
            } else {
                var url = document.location + '';
                if (url.indexOf('?') > -1) {
                    if (url.indexOf('currentPage') > -1) {
                        var reg = /currentPage=\d*/g;
                        url = url.replace(reg, 'currentPage=');
                    } else {
                        url += "&page.currentPage=";
                    }
                } else {
                    url += "?page.currentPage=";
                }
                url = url + page + "&page.showCount=10";
                document.location = url;
            }
        }
        function changeCount(value) {
            if (typeof top.jzts === "function") {top.jzts();}
            if (true && document.forms[0]) {
//                var url = document.forms[0].getAttribute("action");
//                if (url.indexOf('?') > -1) {
//                    url += "&page.currentPage=";
//                }
//                else {
//                    url += "?page.currentPage=";
//                }
//                url = url + "1&page.showCount=" + value;
//                document.forms[0].action = url;
//                document.forms[0].submit();
                doSearch(value, 1);
            } else {
                var url = document.location + '';
                if (url.indexOf('?') > -1) {
                    if (url.indexOf('currentPage') > -1) {
                        var reg = /currentPage=\d*/g;
                        url = url.replace(reg, 'currentPage=');
                    } else {
                        url += "1&page.currentPage=";
                    }
                } else {
                    url += "?page.currentPage=";
                }
                url = url + "&page.showCount=" + value;
                document.location = url;
            }
        }
        function toTZ() {
            var toPaggeVlue = document.getElementById("toGoPage").value;
            if (toPaggeVlue == '') {
                document.getElementById("toGoPage").value = 1;
                return;
            }
            if (isNaN(Number(toPaggeVlue))) {
                document.getElementById("toGoPage").value = 1;
                return;
            }
            nextPage(toPaggeVlue);
        }	</script>
</body>
</html>