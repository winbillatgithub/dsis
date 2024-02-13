jQuery(function($) {
  var ajaxUrl = null;		// url for loading data asynchronously--winbill
  var ajaxParams = {};	// basic params for ajax invoke--winbill

  console.log("Form begin to render");

  var fields = [
    {
      type: 'autocomplete',
      label: 'Custom Autocomplete',
      required: true,
      values: [
        {label: 'SQL'},
        {label: 'C#'},
        {label: 'JavaScript'},
        {label: 'Java'},
        {label: 'Python'},
        {label: 'C++'},
        {label: 'PHP'},
        {label: 'Swift'},
        {label: 'Ruby'}
      ]
    },
    {
      label: 'Star Rating',
      attrs: {
        type: 'starRating'
      },
      icon: '🌟'
    }
  ];

  var replaceFields = [
    {
      type: 'textarea',
      subtype: 'tinymce',
      label: 'tinyMCE',
      required: true,
    }
  ];

  var actionButtons = [{
    id: 'smile',
    className: 'btn btn-success',
    label: '😁',
    type: 'button',
    events: {
      click: function() {
        alert('SMILE!');
      }
    }
  }];

  var templates = {
    starRating: function(fieldData) {
      return {
        field: '<span id="'+fieldData.name+'">',
        onRender: function() {
          $(document.getElementById(fieldData.name)).rateYo({rating: 3.6});
        }
      };
    }
  };

  var inputSets = [];

  var typeUserDisabledAttrs = {
    autocomplete: ['access']
  };

  var typeUserAttrs = {
    checkbox : {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      },
      searchItem: {
        label: '是否检索项',
        type: 'checkbox'
      }
    },
    text: {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      },
      searchItem: {
        label: '是否检索项',
        type: 'checkbox'
      }
    },
    date : {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      },
      searchItem: {
        label: '是否检索项',
        type: 'checkbox'
      }
    },
    datetime : {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      },
      searchItem: {
        label: '是否检索项',
        type: 'checkbox'
      }
    },
    asfile : {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      }
    },
    number : {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      },
      searchItem: {
        label: '是否检索项',
        type: 'checkbox'
      }
    },
    select : {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      },
      searchItem: {
        label: '是否检索项',
        type: 'checkbox'
      }
    },
    textarea : {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      },
      searchItem: {
        label: '是否检索项',
        type: 'checkbox'
      }
    },
    'radio-group' : {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      },
      searchItem: {
        label: '是否检索项',
        type: 'checkbox'
      }
    },
    'checkbox-group' : {
      dataEntityName: {
        label: '界面元素',
        options: {
        },
        style: 'border: 1px solid red'
      },
      searchItem: {
        label: '是否检索项',
        type: 'checkbox'
      }
    }
  };
  //var formData = window.sessionStorage.getItem('formData');
  var formData = $("#JSON_DATA").val();

  // Pass url from formeo.jsp
  var setAjaxParams = function() {
    return {
      setUrl : function(u) {
        ajaxUrl = u;
      },
      getUrl : function() {
        return ajaxUrl;
      },
      alertStr : function() {
        alert(ajaxUrl);
      }
    }
  }();
  // opts.typeUserEvents[type].onadd(field); 可以把opts.formData传过来
  // "[{"type":"date","dataEntityName":"CHECKIN_TIME##签到日期##DATETIME##0##0##1##undefined##undefined####undefined##undefined##",
  // "label":"签到日期","className":"form-control",
  //  "name":"date-1529025371329"}]"
  var loadEntityData = function(fld) {
    return;
    var sel = document.getElementById('dataEntityName-' + fld.id);
    if (!sel) {
      return;
    }
    sel.options.length = 0;
    // Add empty line
    sel.options.add(new Option('', '-1'));

    ajaxUrl = document.getElementById('ajaxUrl').value;
    callAjax(ajaxUrl, true, null, function(dt) {
      if (dt.success == true) {
        for (var i = 0; i < dt.data.length; i++ ) {
          var index = dt.data[i];
          var item = {};
          item["id"] = index['COLUMN_NAME_EN'];
          item["name"] = index['COLUMN_NAME_CN'];
          item["type"] = index['DATA_TYPE_NAME'];
          item["encrypt"] = index['RSV_STR2'];
          item["length"] = index['LENG'];
          item["precision"] = index['DATA_PRECISION'];
          item["nullable"] = index['NULLABLE'];
          item["rule"] = index['RULE'];
          item["rule_msg"] = index['RULE_MSG'];
          item["initial_type"] = index['INITIAL_TYPE_NAME'];
          item["control_type"] = index['CONTROL_TYPE'];
          item["subtype"] = index['CONTROL_SUB_TYPE'];
          item["subtype_format"] = index['CONTROL_FORMAT'];
          item["data_source"] = index['DATA_SOURCE'];
          // Reset select options
          // id, name, type, length, precision, nullable, rule, initial_type, encrypt
          var val = item["id"] + "##" + item["name"] + "##" + item["type"] + "##" + item["length"] + "##"
            + item["precision"] + "##" + item["nullable"] + "##" + item["rule"] + "##" + item["initial_type"] + "##"
            + item["encrypt"] + "##" + item["subtype"] + "##" + item["subtype_format"] + "##" + item["data_source"] + '##'
            + item["control_type"] + "##" + item["rule_msg"];
          sel.options.add(new Option(item["name"], val));
        }
      }
    });
  };

  /* control type
   'autocomplete',
   'button',
   'checkbox',
   'checkbox-group',
   'date',
   'file',
   'header',
   'hidden',
   'paragraph',
   'number',
   'radio-group',
   'select',
   'text',
   'textarea',
   */
  var loadEntitySets = function() {
    ajaxUrl = document.getElementById('ajaxUrl').value;
    var entitySets = [];
    callAjax(ajaxUrl, false, null, function(dt) {
      if (dt.success == true) {
        for (var i = 0; i < dt.data.length; i++ ) {
          var index = dt.data[i];
          var item = (entitySets[i] = {});
          item["id"] = index['COLUMN_NAME_EN'];
          item["name"] = index['COLUMN_NAME_CN'];
          item["data_type"] = (typeof(index['DATA_TYPE_NAME']) == "undefined") ? '' : index['DATA_TYPE_NAME'];
          item["encrypt"] =  (typeof(index['RSV_STR2']) == "undefined") ? '' : index['RSV_STR2'];
          item["length"] = (typeof(index['LENG']) == "undefined") ? '' : index['LENG'];
          item["precision"] = (typeof(index['DATA_PRECISION']) == "undefined") ? '' : index['DATA_PRECISION'];
          item["nullable"] = (typeof(index['NULLABLE']) == "undefined") ? '' : index['NULLABLE'];
          item["rule"] = (typeof(index['RULE']) == "undefined") ? '' : index['RULE'];
          item["rule_msg"] = (typeof(index['RULE_MSG']) == "undefined") ? '' : index['RULE_MSG'];
          item["initial_type"] = (typeof(index['INITIAL_TYPE_NAME']) == "undefined") ? '' : index['INITIAL_TYPE_NAME'];
          item["control_type"] = (typeof(index['CONTROL_TYPE']) == "undefined") ? '' : index['CONTROL_TYPE'];
          item["subtype"] = (typeof(index['CONTROL_SUB_TYPE']) == "undefined") ? '' : index['CONTROL_SUB_TYPE'];
          item["subtype_format"] = (typeof(index['CONTROL_FORMAT']) == "undefined") ? '' : index['CONTROL_FORMAT'];
          item["data_source"] = (typeof(index['DATA_SOURCE']) == "undefined") ? '' : index['DATA_SOURCE'];
          item["parenttype"] = (typeof(index['CONTROL_TYPE']) == "undefined") ? '' : index['CONTROL_TYPE'];
          // Reset select options
          // id, name, type, length, precision, nullable, rule, initial_type, encrypt
          //var val = item["id"] + "##" + item["name"] + "##" + item["type"] + "##" + item["length"] + "##"
          //  + item["precision"] + "##" + item["nullable"] + "##" + item["rule"] + "##" + item["initial_type"] + "##"
          //  + item["encrypt"] + "##" + item["subtype"] + "##" + item["subtype_format"] + "##" + item["data_source"] + "##"
          //  + item["control_type"];

        }
      }
    });
    // System default items
    var len = entitySets.length;
    var item = (entitySets[len++] = {}); // 提交人
    item["id"] = 'CREATE_ID';
    item["name"] = '提交人';
    item["data_type"] = 'STRING';
    item["encrypt"] = '';
    item["length"] = '64';
    item["precision"] = '0';
    item["nullable"] = '1';
    item["rule"] = '';
    item["initial_type"] = 'ZIDONG';//'1010203de673417280116506d4199247';
    item["control_type"] = 'text';
    item["subtype"] = '';
    item["subtype_format"] = '';
    item["data_source"] = '';
    item["parenttype"] = 'text';
    item = (entitySets[len++] = {});  // 提交时间
    item["id"] = 'UPDATE_TIME';
    item["name"] = '提交时间';
    item["data_type"] = 'DATETIME';
    item["encrypt"] = '';
    item["length"] = '0';
    item["precision"] = '0';
    item["nullable"] = '1';
    item["rule"] = '';
    item["initial_type"] = 'ZIDONG';//'1010203de673417280116506d4199247';
    item["control_type"] = 'datetime';
    item["subtype"] = '';
    item["subtype_format"] = '';
    item["data_source"] = '';
    item["parenttype"] = 'datetime';
    item = (entitySets[len++] = {});  // 部门
    item["id"] = 'CORP_ID';
    item["name"] = '单位/部门';
    item["data_type"] = 'STRING';
    item["encrypt"] = '';
    item["length"] = '100';
    item["precision"] = '0';
    item["nullable"] = '1';
    item["rule"] = '';
    item["initial_type"] = 'ZIDONG';//'1010203de673417280116506d4199247';
    item["control_type"] = 'text';
    item["subtype"] = '';
    item["subtype_format"] = '';
    item["data_source"] = '';
    item["parenttype"] = 'text';

    return entitySets;
  };
  /*
   * Handle onadd event
   */
  var handleUserEvents = {
    // Load data entities asynchronously
    checkbox : {
      onadd : function (fld) {
        loadEntityData(fld);
      }
    }, text: {
      onadd: function (fld) {
        loadEntityData(fld);
      }
    }, date: {
      onadd: function (fld) {
        loadEntityData(fld);
      }
    }, datetime: {
      onadd: function (fld) {
        loadEntityData(fld);
      }
    }, asfile: {
      onadd: function (fld) {
        loadEntityData(fld);
      },
      onFileUpload:function (fld) {
        FileUpload('ID', 'COLUMNS_ID', 'COLUMNS_VALUE');
      },
      onFileDownload:function (fld) {
        // a.txt|268|txt|group1/M00/00/01/ZcmPE1mnbXKAXhPIAAABDLtJEg4559.txt|d0b2785501d743d9b87b4d034523822f|group1/M00/00/01/ZcmPE1mnbZSAcunSAABmxQj5exU981.pdf
        FileDownload('txt', 'group1/M00/00/01/ZcmPE1mnbXKAXhPIAAABDLtJEg4559.txt');
      },
      onFilePreview:function (fld) {
        var arr = value.split("|");
        // alert(arr);
        if (arr[2] == 'txt' || arr[2] == 'doc' || arr[2] == 'docx' || arr[2] == 'ppt' || arr[2] == 'pptx' || arr[2] == 'xls'|| arr[2] == 'xlsx') {
          FilePreview(arr[2], arr[5]);
        } else {
          FilePreview(arr[2], arr[3]);
        }

      },
      onFileDelete:function (fld) {
        alert('file delete');
      }
    }, number: {
      onadd: function (fld) {
        loadEntityData(fld);
      }
    }, select: {
      onadd: function (fld) {
        loadEntityData(fld);
      }
    }, textarea: {
      onadd: function (fld) {
        loadEntityData(fld);
      }
    }, 'radio-group': {
      onadd: function (fld) {
        loadEntityData(fld);
      }
    }, 'checkbox-group': {
      onadd: function (fld) {
        loadEntityData(fld);
      }
    }
  }
  function FileUpload(ID, COLUMNS_ID, COLUMNS_VALUE) {
    if (COLUMNS_VALUE == null || COLUMNS_VALUE == 'undefined') {
      COLUMNS_VALUE = '';
    }
    COLUMNS_VALUE = encodeURI(COLUMNS_VALUE);
    // *** TABLES_ID不能为空, 否则上传失败
    var TABLES_ID = '';
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
        //var innerData = dataView.getCurrentPageItems();
        //innerData[ROW_INDEX][COLUMNS_ID] = UPLOAD_FILE_NAME;		// 刷新上传文档的名称和url
        //grid.invalidateRow(ROW_INDEX);
        //grid.render();
      }
      diag.close();
    };
    diag.show();
    return true;
  }
  function FileDownload(type, fid) {
    var url = '/files/video/download.do?tm=' + new Date().getTime();
    $.download(url, 'GET', type, fid);
  }
  function FilePreview(type, fid) {
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
    } else if (type == 'jpg') {
        url = '/files/jpg/page.do?tm=' + new Date().getTime();
        url += "&fid=" + fid + "&type=" + type;
    } else {
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

  // test disabledAttrs
  var disabledAttrs = ['placeholder'];
  var entitySets = loadEntitySets();

  var fbOptions = {
    subtypes: {
      // text: ['datetime-local']
    },
    onSave: function(e, formData) {
      toggleEdit();
      $('.render-wrap').formRender({
        formData: formData,
        templates: templates
      });
      // window.sessionStorage.setItem('formData', JSON.stringify(formData));
    },
    stickyControls: {
      enable: true
    },
    sortableControls: true,
    fields: fields,
    templates: templates,
    inputSets: inputSets,
    typeUserDisabledAttrs: typeUserDisabledAttrs,
    typeUserAttrs: typeUserAttrs,
    // Register Event onAdd
    typeUserEvents: handleUserEvents,
    disableInjectedStyle: false,
    actionButtons: actionButtons,
    disableFields: ['autocomplete'],
    replaceFields: replaceFields,
    disabledFieldButtons: {
      text: ['copy']
    },
    // 实体列表,展示在右侧菜单 TODO:entitySets
    entitySets:entitySets
    // controlPosition: 'left'
    // disabledAttrs
  };
  var editing = true;

  if (formData) {
    formData = decodeURIComponent(atob(formData));
    //formData = JSONArray.parse(formData);
    fbOptions.formData = formData;
  }

  /**
   * Toggles the edit mode for the demo
   * @return {Boolean} editMode
   */
  function toggleEdit() {
    document.body.classList.toggle('form-rendered', editing);
    return editing = !editing;
  }

  var setFormData = '[{"type":"text","label":"Full Name","subtype":"text","className":"form-control","name":"text-1476748004559"},{"type":"select","label":"Occupation","className":"form-control","name":"select-1476748006618","values":[{"label":"Street Sweeper","value":"option-1","selected":true},{"label":"Moth Man","value":"option-2"},{"label":"Chemist","value":"option-3"}]},{"type":"textarea","label":"Short Bio","rows":"5","className":"form-control","name":"textarea-1476748007461"}]';

  var formBuilder = $('.build-wrap').formBuilder(fbOptions);
  var fbPromise = formBuilder.promise;

  fbPromise.then(function(fb) {
    var apiBtns = {
      showData: fb.actions.showData,
      clearFields: fb.actions.clearFields,
      getData: function() {
        console.log(fb.actions.getData());
      },
      setData: function() {
        fb.actions.setData(setFormData);
      },
      addField: function() {
        var field = {
            type: 'text',
            class: 'form-control',
            label: 'Text Field added at: ' + new Date().getTime()
          };
        fb.actions.addField(field);
      },
      removeField: function() {
        fb.actions.removeField();
      },
      testSubmit: function() {
        var formData = new FormData(document.forms[0]);
        console.log('Can submit: ', document.forms[0].checkValidity());
        // Display the key/value pairs
        console.log('FormData:', formData);
        for(var pair in formData.entries()) {
           console.log(pair[0]+ ': '+ pair[1]);
        }
      },
      resetDemo: function() {
        window.sessionStorage.removeItem('formData');
        location.reload();
      }
    };

    //Object.keys(apiBtns).forEach(function(action) {
    //  document.getElementById(action)
    //  .addEventListener('click', function(e) {
    //    apiBtns[action]();
    //  });
    //});
    //
    //document.getElementById('setLanguage')
    //.addEventListener('change', function(e) {
    //  fb.actions.setLang(e.target.value);
    //});
    //
    //document.getElementById('getXML').addEventListener('click', function() {
    //  alert(formBuilder.actions.getData('xml'));
    //});
    //document.getElementById('getJSON').addEventListener('click', function() {
    //  alert(formBuilder.actions.getData('json', true));
    //});
    //document.getElementById('getJS').addEventListener('click', function() {
    //  alert('check console');
    //  console.log(formBuilder.actions.getData());
    //});
  });

  document.getElementById('edit-form').onclick = function() {
    toggleEdit();
  };

  // Get json data in form
  window.getFormJsonData = function() {
    return formBuilder.actions.getData('json', false);
  };
  // Get columns definition in form
  // Refer to template_list
  window.getColumnsDef = function() {
    var columnsDef = {};
    var a = $(".render-wrap :input").serializeArray();
    console.log(a);
    $.each(a, function () {
      var value = {};
      var dataEntityName = $("[name='" + this.name + "']").attr("data-entity-name");
      if (typeof(dataEntityName) == 'undefined') {
        return; // continue;
      }
      // id[0], name[1], type[2], length[3], precision[4], nullable[5], rule[6], initial_type[7], encrypt[8],
      // subtype[9], subtype_format[10], data_source[11], control_type[12]
      var needed = false;
      var items = dataEntityName.split('##');
      var key = items[0];     // COLUMN_NAME_EN
      var encrypt = items[8];
      if (encrypt == '1') {
        value['encrypt'] = encrypt;
        needed = true;
      } else {
        value['encrypt'] = '0';
      }
      // 内联字段
      var join_condition = items[12]; // TODO:扩展
      if (join_condition == '0') {
        value['join_condition'] = join_condition;
        value['join_table_name'] = columns[idx]['join_table_name'];
        value['join_columns_id'] = columns[idx]['join_columns_id'];			// 内联用
        value['join_select_table_column'] = columns[idx]['join_select_table_column'];
        value['master_table_name'] = columns[idx]['master_table_name'];
        value['master_table_column'] = columns[idx]['master_table_column'];
        value['corp_id'] = columns[idx]['corp_id'];
        needed = true;
      }
      if (needed == true) {
        columnsDef[key] = value;
      }
    });
    var definitions = JSON.stringify(columnsDef);
    definitions = encodeURIComponent(definitions); // fixed bug:400 bad request
    return definitions;
  }
  // Get json data in form
  window.getUserJsonData = function() {
    var o = {};
    var a = $(".render-wrap :input").serializeArray();
    console.log(a);
    $.each(a, function () {
      //this.name.substring(0, 13) != 'dtp_datetime-'
      var dataEntityName = $("[name='" + this.name + "']").attr("data-entity-name");
      if (typeof(dataEntityName) == 'undefined') {
        return; // continue;
      }
      var items = dataEntityName.split('##');
      var key = items[0]; // key = this.name; // textarea-237498103748

      if (o[key] !== undefined) {
        if (!o[key].push) {
          o[key] = [o[key]];
        }
        o[key].push(this.value || '');
      } else {
        var dataKey = $("[name='" + this.name + "']").attr("data-key");
        if (dataKey != '' && dataKey != null && dataKey != 'undefined') {
          o[key] = dataKey || '';
        } else {
          o[key] = this.value || '';
        }
      }
    });
    //o = JSON.stringify(o);
    return o;
  };
  // 先走的render流程,后走的addon,导致无法选中select实体
  var formEditFlag = $("#formEditFlag").val();
  if (formEditFlag == 'preview') {
    toggleEdit();
    $('.render-wrap').formRender({
      formData: formData,
      templates: templates
    });
  }

});
