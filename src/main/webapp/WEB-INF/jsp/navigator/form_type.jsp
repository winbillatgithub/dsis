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
    <!-- TAB -->
    <link rel="stylesheet" href="/plugins/tab/css/import_basic.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" id="skin" prePath="/plugins/tab/" />
    <script type="text/javascript" src="/static/js/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="/plugins/tab/js/framework.js"></script>
    <script type="text/javascript" src="/plugins/tab/js/tab.js" charset="utf-8"></script>

</head>
<body>
    <!-- 页面顶部¨ -->
    <input type="hidden" id="formType" name="formType" value="${formType}"/>
    <input type="hidden" id="TABLES_ID" name="TABLES_ID" value="${TABLES_ID}"/>

    <div id="tab_menu" style="height:30px;"></div>
    <div style="width:100%;">
        <div id="page" style="width:100%;height:100%;"></div>
    </div>

    <script type="text/javascript">
        if (typeof top.hangge === "function") {$(top.hangge());}
        var tab;
        // html页面加载完成后, form渲染完毕后加载数据
        $(function() {
            tab = new TabView( {
                containerId :'tab_menu',
                pageid :'page',
                cid :'tab1',
                position :"top"
            });
            $(".tab_item2").css("width", "1200px");

            var cardUrl = "/card/card.do?formEditFlag=preview&TABLES_ID=${TABLES_ID}";
            tab.add( {
                id :'tab1_index1',
                title : "卡片: " + cardUrl,
//                title :"卡片",
                url : cardUrl,
                fixedWidth : '0',
                isClosed : false
            });

            var listUrl = "/list/list.do?TABLES_ID=${TABLES_ID}";
            tab.add( {
                id :'tab1_index2',
              title :"列表" + listUrl,
//                title :"列表",
                url :listUrl,
                fixedWidth : '0',
                isClosed :false
            });

            if (typeof top.jzts === "function") {
                $(top.jzts());
            }
        });

        function resetFrameSize(){
            var hmainT = document.getElementById("page");
            var bheightT = document.documentElement.clientHeight;
            hmainT.style.width = '100%';
            hmainT.style.height = (bheightT  - 41) + 'px';
        }
        resetFrameSize();
        window.onresize=function(){
            resetFrameSize();
        };
        function loadPreview() {
            if (typeof top.jzts === "function") {
                $(top.jzts());
            }
            var formType = $("#formType").val(); // card/list
            if (formType == 'card') {
                var url = "/card/card.do?formEditFlag=preview&TABLES_ID=${TABLES_ID}";
                $("#previewFrame").attr("src", url);
            } else if (formType == 'list') {
                var url = "/list/list.do?TABLES_ID=${TABLES_ID}";
                $("#previewFrame").attr("src", url);
            }
        }
    </script>
</body>
</html>
