<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>拖拽测试</title>

    <link rel="stylesheet" href="/static/resource/css/jquery-ui.css">
    <script src="/static/js/common/common.js" type="text/javascript"></script>
    <script src="/static/resource/js/jquery-2.1.4.min.js"></script>
    <script src="/static/resource/js/jquery-ui.min.js"></script>
    <script src="/static/resource/js/bootstrap.min.js"></script>
    <!-- 删除时确认窗口 -->
    <script src="/static/ace/js/bootbox.js"></script>
    <script src="/static/resource/js/draggable.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/js/dict/pinyin_dict_notone.js"></script>
    <script type="text/javascript" src="/static/js/dict/pinyinUtil.js"></script>

    <%--bootbox.alert的格式需要ace的bootstrap,和datatimepicker的css冲突--%>
    <link rel="stylesheet" href="/static/ace/css/bootstrap.css" />
    <%--<link href="/static/datetimepicker/css/bootstrap.css" rel="stylesheet" media="screen">--%>
    <link href="/static/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" href="/static/formeo/css/demo.css">
    <link rel="stylesheet" href="/static/formeo/css/form-builder.min.css">
    <link rel="stylesheet" href="/static/formeo/css/form-render.min.css">

  <style type="text/css">
	.cut {
		float:left;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space: nowrap;
	}
    .panel-left{
        width:30%;
    }
    .panel-middle{
        width:50%;
    }
    .panel-right{
        width:20%;
        min-width:150px;
    }
	.middle{
		min-height:500px;
		max-height:500px;
        min-width:450px;
		border:1px solid #cccccc;
        border-radius: 5px;
		float: left;
		margin-left:2px;
		padding:0 2px;
		position:relative;
		overflow: auto;
	}
	#ul-right{
		min-height:400px;
		max-height:400px;
        min-width:150px;
		float: left;
		border:1px solid #cccccc;
        border-radius: 5px;
		margin-left:2px;
		padding:0 2px;
		position:relative;
		overflow: auto;
	}
	#rightBottom{
		min-height:50px;
		max-height:50px;
		float: top;
		margin-left:5px;
		padding:0 2px;
		position:relative;
		overflow: auto;
	}
	.theGray{
	    background:#ddd!important;
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
		background: #f1f8fd;
		height:30px;
		width:75px;
		border:1px solid #98c8ff;
		margin: 2px 0;
		font: 10px/25px "微软雅黑";
		text-align: center;
		border-radius: 2px;
		cursor: pointer;
		list-style-type:none;
	}
	.liClass-one-col {
        width:95%;
        background: #f1f8fd;
        border:1px solid #cccccc;
        border-radius: 2px;
        cursor: pointer;
        float:left;
        vertical-align: top;
        margin-top:2%;
        margin-left:2%;
        margin-right:2%;
        list-style:none;
        list-style-type:none;
    }
    .liClass-two-col {
        width:45%;
        background: #f1f8fd;
        border:1px solid #cccccc;
        border-radius: 2px;
        cursor: pointer;
        float:left;
        vertical-align: top;
        margin-top:2%;
        margin-left:2%;
        margin-right:2%;
        list-style:none;
        list-style-type:none;
    }
    .liClass-new {
        color:#bf0711;
    }
    .float-item-left {
        float:left;
        list-style:none;
        list-style-type:none;
    }
    .float-item-right {
        float:right;
        list-style:none;
        list-style-type:none;
    }

    .as-modal-dialog {
        position:absolute;
        top:50% !important;
        left:5% !important;
        transform: translate(0, -50%) !important;
        -ms-transform: translate(0, -50%) !important;
        -webkit-transform: translate(0, -50%) !important;
        margin:auto 5%;
        background: #f3f3f3;
        width:80%;
        height:80%;
    }
    .as-modal-content {
        min-height:100%;
        position:absolute;
        top:0;
        bottom:0;
        left:0;
        right:0;
    }
    .as-modal-body {
        position:absolute;
        top:45px; /** height of header **/
        bottom:45px;  /** height of footer **/
        left:0;
        right:0;
        overflow-y:auto;
    }
    .as-modal-footer {
        position:absolute;
        bottom:0;
        left:0;
        right:0;
    }
    .tr-hide {
        display:none;
    }
	#saveDiv{
    position:relative;
    top:15px;
    left:20px;
	}
  .component {width:80px;height:25px;}
  .pull-right {  float: right !important;  /*向右浮动*/ }
  </style>
  <script>
  $(function() {

      if (typeof top.hangge === "function") {$(top.hangge());}
      $("input[name='radio-input']").click(function() {
          var selected = $(this).val();
          if (selected == 'radio-one') {
              $(".liClass-indexes").removeClass('liClass-two-col');
              $(".liClass-indexes").addClass('liClass-one-col');
          } else if (selected == 'radio-two') {
              $(".liClass-indexes").removeClass('liClass-one-col');
              $(".liClass-indexes").addClass('liClass-two-col');
          }
      });

        //可拖拽组件初始化
        $('.cpList li').each(function(){
            $(this).attr("draggable","true");
            $(this).on("dragstart", function(ev) {//开始拖拽
                var dt = ev.originalEvent.dataTransfer;
                var sourceOb = $(ev.target).closest("li");
                dt.setData('itemType', $(sourceOb).attr("id"));         // 将拖拽组件ID传入
                dt.setData('dataType', $(sourceOb).attr("dataType"));   // 数据类型
                dt.setData('fieldText', $(sourceOb).text());            // 将拖拽组件文本传入
            });
        });
        //拖拽组件
        Draggable({destId:".tab",dragTag:"li"});

        /*  $("#middle_ul").on("mouseenter", ".liClass", function () {
            var ele=$('<span>X</span>');
            ele.css({position:"relative",left:"35px",top:"-17px"});
             $(this).append(ele);
         }).on("mouseleave", ".liClass", function () {
             $("span").remove();
         }).on("click", "span",function () { */
         $("#middle_ul").on("click",function (ev) {
             var selectedLi = $(ev.target).closest("li");
             if (ev.target.type == 'remove') {
                 if (bootbox.confirm("确定删除吗?", function(result) {
                     if (result) {
                         var indexesId = $(selectedLi).attr("indexesId"); //indexesId
                         // 还没有保存, 直接删除
                         if (typeof(indexesId) == "undefined" || indexesId == '') {
                             selectedLi.remove();
                             resetPropertyPanel();
                         } else {
                             // 已保存数据, 删除数据库记录后再清除li
                             var obj = {
                                 "INDEXES_ID": indexesId
                             };
                             callAjax('/indexes/delete.do', true, obj, function (dt) {
                                 if (dt.responseText == 'success') {
                                     selectedLi.remove();
                                     resetPropertyPanel();
                                     bootbox.alert('删除成功!');
                                 } else {
                                     bootbox.alert(dt.responseText);
                                 }
                             });
                         }
                     }
                 }))
                 return;
             } else if (ev.target.type == 'copy') {
                 // copy current entity
                 copyLiItem($(selectedLi));
                 return;
             }

             if( ev.target.nodeName.toUpperCase() == 'A' ) {
             } else if (ev.target.nodeName.toUpperCase() == 'UL') {
             } else {
                 $('.middle li').each(function(index,li){
                     $(li).removeClass("theGray");
                 });
                 $(selectedLi).addClass("theGray"); //
                 showProperty($(selectedLi));
             }
             //var field=$(this).parent().attr("data-field");

         });
  });

  function saveParamTable(){
      var tableData = getTableData();
      if (tableData === 1) {
          bootbox.alert('编码不能重复');
          return false;
      } else if (tableData === 2) {
          bootbox.alert('编码不能为空');
          $("#codeName").focus();
          return false;
      } else if (tableData === 3) {
          bootbox.alert('字典名称不能为空');
          return false;
      } else if (tableData === 4) {
          bootbox.alert('编码个数超过20个');
          return false;
      }
      //bootbox.alert(JSON.stringify(tableData));
      callAjax('/dict/saveJson.do', true, tableData, function(dt) {
          if (dt.result == 'success') {
              var editMode = $("#popMode").val();
              if (editMode == 'add') {
                  $("#dataSource").append("<option value='" + tableData["DICT_ID"] + "'>" + tableData["NAME"] + "</option>");
                  $("#dataSource").attr("value", tableData["DICT_ID"]);
              } else { // edit
                  var codeName = $("#codeName").val().trim();
                  $("#dataSource").find("option:selected").text(codeName);
              }
              resetTableStatus();
              bootbox.alert('保存成功!');
          }
      });
      return false;
  }

  /*
   * get table infomation
   * 1: value已存在
   * 2: value不能为空
   * 3: codeName不能为空
   */
  var codeArr = []; // 已使用的编码列表
  function getTableData() {
      var key = "";
      var value = "";
      var tableData = [];
      var formData = {};
      var table = $("#para_table");
      var tbody = table.children();
      var trs = tbody.children();
      var editMode = $("#popMode").val();

      var codeName = $("#codeName").val().trim();
      var codeInitalName = $("#codeName").attr("initial_value");

      if (editMode == 'add') {
          formData["DICT_ID"] = guid();
      } else {
          formData["DICT_ID"] = $("#dataSource").val(); // Retrieve from dataSource
      }

      // 检查value是否有重复
      var map = new Map();
      for(var i = 1; i < trs.length; i++) {
          var tds = trs.eq(i).children();
          var initialValue = trs.eq(i).attr("initial_value");
          key = tds.eq(0).text().trim();
          if (key == '' || key == 'undefined') {
              return 2; // 不能为空
          }
          // 值, 如男/女
          if (map.has(key)) {
              return 1; // 已存在
          }
          // (男, nan)
          map.set(key, '');

          var item = (tableData[i - 1] = {});
          item['NAME'] = key;
          item['NAME_EN'] = '';
          item['PARENT_ID'] = formData["DICT_ID"];
          if (editMode == 'edit' && initialValue != '') {
              item['BIANMA'] = trs.eq(i).attr("BIANMA");
              item['CORP_ID'] = trs.eq(i).attr("CORP_ID");
              item['DICT_ID'] = trs.eq(i).attr("DICT_ID");
              item['ORDER_BY'] = i;  //trs.eq(i).attr("ORDER_BY");
              item['STATUS'] = trs.eq(i).attr("STATUS");
          } else { // add
              // 编码
              var code = getNextCode();       // guid();
              if (code === false) {
                  return 4;     // 超出范围
              }
              item['BIANMA'] = code;
              item['DICT_ID'] = guid();
              item['ORDER_BY'] = i;
              item['STATUS'] = "0";
          }
          if (initialValue == '') {
              item['edit_status'] = 'added';         // edit/add
          } else if (initialValue == '-1') {
              item['edit_status'] = 'deleted';       // only in edit mode
          } else if (initialValue == key) {
              item['edit_status'] = 'nochange';      // only in edit mode
          } else if (initialValue != key) {
              item['edit_status'] = 'modified';      // only in edit mode
          }
      }
//      tableData = "["+tableData+"]";
      if (codeName == '' || typeof(codeName) == 'undefined') {
          return 3;
      }
      if (codeName == codeInitalName) {
          formData["edit_status"] = "nochange";
      }

      formData["EDIT_MODE"] = editMode; // add / edit
      formData["NAME"] = codeName;
      formData["NAME_EN"] = pinyinUtil.getPinyin(codeName, '', false, false);
      formData["PARENT_ID"] = "0";
      formData["BIANMA"] = pinyinUtil.getPinyin(codeName, '', false, false);
      formData["ORDER_BY"] = 0;
      formData["codeItems"] = JSON.stringify(tableData);
      return formData;
  }
  // reset status flags, after saving data in add mode
  function resetTableStatus() {
      // 修改编辑模式
      $("#popMode").val('edit');

      var table = $("#para_table");
      var tbody = table.children();
      var trs = tbody.children();

      var codeName = $("#codeName").val().trim();
      $("#codeName").attr("initial_value", codeName);
      //
      for(var i = 1; i < trs.length; i++) {
          var tds = trs.eq(i).children();
          var key = tds.eq(0).text().trim();
          trs.eq(i).attr("initial_value", key);
      }
  }
  function tdClick(tdobject){
      var td = $(tdobject);
      td.attr("onclick", "");
      //1,取出当前td中的文本内容保存起来
      var text = td.text();
      //2,清空td里面的内容
      td.html(""); //也可以用td.empty();
      //3，建立一个文本框，也就是input的元素节点
      var input = $("<input style='width:100%'>");
      //4，设置文本框的值是保存起来的文本内容
      input.attr("value",text);
      input.bind("blur",function(){
          var inputnode = $(this);
          var inputtext = inputnode.val();
          var tdNode = inputnode.parent();
          tdNode.html(inputtext);
          tdNode.click(tdClick);
          td.attr("onclick", "tdClick(this)");
      });
      input.keyup(function(event){
          var myEvent = event||window.event;
          var kcode = myEvent.keyCode;
          if(kcode == 13){
              var inputnode = $(this);
              var inputtext = inputnode.val();
              var tdNode = inputnode.parent();
              tdNode.html(inputtext);
              tdNode.click(tdClick);
          }
      });

      //5，将文本框加入到td中
      td.append(input);
      var t =input.val();
      input.val("").focus().val(t);

      //6,清除点击事件
      td.unbind("click");
  }
  var row = 0;
  function addDictRow(BIANMA, CORP_ID, DICT_ID, NAME, NAME_EN, ORDER_BY, PARENT_ID, STATUS) {
      if (BIANMA) {
          codeArr.push(BIANMA);
      }
      if (STATUS != '0') {
          return;
      }
      row ++;
      var table = $("#para_table");
      var tr= $("<tr class='' initial_value='" + NAME + "'>" +
              "<td style='text-align: center;' onclick='tdClick(this)'>" + NAME + "</td>" +
              "<td align='center' onclick='deleteTr(this)'><button type='button' class='btn btn-xs btn-link' >"+"删除"+"</button></td></tr>");
      tr.attr("BIANMA", BIANMA);
      tr.attr("CORP_ID", CORP_ID);
      tr.attr("DICT_ID", DICT_ID);
      tr.attr("NAME", NAME);
      tr.attr("NAME_EN", NAME_EN);
      tr.attr("ORDER_BY", ORDER_BY);
      tr.attr("PARENT_ID", PARENT_ID);
      tr.attr("STATUS", STATUS);
      table.append(tr);
  }

  function deleteTr(tdobject){
      row --;
      var td = $(tdobject);
      var editMode = $("#popMode").val();
      if (editMode == 'add') {
          td.parents("tr").remove();
      } else {
          // 只隐藏,不物理删除
          td.parents("tr").attr("initial_value", "-1");     // -1, deleted
          td.parents("tr").attr("STATUS", "1");             // 1, deleted
          td.parents("tr").addClass("tr-hide");
      }
  }
  </script>
</head>
<body>
    <div id="myModal" class="modal fade" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="as-modal-dialog vertical-align-center modal-dialog">
            <input type="hidden" id="popMode"  value=""/>
			<div class="as-modal-content modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">字典数据</h4>
				</div>

				<div class="as-modal-body modal-body">
                    <div style="height:26px;margin: 5px;">
                        字典名称(如,性别):
                          <input id="codeName" value="" initial_value="" style="border-top: 1px; border-left: 1px;border-right: 1px;"/>

                        <div id="addtrdiv" style="margin-top:-5px; width: 30%; float: right;">
                            <button type="button" class="btn btn-xs btn-link" onclick="addDictRow('','','','','','','','0')">添加</button>
                        </div>
                    </div>
					<table id="para_table" class="table table-bordered">
                        <thead>
                        <tr>
                            <th style="text-align:center" width="80%">编码(如,男/女)</th>
                            <th style="text-align:center" width="20%">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%--动态添加行--%>
                        <tr>
                            <td style="text-align:center; " onclick="tdClick(this)"></td>
                            <td style="text-align:center; " onclick="deleteTr(this)"><button type="button" class="btn btn-xs btn-link">删除</button></td>
                        </tr>
                        </tbody>
                    </table>
				</div>
				<div class="as-modal-footer modal-footer">
                    <button id="save_label" type="button" class="btn btn-primary" onclick="return saveParamTable(this);">保存</button>
                    <button id="close_label" type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
				</div>
			</div>
        </div>
	</div>
    <div class="container">
        <div class="row">
            <div class="col-xs-3 panel-left">
                <div ><h3>控件面板</h3></div>
                <div class="form-wrap form-builder">
                <ul id="left1" class="cpList frmb-control sort-enabled ui-sortable">
                    <li id="left_wb" itemType="left_wb" dataType="31de79884df949b4bc8c54946ef540ef" class="icon-text input-control ui-sortable-handle"><span>单行文本</span></li>
                    <li id="left_dh" itemType="left_dh" dataType="31de79884df949b4bc8c54946ef540ef" class="icon-textarea input-control ui-sortable-handle"><span>多行文本</span></li>
                    <li id="left_zs" itemType="left_zs" dataType="d2fb286ac25f469a981a1db968799344" class="icon-number input-control ui-sortable-handle">整数</li>
                    <li id="left_xs" itemType="left_xs" dataType="e02399f99f254975bf5516f141bad8e6" class="icon-number input-control ui-sortable-handle">浮点数</li>
                    <li id="left_fx" itemType="left_fx" dataType="31de79884df949b4bc8c54946ef540ee" class="icon-checkbox-group input-control ui-sortable-handle">复选框</li>
                    <li id="left_dx" itemType="left_dx" dataType="5d8a580c34fc45d8bde9bab467284067" class="icon-radio-group input-control ui-sortable-handle">单选框</li>
                    <li id="left_xl" itemType="left_xl" dataType="5d8a580c34fc45d8bde9bab467284067" class="icon-select input-control ui-sortable-handle">下拉框</li>
                    <li id="left_rq" itemType="left_rq" dataType="64867ea408d84da1a5d8162b02af04ba" class="icon-date input-control ui-sortable-handle">日期</li>
                    <%--<li id="left_sj" itemType="left_sj" dataType="3cab25ba75434848807adc0ff4594ccc" class="icon-date input-control ui-sortable-handle">时间</li>--%>
                    <li id="left_rqsj" itemType="left_rqsj" dataType="369962cda2384b6f8dd65f046df8109a" class="icon-date input-control ui-sortable-handle">日期时间</li>
                    <li id="left_tp" itemType="left_tp" dataType="1871c3254b414ec0822ed40b45256cce" class="icon-file input-control ui-sortable-handle">图片</li>
                    <li id="left_pdf"  itemType="left_pdf" dataType="ea5c18254ff1476ab9e8d6acec878963" class="icon-file input-control ui-sortable-handle">PDF</li>
                    <li id="left_office"  itemType="left_office" dataType="3dad472def0e4bec841ee214cbeacc47" class="icon-file input-control ui-sortable-handle">office文件</li>
                    <li id="left_sp" itemType="left_sp" dataType="4a4cad441c78407d8b8606b4a331aced" class="icon-file input-control ui-sortable-handle">视频</li>
                    <li id="left_qt" itemType="left_qt" dataType="711f1e52b6af41f782f5d680b166d521" class="icon-file input-control ui-sortable-handle">其他</li>
                </ul>
                </div>
            </div>

            <div id="myZhibiao" class="col-xs-6 panel-middle">
                <div style="display:inline-block;">
                    <label style=""><h3>表单元素</h3></label>
                    <span style="">
                        <input type="radio" name="radio-input" value="radio-one">一列</input>
                        <input type="radio" name="radio-input" value="radio-two" checked="checked">二列</input>
                    </span>
                </div>
                <div class="form-wrap form-builder">
                    <div class="stage-wrap pull-left">
                    <ul id="middle_ul" class="tab middle frmb-indexes ui-sortable">
                        <c:forEach items="${varList}" var="var" varStatus="vs">
                            <%--"text”, ”textarea”, ”select”,  ”radio-group”, ”number”, ”checkbox-group”, ”asfile”, ”date”, ”datetime”--%>
                            <%--class="text-field form-field" type="text"--%>
                            <li id="${var.INDEXES_ID}" indexesId="${var.INDEXES_ID}"
                                controlType="${var.CONTROL_TYPE}" dataType="${var.DATA_TYPE}" itemType="${var.ITEM_TYPE}"
                                class="${var.CONTROL_TYPE}-field form-field liClass-two-col liClass-indexes" titleValue="${var.COLUMN_NAME_CN}"
                                subType="${var.CONTROL_SUB_TYPE}" len="${var.LENG}" presision="${var.DATA_PRECISION}"
                                autoWrite="${var.INITIAL_TYPE}" initValue="${var.INITIAL_DATA}" rule="${var.VALIDATE_RULE}"
                                baomi="${var.RSV_STR2}" dataSource="${var.DATA_SOURCE}" columnNameEn="${var.COLUMN_NAME_EN}">
                                <label class="field-label cut float-item-left" title="${var.COLUMN_NAME_CN}">${var.COLUMN_NAME_CN}</label>
                                <span class="required-asterisk float-item-left"> *</span>
                                <div class="field-actions float-item-right">
                                    <a type="remove" class="del-button icon-cancel delete-confirm"></a>
                                    <a type="copy" class="copy-button icon-copy"></a>
                                </div>
                                <div class="prev-holder">
                                    <div class="fb-${var.CONTROL_TYPE}">
                                        <c:choose>
                                            <c:when test="${var.CONTROL_TYPE == 'checkbox-group'}">
                                                <div class="${var.CONTROL_TYPE}" style="height: 25px;clear:both;">
                                                    <div class="checkbox">
                                                        <input name="xxx" value="option-1" type="checkbox" checked="checked" />
                                                        <label>Option-1</label>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:when test="${var.CONTROL_TYPE == 'select'}">
                                                <select class="form-control" style="height: 25px;clear:both;">
                                                    <option selected="true" value="option-1">选项1</option>
                                                    <option value="option-2">选项2</option>
                                                </select>
                                            </c:when>
                                            <c:when test="${var.CONTROL_TYPE == 'radio-group'}">
                                                <div class="${var.CONTROL_TYPE}" style="height: 25px;clear:both;">
                                                    <div class="radio-inline">
                                                        <input name="xxx" value="option-1" type="radio" checked="checked" />
                                                        <label>选项1</label>
                                                    </div>
                                                    <div class="radio-inline">
                                                        <input name="xxx" value="option-2" type="radio" checked="checked" />
                                                        <label>选项2</label>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:when test="${var.CONTROL_TYPE == 'asfile'}">
                                                <div class="asfile" style="height: 25px;clear:both;">
                                                    <button type="button" class="btn-file-up btn-info">上传</button>
                                                    <button type="button" class="btn-file-dl btn-info">下载</button>
                                                    <button type="button" class="btn-file-pre btn-info">预览</button>
                                                </div>
                                            </c:when>
                                            <c:when test="${var.CONTROL_TYPE == 'datetime'}">
                                                <div class="input-group datetime form_datetime col-md-5" style="height: 25px;width:100%;clear:both;">
                                                    <input class="" style="height:20px;width:100%;" type="text" value="yyyy/mm/dd hh:mm:ss" readonly />
                                                 </div>
                                            </c:when>
                                            <c:otherwise>
                                                <input name="xxxx" type="${var.CONTROL_TYPE}" class="form-control" style="height: 25px;" />
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                    </div>
                </div>
            </div>
            <div class="col-xs-3 panel-right" >
                <div><h3>属性面板</h3></div>
                <ul id="ul-right">
                    <input type="hidden" id="indexesId"  value=""/>
                    <li class="liClass"><label id="title" >标题*</label></li>
                    <li class="liClass"><input id="titleValue" class="propertyInput" type="text" /></li>
                    <li class="liClass"><label id="itemType">类型*</label></li>
                    <li class="liClass">
                        <select id="itemTypeValue" class="propertyInput" disabled>
                          <option value="left_wb">单行文本</option>
                          <option value="left_dh">多行文本</option>
                          <option value="left_zs">整数</option>
                          <option value="left_xs">浮点数</option>
                          <option value="left_fx">复选框</option>
                          <option value="left_dx">单选框</option>
                          <option value="left_xl">下拉框</option>
                          <option value="left_rq">日期</option>
                          <%--<option value="left_sj">时间</option>--%>
                          <option value="left_rqsj">日期时间</option>
                          <option value="left_tp">图片</option>
                          <option value="left_pdf">pdf</option>
                          <option value="left_office">office文件</option>
                          <option value="left_sp">视频</option>
                          <option value="left_qt">其他</option>
                        </select>
                    </li>

                    <li class="liClass"><label id="subTypeLabel">显示类型</label></li>
                    <li class="liClass">
                        <select id="subType"  class="propertyInput">
                            <option value="" selected>无</option>
                            <option value="text" selected>文本</option>
                            <option value="text" selected>密码</option>
                        </select>
                    </li>

                    <li class="liClass" style="display:none"><label id="dataSourceLabel">数据来源</label></li>
                    <li class="liClass" style="display:none">
                      <select id="dataSource" style="float:left;width:70%;height:25px;">
                          <option value="-1" selected>无</option>
                          <c:forEach items="${sourcelist}" var="item">
                                <option value="${item.DICT_ID}">${item.NAME}</option>
                          </c:forEach>
                      </select>
                        <button type="button" onclick="popAdd();" style="float:left;width:10px;height:25px;padding: 0px 0px 0px;" title="新增字典">+</button>
                        <button type="button" onclick="popEdit();" style="float:left;width:10px;height:25px;padding: 0px 0px 0px;" title="编辑当前字典">#</button>
                    </li>
                    <li class="liClass"><label id="len">长度</label></li>
                    <li class="liClass"><input id="lenValue" class="propertyInput" type=text value='' /></li>
                    <li class="liClass"><label id="presision" >小数位</label></li>
                    <li class="liClass"><input id="presisionValue" class="propertyInput" type=text value='' /></li>
                    <li class="liClass"><label id="autoWrite">自动填写</label></li>
                    <li class="liClass">
                         <c:forEach items="${dataTypeList}" var="item">
                            <input type="radio"  name="autoWrite" value="${item.DICTIONARIES_ID }" />
                                    <label>${item.NAME }</label>
                         </c:forEach>

                    </li>
                    <li class="liClass"><label id="init">初始值</label></li>
                    <li class="liClass"><input id="initValue"  class="propertyInput" type=text value='' /></li>
                    <li class="liClass"><label id="rule">校验规则</label></li>
                    <li class="liClass">
                        <select id="ruleValue"  class="propertyInput">
                          <option value="" selected>无</option>
                         <c:forEach items="${ruleList}" var="item">
                            <option value="${item.CELLRULE_ID }">${item.NAME }</option>
                        </c:forEach>
                        </select>
                    </li>
                    <li class="liClass"><label id="baomi">保密*</label></li>
                    <li class="liClass">
                         <input name="baomi" type="radio" value="1" >是
                          <input name="baomi" type="radio" value="0" checked="checked"  >否
                    </li>
                </ul>
                <ul id="rightBottom">
                     <li onclick="saveProperty();" class="liClass"><label>保存</label></li>
                </ul>
                <input type="hidden" id="dataType" value="" />
                <input type="hidden" id="controlType"  value=""/>
            </div>
            <%--<div class="row" >--%>
                <%--<button type="button" onclick="save()" >保存实体</button>--%>
            <%--</div>--%>
        </div>
    </div>
</body>
<script>
function saveProperty() {
    var selectLi =  $("#middle_ul .theGray")[0];
    if (selectLi == '' || selectLi == null || typeof(selectLi) == 'undefined') {
        bootbox.alert('请选中实体.');
        return;
    }
    var INDEXES_ID = $("#indexesId").val();         // ID
    var COLUMN_NAME_CN = $("#titleValue").val().trim();    // 标题 中文
    if (COLUMN_NAME_CN == '' || typeof(COLUMN_NAME_CN) == 'undefined') {
        $("#titleValue").focus();
        bootbox.alert("标题不能为空");
        return false;
    }
    var COLUMN_NAME_EN = pinyinUtil.getPinyin(COLUMN_NAME_CN, '', false, false); // 标题 英文
    var DATA_TYPE = $("#dataType").val();           // 类型,left_wb需要转换为后台支持id
    var CONTROL_TYPE = $("#controlType").val() ;    // 显示类型
    var CONTROL_SUB_TYPE = $("#subType").val() ;    // 显示子类型
    var LENG = $("#lenValue").val();                // 长度
    var DATA_PRECISION = $("#presisionValue").val(); // 小数位
    var INITIAL_TYPE =  $("input[name='autoWrite']:checked").val() ; // 自动填写
    var INITIAL_DATA = $("#initValue").val();       // 初始值
    var VALIDATE_RULE = $("#ruleValue").val();      // 验证
    var DATA_SOURCE = $("#dataSource").val();       // 数据来源
    var RSV_STR2 = $("input[name='baomi']:checked").val() ; //保密
    var changedFlag = 'added';

    if (INDEXES_ID == '' || typeof(INDEXES_ID) == 'undefined') {
        INDEXES_ID = guid();
    } else {
        changedFlag = 'modified';
        // Preserve columns' english name in case of used.
        COLUMN_NAME_EN = $(selectLi).attr("columnNameEn");
    }
    // dataType, controlType, itemType已存在,而且固定,没必要修改
    $(selectLi).attr("id", INDEXES_ID);
    $(selectLi).attr("indexesId", INDEXES_ID);
    $(selectLi).attr("titleValue", COLUMN_NAME_CN);
    $(selectLi).attr("columnNameEn", COLUMN_NAME_EN);
    $(selectLi).attr("subType", CONTROL_SUB_TYPE);
    $(selectLi).attr("len", LENG);
    $(selectLi).attr("presision", DATA_PRECISION);
    $(selectLi).attr("autoWrite", INITIAL_TYPE);
    $(selectLi).attr("initValue", INITIAL_DATA);
    $(selectLi).attr("rule", VALIDATE_RULE);
    $(selectLi).attr("baomi", RSV_STR2);
    $(selectLi).attr("dataSource", DATA_SOURCE);
    $(selectLi).attr("changedFlag", changedFlag);
    $("label", $(selectLi)).text(COLUMN_NAME_CN);
    $("label", $(selectLi)).attr("title", COLUMN_NAME_CN);

    var nodeJson = {"COLUMN_NAME_CN":COLUMN_NAME_CN,
        "COLUMN_NAME_EN":COLUMN_NAME_EN,
        "INDEXES_ID":INDEXES_ID,
        "changedFlag":changedFlag,
        "DATA_TYPE":DATA_TYPE,
        "CONTROL_TYPE":CONTROL_TYPE,
        "CONTROL_SUB_TYPE":CONTROL_SUB_TYPE,
        "LENG":LENG,
        "DATA_PRECISION":DATA_PRECISION,
        "INITIAL_TYPE":INITIAL_TYPE,
        "INITIAL_DATA":INITIAL_DATA,
        "DATA_SOURCE":DATA_SOURCE,
        "RSV_STR2":RSV_STR2,
        "VALIDATE_RULE":VALIDATE_RULE};
    callAjax('/indexes/saveDrapdrop.do', true, nodeJson, function(dt) {
        if (dt.success == true) {
            $("#indexesId").val(INDEXES_ID);
            $(selectLi).removeClass("liClass-new");
            bootbox.alert('保存成功!');
        } else {
            bootbox.alert(dt.msg);
        }
    });
}

function changeCheck(element) {
	if ($(element).is(':checked')) {
		$(element).attr("checked","checked");
	} else {
		$(element).removeAttr("checked");
	}
}
function save() {

    var retStr = '{"delFields":"' + delIds +'","existFields":[';
    $("#middle_ul > li").each(function(i) {

        var INDEXES_ID = $(this).attr("indexesId"); // 指标名
        var COLUMN_NAME_CN = $(this).attr("titleValue"); // 指标名
        var DATA_TYPE = $(this).attr("dataType");        // 指标类型(id...)
        var CONTROL_TYPE = $(this).attr("controlType");   // text / check-group
        var CONTROL_SUB_TYPE = $(this).attr("subType");   // textfield  ==> text / password / email / color / tel
        var LENG = $(this).attr("len");   // 指标长度
        var DATA_PRECISION = $(this).attr("presision");   // 小数位
        var INITIAL_TYPE = $(this).attr("autoWrite");   // 是否有初始值 1-是 2-否
        var INITIAL_DATA = $(this).attr("initValue");   // 指标类型
        var RSV_STR2 = $(this).attr("baomi");   // 指标类型
        var VALIDATE_RULE = $(this).attr("rule");   // 指标类型
        var changedFlag = $(this).attr("changedFlag");  // 是否有修改
        var nodeStr = '{"COLUMN_NAME_CN":"' + COLUMN_NAME_CN + '"'
            + ',' + '"INDEXES_ID":"' + INDEXES_ID+ '"'
            + ',' + '"changedFlag":"' + changedFlag+ '"'
            + ',' + '"DATA_TYPE":"' + DATA_TYPE+ '"'
            + ',' + '"CONTROL_TYPE":"' + CONTROL_TYPE+ '"'
            + ',' + '"CONTROL_SUB_TYPE":"' + CONTROL_SUB_TYPE+ '"'
            + ',' + '"LENG":"' + LENG+ '"'
            + ',' + '"DATA_PRECISION":"' + DATA_PRECISION+ '"'
            + ',' + '"INITIAL_TYPE":"' + INITIAL_TYPE+ '"'
            + ',' + '"INITIAL_DATA":"' + INITIAL_DATA+ '"'
            + ',' + '"RSV_STR2":"' + RSV_STR2+ '"'
            + ',' + '"VALIDATE_RULE":"' + VALIDATE_RULE+ '"'+ '}';
        if($("#middle_ul > li").length > (i+1) ) {
          nodeStr += ','
        }
        retStr += nodeStr;
 	});
    retStr += ']}';
    bootbox.alert(retStr);
    $.ajax({
        type: "POST",
        url: '/indexes/saveNew',
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
    $('#ul-right > li').each(function(index,li){
        $(li).show();
    });
    // 根据控件类型,设置subtype
    var controlType= $(selectLi).attr("controlType");
    resetSubtype(controlType);

    var id = ($(selectLi).attr("indexesId")) ? $(selectLi).attr("indexesId") : '';
	var title = ($(selectLi).attr("titleValue")) ? $(selectLi).attr("titleValue") : '';
	var itemType = $(selectLi).attr("itemType");
    var dataType = $(selectLi).attr("dataType");
	var subType = setDefaultSubtypeValue(controlType, $(selectLi).attr("subType"));
	var len = setDefaultLenValue(controlType, itemType, $(selectLi).attr("len"));
	var presision = setDefaultPresisionValue(controlType, itemType, $(selectLi).attr("presision"));
	var autoWrite = ($(selectLi).attr("autoWrite")) ? $(selectLi).attr("autoWrite") : '51383c3caf8444108da611ce9a7b34c8';
	var initValue = setInitValue(controlType, itemType, $(selectLi).attr("initValue"));
	var baomi = ($(selectLi).attr("baomi")) ? $(selectLi).attr("baomi") : '0';
	var rule = $(selectLi).attr("rule");
    var dataSource = ($(selectLi).attr("dataSource")) ? $(selectLi).attr("dataSource") : '-1';

    $("#indexesId").val(id);
	$("#titleValue").val(title); // 标题
    $("#titleValue").attr('placeholder', "输入标题");
	$("#itemTypeValue").val(itemType);  // 类型, left_wb
    $("#dataType").val(dataType);       // 数据类型
    $("#controlType").val(controlType); // 控件类型, text,check-group...
    $("#subType").val(subType);
	$("#lenValue").val(len); // 长度
	$("#presisionValue").val(presision); // 小数位
	if (autoWrite == '51383c3caf8444108da611ce9a7b34c8') {
        $("input[name='autoWrite']").eq(1).removeAttr('checked');
		$("input[name='autoWrite']").eq(0).attr('checked', 'checked');
	} else {
        $("input[name='autoWrite']").eq(0).removeAttr('checked');
		$("input[name='autoWrite']").eq(1).attr('checked', 'checked');
	}
	$("#initValue").val(initValue); // 初始值
	if (baomi == '1') {
        $("input[name='baomi']").eq(1).removeAttr('checked');
		$("input[name='baomi']").eq(0).attr('checked', 'checked'); //是否加密
	} else {
        $("input[name='baomi']").eq(0).removeAttr('checked');
		$("input[name='baomi']").eq(1).attr('checked', 'checked'); //是否加密
	}

	$("#ruleValue").val(rule); // 验证
    $("#dataSource").val(dataSource);   // 来源

	// 文本
	if (itemType=='left_wb' || itemType=='left_dh'){
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("#dataSourceLabel").parent().hide();
		$("#dataSource").parent().hide();

	// 整数
	}else if (itemType=='left_zs'){
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("#dataSourceLabel").parent().hide();
		$("#dataSource").parent().hide();
	// 小数
	} else if (itemType=='left_xs') {
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
		$("#dataSourceLabel").parent().hide();
		$("#dataSource").parent().hide();
	// 复选
	} else if (itemType=='left_fx') {
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
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

	// 单选/下拉
	} else if (itemType=='left_dx' || itemType=='left_xl') {
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
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
	} else if (itemType=='left_rq' || itemType=='left_sj' || itemType=='left_rqsj') {
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
		$("#len").parent().hide();
		$("#lenValue").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#dataSourceLabel").parent().hide();
		$("#dataSource").parent().hide();
	// 图片
	} else if (itemType=='left_tp') {
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#dataSourceLabel").parent().hide();
		$("#dataSource").parent().hide();
	// pdf
	} else if (itemType=='left_pdf') {
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#dataSourceLabel").parent().hide();
		$("#dataSource").parent().hide();
	// office
	}  else if (itemType=='left_office') {
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#dataSourceLabel").parent().hide();
		$("#dataSource").parent().hide();
	// 视频
	} else if (itemType=='left_sp') {
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#dataSourceLabel").parent().hide();
		$("#dataSource").parent().hide();
	// 其他
	} else if (itemType=='left_qt') {
		$("#subType").parent().hide();
        $("#subTypeLabel").parent().hide();
		$("#presisionValue").parent().hide();
		$("#presision").parent().hide();
		$("input[name='autoWrite']").parent().hide();
		$("#autoWrite").parent().hide();
		$("#initValue").parent().hide();
		$("#init").parent().hide();
		$("#rule").parent().hide();
		$("#ruleValue").parent().hide();
		$("#dataSourceLabel").parent().hide();
		$("#dataSource").parent().hide();
	}
}

/**
 * 删除li后, 重置属性面板
 */
function resetPropertyPanel() {
    $("#indexesId").val('');
    $("#titleValue").val(''); // 标题
    $("#titleValue").attr('placeholder', "");
    $("#itemTypeValue").val('');  // 类型, left_wb
    $("#dataType").val('');       // 数据类型
    $("#controlType").val(''); // 控件类型, text,check-group...
    $("#subType").val('');
    $("#lenValue").val(''); // 长度
    $("#presisionValue").val(''); // 小数位
    $("#initValue").val(''); // 初始值
    $("#ruleValue").val(''); // 验证
    $("#dataSource").val('');   // 来源
}

/**
 * 动态设置子类型
 * text       ==> text / password / email / color / tel  / datetime-local /
 * textarea   ==> textarea / tinymce / quill
 * select     ==> droplist/popup
 * file       ==> BTN_UDP//BTN_UD
 * @param controlType
 */
function resetSubtype(controlType) {
    $("#subType").empty();
    if (controlType == 'text') {
        $("#subType").append("<option value='text'>文本</option>");
        $("#subType").append("<option value='password'>密码</option>");
        $("#subType").append("<option value='email'>电子邮件</option>");
        $("#subType").append("<option value='color'>颜色</option>");
        $("#subType").append("<option value='tel'>电话号码</option>");
    } else if (controlType == 'textarea') {
        $("#subType").append("<option value='textarea'>文本</option>");
        $("#subType").append("<option value='tinymce'>富文本TinyMCE</option>");
        $("#subType").append("<option value='quill'>富文本quill</option>");
    } else if (controlType == 'select') {
        $("#subType").append("<option value='droplist'>下拉列表</option>");
        $("#subType").append("<option value='popup'>弹出框</option>");
    } else if (controlType == 'asfile') {
        $("#subType").append("<option value='BTN_UDP'>上传/下载/预览</option>");
        $("#subType").append("<option value='BTN_UD'>上传/下载</option>");
    }
}

/**
 * 根据控件类型,设置默认子类型
 * 'text','textarea','number','checkbox-group','radio-group','date','time','datetime','asfile'
 * @param controlType
 * @param value 默认值
 */
function setDefaultSubtypeValue(controlType, value) {
    if (value) {
        return value;
    }
    if (controlType == 'text') {
        return 'text';
    } else if (controlType == 'textarea') {
        return 'textarea';
    } else if (controlType == 'number') {
        return '';
    } else if (controlType == 'checkbox-group') {
        return '';
    } else if (controlType == 'radio-group') {
        return '';
    } else if (controlType == 'select') {
        return '';
    } else if (controlType == 'date') {
        return '';
    } else if (controlType == 'time') {
        return '';
    } else if (controlType == 'datetime') {
        return '';
    } else if (controlType == 'asfile') {
        return 'BTN_UDP';
    }
    return '';
}
/**
 * 根据控件类型, 设置长度默认值
 * @param controlType 控件显示类型 text,number...
 * @param itemType    拖拽面板上的类型, 单行,多行,整数,小数...
 * @param value
 * @returns {*}
 */
function setDefaultLenValue(controlType, itemType, value) {
    if (value) {
        return value;
    }
    if (controlType == 'text') {
        return '100';
    } else if (controlType == 'textarea') {
        return '200';
    } else if (controlType == 'number') {
        if (itemType == 'left_zs') {
            return '10';
        } else if (itemType == 'left_xs') {
            return '16';
        }
    } else if (controlType == 'checkbox-group') {
        return '100';
    } else if (controlType == 'radio-group') {
        return '0';
    } else if (controlType == 'select') {
        return '0';
    } else if (controlType == 'date') {
        return '0';
    } else if (controlType == 'time') {
        return '0';
    } else if (controlType == 'datetime') {
        return '0';
    } else if (controlType == 'asfile') {
        return '256';
    }
    return '';
}
/**
 * 根据控件类型,设置精度值
 * @param controlType
 * @param itemType
 * @param value
 * @returns {*}
 */
function setDefaultPresisionValue(controlType, itemType, value) {
    if (value) {
        return value;
    }
    if (controlType == 'text') {
        return '0';
    } else if (controlType == 'textarea') {
        return '0';
    } else if (controlType == 'number') {
        if (itemType == 'left_zs') {
            return '0';
        } else if (itemType == 'left_xs') {
            return '3';
        }
    } else if (controlType == 'checkbox-group') {
        return '0';
    } else if (controlType == 'radio-group') {
        return '0';
    } else if (controlType == 'select') {
        return '0';
    } else if (controlType == 'date') {
        return '0';
    } else if (controlType == 'time') {
        return '0';
    } else if (controlType == 'datetime') {
        return '0';
    } else if (controlType == 'asfile') {
        return '0';
    }
    return '0';
}
function setInitValue(controlType, itemType, value) {
    if (value) {
        return value;
    }
    if (controlType == 'text') {
        return '';
    } else if (controlType == 'textarea') {
        return '';
    } else if (controlType == 'number') {
        if (itemType == 'left_zs') {
            return '0';
        } else if (itemType == 'left_xs') {
            return '0.000';
        }
    } else if (controlType == 'checkbox-group') {
        return '';
    } else if (controlType == 'radio-group') {
        return '';
    } else if (controlType == 'select') {
        return '';
    } else if (controlType == 'date') {
        return '';
    } else if (controlType == 'time') {
        return '';
    } else if (controlType == 'datetime') {
        return '';
    } else if (controlType == 'asfile') {
        return '';
    }
    return '0';
}

function popAdd() {
    $("#popMode").val("add");
    $("#codeName").val("");
    $("#codeName").attr("initial_value", "");
    // Empty rows
    row = 0;
    codeArr = [];       // 重置
//    $("#para_table  tr:not(:first)").empty("");
    $("#para_table tbody").empty("");
	$("#myModal").modal();
}

function popEdit() {
    $("#popMode").val("edit");
    var dataSource = $("#dataSource").val();       // 数据来源
    var dataSourceText = $("#dataSource").find("option:selected").text();
    if (dataSource == "-1") {
        return;
    }
    var obj = {
        "DICT_ID": dataSource
    };
    callAjax('/dict/findItems.do', true, obj, function(dt) {
        if (dt.result == 'success') {
            $("#codeName").val(dataSourceText);
            $("#codeName").attr("initial_value", dataSourceText);
            // Empty rows
            $("#para_table tbody").empty("");
            codeArr = [];       // 重置
            for (var index = 0; index < dt.pd.length; index ++) {
                var item = dt.pd[index];
                // item.BIANMA, item.CORP_ID, item.DICT_ID, item.NAME, item.NAME_EN, item.ORDER_BY, item.PARENT_ID, item.STATUS
                addDictRow(item.BIANMA, item.CORP_ID, item.DICT_ID, item.NAME, item.NAME_EN, item.ORDER_BY, item.PARENT_ID, item.STATUS);
            }
        }
    });
    $("#myModal").modal();
}
function getNextCode() {
    if (codeArr.length > 0) {
        var minNum = 65;  // 'A'
        for (var index = 0; index < codeArr.length; index++) {
            var tmp = codeArr[index].charCodeAt();
            if (tmp > minNum) {
                minNum = tmp;
            }
        }
        minNum ++;
        if (minNum > 85) {
            return false;
        }
        var str = String.fromCharCode(minNum);
        codeArr.push(str);
        return str;
    }
    var str = 'A';
    codeArr.push(str);
    return str;
}

</script>
</html>