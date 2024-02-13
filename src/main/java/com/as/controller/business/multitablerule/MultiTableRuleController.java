package com.as.controller.business.multitablerule;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import com.as.util.Tools;
import com.as.util.UuidUtil;
import com.as.util.Jurisdiction;
import com.as.service.business.columns.ColumnsManager;
import com.as.service.business.common.CommonManager;
import com.as.service.business.indexes.IndexesManager;
import com.as.service.business.listlistrule.ListListRuleManager;
import com.as.service.business.listlistruledetail.ListListRuleDetailManager;
import com.as.service.business.multitablerule.MultiTableRuleManager;
import com.as.service.business.tables.TablesManager;

/** 
 * 说明：多表规则
 * 创建人：antispy
 * 创建时间：2017-03-05
 */
@Controller
@RequestMapping(value="/multitablerule")
public class MultiTableRuleController extends BaseController {

	String menuUrl = "multitablerule/list.do"; //菜单地址(权限用)
	@Resource(name="multitableruleService")
	private MultiTableRuleManager multitableruleService;
	@Resource(name="columnsService")
	private ColumnsManager columnsService;
	@Resource(name="tablesService")
	private TablesManager tablesService;
	@Resource(name="listlistruleService")
	private ListListRuleManager listlistruleService;
	@Resource(name="listlistruledetailService")
	private ListListRuleDetailManager listlistruledetailService;
	@Resource(name="commonService")
	private CommonManager commonService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增MultiTableRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("MULTITABLERULE_ID", this.get32UUID());	//主键
		pd.put("RSV_STR1", "");	//扩展字段1
		pd.put("RSV_STR2", "");	//扩展字段2
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());	//组织编码
		multitableruleService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除MultiTableRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		multitableruleService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改MultiTableRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		multitableruleService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表MultiTableRule");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());

		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = multitableruleService.list(page);	//列出MultiTableRule列表
		mv.setViewName("/business/multitablerule/multitablerule_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		pd.remove("keywords");
		//模板列表
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
		mv.setViewName("/business/multitablerule/multitablerule_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);

		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		Page page = new Page();
		page.setPd(pd);
		List<PageData>	columnsList = columnsService.list(page);
		mv.addObject("columnsList", columnsList);

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
		pd = multitableruleService.findById(pd);	//根据ID读取
		mv.setViewName("/business/multitablerule/multitablerule_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);

		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		Page page = new Page();
		page.setPd(pd);
		List<PageData>	columnsList = columnsService.list(page);
		mv.addObject("columnsList", columnsList);

		return mv;
	}

    /**去修改页面
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/goDragDrop")
    public ModelAndView goDragDrop()throws Exception{
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        pd.put("CORP_ID", Jurisdiction.getTopCorpId());
        mv.addObject("msg", "edit");
        mv.addObject("pd", pd);

        // 从配置表中获取Json数据（如果已存在）
        
        String initData="";
        List<PageData> list_json=commonService.execute("select * from b_corp_json where type=1 and CORP_ID='"+Jurisdiction.getTopCorpId()+"'");
        if(list_json.size()>0) {
        	initData=(String) list_json.get(0).get("JSON");
        }
        if (Tools.isEmpty(initData)) {
            initData = "{}";
        }
        mv.addObject("initdata", initData);

        // 获取所有表数据
        List<PageData>	tableList = tablesService.listAll(pd);
        mv.addObject("tableList", tableList);

        // 获取所有列数据
        Page page = new Page();
        page.setPd(pd);
        page.setShowCount(MAX_SHOW_COUNT);
        List<PageData>	columnsList = columnsService.list(page);
        mv.addObject("columnsList", columnsList);

        // 获取MetaData
        mv.addObject("metadata",getMetaData(tableList,columnsList));

        mv.setViewName("/business/multitablerule/multitable_dragdrop");

        return mv;
    }

    /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除MultiTableRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			multitableruleService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出MultiTableRule到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("规则名称");	//1
		titles.add("模板名称");	//2
		titles.add("字段");	//3
		titles.add("关联模板名称");	//4
		titles.add("关联字段");	//5
		titles.add("关联条件");	//6
		titles.add("扩展字段1");	//7
		titles.add("扩展字段2");	//8
		titles.add("组织编码");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = multitableruleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("NAME"));	//1
			vpd.put("var2", varOList.get(i).getString("TABLES_ID"));	//2
			vpd.put("var3", varOList.get(i).getString("COLUMNS_ID"));	//3
			vpd.put("var4", varOList.get(i).getString("JOIN_TABLES_ID"));	//4
			vpd.put("var5", varOList.get(i).getString("JOIN_COLUMNS_ID"));	//5
			vpd.put("var6", varOList.get(i).getString("JOIN_CONDITION"));	//6
			vpd.put("var7", varOList.get(i).getString("RSV_STR1"));	//7
			vpd.put("var8", varOList.get(i).getString("RSV_STR2"));	//8
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
			saveData(jsonData);
			ret.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("success", false);
			ret.put("msg", e.getMessage());
		}

		return ret;
	}
	private void saveData(String jsonData) throws Exception {
		Map<String, Object>  map = parseJSON2Map(jsonData);
		Map<String, String> model = (Map<String, String>) map.get("model");
		Map<String, Object> nodes = (Map<String, Object>) map.get("nodes");
		Map<String, Object> lines = (Map<String, Object>) map.get("lines");
		List<String> codeMap=new ArrayList<String>();
		for (Iterator<String> it = nodes.keySet().iterator(); it.hasNext(); ) {
			String s = (String) it.next();
			Map<String, String> node = (Map<String, String>) nodes.get(s);
			if(codeMap.contains(node.get("code"))) {
				throw new Exception("视图中表不能重复");
			}
			codeMap.add(node.get("code"));
		}
		
		String CORP_ID=Jurisdiction.getTopCorpId();
		
		
		List<PageData> list=new ArrayList<PageData>();
		for (Iterator<String> it = lines.keySet().iterator(); it.hasNext(); ) {
			String s = (String) it.next();
			Map<String, String> line = (Map<String, String>) lines.get(s);
			PageData pd = this.getPageData();
			
			pd.put("MULTITABLERULE_ID", this.get32UUID());	//主键
			pd.put("NAME", "");	
			pd.put("TABLES_ID", line.get("fromTable"));	
			pd.put("COLUMNS_ID", line.get("fromCol"));	
			pd.put("JOIN_TABLES_ID", line.get("toTable"));	
			pd.put("JOIN_COLUMNS_ID", line.get("toCol"));	
			String label = line.get("label");
			if ("外连".equals(label)) {
				pd.put("JOIN_CONDITION",1);	
				if(line.get("fromTable").equals(line.get("toTable"))) {
					throw new Exception("外联:同一个表不能外联");
				}
				if(!line.get("fromIndex").equals(line.get("toIndex"))) {
					throw new Exception("外联:"+line.get("fromTable")+"与"+line.get("toTable")+"连接字段的来源不一致");
				}
			}else if ("内连".equals(label)) {
				if(!line.get("fromTable").equals(line.get("toTable"))) {
					throw new Exception("内联:不同一个表内能外联");
				}
				if(line.get("fromIndex").equals(line.get("toIndex"))) {
					throw new Exception("内联:相同字段的来源不能一致");
				}
				pd.put("JOIN_CONDITION",0);	
			}
			pd.put("RSV_STR1", "");	//扩展字段1
			pd.put("RSV_STR2", "");	//扩展字段2
			pd.put("CORP_ID", CORP_ID);	//组织编码
			list.add(pd);
		}
		List<PageData> list_json=commonService.execute("select * from b_corp_json where type=1 and CORP_ID='"+CORP_ID+"'");
		if (list_json.size()==0) {
			String sql="INSERT INTO b_corp_json (JSON_ID,CORP_ID,TYPE,JSON,OPERATOR,CREATE_TIME,UPDATE_TIME) VALUES "
					+ "	('"+UuidUtil.get32UUID()+"','"+CORP_ID+"','1','"+jsonData+"','"+Jurisdiction.getUsername()+"','"+Tools.date2Str(new Date())+"','"+Tools.date2Str(new Date())+"')";
			commonService.execute(sql);
		} else {
			commonService.execute("update b_corp_json set JSON='"+jsonData+"',UPDATE_TIME='"+Tools.date2Str(new Date())+"',OPERATOR='"+Jurisdiction.getUsername()+"'  where JSON_ID='"+list_json.get(0).getString("JSON_ID")+"'");
		}
		
		multitableruleService.deleteCORP(CORP_ID);
		multitableruleService.saveList(list);
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
}
