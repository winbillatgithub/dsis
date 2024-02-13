/***
 * Contains basic SlickGrid editors.
 * @module Editors
 * @namespace Slick
 */

(function ($) {
  // register namespace
  $.extend(true, window, {
    "Slick": {
      "Editors": {
        "Text": TextEditor,
        "JoinText": JoinTextEditor,
        "Color": ColorEditor,
        "Password": PasswordEditor,
        "Integer": IntegerEditor,
		"Float": FloatEditor,
        "Date": DateEditor,
        "DateTime": DateTimeEditor,
        "YesNoSelect": YesNoSelectEditor,
        "Checkbox": CheckboxEditor,
        "CheckboxGroup": CheckboxGroupEditor,
        "RadioGroup": RadioGroupEditor,
        "PercentComplete": PercentCompleteEditor,
        "LongText": LongTextEditor,
        "Select": SelectEditor,
        "Label": LabelEditor
      }
    }
  });

  function TextEditor(args) {
    var $input;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $input = $("<INPUT type=text class='editor-text' />")
          .appendTo(args.container)
          .on("keydown.nav", function (e) {
            if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
              e.stopImmediatePropagation();
            }
          })
          .focus()
          .select();
    };

    this.destroy = function () {
      $input.remove();
    };

    this.focus = function () {
      $input.focus();
    };

    this.getValue = function () {
      return $input.val();
    };

    this.setValue = function (val) {
      $input.val(val);
    };

    this.loadValue = function (item) {
      defaultValue = item[args.column.field] || "";
      $input.val(defaultValue);
      $input[0].defaultValue = defaultValue;
      $input.select();
    };

    this.serializeValue = function () {
      return $input.val();
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  function JoinTextEditor(args) {
    var $input;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $input = $("<INPUT type=text class='joineditor-text' />")
        .appendTo(args.container)
        .on("keydown.nav", function (e) {
          if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
            e.stopImmediatePropagation();
          }
        })
        .focus()
        .select();
    };

    this.destroy = function () {
      $input.remove();
    };

    this.focus = function () {
      $input.focus();
    };

    this.getValue = function () {
      return $input.val();
    };

    this.setValue = function (val) {
      $input.val(val);
    };

    this.loadValue = function (item) {
      defaultValue = item[args.column.field] || "";
      $input.val(defaultValue);
      $input[0].defaultValue = defaultValue;
      $input.select();
    };

    this.serializeValue = function () {
      return $input.val();
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }
  /*
   * type color only supported by html5
   */
  function ColorEditor(args) {
    var $input;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $input = $("<INPUT type='color' class='editor-text' />")
        .appendTo(args.container)
        .on("keydown.nav", function (e) {
          if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
            e.stopImmediatePropagation();
          }
        })
        .focus()
        .select();
    };

    this.destroy = function () {
      $input.remove();
    };

    this.focus = function () {
      $input.focus();
    };

    this.getValue = function () {
      return $input.val();
    };

    this.setValue = function (val) {
      $input.val(val);
    };

    this.loadValue = function (item) {
      defaultValue = item[args.column.field] || "";
      $input.val(defaultValue);
      $input[0].defaultValue = defaultValue;
      $input.select();
    };

    this.serializeValue = function () {
      return $input.val();
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  function PasswordEditor(args) {
    var $input;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $input = $("<INPUT type=password class='editor-text' />")
        .appendTo(args.container)
        .on("keydown.nav", function (e) {
          if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
            e.stopImmediatePropagation();
          }
        })
        .focus()
        .select();
    };

    this.destroy = function () {
      $input.remove();
    };

    this.focus = function () {
      $input.focus();
    };

    this.getValue = function () {
      return $input.val();
    };

    this.setValue = function (val) {
      $input.val(val);
    };

    this.loadValue = function (item) {
      defaultValue = item[args.column.field] || "";
      $input.val(defaultValue);
      $input[0].defaultValue = defaultValue;
      $input.select();
    };

    this.serializeValue = function () {
      return $input.val();
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  function IntegerEditor(args) {
    var $input;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $input = $("<INPUT type=text class='editor-text' />");

      $input.on("keydown.nav", function (e) {
        if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
          e.stopImmediatePropagation();
        }
      });

      $input.appendTo(args.container);
      $input.focus().select();
    };

    this.destroy = function () {
      $input.remove();
    };

    this.focus = function () {
      $input.focus();
    };

    this.loadValue = function (item) {
      defaultValue = item[args.column.field];
      $input.val(defaultValue);
      $input[0].defaultValue = defaultValue;
      $input.select();
    };

    this.serializeValue = function () {
      return parseInt($input.val(), 10) || 0;
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (isNaN($input.val())) {
        return {
          valid: false,
          msg: "Please enter a valid integer"
        };
      }

      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  function FloatEditor(args) {
    var $input;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $input = $("<INPUT type=text class='editor-text' />");

      $input.on("keydown.nav", function (e) {
        if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
          e.stopImmediatePropagation();
        }
      });

      $input.appendTo(args.container);
      $input.focus().select();
    };

    this.destroy = function () {
      $input.remove();
    };

    this.focus = function () {
      $input.focus();
    };

	function getDecimalPlaces() {
		// returns the number of fixed decimal places or null
		var rtn = args.column.editorFixedDecimalPlaces;
		if (typeof rtn == 'undefined') {
			rtn = FloatEditor.DefaultDecimalPlaces;
		}
		return (!rtn && rtn!==0 ? null : rtn);
	}

    this.loadValue = function (item) {
      defaultValue = item[args.column.field];

	  var decPlaces = getDecimalPlaces();
	  if (decPlaces !== null
	  && (defaultValue || defaultValue===0)
	  && defaultValue.toFixed) {
		defaultValue = defaultValue.toFixed(decPlaces);
	  }

      $input.val(defaultValue);
      $input[0].defaultValue = defaultValue;
      $input.select();
    };

    this.serializeValue = function () {
	  var rtn = parseFloat($input.val()) || 0;

	  var decPlaces = getDecimalPlaces();
	  if (decPlaces !== null
	  && (rtn || rtn===0)
	  && rtn.toFixed) {
		rtn = parseFloat(rtn.toFixed(decPlaces));
	  }

      return rtn;
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (isNaN($input.val())) {
        return {
          valid: false,
          msg: "Please enter a valid number"
        };
      }

      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  FloatEditor.DefaultDecimalPlaces = null;
  function writeObj(obj){ 
	  var description = ""; 
	  for(var i in obj){ 
	  var property=obj[i]; 
	  	if(Array.isArray(property)){
	  		description+=i+" = "+writeObj(property)+"\n"; 
	  	}else{
	  		description+=i+" = "+property+"\n"; 
	  	}
	  } 
	  alert(description); 
	 }
  function DateEditor(args) {
//	  console.log(args);
//	  alert(args.cancelChanges.name)
	  
    var $input;
    var $input1;
    var defaultValue;
    var scope = this;
    var calendarOpen = false;
    this.init = function () {
    	if(args.cancelChanges.name=='noop'){
    		$input = $("<INPUT type=text class='editor-text1' />");
    		$input.appendTo(args.container);
  	      $input.focus().select();
  	      $input.datepicker({
  	        showOn: "button",
  	        dateFormat: "yy-mm-dd", // winbill, 20170303
  	        buttonImageOnly: true,
  	        buttonImage: "/static/slickgrid/images/calendar.gif",  // winbill
  	        beforeShow: function () {
  	          calendarOpen = true
  	        },
  	        onClose: function () {
  	          calendarOpen = false
  	        }
  	      });
  	      $input.width($input.width() - 18);
	  	    $input1 = $("<INPUT type=text class='editor-text1' />");
	  	    if($input1){
		  	    $input1.appendTo(args.container);
			      $input1.focus().select();
			      $input1.datepicker({
			        showOn: "button",
			        dateFormat: "yy-mm-dd", // winbill, 20170303
			        buttonImageOnly: true,
			        buttonImage: "/static/slickgrid/images/calendar.gif",  // winbill
			        beforeShow: function () {
			          calendarOpen = true
			        },
			        onClose: function () {
			          calendarOpen = false
			        }
			      });
			      $input1.width($input1.width() - 18);
	  	    }
    	}else{
    		$input = $("<INPUT type=text class='editor-text' />");
    		$input.appendTo(args.container);
    	      $input.focus().select();
    	      $input.datepicker({
    	        showOn: "button",
    	        dateFormat: "yy-mm-dd", // winbill, 20170303
    	        buttonImageOnly: true,
    	        buttonImage: "/static/slickgrid/images/calendar.gif",  // winbill
    	        beforeShow: function () {
    	          calendarOpen = true
    	        },
    	        onClose: function () {
    	          calendarOpen = false
    	        }
    	      });
    	      $input.width($input.width() - 18);
    	}
      
    };

    this.destroy = function () {
      $.datepicker.dpDiv.stop(true, true);
      $input.datepicker("hide");
      $input.datepicker("destroy");
      $input.remove();
    };

    this.show = function () {
      if (calendarOpen) {
        $.datepicker.dpDiv.stop(true, true).show();
      }
    };

    this.hide = function () {
      if (calendarOpen) {
        $.datepicker.dpDiv.stop(true, true).hide();
      }
    };

    this.position = function (position) {
      if (!calendarOpen) {
        return;
      }
      $.datepicker.dpDiv
          .css("top", position.top + 30)
          .css("left", position.left);
    };

    this.focus = function () {
      $input.focus();
    };

    this.loadValue = function (item) {
      defaultValue = item[args.column.field];
      $input.val(defaultValue);
      $input[0].defaultValue = defaultValue;
      $input.select();
    };

    this.serializeValue = function () {
    	if(!$input1){
    		return $input.val()
    	}else{
    		if ($input.val()=="" && $input1.val()==""){
    			return "";
    		}
//    		return "'"+($input.val()==''?'0000-01-01':$input.val())+"' and '"+($input1.val()==''?'3000-01-01':$input1.val())+"'";
    		return ($input.val()==''?'0000-01-01':$input.val())+","+($input1.val()==''?'3000-01-01':$input1.val());
    	}
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  function DateTimeEditor(args) {
    var $input;
    var $input1;
    var defaultValue;
    var scope = this;
    var calendarOpen = false;

    this.init = function () {
    	if(args.cancelChanges.name=='noop'){
    		$input = $("<INPUT type=text class='editor-text1' />");
    		$input.appendTo(args.container);
    	      $input.focus().select();
    	      $input.datetimepicker({
    	        showOn: "button",
    	        dateFormat: "yy-mm-dd", // winbill, 20170303
    	        timeFormat: 'hh:mm:ss',
    	        controlType: 'select',
    	        oneLine: true,
    	        buttonImageOnly: true,
    	        buttonImage: "/static/slickgrid/images/calendar.gif",  // winbill
    	        beforeShow: function () {
    	          calendarOpen = true
    	        },
    	        onClose: function () {
    	          calendarOpen = false
    	        }
    	      });
    	      $input.width($input.width() - 18);
    	    $input1 = $("<INPUT type=text class='editor-text1' />");
    	    if($input1){
	      		$input1.appendTo(args.container);
	      	      $input1.focus().select();
	      	      $input1.datetimepicker({
	      	        showOn: "button",
	      	        dateFormat: "yy-mm-dd", // winbill, 20170303
	      	        timeFormat: 'hh:mm:ss',
	      	        controlType: 'select',
	      	        oneLine: true,
	      	        buttonImageOnly: true,
	      	        buttonImage: "/static/slickgrid/images/calendar.gif",  // winbill
	      	        beforeShow: function () {
	      	          calendarOpen = true
	      	        },
	      	        onClose: function () {
	      	          calendarOpen = false
	      	        }
	      	      });
	      	      $input1.width($input1.width() - 18);
    	    }
    	}else{
    		$input = $("<INPUT type=text class='editor-text' />");
    		$input.appendTo(args.container);
    	      $input.focus().select();
    	      $input.datetimepicker({
    	        showOn: "button",
    	        dateFormat: "yy-mm-dd", // winbill, 20170303
    	        timeFormat: 'hh:mm:ss',
    	        controlType: 'select',
    	        oneLine: true,
    	        buttonImageOnly: true,
    	        buttonImage: "/static/slickgrid/images/calendar.gif",  // winbill
    	        beforeShow: function () {
    	          calendarOpen = true
    	        },
    	        onClose: function () {
    	          calendarOpen = false
    	        }
    	      });
    	      $input.width($input.width() - 18);
    	}
      
    };

    this.destroy = function () {
      $.datepicker.dpDiv.stop(true, true);
      $input.datetimepicker("hide");
      $input.datetimepicker("destroy");
      $input.remove();
    };

    this.show = function () {
      if (calendarOpen) {
        $.datepicker.dpDiv.stop(true, true).show();
      }
    };

    this.hide = function () {
      if (calendarOpen) {
        $.datepicker.dpDiv.stop(true, true).hide();
      }
    };

    this.position = function (position) {
      if (!calendarOpen) {
        return;
      }
      $.datepicker.dpDiv
          .css("top", position.top + 30)
          .css("left", position.left);
    };

    this.focus = function () {
      $input.focus();
    };

    this.loadValue = function (item) {
      defaultValue = item[args.column.field];
      $input.val(defaultValue);
      $input[0].defaultValue = defaultValue;
      $input.select();
    };

    this.serializeValue = function () {
    	if(!$input1){
    		return $input.val()
    	}else{
    		if ($input.val()=="" && $input1.val()==""){
    			return "";
    		}
//    		return "'"+($input.val()==''?'0000-01-01':$input.val())+"' and '"+($input1.val()==''?'3000-01-01':$input1.val())+"'";
    		return ($input.val()==''?'0000-01-01':$input.val())+","+($input1.val()==''?'3000-01-01':$input1.val());
    	}
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }
  
  
  
  
  function DateTimeEditor1(args) {
	    var $input;
	    var $input1;
	    var defaultValue;
	    var scope = this;
	    var calendarOpen = false;

	    this.init = function () {
	    	if(args.cancelChanges.name=='noop'){
	    		$input = $("<INPUT type=text class='editor-text1' />");
	    		$input.appendTo(args.container);
	    	      $input.focus().select();
	    	      $input.datetimepicker({
	    	        showOn: "button",
	    	        dateFormat: "yy-mm-dd", // winbill, 20170303
	    	        timeFormat: 'hh:mm:ss',
	    	        controlType: 'select',
	    	        oneLine: true,
	    	        buttonImageOnly: true,
	    	        buttonImage: "/static/slickgrid/images/calendar.gif",  // winbill
	    	        beforeShow: function () {
	    	          calendarOpen = true
	    	        },
	    	        onClose: function () {
	    	          calendarOpen = false
	    	        }
	    	      });
	    	      $input.width($input.width() - 18);
	    	    $input1 = $("<INPUT type=text class='editor-text1' />");
	    	    if($input1){
		      		$input1.appendTo(args.container);
		      	      $input1.focus().select();
		      	      $input1.datetimepicker({
		      	        showOn: "button",
		      	        dateFormat: "yy-mm-dd", // winbill, 20170303
		      	        timeFormat: 'hh:mm:ss',
		      	        controlType: 'select',
		      	        oneLine: true,
		      	        buttonImageOnly: true,
		      	        buttonImage: "/static/slickgrid/images/calendar.gif",  // winbill
		      	        beforeShow: function () {
		      	          calendarOpen = true
		      	        },
		      	        onClose: function () {
		      	          calendarOpen = false
		      	        }
		      	      });
		      	      $input1.width($input1.width() - 18);
	    	    }
	    	}else{
	    		$input = $("<INPUT type=text class='editor-text' />");
	    		$input.appendTo(args.container);
	    	      $input.focus().select();
	    	      $input.datetimepicker({
	    	        showOn: "button",
	    	        dateFormat: "yy-mm-dd",
	    	        timeFormat: 'hh:mm:ss',
	    	        controlType: 'select',
	    	        oneLine: true,
	    	        buttonImageOnly: true,
	    	        buttonImage: "/static/slickgrid/images/calendar.gif",  // winbill
	    	        beforeShow: function () {
	    	          calendarOpen = true
	    	        },
	    	        onClose: function () {
	    	          calendarOpen = false
	    	        }
	    	      });
	    	      $input.width($input.width() - 18);
	    	}
	      
	    };

	    this.destroy = function () {
	      $.datepicker.dpDiv.stop(true, true);
	      $input.datetimepicker("hide");
	      $input.datetimepicker("destroy");
	      $input.remove();
	    };

	    this.show = function () {
	      if (calendarOpen) {
	        $.datepicker.dpDiv.stop(true, true).show();
	      }
	    };

	    this.hide = function () {
	      if (calendarOpen) {
	        $.datepicker.dpDiv.stop(true, true).hide();
	      }
	    };

	    this.position = function (position) {
	      if (!calendarOpen) {
	        return;
	      }
	      $.datepicker.dpDiv
	          .css("top", position.top + 30)
	          .css("left", position.left);
	    };

	    this.focus = function () {
	      $input.focus();
	    };

	    this.loadValue = function (item) {
	      defaultValue = item[args.column.field];
	      $input.val(defaultValue);
	      $input[0].defaultValue = defaultValue;
	      $input.select();
	    };

	    this.serializeValue = function () {
	    	if(!$input1){
	    		return $input.val()
	    	}else{
	    		if ($input.val()=="" && $input1.val()==""){
	    			return "";
	    		}
//	    		return ""+($input.val()==''?'0000-01-01':$input.val())+" and "+($input1.val()==''?'3000-01-01':$input1.val())+"";
	    		return ($input.val()==''?'0000-01-01':$input.val())+","+($input1.val()==''?'3000-01-01':$input1.val());
	    	}
	    };

	    this.applyValue = function (item, state) {
	      item[args.column.field] = state;
	    };

	    this.isValueChanged = function () {
	      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
	    };

	    this.validate = function () {
	      if (args.column.validator) {
	        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
	        if (!validationResults.valid) {
	          return validationResults;
	        }
	      }

	      return {
	        valid: true,
	        msg: null
	      };
	    };

	    this.init();
	  }

  function YesNoSelectEditor(args) {
    var $select;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $select = $("<SELECT tabIndex='0' class='editor-yesno'><OPTION value='yes'>Yes</OPTION><OPTION value='no'>No</OPTION></SELECT>");
      $select.appendTo(args.container);
      $select.focus();
    };

    this.destroy = function () {
      $select.remove();
    };

    this.focus = function () {
      $select.focus();
    };

    this.loadValue = function (item) {
      $select.val((defaultValue = item[args.column.field]) ? "yes" : "no");
      $select.select();
    };

    this.serializeValue = function () {
      return ($select.val() == "yes");
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return ($select.val() != defaultValue);
    };

    this.validate = function () {
      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  function CheckboxEditor(args) {
    var $select;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $select = $("<INPUT type=checkbox value='true' class='editor-checkbox' hideFocus>");
      $select.appendTo(args.container);
      $select.focus();
    };

    this.destroy = function () {
      $select.remove();
    };

    this.focus = function () {
      $select.focus();
    };

    this.loadValue = function (item) {
      // Modified by winbill. for true/false string stored in db
      //defaultValue = !!item[args.column.field];
      defaultValue = (item[args.column.field] === 'true' || item[args.column.field] === true);
      if (defaultValue) {
        $select.prop('checked', true);
      } else {
        $select.prop('checked', false);
      }
    };

    this.serializeValue = function () {
      return $select.prop('checked');
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (this.serializeValue() !== defaultValue);
    };

    this.validate = function () {
      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  function PercentCompleteEditor(args) {
    var $input, $picker;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $input = $("<INPUT type=text class='editor-percentcomplete' />");
      $input.width($(args.container).innerWidth() - 25);
      $input.appendTo(args.container);

      $picker = $("<div class='editor-percentcomplete-picker' />").appendTo(args.container);
      $picker.append("<div class='editor-percentcomplete-helper'><div class='editor-percentcomplete-wrapper'><div class='editor-percentcomplete-slider' /><div class='editor-percentcomplete-buttons' /></div></div>");

      $picker.find(".editor-percentcomplete-buttons").append("<button val=0>Not started</button><br/><button val=50>In Progress</button><br/><button val=100>Complete</button>");

      $input.focus().select();

      $picker.find(".editor-percentcomplete-slider").slider({
        orientation: "vertical",
        range: "min",
        value: defaultValue,
        slide: function (event, ui) {
          $input.val(ui.value)
        }
      });

      $picker.find(".editor-percentcomplete-buttons button").on("click", function (e) {
        $input.val($(this).attr("val"));
        $picker.find(".editor-percentcomplete-slider").slider("value", $(this).attr("val"));
      })
    };

    this.destroy = function () {
      $input.remove();
      $picker.remove();
    };

    this.focus = function () {
      $input.focus();
    };

    this.loadValue = function (item) {
      $input.val(defaultValue = item[args.column.field]);
      $input.select();
    };

    this.serializeValue = function () {
      return parseInt($input.val(), 10) || 0;
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ((parseInt($input.val(), 10) || 0) != defaultValue);
    };

    this.validate = function () {
      if (isNaN(parseInt($input.val(), 10))) {
        return {
          valid: false,
          msg: "Please enter a valid positive number"
        };
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  /*
   * An example of a "detached" editor.
   * The UI is added onto document BODY and .position(), .show() and .hide() are implemented.
   * KeyDown events are also handled to provide handling for Tab, Shift-Tab, Esc and Ctrl-Enter.
   */
  function LongTextEditor(args) {
    var $input, $wrapper;
    var defaultValue;
    var scope = this;

    this.init = function () {
      var $container = $("body");

      $wrapper = $("<DIV style='z-index:10000;position:absolute;background:white;padding:5px;border:3px solid gray; -moz-border-radius:10px; border-radius:10px;'/>")
          .appendTo($container);

      $input = $("<TEXTAREA hidefocus rows=5 style='backround:white;width:250px;height:80px;border:0;outline:0'>")
          .appendTo($wrapper);

      $("<DIV style='text-align:right'><BUTTON>Save</BUTTON><BUTTON>Cancel</BUTTON></DIV>")
          .appendTo($wrapper);

      $wrapper.find("button:first").on("click", this.save);
      $wrapper.find("button:last").on("click", this.cancel);
      $input.on("keydown", this.handleKeyDown);

      scope.position(args.position);
      $input.focus().select();
    };

    this.handleKeyDown = function (e) {
      if (e.which == $.ui.keyCode.ENTER && e.ctrlKey) {
        scope.save();
      } else if (e.which == $.ui.keyCode.ESCAPE) {
        e.preventDefault();
        scope.cancel();
      } else if (e.which == $.ui.keyCode.TAB && e.shiftKey) {
        e.preventDefault();
        args.grid.navigatePrev();
      } else if (e.which == $.ui.keyCode.TAB) {
        e.preventDefault();
        args.grid.navigateNext();
      }
    };

    this.save = function () {
      args.commitChanges();
    };

    this.cancel = function () {
      $input.val(defaultValue);
      args.cancelChanges();
    };

    this.hide = function () {
      $wrapper.hide();
    };

    this.show = function () {
      $wrapper.show();
    };

    this.position = function (position) {
      $wrapper
          .css("top", position.top - 5)
          .css("left", position.left - 5)
    };

    this.destroy = function () {
      $wrapper.remove();
    };

    this.focus = function () {
      $input.focus();
    };

    this.loadValue = function (item) {
      $input.val(defaultValue = item[args.column.field]);
      $input.select();
    };

    this.serializeValue = function () {
      return $input.val();
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  function SelectEditor(args) {
    var $select;
    var defaultValue;
    var scope = this;

    this.init = function() {
    // 数据已经初始化
    if (args.column.options) {
      option_str = "";
		  $.each(args.column.options,function(key,value){
          option_str += "<OPTION value='" + key + "'>" + value + "</OPTION>";
      });
      $select = $("<SELECT tabIndex='0' class='editor-select'>"+ option_str +"</SELECT>");
      $select.appendTo(args.container);
      $select.focus();
    } else if (args.column.joinurl) {
    	// 数据异步初始化，formatter中会有问题，grid初始化时显示formatter没有数据
  		var url = args.column.joinurl;
		  var param = "{'tablesId':'" + args.column.table_name + "','corpId':'" + args.column.corp_id + "','columnsId':'" + args.column.table_column + "'}";
    	var obj = eval('(' + param + ')');
  		callAjax(url, true, obj, function(dt) {
	  		if (dt.success == true) {
		  	  //opt_values = args.column.options.split(',');
			    option_str = "";
			    var json = eval(dt.data);
  			  args.column.options = json;
	  		  $.each(json,function(key,value){
                  option_str += "<OPTION value='" + key + "'>" + value + "</OPTION>";
			    });
          $select = $("<SELECT tabIndex='0' class='editor-select'>"+ option_str +"</SELECT>");
          $select.appendTo(args.container);
          $select.focus();
          // Asynchronous callback
          args.initCallback();
			  } else {
				  alert(dt.msg);
			  }
		  });
    } else {
        opt_values ="yes,no".split(',');
        option_str = "";
        for( i in opt_values ){
          v = opt_values[i];
          // value->id, inner text->text
          option_str += "<OPTION value='"+v+"'>"+v+"</OPTION>";
        }
        $select = $("<SELECT tabIndex='0' class='editor-select'>"+ option_str +"</SELECT>");
        $select.appendTo(args.container);
        $select.focus();
      }
    };

    this.destroy = function() {
      if ($select) $select.remove();
    };

    this.focus = function() {
    	if ($select) $select.focus();
    };

    this.loadValue = function(item) {
      defaultValue = item[args.column.field];
      if ($select) $select.val(defaultValue);
    };

    this.position = function (position) {
      if ($select) {
        $select
          .css("width", position.width - 10)
      }
    };

    this.serializeValue = function() {
      if ($select) {
        if(args.column.joinurl){
          return $select.val();
        }else{
          // Fixed bug: value turned into true/false
          // return ($select.val() == "yes");
          return $select.val();
        }
      }
    };

    this.applyValue = function(item,state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function() {
      return ($select.val() != defaultValue);
    };

    this.validate = function() {
      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }
  // readonly TextEditor
  function LabelEditor(args) {
    var $input;
    var defaultValue;
    var scope = this;

    this.init = function () {
      $input = $("<INPUT type=text readonly='readonly' class='editor-text' />")
          .appendTo(args.container)
          .on("keydown.nav", function (e) {
            if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
              e.stopImmediatePropagation();
            }
          })
          .focus()
          .select();
    };

    this.destroy = function () {
      $input.remove();
    };

    this.focus = function () {
      $input.focus();
    };

    this.getValue = function () {
      return $input.val();
    };

    this.setValue = function (val) {
      $input.val(val);
    };

    this.loadValue = function (item) {
      defaultValue = item[args.column.field] || "";
      $input.val(defaultValue);
      $input[0].defaultValue = defaultValue;
      $input.select();
    };

    this.serializeValue = function () {
      return $input.val();
    };

    this.applyValue = function (item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function () {
      return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
    };

    this.validate = function () {
      if (args.column.validator) {
        var validationResults = args.column.validator($input.val(), args.column.regrule, args.column.regmsg);
        if (!validationResults.valid) {
          return validationResults;
        }
      }

      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  /**
   * CheckboxGroupEditor
   * 为了和formeo保持一致, 保存值, 如果只选中一个那么是7cade98746c64077b842ab2c24cecf72,
   * 两个以上是["7cade98746c64077b842ab2c24cecf72","7cade98746c64077b842ab2c24cecf72"]
   * @param args
   * @constructor
   */
  function CheckboxGroupEditor(args) {
    var $wrapper;
    var $checkBoxInput = [];
    var defaultValue = [];    // ["5b53a5c168144859aa61d0f44e526caa","d4146bf2b56f41f9b453a595350f9810"]
    var scope = this;

    this.init = function() {
      // 数据已经初始化
      $wrapper = $("<div />").appendTo(args.container);

      if (args.column.options) {
        var index = 0;
        $.each(args.column.options, function(key, value) {
          $checkBoxInput[index] = $("<INPUT type=checkbox value='" + key + "' /> <label>" + value + "</label>");
          $wrapper.append($checkBoxInput[index]);
          index++;
        });
        //$wrapper.appendTo(args.container);
        $wrapper.focus();
      }
    };

    this.destroy = function() {
      if ($wrapper) $wrapper.remove();
    };

    this.focus = function() {
      if ($wrapper) $wrapper.focus();
    };

    this.loadValue = function(item) {
      var obj = item[args.column.field];
      if (obj instanceof Array) {
        defaultValue = obj;           // Reserve default value
      } else if (typeof(obj) == "undefined") {
        defaultValue = [];
        obj = [];
      } else if (typeof(obj) == "string" || typeof(obj) == "number") {
        try {
          obj = obj + '';
          obj = JSON.parse(obj);
        } catch (e) {
          // string convert to array
          var arr = [];
          arr[0] = obj;
          obj = arr;
        }
        defaultValue = obj;
      }
      for (var i = 0; i < $checkBoxInput.length; i++) {
        var bFound = false;
        for (var index = 0; index < obj.length; index++) {
          if (obj[index] == $checkBoxInput[i].val()) {
            bFound = true;
            break;
          }
        }
        if (bFound == true) {
          $checkBoxInput[i][0].checked = true;
        } else {
          $checkBoxInput[i][0].checked = false;
        }
      }
      //if ($wrapper) $wrapper.val(defaultValue);
    };

    this.position = function (position) {
      if ($wrapper) {
        $wrapper
          .css("width", position.width - 10)
      }
    };

    this.serializeValue = function() {
      var val = [];
      if ($checkBoxInput) {
        var j = 0;
        for (var i = 0; i < $checkBoxInput.length; i++) {
          if ($checkBoxInput[i][0].checked == true) {
            val[j++] = $checkBoxInput[i].val();
          }
        }
      }
      if (val.length == 0) {
        return '';
      } else if (val.length == 1) {
        return val[0];  // string
      };
      return val;       // array
    };

    this.applyValue = function(item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function() {
      var obj = defaultValue;
      if (typeof(obj) == "string" || typeof(obj) == "number") {
        // string convert to array
        var arr = [];
        arr[0] = obj + '';
        obj = arr;
      }
      //if ($checkBoxInput.length != obj.length) {
      //  return true;
      //}
      var bChanged = false;
      var checkCount = 0;
      for (var i = 0; i < $checkBoxInput.length; i++) {
        if ($checkBoxInput[i][0].checked == true) {
          checkCount += 1;
        }
        var bFound = false;
        for (var index = 0; index < obj.length; index++) {
          if (obj[index] == $checkBoxInput[i].val()) {
            bFound = true;
          }
          if (bFound == true) {
            if ($checkBoxInput[i][0].checked == false) {
              // value有该项,但是没有check,说明变化了
              return true;
            }
          } else {
            if ($checkBoxInput[i][0].checked == true) {
              // value没有该项,但是有check,说明变化了
              return true;
            }
          }
        }
      }
      // Checked count not equals to values count
      if (obj.length != checkCount) {
        return true;
      }
      return false;
      //return ($wrapper.val() != defaultValue);
    };

    this.validate = function() {
      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }

  function RadioGroupEditor(args) {
    var $wrapper;
    var $radioInput = [];
    var name = '';
    var defaultValue;   // 默认值是key
    var scope = this;

    this.init = function() {
      // 数据已经初始化
      $wrapper = $("<div style=''/>").appendTo(args.container);

      if (args.column.options) {
        name = guid();
        var index = 0;
        $.each(args.column.options, function(key, value) {
          $radioInput[index] = $("<INPUT type='radio' name='" + name + "' value='" + key + "' /> <label>" + value + "</label>");
          $wrapper.append($radioInput[index]);
          index++;
        });
        //$wrapper.appendTo(args.container);
        $wrapper.focus();
      }
    };

    this.destroy = function() {
      if ($wrapper) $wrapper.remove();
    };

    this.focus = function() {
      if ($wrapper) $wrapper.focus();
    };

    this.loadValue = function(item) {
      defaultValue = item[args.column.field];
      if (defaultValue) {
        $("input[name='" + name +"'][value='" + defaultValue + "']").attr("checked", true);
      }
      //$("input[name='" + name +"']:checked").val();
      //if ($wrapper) $wrapper.val(defaultValue);
      //$("input[name='radioName'][value=2]").attr("checked", true);
    };

    this.position = function (position) {
      if ($wrapper) {
        $wrapper
          .css("width", position.width - 10)
      }
    };

    this.serializeValue = function() {
      var val = $("input[name='" + name +"']:checked").val();
      return val;
    };

    this.applyValue = function(item, state) {
      item[args.column.field] = state;
    };

    this.isValueChanged = function() {
      var val = $("input[name='" + name +"']:checked").val();
      return (val != defaultValue);
    };

    this.validate = function() {
      return {
        valid: true,
        msg: null
      };
    };

    this.init();
  }
})(jQuery);
