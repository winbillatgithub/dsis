package com.as.controller.business.tablerule;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.as.util.Jurisdiction;
import com.as.service.business.singletablebusinessrule.SingleTableBusinessRuleManager;
import com.as.service.business.tablerule.TableRuleManager;
import com.as.service.business.tables.TablesManager;

/** 
 * 说明：模板业务规则
 * 创建人：antispy
 * 创建时间：2016-09-20
 */
@Controller
@RequestMapping(value="/tablerule")
public class TableRuleController extends BaseController {
	
	String menuUrl = "tablerule/list.do"; //菜单地址(权限用)
	@Resource(name="tableruleService")
	private TableRuleManager tableruleService;
	// 模板
	@Resource(name="tablesService")
	private TablesManager tablesService;
	@Resource(name="singletablebusinessruleService")
	private SingleTableBusinessRuleManager singletablebusinessruleService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增TableRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("TABLERULE_ID", this.get32UUID());	//主键
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		tableruleService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除TableRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		tableruleService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改TableRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		tableruleService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表TableRule");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = tableruleService.list(page);	//列出TableRule列表
		mv.setViewName("/business/tablerule/tablerule_list");
		for(int index = 0; index < varList.size(); index ++) {
			PageData item = varList.get(index);
			String OUT_FIELD = item.getString("OUT_FIELD_CN");
			String IN_FIELD1 = item.getString("IN_FIELD1_CN");
			String IN_FIELD2 = item.getString("IN_FIELD2_CN");
			String OPERATOR_FIELD = item.getString("OPERATOR_FIELD");
			String ruleContent = OUT_FIELD + " = " + IN_FIELD1 + " " + OPERATOR_FIELD + " " + IN_FIELD2;
			item.put("RULE_CONTENT", ruleContent);
			varList.set(index, item);
		}
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		List<PageData>	rulesList = singletablebusinessruleService.listAll(pd);
		mv.addObject("rulesList", rulesList);
		
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
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		mv.setViewName("/business/tablerule/tablerule_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		List<PageData>	rulesList = singletablebusinessruleService.listAll(pd);
		mv.addObject("rulesList", rulesList);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = tableruleService.findById(pd);	//根据ID读取

		String OUT_FIELD = pd.getString("OUT_FIELD_CN");
		String IN_FIELD1 = pd.getString("IN_FIELD1_CN");
		String IN_FIELD2 = pd.getString("IN_FIELD2_CN");
		String OPERATOR_FIELD = pd.getString("OPERATOR_FIELD");
		String ruleContent = OUT_FIELD + " = " + IN_FIELD1 + " " + OPERATOR_FIELD + " " + IN_FIELD2;
		pd.put("RULE_CONTENT", ruleContent);

		mv.setViewName("/business/tablerule/tablerule_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);

		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		List<PageData>	rulesList = singletablebusinessruleService.listAll(pd);
		mv.addObject("rulesList", rulesList);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除TableRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			tableruleService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出TableRule到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("模板名称");	//1
		titles.add("指标ID");	//2
		titles.add("规则内容");	//3
		titles.add("排序码");	//4
		dataMap.put("titles", titles);
		List<PageData> varOList = tableruleService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TABLE_ID"));	//1
			vpd.put("var2", varOList.get(i).getString("RULE_ID"));	//2
			vpd.put("var3", varOList.get(i).getString("RULE_CONTENT"));	//3
			vpd.put("var4", varOList.get(i).get("SORT_NO").toString());	//4
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
}
