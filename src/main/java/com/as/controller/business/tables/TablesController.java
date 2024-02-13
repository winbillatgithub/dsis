package com.as.controller.business.tables;

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
import com.as.util.Tools;
import com.as.util.Jurisdiction;
import com.as.service.business.tables.TablesManager;

/** 
 * 说明：模板维护
 * 创建人：antispy
 * 创建时间：2016-09-09
 */
@Controller
@RequestMapping(value="/tables")
public class TablesController extends BaseController {
	
	String menuUrl = "tables/list.do"; //菜单地址(权限用)
	@Resource(name="tablesService")
	private TablesManager tablesService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Tables");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("TABLES_ID", pd.getString("TABLE_NAME_EN"));	//主键
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		tablesService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		
		// 创建物理表
		tablesService.createTable(pd);
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Tables");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = this.getPageData();
		PageData record = tablesService.findById(pd);
		tablesService.delete(pd);

		// 删除物理表
		tablesService.dropTable(record);

		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Tables");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		tablesService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");

		// 修改物理表
		String tableNameOld = pd.getString("TABLE_NAME_EN_OLD");
		String tableNameNew = pd.getString("TABLE_NAME_EN");
		if (!Tools.isEmpty(tableNameOld) && !tableNameNew.equals(tableNameOld)) {
			// rename table
			tablesService.renameTable(pd);
		}

		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Tables");
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
		List<PageData>	varList = tablesService.list(page);	//列出Tables列表
		mv.setViewName("/business/tables/tables_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
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
		mv.setViewName("/business/tables/tables_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
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
		pd = tablesService.findById(pd);	//根据ID读取
		mv.setViewName("/business/tables/tables_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Tables");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = this.getPageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			tablesService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}

	/**判断编码是否存在
	 * @return
	 */
	@RequestMapping(value="/hasBianma")
	@ResponseBody
	public Object hasBianma(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = this.getPageData();
		try{
			// 总公司下面的所有table name不能重复，因为在一个schema下面
			pd.put("CORP_ID", Jurisdiction.getTopCorpId());
			if(tablesService.findByBianma(pd) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			errInfo = "error";
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出Tables到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("模板名称");	//1
		titles.add("模板ID");	//2
		titles.add("模板描述");	//3
		titles.add("排序号");	//4
		titles.add("状态");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = tablesService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TABLE_NAME"));	//1
			vpd.put("var2", varOList.get(i).getString("TABLE_NAME_EN"));	//2
			vpd.put("var3", varOList.get(i).getString("REMARK"));	//3
			vpd.put("var4", varOList.get(i).get("SORT_NO").toString());	//4
			vpd.put("var5", varOList.get(i).getString("STATUS"));	//5
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
