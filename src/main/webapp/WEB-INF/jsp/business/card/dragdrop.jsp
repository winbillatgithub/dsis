<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>拖拽测试</title>
    <link rel="stylesheet" href="/static/resource/css/jquery-ui.css">

  <style type="text/css">
	*{
		padding:0;
		margin:0;
		box-sizing: border-box;
	}
	.cut {
		float:left;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space: nowrap; 
		width:80%;
	}
	#left{
		min-height:450px;
		max-height:450px;
		width:380px;
		border:1px solid #000;
		float: left;
		margin-left:2px;
		padding:0 2px;
		position:relative;
		overflow: auto;
	}
	#middle{
		min-height:450px;
		max-height:450px;
		width:340px;
		border:1px solid #000;
		float: left;
		margin-left:2px;
		padding:0 2px;
		position:relative;
		overflow: auto;
	}
	#right{
		min-height:450px;
		max-height:450px;
		width:200px;
		border:1px solid #000;
		float: left;
		margin-left:10px;
		padding:0 10px;
		position:relative;
	}
	.theGrey{
	    background:#ccc!important;
	}
  	.liClass {
		float: left;
		background: #CCFFFF;
		height:30px;
		width:70px;
		border:1px solid #5F9EDF;
		margin: 2px 0;
		text-overflow:ellipsis;
		white-space: nowrap; 
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
 <script src="/static/resource/js/draggableCard.js" type="text/javascript"></script>
    <script src="/static/ace/js/bootbox.js"></script>
  <script>
  $(function() {
	    if (typeof top.hangge === "function") {$(top.hangge());}//关闭加载状态
	  
  		//可拖拽组件初始化
		$('.cpList li').each(function(){
			$(this).attr("draggable","true");
			$(this).on("dragstart", function(ev) {//开始拖拽
				var dt = ev.originalEvent.dataTransfer;
				dt.setData('sourceId', ev.target.id);//将拖拽组件ID传入
				dt.setData('fieldText', $(ev.target).text());//将拖拽组件文本传入
			});
		});
		//拖拽组件
		Draggable({destId:".tab",dragTag:"li"});
		
		// 对已经存在的字段,左侧对应指标禁止拖拽
		$("#middle > li").each(function(i){
			 var INDEXES_ID = $(this).attr("sourceId");
			 $("#" + INDEXES_ID).addClass("theGrey");
			 $("#" + INDEXES_ID).attr("draggable","false");
			 
	    });

         $("#middle").on("click", "a",function () {
        	 var field=$(this).parent().attr("data-field");
             var parentLi = $(this).parent();
             if (confirm("确定删除吗?")) {
            	 parentLi.remove();
                 //bootbox.alert("删除元素");
                 $("#left .liClass").each(function(){
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
                 });
             }
         });
  });
  
 
  </script>
</head>
<body>
 <div class="container">
 	<div class="row">
 	<div class="col-xs-5">
 	<div ><h3>我的指标库</h3></div>
	<ul id="left" class="cpList">
		<c:forEach items="${varList}" var="var" varStatus="vs">							
		    <li id="${var.INDEXES_ID}" liType="label_type_field"  data-field="${var.COLUMN_NAME_EN}" class="liClass">${var.COLUMN_NAME_CN}</li>
	    </c:forEach>
	</ul>
 	</div>
 	<div class="col-xs-4">
 	<div><h3>模型指标(搜索用字段请打钩)</h3></div>
	<ul id="middle" class="tab">
		<c:forEach items="${colList}" var="var" varStatus="vs">
			<li class="liClass2" data-field="${var.COLUMN_NAME_EN}" sourceid="${var.INDEXES_ID}">
				
				<input <c:if test="${var.SEARCH =='1'}" >checked</c:if> style="float:left" type="checkbox" name="isSearch" onclick="changeCheck(this)"><label class="cut">${var.COLUMN_NAME_CN}</label>
			<a href="javascript:void(0)" class="pull-right">X</a></li>
		</c:forEach>
	</ul>
 </div>
 	<div class="col-xs-3">
 	<div><h3>系统指标库</h3></div>
 	<ul id="right" class="cpList">
	    <!-- <li id="right_student_custom" liType="input_type_field" data-field="student_custom" class="liClass">输入框</li> -->
	    <c:forEach items="${sysList}" var="item">                 
             <li id="${item.INDEXES_ID}" liType="sys_type_field" data-field="${item.COLUMN_NAME_EN}"  class="liClass">${item.COLUMN_NAME_CN}</li>         
         </c:forEach>
	    
	</ul>
	</div>
 	</div>
 	<div class="row" >
 			<div class="col-xs-6">
 			
			模板ID：<input type="text" readonly=true value='${tableId}' id="modelId" style="height: 32px;margin-right:5px;"/>
			</div>
			<div class="col-xs-6">
			模型描述：<input type="text" value='' id="modelDesc" style="height: 32px;margin-right:5px;"/>
			<button type="button" onclick="save()" >保存</button>
		</div>
		<input type="hidden" value='${colListJson}' id="initIndexs">
	</div>
</div>
</body>
<script>

function changeCheck(element) {
	if ($(element).is(':checked')) {
		$(element).attr("checked","checked");
	} else {
		$(element).removeAttr("checked");
	}
	
	
}
function save() {
	  var modelId = $("#modelId").val();
	  var modelDesc = $("#modelDesc").val();
	  var initIndexs = $("#initIndexs").val();
	  var retStr = '{"model":{"modelId":"' + modelId +'","modelDesc":"' + modelDesc + '","initIndexs":' + initIndexs + '},"field":[';
	  $("#middle > li").each(function(i){
		 var INDEXES_ID = $(this).attr("sourceId");
     	 var COLUMN_NAME_EN = $(this).attr("data-field");
		 var COLUMN_NAME_CN = $(this).find("label").text();
		 var field_checked = $(this).find("input[type=checkbox]").attr("checked");
		 //bootbox.alert("checked:" + field_isSearch);
		 var SEARCH = "0";
		 if (SEARCH == "checked") {
			 SEARCH = "1";
		 }
		 var nodeStr = '{"INDEXS_ID":"' + INDEXES_ID + '"'
		 	+ ',' + '"COLUMN_NAME_EN":"' + COLUMN_NAME_EN+ '"'
			+ ',' + '"COLUMN_NAME_CN":"' + COLUMN_NAME_CN+ '"'
			+ ',' + '"SEARCH":"' + SEARCH+ '"'
			+ ',' + '"SORT_NO":"' + i+ '"'+ '}';
			 if($("#middle > li").length > (i+1) ) {
				  nodeStr += ','
			  }
		  retStr += nodeStr;    
   	  });
	  retStr += ']}';
	  bootbox.alert(retStr);
	  $.ajax({
			type: "POST",
			url: '/card/saveNew',
	    	data: {data:retStr},
			dataType:'json',
			cache: false,
			success: function(result){
				bootbox.alert("save end");
				top.Dialog.close();
			}
		});
}
	  
</script>
</html>