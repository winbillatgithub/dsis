function startWith(str, prefix) {
    if(str==null||str==""||str.length==0)
      return false;
    if(str.substr(0,prefix.length)==prefix)
      return true;
    else
      return false;
    return true;  
}

// ajax call function
function callAjax(url, async, params, callback) {
	var xhr = $.ajax({
        type: 'post', 
        cache: false, 
        dataType: 'json',
        async: async,
        url: url,
        data: params,
        success: function (data) {
			callback(data);
        },
        error : function(data){
        	callback(data);
        },
        beforeSend: function () {
        }
    });
	return xhr;
}

// 32byte guid
function guid() {
    return 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}

function my_guid() {
  return guid();
}

function regExpCheck(value, rule) {
	if (rule == '' || rule == null || rule == 'undefined') {
		return true;
	}
	var reg = new RegExp(rule);
	var bRet = reg.test(value);
	 if(!bRet) {
		 return false;
	 }
	 return true;
}
