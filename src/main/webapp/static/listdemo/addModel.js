
//保存模型
function saveToDb(dataList)
{
    
    var result = exportData();
    //sessionStorage.setItem("flowsheet",JSON.stringify(result));
    sessionStorage.setItem("flowsheet",result);
    return result;
    

}
//把模型导出到一个变量中
function exportData()
{
	var modelName = $("#modelName").val();
    var modelDesc = $("#modelDesc").val();
    //var modelStr = '{model:{modelName:' + modelName +',modelDesc:' + modelDesc + '},' ;
    var retStr = '{"model":{"modelName":"' + modelName +'","modelDesc":"' + modelDesc + '"},"nodes":{';

    if($("#container").children().length > 0){
        $("#container .model").each(function(i){
            var nodeStr = "";
            nodeStr += '"' + $(this).attr("id") + '":{"code":"' + $(this).attr("modelCode") + '"'
            			+ ',' + '"modelType":"' + $(this).attr("modelType") + '"'
                        + ',' + '"left":"' + $(this).css("left") + '"'
                        + ',' + '"top":"' + $(this).css("top") + '"' + '}';
            if((i+1) < $("#container .model").length)
                nodeStr += ','
            retStr += nodeStr;
        });
        retStr += '},';
        //连接
        var connections = instance.getAllConnections();
        if(connections.length > 0){
            var lineStr = '"lines":{';
            for(var i = 0; i < connections.length; i++){
                var sourceId = connections[i].sourceId;
                var sourceCol = $("#" + sourceId).attr("colCode");
                var targetId = connections[i].targetId;
                var targetCol = $("#" + targetId).attr("colCode");
                var sourceLocationType = connections[i].endpoints[0].anchor.type; // 起点在li元素中的位置(左或右)
                var targetLocationType = connections[i].endpoints[1].anchor.type; // 终点在li元素中的位置(左或右)
                var label = connections[i].getOverlay("label").label;
                var twoWay = connections[i].getParameter("twoWay");
                var str = '"demo_line_' + i + '":{'
                            + '"from":"' + sourceId + '"'
                            + ',' +'"fromTable":"' + $("#" + sourceId).attr("tabName") + '"'
                            + ',' + '"fromCol":"' + sourceCol + '"'
                            + ',' + '"fromType":"' + sourceLocationType + '"'
                            + ',' + '"to":"' + targetId + '"'
                            + ',' + '"toTable":"' + $("#" + targetId).attr("tabName") + '"'
                            + ',' + '"toCol":"' + targetCol + '"'
                            + ',' + '"toType":"' + targetLocationType + '"'
                            + ',' + '"label":"' + label + '"'
                            + ',' + '"twoWay":' + twoWay
                            + '}';
                if((i+1) < connections.length)
                    str += ',';
                lineStr += str;
            }
            lineStr += '}';
            retStr += lineStr;
        }else{
            myalert("请检查模型连线是否正确");
            return false;
        }
    }else{
        myalert("请选择模型");
    }
    retStr += '}';
//    alert(retStr);
//    var retObj = JSON.parse(retStr);
    return retStr;
}



//弹窗
function myalert(info) {
    bootbox.dialog({
        "message" : info,
        "buttons" : {
            "success" : {
                "label" : "OK",
                "className" : "btn-sm btn-primary"
            }
        }
    });
}