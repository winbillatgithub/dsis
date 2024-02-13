<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>

<!-- 下拉框 -->
<link rel="stylesheet" href="/static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../system/index/top.jsp"%>
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
					<span style="float: left; margin: 30px;"> <h4>卡片:</h4>
						<img width="260"  height="260" id="publicCard"/>
					</span>
					<span style="float: right; margin: 30px;">	<h4>列表：</h4>
						<img width="260" height="260" id="publicList"/>
					</span>
					</div>
                    <div class="row">
                        <c:if test="${pd.undo == '1'}">
                            <span style="float: left; margin: 30px;">
                                取消表单公开访问, 用户将没有访问,录入该表单数据的权限.<br/><br/>
                                取消该表单的公开访问?
                            </span>
                        </c:if>
                    </div>
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
	<%@ include file="../system/index/foot.jsp"%>
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
        $(function() {
           $('#publicCard').attr('src',"data:image/jpg;base64,"+decodeURI('${publicCard}'));
           $('#publicList').attr('src',"data:image/jpg;base64,"+decodeURI('${publicList}'));
        });
		</script>
</body>
</html>