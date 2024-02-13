var htmlMap={
		left_wb:'<input type=text  class="liInput" value="请输入文本" readonly=true />',
		left_zs:'<input type=text  class="liInput" value="请输入整数" readonly=true />',
		left_xs:'<input type=text  class="liInput" value="请输入浮点数" readonly=true />',
		left_fx:'<img src="static/resource/img/check.JPG"  class="liInput" />',
		left_dx:'<img src="static/resource/img/radio.JPG"  class="liInput" />',
		left_rq:'<input type=text  class="liInput" value="请输入日期" readonly=true />',
		left_sj:'<input type=text  class="liInput" value="请输入时间" readonly=true />',
		left_rqsj:'<input type=text  class="liInput" value="请输入日期和时间" readonly=true />',
		left_tp:'<img src="static/resource/img/pic.JPG"  class="liInput" />',
		left_pdf:'<img src="static/resource/img/pdf.JPG"  class="liInput" />',
		left_office:'<img src="static/resource/img/word.JPG"  class="liInput" />',
		left_sp:'<img src="static/resource/img/mp4.JPG"  class="liInput" />',
		left_qt:'<img src="static/resource/img/other.JPG"  class="liInput" />'
};

function Draggable(options) {
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
    var itemType = df.getData("itemType");
    var dataType = df.getData("dataType");
		var fieldText = df.getData("fieldText");
		var eventTargetEl = ev.target;            // 拖拽到位置后鼠标所处目标对象
		var targetContainer = $(this).get(0);     // 拖动到位置后所处当前容器
		if (itemType == "") {
			return false;
		}
		if ($("#" + itemType).attr("draggable") == "false") {
			return false;
		}
    var controlType = getControlType(itemType);
    var itemType =  $("#" + itemType).attr("itemType");
		// 当前位置目标对象不是LI,也不是UL,说明是内部的label或者input,需要获取他们的父节点,即LI对象
		while (eventTargetEl.nodeName.toUpperCase() != 'LI'
      && eventTargetEl.nodeName.toUpperCase() != 'UL'
      && eventTargetEl.nodeName.toUpperCase() != 'BODY') {
      eventTargetEl = eventTargetEl.parentNode;
    }

    var litem = document.createElement('li');
    var item = $(litem);
    $(item).attr("dataType", dataType);
    $(item).attr("controlType", controlType);
    $(item).attr("itemType", itemType);

    var html = makeLiHtml(controlType, '指标名');

    // liclass2 宽度是liclass2倍
    // 修改css宽度
    var selected = $("[name='radio-input']").filter(":checked");
    var selected = selected.val();
    if (selected == 'radio-two') {
      $(item).addClass(controlType + "-field form-field liClass-two-col liClass-indexes liClass-new");
    } else {
      $(item).addClass(controlType + "-field form-field liClass-one-col liClass-indexes liClass-new");
    }
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
    $(item).click();
    // 拖拽完成后禁止左侧元素重复拖拽
    //$("#" + itemType).attr("draggable","false");
    //$("#" + itemType).addClass("theGray"); // 禁止其拖动功能
		//} while(eventTargetEl != targetContainer && (eventTargetEl = eventTargetEl.parentNode));//当前目标对象不是容器，那么就将当前目标对象的父级节点赋值给当前目标对象
	});
	$dest.sortable();
}

function getControlType(itemType) {
  if (itemType == 'left_wb') {
    return 'text';
  } else if (itemType == 'left_dh') {
    return 'textarea';
  } else if (itemType == 'left_zs' || itemType == 'left_xs') {
    return 'number';
  } else if (itemType == 'left_fx') {
    return 'checkbox-group';
  } else if (itemType == 'left_dx') {
    return 'radio-group';
  } else if (itemType == 'left_xl') {
    return 'select';
  } else if (itemType == 'left_rq') {
    return 'date';
  } else if (itemType == 'left_sj') {
    return 'time';
  } else if (itemType == 'left_rqsj') {
    return 'datetime';
  } else if (itemType == 'left_tp' || itemType == 'left_pdf' || itemType == 'left_office' || itemType == 'left_sp' || itemType == 'left_qt') {
    return 'asfile';
  } else {
    return 'text';
  }
}

function makeLiHtml(controlType, titleValue) {
  var html = '';
  html += '<label class="field-label cut float-item-left" title="标题">' + titleValue + '</label>';
  html += '<span class="required-asterisk float-item-left"> *</span>';
  html += '<div class="field-actions float-item-right">';
  html += '<a type="remove" class="del-button icon-cancel delete-confirm"></a>';
  html += '<a type="copy" class="copy-button icon-copy"></a>';
  html += '</div>';
  html += '<div class="prev-holder">';
  html += '<div class="fb-' + controlType + '">';
  if (controlType == 'checkbox-group') {
    html += '<div class="' + controlType + '" style="height: 25px;clear:both;">';
    html += '<div class="checkbox">';
    html += '<input name="xxx" value="option-1" type="checkbox" checked="checked" />';
    html += '<label>Option-1</label>';
    html += '</div>';
    html += '</div>';
  } else if (controlType == 'select') {
    html += '<select class="form-control" style="height: 25px;clear:both;">';
    html += '<option selected="true" value="option-1">选项1</option>';
    html += '<option value="option-2">选项2</option>';
    html += '</select>';
  } else if (controlType == 'radio-group') {
    html += '<div class="' + controlType + '" style="height: 25px;clear:both;">';
    html += '<div class="radio-inline">';
    html += '<input name="xxx" value="option-1" type="radio" checked="checked" />';
    html += '<label>选项1</label>';
    html += '</div>';
    html += '<div class="radio-inline">';
    html += '<input name="xxx" value="option-2" type="radio" checked="checked" />';
    html += '<label>选项2</label>';
    html += '</div>';
    html += '</div>';
  } else if (controlType == 'asfile') {
    html += '<div class="' + controlType + '" style="height: 25px;clear:both;">';
    html += '<button type="button" class="btn-file-up btn-info">上传</button>';
    html += '<button type="button" class="btn-file-dl btn-info">下载</button>';
    html += '<button type="button" class="btn-file-pre btn-info">预览</button>';
    html += '</div>';
  } else if (controlType == 'datetime') {
    html += '<div class="input-group datetime form_datetime col-md-5" style="height:25px;width:100%;clear:both;">';
    html += '<input class="" style="height:20px;width:100%;" type="text" value="yyyy/mm/dd hh:mm:ss" readonly />';
    //html += '<span class="input-group-addon"> <span class="glyphicon glyphicon-th"></span></span>';
    html += '</div>';
  } else {
    html += '<input name="xxxx" type="' + controlType + '" class="form-control" style="height: 25px;" />';
  }
  html += '</div>';
  html += '</div>';
  return html;
}

function copyLiItem(selectedLi) {
  // selectedLi.attr("id");
  // dataType, controlType, itemType已存在, 而且固定, 没必要修改
  var liItem = document.createElement('li');
  var item = $(liItem);
  $(item).attr("indexesId", '');  // new added item, indexesId should be empty
  $(item).attr("dataType", $(selectedLi).attr("dataType"));
  $(item).attr("controlType", $(selectedLi).attr("controlType"));
  $(item).attr("itemType", $(selectedLi).attr("itemType"));
  $(item).attr("titleValue", $(selectedLi).attr("titleValue"));
  $(item).attr("subType", $(selectedLi).attr("subType"));
  $(item).attr("len", $(selectedLi).attr("len"));
  $(item).attr("presision", $(selectedLi).attr("presision"));
  $(item).attr("autoWrite", $(selectedLi).attr("autoWrite"));
  $(item).attr("initValue", $(selectedLi).attr("initValue"));
  $(item).attr("rule", $(selectedLi).attr("rule"));
  $(item).attr("baomi", $(selectedLi).attr("baomi"));
  $(item).attr("dataSource", $(selectedLi).attr("dataSource"));
  $(item).attr("changedFlag", "added");

  var html = makeLiHtml(controlType, $(selectedLi).attr("titleValue"));

  // liclass2 宽度是liclass2倍
  // 修改css宽度
  var selected = $("[name='radio-input']").filter(":checked");
  var selected = selected.val();
  if (selected == 'radio-two') {
    $(item).addClass(controlType + "-field form-field liClass-two-col liClass-indexes liClass-new");
  } else {
    $(item).addClass(controlType + "-field form-field liClass-one-col liClass-indexes liClass-new");
  }
  $(item).append(html);

  if ($(selectedLi).get(0)) {
    selectedLi = $(selectedLi).get(0);
    if (selectedLi.nextElementSibling) { //如果当前对象的下一个兄弟节点存在，那么就将当前拖拽的对象插入到这个兄弟节点的前面
      selectedLi.parentNode.insertBefore($(item).get(0), selectedLi.nextElementSibling);
    } else {//兄弟节点不存，那么直接append到容器中
      selectedLi.parentNode.appendChild($(item).get(0));
    }
  }

  $(item).click();
}