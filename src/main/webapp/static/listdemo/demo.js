/**
 *模型分析
 */


/**模型计数器*/
var modelCounter = 0;
/**
 * 初始化一个jsPlumb实例
 */
var instance = jsPlumb.getInstance({
	DragOptions: { cursor: "pointer", zIndex: 2000 },
	ConnectionOverlays: [
			[ "Arrow", {
			    location: 1,
			    visible:true,
			    width:11,
			    length:11,
			    direction:1,
			    id:"arrow_forwards"
			} ],
			[ "Arrow", {
			    location: 0,
			    visible:true,
			    width:11,
			    length:11,
			    direction:-1,
			    id:"arrow_backwards"
			} ],
            [ "Label", {
                location: 0.5,
                id: "label",
                cssClass: "aLabel"
            }]
        ],
	Container: "container"
});
instance.importDefaults({
	 ConnectionsDetachable:true,
	 ReattachConnections:true
});
/**
 * 设置左边菜单
 * @param Data
 */
function setLeftMenu(list)
{
	for(var obj in list){
		for(var tmp in list[obj]){
			var element_str = '<li id="' + list[obj][tmp].code + '" model_code="' + tmp + '">' + list[obj][tmp].name + '</li>';
			$("#leftMenu").append(element_str);
		}
	}
	//拖拽设置
	$("#leftMenu li").draggable({
		helper: "clone",
		scope: "plant"
	});
	$("#container").droppable({
		scope: "plant",
		drop: function(event, ui){
			CreateModel(ui, $(this));
		}
	});
}

function intModel(initdata)
{
	// 初始值不为空,说明是编辑
	if (!jQuery.isEmptyObject(initdata)) {
	//if(sessionStorage.getItem("flowsheet")){
		//var flowsheet=JSON.parse(sessionStorage.getItem("flowsheet"));
		var flowsheet=initdata;
		var modelName = flowsheet.model.modelName;
		$("#modelName").val(modelName);
	    var modelDesc = flowsheet.model.modelDesc;
	    $("#modelDesc").val(modelDesc);
		for(var key in flowsheet.nodes){  
			var mainHTML=""
            var id = key; // id
            var mode=flowsheet.nodes[key];
            var code = $(mode).attr("code"); // 表名
            var modelType = $(mode).attr("modelType"); //原表从表
            var left = $(mode).attr("left"); // 位置
            var top = $(mode).attr("top"); // 
            mainHTML+='<div class="model" id="' + id + '" modelCode="' + code + '" modelType="' + modelType +'">' + getModelElementStr(id,code,modelType) + '</div>';
            $("#container").append(mainHTML);
            $("#"+id).css("position","absolute").css("left",left).css("top",top);
            
            instance.addEndpoint(id, { anchors: "TopCenter" }, hollowCircle2);
            
             var liList = $("#"+id).find("li");
	       	 for(var i=0;i<liList.length;i++){
	       		 	instance.addEndpoint(liList[i].id, { anchors: "LeftMiddle" }, hollowCircle);
	       			instance.addEndpoint(liList[i].id, { anchors: "RightMiddle" }, hollowCircle);
	       	 }
	       	// 在左边列表中禁用此表
	       	$("#" + code).addClass("theGrey").draggable("disable");
	       //注册实体可draggable
        	$("#" + id).draggable({
        		containment: "parent",
        		drag: function () {
        			instance.repaintEverything();
        		},
	      	      stop: function () {
	      	    	  instance.repaintEverything();
	      	      }
        	});
        	/*$("#" + id).resizable({
        		containment: "parent",
    	      resize: function () {
    	    	  instance.repaintEverything();
    	      },
    	      stop: function () {
    	    	  instance.repaintEverything();
    	      }
        	});*/
		}
		for(var key in flowsheet.lines){  
			var line=flowsheet.lines[key];
            var fromId = line.from; // source
            var toId = line.to; // id
            var labelText = line.label; 
            var formPointLocation = line.fromType; 
            var toPointLocation = line.toType; 
            var twoWay = line.twoWay;
           var sourceEndpoints = instance.getEndpoints($("#" + fromId));
           var targetEndpoints = instance.getEndpoints($("#" + toId));
           var sourceEndpiont=null;
           var targetEndpoint=null;
           if ("LeftMiddle" == formPointLocation) {
        	   sourceEndpiont = sourceEndpoints[0];
           } else {
        	   sourceEndpiont = sourceEndpoints[1];
           }
           if ("LeftMiddle" == toPointLocation) {
        	   targetEndpoint = targetEndpoints[0];
           } else {
        	   targetEndpoint = targetEndpoints[1];
           }
           var conn=instance.connect( {source:sourceEndpiont, target:targetEndpoint} );
            conn.getOverlay("label").setLabel(labelText);
            if (twoWay == false) {
            	conn.hideOverlay("arrow_backwards");
            	conn.setParameter("twoWay",false);
            } else {
            	conn.showOverlay("arrow_backwards");
            	conn.setParameter("twoWay",true);
            }
            
		}
		
		
	}
}

function deepCopy(p, c) {  //克隆对象
    var c = c || {};  
    for (var i in p) {  
        if(! p.hasOwnProperty(i)){  
            continue;  
        }  
        if (typeof p[i] === 'object') {  
            c[i] = (p[i].constructor === Array) ? [] : {};  
            deepCopy(p[i], c[i]);  
        } else {  
            c[i] = p[i];  
        }  
    }  
    return c;  
} 

/**
 * 添加模型
 * @param ui
 * @param selector
 */
function CreateModel(ui, selector)
{
	var modelId = $(ui.draggable).attr("id");
	var id = modelId + "_model_" + modelCounter++;
	var code = $(ui.draggable).attr("model_code");
	$(selector).append('<div class="model" id="' + id + '" modelCode="'+ code + '" modelType="" >' 
			+ getModelElementStr(id,code,'') + '</div>');
	var left = parseInt(ui.offset.left - $(selector).offset().left);
	var top = parseInt(ui.offset.top - $(selector).offset().top);
	$("#"+id).css("position","absolute").css("left",left).css("top",top);
	//添加连接点(不加这句出来点位置不对,以后再研究能否去掉)
	instance.addEndpoint(id, { anchors: "TopCenter" },hollowCircle2);


	 var liList = $("#"+id).find("li");
	 for(var i=0;i<liList.length;i++){
		 	instance.addEndpoint(liList[i].id, { anchors: "LeftMiddle" },  hollowCircle);
			instance.addEndpoint(liList[i].id, { anchors: "RightMiddle" }, hollowCircle);
	 }
	
	//注册实体可draggable
	$("#" + id).draggable({
		containment: "parent",
		drag: function (event, ui) {
			instance.repaintEverything();
		},
		stop: function () {
			instance.repaintEverything();
		}
	});
	// 表加载完添加右键监听
	$("#" + id).bind("contextmenu", function(){
		onContextMenu(mainMenu,window.event,this);
	})
	$(ui.draggable).addClass("theGrey").draggable("disable");
	
}

//基本连接线样式
var connectorPaintStyle = {
	stroke: "#61B7CF",
	strokeWidth: 2
};
//鼠标悬浮在连接线上的样式
var connectorHoverStyle = {
	    strokeWidth: 2,
	    stroke: "#216477",
	    outlineWidth: 4
};
//端点样式设置
var hollowCircle = {
	endpoint: ["Dot",{ cssClass: "endpointcssClass"}], //端点形状
	connectorStyle: connectorPaintStyle,
	connectorHoverStyle: connectorHoverStyle,
	paintStyle: {
		fill: "#803e18",
		radius: 3
	},		//端点的颜色样式
	isSource: true, //是否可拖动（作为连接线起点）
	connector: ["Flowchart", {stub: 30, gap: 0, coenerRadius: 0, alwaysRespectStubs: true, midpoint: 0.5 }],
	isTarget: true, //是否可以放置（连接终点）
	maxConnections: -1
};
//端点样式设置
var hollowCircle2 = {
	endpoint: ["Dot",{ cssClass: "displaynone"}], //端点形状
	connectorStyle: connectorPaintStyle,
	connectorHoverStyle: connectorHoverStyle,
	paintStyle: {
		fill: "#803e18",
		radius: 0.1
	},		//端点的颜色样式
	isSource: false, //是否可拖动（作为连接线起点）
	connector: ["Flowchart", {stub: 30, gap: 0, coenerRadius: 0, alwaysRespectStubs: true, midpoint: 0.5 }],
	isTarget: false, //是否可以放置（连接终点）
	maxConnections: -1
};
/**
 * 创建模型内部元素
 * @param code
 * @returns {String}
 */
function getModelElementStr(pid,code,modelType)
{
	if (modelType == '' || modelType =='undefined') {
		modelType = '';
	} else {
		modelType = '[' + modelType + ']';
	}
	var list = '';
	for(var data in metadata){
		for(var data_code in metadata[data]){
			var model_data = metadata[data][data_code];
			if(code == model_data.code){
				list += '<h4><span index="' 
					+ model_data.code + '">' 
					+ model_data.name 
					+ '</span><span name=modelType>' + modelType + '</span><a href="javascript:void(0)" class="pull-right" onclick="removeElement(this);">X</a>'
					+ '</h4>';
				list += '<ul>'
				var properties = model_data.properties;
				list += parseProperties(pid,properties,code);
				list += '</ul>';
			}
		}
	}
	return list;
}


function sortByNo(a,b){  
	
    return a.sortno-b.sortno ; 
 }
/**
 * 循环遍历properties
 * @param obj
 * @returns {String}
 */
function parseProperties(pid,obj,tableName)
{
	var str = "";
	obj.sort(sortByNo);
	for(var i=0;i<obj.length;i++){
		var colProperty = obj[i];
		
		if(colProperty.colCode != undefined){
			str += '<li id=' + pid + '_' + colProperty.colCode +  '  tabName=' + tableName +   '  colCode=' + colProperty.colCode + '>' 
				+ colProperty.des + '</li>';
		}else{
			//str += arguments.callee(colProperty);
			alert("parseProperties  error " );
			str += "";
		}
	}
	return str;
}
//设置连接Label的label
function init(conn)
{
//	var label_text;
//	alert("init");
//	conn.hideOverlay("arrow_backwards");
//	$("#select_sourceList").empty();
//	$("#select_targetList").empty();
	/*var sourceName = $("#" + conn.sourceId).attr("modelCode");
	var targetName = $("#" + conn.targetId).attr("modelCode");
	for(var i = 0; i < metadata.length; i++){
		for(var obj in metadata[i]){
			if(obj == sourceName){
				var optionStr = getOptions(metadata[i][obj].properties,metadata[i][obj].name);
				$("#select_sourceList").append(optionStr);
			}else if(obj == targetName){
				var optionStr = getOptions(metadata[i][obj].properties,metadata[i][obj].name);
				$("#select_targetList").append(optionStr);
			}
		}
	}*/
//	$("#submit_label").unbind("click");
//	$("#submit_label").on("click",function(){
//		setlabel(conn);
//	});
//	$("#myModal").modal();
	
	setlabel(conn);
}
/**
 * 获取option
 * @param obj
 * @returns {String}
 */
function getOptions(obj,head)
{
	var str = "";
	for(var v in obj){
		if(obj[v].properties == undefined){
			var val = head + '.' + obj[v].des;
			str += '<option value="' + val + '">'
					+val
					+'</option>';
		}else{
			str += arguments.callee(obj[v].properties,head);
		}
	}
	return str;
}
//setlabel
function setlabel(conn)
{
	conn.getOverlay("label").setLabel($("#lineType").val());
	
	if ($("#wayType").val() == '双向') {
		conn.setParameter("twoWay",true);
	} else {
		conn.setParameter("twoWay",false);
	}

}
//删除节点
function removeElement(obj)
{
	var element = $(obj).parents(".model");
	var modelName = $(element).attr("modelCode");
	if(confirm("确定删除该模型？")) {
		 instance.remove(element);
		 $("#leftMenu > li").each(function(){
         	if($(this).attr("model_code")==modelName){
         		$(this).removeClass("theGrey").draggable("enable"); // 激活其拖动功能
         	}
         });
	}
		
}

