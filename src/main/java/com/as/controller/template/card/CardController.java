/**
 * 卡片（单表）创建，实现两个功能：
 * ①用拖拽方式替换了列表模板管理的功能，zz实现 card/goAddDragDrop.do
 * ②采用Formeo通过拖拽方式实现表单的创建 card/card_single.do
 */
package com.as.controller.template.card;

import Decoder.BASE64Decoder;
import com.as.controller.template.TemplateController;
import com.as.db.proxy.DBProxy;
import com.as.entity.Page;
import com.as.entity.business.Column;
import com.as.service.business.common.CommonManager;
import com.as.util.JsonHelper;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import com.as.util.StringUtil;
import com.as.util.Tools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/card")
public class CardController extends TemplateController {
    //菜单地址(权限用)
    String menuUrl = "card/list.do";

    @Resource(name = "commonService")
    private CommonManager commonService;

    @RequestMapping(value = "/goAddDragDrop")
    public ModelAndView goAddDragDrop(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "card获取Indexes");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        pd.put("CORP_ID", Jurisdiction.getCorpId());
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }

        pd.put("SYS_DEFAUTL", "0");

        page.setPd(pd);
        List<PageData> varList = indexesService.listAll(pd);    //根据表id列出字段列表// SYSDEFAULT =1
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);

        pd.put("SYS_DEFAUTL", "1");
        // 系统属性的指标
        List<PageData> sysList = indexesService.listAll(pd);
        mv.addObject("sysList", sysList);

        //mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
        mv.setViewName("/business/card/card_dragdrop");
        return mv;
    }


    @RequestMapping(value = "/goEditDragDrop")
    public ModelAndView goEditDragDrop(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "card获取Indexes");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String tableId = pd.getString("TABLES_ID");
        logger.info("***edit table: " + tableId);
        pd.put("CORP_ID", Jurisdiction.getCorpId());
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        pd.put("SYS_DEFAUTL", "0");
        List<PageData> varList = indexesService.listAll(pd);    //根据表id列出字段列表


        List<PageData> colList = columnsService.list(page);    //根据表id列出字段列表
        mv.addObject("colList", colList);

        JSONArray p = new JSONArray();
        for (PageData record : colList) {
            JSONObject t = new JSONObject();
            t.put("INDEXS_ID", record.get("INDEXS_ID"));
            t.put("COLUMN_NAME_EN", record.get("COLUMN_NAME_EN"));
            t.put("COLUMN_NAME_CN", record.get("COLUMN_NAME_CN"));
            t.put("SEARCH", record.get("SEARCH"));
            t.put("SORT_NO", record.get("SORT_NO"));
            p.add(t);
        }
        mv.addObject("colListJson", p.toString());//
        logger.info("card已有指标json:" + p.toString());
        mv.addObject("varList", varList);//
        mv.addObject("pd", pd);
        mv.addObject("tableId", tableId);
        //mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
        // 系统属性的指标
        pd.put("SYS_DEFAUTL", "1");
        List<PageData> sysList = indexesService.listAll(pd);
        mv.addObject("sysList", sysList);

        mv.setViewName("/business/card/card_dragdrop");
        return mv;
    }


    /**
     * 保存表单模板到B_TABLES
     * {TABLES_ID:'',DATA:'[
     * {
     * "type": "datetime",
     * "dataEntityName": "CHECKIN_TIME##签到日期##DATETIME##0##0##1##undefined##undefined####undefined##undefined##",
     * "label": "签到日期",
     * "className": "form-control",
     * "name": "datetime-1528553091068",
     * "value": "2018-03-28 14:05:58"
     * }, {
     * "type": "date",
     * "dataEntityName": "CHECKOUT_TIME##签出日期##DATETIME##0##0##1##undefined##undefined####undefined##undefined##",
     * "label": "签出日期",
     * "className": "form-control",
     * "name": "date-1528553131294",
     * "value": "2018-06-09"
     * }
     * ]'
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/saveNew")
    @ResponseBody
    public Map saveNew() throws Exception {

        logBefore(logger, Jurisdiction.getUsername() + "新增CARD表");
        BASE64Decoder base64Decoder = new BASE64Decoder();
        Map<String, Object> ret = new HashMap<String, Object>();
        String warning = null;

        PageData pd = this.getPageData();
        String formEditFlag = pd.getString("formEditFlag");
        String tableId = pd.getString("TABLES_ID");
        String tableName = pd.getString("TABLE_NAME");
        String tableEnName = pd.getString("TABLE_NAME_EN");
        String remark = pd.getString("REMARK");
        String encodedJsonData = pd.getString("DATA");
        String jsonData = encodedJsonData;
        if (StringUtil.strIsNotEmpty(jsonData)) {
            byte[] b = base64Decoder.decodeBuffer(jsonData);
            jsonData = new String(b, "UTF-8");
            jsonData = URLDecoder.decode(jsonData, "UTF-8");
            if (jsonData.startsWith("\"")) {
                jsonData = jsonData.substring(1);
            }
            if (jsonData.endsWith("\"")) {
                jsonData = jsonData.substring(0, jsonData.length() - 1);
            }
            jsonData = jsonData.replaceAll("(\r|\n|\t)", "");
        }
        JSONArray jsonArray = JSONArray.fromObject(jsonData);

        try {
            // 建表 tablesService, 插入列
            if ("add".equals(formEditFlag)) {
                // tableID 没有说明新增 直接拿jsArray 进行insert即可
                PageData pdTable = new PageData();
                pdTable.put("TABLE_NAME", tableName);
                pdTable.put("TABLE_NAME_EN", tableEnName);
                pdTable.put("TABLES_ID", tableId);
                pdTable.put("REMARK", remark);
                pdTable.put("SORT_NO", 0);
                pdTable.put("STATUS", "0");
                pdTable.put("CORP_ID", Jurisdiction.getCorpId());
                pdTable.put("JSON_DATA", encodedJsonData);   //CARD数据

                //新增表
                tablesService.save(pdTable);
                // 创建物理表
                tablesService.createTable(pdTable);
                //
                // 循环新增column,然后新增物理列.
                PageData pdSave = new PageData();
                pdSave.put("TABLES_ID", tableId);
                pdSave.put("CORP_ID", Jurisdiction.getCorpId());
                for (int index = 0; index < jsonArray.size(); index++) {
                    JSONObject tempObj = jsonArray.getJSONObject(index);
                    String entityName = String.valueOf(tempObj.get("dataEntityName"));
                    if (StringUtils.isEmpty(entityName)) {
                        continue;
                    }
                    String[] items = entityName.split("##");
                    pdSave.put("COLUMN_NAME_EN", items[0]);
                    // 系统默认列, 不发生变化
                    String indexesId = "";
                    PageData columns = null;
                    if (isSysDefault(items[0])) {
                        indexesId = getSysDefaultIndexesId(items[0]);
                        columns = new PageData();
                        columns.put("SYS_DEFAUTL", "1");
                        columns.put("INDEXES_ID", indexesId);
                    } else {
                        // 不存在,插入操作
                        columns = indexesService.findByEnName(pdSave);
                    }
                    pdSave.put("COLUMNS_ID", this.get32UUID());    //主键
                    pdSave.put("INDEXES_ID", columns.get("INDEXES_ID"));    //entityName ->split to index_id
                    pdSave.put("SEARCH", tempObj.get("searchItem") == null ? "0" : "1");  //检索项标志
                    pdSave.put("SORT_NO", (index + 1) * 1000);        //排序号
                    columnsService.save(pdSave);

                    // 物理插入列
//                  PageData columns = indexesService.findById(pdSave);
                    // 只针对用户自定义指标，系统默认指标会自动插入数据库
                    if ("0".equals(columns.getString("SYS_DEFAUTL"))) {
                        columns.put("TABLE_NAME_EN", tableEnName);
                        DBProxy proxy = new DBProxy();
                        columns = proxy.resetDataType(columns);
                        columnsService.addColumn(columns);
                    }

                }
            } else if ("edit".equals(formEditFlag)) {
                // 说明是编辑已有,需要进行比较分类
                // 先循环前端返回的json数据,先将json中COLUMN_NAME_EN缓存起来,便于后续循环使用
                Map<String, String> columnsMap = new HashMap<String, String>();
                //
                // 判断是否为新增column,然后新增物理列.
                // 判断是否删除了column,然后删除column数据（物理表数据）
                PageData pdSave = new PageData();
                pdSave.put("TABLES_ID", tableId);
                pdSave.put("CORP_ID", Jurisdiction.getCorpId());
                for (int index = 0; index < jsonArray.size(); index++) {
                    JSONObject tempObj = jsonArray.getJSONObject(index);
                    String entityName = String.valueOf(tempObj.get("dataEntityName"));
                    if (StringUtils.isEmpty(entityName)) {
                        continue;
                    }
                    String[] items = entityName.split("##");
                    pdSave.put("COLUMN_NAME_EN", items[0]);
                    // 缓存COLUMN_NAME_EN
                    columnsMap.put(items[0], "");
                    // 判断是否已存在, 已存在不发生变化
                    PageData pdExist = columnsService.findByEnName(pdSave);
                    if (pdExist != null) {
                        // 可能SEARCH标志, SORTNO发生变化,需要更新
                        pdExist.put("SEARCH", tempObj.get("searchItem") == null ? "0" : "1");   //检索项标志
                        pdExist.put("SORT_NO", (index + 1) * 1000);        //排序号
                        columnsService.edit(pdExist);
                        continue;
                    }
                    // 系统默认列, 不发生变化
                    String indexesId = "";
                    PageData columns = null;
                    if (isSysDefault(items[0])) {
                        indexesId = getSysDefaultIndexesId(items[0]);
                        columns = new PageData();
                        columns.put("SYS_DEFAUTL", "1");
                        columns.put("INDEXES_ID", indexesId);
                    } else {
                        // 不存在,插入操作
                        columns = indexesService.findByEnName(pdSave);
                    }
                    pdSave.put("COLUMNS_ID", this.get32UUID());    //主键
                    pdSave.put("INDEXES_ID", columns.get("INDEXES_ID"));    //entityName ->split to index_id
                    pdSave.put("SEARCH", tempObj.get("searchItem") == null ? "0" : "1");   //检索项标志
                    pdSave.put("SORT_NO", (index + 1) * 1000);        //排序号
                    columnsService.save(pdSave);

                    // 物理插入列
                    // 只针对用户自定义指标，系统默认指标会自动插入数据库
                    if ("0".equals(columns.getString("SYS_DEFAUTL"))) {
                        PageData tables = tablesService.findById(pdSave);
                        columns.put("TABLE_NAME_EN", tables.get("TABLE_NAME_EN"));
                        DBProxy proxy = new DBProxy();
                        columns = proxy.resetDataType(columns);
                        columnsService.addColumn(columns);
                    }

                }
                // 再循环数据库已存columns数据
                List<PageData> listExists = columnsService.listAll(pdSave);
                for (int i = 0; i < listExists.size(); i++) {
                    PageData pdExists = listExists.get(i);
                    String columnExists = pdExists.getString("COLUMN_NAME_EN");
                    // 已经存在数据库中, 无变化
                    if (columnsMap.containsKey(columnExists)) {
                        continue;
                    }
                    // 有外联关系的master数据不能删除
                    pdExists.put("CORP_ID", Jurisdiction.getCorpId());
                    List<PageData> usedList = multitableruleService.findByJoinColumnsId(pdExists);
                    if (usedList.size() > 0) {
                        warning = "表单<" + tableName + ">的元素<" + pdExists.getString("COLUMN_NAME_CN") + ">被表单<";
                        for (int j = 0; j < usedList.size(); j++) {
                            PageData pdUsed = usedList.get(j);
                            String tmp = pdUsed.getString("TABLE_NAME");
                            warning += tmp;
                            if (j < usedList.size() - 1) {
                                warning += ",";
                            }
                        }
                        warning += ">引用, 不能删除!";
                    } else {
                        // 否则,删除columnExists
//                    PageData columns = indexesService.findById(pdExists);
                        columnsService.delete(pdExists);

                        pdExists.put("TABLE_NAME_EN", tableEnName);
                        DBProxy proxy = new DBProxy();
                        pdExists = proxy.resetDataType(pdExists);
                        columnsService.dropColumn(pdExists);
                    }
                }
                // 更新JSON_DATA
                PageData pdTable = new PageData();
                pdTable.put("TABLE_NAME", tableName);
                pdTable.put("TABLE_NAME_EN", tableEnName);
                pdTable.put("TABLES_ID", tableId);
                pdTable.put("SORT_NO", 0);
                pdTable.put("STATUS", "0");
                pdTable.put("JSON_DATA", encodedJsonData);   //CARD数据

                //更新JSON_DATA数据
                tablesService.edit(pdTable);
            }
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("msg", e.getMessage());
            return ret;
        }

        ret.put("msg", tableId);
        ret.put("success", true);
        if (warning != null) {
            // 没有删除关联列
            ret.put("warning", warning);
        }
        return ret;
    }

    /**
     * 采用Formeo框架，通过拖拽方式实现单表的创建
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/card")
    public ModelAndView list(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表SingleTableBusinessRule");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        } //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        /*
         * 公开表单,默认为追加模式,所以生成url的时候没有添加dataEditFlag标志
         * Fixed bug: 如果没有这个标志,会导致关联字段不能弹出.
         * winbill. 20190628
         */
        if (StringUtils.isEmpty(pd.getString("dataEditFlag"))) {
            pd.put("dataEditFlag", "add");
        }
        // Add CORP_ID Winbill. 20170301 ==>
        pd.put("CORP_ID", Jurisdiction.getCorpId());
        // Add CORP_ID Winbill. 20170301 <==
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        pd.put("CHECKED", "1");
        page.setPd(pd);
        String corpId = pd.getString("CORP_ID");    // 提交人公司ID
        String tablesId = pd.getString("TABLES_ID");
        if (StringUtil.strIsNotEmpty(tablesId)) {
            PageData tables = tablesService.findById(pd);
            String jsonData = tables.getString("JSON_DATA");
            pd.put("JSON_DATA", jsonData);
            pd.put("TABLE_NAME", tables.getString("TABLE_NAME"));
            pd.put("TABLE_NAME_EN", tables.getString("TABLE_NAME_EN"));
            pd.put("REMARK", tables.getString("REMARK"));
        }
        mv.addObject("formEditFlag", pd.getString("formEditFlag"));
        mv.addObject("dataEditFlag", pd.getString("dataEditFlag"));
        mv.addObject("DATA_ID", pd.getString("DATA_ID"));
        String userData = pd.getString("USER_DATA");
        mv.addObject("USER_DATA", (userData == null) ? "{}" : userData);
        // Get columns definition
        Map<String, Object> map = getAllColumnsList(page, Jurisdiction.getTopCorpId());

        List<Column> columnList = (List<Column>) map.get("columnList");
        List<PageData> indexesList = (List<PageData>) map.get("indexesList");
        List<PageData> dimList = (List<PageData>) map.get("dimList");

        JSONArray jsonarray = JSONArray.fromObject(indexesList);
        mv.addObject("indexesList", jsonarray);
        jsonarray = JSONArray.fromObject(dimList);
        mv.addObject("dimList", jsonarray);
//        if ("edit".equals(dataEditFlag) && StringUtil.strIsNotEmpty(dataId)) {
//            templateService.findById();
//            commonService.searchTable();
//        }
//		List<PageData>	varList = singletablebusinessruleService.list(page);	//列出SingleTableBusinessRule列表
//		mv.setViewName("/business/singletablebusinessrule/singletablebusinessrule_list");
//		mv.addObject("varList", varList);
//		mv.addObject("pd", pd);
//		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
//		
//		List<PageData>	indexesList = indexesService.listAll(pd);
//		mv.addObject("indexesList", indexesList);
//		List<Dictionaries> list = dictionariesService.listSubDictByParentId(DICTIONARIES_ID);
//		mv.addObject("operatorList", list);

        mv.addObject("pd", pd);
        mv.setViewName("/business/card/card_single");
        return mv;
    }

    @RequestMapping(value = "/formeo-test")
    public ModelAndView formeoTest(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表SingleTableBusinessRule");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        } //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        // Add CORP_ID Winbill. 20170301 ==>
        pd.put("CORP_ID", Jurisdiction.getCorpId());
        // Add CORP_ID Winbill. 20170301 <==
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
//		List<PageData>	varList = singletablebusinessruleService.list(page);	//列出SingleTableBusinessRule列表
//		mv.setViewName("/business/singletablebusinessrule/singletablebusinessrule_list");
//		mv.addObject("varList", varList);
//		mv.addObject("pd", pd);
//		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
//		
//		List<PageData>	indexesList = indexesService.listAll(pd);
//		mv.addObject("indexesList", indexesList);
//		List<Dictionaries> list = dictionariesService.listSubDictByParentId(DICTIONARIES_ID);
//		mv.addObject("operatorList", list);

        mv.setViewName("/business/card/formeo-test");
        return mv;
    }

    /**
     * 返回当前用户能够访问的数据实体（指标），能否formeo使用的
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getListService", method = RequestMethod.POST)
    @ResponseBody
    public Map getListService(/*String CORP_ID, String TABLES_ID*/) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表Columns服务");
        // 保持顺序
        Map<String, Object> ret = new HashMap<String, Object>();
        boolean bRet = true;
        Page page = new Page();
        PageData pd = this.getPageData();
        pd.put("CORP_ID", Jurisdiction.getCorpId());
        page.setPd(pd);
        pd.put("SYS_DEFAUTL", "0");
        List<PageData> varList = indexesService.listAll(pd);    //列出Indexes列表

        //
        pd.put("CORP_ID", Jurisdiction.getTopCorpId());
        for (int index = 0; index < varList.size(); index++) {
            PageData item = varList.get(index);
            if (item.get("DATA_SOURCE") == null || "".equals(item.get("DATA_SOURCE"))) {
                continue;
            }
            String dataSource = String.valueOf(item.get("DATA_SOURCE"));
            pd.put("DICT_ID", dataSource);
            List<PageData> dictList = dictService.listAllForControls(pd);
            JSONArray dictArray = JSONArray.fromObject(dictList);
            item.put("DATA_SOURCE", dictArray.toString());
            varList.set(index, item);
        }

        JSONArray jsonArray = JSONArray.fromObject(varList);
        ret.put("success", bRet);
        ret.put("data", jsonArray);
        return ret;
    }

    private JSONObject createJsonObject(PageData pd) {
        // attrs
        JSONObject attrsObj = new JSONObject();
        attrsObj.put("required", false);
        attrsObj.put("className", "");
        // config
        JSONObject configObj = new JSONObject();
        JSONArray disabledAttrsArray = new JSONArray();
        disabledAttrsArray.add("type");
        configObj.put("disabledAttrs", disabledAttrsArray);
        configObj.put("label", pd.getString("COLUMN_NAME_CN"));
        // meta
        JSONObject metaObj = new JSONObject();
        metaObj.put("group", "common");
        metaObj.put("icon", "text-input");
        metaObj.put("id", pd.getString("INDEXES_ID"));

        // item object
        JSONObject itemObj = new JSONObject();
        if ("STRING".equals(pd.getString("DATA_TYPE_NAME"))) {
            itemObj.put("tag", "input");
            attrsObj.put("type", "text");
        } else if ("DATE".equals(pd.getString("DATA_TYPE_NAME"))) {
            itemObj.put("tag", "input");
            attrsObj.put("type", "date");
        } else if ("DATETIME".equals(pd.getString("DATA_TYPE_NAME"))) {
            itemObj.put("tag", "input");
            attrsObj.put("type", "date");
        } else if ("BOOL".equals(pd.getString("DATA_TYPE_NAME"))) {
            itemObj.put("tag", "input");
            attrsObj.put("type", "checkbox");
        } else if ("INTEGER".equals(pd.getString("DATA_TYPE_NAME"))) {
            itemObj.put("tag", "input");
            attrsObj.put("type", "number");
        } else if ("NUMBER".equals(pd.getString("DATA_TYPE_NAME"))) {
            itemObj.put("tag", "input");
            attrsObj.put("type", "number");
        }
        itemObj.put("attrs", attrsObj);
        itemObj.put("config", configObj);
        itemObj.put("meta", metaObj);
        itemObj.put("fMap", "attrs.value");
        return itemObj;
    }

    /**
     * 根据id,sortNo,search三个字段来比较是否新增/删除/修改
     *
     * @param oldIndex
     * @param nowIndex
     * @return
     */
    public JSONObject classify(JSONArray oldIndex, JSONArray nowIndex) {
        JSONObject result = new JSONObject();
        JSONArray addArray = new JSONArray();
        JSONArray modArray = new JSONArray();
        JSONArray delArray = new JSONArray();
        for (int i = 0; i < nowIndex.size(); i++) {
            JSONObject ob = nowIndex.getJSONObject(i);
            String indexId = ob.getString("INDEXS_ID");
            String sortNo = ob.getString("SORT_NO");
            String search = ob.getString("SEARCH");
            boolean findIndex = false; // 是否已有字段
            for (int j = 0; j < oldIndex.size(); j++) {
                JSONObject oldob = oldIndex.getJSONObject(j);
                String oldindexId = oldob.getString("INDEXS_ID");
                String oldsortNo = oldob.getString("SORT_NO");
                String oldsearch = oldob.getString("SEARCH");
                // 旧字段中有此id
                if (indexId.equals(oldindexId)) {
                    findIndex = true;
                    // 字段其他属性也一样, 说明不用修改
                    if (sortNo.equals(oldsortNo) && search.equals(oldsearch)) {

                        // 字段有修改, 放入修改列表
                    } else {
                        modArray.add(ob);
                    }
                    oldIndex.discard(j);// 旧字段列表中去除,剩下的就是旧表有,新表无的字段,说明是要要删除掉的
                    break;
                } else {
                    continue;
                }
            }
            // 没在旧字段列表中找到id,说明是新增加的指标字段
            if (!findIndex) {
                addArray.add(ob);
            }
        }
        result.put("add", addArray);
        result.put("mod", modArray);
        result.put("del", oldIndex);// 留下来的都是新表中没有的字段,是要删除的字段;

        return result;
    }

    /**
     * 保存表单数据到各独立表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveData() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增表单数据");
        String corpId = Jurisdiction.getCorpId();
        String userId = Jurisdiction.getUserId();

        Map<String, Object> ret = new HashMap<String, Object>();
        boolean bRet = false;
        try {
            PageData pd = this.getPageData();
            String dataDate = pd.getString("DATA_DATE");
            String tablesId = pd.getString("TABLES_ID");
            String transId = pd.getString("TRANS_ID");
            String formData = pd.getString("DATA");
            String dataEditFlag = pd.getString("dataEditFlag");
            String columnsDef = pd.getString("COLUMNS_DEF");
            Map<String, Object> colDef = JsonHelper.parseJSON2Map(columnsDef);
            // 新增记录
            formData = URLDecoder.decode(formData, "UTF-8");
            if (formData.startsWith("\"")) {
                formData = formData.substring(1);
            }
            if (formData.endsWith("\"")) {
                formData = formData.substring(0, formData.length() - 1);
            }
            JSONObject jsonObject = JSONObject.fromObject(formData);
            // Prepare data
            PageData data = new PageData();
            for (Object key : jsonObject.keySet()) {
                data.put(key, jsonObject.get(key));
            }
            // 非用户自定义字段
            data.put("CORP_ID", corpId);
            if (dataDate != null) {
                data.put("DATA_DATE", dataDate);
            }
            data.put("SORT_NO", 0);
            data.put("PARENT_ID", "");
            data.put("LEVEL", "1");
            if (Tools.isEmpty(transId)) {
                // 对于不需要审核的记录，默认可用状态为可用
                data.put("STATUS", "1");
            } else {
                // 对于需要审核的记录，默认可用状态为不可用，审批通过之后才可用
                data.put("STATUS", "0");
            }
            data.put("CREATE_ID", Jurisdiction.getUsername());
            data.put("CREATE_TIME", getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
            data.put("UPDATE_TIME", data.getString("CREATE_TIME"));
            if ("add".equals(dataEditFlag)) {
                templateService.insert(tablesId, data, colDef);
            } else if ("edit".equals(dataEditFlag)) {
                templateService.save(tablesId, data, colDef);
            }

            // 更新B_TRANS表的commit flag状态
            if (!Tools.isEmpty(transId)) {
                data.clear();
                data.put("TRANS_ID", transId);
                data.put("COMMIT_TIME", Tools.date2Str(new Date()));
                data.put("COMMIT_FLAG", "1"); //提交标志：0：驳回 1：已保存 2：待审核 3：一审通过 4：二审通过
                data.put("COMMIT_USER", userId);
                transService.editFlag(data);
            }
            bRet = true;
        } catch (Exception e) {
            ret.put("msg", e.getMessage());
        }
        ret.put("success", bRet);
        return ret;
    }

    /**
     * 去修改页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goPopupDict")
    public ModelAndView goPopupDict() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String param = pd.getString("param");
        param = URLDecoder.decode(param, "UTF-8");
        JSONObject jsonObject = JSONObject.fromObject(param);

        String dictKey = "";
        if (jsonObject.has("DICT_KEY")) {
            dictKey = jsonObject.getString("DICT_KEY");
        }
        String dictValue = "";
        if (jsonObject.has("DICT_VALUE")) {
            dictValue = jsonObject.getString("DICT_VALUE");
        }
        String showCount = jsonObject.getString("showCount");
        String currentPage = jsonObject.getString("currentPage");
        Page page = new Page();
        if (Tools.isEmpty(showCount)) {
            page.setShowCount(10);
        } else {
            page.setShowCount(Integer.parseInt(showCount));
        }
        if (Tools.isEmpty(currentPage)) {
            page.setCurrentPage(1);     // from 1
        } else {
            page.setCurrentPage(Integer.parseInt(currentPage));
        }
        PageData pdCommon = new PageData();
        pdCommon.put("tablesId", jsonObject.getString("tablesId"));
        pdCommon.put("joinColumnsId", jsonObject.getString("joinColumnsId"));
        pdCommon.put("joinColumnsIdAlias", "_KEY_");
        pdCommon.put("selectColumnsId", jsonObject.getString("selectColumnsId"));
        pdCommon.put("selectColumnsIdAlias", "_VALUE_");
        pdCommon.put("corpId", jsonObject.getString("corpId"));
        if (jsonObject.has("keywords")) {
            pdCommon.put("keywords", jsonObject.getString("keywords"));
        }
        long totalResult = commonService.getColumnIDMapCount(pdCommon, page);
        page.setTotalResult((int) totalResult);
        List<PageData> varList = commonService.getColumnIDMap(pdCommon, page);

        mv.addObject("varList", varList);
        mv.addObject("label", jsonObject.getString("label"));
        mv.addObject("DICT_KEY", dictKey);
        mv.addObject("DICT_VALUE", dictValue);

        mv.addObject("pd", pdCommon);
        page.setTotalResult((int) totalResult);
        page.getTotalPage();
        mv.addObject("pageStr", page.getPageString());
        mv.setViewName("/business/card/card_dict");

        return mv;
    }
}
