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

    <!-- bootstrap 提示框 -->
    <script src="/static/ace/js/bootstrap.js"></script>
    <script src="/static/ace/js/bootbox.js"></script>
    <!-- 全局提示 -->
    <script type="text/javascript" src="/static/js/myjs/head.js"></script>
    <!--引入弹窗组件start-->
    <script type="text/javascript" src="/plugins/attention/zDialog/zDrag.js"></script>
    <script type="text/javascript" src="/plugins/attention/zDialog/zDialog.js"></script>
    <!--引入弹窗组件end-->
    <!--提示框-->
    <script type="text/javascript" src="/static/js/jquery.tips.js"></script>

</head>
<body class="skin-aliyun" style="">
<!-- 页面顶部¨ -->
<div id="body">
    <div class="">
        <div id="cwts" style="display:none; width:100%; position:fixed; z-index:99999999;">
            <div style="padding-left: 70%;padding-top: 1px;">
                <div style="float: left;margin-top: 3px;"><img src="/static/images/error.gif" /> </div>
                <div style="margin-top: 6px;"><h4 id="errtip" class="lighter block red">&nbsp;错误了</h4></div>
            </div>
        </div>
        <div id="jzts" style="display:block; width:100%; position:fixed; z-index:99999999;">
            <div class="commitopacity" id="bkbgjz"></div>
            <div id="bkbgwz" style="padding-left: 48%;padding-top: 1px;">
                <div style="float: left;margin-top: 3px;"><img src="/static/images/loadingi.gif" /> </div>
                <div style="margin-top: 6px;"><h4 class="lighter block red">&nbsp;加载中 ...</h4></div>
            </div>
        </div>
    </div>

    <div data-reactroot="" class="ant-gauss-app">
        <%--<div class="ant-gauss-app-header"></div>--%>
        <div class="ant-gauss-app-body">
            <div class="navi"></div>
            <div class="main">
                <div class="ant-gauss-page-robots">
                    <div class="ali-gauss-view-robotlist create-status">
                        <%--<div class="robotlist-title">我的表单</div>--%>
                        <%--<div class="robotlist-body"></div>--%>
                        <div class="ant-gauss-view-robotcreate">
                            <input type="hidden" id="currentStep" name="currentStep" value="${currentStep}"/>
                            <input type="hidden" id="formEditFlag" name="formEditFlag" value="${formEditFlag}"/>
                            <input type="hidden" id="TABLES_ID" name="TABLES_ID" value="${TABLES_ID}"/>
                            <input type="hidden" id="formType" name="formType" value="${formType}"/>
                            <div class="robotcreate-body">
                                <div class="robotcreate-steps">
                                    <div class="ant-steps ant-steps-horizontal ant-steps-label-horizontal">
                                        <div id="divStep1" class="ant-steps-item ant-steps-status-process" style="margin-right: -31px; width: 20%;">
                                            <div class="ant-steps-tail" style="padding-right: 31px;"></div>
                                            <div class="ant-steps-step">
                                                <div class="ant-steps-head">
                                                    <div class="ant-steps-head-inner"><span class="ant-steps-icon">1</span></div>
                                                </div>
                                                <div class="ant-steps-main">
                                                    <div class="ant-steps-title">配置表单元素</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="divStep2" class="ant-steps-item ant-steps-status-wait" style="margin-right: -31px; width: 20%;">
                                            <div class="ant-steps-tail" style="padding-right: 31px;"></div>
                                            <div class="ant-steps-step">
                                                <div class="ant-steps-head">
                                                    <div class="ant-steps-head-inner"><span class="ant-steps-icon">2</span></div>
                                                </div>
                                                <div class="ant-steps-main">
                                                    <div class="ant-steps-title">配置表单</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="divStep3" class="ant-steps-item ant-steps-status-wait" style="margin-right: -31px; width: 20%;">
                                            <div class="ant-steps-tail" style="padding-right: 31px;"></div>
                                            <div class="ant-steps-step">
                                                <div class="ant-steps-head">
                                                    <div class="ant-steps-head-inner"><span class="ant-steps-icon">3</span></div>
                                                </div>
                                                <div class="ant-steps-main">
                                                    <div class="ant-steps-title">预览</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="divStep4" class="ant-steps-item ant-steps-status-wait" style="margin-right: -31px; width: 20%;">
                                            <div class="ant-steps-tail" style="padding-right: 31px;"></div>
                                            <div class="ant-steps-step">
                                                <div class="ant-steps-head">
                                                    <div class="ant-steps-head-inner"><span class="ant-steps-icon">4</span></div>
                                                </div>
                                                <div class="ant-steps-main">
                                                    <div class="ant-steps-title">菜单配置</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="divStep5" class="ant-steps-item ant-steps-status-wait" style="margin-right: -31px; width: 20%;">
                                            <div class="ant-steps-tail" style="padding-right: 31px;"></div>
                                            <div class="ant-steps-step">
                                                <div class="ant-steps-head">
                                                    <div class="ant-steps-head-inner"><span class="ant-steps-icon">5</span></div>
                                                </div>
                                                <div class="ant-steps-main">
                                                    <div class="ant-steps-title">权限配置</div>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="divStep6" class="ant-steps-item ant-steps-status-wait">
                                            <div class="ant-steps-tail" style="padding-right: 31px;"></div>
                                            <div class="ant-steps-step">
                                                <div class="ant-steps-head">
                                                    <div class="ant-steps-head-inner"><span class="ant-steps-icon">6</span></div>
                                                </div>
                                                <div class="ant-steps-main">
                                                    <div class="ant-steps-title">用户配置</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="robotcreate-content">
                                    <div class="ant-gauss-component-robotform form-create">
                                        <iframe name="contentFrame" id="contentFrame" frameborder="0" src="/indexes/list.do?view_type=dragdrop" style="margin:0 auto;width:100%;height:100%;"></iframe>
                                        <div class="robot-actions">
                                            <button id="btnPrev" type="button" onclick="goPrev()" style="display: none;"
                                                    class="ant-btn ant-btn-primary ant-btn-lg">
                                                <span>上一步</span>
                                            </button>
                                            <button id="btnNext" type="button" onclick="goNext()"
                                                    class="ant-btn ant-btn-primary ant-btn-lg">
                                                <span>下一步</span>
                                            </button>
                                            <button id="btnReturn" type="button" onclick="goReturn()"
                                                    class="ant-btn ant-btn-ghost ant-btn-lg">
                                                <span>返回</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
    <script type="text/javascript">
        function setShadowSize() {
            var hmain = document.getElementById("contentFrame");
            var bheight = document.documentElement.clientHeight;
            var bkbgjz = document.getElementById("bkbgjz");
            bkbgjz.style.height = bheight + 'px';
            var bkbgwz = document.getElementById("bkbgwz");
            bkbgwz.style.paddingTop = (bheight / 2 - 10) + 'px';
        }
        setShadowSize();
        window.onresize = function(){
            setShadowSize();
        };
        function goNext() {
            var TABLES_ID = $("#TABLES_ID").val();
            var currentStep = $("#currentStep").val();
            $(top.jzts());
            if (currentStep == "0") {
                // go to card/list edit mode
                $("#divStep1").removeClass("ant-steps-status-process");
                $("#divStep1").addClass("ant-steps-status-wait");
                $("#divStep2").removeClass("ant-steps-status-wait");
                $("#divStep2").addClass("ant-steps-status-process");
                var url = '';
                if (TABLES_ID == 'undefined' || TABLES_ID == '' || TABLES_ID == null) {
                    url = "/card/card.do?formEditFlag=add";
                    <%--url = "/card/card.do?formEditFlag=edit&TABLES_ID=xxx_3eeb0f9a40d74b59bd1edcbc4400d451";--%>
                } else {
                    url = "/card/card.do?formEditFlag=edit&TABLES_ID=" + TABLES_ID;
                }
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("1");
                $("#btnPrev").show();
                $("#btnNext").show();
            } else if (currentStep == "1") {
                // go to preview mode
                // whether form was saved, means TABLES_ID is not null
                if (TABLES_ID == 'undefined' || TABLES_ID == '' || TABLES_ID == null) {
                    // 新增模式
                    TABLES_ID = $("#contentFrame").contents().find("#TABLES_ID").val();
                    if (TABLES_ID == 'undefined' || TABLES_ID == '' || TABLES_ID == null) {
                        if (typeof top.hangge === "function") {$(top.hangge());}
                        bootbox.alert("请先保存表单.");
                        return;
                    }
                    $("#TABLES_ID").val(TABLES_ID);
                }
                $("#divStep2").removeClass("ant-steps-status-process");
                $("#divStep2").addClass("ant-steps-status-wait");
                $("#divStep3").removeClass("ant-steps-status-wait");
                $("#divStep3").addClass("ant-steps-status-process");
                var url = "/nav/form_type.do?formType=card&TABLES_ID=" + TABLES_ID;
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("2");
                $("#btnPrev").show();
                $("#btnNext").show();
            } else if (currentStep == "2") {
                // go to menu setting
                $("#divStep3").removeClass("ant-steps-status-process");
                $("#divStep3").addClass("ant-steps-status-wait");
                $("#divStep4").removeClass("ant-steps-status-wait");
                $("#divStep4").addClass("ant-steps-status-process");
                var url = "/menu/listAllMenu.do";
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("3");
                // set action buttons
                $("#btnPrev").show();
                $("#btnNext").show();
            } else if (currentStep == "3") {
                // go to role menu
                $("#divStep4").removeClass("ant-steps-status-process");
                $("#divStep4").addClass("ant-steps-status-wait");
                $("#divStep5").removeClass("ant-steps-status-wait");
                $("#divStep5").addClass("ant-steps-status-process");
                var url = "/role.do";
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("4");
                // set action buttons
                $("#btnPrev").show();
                $("#btnNext").show();
            } else if (currentStep == "4") {
                // go to user menu
                $("#divStep5").removeClass("ant-steps-status-process");
                $("#divStep5").addClass("ant-steps-status-wait");
                $("#divStep6").removeClass("ant-steps-status-wait");
                $("#divStep6").addClass("ant-steps-status-process");
                var url = "/user/listUsers.do";
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("5");
                // set action buttons
                $("#btnPrev").show();
                $("#btnNext").hide();
            }
        }
        function goPrev() {
            $(top.jzts());
            var TABLES_ID = $("#TABLES_ID").val();
            var currentStep = $("#currentStep").val();
            if (currentStep == "1") {
                // go to card/list edit mode
                $("#divStep2").removeClass("ant-steps-status-process");
                $("#divStep2").addClass("ant-steps-status-wait");
                $("#divStep1").removeClass("ant-steps-status-wait");
                $("#divStep1").addClass("ant-steps-status-process");
                var url = "/indexes/list.do?view_type=dragdrop";
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("0");
                $("#btnPrev").hide();
                $("#btnNext").show();
            } else if (currentStep == "2") {
                // go to card/list edit mode
                $("#divStep3").removeClass("ant-steps-status-process");
                $("#divStep3").addClass("ant-steps-status-wait");
                $("#divStep2").removeClass("ant-steps-status-wait");
                $("#divStep2").addClass("ant-steps-status-process");
                var url = '';
                if (TABLES_ID == 'undefined' || TABLES_ID == '' || TABLES_ID == null) {
                    url = "/card/card.do?formEditFlag=add";
                    <%--url = "/card/card.do?formEditFlag=edit&TABLES_ID=xxx_3eeb0f9a40d74b59bd1edcbc4400d451";--%>
                } else {
                    url = "/card/card.do?formEditFlag=edit&TABLES_ID=" + TABLES_ID;
                }
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("1");
                $("#btnPrev").show();
                $("#btnNext").show();
            } else if (currentStep == "3") {
                // go to preview mode
                // whether form was saved, means TABLES_ID is not null
                if (TABLES_ID == 'undefined' || TABLES_ID == '' || TABLES_ID == null) {
                    // 新增模式
                    TABLES_ID = $("#contentFrame").contents().find("#TABLES_ID").val();
                    if (TABLES_ID == 'undefined' || TABLES_ID == '' || TABLES_ID == null) {
                        bootbox.alert("请先保存表单.");
                        return;
                    }
                    $("#TABLES_ID").val(TABLES_ID);
                }
                $("#divStep4").removeClass("ant-steps-status-process");
                $("#divStep4").addClass("ant-steps-status-wait");
                $("#divStep3").removeClass("ant-steps-status-wait");
                $("#divStep3").addClass("ant-steps-status-process");
                var url = "/nav/form_type.do?formType=card&TABLES_ID=" + TABLES_ID;
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("2");
                $("#btnPrev").show();
                $("#btnNext").show();

            } else if (currentStep == "4") {
                // go to role menu
                $("#divStep5").removeClass("ant-steps-status-process");
                $("#divStep5").addClass("ant-steps-status-wait");
                $("#divStep4").removeClass("ant-steps-status-wait");
                $("#divStep4").addClass("ant-steps-status-process");
                var url = "/menu/listAllMenu.do";
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("3");
                // set action buttons
                $("#btnPrev").show();
                $("#btnNext").show();
            } else if (currentStep == "5") {
                // go to user menu
                $("#divStep6").removeClass("ant-steps-status-process");
                $("#divStep6").addClass("ant-steps-status-wait");
                $("#divStep5").removeClass("ant-steps-status-wait");
                $("#divStep5").addClass("ant-steps-status-process");
                var url = "/role.do";
                $("#contentFrame").attr("src", url);
                $("#currentStep").val("4");
                // set action buttons
                $("#btnPrev").show();
                $("#btnNext").hide();
            }
        }
        function goReturn() {
            window.location.href='/nav/my_forms.do?domain=' + '${domain}';
        }
    </script>
</body>
</html>
