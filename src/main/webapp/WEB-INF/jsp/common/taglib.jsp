<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="language"
	value="${not empty param.language ? param.language : pageContext.request.locale}"
	scope="session" />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<fmt:setLocale value="${language}" />
<fmt:setBundle basename="internationalization" />

<script>var ctx = '${ctx}';</script>

<script src="/static/js/common/common.js"></script>
<script src="/static/slickgrid/lib/jquery-1.11.2.min.js"></script>
