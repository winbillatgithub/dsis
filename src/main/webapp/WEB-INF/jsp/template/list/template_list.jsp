<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglib.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>

<!-- 下拉框 -->
<link rel="stylesheet" href="/static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="/static/ace/css/datepicker.css" />

<link rel="stylesheet" href="/static/slickgrid/slick.grid.css" type="text/css"/>
<link rel="stylesheet" href="/static/slickgrid/css/smoothness/jquery-ui-1.11.3.custom.css" type="text/css"/>
<link rel="stylesheet" href="/static/slickgrid/css/examples.css" type="text/css"/>
<link rel="stylesheet" href="/static/slickgrid/controls/slick.columnpicker.css" type="text/css"/>
<link rel="stylesheet" href="/static/slickgrid/controls/slick.pager.css" type="text/css"/>

  <style>
    .cell-title {
      font-weight: bold;
    }
    .cell-text-align-center {
      text-align: center;
    }
    .cell-text-align-left {
      text-align: left;
    }
    .cell-text-align-right {
      text-align: right;
    }
    .cell-dimentions {
      border-right-color: silver;
      border-right-style: solid;
      background: #f5f5f5;
      color: gray;
      text-align: left;
    }
    .cell-selection {
      border-right-color: silver;
      border-right-style: solid;
      background: #f5f5f5;
      color: gray;
      text-align: right;
      font-size: 10px;
    }
    .slick-row.selected .cell-selection {
      background-color: transparent; /* show default selected row background */
    }
    .item-details-form {
      z-index: 10000;
      display: inline-block;
      border: 1px solid black;
      padding: 10px;
      background: #efefef;
      -moz-box-shadow: 0px 0px 15px black;
      -webkit-box-shadow: 0px 0px 15px black;
      box-shadow: 0px 0px 15px black;
      position: absolute;
      top: 10px;
      left:0px;
      right: 0px;
      margin:auto;
/*       text-align: center; */
/* 	  vertical-align:middle; */
      width:350px;
    }
    .item-details-form-buttons {
		text-align:center
    }
    .item-details-label {
      margin-left: 10px;
      margin-top: 20px;
      display: inline-block;
      font-weight: bold;
      width:120px;
    }
    .item-details-editor-container {
      width: 330px;
      height: 30px;
      border: 1px solid silver;
      background: white;
      display: inline-block;
      margin: 0px;
      margin-top: 0px;
      padding: 0;
      padding-left: 4px;
      padding-right: 4px;
    }
     .item-details-editor-container-date {
      width: 98px;
      height: 30px;
      border: 1px solid silver;
      background: white;
      display: inline-block;
      margin: 0px;
      margin-top: 0px;
      padding: 0;
      padding-left: 4px;
      padding-right: 4px;
    }
    .loading-indicator {
      display: inline-block;
      padding: 12px;
      background: white;
      -opacity: 0.5;
      color: black;
      font-weight: bold;
      z-index: 9999;
      border: 1px solid red;
      -moz-border-radius: 10px;
      -webkit-border-radius: 10px;
      -moz-box-shadow: 0 0 5px red;
      -webkit-box-shadow: 0px 0px 5px red;
      -text-shadow: 1px 1px 1px white;
    }
    .loading-indicator label {
      padding-left: 20px;
      background: url('/static/images/loadingi.gif') no-repeat center left;
    }
  </style>
</head>
<body>
	<!-- /section:basics/navbar.layout -->
	<div style="position:relative">
		<div>
			<!-- 检索  -->
			<form action="/${fromAction}?FROM=${FROM}&TABLES_ID=${TABLES_ID}" method="post" name="Form" id="Form">
		    <input type="hidden" name="CORP_ID" id="CORP_ID" value="${CORP_ID}"/>
		    <input type="hidden" name="SLAVE_TABLES_ID" id="SLAVE_TABLES_ID" value="${SLAVE_TABLES_ID}"/>
		    <input type="hidden" name="TRANS_ID" id="TRANS_ID" value="${TRANS_ID}"/>
		    <input type="hidden" name="COMMIT_FLAG" id="COMMIT_FLAG" value="${COMMIT_FLAG}"/>
		    <input type="hidden" name="IS_COMMIT_USER" id="IS_COMMIT_USER" value="${IS_COMMIT_USER}"/>
		    <input type="hidden" name="LIST_LIST_MASTER" id="LIST_LIST_MASTER" value="${LIST_LIST_MASTER}"/>
		    <input type="hidden" name="WHERE" id="WHERE" value="${WHERE}"/>

 			<table id="table_report" class="table table-striped table-bordered table-hover">
				<tr>
				<c:if test="${TRANS_ID != null && TRANS_ID != ''}">
					<td style="width:90px;text-align: right;padding-top: 13px;">数据日期:</td>
					<td>
					  <input class="span10 date-picker" name="DATA_DATE" id="DATA_DATE" value="${DATA_DATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="数据日期" title="数据日期" style="width:98%;"/>
					</td>
					<td style="width:90px;text-align: right;padding-top: 13px;">上报单位:</td>
					<td>
					<input type="text" name="CORP_NAME" id="CORP_NAME" readonly="readonly" value="${CORP_NAME}" title="上报单位" style="width:98%;"/>
					</td>
				</c:if>
				<c:if test="${TRANS_ID == null || TRANS_ID == ''}">
					<td style="width:90px;text-align: right;padding-top: 13px;">单位:</td>
					<td>
					<input type="text" name="CORP_NAME" id="CORP_NAME1" readonly="readonly" value="${CORP_NAME}" title="单位" style="width:98%;"/>
					</td>
				</c:if>
				<%-- <td>
					<div class="nav-search">
						<span class="input-icon">
							<input type="text" placeholder="这里输入查询内容" class="nav-search-input" id="nav-search-input" autocomplete="off" name="LIST_KEYWORDS" value="${pd.LIST_KEYWORDS}" placeholder="这里输入查询内容"/>
							<i class="ace-icon fa fa-search nav-search-icon"></i>
						</span>
					</div>
				</td> --%>
				<c:if test="${QX.cha == 1 }">
				<td style="vertical-align:top;padding-left:2px">
					<a class="btn btn-light btn-xs" onclick="tosearch();"  title="检索">
						<i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i>
					</a>
					<a class="btn btn-light btn-xs" onclick="clearSearch();"  title="清空">
						<i id="nav-search-icon1" class="ace-icon fa fa-refresh bigger-110 green"></i>
					</a>
				</td>
				</c:if>
				</tr>
			</table>
			</form>
		</div>
		<div>
			<div id="myGrid" style="width:100%;height:380px;"></div>
			<div id="pager" style="width:100%;height:20px;"></div>
			<div style="clear:both;"></div>
		</div>
		<div style="float:left;">
			<!-- 审批流程 -->
			<c:if test="${TRANS_ID != null && TRANS_ID != ''}">
				<c:if test="${(COMMIT_FLAG == null || COMMIT_FLAG == '' || COMMIT_FLAG == '0' || COMMIT_FLAG == '1') && IS_COMMIT_USER == '1'}">
					<c:if test="${QX.add == 1 }">
					<a id="add" class="btn btn-sm btn-success" onclick="addPopup();">新建</a>
					</c:if>
					<c:if test="${QX.del == 1 }">
					<a id="delete" class="btn btn-sm btn-success" onclick="deleteRow('${TRANS_ID}','${TABLES_ID}','${CORP_ID}','${DATA_DATE}');">删除</a>
					</c:if>
					<c:if test="${QX.add == 1 || QX.del == 1 || QX.edit == 1}">
					<a id="save" class="btn btn-sm btn-success" onclick="save('','${TRANS_ID}','${TABLES_ID}','${CORP_ID}','${DATA_DATE}');">保存</a>
					<a id="submit" class="btn btn-sm btn-success" onclick="submit('${TRANS_ID}','${TABLES_ID}','${CORP_ID}','${DATA_DATE}','${TYPE}');">提交审核</a>
					</c:if>
				</c:if>
				<c:if test="${COMMIT_FLAG > '1' && COMMIT_FLAG != '9' && IS_COMMIT_USER == '0'}">
					<a id="approve" class="btn btn-sm btn-primary" onclick="approve('ok','${TRANS_ID}','${TABLES_ID}','${COMMIT_FLAG}','${AUDIT_LEVEL}','${AUDIT_USER_LEVEL1}','${AUDIT_USER_LEVEL2}','${AUDIT_USER_LEVEL3}');">审核通过</a>
					<a id="deny" class="btn btn-sm btn-danger" onclick="approve('ng','${TRANS_ID}','${TABLES_ID}','${COMMIT_FLAG}','${AUDIT_LEVEL}','${AUDIT_USER_LEVEL1}','${AUDIT_USER_LEVEL2}','${AUDIT_USER_LEVEL3}');">驳回</a>
				</c:if>
			</c:if>
			<!-- 基础数据维护，非审批 -->
			<c:if test="${(TRANS_ID == null || TRANS_ID == '') && (COMMIT_FLAG == null || COMMIT_FLAG == '' || COMMIT_FLAG == '0' || COMMIT_FLAG == '1')}">
				<c:if test="${QX.add == 1 }">
				<a id="add" class="btn btn-sm btn-success btnEdit" onclick="addPopup();">新建</a>
				</c:if>
				<c:if test="${QX.del == 1 }">
				<a id="delete" class="btn btn-sm btn-success btnEdit" onclick="deleteRow('${TRANS_ID}','${TABLES_ID}','${CORP_ID}','${DATA_DATE}');">删除</a>
				</c:if>
				<c:if test="${QX.add == 1 || QX.del == 1 || QX.edit == 1}">
				<a id="save" class="btn btn-sm btn-success btnEdit" onclick="save('','${TRANS_ID}','${TABLES_ID}','${CORP_ID}','${DATA_DATE}');">保存</a>
				</c:if>
			</c:if>
			<a class="btn btn-sm btn-success" onclick="Excelto();">导入</a>
			<a class="btn btn-sm btn-success" onclick="toExcel('${fromAction}','${FROM}','${TABLES_ID}');">导出</a>
			<c:if test="${TRANS_ID != null && TRANS_ID != ''}">
			<a class="btn btn-sm btn-success" onclick="goSondict('${FROM}','${TRANS_ID}','${TABLES_ID}');">返回</a>
			</c:if>

		</div>

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp"%>
<!-- 删除时确认窗口 -->
<script src="/static/ace/js/bootbox.js"></script>
<!-- ace scripts -->
<script src="/static/ace/js/ace/ace.js"></script>
<!-- 下拉框 -->
<script src="/static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<script src="/static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="/static/js/jquery.tips.js"></script>

<!-- SlickGrid控件相关 -->
<script src="/static/slickgrid/lib/firebugx.js"></script>

<script src="/static/slickgrid/lib/jquery-1.11.2.min.js"></script>
<script src="/static/slickgrid/lib/jquery-ui-1.11.3.min.js"></script>
<script src="/static/slickgrid/lib/jquery.event.drag-2.3.0.js"></script>
<script src="/static/slickgrid/lib/jquery.tmpl.js"></script>
<script src="/static/slickgrid/lib/jquery.jsonp-2.4.min.js"></script>

<script src="/static/slickgrid/slick.core.js"></script>
<script src="/static/slickgrid/slick.formatters.js"></script>
<script src="/static/slickgrid/slick.editors.js"></script>
<script src="/static/slickgrid/plugins/slick.rowselectionmodel.js"></script>
<script src="/static/slickgrid/slick.grid.js"></script>
<script src="/static/slickgrid/slick.remotemodel.js"></script>
<script src="/static/slickgrid/slick.dataview.js"></script>
<script src="/static/slickgrid/controls/slick.columnpicker.js"></script>
<script src="/static/slickgrid/controls/slick.pager.js"></script>
<script src="/static/slickgrid/slick.compositeeditor.js"></script>

<!-- timepicker addon -->
<script src="/static/js/timepicker/jquery-ui-timepicker-addon.js"></script>
<link rel="stylesheet" href="/static/js/timepicker/jquery-ui-timepicker-addon.css" type="text/css"/>

<script>
    if (typeof top.hangge === "function") {$(top.hangge());}
	//检索
	function tosearch() {
		openDetails();
	}

/* 	$(document).ready(function() {
		// Cached window jQuery object
	    var $window = $(window);

	    // Store the window width
	    var windowWidth = $window.width();

		$(window).resize(function(e) {
			// Check window width has actually changed and it's not just iOS triggering a resize event on scroll
	        if ($window.width() != windowWidth) {
	        	// Update the window width for next time
	            windowWidth = $window.width();
				grid.resizeCanvas();
	        }
		});
	}); */
	var NOT_EDIT = 0;	// 不能编辑
	var CAN_EDIT = 1;	// 可以编辑
	var MID_EDIT = 2; // 中间状态，LISTLIST的MASTER的关联字段状态
	var dataView;
	var grid;
	var loadingIndicator = null;
	var rules = [];
	//var dimList = ${dimList};
	var columns = [];
	var options = {
		//editable: true,
		enableAddRow: true,
		leaveSpaceForNewRows: false,
		enableCellNavigation: true,
		asyncEditorLoading: true,
		forceFitColumns: false,
		/* fullWidthRows: true, */
		rowHeight: 35,
		multiSelect: false,
		enableTextSelectionOnCells: true,
		autoHeight: false,
		forceSyncScrolling: true
	};
	// 判断控件的可编辑状态
    var commitFlag = $("#COMMIT_FLAG").val();
    var isCommitUser = $("#IS_COMMIT_USER").val();
    var transId = $("#TRANS_ID").val();
    var bEditale = false;
    if (transId != null && transId != '') {
    	if ((commitFlag == null || commitFlag == '' || commitFlag == '0' || commitFlag == '1') && isCommitUser == '1') {
    		bEditale = true;
    	}
    } else if (commitFlag == null || commitFlag == '' || commitFlag == '0' || commitFlag == '1') {
		bEditale = true;
    }
    options['editable'] = bEditale;

	var sortcol = "title";
	var sortdir = 1;
	var percentCompleteThreshold = 0;
	var searchString = "";
	function requiredFieldValidator(value) {
		if (value == null || value == undefined || !value.length) {
		  return {valid: false, msg: "This is a required field"};
		}
		else {
		  return {valid: true, msg: null};
		}
	}
	function regExpValidator(value, rule, msg) {
		if (rule == '' || rule == null || rule == 'undefined') {
			return {valid: true, msg: null};
		}
		var reg = new RegExp(rule);
		var bRet = reg.test(value);
		 if(!bRet) {
			 if (msg == '') msg = '不符合设定规则';
             if (typeof top.cwts === "function") {$(top.cwts(msg));}
			 return {valid: false, msg: msg};
		 }
		 return {valid: true, msg: null};
	}
	function myFilter(item, args) {
		//if (item["percentComplete"] < args.percentCompleteThreshold) {
		//  return false;
		//}
		if (args.searchString != "" && item["title"].indexOf(args.searchString) == -1) {
		  return false;
		}
		return true;
	}
	function percentCompleteSort(a, b) {
		return a["percentComplete"] - b["percentComplete"];
	}
	function comparer(a, b) {
		var x = a[sortcol], y = b[sortcol];
		return (x == y ? 0 : (x > y ? 1 : -1));
	}

	$(".grid-header .ui-icon")
	    .addClass("ui-state-default ui-corner-all")
	    .mouseover(function (e) {
	      $(e.target).addClass("ui-state-hover")
	    })
	    .mouseout(function (e) {
	      $(e.target).removeClass("ui-state-hover")
	    });
	
	$(function () {

        var colIndex = 0;
		var item = (columns[colIndex++] = {});
		item["id"] = "sel";
		item["name"] = '行号';
		item["field"] = 'num';
		item["behavior"] = 'select';
		item["cssClass"] = 'cell-selection';
		item["width"] = 40;
		item["cannotTriggerInsert"] = true;
		item["resizable"] = false;
		item["selectable"] = false;				// cell can be navigated
    	item["editor"] = Slick.Editors.Label;	// readonly text editor
    	item["readonly"] = NOT_EDIT;
    	item["encrypt"] = '0';
		var dimList = ${dimList};  //eval('${dimList}');
		for (var i = 0; i < dimList.length; i++ ) {
		    var dim = dimList[i];
		    var item = (columns[colIndex++] = {});
		    item["id"] = dim['DIMENTIONS_NAME_EN'];
		    item["name"] = dim['DIMENTIONS_NAME_CN'];
		    item["field"] = dim['DIMENTIONS_NAME_EN'];
		    item["cssClass"] = "cell-dimentions";
			item["resizable"] = true;
	    	item["editor"] = Slick.Editors.Label;	// readonly text editor
	    	item["readonly"] = NOT_EDIT;
	    	item["encrypt"] = '0';
		}
		var indexesList = ${indexesList};
		for (var i = 0; i < indexesList.length; i++ ) {
		    var index = indexesList[i];
		    var item = (columns[colIndex++] = {});
		    item["id"] = index['COLUMN_NAME_EN'];
			item["name"] = index['COLUMN_NAME_CN'];
			item["field"] = index['COLUMN_NAME_EN'];
			item["type"] = index['DATA_TYPE_NAME'];
	    	item["encrypt"] = index['RSV_STR2'];
			if (index['LENG'] == 0) {
				if ('DATETIME' == index['DATA_TYPE_NAME']) {
					item["width"] = 200;	// DATETIME
				} else if ('DATE' == index['DATA_TYPE_NAME']) {
					item["width"] = 120;	// DATE
				} else {
					item["width"] = 100;	// DATE
				}
			} else {
                var maxLen = (index['LENG'] * 2) > 300 ? 300 : (index['LENG'] * 2);
				item["width"] = (index['LENG'] * 2) < 80 ? 80 : maxLen;
			}

			item["resizable"] = true;
		    if ('NUMBER' == index['DATA_TYPE_NAME']) {
		    	item["cssClass"] = "cell-text-align-right";
		    	item["editor"] = Slick.Editors.Text;
			    item["formatter"] = Slick.Formatters.Float;
			    item["precision"] = index['DATA_PRECISION'];
		    } else if ('INTEGER' == index['DATA_TYPE_NAME']) {
		    	item["cssClass"] = "cell-text-align-right";
		    	item["editor"] = Slick.Editors.Integer;
		    } else if ('BOOL' == index['DATA_TYPE_NAME']) {
		    	item["cssClass"] = "cell-text-align-center";
		    	item["editor"] = Slick.Editors.Checkbox;
		    	item["formatter"] = Slick.Formatters.Checkmark;
		    	// CONTROL_TYPE=checkbox|select
		    	// 根据配置可以切换显示类型
		    	// TODO
                if (index["CONTROL_TYPE"] == 'radio-group') {
                    item["editor"] = Slick.Editors.RadioGroup;
                    item["formatter"] = Slick.Formatters.RadioGroup;
                    item["options"] = parseDataSource(index['DATA_SOURCE']);
                } else if (index["CONTROL_TYPE"] == 'select') {
                    item["editor"] = Slick.Editors.Select;
                    item["formatter"] = Slick.Formatters.SelectFormatter;
                    item["options"] = parseDataSource(index['DATA_SOURCE']);
                }
		    } else if ('DATETIME' == index['DATA_TYPE_NAME']) {
		    	item["cssClass"] = "cell-text-align-left";
		    	item["editor"] = Slick.Editors.DateTime;
		    	item["formatter"] = Slick.Formatters.DateTime;
		    } else if ('DATE' == index['DATA_TYPE_NAME']) {
		    	item["cssClass"] = "cell-text-align-left";
		    	item["editor"] = Slick.Editors.Date;
		    } else if ('STRING' == index['DATA_TYPE_NAME']) {
		    	item["cssClass"] = "cell-text-align-left";
		    	item["editor"] = Slick.Editors.Text;
		    	// CONTROL_TYPE=text|select|combobox
		    	// 根据配置可以切换显示类型
		    	// TODO
		    	// BTN_UDP update/download/preview
		    	if (index["CONTROL_TYPE"] == 'asfile' && index["CONTROL_SUB_TYPE"] == 'BTN_UDP') {
			    	item["editor"] = null;
			    	item["formatter"] = Slick.Formatters.ButtonUDP;
                } else if (index["CONTROL_TYPE"] == 'checkbox-group') {
                    item["editor"] = Slick.Editors.CheckboxGroup;
                    item["formatter"] = Slick.Formatters.CheckboxGroup;
                    item["options"] = parseDataSource(index['DATA_SOURCE']);
                } else if (index["CONTROL_TYPE"] == 'text' && index["CONTROL_SUB_TYPE"] == 'password') {
                    item["editor"] = Slick.Editors.Password;
                    item["formatter"] = Slick.Formatters.Password;
                } else if (index["CONTROL_TYPE"] == 'text' && index["CONTROL_SUB_TYPE"] == 'color') {
                    item["editor"] = Slick.Editors.Color;
                    item["formatter"] = Slick.Formatters.Color;
		    	}
		    } else {
		    	item["cssClass"] = "cell-text-align-left";
		    }
		    if (0 == index['NULLABLE']) {
/* 		    	item["regrule"] = '^\\d{2,4}$';、、var str = ""; /^$/.test(str)
		    	item["validator"] = regExpValidator;
 */		    }
		    // 设置规则校验
		    if (index['RULE'] != null && index['RULE'] != '') {
		    	item["regrule"] = index['RULE'];  	//'^\\d{2,4}$';
		    	item["regmsg"] = index['RULE_MSG'];	// Add message
		    	item["validator"] = regExpValidator;
			} else {
				item["regrule"] = '';
				item["regmsg"] = '';
			}
		    // 多表关联字段，下拉列表显示
		    if (index['JOIN_TABLES_ID'] != null && index['JOIN_TABLES_ID'] != '') {
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
					var url = '/common/getjoinlist.do?tm='+new Date().getTime();
			    	item["joinurl"] = url;
			    	item["editor"] = Slick.Editors.JoinText;
			    	item["formatter"] = Slick.Formatters.JoinText;
			    	item["options"] = {};// dynamic option data, {"id1":"va1","id2","val2"}
			    	// 要求该请求满足一定的sla
//					var param = "{'tablesId':'" + item["join_table_name"] + "','corpId':'" + item["corp_id"]
//			    		+ "','joinColumnsId':'" + item["join_table_column"] + "','selectColumnsId':'" + item["join_select_table_column"] + "'}";
//			    	var obj = eval('(' + param + ')');
//					callAjax(url, false, obj, function(dt) {
//						if (dt.success == true) {
//							item["options"] = eval(dt.data);
//						}
//					});
                    item["readonly"] = NOT_EDIT;
                    item["cssClass"] = "cell-text-align-left cell-join";
                } else {
		    		// 多表关联的内联字段，不能编辑
                    item["readonly"] = NOT_EDIT;
		    		item['initialType'] = 'ZIDONG';
                    item["cssClass"] = "cell-text-align-left";
                }
			} else {
		    	item["join_table_name"] = '';
		    	item["join_table_column"] = '';
		    	item["corpid"] = '';
		    	item["joinurl"] = '';
			}
		    // 自动录入不能编辑
		    if ('ZIDONG' == index['INITIAL_TYPE_NAME']) {
		    	//item["editor"] = Slick.Editors.Label;
		    	item["readonly"] = NOT_EDIT;
                item['initialType'] = 'ZIDONG';
		    }
		    // 可检索项
		    if ('1' == index['SEARCH']) {
		    	item["searchable"] = true;
		    }
		}
		// Operating buttons(List-List Mode)
        var LIST_LIST_MASTER = $("#LIST_LIST_MASTER").val();
        var listListRule = null;
		if (LIST_LIST_MASTER == '1') {	// master mode
			// 解析list-list规则
			listListRule = JSON.parse('${LIST_LIST_RULE}');

			item = (columns[colIndex++] = {});
			item["id"] = "btn";
			item["name"] = '进入';
			item["field"] = 'btn';
			item["width"] = 60;
			item["resizable"] = true;
			item["selectable"] = true;				// cell can be navigated
	    	item["formatter"] = Slick.Formatters.Button;
			// 进入SLAVE LIST之后，当前行的MASTER关联字段不能再修改
	    	var MASTER_COLUMNS_NAME_EN = listListRule['MASTER_COLUMNS_NAME_EN'];
			for (var index = 0; index < columns.length; index ++) {
				if (columns[index]['field'] == MASTER_COLUMNS_NAME_EN) {
					break;
				}
			}
			// 只有新增的时候，可以修改关联字段，一旦进入SLAVE LIST则不能修改
			// winbill. 0422
			columns[index]["readonly"] = MID_EDIT; // 特殊状态，list-list的MASTER的关联字段的状态控制
		} else if (LIST_LIST_MASTER == '0') {	// slave mode
			// 新建-删除按钮隐藏
			$("#add").hide();
			$("#delete").hide();
		}
		console.log(item)
		rules = ${ruleList}; //eval('${ruleList}');
		dataView = new Slick.Data.DataView({ inlineFilters: true });
		setAjaxProperties();
		grid = new Slick.Grid("#myGrid", dataView, columns, options);
		grid.setSelectionModel(new Slick.RowSelectionModel());
		var pager = new Slick.Controls.Pager(dataView, grid, $("#pager"));
		var columnpicker = new Slick.Controls.ColumnPicker(columns, grid, options);

		grid.onViewportChanged.subscribe(function (e, args) {
		  /* if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		  } */
	      var vp = grid.getViewport();
	      dataView.ensureData(vp.top, vp.bottom, false, false);
	    });
		dataView.onDataLoading.subscribe(function () {
	      if (!loadingIndicator) {
	        loadingIndicator = $("<span class='loading-indicator'><label>拼命加载中...</label></span>").appendTo(document.body);
	        var $g = $("#myGrid");
	        loadingIndicator
	            .css("position", "absolute")
	            .css("top", $g.position().top + $g.height()/2 - loadingIndicator.height()/2).css("left", $g.position().left + $g.width()/2 - loadingIndicator.width()/2);
	      }
	      loadingIndicator.show();
	    });
		dataView.onDataLoaded.subscribe(function (e, args) {
            // 获取Join列的value,异步获取关联数据,然后单独刷新一列
            var innerData = dataView.getCurrentPageItems();
            refreshJoinColumns(innerData, args.from, args.to, '0');

            for (var i = args.from; i <= args.to; i++) {
                grid.invalidateRow(i);
            }
            grid.updateRowCount();
            grid.render();
            if (loadingIndicator) loadingIndicator.fadeOut();
	    });
		grid.onClick.subscribe(function (e, args) {
			if ($(e.target).hasClass('show-detail-item')) {
		        // LIST-LIST模式，进入Slave页面
				// var listListRule = eval("("+'${LIST_LIST_RULE}'+")");
		        if (listListRule == null || listListRule == 'undefined') {
		        	bootbox.alert('请联系管理员，设定List-List规则.');
		        	return;
		        }
		        var innerData = dataView.getCurrentPageItems();
				if (innerData[args.row]["dirty"] === '1' || innerData[args.row]["dirty"] === '2') {
					bootbox.alert("请先保存.");
					return;
				}
		        var SLAVE_TABLES_ID = listListRule['SLAVE_TABLES_ID'];
		        var LISTLISTRULE_ID = listListRule['LISTLISTRULE_ID'];
		        var MASTER_COLUMNS_ID = listListRule['MASTER_COLUMNS_ID'];
		        var MASTER_COLUMNS_NAME_EN = listListRule['MASTER_COLUMNS_NAME_EN'];
		        var SOURCE_COLUMNS_NAME_EN = listListRule['SOURCE_COLUMNS_NAME_EN'];
		        var MASTER_ROW_ID_VALUE = innerData[args.row]['id'];		// 获取ID对应值(数据隔离)

		        // innerData is Master tables' data
		        var MASTER_COLUMNS_VALUE = innerData[args.row][MASTER_COLUMNS_NAME_EN];
		        // Parameters: Join Column name&value, slave table name, corp_id
		        var bRet = goDetailItem(LISTLISTRULE_ID, SLAVE_TABLES_ID, MASTER_COLUMNS_ID, MASTER_COLUMNS_VALUE,SOURCE_COLUMNS_NAME_EN,MASTER_ROW_ID_VALUE);
				// 进入SLAVE LIST之后，当前行的MASTER关联字段不能再修改
				if (bRet == true) {
					// 和有新增权限，没有编辑权限一样的处理逻辑
					// 这一行的这一列不能修改！！！！
		        	// dirty标志
				}
		    } else if ($(e.target).hasClass('attach-file-upload')) {
		        var innerData = dataView.getCurrentPageItems();
				// dirty:0 无变化  1：新增 2：修改 3：删除
		        var dirty = innerData[args.row]['dirty'];
		        if (dirty == 1) {
					bootbox.alert("是新增数据，请先点击保存按钮.");
					return;
		        }
		        var id = innerData[args.row]['id'];		// 获取ID对应值(数据隔离)
		        var columnsId = columns[args.cell]['field'];
		        var columnsValue = innerData[args.row][columnsId];
		    	onFileUpload(id, columnsId, columnsValue, args.row);
		    } else if ($(e.target).hasClass('attach-file-download')) {
		        var innerData = dataView.getCurrentPageItems();
		        var columnsId = columns[args.cell]['field'];
		        var value = innerData[args.row][columnsId];
		        if (value == null || value == '' || value == 'undefined') {
					bootbox.alert("没有文件可下载，请先上传文件.");
					return;
		        }
		        var arr = value.split("|");
		    	onFileDownload(arr[2], arr[3]);
		    } else if ($(e.target).hasClass('attach-file-preview')) {
		        var innerData = dataView.getCurrentPageItems();
				// dirty:0 无变化  1：新增 2：修改 3：删除
		        var dirty = innerData[args.row]['dirty'];
		        if (dirty == 1) {
					bootbox.alert("是新增数据，请先点击保存按钮.");
					return;
		        }
		        var columnsId = columns[args.cell]['field'];
		        var value = innerData[args.row][columnsId];
		        if (value == null || value == '' || value == 'undefined') {
					bootbox.alert("没有文件可预览，请先上传文件.");
					return;
		        }
		        var arr = value.split("|");
		        // alert(arr);
		    	if (arr[2] == 'txt' || arr[2] == 'doc' || arr[2] == 'docx' || arr[2] == 'ppt' || arr[2] == 'pptx' || arr[2] == 'xls'|| arr[2] == 'xlsx') {
		        	onFilePreview(arr[2], arr[5]);
		        } else {
		        	onFilePreview(arr[2], arr[3]);
		        }
		    }
		});
        grid.onDblClick.subscribe(function (e, args) {
            if ($(e.target).hasClass('cell-selection')) {
                var innerData = dataView.getCurrentPageItems();
				if (innerData == null || innerData == 'undefined' || innerData.length == 0) {
                    return;
                }
                // dirty:0 无变化  1：新增 2：修改 3：删除
                var dirty = innerData[args.row]['dirty'];
                if (dirty == 1) {
                    bootbox.alert("是新增数据，请先点击保存按钮.");
                    return;
                }
                var id = innerData[args.row]['id'];		// 获取ID对应值(数据隔离)
                var userData = innerData[args.row];
                // popup
                editPopup(id, args.row, userData);
            } else if ($(e.target).hasClass('cell-join')) {
                var initialType = columns[args.cell]['initialType'];
                if (initialType === 'ZIDONG') {
                    return;
                }
                var innerData = dataView.getCurrentPageItems();
                if (innerData == null || innerData == 'undefined' || innerData.length == 0) {
                    return;
                }
                // dirty:0 无变化  1：新增 2：修改 3：删除
                var dirty = innerData[args.row]['dirty'];
                if (dirty == 1) {
                    bootbox.alert("是新增数据，请先点击保存按钮.");
                    return;
                }
                var columnsId = columns[args.cell]['field'];
                var key = innerData[args.row][columnsId];
                var value = columns[args.cell].options[key];
                var param = {
                    'tablesId':columns[args.cell]["join_table_name"],
                    'corpId':columns[args.cell]["corp_id"],
                    'joinColumnsId':columns[args.cell]["join_table_column"],
                    'selectColumnsId':columns[args.cell]["join_select_table_column"],
                    'field':columns[args.cell]["field"],
                    'id':innerData[args.row]['id']
                };
                popupCardDict(param, args.row, args.cell, key, value, columns[args.cell]['name'], columns[args.cell]["options"]);
            }
        });
		grid.onBeforeEditCell.subscribe(function(e,args) {
		  if (!isCellEditable(args.row, args.cell, args.item)) {
		    return false;
		  }
		});
		grid.onCellChange.subscribe(function (e, args) {
          // 清除错误标志
          if (typeof top.cwtsyc === "function") {$(top.cwtsyc());}
		  // 设置当前行修改标志 dirty:0 无变化  1：新增 2：修改 3：删除
		  var innerData = dataView.getCurrentPageItems();
		  if (innerData[args.row]["dirty"] !== '1' &&
			  innerData[args.row]["dirty"] !== '3') {
		    innerData[args.row]["dirty"] = '2';
		  }
		  // 应用业务规则
		  for (var i = 0; i < rules.length; i++) {
			  if (rules[i]["CHECKED"] == false) {
				  continue;
			  }
			  var columnName1 = rules[i]["IN_FIELD1"];
			  var columnName2 = rules[i]["IN_FIELD2"];
			  if (columns[args.cell]['field'] != columnName1 && columns[args.cell]['field'] != columnName2) {
				  continue;
			  }
			  var columnOut = rules[i]["OUT_FIELD"];
			  var field1 = innerData[args.row][columnName1];
			  var field2 = innerData[args.row][columnName2];
			  var dataType = rules[i]["DATA_TYPE"];
			  if (dataType == 'NUMBER') {
				  if (field1 == null || field1 == '' || field1 == 'undefined') {
					  field1 = 0;
				  }
				  if (field2 == null || field2 == '' || field2 == 'undefined') {
					  field2 = 0;
				  }
				  field1 = parseFloat(field1);
				  field2 = parseFloat(field2);
			  } else if (dataType == 'INTEGER') {
				  if (field1 == null || field1 == '' || field1 == 'undefined') {
					  field1 = 0;
				  }
				  if (field2 == null || field2 == '' || field2 == 'undefined') {
					  field2 = 0;
				  }
				  field1 = parseInt(field1);
				  field2 = parseInt(field2);
			  } else if (dataType == 'STRING') {
				  if (field1 == null || field1 == '' || field1 == 'undefined') {
					  field1 = '';
				  }
				  if (field2 == null || field2 == '' || field2 == 'undefined') {
					  field2 = '';
				  }
			  }
			  if (rules[i]["OPERATOR_FIELD"] == 'PLUS') {
				  innerData[args.row][columnOut] = field1 + field2;
			  } else if (rules[i]["OPERATOR_FIELD"] == 'MINUS') {
				  innerData[args.row][columnOut] = field1 - field2;
			  } else if (rules[i]["OPERATOR_FIELD"] == 'MULTIPLE') {
				  innerData[args.row][columnOut] = field1 * field2;
			  } else if (rules[i]["OPERATOR_FIELD"] == 'DEVIDE') {
				  innerData[args.row][columnOut] = field1 / field2;
			  } else if (rules[i]["OPERATOR_FIELD"] == 'MAX') {
				  innerData[args.row][columnOut] = (field1 > field2) ? field1 : field2;
			  } else if (rules[i]["OPERATOR_FIELD"] == 'MIN') {
				  innerData[args.row][columnOut] = (field1 > field2) ? field2 : field1;
			  } else if (rules[i]["OPERATOR_FIELD"] == 'MOD') {
				  innerData[args.row][columnOut] = field1 % field2;
			  } else if (rules[i]["OPERATOR_FIELD"] == 'AVERAGE') {
				  innerData[args.row][columnOut] = (field1 + field2)/2;
			  }
		  }
		  //dataView.setData(innerData);
		  dataView.updateItem(args.item.id, args.item);
		});
		grid.onAddNewRow.subscribe(function (e, args) {
		  // 修改columns
		  // TODO 
		  /* $.extend(item, args.item);
		  dataView.addItem(item); */
		});
		grid.onKeyDown.subscribe(function (e) {
		  // select all rows on ctrl-a
		  if (e.which != 65 || !e.ctrlKey) {
		    return false;
		  }
		  var rows = [];
		  for (var i = 0; i < dataView.getLength(); i++) {
		    rows.push(i);
		  }
		  grid.setSelectedRows(rows);
		  e.preventDefault();
		});
		grid.onSort.subscribe(function (e, args) {
		  sortdir = args.sortAsc ? 1 : -1;
		  sortcol = args.sortCol.field;
		  if ($.browser.msie && $.browser.version <= 8) {
		    // using temporary Object.prototype.toString override
		    // more limited and does lexicographic sort only by default, but can be much faster
		    var percentCompleteValueFn = function () {
		      var val = this["percentComplete"];
		      if (val < 10) {
		        return "00" + val;
		      } else if (val < 100) {
		        return "0" + val;
		      } else {
		        return val;
		      }
		    };
		    // use numeric sort of % and lexicographic for everything else
		    dataView.fastSort((sortcol == "percentComplete") ? percentCompleteValueFn : sortcol, args.sortAsc);
		  } else {
		    // using native sort with comparer
		    // preferred method but can be very slow in IE with huge datasets
		    dataView.sort(comparer, args.sortAsc);
		  }
		});
		// wire up model events to drive the grid
		dataView.onRowCountChanged.subscribe(function (e, args) {
		  grid.updateRowCount();
		  grid.render();
		});
		dataView.onRowsChanged.subscribe(function (e, args) {
		  grid.invalidateRows(args.rows);
		  grid.render();
		});
		
		dataView.onPagingInfoChanged.subscribe(function (e, pagingInfo) {
			/* var isLastPage = pagingInfo.pageNum == pagingInfo.totalPages - 1;
			var enableAddRow = isLastPage || pagingInfo.pageSize == 0;
			var options = grid.getOptions();
			if (options.enableAddRow != enableAddRow) {
			  grid.setOptions({enableAddRow: enableAddRow});
			} */
		});
		var h_runfilters = null;
		// wire up the slider to apply the filter to the model
		$("#pcSlider,#pcSlider2").slider({
		  "range": "min",
		  "slide": function (event, ui) {
		    Slick.GlobalEditorLock.cancelCurrentEdit();
		    if (percentCompleteThreshold != ui.value) {
		      window.clearTimeout(h_runfilters);
		      h_runfilters = window.setTimeout(updateFilter, 10);
		      percentCompleteThreshold = ui.value;
		    }
		  }
		});
		// wire up the search textbox to apply the filter to the model
		$("#txtSearch,#txtSearch2").keyup(function (e) {
		  Slick.GlobalEditorLock.cancelCurrentEdit();
		  // clear on Esc
		  if (e.which == 27) {
		    this.value = "";
		  }
		  searchString = this.value;
		  updateFilter();
		});
		function updateFilter() {
		  dataView.setFilterArgs({
		    percentCompleteThreshold: percentCompleteThreshold,
		    searchString: searchString
		  });
		  dataView.refresh();
		}
		$("#btnSelectRows").click(function () {
		  if (!Slick.GlobalEditorLock.commitCurrentEdit()) {
		    return;
		  }
		  var rows = [];
		  for (var i = 0; i < 10 && i < dataView.getLength(); i++) {
		    rows.push(i);
		  }
		  grid.setSelectedRows(rows);
		});
		// initialize the model after all the events have been hooked up
		dataView.beginUpdate();
		dataView.setPagingOptions({ pageSize: 25 }, false, false);
		//dataView.reloadData();
		//dataView.setItems(data);
		dataView.setFilterArgs({
		  percentCompleteThreshold: percentCompleteThreshold,
		  searchString: searchString
		});
		dataView.setFilter(myFilter);
		dataView.endUpdate();
		// if you don't want the items that are not visible (due to being filtered out
		// or being on a different page) to stay selected, pass 'false' to the second arg
		dataView.syncGridSelection(grid, true);
		$("#gridContainer").resizable();
	})

	function isCellEditable(row, cell, item) {
		//var addflag = eval('${QX.add}');
		// The last controll row can't be edited
		var innerData = dataView.getCurrentPageItems();
		if (row >= innerData.length) {
			return false;
		}
// 		alert(columns[cell]["readonly"]);
		
		// 不管有没有新增、编辑权限，NOT_EDIT列永远不能编辑
		// READOONLY columns can't be edited
		if (columns[cell]["readonly"] === NOT_EDIT) {
			return false;
		}
		// The new added row can be edited
		if (innerData[row]["dirty"] === '1') {
			// 新增状态，NOT_EDIT列不可以编辑，CAN_EDIT&MID_EDIT可以编辑
			/* if (columns[cell]["readonly"] === NOT_EDIT) {
				return false;
			} */
			return true;
		}
		// Has edit priviledge, can edit the row
		var editflag = eval('${QX.edit}');
		if (editflag == 1) {
			// 编辑状态，NOT_EDIT&MID_EDIT列不可以编辑，CAN_EDIT可以编辑
			if (columns[cell]["readonly"] === MID_EDIT) {
				return false;
			}
			return true;
		}
		return false;
	}
	function add() {
		if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		}
		var newRow = {"id": guid()};
		for (var i = 0; i < columns.length; i++ ) {
		    var column = columns[i];
		    // 注意有默认值填充默认值
		    // TODO
		    if (column['type'] == 'BOOL') {
			    newRow[column['field']] = 'false'; //column['field'];
		    } else {
		    	newRow[column['field']] = ''; //column['field'];
		    }
		}
	    newRow['dirty'] = '1';
	    newRow['num'] = dataView.getTotalAllRows() + 1;
		dataView.addItem(newRow);
		grid.setActiveCell(dataView.getLength() - 1, 1);
	}
    function addPopup() {
        if (typeof top.jzts === "function") {top.jzts();}
        if (!grid.getEditorLock().commitCurrentEdit()) {
            return false;
        }
        var TABLES_ID = '${TABLES_ID}';
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title ="新增";
        diag.URL = '/card/card.do?formEditFlag=preview&dataEditFlag=add&TABLES_ID=' + TABLES_ID;
        diag.Width = 800;
        diag.Height = 600;
        diag.CancelEvent = function(){ //关闭事件
            var changedData = diag.innerFrame.contentWindow.document.getElementById('USER_DATA').value;
            if (changedData != '') {
                var where = $("#WHERE").val();
                // 考虑那种方式, form还是ajax
                // form 需要将where传回
                //$("#Form").submit();
                dataView.reloadData(where);
            }
            diag.close();
        };
        diag.show();
    }
    function editPopup(dataId, row, userData) {
        if (typeof top.jzts === "function") {top.jzts();}
        if (!grid.getEditorLock().commitCurrentEdit()) {
            return false;
        }
        var TABLES_ID = '${TABLES_ID}';
//        var userDataStr = JSON.stringify(userData);
//        userDataStr = encodeURIComponent(userDataStr);
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title ="编辑";
        diag.URL = '/card/card.do?formEditFlag=preview&dataEditFlag=edit&TABLES_ID='
                + TABLES_ID + '&DATA_ID=' + dataId;
        diag.Width = 800;
        diag.Height = 600;
        diag.CancelEvent = function() { //关闭事件
            // 传回变动后的userData, 只刷新这一行数据
            var changedData = diag.innerFrame.contentWindow.document.getElementById('USER_DATA').value;
            if (changedData != '') {
                changedData = decodeURIComponent(changedData);
                changedData = JSON.parse(changedData);
                changedData['num'] = userData['num'];

                dataView.updateItem(dataId, changedData);
                grid.updateRow(row);
                // 更新当前行的join列
                var innerData = [];
                innerData[0] = changedData;
                refreshJoinColumns(innerData, row, row, '1');
            }
            diag.close();
        };
        diag.OnLoad = function() {
            var userDataStr = JSON.stringify(userData);
            diag.innerFrame.contentWindow.document.getElementById('ROW_DATA').value = userDataStr;
        };
        diag.show();
    }
    // 弹出字典
    function popupCardDict(param, row, cell, key, value, label, options) {
        if (typeof top.jzts === "function") {top.jzts();}
        param['label'] = label;
        param['showCount'] = 10;
        param['currentPage'] = 1;

        param['DICT_KEY'] = key;
        param['DICT_VALUE'] = value;

        var diag = new top.Dialog();
        diag.Drag=true;
        diag.Title ="编辑";
        diag.URL = '/card/goPopupDict.do?param='+encodeURIComponent(JSON.stringify(param));
        diag.Width = 800;
        diag.Height = 600;
        diag.CancelEvent = function(){ //关闭事件

            var dblClick = diag.innerFrame.contentWindow.document.getElementById('DBL_CLICK').value;
            if (dblClick == '' || dblClick == 'undefined') {
                diag.close();
                return;
            }
            var newKey = diag.innerFrame.contentWindow.document.getElementById('DICT_KEY').value;
            var newValue = diag.innerFrame.contentWindow.document.getElementById('DICT_VALUE').value
            if (newKey == null || newKey == '' || newKey == 'undefined') {
                bootbox.alert('请选择' + label);
                return;
            }
            if (newKey != key) {
                // 更新columns的定义
                options[newKey] = newValue;
                grid.updateColumnOptions(param['field'], options);
                // 触发jointext formatter
                var item = dataView.getItemByIdx(row);
                item[param['field']] = newKey;
                item['dirty'] = '2';
                dataView.updateItem(param['id'], item);
                grid.updateCell(row, cell);
            }
            diag.close();
        };
        diag.OKEvent = function() {
            var newKey = diag.innerFrame.contentWindow.document.getElementById('DICT_KEY').value;
            var newValue = diag.innerFrame.contentWindow.document.getElementById('DICT_VALUE').value
            if (newKey == null || newKey == '' || newKey == 'undefined') {
                bootbox.alert('请选择' + label);
                return;
            }
            if (newKey != key) {
                // 更新columns的定义
                options[newKey] = newValue;
                grid.updateColumnOptions(param['field'], options);
                // 触发jointext formatter
                var item = dataView.getItemByIdx(row);
                item[param['field']] = newKey;
                item['dirty'] = '2';
                dataView.updateItem(param['id'], item);
                grid.updateCell(row, cell);
            }
            diag.close();
        }
        diag.show();
    }
	// 0:普通模式
	// 1:LIST-LIST模式
	function getBasicUri() {
		var uri = '/list/';
		var LIST_LIST_MASTER = $("#LIST_LIST_MASTER").val();
		if (LIST_LIST_MASTER === '1' || LIST_LIST_MASTER === '0' || LIST_LIST_MASTER === '2') {	// master mode
			uri = '/listlist/';
		}
		return uri;
	}
	function save(METHOD,TRANS_ID,TABLES_ID,CORP_ID,DATA_DATE) {

		if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		}
		// add,edit,del都可以save
		var addflag = eval('${QX.add}');
		var editflag = eval('${QX.edit}');
		// 对于只能冲正的场景
		// 如果没有edit权限，但是有add权限
		if (editflag != 1) {
			bootbox.confirm("保存之后将不能修改, 确定要保存吗?", function(result) {
				if (result) {
					saveData(METHOD,TRANS_ID,TABLES_ID,CORP_ID,DATA_DATE);
				}
			});
		} else {
			return saveData(METHOD,TRANS_ID,TABLES_ID,CORP_ID,DATA_DATE);
		}
		return true;
	}

	function saveData(METHOD,TRANS_ID,TABLES_ID,CORP_ID,DATA_DATE) {

		var appended = [];
		var modified = [];
		var deleted  = [];
		var a = 0, m = 0; d = 0;
		var innerData = dataView.getCurrentPageItems();
		for (var i = 0; i < innerData.length; i++) {
			if (innerData[i]["dirty"] === '1') {
				appended[a++] = innerData[i];
			} else if (innerData[i]["dirty"] === '2') {
				modified[m++] = innerData[i];
			} else if (innerData[i]["dirty"] === '3') {
				deleted[d++] = innerData[i];
			}
		}
		if (appended.length == 0 && modified.length == 0 && deleted.length == 0) {
			bootbox.alert('没有数据变化');
			return false;
		}

		// Columns definitions
		var definitions = getColumnsDef();
		appended = JSON.stringify(appended);
		modified = JSON.stringify(modified);
		deleted  = JSON.stringify(deleted);
        var obj = {};
        obj['APPENDED'] = appended;
        obj['MODIFIED'] = modified;
        obj['DELETED'] = deleted;
        obj['TABLES_ID'] = TABLES_ID;
        // old ways
//		var param = "{" + "'APPENDED':'" + appended + "',"
//			+ "'MODIFIED':'" + modified + "',"
//			+ "'DELETED':'" + deleted + "',"
//			+ "'TABLES_ID':'" + TABLES_ID + "'}";
//    	var obj = eval('(' + param + ')');
		var url = getBasicUri()+'save.do?TRANS_ID='+TRANS_ID+'&CORP_ID='+CORP_ID+'&DATA_DATE='+DATA_DATE+'&tm='+new Date().getTime();
		url += "&COLUMNS_DEF=" + definitions;
		callAjax(url, true, obj, function(dt) {
			if (dt.success == true) {
				for (var i = innerData.length - 1; i >= 0; i--) {
					if (innerData[i]["dirty"] === '1' || innerData[i]["dirty"] === '2') {
						if (METHOD === 'DELETE') {
							// 删除模式，只操作删除的行
							continue;
						}
						innerData[i]["dirty"] = '0';
						/* var rowIndices = [];
						rowIndices[0] = i;
						var rowIds = dataView.mapRowsToIds(rowIndices);
						dataView.updateItem(rowIds[0].id, rowIds[0]); */
					} else if (innerData[i]["dirty"] === '3') {
						if (METHOD !== 'DELETE') {
							// 有后台删除失败的情况
							continue;
						}
						var rowIndices = [];
						rowIndices[0] = i;
						var rowIds = dataView.mapRowsToIds(rowIndices);
						dataView.deleteItem(rowIds[0]);
					}
				}
				grid.invalidate();

				bootbox.alert('操作成功！');
			} else {
				bootbox.alert(dt.msg);
			}
		});
	}
	function deleteRow(TRANS_ID,TABLES_ID,CORP_ID,DATA_DATE) {
		if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		}
		// Returns an array of row indices corresponding to the currently selected rows.
		var rowIndices = grid.getSelectedRows();
		if (rowIndices.length == 0) {
			bootbox.alert('没有选中行');
			return;
		}
		bootbox.confirm("确定要删除吗?", function(result) {
			if (result) {
                // if (typeof top.jzts === "function") {top.jzts();}
				var selectedRowIds = dataView.mapRowsToIds(rowIndices);
				var innerData = dataView.getCurrentPageItems();
				var item = innerData[rowIndices[0]];
				if (item['dirty'] == '1') {
					dataView.deleteItem(selectedRowIds[0]);
				} else {
					var item = dataView.getItemById(selectedRowIds[0]);
					innerData[rowIndices[0]]['dirty'] = '3';
					//dataView.setData(innerData);
	
					return save('DELETE',TRANS_ID,TABLES_ID,CORP_ID,DATA_DATE);
				}
			}
		});
	}
	function submit(TRANS_ID,TABLES_ID,CORP_ID,DATA_DATE,TYPE) {
		if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		}
		var innerData = dataView.getCurrentPageItems();
		for (var i = 0; i < innerData.length; i++) {
			if (innerData[i]["dirty"] === '1') {
				bootbox.alert("有新增数据，请先点击保存按钮.");
				return false;
			} else if (innerData[i]["dirty"] === '2') {
				bootbox.alert("有修改数据，请先点击保存按钮.");
				return false;
			} else if (innerData[i]["dirty"] === '3') {
				bootbox.alert("有删除数据，请先点击保存按钮.");
				return false;
			}
		}
		bootbox.confirm("确定要提交审核吗?", function(result) {
			if (result) {
				// 插入到B_TRANS表
				var url = getBasicUri()+'submit.do?tm='+new Date().getTime();
				var param = "{'TYPE':'" + TYPE + "','TRANS_ID':'" + TRANS_ID + "','TABLES_ID':'" + TABLES_ID + "','CORP_ID':'" + CORP_ID + "','DATA_DATE':'" + DATA_DATE + "'}";
		    	var obj = eval('(' + param + ')');
				callAjax(url, true, obj, function(dt) {
					if (dt.success == true) {
						bootbox.alert('提交审批成功！');
						$("#add").hide();
						$("#delete").hide();
						$("#save").hide();
						$("#submit").hide();
					} else {
						bootbox.alert(dt.msg);
					}
				});
			}
		});
	}
	//返回
	function goSondict(FROM, TRANS_ID, TABLES_ID) {
		if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		}
        if (typeof top.jzts === "function") {top.jzts();}
		var i = 0;
		window.location.href="/trans/listDaily.do?FROM="+FROM+"&TRANS_ID="+TRANS_ID+"&TABLES_ID="+TABLES_ID;
	}
	// 审批/驳回
	function approve(APPROVE,TRANS_ID,TYPE,COMMIT_FLAG,AUDIT_LEVEL,AUDIT_USER_LEVEL1,AUDIT_USER_LEVEL2,AUDIT_USER_LEVEL3){
		if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		}
		var msg = '';
		if (APPROVE == 'ok') {
			msg = '审核通过?';
		} else {
			msg = '驳回?';
		}
		bootbox.confirm(msg, function(result) {
		if (result) {
			var CORP_ID = $("#CORP_ID").val();
			var DATA_DATE = $("#DATA_DATE").val();
			var url = '/trans/approve.do?tm='+new Date().getTime();
			var param = "{'TYPE':'" + TYPE + "','TRANS_ID':'" + TRANS_ID + "','APPROVE':'" + APPROVE
				+ "','COMMIT_FLAG':'" + COMMIT_FLAG + "','AUDIT_LEVEL':'" + AUDIT_LEVEL 
				+ "','AUDIT_USER_LEVEL1':'" + AUDIT_USER_LEVEL1 + "','AUDIT_USER_LEVEL2':'" + AUDIT_USER_LEVEL2 
				+ "','AUDIT_USER_LEVEL3':'" + AUDIT_USER_LEVEL3 
				+ "','CORP_ID':'" + CORP_ID + "','DATA_DATE':'" + DATA_DATE
				+ "'}";
	    	var obj = eval('(' + param + ')');
			callAjax(url, true, obj, function(dt) {
				if (dt.success == true) {
					if (APPROVE == 'ok') {
						bootbox.alert('审核成功!');
					} else {
						bootbox.alert('驳回成功!');
					}
					$("#approve").hide();
					$("#deny").hide();
				} else {
					bootbox.alert(dt.msg);
				}
			});
		}});
	}
	// LIST-LIST模式，进入SLAVE列表
	function goDetailItem(LISTLISTRULE_ID, SLAVE_TABLES_ID, MASTER_COLUMNS_ID, MASTER_COLUMNS_VALUE,SOURCE_COLUMNS_NAME_EN,MASTER_ROW_ID_VALUE) {
		if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		}
		// 插入到B_TRANS表
        if (typeof top.jzts === "function") {top.jzts();}
		var definitions = getColumnsDef();
		var url = getBasicUri()+'slave.do?tm='+new Date().getTime();
		url += "&SLAVE_TABLES_ID=" + SLAVE_TABLES_ID + "&MASTER_COLUMNS_ID=" + MASTER_COLUMNS_ID;
		url += "&MASTER_COLUMNS_VALUE=" + MASTER_COLUMNS_VALUE + "&LISTLISTRULE_ID=" + LISTLISTRULE_ID;
		url += "&SOURCE_COLUMNS_NAME_EN=" + SOURCE_COLUMNS_NAME_EN + "&MASTER_ROW_ID_VALUE=" + MASTER_ROW_ID_VALUE;
		url += "&COLUMNS_DEF=" + definitions;
		var diag = new top.Dialog();
		diag.Drag=true;
		diag.Title ="编辑";
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

	// Open search panel
    function openDetails() {

        if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		}
        var $modal = $("<div class='item-details-form'></div>");
        $modal = $("#itemDetailsTemplate")
            .tmpl({
              context: grid.getDataItem(0),  // grid.getActiveCell().row
              columns: columns
            })
            .appendTo("body");
        $modal.keydown(function (e) {
          if (e.which == $.ui.keyCode.ENTER) {
            grid.getEditController().commitCurrentEdit();
            e.stopPropagation();
            e.preventDefault();
          } else if (e.which == $.ui.keyCode.ESCAPE) {
            grid.getEditController().cancelCurrentEdit();
            e.stopPropagation();
            e.preventDefault();
          }
        });
        $modal.find("[data-action=search]").click(function () {
          search();
          grid.getEditControllerForSearch().cancelCurrentEditForSearch();
        });
        $modal.find("[data-action=cancel]").click(function () {
          grid.getEditControllerForSearch().cancelCurrentEditForSearch();
        });
        var containers = $.map(columns, function (c) {
          return $modal.find("[data-editorid=" + c.id + "]");
        });
        var compositeEditor = new Slick.CompositeEditor(
            columns,
            containers,
            {
              destroy: function () {
                $modal.remove();
              }
            }
        );
        grid.editActiveCellForSearch(compositeEditor);
    }
    // Clicked search button
    function search() {
        //if (typeof top.jzts === "function") {top.jzts();}
    	var compositeEditor = grid.getCellEditorForSearch();
    	var serializedEditorValue = compositeEditor.serializeValue();
    	console.log(serializedEditorValue);
        var idx = 0;
        var editorIndex = 0;
    	var where = [];
    	var whereIndex = 0;
        while (idx < columns.length) {
          if (columns[idx].searchable && columns[idx].editor) {
        	var val = serializedEditorValue[editorIndex];
        	if (val !== null && val !== '' && val !== 'undefined'&& val !==undefined) {
    		  var item = (where[whereIndex++] = {});
    		  item["column"] = columns[idx];
    		  var value = {};
    		  value["value"] = val;
    		  item["value"] = value;
            }
  		    editorIndex ++;
          }
  		  idx ++;
        }
        console.log(where)
		where = JSON.stringify(where);
		$("#WHERE").val(where);
        // 考虑那种方式, form还是ajax
        // form 需要将where传回
		//$("#Form").submit();
        dataView.reloadData(where);
    }
    // Clear search
    function clearSearch() {
        if (typeof top.jzts === "function") {top.jzts();}
		$("#WHERE").val('');
        // 考虑那种方式, form还是ajax
        // form 需要将where传回
		$("#Form").submit();
    }
    // Set url&parameters for ajax request
    function setAjaxProperties() {
		var uri = 'ajaxlist.do';
		var LIST_LIST_MASTER = '${LIST_LIST_MASTER}';
		if (LIST_LIST_MASTER === '1' || LIST_LIST_MASTER === '2') {
			uri = 'ajax_ll_master.do';
		} else if (LIST_LIST_MASTER === '0') {	// master mode
			uri = 'ajax_ll_slave.do';
		}
		var COLUMNS_DEF = getColumnsDef();

		var fromAction = '${fromAction}';
		var TRANS_ID = '${TRANS_ID}';
		var CORP_ID = '${CORP_ID}';
		var TABLES_ID = '${TABLES_ID}';
		var DATA_DATE = '${DATA_DATE}';
		var SLAVE_TABLES_ID = '${SLAVE_TABLES_ID}';
		var COMMIT_FLAG = '${COMMIT_FLAG}';
		var IS_COMMIT_USER = '${IS_COMMIT_USER}';
		var MASTER_ROW_ID_VALUE = '${MASTER_ROW_ID_VALUE}';
        var LISTLISTRULE_ID = '${LISTLISTRULE_ID}';
        var MASTER_COLUMNS_ID = '${MASTER_COLUMNS_ID}';
        var MASTER_COLUMNS_VALUE = '${MASTER_COLUMNS_VALUE}';
        var MASTER_COLUMNS_NAME_EN = '${MASTER_COLUMNS_NAME_EN}';
        var SOURCE_COLUMNS_NAME_EN = '${SOURCE_COLUMNS_NAME_EN}';

		var url = getBasicUri()+uri+'?tm='+new Date().getTime();
		var params = "{'TRANS_ID':'" + TRANS_ID + "','CORP_ID':'" + CORP_ID + "','TABLES_ID':'" + TABLES_ID +
			"','DATA_DATE':'" + DATA_DATE + "','SLAVE_TABLES_ID':'" + SLAVE_TABLES_ID + "','COMMIT_FLAG':'" + COMMIT_FLAG +
			"','IS_COMMIT_USER':'" + IS_COMMIT_USER + "','LIST_LIST_MASTER':'" + LIST_LIST_MASTER +
			"','MASTER_ROW_ID_VALUE':'" + MASTER_ROW_ID_VALUE + "','LISTLISTRULE_ID':'" + LISTLISTRULE_ID +
			"','MASTER_COLUMNS_ID':'" + MASTER_COLUMNS_ID + "','MASTER_COLUMNS_NAME_EN':'" + MASTER_COLUMNS_NAME_EN +
			"','SOURCE_COLUMNS_NAME_EN':'" + SOURCE_COLUMNS_NAME_EN + "','MASTER_COLUMNS_VALUE':'" + MASTER_COLUMNS_VALUE +
			"','COLUMNS_DEF':'" + COLUMNS_DEF +
			"'}";
// 			alert(params)
    	var obj = eval('(' + params + ')');
    	
		dataView.setExtentProperties(url, obj);
    }
    function onFileUpload(ID, COLUMNS_ID, COLUMNS_VALUE, ROW_INDEX) {
		if (!grid.getEditorLock().commitCurrentEdit()) {
			return false;
		}
		if (COLUMNS_VALUE == null || COLUMNS_VALUE == 'undefined') {
			COLUMNS_VALUE = '';
		}
        COLUMNS_VALUE = encodeURI(COLUMNS_VALUE);
        var TABLES_ID = '${TABLES_ID}';
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
			//if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none') {
			//	nextPage(${page.currentPage});
			//}
			var UPLOAD_FILE_NAME = diag.innerFrame.contentWindow.document.getElementById('UPLOAD_FILE_NAME').value;
			if (UPLOAD_FILE_NAME != '') {
		        var innerData = dataView.getCurrentPageItems();
		        innerData[ROW_INDEX][COLUMNS_ID] = UPLOAD_FILE_NAME;		// 刷新上传文档的名称和url
				grid.invalidateRow(ROW_INDEX);
				grid.render();
			}
			diag.close();
		};
		diag.show();
		return true;
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
    function onFileDownload(type, fid) {
    	var url = '/files/video/download.do?tm=' + new Date().getTime();
    	$.download(url, 'GET', type, fid);
    }
    function onFilePreview(type, fid) {
		// play the video or preview the file
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
    function getColumnsDef() {
		var columnsDef = {};
		for (var idx = 0; idx < columns.length; idx++) {
			var needed = false;
			var encrypt = columns[idx]['encrypt'];
			var join_condition = columns[idx]['join_condition'];
  		    var value = {};
  		    // 加密字段
        	if (encrypt == '1') {
    		  value["encrypt"] = encrypt;
    		  needed = true;
            } else {
              value["encrypt"] = '0';
            }
        	// 内联字段
        	if (join_condition == '0') {
				value["join_condition"] = join_condition;
				value["join_table_name"] = columns[idx]['join_table_name'];
				value["join_columns_id"] = columns[idx]["join_columns_id"];			// 内联用
				value["join_select_table_column"] = columns[idx]['join_select_table_column'];
				value["master_table_name"] = columns[idx]['master_table_name'];
				value["master_table_column"] = columns[idx]['master_table_column'];
				value["corp_id"] = columns[idx]['corp_id'];
				needed = true;
        	}
        	if (needed == true) {
      		    columnsDef[columns[idx]['field']] = value;
        	}
		}
		var definitions = JSON.stringify(columnsDef);
        definitions = encodeURIComponent(definitions); // fixed bug:400 bad request
		return definitions;
    }
    function parseDataSource(dataSource) {
        var arr = JSON.parse(dataSource);
        var options = {};
        for (var i = 0; i < arr.length; i++) {
            options[arr[i]['BIANMA']] = arr[i]['NAME'];
        }
        return options;
    }
    function refreshJoinColumns(innerData, from, to, oneCell) {
        for (var i = 0; i < columns.length && innerData.length > 0; i++) {
            var item = columns[i];
            if (item['join_condition'] !== '1') {
                continue;
            }
            var colName = item['master_table_column'];
            var inStr = '';
            var j = 0;
            for (j = 0; j < innerData.length; j++) {
                var value = innerData[j][colName];
                inStr += "'";
                inStr += value;
                if (j == innerData.length - 1) {
                    inStr += "'";
                } else {
                    inStr += "',";
                }
            }
            var param = {
                'tablesId':item["join_table_name"],
                'corpId':item["corp_id"],
                'joinColumnsId':item["join_table_column"],
                'selectColumnsId':item["join_select_table_column"],
                'in':inStr,
                'reserved':JSON.stringify({'id':item['id'],'cell':i,'oneCell':oneCell,'options':columns[i]["options"]})  // returned by backend
            };
            var url = item["joinurl"];
            callAjax(url, true, param, function (dt) {
                if (dt.success == true) {
                    var reserved = JSON.parse(dt.reserved);
                    var newOptions = eval(dt.data);
                    var options;
                    if (oneCell == '1') {
                        options = reserved['options'];
                        for(var attr in newOptions){
                            options[attr] = newOptions[attr];
                        }
                    } else {
                        options = newOptions;
                    }

                    // 更新columns的定义
                    grid.updateColumnOptions(reserved['id'], options);
                    // 触发jointext formatter
                    for (var k = from; k <= to; k++) {
                        grid.updateCell(k, reserved['cell']);
                    }
                }
            });
        }
    }
    function Excelto() {
    	bootbox.alert('实现中, 敬请期待.');
	}
    function toExcel(fromAction,FROM,TABLES_ID) {
<%--     	window.location.href='/'+fromAction+'Excel?FROM='+${FROM}+'&TABLES_ID='+${TABLES_ID};   --%>
<%--     	'/trans/listDailyExcel.do?FROM='+FROM+'&TABLES_ID='+TABLES_ID; --%>

		var uri = 'ajaxlistExcel.do';
		var LIST_LIST_MASTER = '${LIST_LIST_MASTER}';
		if (LIST_LIST_MASTER === '1' || LIST_LIST_MASTER === '2') {
			uri = 'ajax_ll_masterExcel.do';
		} else if (LIST_LIST_MASTER === '0') {	// master mode
			uri = 'ajax_ll_slaveExcel.do';
		}
		var COLUMNS_DEF = getColumnsDef();
		
		var fromAction = '${fromAction}';
		var TRANS_ID = '${TRANS_ID}';
		var CORP_ID = '${CORP_ID}';
		var TABLES_ID = '${TABLES_ID}';
		var DATA_DATE = '${DATA_DATE}';
		var SLAVE_TABLES_ID = '${SLAVE_TABLES_ID}';
		var COMMIT_FLAG = '${COMMIT_FLAG}';
		var IS_COMMIT_USER = '${IS_COMMIT_USER}';
		var MASTER_ROW_ID_VALUE = '${MASTER_ROW_ID_VALUE}';
		var LISTLISTRULE_ID = '${LISTLISTRULE_ID}';
		var MASTER_COLUMNS_ID = '${MASTER_COLUMNS_ID}';
		var MASTER_COLUMNS_VALUE = '${MASTER_COLUMNS_VALUE}';
		var MASTER_COLUMNS_NAME_EN = '${MASTER_COLUMNS_NAME_EN}';
		var SOURCE_COLUMNS_NAME_EN = '${SOURCE_COLUMNS_NAME_EN}';
		
		var url = getBasicUri()+uri+'?tm='+new Date().getTime();
		var params = "{'TRANS_ID':'" + TRANS_ID + "','CORP_ID':'" + CORP_ID + "','TABLES_ID':'" + TABLES_ID +
			"','DATA_DATE':'" + DATA_DATE + "','SLAVE_TABLES_ID':'" + SLAVE_TABLES_ID + "','COMMIT_FLAG':'" + COMMIT_FLAG +
			"','IS_COMMIT_USER':'" + IS_COMMIT_USER + "','LIST_LIST_MASTER':'" + LIST_LIST_MASTER +
			"','MASTER_ROW_ID_VALUE':'" + MASTER_ROW_ID_VALUE + "','LISTLISTRULE_ID':'" + LISTLISTRULE_ID +
			"','MASTER_COLUMNS_ID':'" + MASTER_COLUMNS_ID + "','MASTER_COLUMNS_NAME_EN':'" + MASTER_COLUMNS_NAME_EN +
			"','SOURCE_COLUMNS_NAME_EN':'" + SOURCE_COLUMNS_NAME_EN + "','MASTER_COLUMNS_VALUE':'" + MASTER_COLUMNS_VALUE +
			"','COLUMNS_DEF':'" + COLUMNS_DEF +
			"'}";
		var obj = eval('(' + params + ')');
		var params = $.param(obj);
    	document.location.href = getBasicUri()+uri+'?tm='+new Date().getTime()+'&' + params;
	}
</script>

<!-- <div class='item-details-editor-container' data-editorid='@{column.id}'></div> -->
<script id="itemDetailsTemplate" type="text/x-jquery-tmpl">
    <div class='item-details-form'>
        {{each(i,column) columns}}
        {{if column.searchable==true }}
		<div>
        	<span class='item-details-label'>
            	@{column.name}
       		 </span>
        	{{if column.joinurl }} 
       			 <span class='' data-editorid='@{column.id}'></span>
        	{{else}}
       			 <span class='item-details-editor-container' data-editorid='@{column.id}'></span>
        	{{/if}}
		</div>
        {{/if}}
        {{/each}}

        <hr/>
        <div class='item-details-form-buttons'>
            <button data-action='search'>查找</button>
            <button data-action='cancel'>关闭</button>
        </div>
    </div>
</script>
<!-- 			{{else column.type=='DATE'}} -->
<!--        			 <span class='item-details-editor-container' data-editorid='@{column.id}'></span> -->
</body>
</html>