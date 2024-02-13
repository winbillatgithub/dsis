function Draggable(options){
	var options = options||{};
	var tag = options.dragTag||"LI";//目前只支持li
	tag = tag.toUpperCase();
	var $dest = $(options.destId);//拖拽放入的容器对象
	$dest.on("dragover", function(ev) {
		ev.preventDefault();
	});
	$dest.on("drop", function(ev) {
		ev.preventDefault();
		var df = ev.originalEvent.dataTransfer;
		var sourceId = df.getData("sourceId");
		var fieldText = df.getData("fieldText");
		var eventTargetEl = ev.target;//拖拽到位置后鼠标所处目标对象
		var targetContainer = $(this).get(0);// 拖动到位置后所处当前容器
		if (sourceId==""){
			return false;
		}
		if ($("#" + sourceId).attr("draggable") == "false") {
			return false;
		}
		// 当前位置目标对象不是LI,也不是UL,说明是内部的label或者input,需要获取他们的父节点,即LI对象
		while (eventTargetEl.nodeName.toUpperCase() != 'LI' && eventTargetEl.nodeName.toUpperCase() != 'UL' && eventTargetEl.nodeName.toUpperCase() != 'BODY') 
		{ eventTargetEl = eventTargetEl.parentNode;}
		
		//do
			var litem = document.createElement('li');
			var item = $(litem);
			var fieldCol = $("#" + sourceId).attr("data-field");
			var liType =  $("#" + sourceId).attr("liType");
			var html = '<input style="float:left" type="checkbox" name="isSearch" onclick="changeCheck(this)" />'; // 是否检索项的框
			if (liType=='input_type_field'){
				html += '<input type="text" readonly value="' + fieldText + '"/>';
			}else if (liType=='label_type_field'){
				html += '<label class="cut">' + fieldText + '</label>';
			} else if (liType=='sys_type_field') {
				html += '<label class="cut">' + fieldText + '</label>';
			}
			html+='<a href="javascript:void(0)" class="pull-right" >X</a>';
			$(item).addClass("liClass2");
			$(item).attr("data-field",fieldCol);
			$(item).attr("sourceId",sourceId);
			$(item).append(html);
			if ($(this).children().length>0 && eventTargetEl != targetContainer){//当前容器下有li
				if(tag == '' || eventTargetEl.nodeName == tag){//当前目标对象的tagName是LI
					if (eventTargetEl.nextElementSibling){//如果当前对象的下一个兄弟节点存在，那么就将当前拖拽的对象插入到这个兄弟节点的前面
						eventTargetEl.parentNode.insertBefore($(item).get(0),eventTargetEl.nextElementSibling);
					}else{//兄弟节点不存，那么直接append到容器中
						targetContainer.appendChild($(item).get(0));
					}
				} 
			}else{//当前容器下没有li，直接append进这个容器中
				targetContainer.appendChild($(item).get(0));
			}
			// 拖拽完成后禁止左侧元素重复拖拽
			$("#" + sourceId).attr("draggable","false");
			$("#" + sourceId).addClass("theGrey"); // 禁止其拖动功能
		//} while(eventTargetEl != targetContainer && (eventTargetEl = eventTargetEl.parentNode));//当前目标对象不是容器，那么就将当前目标对象的父级节点赋值给当前目标对象
	});
	$dest.sortable(); 
	
//	return (function(){
//		alert("over");
//	})(options);
}