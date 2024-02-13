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
	
	<!-- 下拉框 -->
	<link rel="stylesheet" href="/static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="/static/ace/css/datepicker.css" />

	<script src="/static/js/common/common.js"></script>
	<link rel="stylesheet" href="/static/listdemo/bootstrap.min.css" />
	<link rel="stylesheet" href="/static/listdemo/jquery-ui.custom.min.css" />
	<link rel="stylesheet" href="/static/listdemo/demo.css" />
	<script type="text/javascript" src="/static/listdemo/jquery-2.1.4.min.js"></script>
	<script type="text/javascript" src="/static/listdemo/jquery-ui.min.js"></script>
	<script src="/static/listdemo/bootstrap.min.js"></script>
	<script type="text/javascript" src="/static/listdemo/jsPlumb-2.2.8.js"></script>
	<script type="text/javascript" src="/static/listdemo/demo.js"></script>
	<script type="text/javascript" src="/static/listdemo/addModel.js"></script>
    <script src="/static/ace/js/bootbox.js"></script>
	<script type="text/javascript">
		var metadata;
		if (typeof top.hangge === "function") {$(top.hangge());}
		jQuery(document).ready(function(){
			metadata= ${metadata};
			var initdata = ${initdata};
// 			bootbox.alert(initdata);
// 			bootbox.alert(metadata);
			setLeftMenu(metadata);
			intModel(initdata);
			//监听新的连接
				instance.bind("connection", function (connInfo, originalEvent) {
					if (connInfo.connection.sourceId == connInfo.connection.targetId) {      
						instance.detach(connInfo);      
		                bootbox.alert("不能连接自己！");      
		            }  else {
		            	init(connInfo.connection);
		            }
		            
		        }); 
					
					// 绑定右键事件
				  instance.bind("contextmenu", function (conn, originalEvent) {
				
	                 onContextMenu(menu,originalEvent,conn);
	                 return false;
		        });
				  
				// 表类型模态框确认按钮
			   $("#submit_label2").on("click",function(){
					var modelType = $("#modelType").val();
					var modelName = $(nowRigthObj).attr("modelCode");
					$(nowRigthObj).attr("modelType",modelType);
					$(nowRigthObj).find('span[name=modelType]').text("[" +modelType + "]")
					//bootbox.alert(modelName);
				});
				// 连线模态框确认按钮
			   $("#submit_label").on("click",function(){
					var modalLineType = $("#modalLineType").val();
					nowRigthObj.getOverlay("label").setLabel(modalLineType);
					
					if (modalLineType == '内连') {
						nowRigthObj.hideOverlay("arrow_backwards");
						nowRigthObj.setParameter("twoWay",false);
		            } else {
		            	nowRigthObj.showOverlay("arrow_backwards");
		            	nowRigthObj.setParameter("twoWay",true);
		            }
					
				});
			   
			   
		});
		
		function save() {
			
			var dataJson = saveToDb(metadata);
			//bootbox.alert(dataJson);
			var url = '/listlistrule/saveNew.do?tm='+new Date().getTime();
			var param = "{'data':'" + dataJson + "'}";
	    	var obj = eval('(' + param + ')');
			callAjax(url, true, obj, function(data) {
				if (data.success == true) {
					$("#modelName").val(data.id);
					//top.Dialog.close();
				} else {
					bootbox.alert(data.msg);
				}
			});

			/* $.ajax({
				type: "POST",
				url: '/listlistrule/saveNew',
		    	data: {data:dataJson},
				dataType:'json',
				cache: false,
				success: function(result){
					//bootbox.alert("save end");
					//top.Dialog.close();
				}
			}); */
			
		}
		function changeLine() {
			if ($("#lineType").val() == '关系映射') {
				$("#wayType").val("双向");
			} else if ($("#lineType").val() == '外连') {
				$("#wayType").val("双向");
			}  else if ($("#lineType").val() == '内连') {
				$("#wayType").val("单向");
			} 
			changeArrow();
		}
		function changeArrow() {
			if ($("#wayType").val() == '双向') {
				// 第二种箭头,就是后箭头可见,即双向
				instance.Defaults.ConnectionOverlays[1][1].visible = true;
			} else if ($("#wayType").val() == '单向') {
				instance.Defaults.ConnectionOverlays[1][1].visible = false;
			}
		}
	</script>
	
</head>
<body>
<menu id="lineMenu" class="menu">
    <li class="menu-item">
        <button type="button" class="menu-btn" onclick="menuClick1()">
            <i class="fa fa-folder-open"></i>
            <span class="menu-text">属性</span>
        </button>
    </li>
    <li class="menu-item">
        <button type="button" class="menu-btn" onclick="menuClick2()">
            <i class="fa fa-folder-open"></i>
            <span class="menu-text">删除</span>
        </button>
    </li>
   <!-- 以后扩展用,2级3级菜单 <li class="menu-item submenu">
        <button type="button" class="menu-btn">
            <i class="fa fa-users"></i>
            <span class="menu-text">Social</span>
        </button>
        二级菜单
        <menu class="menu">
            <li class="menu-item">
                <button type="button" class="menu-btn">
                    <i class="fa fa-comment"></i>
                    <span class="menu-text">Comment</span>
                </button>
            </li>
            <li class="menu-item submenu">
                <button type="button" class="menu-btn">
                    <i class="fa fa-share"></i>
                    <span class="menu-text">Share</span>
                </button>
                三级菜单
                <menu class="menu">
                    <li class="menu-item">
                        <button type="button" class="menu-btn">
                            <i class="fa fa-twitter"></i>
                            <span class="menu-text">Twitter</span>
                        </button>
                    </li>
                    
                </menu>
            </li>
        </menu>
    </li> -->
</menu>   
<menu id="mainMenu" class="menu">
    <li class="menu-item">
        <button type="button" class="menu-btn" onclick="menuClick3()">
            <i class="fa fa-folder-open"></i>
            <span class="menu-text">属性</span>
        </button>
    </li>  
</menu> 
<div id="demo"  style="margin:20px;">
	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-2" style="min-height: 500px;background-color: #62a8d1;border-radius:5px;padding-top: 12px;width: auto;">
				<div class="content_wrap" style="width: 100%;">
					<div class="left" style="width: 100%;">
						<ul id="leftMenu" style="overflow: auto;margin: 0px;padding:0px;"></ul>
					</div>
				</div>
			</div>
			<div class="col-xs-10">
				<div id="configDiv">
					<div class="row">
					<div class="col-xs-4">
					连线类型:<select id="lineType" onchange="changeLine()">
						<option value="关系映射">关系映射</option>
						<option value="内连">内连</option>
						<option value="外连">外连</option>
						</select>
					</div>
					<div class="col-xs-8">
					箭头类型:<select id="wayType" onchange="changeArrow()" disabled>
						<option value="双向">双向</option>
						<option value="单向">单向</option>
						</select>
					</div>
				</div>
				<div id="container"></div>
			</div>
		</div>
	</div>
		<div style="padding-top: 10px;">
			模型名称：<input type="text" value='' id="modelName" style="height: 32px;margin-right:5px;"/>
			模型描述：<input type="text" value='' id="modelDesc" style="height: 32px;margin-right:5px;"/>
			<button class="btn btn-white btn-info" type="button" onclick="save()" style="display: inline-block;">
										<i class="ace-icon fa fa-check bigger-120 blue"></i>保存111111</button>
		</div>



</div>
</div>
<!-- 弹出框1 -->
	 <div id="myModal" class="modal fade" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">请选择连线类型</h4>
				</div>
				<div class="modal-body">
					连线类型:<select id="modalLineType" >
						<option value="关系映射">关系映射</option>
						<option value="内连">内连</option>
						<option value="外连">外连</option>
					</select>
					
				</div>
				<div class="modal-footer">
					<button id="submit_label" type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
				</div>
			</div>
		</div>
	</div> 
<!-- 弹出框2 表的属性-主表元表从表 -->
	<div id="myModal2" class="modal fade" data-backdrop="static" aria-labelledby="myModalLabel2" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel2">请选择表类型</h4>
				</div>
				<div class="modal-body">
				表类型:<select id="modelType" >
					<option value="主表">主表</option>
					<option value="源表">源表</option>
					<option value="从表">从表</option>
					</select>
					
				</div>
				<div class="modal-footer">
					<button id="submit_label2" type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var menu = document.querySelector('#lineMenu');
	var mainMenu = document.querySelector('#mainMenu');
	
	var validMenu; // 当前应该出现的菜单(是表的菜单还是线的菜单)
	var nowRigthObj;// 当前右键单击的对象
	function showMenu(x, y){
		validMenu.style.left = x + 'px';
		validMenu.style.top = y + 'px';
		validMenu.classList.add('show-menu');
	}
	function hideMenu(){
		validMenu.classList.remove('show-menu');
	}
	function onContextMenu(obj,e,srcObj){
		nowRigthObj = srcObj;
		validMenu = obj;
	    e.preventDefault();
	    showMenu(e.pageX, e.pageY);
	    document.addEventListener('click', clickAfterMenu, false);
	}

	function clickAfterMenu() {
		//bootbox.alert("clickaftermenu");
		hideMenu(validMenu);
		document.removeEventListener('click', clickAfterMenu);
	}
	function menuClick1(ev) {
		//bootbox.alert("属性");
		var modalLineType  = nowRigthObj.getOverlay("label").label;
		$("#modalLineType").val(modalLineType);
		$("#myModal").modal();
	}
	function menuClick2(ev) {
		//bootbox.alert("删除");
		if (confirm("要删除此连接么?")){
		    instance.detach(nowRigthObj);
	   	} 
	}
	function menuClick3(ev) {
		var modelType = $(nowRigthObj).attr("modelType");
		if (modelType != '' && modelType != null && modelType != 'undefined') {
			$("#modelType").val(modelType);
		}
		
		$("#myModal2").modal();
	}
	/* function onMouseDown(e){
	    //hideMenu();	  
	    if (3== e.which) {
	    	bootbox.alert("右键");
	    } else if (2== e.which) {
	    	bootbox.alert("中键");
	    } else if(1== e.which) {
	    	bootbox.alert("左键");
	    }
	    bootbox.alert(e.bubbles );
	   var flag =  isParent(event.target, $(".menu")[0])
		if (flag == false) {
			hideMenu();
		} else {
			
			
		}
	    document.removeEventListener('mousedown', onMouseDown);
	} */
	
	
	
	
	/* document.addEventListener('contextmenu', onContextMenu, false);   */
	
	/* function isParent (obj,parentObj){
	    while (obj != undefined && obj != null && obj.tagName.toUpperCase() != 'BODY'){
	        if (obj == parentObj){
	            return true;
	        }
	        obj = obj.parentNode;
	    }
    	return false;
	} */
</script>
</html>