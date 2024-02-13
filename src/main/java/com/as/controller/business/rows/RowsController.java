package com.as.controller.business.rows;

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
import com.as.db.proxy.DBProxy;
import com.as.entity.Page;
import com.as.entity.business.Dimentions;
import com.as.util.AppUtil;
import com.as.util.ObjectExcelView;
import com.as.util.PageData;
import com.as.util.Jurisdiction;
import com.as.service.business.rows.RowsManager;
import com.as.service.business.dimentions.DimentionsManager;
import com.as.service.business.indexes.IndexesManager;
import com.as.service.business.tables.TablesManager;

/** 
 * 说明：模板指标对照表
 * 创建人：antispy
 * 创建时间：2016-09-09
 */
@Controller
@RequestMapping(value="/rows")
public class RowsController extends BaseController {
	
	String menuUrl = "rows/list.do"; //菜单地址(权限用)
	@Resource(name="rowsService")
	private RowsManager rowsService;
	@Resource(name="tablesService")
	private TablesManager tablesService;
	@Resource(name="dimentionsService")
	private DimentionsManager dimentionsService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Rows");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("ROWS_ID", this.get32UUID());	//主键
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		rowsService.save(pd);

		// 物理插入列
		PageData tables = tablesService.findById(pd);
		PageData dim = dimentionsService.findById(pd);
		dim.put("TABLE_NAME_EN", tables.get("TABLE_NAME_EN"));
		dim.put("COLUMN_NAME_EN", dim.get("NAME_EN"));
		dim.put("COLUMN_TYPE", "VARCHAR(200)");
		rowsService.addColumn(dim);

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
		logBefore(logger, Jurisdiction.getUsername()+"删除Rows");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = this.getPageData();

		// 物理插入列
		PageData tables = tablesService.findById(pd);
		PageData dim = dimentionsService.findById(pd);

		rowsService.delete(pd);

		dim.put("TABLE_NAME_EN", tables.get("TABLE_NAME_EN"));
		dim.put("COLUMN_NAME_EN", dim.get("NAME_EN"));
		rowsService.dropColumn(dim);

		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Rows");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		rowsService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Rows");
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
		List<PageData>	varList = rowsService.list(page);	//列出Rows列表
		mv.setViewName("/business/rows/rows_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		List<Dimentions>	dimentionsList = dimentionsService.listSubDictByParentId("0");
		mv.addObject("dimentionsList", dimentionsList);
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
		mv.setViewName("/business/rows/rows_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		List<Dimentions>	dimentionsList = dimentionsService.listSubDictByParentId("0");
		mv.addObject("dimentionsList", dimentionsList);
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
		pd = rowsService.findById(pd);	//根据ID读取
		mv.setViewName("/business/rows/rows_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		List<Dimentions>	dimentionsList = dimentionsService.listSubDictByParentId("0");
		mv.addObject("dimentionsList", dimentionsList);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Rows");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = this.getPageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			rowsService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Rows到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("模板名称");	//1
		titles.add("指标ID");	//2
		titles.add("排序号");	//3
		dataMap.put("titles", titles);
		List<PageData> varOList = rowsService.listAll(pd);
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
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
