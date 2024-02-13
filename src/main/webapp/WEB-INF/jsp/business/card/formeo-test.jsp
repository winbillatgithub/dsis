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

  <meta charset='utf-8'>
<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
  <link rel="stylesheet" href="/static/formeo-test/css/demo.css">
  <link rel="stylesheet" href="/static/formeo-test/css/form-builder.min.css">
  <link rel="stylesheet" href="/static/formeo-test/css/form-render.min.css">
</head>
<body>
  <div id="fb-root"></div>
  <div class="site-wrap container">
      <input type="hidden" name="ajaxUrl" id="ajaxUrl" />
    <div id="news-wrap"></div>
    <!-- MAIN CONTENT -->
    <div id="main-content-wrap" class="outer">
      <section id="main_content" class="inner">
    <div id="stage1" class="build-wrap"></div>
    <form class="render-wrap"></form>
    <button id="edit-form">Edit Form</button>
    <div class="action-buttons">
      <h2>Actions</h2>
      <button id="showData" type="button">Show Data</button>
      <button id="clearFields" type="button">Clear All Fields</button>
      <button id="getData" type="button">Get Data</button>
      <button id="getXML" type="button">Get XML Data</button>
      <button id="getJSON" type="button">Get JSON Data</button>
      <button id="getJS" type="button">Get JS Data</button>
      <button id="setData" type="button">Set Data</button>
      <button id="addField" type="button">Add Field</button>
      <button id="removeField" type="button">Remove Field</button>
      <button id="testSubmit" type="submit">Test Submit</button>
      <button id="resetDemo" type="button">Reset Demo</button>
<!--       <h2>i18n</h2> -->
<!--       <select id="setLanguage"> -->
<!--         <option value="ar-TN" dir="rtl">تونسي</option> -->
<!--         <option value="de-DE">Deutsch</option> -->
<!--         <option value="en-US">English</option> -->
<!--         <option value="es-ES">español</option> -->
<!--         <option value="fa-IR" dir="rtl">فارسى</option> -->
<!--         <option value="fr-FR">français</option> -->
<!--         <option value="nb-NO">norsk</option> -->
<!--         <option value="nl-NL">Nederlands</option> -->
<!--         <option value="pt-BR">português</option> -->
<!--         <option value="ro-RO">român</option> -->
<!--         <option value="ru-RU">Русский язык</option> -->
<!--         <option value="tr-TR">Türkçe</option> -->
<!--         <option value="vi-VN">tiếng việt</option> -->
<!--         <option value="zh-TW">台語</option> -->
<!--       </select> -->
    </div>
      </section>
    </div>
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
	 <script src="/static/js/common/common.js"></script>
	<script type="text/javascript">
		if (typeof top.hangge === "function") {$(top.hangge());}//关闭加载状态
		var url = '/card/getListService.do?tm='+new Date().getTime();
        var urlInput = document.getElementById("ajaxUrl");
        $('#ajaxUrl').val(url);
//		callAjax(url, false, null, function(dt) {
//			if (dt.success == true) {
//				// alert(eval(dt.data));
//			}
//		});
	</script>
<!--
  <script src="/static/formeo-test/js/vendor.min.js"></script>
  <script src="/static/formeo-test/js/site.min.js"></script>
 -->
  <script src="/static/formeo-test/js/vendor.js"></script>
  <script src="/static/formeo-test/js/form-builder.min.js"></script>
  <script src="/static/formeo-test/js/form-render.min.js"></script>
  <script src="/static/formeo-test/js/jquery.rateyo.min.js"></script>
  <script src="/static/formeo-test/js/demo.js"></script>
</body>

</html>
