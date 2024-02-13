/***
 * Contains basic SlickGrid formatters.
 * 
 * NOTE:  These are merely examples.  You will most likely need to implement something more
 *        robust/extensible/localizable/etc. for your use!
 * 
 * @module Formatters
 * @namespace Slick
 */

(function ($) {
  // register namespace
  $.extend(true, window, {
    "Slick": {
      "Formatters": {
        "JoinText": JoinTextFormatter,
        "PercentComplete": PercentCompleteFormatter,
        "PercentCompleteBar": PercentCompleteBarFormatter,
        "DateTime": DateTimeFormatter,
        "YesNo": YesNoFormatter,
        "SelectFormatter": SelectFormatter,
        "Float": FloatFormatter,
        "Password": PasswordFormatter,
        "Color": ColorFormatter,
        "Checkmark": CheckmarkFormatter,
        "CheckboxGroup": CheckboxGroupFormatter,
        "RadioGroup": RadioGroupFormatter,
        "Button": ButtonFormatter,
        "ButtonUDP": ButtonUDPFormatter,
        "ButtonUD": ButtonUDFormatter
      }
    }
  });

  function JoinTextFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "" || value == "undefined") {
      return "";
    }
    return columnDef.options[value];
  }

  function PercentCompleteFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "") {
      return "-";
    } else if (value < 50) {
      return "<span style='color:red;font-weight:bold;'>" + value + "%</span>";
    } else {
      return "<span style='color:green'>" + value + "%</span>";
    }
  }

  function PercentCompleteBarFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "") {
      return "";
    }

    var color;

    if (value < 30) {
      color = "red";
    } else if (value < 70) {
      color = "silver";
    } else {
      color = "green";
    }

    return "<span class='percent-complete-bar' style='background:" + color + ";width:" + value + "%'></span>";
  }

  function YesNoFormatter(row, cell, value, columnDef, dataContext) {
    return value ? "Yes" : "No";
  }

  function CheckmarkFormatter(row, cell, value, columnDef, dataContext) {
    // Modified by winbill. for true/false string stored in db
    //defaultValue = !!item[args.column.field];
    var boolValue = (value === 'true' || value === true);
    return boolValue ? "<img src='static/slickgrid/images/tick.png'>" : "";
  }
  // Winbill ==>
  function DateTimeFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "" || value == "undefined") {
      return '';
    }
    // 2017-03-04 02:56:04
    if (typeof(value) == "string") {
    	return value;
    }
    // Object from datetimepicker
    var date = new Date();
    date.setTime(value.time);

    var formatTime = $.datepicker.formatTime('HH:mm:ss', value, {});
    var formatDate = $.datepicker.formatDate('yy-mm-dd', date, null);

    date = null;
    return formatDate + ' ' + formatTime;
/*
	var date = value;
    // Thu Oct 22 2015 15:48:56 GMT+0800
    var y = date.getFullYear();
    var m = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date
            .getMonth() + 1;
    var d = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var h = date.getHours() > 9 ? date.getHours() : '0' + date.getHours();
    var mm = date.getMinutes() > 9 ? date.getMinutes() : '0'
            + date.getMinutes();
    var s = date.getSeconds() > 9 ? date.getSeconds() : '0' + date.getSeconds();
    return y + '-' + m + '-' + d + ' ' + h + ":" + mm + ":" + s;
*/
  }
  function FloatFormatter(row, cell, value, columnDef, dataContext) {
	  if (value == null || value === "" || value == "undefined") {
	      return "0.00";
	  }
	  var nDecimals =  columnDef.precision;
	  if (nDecimals == null || nDecimals === "" || nDecimals == "undefined") {
		  nDecimals = 2;
	  }
	  //return Number(value).toFixed(nDecimals);
	  var num = value + '';
	  num += '';  
		var bMinus = false;
		if (startWith(num, '-')) {
			bMinus = true;
		}
	    num = num.replace(/[^0-9|\.]/g, ''); //清除字符串中的非数字非.字符  
	      
	    if(/^0+/){ //清除字符串开头的0  
	        num = num.replace(/^0+/, '');  
	    }
	    if(!/\./.test(num)){ //为整数字符串在末尾添加.00  
	        num += '.00';  
	    }
	    if(/^\./.test(num)){ //字符以.开头时,在开头添加0  
	        num = '0' + num;
	    }
	    //num = Math.round(num*100)/100;  //四舍五入的格式保留2位小数
	    num = Number(num).toFixed(nDecimals);
	    if (bMinus) {
	    	num = '-' + num;
	    }
		return num;
  }
  function SelectFormatter(row, cell, value, columnDef, dataContext) {
	  if (value == null || value === "" || value == "undefined") {
	      return "";
	  }
    return columnDef.options[value];
  }
  function ButtonFormatter(row, cell, value, columnDef, dataContext) {
	  var input = "<input type='button' class='show-detail-item' value='" + columnDef['name'] + "' id='clickBtn1'/>";
      return input;
  }
  function ButtonUDPFormatter(row, cell, value, columnDef, dataContext) {
	  var fileName = '';
	  if (value != null && value != '' && value != 'undefined') {
		  fileName = value.split("|", 1);
	  }
	  var input = "<input type='button' class='attach-file-upload' value='上传' id='clickBtnUpload'/>";
	  input += "<input type='button' class='attach-file-download' value='下载' id='clickBtnDownload'/>";
	  input += "<input type='button' class='attach-file-preview' value='预览' id='clickBtnPreview'/>";
	  input += "<label>" + fileName + "</label>";
      return input;
  }
  function ButtonUDFormatter(row, cell, value, columnDef, dataContext) {
	  var input = "<input type='button' class='attach-file-upload' value='上传' id='clickBtnUpload'/>";
	  input += "<input type='button' class='attach-file-download' value='下载' id='clickBtnDownload'/>";
      return input;
  }
  function CheckboxGroupFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "" || typeof(value) == "undefined") {
      return "";
    }
    var show = '';
    var arr = value;
    // 从数据库进入的数据,array也是字符串形式
    if (value instanceof Array) {
      arr = value;
    } else if (typeof(value) == "string" || typeof(value) == "number") {
      // 判断是否为数组
      value = value + '';
      try {
        arr = JSON.parse(value);
      } catch (e) {
        // 单个字符串
        var tmp = [];
        tmp[0] = value;
        arr = tmp;
      }
    }
    $.each(columnDef.options, function(key, value) {
      for (var index = 0; index < arr.length; index++) {
        if (arr[index] == key) {
          show += value;
          show += ',';
        }
      }
    });
    if (show.length > 0) {
      show = show.substring(0, show.length - 1);
    }
    return show;
    //return columnDef.options[value];
  }
  function RadioGroupFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "" || value == "undefined") {
      return "";
    }
    return columnDef.options[value];
  }
  function PasswordFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "" || typeof(value) == "undefined") {
      return "";
    }
    var input = "<input type='password' value='" + value + "' style='border: none' />";
    return input;
  }
  function ColorFormatter(row, cell, value, columnDef, dataContext) {
    if (value == null || value === "" || typeof(value) == "undefined") {
      return "";
    }
    var input = "<input type='text' class='editor-color' value='' style='background:" + value + "' />";
    return input;
  }

})(jQuery);
