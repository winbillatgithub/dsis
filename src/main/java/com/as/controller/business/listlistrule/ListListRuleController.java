package com.as.controller.business.listlistrule;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.as.controller.base.BaseController;
import com.as.entity.Page;
import com.as.util.AppUtil;
import com.as.util.ObjectExcelView;
import com.as.util.PageData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.as.util.Jurisdiction;
import com.as.service.business.columns.ColumnsManager;
import com.as.service.business.listlistrule.ListListRuleManager;
import com.as.service.business.listlistruledetail.ListListRuleDetailManager;
import com.as.service.business.tables.TablesManager;

/** 
 * 说明：ListList规则
 * 创建人：antispy
 * 创建时间：2017-03-28
 */
@Controller
@RequestMapping(value="/listlistrule")
public class ListListRuleController extends BaseController {
	
	String menuUrl = "listlistrule/list.do"; //菜单地址(权限用)
	@Resource(name="listlistruleService")
	private ListListRuleManager listlistruleService;
	@Resource(name="columnsService")
	private ColumnsManager columnsService;
	@Resource(name="tablesService")
	private TablesManager tablesService;
	@Resource(name="listlistruledetailService")
	private ListListRuleDetailManager listlistruledetailService;

	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增ListListRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("LISTLISTRULE_ID", this.get32UUID());	//主键
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());	//组织编码
		listlistruleService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除ListListRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		listlistruleService.delete(pd);
		// delete detail table
		listlistruledetailService.deleteByRuleId(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改ListListRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		listlistruleService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表ListListRule");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());

		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = listlistruleService.list(page);	//列出ListListRule列表
		mv.setViewName("/business/listlistrule/listlistrule_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		
		//模板列表
		pd.remove("keywords");
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		page.setShowCount(MAX_SHOW_COUNT);
		List<PageData>	columnsList = columnsService.list(page);
		mv.addObject("columnsList", columnsList);
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		mv.setViewName("/business/listlistrule/listlistrule_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);

		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		Page page = new Page();
		page.setPd(pd);
		page.setShowCount(MAX_SHOW_COUNT);
		List<PageData>	columnsList = columnsService.list(page);
		mv.addObject("columnsList", columnsList);
		return mv;
	}

	/**去新增页面--DragDrop
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAddDragDrop")
	public ModelAndView goAddDragDrop()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		mv.setViewName("/business/listlistrule/listlistrule_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);

		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		Page page = new Page();
		page.setPd(pd);
		page.setShowCount(MAX_SHOW_COUNT);
		List<PageData>	columnsList = columnsService.list(page);
		mv.addObject("columnsList", columnsList);
		// modify start
		mv.addObject("metadata",getMetaData(tableList,columnsList));
		mv.addObject("initdata",new JSONObject());
		mv.setViewName("/business/listlistrule/dragdrop");
		// modify end
		return mv;
	}

	/**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd = listlistruleService.findById(pd);	//根据ID读取
		mv.setViewName("/business/listlistrule/listlistrule_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);

		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		Page page = new Page();
		page.setPd(pd);
		page.setShowCount(MAX_SHOW_COUNT);
		List<PageData>	columnsList = columnsService.list(page);
		mv.addObject("columnsList", columnsList);

		return mv;
	}	

	/**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditDragDrop")
	public ModelAndView goEditDragDrop()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd = listlistruleService.findById(pd);	//根据ID读取
		mv.setViewName("/business/listlistrule/listlistrule_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);

		//模板列表
//		List<PageData>	tableList = tablesService.listAll(pd);
//		mv.addObject("tableList", tableList);
//		Page page = new Page();
//		page.setPd(pd);
//		page.setShowCount(MAX_SHOW_COUNT);
//		List<PageData>	columnsList = columnsService.list(page);
//		mv.addObject("columnsList", columnsList);
		
		// modify start
		String initJson = pd.getString("JSON_DATA");
		JSONObject initData = JSONObject.fromObject(initJson);
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		Page page = new Page();
		page.setPd(pd);
		page.setShowCount(MAX_SHOW_COUNT);
		List<PageData>	columnsList = columnsService.list(page);
		mv.addObject("columnsList", columnsList);
		mv.addObject("metadata",getMetaData(tableList,columnsList));
		mv.addObject("initdata", initData);
		mv.setViewName("/business/listlistrule/dragdrop");
		// modify end
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除ListListRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			listlistruleService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出ListListRule到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("规则名称");	//1
		titles.add("主表名称");	//2
		titles.add("主表字段");	//3
		titles.add("从表名称");	//4
		titles.add("从表字段");	//5
		titles.add("源表名称");	//6
		titles.add("源表字段");	//7
		titles.add("初始化标志");	//8
		titles.add("组织编码");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = listlistruleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("NAME"));	//1
			vpd.put("var2", varOList.get(i).getString("MASTER_TABLES_ID"));	//2
			vpd.put("var3", varOList.get(i).getString("MASTER_COLUMNS_ID"));	//3
			vpd.put("var4", varOList.get(i).getString("SLAVE_TABLES_ID"));	//4
			vpd.put("var5", varOList.get(i).getString("SLAVE_COLUMNS_ID"));	//5
			vpd.put("var6", varOList.get(i).getString("SOURCE_TABLES_ID"));	//6
			vpd.put("var7", varOList.get(i).getString("SOURCE_COLUMNS_ID"));	//7
			vpd.put("var8", varOList.get(i).getString("INIT_ALL"));	//8
			vpd.put("var9", varOList.get(i).getString("CORP_ID"));	//9
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}

	/**
	 * json 转map
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr){    
        Map<String, Object> map = new HashMap<String, Object>();    
        JSONObject json = JSONObject.fromObject(jsonStr);    
        for(Object k : json.keySet()){    
            Object v = json.get(k);     
            if(v instanceof JSONArray){    
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();    
                Iterator<JSONObject> it = ((JSONArray)v).iterator();    
                while(it.hasNext()){    
                    JSONObject json2 = it.next();    
                    list.add(parseJSON2Map(json2.toString()));    
                }    
                map.put(k.toString(), list);    
            } else {    
                map.put(k.toString(), v);    
            }    
        }    
        return map;    
    }  
	
	/**图模型保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/saveNew" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object saveNew() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增ListListRule");
//		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
//		PageData pd = new PageData();
		PageData pd = this.getPageData();
		String jsonData = pd.getString("data");
		//jsonData = "{\"model\":{\"modelName\":\"出勤规则2\",\"modelDesc\":\"\"},\"nodes\":{\"STUDENT_ROSTER_model_0\":{\"code\":\"STUDENT_ROSTER\",\"modelType\":\"主表\",\"left\":\"83px\",\"top\":\"67px\"},\"STUDENT_ROSTER_ITEM_model_1\":{\"code\":\"STUDENT_ROSTER_ITEM\",\"modelType\":\"从表\",\"left\":\"376px\",\"top\":\"68px\"},\"STUDENT_COURSE_model_2\":{\"code\":\"STUDENT_COURSE\",\"modelType\":\"源表\",\"left\":\"710px\",\"top\":\"64px\"}},\"lines\":{\"demo_line_0\":{\"from\":\"STUDENT_ROSTER_model_0_d9651486cf6b49f19eb8fd87d38279f8\",\"fromTable\":\"STUDENT_ROSTER\",\"fromCol\":\"d9651486cf6b49f19eb8fd87d38279f8\",\"fromType\":\"RightMiddle\",\"to\":\"STUDENT_ROSTER_ITEM_model_1_cbbd6067644449c3991d307d81e58bdb\",\"toTable\":\"STUDENT_ROSTER_ITEM\",\"toCol\":\"cbbd6067644449c3991d307d81e58bdb\",\"toType\":\"LeftMiddle\",\"label\":\"关系映射\",\"twoWay\":true},\"demo_line_1\":{\"from\":\"STUDENT_ROSTER_ITEM_model_1_cbbd6067644449c3991d307d81e58bdb\",\"fromTable\":\"STUDENT_ROSTER_ITEM\",\"fromCol\":\"cbbd6067644449c3991d307d81e58bdb\",\"fromType\":\"RightMiddle\",\"to\":\"STUDENT_COURSE_model_2_f4a11c1461284608beb31cd67c4be851\",\"toTable\":\"STUDENT_COURSE\",\"toCol\":\"f4a11c1461284608beb31cd67c4be851\",\"toType\":\"LeftMiddle\",\"label\":\"关系映射\",\"twoWay\":true},\"demo_line_2\":{\"from\":\"STUDENT_ROSTER_ITEM_model_1_2aa9b77063d248c4bd5b6d72e03cdf28\",\"fromTable\":\"STUDENT_ROSTER_ITEM\",\"fromCol\":\"2aa9b77063d248c4bd5b6d72e03cdf28\",\"fromType\":\"RightMiddle\",\"to\":\"STUDENT_COURSE_model_2_a582817bf1ee4f05a22be7da27d1bd60\",\"toTable\":\"STUDENT_COURSE\",\"toCol\":\"a582817bf1ee4f05a22be7da27d1bd60\",\"toType\":\"LeftMiddle\",\"label\":\"外连\",\"twoWay\":true},\"demo_line_3\":{\"from\":\"STUDENT_ROSTER_ITEM_model_1_3c61e4e4b1b945ab82208b5090220b37\",\"fromTable\":\"STUDENT_ROSTER_ITEM\",\"fromCol\":\"3c61e4e4b1b945ab82208b5090220b37\",\"fromType\":\"LeftMiddle\",\"to\":\"STUDENT_ROSTER_ITEM_model_1_2aa9b77063d248c4bd5b6d72e03cdf28\",\"toTable\":\"STUDENT_ROSTER_ITEM\",\"toCol\":\"2aa9b77063d248c4bd5b6d72e03cdf28\",\"toType\":\"LeftMiddle\",\"label\":\"内连\",\"twoWay\":false},\"demo_line_4\":{\"from\":\"STUDENT_ROSTER_ITEM_model_1_5ef947a94da146a08854abb21cd917ad\",\"fromTable\":\"STUDENT_ROSTER_ITEM\",\"fromCol\":\"5ef947a94da146a08854abb21cd917ad\",\"fromType\":\"LeftMiddle\",\"to\":\"STUDENT_ROSTER_ITEM_model_1_2aa9b77063d248c4bd5b6d72e03cdf28\",\"toTable\":\"STUDENT_ROSTER_ITEM\",\"toCol\":\"2aa9b77063d248c4bd5b6d72e03cdf28\",\"toType\":\"LeftMiddle\",\"label\":\"内连\",\"twoWay\":false}}}";
		System.out.println(jsonData);
		// 从dd获取数据插入listlistrule, listlistruledetail和配置表中
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			String listListRuleId = saveData(jsonData);
			ret.put("success", true);
			ret.put("id", listListRuleId);		// 返回ID给前端
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("success", false);
			ret.put("msg", e.getMessage());
		}

		return ret;
	}
	private String saveData(String jsonData) throws Exception {
		Map<String, Object>  map = parseJSON2Map(jsonData);
		Map<String, String> model = (Map<String, String>) map.get("model");
		Map<String, Object> nodes = (Map<String, Object>) map.get("nodes");
		Map<String, Object> lines = (Map<String, Object>) map.get("lines");
		String ruleName = model.get("modelName");
		if (nodes.size() != 3) {
			throw new Exception("LIST-LIST规则必须有且只有3张表.");
		}
		String masterTableId = null;
		String slaveTableId = null;
		String sourceTableId = null;
		for (Iterator<String> it = nodes.keySet().iterator(); it.hasNext(); ) {
			String s = (String) it.next();
			Map<String, String> node = (Map<String, String>) nodes.get(s);
			String modelType = node.get("modelType");
			if ("主表".equals(modelType)) {
				masterTableId = node.get("code");
			} else if ("从表".equals(modelType)) {
				slaveTableId = node.get("code");
			} else if ("源表".equals(modelType)) {
				sourceTableId = node.get("code");
			} else {
				throw new Exception("不能识别的模式类型:" + modelType);
			}
		}
		String masterColumnId = null;
		String slaveColumnId = null;
		String sourceColumnId = null;
		String outerSlaveColumnId = null;
		String outerSourceColumnId = null;
		String innerSlaveColumnId = null;
		String innerSourceColumnId = null;
		boolean bFound = false;
		for (Iterator<String> it = lines.keySet().iterator(); it.hasNext(); ) {
			String s = (String) it.next();
			Map<String, String> line = (Map<String, String>) lines.get(s);
			String label = line.get("label");
			if ("关系映射".equals(label)) {
				String fromTable = line.get("fromTable");
				String toTable = line.get("toTable");
				if (masterTableId.equals(fromTable) && slaveTableId.equals(toTable)) {
					// 主从关系映射
					masterColumnId = line.get("fromCol");
					slaveColumnId = line.get("toCol");
				} else if (masterTableId.equals(toTable) && slaveTableId.equals(fromTable)) {
					// 主从关系映射
					masterColumnId = line.get("toCol");
					slaveColumnId = line.get("fromCol");
				} else if (slaveTableId.equals(fromTable) && sourceTableId.equals(toTable)) {
					// 从源关系映射
					slaveColumnId = line.get("fromCol");
					sourceColumnId = line.get("toCol");
				} else if (slaveTableId.equals(toTable) && sourceTableId.equals(fromTable)) {
					// 从源关系映射
					slaveColumnId = line.get("toCol");
					sourceColumnId = line.get("fromCol");
				} else {
					throw new Exception("不能和nodes匹配关系映射:fromTable=" + fromTable + ",toTable=" + toTable);
				}
			}
		}
		if (masterColumnId == null || slaveColumnId == null || sourceColumnId == null) {
			throw new Exception("需要主-从，从-源两个关系映射");
		}
		// B_LISTLISTRULE表
		String listListRuleId = this.get32UUID();
		PageData pd = new PageData();
		pd.put("LISTLISTRULE_ID", listListRuleId);		//主键
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());	//组织编码
		pd.put("NAME", ruleName);
		pd.put("MASTER_TABLES_ID", masterTableId);
		pd.put("MASTER_COLUMNS_ID", masterColumnId);
		pd.put("SLAVE_TABLES_ID", slaveTableId);
		pd.put("SLAVE_COLUMNS_ID", slaveColumnId);
		pd.put("SOURCE_TABLES_ID", sourceTableId);
		pd.put("SOURCE_COLUMNS_ID", sourceColumnId);
		pd.put("JSON_DATA", jsonData);
		pd.put("INIT_ALL", "1");
		listlistruleService.save(pd);

		int outerJoin = 0;
		for (Iterator<String> it = lines.keySet().iterator(); it.hasNext(); ) {
			String s = (String) it.next();
			Map<String, String> line = (Map<String, String>) lines.get(s);
			String label = line.get("label");
			if ("外连".equals(label)) {
				outerJoin ++;
				String fromTable = line.get("fromTable");
				String fromCol = line.get("fromCol");
				String toTable = line.get("toTable");
				String toCol = line.get("toCol");
				// from table is slave table, to table is source table
				if (slaveTableId.equals(fromTable) && sourceTableId.equals(toTable)) {
					outerSlaveColumnId = fromCol;
					outerSourceColumnId = toCol;
				} else if (slaveTableId.equals(toTable) && sourceTableId.equals(fromTable)) {
					outerSlaveColumnId = toCol;
					outerSourceColumnId = fromCol;
				} else {
					throw new Exception("不能匹配从表及源表:fromTable=" + fromTable + ",toTable=" + toTable);
				}
			} else if ("内连".equals(label)) {
				String fromTable = line.get("fromTable");
				String fromCol = line.get("fromCol");
				String toTable = line.get("toTable");
				String toCol = line.get("toCol");
				// from table is slave table, to table is source table
				if (slaveTableId.equals(fromTable) && slaveTableId.equals(toTable)) {
					innerSlaveColumnId = fromCol;
					innerSourceColumnId = toCol;
				} else {
					throw new Exception("只能在从表里内联:fromTable=" + fromTable + ",toTable=" + toTable);
				}
				pd.clear();
				pd.put("LISTLISTRULEDETAIL_ID", this.get32UUID());	//主键
				pd.put("CORP_ID", Jurisdiction.getTopCorpId());		//组织编码
				pd.put("LISTLISTRULE_ID", listListRuleId);			//主键
				pd.put("SLAVE_TABLES_ID", slaveTableId);
				pd.put("SLAVE_COLUMNS_ID", innerSlaveColumnId);
				pd.put("SOURCE_TABLES_ID", slaveTableId);
				pd.put("SOURCE_COLUMNS_ID", innerSourceColumnId);
				pd.put("JOIN_CONDITION", "0");
				listlistruledetailService.save(pd);
			} else if ("关系映射".equals(label)) {
			} else {
				throw new Exception("不能识别的连接类型:" + label);
			}
		}
		if (outerJoin != 1) {
			throw new Exception("有且只能有一个外联");
		}
		// B_LISTLISTRULEDETAIL表
		// 外联-用户设置的外联
		pd.clear();
		pd.put("LISTLISTRULEDETAIL_ID", this.get32UUID());	//主键
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());		//组织编码
		pd.put("LISTLISTRULE_ID", listListRuleId);			//主键
		pd.put("SLAVE_TABLES_ID", slaveTableId);
		pd.put("SLAVE_COLUMNS_ID", outerSlaveColumnId);
		pd.put("SOURCE_TABLES_ID", sourceTableId);
		pd.put("SOURCE_COLUMNS_ID", outerSourceColumnId);
		pd.put("JOIN_CONDITION", "1");
		listlistruledetailService.save(pd);
		// B_LISTLISTRULEDETAIL表
		// 外联-关系映射对应的外联（从表-源表自动外联）
		pd.clear();
		pd.put("LISTLISTRULEDETAIL_ID", this.get32UUID());	//主键
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());		//组织编码
		pd.put("LISTLISTRULE_ID", listListRuleId);			//主键
		pd.put("SLAVE_TABLES_ID", slaveTableId);
		pd.put("SLAVE_COLUMNS_ID", slaveColumnId);
		pd.put("SOURCE_TABLES_ID", slaveTableId);
		pd.put("SOURCE_COLUMNS_ID", sourceColumnId);
		pd.put("JOIN_CONDITION", "1");
		listlistruledetailService.save(pd);
		return listListRuleId;
	}
	
}
