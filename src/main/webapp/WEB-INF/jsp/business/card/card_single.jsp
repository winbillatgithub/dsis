﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglib.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>

  <meta charset='utf-8'>
<!-- jsp文件头和头部 -->
  <%@ include file="../../system/index/top.jsp"%>
  <%--<link href="/static/datetimepicker/css/bootstrap.css" rel="stylesheet" media="screen">--%>
  <link href="/static/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet" media="screen">
  <link rel="stylesheet" href="/static/formeo/css/demo.css">
  <link rel="stylesheet" href="/static/formeo/css/form-builder.min.css">
  <link rel="stylesheet" href="/static/formeo/css/form-render.min.css">
</head>
<body>
<div id="myModal" class="modal fade" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog vertical-align-center">
        <input type="hidden" id="popMode"  value=""/>
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">字典数据</h4>
            </div>
            <div class="modal-body">
                字典名称:(如,性别)<input id="codeName" value="" initial_value=""/>
                <div id="addtrdiv" style="margin-top:-5px; width: 13%; float: right;">
                    <button type="button" class="btn btn-xs btn-link" onclick="addDictRow('','','','','','','','0')">添加</button>
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
            <div class="modal-footer">
                <button id="save_label" type="button" class="btn btn-primary" onclick="return saveParamTable(this);">保存</button>
                <button id="close_label" type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
  <div id="fb-root"></div>
  <div class="site-wrap container">
      <input type="hidden" name="dataEditFlag" id="dataEditFlag" value="${dataEditFlag}" />
      <input type="hidden" name="formEditFlag" id="formEditFlag" value="${formEditFlag}" />
      <input type="hidden" name="ajaxUrl" id="ajaxUrl" />
      <input type="hidden" name="TABLES_ID" id="TABLES_ID" value="${pd.TABLES_ID}" />
      <input type="hidden" name="TABLE_NAME_EN" id="TABLE_NAME_EN" value="${pd.TABLE_NAME_EN}" />
      <input type="hidden" name="JSON_DATA" id="JSON_DATA" value="${pd.JSON_DATA}" />
      <input type="hidden" name="USER_DATA" id="USER_DATA" value="" />
      <input type="hidden" name="DATA_ID" id="DATA_ID" value="${pd.DATA_ID}" />
      <input type="hidden" name="ROW_DATA" id="ROW_DATA" value="" />
      <%--弹出选择隐藏项--%>
      <input type="hidden" name="DICT_KEY" id="DICT_KEY" value="" />
      <input type="hidden" name="DICT_VALUE" id="DICT_VALUE" value="" />
    <div id="news-wrap"></div>
    <div style="padding-top: 10px;">
      <label for="TABLE_NAME">表单名称：</label>
      <input type="text" value="${pd.TABLE_NAME}" id="TABLE_NAME" <c:if test="${formEditFlag =='preview'}">readonly="readonly"</c:if> style="height: 32px;margin-right:5px;"/>
      <label for="TABLE_NAME">表单描述：</label>
      <input type="text" value="${pd.REMARK}" id="REMARK" <c:if test="${formEditFlag =='preview'}">readonly='readonly'</c:if> style="height: 32px;margin-right:5px;"/>
        <%--<c:if test="${formEditFlag !='preview'}">--%>
          <button class="btn btn-white btn-info" type="button" onclick="save()" style="display: inline-block;">
            <i class="ace-icon fa fa-check bigger-120 blue"></i>保存
          </button>
          <button class="btn btn-white btn-info" type="button" onclick="clearForm()" style="display: inline-block;">
            <i class="ace-icon fa fa-remove bigger-120 blue"></i>清空
          </button>
        <%--</c:if>--%>
    </div>
    <!-- MAIN CONTENT -->
    <div id="main-content-wrap" class="outer">
      <section id="main_content" class="inner">
        <div id="stage1" class="build-wrap"></div>
        <form class="render-wrap"></form>
        <button id="edit-form" <c:if test="${formEditFlag == 'preview'}">style='display:none'</c:if>>编辑表单</button>
          <%--<div class="action-buttons">--%>
              <%--<h2>Actions</h2>--%>
              <%--<button id="saveForm" type="button" onclick="saveForm()">保存</button>--%>
              <%--<button id="showData" type="button">Show Data</button>--%>
              <%--<button id="clearFields" type="button">Clear All Fields</button>--%>
              <%--<button id="getData" type="button">Get Data</button>--%>
              <%--<button id="getXML" type="button">Get XML Data</button>--%>
              <%--<button id="getJSON" type="button">Get JSON Data</button>--%>
              <%--<button id="getJS" type="button">Get JS Data</button>--%>
              <%--<button id="setData" type="button">Set Data</button>--%>
              <%--<button id="addField" type="button">Add Field</button>--%>
              <%--<button id="removeField" type="button">Remove Field</button>--%>
              <%--<button id="testSubmit" type="submit">Test Submit</button>--%>
              <%--<button id="resetDemo" type="button">Reset Demo</button>--%>
              <%--<h2>i18n</h2>--%>
              <%--<select id="setLanguage">--%>
                  <%--<option value="en-US">English</option>--%>
                  <%--<option value="zh-TW">台語</option>--%>
              <%--</select>--%>
          <%--</div>--%>
      </section>
    </div>
  </div>
  	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="/static/ace/js/bootbox.js"></script>
    <!--引入弹窗组件start-->
    <script type="text/javascript" src="/plugins/attention/zDialog/zDrag.js"></script>
    <script type="text/javascript" src="/plugins/attention/zDialog/zDialog.js"></script>
    <!-- ace scripts -->
	<script src="/static/ace/js/ace/ace.js"></script>
	<!-- 下拉框 -->
	<script src="/static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="/static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="/static/js/jquery.tips.js"></script>

  <!--
  <script src="/static/formeo/js/vendor.min.js"></script>
  <script src="/static/formeo/js/site.min.js"></script>
 -->
    <!-- required by bootstrap-datetimepicker -->
    <script type="text/javascript" src="/static/datetimepicker/js/jquery-1.8.3.min.js" charset="UTF-8"></script>
    <script type="text/javascript" src="/static/datetimepicker/js/bootstrap.js"></script>
    <!-- form-builder -->
    <script type="text/javascript" src="/static/formeo/js/vendor.js"></script>
    <script type="text/javascript" src="/static/formeo/js/form-builder.min.js"></script>
    <script type="text/javascript" src="/static/formeo/js/form-render.min.js"></script>
    <script type="text/javascript" src="/static/formeo/js/jquery.rateyo.min.js"></script>
    <script type="text/javascript" src="/static/datetimepicker/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="/static/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
    <script type="text/javascript" src="/static/formeo/js/demo.js"></script>

    <script type="text/javascript" src="/static/js/dict/pinyin_dict_notone.js"></script>
    <script type="text/javascript" src="/static/js/dict/pinyinUtil.js"></script>

  <script type="text/javascript">
      // 获取实体列表
      var colIndex = 0;
      var columns = [];

      var url = '/card/getListService.do?tm='+new Date().getTime();
      var urlInput = document.getElementById("ajaxUrl");
      $('#ajaxUrl').val(url);
//      if () {
//          document.body.classList.toggle('form-rendered', editing);
//      }
      // html页面加载完成后, form渲染完毕后加载数据
      $(function() {
          console.log("html页面加载完成后");
          var formEditFlag = $("#formEditFlag").val(); // add, edit, preview
          var dataEditFlag = $("#dataEditFlag").val(); // add, edit
          if (formEditFlag == 'preview') {
              if (dataEditFlag == 'edit') {
                  loadIndexes();
                  loadData();
//                  testReadOnly();
              } else if (dataEditFlag == 'add') {
                  loadIndexes();
                  setCtrlProps();
              }
          }
          if (typeof top.hangge === "function") {$(top.hangge());}
      });

      function saveCheck() {
          var checkResult = true;
          var controls = serializeAll();
          var frm = $('.render-wrap');
          // 遍历每一个控件,根据 dataEntityName查找item[0]对应的控件,然后设置数值
          // id[0], name[1], type[2], length[3], precision[4], nullable[5], rule[6], initial_type[7], encrypt[8],
          // subtype[9], subtype_format[10], data_source[11], control_type[12]
          $.each(controls, function () {
              var dataEntityName = $("[name='" + this.name + "']").attr("data-entity-name");
              if (typeof(dataEntityName) == 'undefined') {
                  return true; // continue;
              }
              var items = dataEntityName.split('##');
              var entityKey = items[0]; // key = this.name; // textarea-237498103748
              var $ctrl = $('[name=' + this.name + ']', frm);
              console.log($ctrl.attr("type"));
              switch ($ctrl.attr("type")) {
                  case "text":
                  case "textarea":
                  case "number":
                      var id = $ctrl.attr("id");
                      var required = $ctrl.attr("required");
                      var rule = items[6];
                      var ruleMsg = items[13];
                      var ret = requiredCheck(id, items[1], required);
                      if (ret.valid === false) {
                          checkResult = false;
                          return false;
                      }
                      ret = blurCallback(id, items[1], rule, ruleMsg);
                      if (ret.valid === false) {
                          checkResult = false;
                          return false;
                      }
                      break;
                  case "asfile":
                  case "date":
                  case "time":
                  case "datetime":
                      var id = $ctrl.attr("id");
                      var required = $ctrl.attr("required");
                      var ret = requiredCheck(id, items[1], required);
                      if (ret.valid === false) {
                          checkResult = false;
                          return false;
                      }
                      break;
                  case "checkbox-group":
                  case "radio-group":
                  case "select":
                      var id = $ctrl.attr("id");
                      var required = $ctrl.attr("required");
                      var ret = requiredCheck(id, items[1], required);
                      if (ret.valid === false) {
                          checkResult = false;
                          return false;
                      }
                      break;
              }
          });
          return checkResult;
      }
      function save() {
          var formEditFlag = $("#formEditFlag").val();
          if (formEditFlag == 'preview') {
              return saveData();
          }
          return saveForm();
      }

      function saveForm() {
          // 保存当前表单到数据库
          var formEditFlag = $("#formEditFlag").val();
          if (formEditFlag == '' || typeof(formEditFlag) == 'undefined') {
              // default mode: add
              formEditFlag = 'add';
          }
          var REMARK = $.trim($("#REMARK").val());
          var TABLE_NAME = $.trim($("#TABLE_NAME").val());
          if (TABLE_NAME == '' || typeof(TABLE_NAME) == 'undefined') {
              bootbox.alert('表单名称不能为空!');
              $("#TABLE_NAME").focus();
              return;
          }
          // regular expression 中英数_
          var bRet = regExpCheck(TABLE_NAME, '^[\u4e00-\u9fa5a-zA-Z_]{1}[\u4e00-\u9fa5_a-zA-Z0-9]{0,49}$');
          if (!bRet) {
              bootbox.alert('表单名称只能包含中文, 英数字和下划线, 且不能以数字开头, 长度范围1-50!');
              $("#TABLE_NAME").focus();
              return;
          }
          bRet = regExpCheck(REMARK, '^[\u4e00-\u9fa5_a-zA-Z0-9]{0,100}$');
          if (!bRet) {
              bootbox.alert('表单描述只能包含中文, 英数字和下划线, 最大长度100!');
              $("#REMARK").focus();
              return;
          }
          var guid = my_guid();
          var pinyin = pinyinUtil.getPinyin(TABLE_NAME, '', false, false).substr(0, 50);
          var TABLES_ID = $("#TABLES_ID").val();
          if (TABLES_ID == '' || typeof(TABLES_ID) == 'undefined') {
              TABLES_ID = pinyin + '_' + guid;
          }
          var TABLE_NAME_EN = $("#TABLE_NAME_EN").val();
          if (TABLE_NAME_EN == '' || typeof(TABLE_NAME_EN) == 'undefined') {
              TABLE_NAME_EN = pinyin + '_' + guid;
          }
          var formData = getFormJsonData();
          formData = encodeURIComponent(formData);
          formData = btoa(formData);
          if (formData == '' || typeof(formData) == 'undefined' || formData == 'JTVCJTVE') {
              bootbox.alert('请添加表单元素');
             return;
          }
//          formData = btoa(encodeURIComponent(formData));
          var url = '/card/saveNew.do?tm='+new Date().getTime();
          var param = "{'TABLES_ID':'" + TABLES_ID + "','TABLE_NAME_EN':'" + TABLE_NAME_EN
                  + "','TABLE_NAME':'" + TABLE_NAME + "','REMARK':'" + REMARK + "','formEditFlag':'" + formEditFlag
                  + "','DATA':'" + formData + "'}";

          var obj = eval('(' + param + ')');
          callAjax(url, true, obj, function(dt) {
              if (dt.success == true) {
                  $("#TABLES_ID").val(TABLES_ID);
                  $("#TABLE_NAME_EN").val(TABLE_NAME_EN);
                  $("#formEditFlag").val('edit');   // add, edit, preview
                  if (typeof(dt.warning) != 'undefined') {
                      // refresh the card
                      bootbox.alert(dt.warning);
                  } else {
                      bootbox.alert('保存成功!');
                  }
              } else {
                  if (typeof(dt.msg) == 'undefined' || dt.msg == null) {
                      bootbox.alert('未知异常, 请联系管理员.');
                  } else {
                      bootbox.alert(dt.msg);
                  }
              }
          });
      }

      function clearForm() {

      }

      // Save dynamic data to db
      // dirty:0 无变化  1：新增 2：修改 3：删除
      function saveData() {
          $("#USER_DATA").val('');
          // data-entity-name, REMARK##备注##STRING##2
          // retrieve field value and data-entity-name value
          // send to backend
          // add, edit
          if (saveCheck() == false) {
              return;
          }
          if (typeof top.cwtsyc === "function") {$(top.cwtsyc());}    // 清空错误提示

          var dataEditFlag = 'edit';
          var dataId = $("#DATA_ID").val();
          if (typeof(dataId) == 'undefined' || dataId == '') {
              dataId = my_guid();
              dataEditFlag = 'add';
          }
          var TABLES_ID = $("#TABLES_ID").val();
          var definitions = getColumnsDef();

          var url = '/card/saveData.do?tm='+new Date().getTime();
          url += "&TABLES_ID=" + TABLES_ID;
          url += "&dataEditFlag=" + dataEditFlag;
//          url += "&DATA=" + formData;
          url += "&COLUMNS_DEF=" + definitions;

          var formData = getUserJsonData();
          formData['id'] = dataId;
          formData = JSON.stringify(formData);
          var obj = {};
          obj['DATA'] = formData;
          callAjax(url, true, obj, function(dt) {
              if (dt.success == true) {
                  $("#dataEditFlag").val('edit');
                  $("#DATA_ID").val(dataId);
                  bootbox.alert('保存成功!');
                  if (top.Dialog != null && typeof top.Dialog.close === "function") {
                      // 保存getUserJsonData的数据到list
                      formData = encodeURIComponent(formData);
                      $("#USER_DATA").val(formData);
                      top.Dialog.close();
                  }
              } else {
                  if (typeof(dt.msg) == 'undefined') {
                      bootbox.alert('未知异常, 请联系管理员.');
                  } else {
                      bootbox.alert(dt.msg);
                  }
              }
          });
      }
      function  serializeAll() {
          var frm = $('.render-wrap');
          var a = $(".render-wrap :input").serializeArray();
          var $radio = $('input[type=radio], input[type=checkbox]', frm);
          var temp = {};
          $.each($radio, function () {
              if (!temp.hasOwnProperty(this.name)) {
                  if ($("input[name='" + this.name + "']:checked").length == 0) {
                      temp[this.name] = "";
                      a.push({name: this.name, value: this.value});
                  }
              }
          });
          return a;
      };
      function loadIndexes() {
          var indexesList = ${indexesList};
          for (var i = 0; i < indexesList.length; i++ ) {
              var index = indexesList[i];
              // 多表关联字段，下拉列表显示
              if (index['JOIN_TABLES_ID'] != null && index['JOIN_TABLES_ID'] != '') {
                  var item = (columns[colIndex++] = {});
                  item["join_table_name"] = index["JOIN_TABLES_ID"];
                  item["join_table_column"] = index["JOIN_COLUMNS_NAME_EN"];	// 外联用
                  item["join_columns_id"] = index["JOIN_COLUMNS_ID"];			// 内联用
                  item["join_select_table_column"] = index["JOIN_SELECT_COLUMNS_NAME_EN"];
                  item["master_table_name"] = index["MASTER_TABLES_ID"];		// 默认master_table_name和join_table_name是一致的，SYS_CORP是特例
                  item["master_table_column"] = index["MASTER_COLUMNS_NAME_EN"];
                  item["corp_id"] = index["CORP_ID"];
                  item["join_condition"] = index["JOIN_CONDITION"];
                  // 外联，其他表的字段
                  if (index['JOIN_CONDITION'] == '1') {
                      var url = '/common/getjoinlist.do?tm=' + new Date().getTime();
                      item["url"] = url;
                      // 要求该请求满足一定的sla
//                      var param = "{'tablesId':'" + item["join_table_name"] + "','corpId':'" + item["corp_id"]
//                              + "','joinColumnsId':'" + item["join_table_column"] + "','selectColumnsId':'" + item["join_select_table_column"] + "'}";
                      var param = {
                          'tablesId':item["join_table_name"],
                          'corpId':item["corp_id"],
                          'joinColumnsId':item["join_table_column"],
                          'selectColumnsId':item["join_select_table_column"]
                      };
                      item["param"] = param;
                  } else {
                      // 多表关联的内联字段，不能编辑
                      index['INITIAL_TYPE_NAME'] = 'ZIDONG';
                  }
              }
          }
      }
      // 需要根据getUserJsonData()获取到的json数据, 反向推算
      // https://github.com/kevinchappell/formBuilder/issues/238
      function loadData() {
          // Fixed bug: Request header too large
          // var formData = ${USER_DATA};
          var formData =$("#ROW_DATA").val();
          if (formData == '' || typeof(formData) == 'undefined') {
              formData = '{}';
          }
          formData = JSON.parse(formData);

          if (formData == '' || typeof(formData) == 'undefined' || $.isEmptyObject(formData)) {
              return;
          }
          var controls = serializeAll();
//          var controls = $(".render-wrap :input").serializeArray();
          console.log(controls);
          var frm = $('.render-wrap');
          $.each(formData, function (key, value) {
              // console.log('[name=' + key + ']');
              // 遍历每一个控件,根据 dataEntityName查找item[0]对应的控件,然后设置数值
              $.each(controls, function () {
                  var dataEntityName = $("[name='" + this.name + "']").attr("data-entity-name");
                  if (typeof(dataEntityName) == 'undefined') {
                      return true; // continue;
                  }
                  // id[0], name[1], type[2], length[3], precision[4], nullable[5], rule[6], initial_type[7], encrypt[8],
                  // subtype[9], subtype_format[10], data_source[11], control_type[12]
                  var items = dataEntityName.split('##');
                  var entityKey = items[0]; // key = this.name; // chanpinmingcheng
                  var label = items[1];
                  var initialType = items[7];
                  if (entityKey == key) {
                      var $ctrl = $('[name=' + this.name + ']', frm);
                      console.log($ctrl.attr("type") + ',key=' + entityKey);
                      switch ($ctrl.attr("type")) {
                          case "text":
                          case "hidden":
                          case "textarea":
                              // dtp_datetime-1533559817285
                              if (this.name.substring(0, 4) == 'dtp_') {
                                  $('#form_datetime' + this.name.substring(4)).data('datetimepicker').setDate(new Date(value));
                                  var obj = isOuterJoinField(entityKey);
                                  if (obj['readOnly'] == true) {
                                      $('.input-group-addon', $('#form_datetime' + this.name.substring(4))).hide();
                                      if (obj['outerJoin'] == true) {
                                          setOuterJoinValue(obj['url'], obj['param'], value, $ctrl);
                                          //弹出
                                          popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                                      }
                                  }
                              } else {
                                  var obj = isOuterJoinField(entityKey);
                                  if (obj['readOnly'] == true) {
                                      //只读属性
                                      $ctrl.attr("readonly", "readonly");
                                      if (obj['outerJoin'] == true) {
                                          setOuterJoinValue(obj['url'], obj['param'], value, $ctrl);
                                          //弹出
                                          popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                                      }
                                  } else {
                                      $ctrl.val(value);
                                  }
                              }
                              break;
                          case "date":
                              $ctrl.val(value);
                              var obj = isOuterJoinField(entityKey);
                              if (obj['readOnly'] == true) {
                                  $ctrl.attr("readonly", "readonly");
                                  if (obj['outerJoin'] == true) {
                                      setOuterJoinValue(obj['url'], obj['param'], value, $ctrl);
                                      //弹出
                                      popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                                  }
                              }
                              break;
                          case "radio":
                              $ctrl.each(function () {
                                  if ($(this).attr('value') == value) {
                                      $(this).attr("checked", value);
                                  }
                              });
                              var obj = isOuterJoinField(entityKey);
                              if (obj['readOnly'] == true) {
                                  $ctrl.attr("onclick", "return false");
                                  if (obj['outerJoin'] == true) {
                                      setOuterJoinValue(obj['url'], obj['param'], value, $ctrl);
                                      //弹出
                                      popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                                  }
                              }
                              break;
                          case "checkbox":
                              for (var i = 0; i < value.length; i++) {
                                  $ctrl.each(function () {
                                      if ($(this).attr('value') == value[i]) {
                                          $(this).attr("checked", value[i]);
                                      }
                                  });
                              }
                              var obj = isOuterJoinField(entityKey);
                              if (obj['readOnly'] == true) {
                                  $ctrl.attr("onclick", "return false");
                                  if (obj['outerJoin'] == true) {
                                      setOuterJoinValue(obj['url'], obj['param'], value, $ctrl);
                                      //弹出
                                      popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                                  }
                              }
                              break;
                          case "asfile":
                              // 354820.mp4|2387427|mp4|group1/M00/00/00/CqMY5V0WQ1WABHjrACRt4zK0cpg680.mp4|ba63bbe4a6e24d988eb5bd2672910ca6|null
                              if (value) {
                                  var items = value.split('|');
                                  $ctrl.val(items[0]);
                                  $ctrl.attr("file-value", value);
                                  $ctrl.attr("file-type", items[2]);
                                  $ctrl.attr("file-url", items[3]);
                                  $ctrl.attr("data-key", value);
                              }
                              break;
                          case undefined:
                              $ctrl.val(value);
                              var obj = isOuterJoinField(entityKey);
                              if (obj['readOnly'] == true) {
                                  if ($ctrl.prop("type") == 'select-one') {
                                      $('#' + $ctrl.attr("id") + ' option:not(:selected)').prop('disabled', true);
                                  } else {
                                      $ctrl.attr("readonly", "readonly");
                                  }
                                  if (obj['outerJoin'] == true) {
                                      setOuterJoinValue(obj['url'], obj['param'], value, $ctrl);
                                      //弹出
                                      popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                                  }
                              }
                              break;
                          default:
                              $ctrl.val(value);
                              var obj = isOuterJoinField(entityKey);
                              if (obj['readOnly'] == true) {
                                  $ctrl.attr("readonly", "readonly");
                                  if (obj['outerJoin'] == true) {
                                      setOuterJoinValue(obj['url'], obj['param'], value, $ctrl);
                                      //弹出
                                      popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                                  }
                              }
                      }
                      return false;
                  }
              });
//              $.each() {
//              }

          });
      }
      /**
       * 当preview的add状态, 需要设定相关控件的属性
       */
      function setCtrlProps() {
          var controls = serializeAll();
          var frm = $('.render-wrap');
          $.each(controls, function () {
              var dataEntityName = $("[name='" + this.name + "']").attr("data-entity-name");
              if (typeof(dataEntityName) == 'undefined') {
                  return true; // continue;
              }
              // id[0], name[1], type[2], length[3], precision[4], nullable[5], rule[6], initial_type[7], encrypt[8],
              // subtype[9], subtype_format[10], data_source[11], control_type[12]
              var items = dataEntityName.split('##');
              var entityKey = items[0]; // key = this.name; // chanpinmingcheng
              var label = items[1];
              var initialType = items[7];
              var $ctrl = $('[name=' + this.name + ']', frm);
              console.log($ctrl.attr("type") + ',key=' + entityKey);
              switch ($ctrl.attr("type")) {
                  case "text":
                  case "hidden":
                  case "textarea":
                      // dtp_datetime-1533559817285
                      if (this.name.substring(0, 4) == 'dtp_') {
                          var obj = isOuterJoinField(entityKey);
                          if (obj['readOnly'] == true) {
                              $('.input-group-addon', $('#form_datetime' + this.name.substring(4))).hide();
                              if (obj['outerJoin'] == true) {
                                  //弹出
                                  popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                              }
                          }
                      } else {
                          var obj = isOuterJoinField(entityKey);
                          if (obj['readOnly'] == true) {
                              //只读属性
                              $ctrl.attr("readonly", "readonly");
                              if (obj['outerJoin'] == true) {
                                  //弹出
                                  popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                              }
                          }
                      }
                      break;
                  case "date":
                      var obj = isOuterJoinField(entityKey);
                      if (obj['readOnly'] == true) {
                          $ctrl.attr("readonly", "readonly");
                          if (obj['outerJoin'] == true) {
                              //弹出
                              popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                          }
                      }
                      break;
                  case "radio":
                  case "checkbox":
                      var obj = isOuterJoinField(entityKey);
                      if (obj['readOnly'] == true) {
                          $ctrl.attr("onclick", "return false");
                          if (obj['outerJoin'] == true) {
                              //弹出
                              popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                          }
                      }
                      break;
                  case undefined:
                      var obj = isOuterJoinField(entityKey);
                      if (obj['readOnly'] == true) {
                          if ($ctrl.prop("type") == 'select-one') {
                              $('#' + $ctrl.attr("id") + ' option:not(:selected)').prop('disabled', true);
                          } else {
                              $ctrl.attr("readonly", "readonly");
                          }
                          if (obj['outerJoin'] == true) {
                              //弹出
                              popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                          }
                      }
                      break;
                  default:
                      var obj = isOuterJoinField(entityKey);
                      if (obj['readOnly'] == true) {
                          $ctrl.attr("readonly", "readonly");
                          if (obj['outerJoin'] == true) {
                              //弹出
                              popupOuterJoinWindow($ctrl, initialType, label, obj['url'], obj['param']);
                          }
                      }
              }
          });
      }
      function isOuterJoinField(entityKey) {
          var ret = {};
          ret['readOnly'] = false;
          for (var i = 0; i < columns.length; i++) {
              if (columns[i]["master_table_column"] == entityKey) {
                  ret['readOnly'] = true;
                  ret['url'] = columns[i]["url"];
                  ret['param'] = columns[i]["param"];
                  if (columns[i]["join_condition"] == '1') {
                      ret['outerJoin'] = true;
                  }
                  break;
              }
          }
          return ret;
      }
      function setOuterJoinValue(url, param, val, $ctrl) {
          // 获取关联列的name属性, 用来显示数据
          // in('aaa',bbb','ccc','ddd','eee','fff');
          param['in'] = "'" + val + "'";
          callAjax(url, true, param, function (dt) {
              if (dt.success == true) {
                  var options = eval(dt.data);
                  var key = val;
                  var value = options[val];
                  // 要根据控件类型来设置 winbill 多值的checkbox不支持外联
                  switch ($ctrl.attr("type")) {
                      case "text":
                      case "hidden":
                          //
                          // dtp_datetime-1533559817285
                          if ($ctrl.attr("name").substring(0, 4) == 'dtp_') {
                              $('#form_datetime' + $ctrl.attr("name").substring(4)).data('datetimepicker').setDate(new Date(value));
                          } else {
                              $ctrl.val(value);
                          }
                          break;
                      case "radio":
                          $ctrl.each(function () {
                              if ($(this).attr('value') == value) {
                                  $(this).attr("checked", value);
                              }
                          });
                          break;
                      case "checkbox":
                          bootbox.alert('复选框不支持外联');
                          break;
                      default:
                          $ctrl.val(value);
                  }
                  $ctrl.attr('data-key', key);
                  $ctrl.attr('data-value', value);
              }
          });
      }
      function popupOuterJoinWindow($ctrl, initialType, label, url, param) {
          // 获取关联列的name属性, 用来显示数据
          // in('aaa',bbb','ccc','ddd','eee','fff');
          // 新增模式没有in
          // param['in'] = "'" + value + "'";
          // 根据类型设置事件
          $ctrl.dblclick(function() {
              //双击事件的执行代码
              if (initialType != 'ZIDONG') {
                  // CORP_ID等不能查询修改
                  popupCardDict($ctrl, label, url, param);
              }
          })
      }
      function testReadOnly() {
          var formData = ${USER_DATA};
          if (formData == '' || typeof(formData) == 'undefined' || $.isEmptyObject(formData)) {
              return;
          }
          var controls = serializeAll();
          console.log(controls);
          var frm = $('.render-wrap');
          $.each(formData, function (key, value) {
              // console.log('[name=' + key + ']');
              // 遍历每一个控件,根据 dataEntityName查找item[0]对应的控件,然后设置数值
              $.each(controls, function () {
                  var dataEntityName = $("[name='" + this.name + "']").attr("data-entity-name");
                  if (typeof(dataEntityName) == 'undefined') {
                      return true; // continue;
                  }
                  // id[0], name[1], type[2], length[3], precision[4], nullable[5], rule[6], initial_type[7], encrypt[8],
                  // subtype[9], subtype_format[10], data_source[11], control_type[12]
                  var items = dataEntityName.split('##');
                  var entityKey = items[0]; // key = this.name; // chanpinmingcheng
                  var label = items[1];
                  var initialType = items[7];
                  if (entityKey == key) {
                      var $ctrl = $('[name=' + this.name + ']', frm);
                      console.log($ctrl.attr("type") + ',key=' + entityKey);
                      switch ($ctrl.attr("type")) {
                          case "text":
                          case "hidden":
                          case "textarea":
                              // dtp_datetime-1533559817285
                              if (this.name.substring(0, 4) == 'dtp_') {
                                  $('#form_datetime' + this.name.substring(4)).data('datetimepicker').setDate(new Date(value));
                                  $('.input-group-addon', $('#form_datetime' + this.name.substring(4))).hide();
                              } else {
                                  //只读属性
                                  $ctrl.attr("readonly", "readonly");
                                  $ctrl.val(value);
                              }
                              break;
                          case "date":
                              $ctrl.val(value);
                              $ctrl.attr("readonly", "readonly");

                              break;
                          case "radio":
                              $ctrl.each(function () {
                                  if ($(this).attr('value') == value) {
                                      $(this).attr("checked", value);
                                  }
                              });
                              $ctrl.attr("onclick", "return false");
                              break;
                          case "checkbox":
                              for (var i = 0; i < value.length; i++) {
                                  $ctrl.each(function () {
                                      if ($(this).attr('value') == value[i]) {
                                          $(this).attr("checked", value[i]);
                                      }
                                  });
                              }
                              $ctrl.attr("onclick", "return false");

                              break;
                          case undefined:
                              $ctrl.val(value);
                              if ($ctrl.prop("type") == 'select-one') {
                                  $('#' + $ctrl.attr("id") + ' option:not(:selected)').prop('disabled', true);
                              } else {
                                  $ctrl.attr("readonly", "readonly");
                              }
                              break;
                          default:
                              $ctrl.val(value);
                              $ctrl.attr("onclick", "return false");

                      }
                      return false;
                  }
              });
//              $.each() {
//              }

          });
      }
      /**
       * regular for input text
       * @param {String} value - input value
       * @param {String} rule  - regular rules
       * @param {String} msg   - error message
       * @return {Object} return
       */
      function regExpValidator(value, rule, msg) {
          if (!rule) {
              return {valid: true, msg: null};
          }
          var reg = new RegExp(rule);
          var bRet = reg.test(value);
          if(!bRet) {
              if (msg == '') msg = '不符合设定规则';
              if (typeof top.cwts === "function") {$(top.cwts(msg));}    // 显示错误提示
              return {valid: false, msg: msg};
          }
          return {valid: true, msg: null};
      }

      /**
       * callback function of blur
       * @param {String} id
       * @param {String} label
       * @param {String} rule
       * @param {String} msg
       * @return {Object} return
       */
      function blurCallback(id, label, rule, msg) {
          var val = $.trim($('#' + id).val());
          var result = this.regExpValidator(val, rule, '<' + label + '>:' + msg);
          if (result.valid === false) {
              $('#' + id).focus();
          }
          return result;
      }
      function requiredCheck(id, label, required) {
          var val = $.trim($('#' + id).val());
          if (required === 'required' && (val == null || val == '' || val == 'undefined')) {
              var msg = '<' + label + '>:该输入项不能为空!';
              if (typeof top.cwts === "function") {$(top.cwts(msg));}
              $('#' + id).focus();
              return {valid: false, msg: msg};
          }
          return {valid: true, msg: null};
      }
      function onFileUpload(fld, asfile) {
          var ID = $("#DATA_ID").val();
          var COLUMNS_ID = fld;
          var asfileCtl = $("[id='" + asfile + "']");
          var COLUMNS_VALUE = asfileCtl.attr("file-value");
          var fid = asfileCtl.attr("file-url");
          if (COLUMNS_VALUE == null || COLUMNS_VALUE == 'undefined') {
              COLUMNS_VALUE = '';
          }
          COLUMNS_VALUE = encodeURI(COLUMNS_VALUE);
          // *** TABLES_ID不能为空, 否则上传失败
          var TABLES_ID = $("#TABLES_ID").val();
          // popup upload form
          var url = '/files/fileUploadForm.do?';
          url += "id=" + ID + "&tablesId=" + TABLES_ID + "&columnsId=" + COLUMNS_ID + "&columnsValue=" + COLUMNS_VALUE;

          var diag = new top.Dialog();
          diag.Drag=true;
          diag.Title ="上传";
          diag.URL = url;
          diag.Width = 600;
          diag.Height = 300;
          diag.CancelEvent = function() { //关闭事件
              var UPLOAD_FILE_NAME = diag.innerFrame.contentWindow.document.getElementById('UPLOAD_FILE_NAME').value;
              if (UPLOAD_FILE_NAME != '') {
                  var items = UPLOAD_FILE_NAME.split('|');
                  asfileCtl.val(items[0]);
                  asfileCtl.attr("file-value", UPLOAD_FILE_NAME);
                  asfileCtl.attr("file-type", items[2]);
                  asfileCtl.attr("file-url", items[3]);
                  // 复用 getUserJsonData 中的data-key, 提交保存的时候使用
                  asfileCtl.attr("data-key", UPLOAD_FILE_NAME);
              }
              diag.close();
          };
          diag.show();
          return true;
      }
      function onFileDownload(fld, asfile) {
          var asfileCtl = $("[id='" + asfile + "']");
          var type = asfileCtl.attr("file-type");
          var fid = asfileCtl.attr("file-url");
          var url = '/files/video/download.do?tm=' + new Date().getTime();
          $.download(url, 'GET', type, fid);
      }
      // 文件下载
      jQuery.download = function(url, method, type, fid){
          if(url.indexOf("/")==0){
              url=url.substr(1);
          }
          jQuery('<form action="/'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
                  '<input type="text" name="type" value="'+type+'"/>' + // 文件类型
                  '<input type="text" name="fid" value="'+fid+'"/>' + // 文件url
                  '</form>')
                  .appendTo('body').submit().remove();
      };
      function onFilePreview(fld, asfile) {
          // play the video or preview the file
          var asfileCtl = $("[id='" + asfile + "']");
          var type = asfileCtl.attr("file-type");
          var fid = asfileCtl.attr("file-url");
          var url;
          // currently can play 3 video formats, mp4/webm/ogv/ogg
          if (type == 'mp4' || type == 'webm' || type == 'ogv') {
              if (type == 'ogv') {
                  type = 'ogg';
              }
              url = '/files/video/page.do?tm=' + new Date().getTime();
              url += "&fid=" + fid + "&type=" + type;
          } else if (type == 'txt' ||type == 'xls' || type == 'xlsx' || type == 'docx' || type == 'doc' || type == 'pdf'  || type == 'ppt' || type == 'pptx') {
              url = '/files/pdf/page.do?tm=' + new Date().getTime();
              url += "&fid=" + fid + "&type=" + type;
          } else if (type == 'jpg' || type == 'png' || type == 'jpeg') {
              url = '/files/jpg/page.do?tm=' + new Date().getTime();
              url += "&fid=" + fid + "&type=" + type;
          }  else {
              bootbox.alert("不支持的文件类型.");
              return;
          }

          var diag = new top.Dialog();
          diag.Drag=true;
          diag.Title ="预览";
          diag.URL = url;
          diag.Width = 800;
          diag.Height = 600;
          diag.CancelEvent = function() { //关闭事件
              //if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
              //	nextPage(${page.currentPage});
              //}
              diag.close();
          };
          diag.show();
          return true;
      }
      // 弹出
      function popupCardDict(ctrl, label, url, param) {
          if (typeof top.jzts === "function") {top.jzts();}
          param['label'] = label;
          param['showCount'] = 10;
          param['currentPage'] = 0;

          var key = ctrl.attr('data-key');
          if (typeof(key) == 'undefined') {
              key = '';
          }
          param['DICT_KEY'] = key;
          var value = ctrl.attr('data-value');
          if (typeof(value) == 'undefined') {
              value = '';
          }
          param['DICT_VALUE'] = value;

          var diag = new top.Dialog();
          diag.Drag=true;
          diag.Title ="编辑";
          diag.URL = '/card/goPopupDict.do?param='+encodeURIComponent(JSON.stringify(param));
          diag.Width = 450;
          diag.Height = 355;
          diag.CancelEvent = function(){ //关闭事件

              var dblClick = diag.innerFrame.contentWindow.document.getElementById('DBL_CLICK').value;
              if (dblClick == '' || dblClick == 'undefined') {
                  diag.close();
                  return;
              }
              var key = diag.innerFrame.contentWindow.document.getElementById('DICT_KEY').value;
              var value = diag.innerFrame.contentWindow.document.getElementById('DICT_VALUE').value
              if (key == null || key == '' || key == 'undefined') {
                  bootbox.alert('请选择' + label);
                  return;
              }
              $('#DICT_KEY').val(key);
              $('#DICT_VALUE').val(value);
              ctrl.val(value);
              ctrl.attr('data-key', key);
              ctrl.attr('data-value', value);
              diag.close();
          };
          diag.OKEvent = function() {
              var key = diag.innerFrame.contentWindow.document.getElementById('DICT_KEY').value;
              var value = diag.innerFrame.contentWindow.document.getElementById('DICT_VALUE').value
              if (key == null || key == '' || key == 'undefined') {
                  bootbox.alert('请选择' + label);
                  return;
              }
              $('#DICT_KEY').val(key);
              $('#DICT_VALUE').val(value);
              ctrl.val(value);
              ctrl.attr('data-key', key);
              ctrl.attr('data-value', value);
              diag.close();
          }
          diag.show();
      }

  </script>

</body>

</html>
