package com.as.controller.business.listlistruledetail;

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
import com.as.service.business.columns.ColumnsManager;
import com.as.service.business.listlistruledetail.ListListRuleDetailManager;
import com.as.service.business.tables.TablesManager;

/** 
 * 说明：ListList规则
 * 创建人：antispy
 * 创建时间：2017-03-28
 */
@Controller
@RequestMapping(value="/listlistruledetail")
public class ListListRuleDetailController extends BaseController {
	
	String menuUrl = "listlistruledetail/list.do"; //菜单地址(权限用)
	@Resource(name="listlistruledetailService")
	private ListListRuleDetailManager listlistruledetailService;
	@Resource(name="columnsService")
	private ColumnsManager columnsService;
	@Resource(name="tablesService")
	private TablesManager tablesService;

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
		pd.put("LISTLISTRULEDETAIL_ID", this.get32UUID());	//主键
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());	//组织编码
		listlistruledetailService.save(pd);
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
		listlistruledetailService.delete(pd);
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
		listlistruledetailService.edit(pd);
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
		//LISTLISTRULE_ID&SLAVE_TABLES_ID&SOURCE_TABLES_ID
		List<PageData>	varList = listlistruledetailService.list(page);	//列出ListListRule列表
		mv.setViewName("/business/listlistruledetail/listlistruledetail_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限

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
		mv.setViewName("/business/listlistruledetail/listlistruledetail_edit");
		mv.addObject("msg", "save");
		//LISTLISTRULE_ID&SLAVE_TABLES_ID&SOURCE_TABLES_ID
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
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd = listlistruledetailService.findById(pd);	//根据ID读取
		mv.setViewName("/business/listlistruledetail/listlistruledetail_edit");
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
			listlistruledetailService.deleteAll(ArrayDATA_IDS);
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
		titles.add("主表名称");	//2
		titles.add("主表字段");	//3
		titles.add("从表名称");	//4
		titles.add("从表字段");	//5
		titles.add("源表名称");	//6
		titles.add("源表字段");	//7
		titles.add("初始化标志");	//8
		titles.add("组织编码");	//9
		dataMap.put("titles", titles);
		List<PageData> varOList = listlistruledetailService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
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
}
