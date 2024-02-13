<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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

					<form action="/dict/${msg}.do" name="Form" id="Form" method="post">
						<input type="hidden" name="DICT_ID" id="DICT_ID" value="${pd.DICT_ID}"/>
						<input type="hidden" name="PARENT_ID" id="PARENT_ID" value="${null == pd.PARENT_ID ? DICT_ID:pd.PARENT_ID}"/>
						<input type="hidden" name="NUMBER" id="NUMBER" value="${NUMBER}"/>
                        <input type="hidden" name="STATUS" id="STATUS" value="${pd.STATUS}" />
						<div id="zhongxin">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">上级:</td>
								<td>
									<div class="col-xs-4 label label-lg label-light arrowed-in arrowed-right">
										<b>${null == pds.NAME ?'(无) 此项为顶级':pds.NAME}</b>
									</div>
								</td>
							</tr>

						    <c:if test="${NUMBER == 0}">
							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">分类名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="50" placeholder="这里输入分类名称" title="分类名称" style="width:98%;"/></td>
							</tr>
							</c:if>
							<c:if test="${NUMBER == 1}">
							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">编码名称:</td>
								<td><input type="text" name="NAME" id="NAME" value="${pd.NAME}" maxlength="50" placeholder="这里输入编码名称" title="编码名称" style="width:98%;"/></td>
							</tr>
							</c:if>

							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">编码:</td>
								<td><input type="text" name="BIANMA" id="BIANMA" value="${pd.BIANMA}" maxlength="32" placeholder="这里输入编码 (不重复, 禁止修改)" title="编码" style="width:76%;" <c:if test="${null != pd.BIANMA}">readonly="readonly"</c:if> <c:if test="${null == pd.BIANMA}">onblur="hasBianma(true);"</c:if> /></td>
							</tr>

							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">排序:</td>
								<td><input type="number" name="ORDER_BY" id="ORDER_BY" value="${pd.ORDER_BY}" maxlength="32" placeholder="这里输入排序" title="排序"/></td>
							</tr>
                            <tr>
                                <td style="width:70px;text-align: right;padding-top: 13px;">状态:</td>
                                <td>
                                    <label style="float:left;padding-left: 12px;"><input class="ace" name="form-field-radio" id="form-field-radio1" onclick="setType('0');" <c:if test="${pd.STATUS == '0' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">启用</span></label>
                                    <label style="float:left;padding-left: 5px;"><input class="ace" name="form-field-radio" id="form-field-radio2" onclick="setType('1');" <c:if test="${pd.STATUS == '1' }">checked="checked"</c:if> type="radio" value="icon-edit"><span class="lbl">禁用</span></label>
                                </td>
                            </tr>

							<tr>
								<td style="width:70px;text-align: right;padding-top: 13px;">备注:</td>
								<td>
									<textarea rows="3" cols="46" name="BZ" id="BZ" placeholder="这里输入备注" title="备注"  style="width:98%;">${pd.BZ}</textarea>
								</td>
							</tr>
							<tr>
								<td class="center" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save('${NUMBER}');">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="/static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
						
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
	<%@ include file="../../system/index/foot.jsp"%>
	<!--提示框-->
	<script type="text/javascript" src="/static/js/jquery.tips.js"></script>
    <script type="text/javascript" src="/static/js/dict/pinyin_dict_notone.js"></script>
    <script type="text/javascript" src="/static/js/dict/pinyinUtil.js"></script>

    <script type="text/javascript">
		if (typeof top.hangge === "function") {$(top.hangge());}
        $(document).ready(function(){
            $("#NAME").on('input',function(e) {
                var value = $("#NAME").val().trim();
                var pinyin = pinyinUtil.getPinyin(value, '', false, false).substr(0, 100);
                if ($("#BIANMA").attr("readonly") != 'readonly') {
                    $("#BIANMA").val(pinyin);
                }
            });
        });
		//保存
		function save(NUMBER){
			if($("#NAME").val()==""){
				$("#NAME").tips({
					side:3,
		            msg:'请输入编码名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#NAME").focus();
                return false;
            }
            if($("#BIANMA").val()==""){
				$("#BIANMA").tips({
					side:3,
		            msg:'请输入编码ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BIANMA").focus();
			    return false;
            } else if (hasBianma(false) == true) {
                return false;
            }
            if($("#ORDER_BY").val()==""){
                $("#ORDER_BY").tips({
                    side:3,
                    msg:'请输入数字',
                    bg:'#AE81FF',
                    time:2
                });
                $("#ORDER_BY").focus();
                return false;
            }
			
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
			
		}
		
		//判断编码是否存在
		function hasBianma(async){
            if ($("#BIANMA").attr("readonly") == 'readonly') {
                return false;
            }
			var BIANMA = $.trim($("#BIANMA").val());
			if("" == BIANMA)return;
            var DICT_ID = $("#PARENT_ID").val();
            var bResult = false;
			$.ajax({
				type: "POST",
				url: '/dict/hasBianma.do',
		    	data: {BIANMA:BIANMA,DICT_ID:DICT_ID,tm:new Date().getTime()},
				dataType:'json',
                async:async,
				cache: false,
				success: function(data){
                    if("success" == data.result){
                    }else{
                        $("#BIANMA").tips({
                            side:1,
                            msg:'编码'+BIANMA+'已存在,重新输入',
                            bg:'#AE81FF',
                            time:5
                        });
                        $('#BIANMA').focus();
                        bResult = true;
                    }
				}
			});
            return bResult;
		}
        function setType(value){
            $("#STATUS").val(value);
        }
	</script>
</body>
</html>