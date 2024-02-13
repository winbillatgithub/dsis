<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>拖拽测试</title>
  <link rel="stylesheet" href="/static/resource/css/jquery-ui.css">
  <style>
  <style type="text/css">
	*{
		padding:0;
		margin:0;
		box-sizing: border-box;
		border:1px solid #000;
	}
	.left{
		min-height:450px;
		max-height:450px;
		width:200px;
		border:1px solid #000;
		float: left;
		margin-left:2px;
		padding:0 2px;
		position:relative;
		overflow: auto;
	}
	.cut {
		float:left;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space: nowrap; 
		width:40%;
	}
	
	.middle{
		min-height:450px;
		max-height:450px;
		width:520px;
		border:1px solid #000;
		float: left;
		margin-left:2px;
		padding:0 2px;
		position:relative;
		overflow: auto;
	}
	#right{
		min-height:400px;
		max-height:400px;
		width:150px;
		float: right;
		border:1px solid #000;
		margin-left:2px;
		padding:0 2px;
		position:relative;
		overflow: auto;
	}
	#rightBottom{
		min-height:50px;
		max-height:50px;
		width:150px;
		float: top;
		margin-left:5px;
		padding:0 2px;
		position:relative;
		overflow: auto;
	}
	.theGrey{
	    background:#ccc!important;
	}
	.liInput {
		float:left;
		width:50%;
		height:25px;
	}
	.propertyInput {
		float:left;
		width:100%;
		height:25px;
	}
	
  	.liClass {
  		float: left;
		background: #CCFFFF;
		height:30px;
		width:70px;
		border:1px solid #5F9EDF;
		margin: 2px 0;
		font: 10px/25px "微软雅黑";
		text-align: center;
		border-radius: 2px;
		cursor: pointer;
		list-style-type:none;
	}
	.liClass2 {
  		float: left;
		background: #CCFFFF;
		height:30px;
		width:160px;
		border:1px solid #5F9EDF;
		margin: 2px 0;
		font: 10px/25px "微软雅黑";
		text-align: center;
		border-radius: 2px;
		cursor: pointer;
		list-style-type:none;
	}
	#saveDiv{
    position:relative;
    top:15px;
    left:20px;     
	}  
  .component {width:80px;height:25px;}
  .pull-right {  float: right !important;  /*向右浮动*/ }
  </style>
   <link rel="stylesheet" href="/static/resource/css/bootstrap.min.css" />
	<script src="/static/resource/js/bootstrap.min.js"></script>
  <script src="/static/resource/js/jquery-2.1.4.min.js"></script>
 <script src="/static/resource/js/jquery-ui.min.js"></script>
 <script src="/static/resource/js/draggable.js" type="text/javascript"></script>
  <script>
  $(function() {
	  	
	  if (typeof top.hangge === "function") {$(top.hangge());}
  		//可拖拽组件初始化
		$('.cpList li').each(function(){
			$(this).attr("draggable","true");
			$(this).on("dragstart", function(ev) {//开始拖拽
				var dt = ev.originalEvent.dataTransfer;
				var sourceOb = $(ev.target).closest("li");
				dt.setData('sourceId', $(sourceOb).attr("id"));//将拖拽组件ID传入
				dt.setData('fieldText', $(sourceOb).text());//将拖拽组件文本传入
			});
		});
		//拖拽组件
		Draggable({destId:".tab",dragTag:"li"});
		
		/*  $("#middle").on("mouseenter", ".liClass", function () {
         	var ele=$('<span>X</span>');
         	ele.css({position:"relative",left:"35px",top:"-17px"});
             $(this).append(ele);
         }).on("mouseleave", ".liClass", function () {
             $("span").remove();
         }).on("click", "span",function () { */
         $("#middle").on("click",function (ev) {
        	 var selectedLi = $(ev.target).closest("li");
        	 if( ev.target.nodeName.toUpperCase() == 'A' ) {
        		 
                 if (confirm("确定删除吗?")) {
                	 selectedLi.remove();
                     //bootbox.alert("删除元素");
                     /* $("#left .liClass").each(function(){
                     	if($(this).attr("data-field")==field){
                     		$(this).removeClass("theGrey"); // 激活其拖动功能
                     		$(this).attr("draggable","true");
                     	}
                     });
                     $("#right .liClass").each(function(){
                      	if($(this).attr("data-field")==field){
                      		$(this).removeClass("theGrey"); // 激活其拖动功能
                      		$(this).attr("draggable","true");
                      	}
                     }); */
                 }
        	 } else if (ev.target.nodeName.toUpperCase() == 'UL') {
        		
        	 } else {
        		 $('.middle li').each(function(index,li){
        			 $(li).removeClass("theGrey");
        		 });
         		 $(selectedLi).addClass("theGrey"); // 
         		 showProperty($(selectedLi));
        	 }
        	 //var field=$(this).parent().attr("data-field");
             
         });
  });
  
 
  </script>
</head>
<body>
 <div class="container">
 	<div class="row" style="border:1px solid #000">
 	<div class="col-xs-3">
 	<div ><h3>控件面板</h3></div>
	<ul id="left1" class="left cpList">
	    <li id="left_wb"   itemType="left_wb" class="liClass">文本</li>
	    <li id="left_zs"   itemType="left_zs" class="liClass">整数</li>
	    <li id="left_xs"   itemType="left_xs" class="liClass">小数</li>
	    <li id="left_fx"   itemType="left_fx" class="liClass"><img src="/static/resource/img/check.JPG" style="width:100%;height:100%" /></li>
	    <li id="left_dx"   itemType="left_dx" class="liClass"><img src="/static/resource/img/radio.JPG" style="width:100%;height:100%" /></li>
	    <li id="left_rq"   itemType="left_rq" class="liClass">日期/时间</li>
	    <li id="left_tp"   itemType="left_tp" class="liClass">图片</li>
	    <li id="left_pdf"  itemType="left_pdf" class="liClass theGrey">PDF</li>
	    <li id="left_office"  itemType="left_office" class="liClass theGrey">office文件</li>
	    <li id="left_sp"   itemType="left_sp" class="liClass theGrey">视频</li>
	    <li id="left_qt"   itemType="left_qt" class="liClass theGrey">其他</li>
	</ul>
 	</div>
 	<div id="myZhibiao" class="col-xs-7">
 	<div><h3>指标面板</h3></div>
		<ul id="middle" class="tab middle">
			<!-- <li class="liClass" data-field="old_id"><label>初始值</label>
			<a href="javascript:void(0)" class="pull-right">X</a></li> -->
		</ul>
 </div>
 	<div class="col-xs-2" >
 	<div><h3>属性面板</h3></div>
 	<ul id="right">
	    <li class="liClass"><label id="title" >标题</label></li>
	    <li class="liClass"><input id="titleValue" class="propertyInput" type=text></input></li>
	    <li class="liClass"><label id="itemType" >类型</label></li>
	    <li class="liClass">
	    	<select id="itemTypeValue" class="propertyInput" disabled>
              <option value="left_wb">文本</option>
              <option value="left_zs">整数</option>
              <option value="left_xs">浮点数</option>
              <option value="left_fx">复选框</option>
              <option value="left_dx">单选框</option>
              <option value="left_rq">日期/时间</option>
              <option value="left_tp">图片</option>
              <option value="left_pdf">pdf</option>
              <option value="left_office">office文件</option>
              <option value="left_sp">视频</option>
              <option value="left_qt">其他</option>
            </select>
        </li>
        <li class="liClass"><label id="manyRow">是否多行</label></li>
        <li class="liClass"> 
              <input name="manyRow" type="radio" value="1" checked="checked" >是
              <input name="manyRow" type="radio" value="2" >否
        </li>
        <li class="liClass" style="display:none"><label id="optionSource">选项来源</label></li>
        <li class="liClass" style="display:none"> 
              <select id="catogeryType"  class="propertyInput">
		    	  <option value="" selected>无</option>
              </select>
        </li>
        <li class="liClass"><label id="len">长度</label></li>
	    <li class="liClass"><input id="lenValue" class="propertyInput" type=text value=''></input></li>
	    <li class="liClass"><label id="presision" >小数位</label></li>
	    <li class="liClass"><input id="presisionValue" class="propertyInput" type=text value=''></input></li>
	    <li class="liClass"><label id="autoWrite"  >自动填写</label></li>
	    <li class="liClass">
	    	 <c:forEach items="${dataTypeList}" var="item">                 
            	<input type="radio"  name="autoWrite" value="${item.DICTIONARIES_ID }" />
            			<label>${item.NAME }</label>            
       		 </c:forEach>  
	    	 <!-- <input name="autoWrite" type="radio" value="1" checked="checked" >是
             <input name="autoWrite" type="radio" value="2" >否 -->
	    </li>
	    <li class="liClass"><label id="init">初始值</label></li>
	    <li class="liClass"><input id="initValue"  class="propertyInput" type=text value=''></input></li>
	    <li class="liClass"><label id="rule">校验规则</label></li>
	    <li class="liClass">
	    	<select id="ruleValue"  class="propertyInput">
	    	  <option value="" selected>无</option>
             <c:forEach items="${ruleList}" var="item">
				<option value="${item.CELLRULE_ID }">${item.NAME }</option>
			</c:forEach>
            </select>
	    </li>
	    <li class="liClass"><label id="baomi">保密</label></li>
	    <li class="liClass">
	    	 <input name="baomi" type="radio" value="1" >是
              <input name="baomi" type="radio" value="2" checked="checked"  >否
	    </li>
	</ul>
	<ul id="rightBottom">
		 <li onclick="saveProperty();" class="liClass"><label>保存属性</label></li>
	</ul>
	</div>
 	</div>
 	<div class="row" >
			<button type="button" onclick="save()" >保存指标</button>
		</div>
	</div>
</div>
</body>
<script>
function saveProperty() {
	var title = $("#titleValue").val(); // 标题
	var itemTypeValue = $("#itemTypeValue").val();  // 类型
 	var manyRow = $("input[name='manyRow']:checked").val() ; //是否多行
	var lenValue = $("#lenValue").val(); // 长度
	var presisionValue = $("#presisionValue").val(); // 小数位
	var autoWrite =  $("input[name='autoWrite']:checked").val() ; //自动填写
	var initValue = $("#initValue").val(); // 初始值
	var rule = $("#ruleValue").val(); // 验证
	var baomi = $("input[name='baomi']:checked").val() ; //保密
	var selectLi =  $("#middle .theGrey")[0];
	if (selectLi) {
		$(selectLi).attr("titleValue",title);
		$(selectLi).attr("itemType",itemTypeValue);
		$(selectLi).attr("manyRow",manyRow);
		$(selectLi).attr("len",lenValue);
		$(selectLi).attr("presision",presisionValue);
		$(selectLi).attr("autoWrite",autoWrite);
		$(selectLi).attr("initValue",initValue);
		$(selectLi).attr("rule",rule);
		$(selectLi).attr("baomi",baomi);
		$("label",$(selectLi)).text(title);
		$("label",$(selectLi)).attr("title",title);
	}
}
function changeCheck(element) {
	if ($(element).is(':checked')) {
		$(element).attr("checked","checked");
	} else {
		$(element).removeAttr("checked");
	}
}
function save() {
	var retStr = '{"field":{';
	  $("#middle > li").each(function(i){
   	 var COLUMN_NAME_CN = $(this).attr("titlevalue"); // 指标名
		 var DATA_TYPE = $(this).attr("itemtype");   // 指标类型(文本...)
		 var CONTROL_TYPE = $(this).attr("manyrow");   // 是否多行1-是 2-否
		 var LENG = $(this).attr("len");   // 指标长度
		 var DATA_PRECISION = $(this).attr("presision");   // 小数位
		 var INITIAL_TYPE = $(this).attr("autowrite");   // 是否有初始值 1-是 2-否
		 var INITIAL_DATA = $(this).attr("initvalue");   // 指标类型
		 var ENCRYPT = $(this).attr("baomi");   // 指标类型
		 var VALIDATE_RULE = $(this).attr("rule");   // 指标类型
		 var nodeStr = '"' + COLUMN_NAME_CN + '":{"COLUMN_NAME_CN":"' + COLUMN_NAME_CN + '"'
			+ ',' + '"DATA_TYPE":"' + DATA_TYPE+ '"'
			+ ',' + '"CONTROL_TYPE":"' + CONTROL_TYPE+ '"'
			+ ',' + '"LENG":"' + LENG+ '"'
			+ ',' + '"DATA_PRECISION":"' + DATA_PRECISION+ '"'
			+ ',' + '"INITIAL_TYPE":"' + INITIAL_TYPE+ '"'
			+ ',' + '"INITIAL_DATA":"' + INITIAL_DATA+ '"'
			+ ',' + '"ENCRYPT":"' + ENCRYPT+ '"'
			+ ',' + '"VALIDATE_RULE":"' + VALIDATE_RULE+ '"'+ '}';
			  if($("#middle > li").length > (i+1) ) {
				  nodeStr += ','
			  }
		  retStr += nodeStr;    
 	  });
	  retStr += '}';
	  bootbox.alert(retStr);
	  $.ajax({
			type: "POST",
			url: 'indexes/saveNew',
	    	data: {data:retStr},
			dataType:'json',
			cache: false,
			success: function(result){
				bootbox.alert("save end");
				top.Dialog.close();
			}
		});
}
	
/**
 * 
 */
function showProperty(selectLi) {
	 $('#right > li').each(function(index,li){
		 $(li).show();
	 });
	 
	var title = $(selectLi).attr("titleValue");
	var itemType= $(selectLi).attr("itemType");
	var manyRow = $(selectLi).attr("manyRow");
	var len = $(selectLi).attr("len");
	var presision = $(selectLi).attr("presision");
	var autoWrite = $(selectLi).attr("autoWrite");
	var initValue = $(selectLi).attr("initValue");
	var baomi = $(selectLi).attr("baomi");
	var rule = $(selectLi).attr("rule");
	$("#titleValue").val(title); // 标题
	$("#itemTypeValue").val(itemType);  // 类型
	if (manyRow == '1') {
		$("input[name='manyRow']").eq(0).attr('checked', 'checked') ; //是否多行
	} else {
		$("input[name='manyRow']").eq(1).attr('checked', 'checked') ; //是否多行
	}
	$("#lenValue").val(len); // 长度
	$("#presisionValue").val(presision); // 小数位
	if (autoWrite == '1') {
		$("input[name='autoWrite']").eq(0).attr('checked', 'checked') ;
	} else {
		$("input[name='autoWrite']").eq(1).attr('checked', 'checked') ;
	}
	$("#initValue").val(initValue); // 初始值
	if (baomi == '1') {
		$("input[name='baomi']").eq(0).attr('checked', 'checked') ; //是否多行
	} else {
		$("input[name='baomi']").eq(1).attr('checked', 'checked') ; //是否多行
	}
	
	
	$("#ruleValue").val(rule); // 验证
	
	var itemType = selectLi.attr("itemType");
	// 文本
	if (itemType=='left_wb'){
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("#optionSource").parent().hide();
		$("#catogeryType").parent().hide();
		
	// 整数
	}else if (itemType=='left_zs'){
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("#optionSource").parent().hide();
		$("#catogeryType").parent().hide();
	// 小数
	} else if (itemType=='left_xs') {
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#optionSource").parent().hide();
		$("#catogeryType").parent().hide();
	// 复选
	} else if (itemType=='left_fx') {
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#len").parent().hide();
		$("#lenValue").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		
	// 单选
	} else if (itemType=='left_dx') {
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#len").parent().hide();
		$("#lenValue").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
	// 日期/时间
	} else if (itemType=='left_rq') {
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#len").parent().hide();
		$("#lenValue").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#optionSource").parent().hide();
		$("#catogeryType").parent().hide();
	// 图片
	} else if (itemType=='left_tp') {
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#manyRow").parent().hide();
		$("#optionSource").parent().hide();
		$("#catogeryType").parent().hide();
	// pdf
	} else if (itemType=='left_pdf') {
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#manyRow").parent().hide();
		$("#optionSource").parent().hide();
		$("#catogeryType").parent().hide();
	// office
	}  else if (itemType=='left_office') {
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#manyRow").parent().hide();
		$("#optionSource").parent().hide();
		$("#catogeryType").parent().hide();
	// 视频
	} else if (itemType=='left_sp') {
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#manyRow").parent().hide();
		$("#optionSource").parent().hide();
		$("#catogeryType").parent().hide();
	// 其他
	} else if (itemType=='left_qt') {
		$("#manyRow").parent().hide();
		$("input[name='manyRow']").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#manyRow").parent().hide();
		$("#optionSource").parent().hide();
		$("#catogeryType").parent().hide();
	} 
}

	  
</script>
</html>