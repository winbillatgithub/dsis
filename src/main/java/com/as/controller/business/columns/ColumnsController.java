package com.as.controller.business.columns;

import com.as.controller.base.BaseController;
import com.as.db.proxy.DBProxy;
import com.as.entity.Page;
import com.as.service.business.columns.ColumnsManager;
import com.as.service.business.indexes.IndexesManager;
import com.as.service.business.tables.TablesManager;
import com.as.util.AppUtil;
import com.as.util.Jurisdiction;
import com.as.util.ObjectExcelView;
import com.as.util.PageData;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * 说明：模板指标对照表
 * 创建人：antispy
 * 创建时间：2016-09-09
 */
@Controller
@RequestMapping(value="/columns")
public class ColumnsController extends BaseController {

	String menuUrl = "columns/list.do"; //菜单地址(权限用)
	@Resource(name="columnsService")
	private ColumnsManager columnsService;
	@Resource(name="tablesService")
	private TablesManager tablesService;
	@Resource(name="indexesService")
	private IndexesManager indexesService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Columns");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("COLUMNS_ID", this.get32UUID());	//主键
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		columnsService.save(pd);

		// 物理插入列
		PageData columns = indexesService.findById(pd);
		// 只针对用户自定义指标，系统默认指标会自动插入数据库
		if ("0".equals(columns.getString("SYS_DEFAUTL"))) {
			PageData tables = tablesService.findById(pd);
			columns.put("TABLE_NAME_EN", tables.get("TABLE_NAME_EN"));
			DBProxy proxy = new DBProxy();
			columns = proxy.resetDataType(columns);
			columnsService.addColumn(columns);
		}

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
		logBefore(logger, Jurisdiction.getUsername()+"删除Columns");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = this.getPageData();
		
		// 物理插入列
		PageData tables = tablesService.findById(pd);
		PageData columns = indexesService.findById(pd);

		columnsService.delete(pd);

		columns.put("TABLE_NAME_EN", tables.get("TABLE_NAME_EN"));
		DBProxy proxy = new DBProxy();
		columns = proxy.resetDataType(columns);
		columnsService.dropColumn(columns);

		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Columns");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		columnsService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Columns");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = columnsService.list(page);	//列出Columns列表
		mv.setViewName("/business/columns/columns_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		pd.remove("keywords");
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		List<PageData>	indexesList = indexesService.listAll(pd);
		mv.addObject("indexesList", indexesList);
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
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		mv.setViewName("/business/columns/columns_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		List<PageData>	indexesList = indexesService.listAll(pd);
		mv.addObject("indexesList", indexesList);
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
		pd = columnsService.findById(pd);	//根据ID读取
		mv.setViewName("/business/columns/columns_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		List<PageData>	indexesList = indexesService.listAll(pd);
		mv.addObject("indexesList", indexesList);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Columns");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = this.getPageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			columnsService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Columns到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("模板名称");	//1
		titles.add("指标ID");	//2
		titles.add("排序号");	//3
		dataMap.put("titles", titles);
		List<PageData> varOList = columnsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TABLES_ID"));	//1
			vpd.put("var2", varOList.get(i).getString("INDEXES_ID"));	//2
			vpd.put("var3", varOList.get(i).get("SORT_NO").toString());	//3
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}

	/**
	 * 根据表名获取对应的Columns信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getListService", method = RequestMethod.POST)
	@ResponseBody
	public Map getListService(/*String CORP_ID, String TABLES_ID*/) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Columns服务");
		// 保持顺序
		Map<String, Object> ret = new HashMap<String, Object>();
		boolean bRet = false;
		Page page = new Page();
		PageData pd = this.getPageData();
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		page.setPd(pd);
		List<PageData>	varList = null;
		try {
			page.setShowCount(MAX_SHOW_COUNT);
			varList = columnsService.list(page);	//列出Columns列表
			bRet = true;
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("msg", e.getMessage());
		}
		ret.put("success", bRet);
		ret.put("data", varList);
		return ret;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
