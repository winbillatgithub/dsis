<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../common/taglib.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
    

    <!-- jsp文件头和头部 -->
    <%@ include file="../system/index/top.jsp"%>
    <style type="text/css">
        .commitopacity{position:absolute; width:100%; height:100px; background:#7f7f7f; filter:alpha(opacity=50); -moz-opacity:0.8; -khtml-opacity: 0.5; opacity: 0.5; top:0px; z-index:99999;}
    </style>
    <!-- 下拉框 -->
    <link rel="stylesheet" href="/static/ace/css/chosen.css" />
    <!-- jsp文件头和头部 -->
    <%@ include file="../system/index/top.jsp"%>
    <!-- 日期框 -->
    <link rel="stylesheet" href="/static/nav/navigator.css" />

</head>
<body class="skin-aliyun" style="">
<!-- 页面顶部¨ -->
<div id="body">

    <div class="ant-gauss-app">
        <div class="ant-gauss-app-header">
            <div class="robot-switch-wrap"></div>
            <div class="navi-actions"></div>
            <div class="product"></div>
            <div class="member-info">
                <c:if test="${domain != null && domain != ''}">
                    <a href="${domain}" style="color: rgb(255, 255, 255);">返回登录页面</a>
                </c:if>
                <c:if test="${domain == null || domain == ''}">
                    <a href="/main/index" style="color: rgb(255, 255, 255);">返回</a>
                </c:if>
            </div>
        </div>
        <div class="ant-gauss-app-body">
            <div class="navi"></div>
            <div class="main">
                <div class="ant-gauss-page-robots">
                    <div class="ant-gauss-view-robotlist">
                        <div class="robotlist-title">我的表单</div>
                        <div class="robotlist-body">
                            <div class="ant-gauss-component-instance instance-create">
                                <div class="instance-create" onclick="goCreate('${formItem.TABLES_ID}');">
                                    <span class="">＋创建表单</span>
                                </div>
                            </div>

                            <c:forEach items ="${mapForm.rows}" var = "formItem" varStatus="status">
                            <div class="ant-gauss-component-instance instance-edit">
                                <div class="instance-wrap">

                                    <div class="item-header">
                                        <c:if test="${'0' == formItem.STATUS}">
                                        <div class="link" onclick="goLink('${formItem.TABLES_ID}', '${formItem.CORP_ID}');">
                                            <span class="iconfont icon-link">
                                            </span>
                                        </div>
                                        </c:if>
                                        <c:if test="${'1' == formItem.STATUS}">
                                            <div class="link" onclick="goUnlink('${formItem.TABLES_ID}', '${formItem.CORP_ID}');">
                                            <span class="iconfont icon-Personnel_files">
                                            </span>
                                            </div>
                                        </c:if>
                                        <div class="edit" onclick="goEdit('${formItem.TABLES_ID}');">
                                            <span class="iconfont icon-edit">
                                            </span>
                                        </div>
                                        <div class="delete" onclick="goDelete('${formItem.TABLES_ID}');">
                                            <span class="iconfont icon-delete">
                                            </span>
                                        </div>
                                        <%--/card/card.do?formEditFlag=preview&dataEditFlag=add&TABLES_ID=' + TABLES_ID--%>
                                        <div class="avatar"  style="background-size: cover; background-image: url('/static/nav/form_logo.png');"></div>
                                    </div>
                                    <div class="body">
                                        <div class="title">${formItem.TABLE_NAME}</div>
                                        <div class="info"></div>
                                    </div>
                                </div>
                            </div>
                            </c:forEach>
                            <div class="ant-gauss-view-robotcreate"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

    <script type="text/javascript">
        if (typeof top.hangge === "function") {$(top.hangge());}
        var domain = '${domain}';
        // 导航页-创建
        function goCreate(TABLES_ID) {
            if (typeof top.jzts === "function") {top.jzts();}
            window.location.href='/nav/create_form_steps.do?TABLES_ID=' + TABLES_ID + '&domain=' + domain;
        }
        //去此ID下子级列表
        function goEdit(TABLES_ID) {
            if (typeof top.jzts === "function") {top.jzts();}
            window.location.href='/nav/create_form_steps.do?TABLES_ID=' + TABLES_ID + '&domain=' + domain;
        }

        function goLink(TABLES_ID, CORP_ID) {
            if (typeof top.jzts === "function") {top.jzts();}
            bootbox.confirm("设置表单公开访问, 可以用于收集用户表单信息.<br/>" +
                    "但是该表单数据可能会被访问, 修改和删除.<br/><br/>" +
                    "确定设置该表单为公开访问?", function(result) {
                if(result) {
                    var diag = new top.Dialog();
                    diag.Drag = true;
                    diag.Title ="设置公开访问";
                    diag.URL = '/nav/code.do?TABLES_ID=' + TABLES_ID + '&CORP_ID=' + CORP_ID + '&undo=0&domain=' + domain;
                    diag.Width = 800;
                    diag.Height = 400;
                    diag.CancelEvent = function(){
                        diag.close();
                        parent.location.reload();
                    };
                    diag.show();
                }
            });
        }
        function goUnlink(TABLES_ID, CORP_ID) {
            if (typeof top.jzts === "function") {top.jzts();}

            var diag = new top.Dialog();
            diag.Drag = true;
            diag.Title ="取消公开访问";
            diag.URL = '/nav/code.do?TABLES_ID=' + TABLES_ID + '&CORP_ID=' + CORP_ID + '&undo=1&domain=' + domain;
            diag.Width = 800;
            diag.Height = 500;
            diag.CancelEvent = function(){
                diag.close();
            };
            diag.OKEvent = function() {
                var url = '/nav/make_private.do?tm='+new Date().getTime();
                var param = {'TABLES_ID': TABLES_ID};
                callAjax(url, true, param, function(dt) {
                    if (dt.success == true) {
                        diag.close();
                        self.location.reload();
                    }
                });
            };
            diag.show();
        }

        function goDelete(TABLES_ID) {
            if (typeof top.jzts === "function") {top.jzts();}
            bootbox.confirm("该表单数据将被全部删除!<br/>数据不可恢复!<br/><br/>确定要删除吗?", function(result) {
                if(result) {
                    var url = "/tables/delete.do?TABLES_ID="+TABLES_ID+"&tm="+new Date().getTime();
                    $.get(url, function(data){
                        // 刷新自己
                        self.location.reload();
                    });
                }
            });
        }
    </script>
<script type="text/javascript" src="/plugins/attention/zDialog/zDrag.js"></script>
<script type="text/javascript" src="/plugins/attention/zDialog/zDialog.js"></script>
<!-- bootbox -->
<script src="/static/ace/js/bootstrap.js"></script>
<script src="/static/ace/js/bootbox.js"></script>
</body>
</html>
