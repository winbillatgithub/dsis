package com.as.controller.business.approverule;

import com.as.controller.base.BaseController;
import com.as.entity.Page;
import com.as.entity.system.Dictionaries;
import com.as.entity.system.Role;
import com.as.service.business.approverule.ApproveRuleManager;
import com.as.service.business.tables.TablesManager;
import com.as.service.system.dictionaries.DictionariesManager;
import com.as.service.system.role.RoleManager;
import com.as.util.AppUtil;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import net.sf.json.JSONArray;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * 说明：数据字典
 * 创建人：antispy
 * 创建时间：2015-12-16
 */
@Controller
@RequestMapping(value="/approverule")
public class ApproveRuleController extends BaseController {
	
	String menuUrl = "approverule/list.do"; //菜单地址(权限用)
	@Resource(name="approveruleService")
	private ApproveRuleManager approveruleService;
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	@Resource(name="roleService")
	private RoleManager roleService;

	// 模板
	@Resource(name="tablesService")
	private TablesManager tablesService;

	private final String APPROVE_TYPE_ID = "856571070bcb4e149130d66f0807980e";
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增ApproveRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("APPROVERULE_ID", this.get32UUID());	//主键
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		approveruleService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		return mv;
	}

	/**
	 * 删除
	 * @param APPROVERULE_ID
	 * @param
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete(@RequestParam String APPROVERULE_ID) throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"删除ApproveRule");
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd.put("APPROVERULE_ID", APPROVERULE_ID);
		String errInfo = "success";
		if(approveruleService.listSubDictByParentId(APPROVERULE_ID).size() > 0){//判断是否有子级，是：不允许删除
			errInfo = "false";
		}else{
			pd = approveruleService.findById(pd);//根据ID读取
			if(null != pd.get("TBSNAME") && !"".equals(pd.getString("TBSNAME"))){
				String[] table = pd.getString("TBSNAME").split(",");
				for(int i=0;i<table.length;i++){
					pd.put("thisTable", table[i]);
					try {
						if(Integer.parseInt(approveruleService.findFromTbs(pd).get("zs").toString())>0){//判断是否被占用，是：不允许删除(去排查表检查字典表中的编码字段)
							errInfo = "false";
							break;
						}
					} catch (Exception e) {
							errInfo = "false2";
							break;
					}
				}
			}
		}
		if("success".equals(errInfo)){
			approveruleService.delete(pd);	//执行删除
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改ApproveRule");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		if (pd.getString("BIANMA") == null) {
			pd.put("BIANMA", "");
		}
		approveruleService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表ApproveRule");
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		String keywords = pd.getString("keywords");					//检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String APPROVERULE_ID = null == pd.get("APPROVERULE_ID")?"":pd.get("APPROVERULE_ID").toString();
		if(null != pd.get("id") && !"".equals(pd.get("id").toString())){
			APPROVERULE_ID = pd.get("id").toString();
		}
		pd.put("APPROVERULE_ID", APPROVERULE_ID);					//上级ID
		page.setPd(pd);
		List<PageData>	varList = approveruleService.list(page);	//列出ApproveRule列表
		mv.addObject("pd", approveruleService.findById(pd));		//传入上级所有信息
		mv.addObject("APPROVERULE_ID", APPROVERULE_ID);			//上级ID
		mv.setViewName("/business/approverule/approverule_list");
		mv.addObject("varList", varList);
		mv.addObject("QX",Jurisdiction.getHC());					//按钮权限
		
		Integer NUMBER = (pd.get("NUMBER") == null) ? 0 : Integer.parseInt(pd.getString("NUMBER"));
		if (NUMBER == null) {
			NUMBER = 0;
		}
		mv.addObject("NUMBER", NUMBER);					//层级

		// 字典
		List<Dictionaries> list = dictionariesService.listSubDictByParentId(APPROVE_TYPE_ID);
		PageData fpd = new PageData();
		fpd.put("ROLE_ID", "1");
		List<Role> roleList = roleService.listAllRolesByPId(fpd);		//列出组(页面横向排列的一级组)
		mv.addObject("nodeTypeList", list);
		mv.addObject("roleList", roleList);
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
		
		return mv;
	}
	
	/**
	 * 显示列表ztree
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/listAllDict")
	public ModelAndView listAllDict(Model model,String APPROVERULE_ID,Integer NUMBER)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			JSONArray arr = JSONArray.fromObject(approveruleService.listAllDict("0"));
			String json = arr.toString();
			json = json.replaceAll("APPROVERULE_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("NAME", "name").replaceAll("subDict", "nodes").replaceAll("hasDict", "checked").replaceAll("treeurl", "url");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("APPROVERULE_ID",APPROVERULE_ID);
			if (NUMBER == null) {
				NUMBER = 0;
			}
			mv.addObject("NUMBER",NUMBER);
			mv.addObject("pd", pd);	
			mv.setViewName("/business/approverule/approverule_ztree");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
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
		String APPROVERULE_ID = null == pd.get("APPROVERULE_ID")?"":pd.get("APPROVERULE_ID").toString();
		pd.put("APPROVERULE_ID", APPROVERULE_ID);					//上级ID
		mv.addObject("pds",approveruleService.findById(pd));		//传入上级所有信息
		mv.addObject("APPROVERULE_ID", APPROVERULE_ID);			//传入ID，作为子级ID用
		mv.setViewName("/business/approverule/approverule_edit");
		mv.addObject("msg", "save");
		
		Integer NUMBER = (pd.get("NUMBER") == null) ? 0 : Integer.parseInt(pd.getString("NUMBER"));
		if (NUMBER == null) {
			NUMBER = 0;
		}
		mv.addObject("NUMBER", NUMBER);					//层级

		// 字典
		List<Dictionaries> list = dictionariesService.listSubDictByParentId(APPROVE_TYPE_ID);
		PageData fpd = new PageData();
		fpd.put("ROLE_ID", "1");
		List<Role> roleList = roleService.listAllRolesByPId(fpd);		//列出组(页面横向排列的一级组)
		mv.addObject("nodeTypeList", list);
		mv.addObject("roleList", roleList);
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);
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
		
		Integer NUMBER = (pd.get("NUMBER") == null) ? 0 : Integer.parseInt(pd.getString("NUMBER"));
		if (NUMBER == null) {
			NUMBER = 0;
		}
		mv.addObject("NUMBER", NUMBER);					//层级

		String APPROVERULE_ID = pd.getString("APPROVERULE_ID");
		pd = approveruleService.findById(pd);	//根据ID读取
		mv.addObject("pd", pd);					//放入视图容器
		pd.put("APPROVERULE_ID",pd.get("PARENT_ID").toString());			//用作上级信息
		mv.addObject("pds",approveruleService.findById(pd));				//传入上级所有信息
		mv.addObject("APPROVERULE_ID", pd.get("PARENT_ID").toString());	//传入上级ID，作为子ID用
		pd.put("APPROVERULE_ID",APPROVERULE_ID);							//复原本ID
		mv.setViewName("/business/approverule/approverule_edit");
		mv.addObject("msg", "edit");

		
		
		// 字典
		List<Dictionaries> list = dictionariesService.listSubDictByParentId(APPROVE_TYPE_ID);
		PageData fpd = new PageData();
		fpd.put("ROLE_ID", "1");
		List<Role> roleList = roleService.listAllRolesByPId(fpd);		//列出组(页面横向排列的一级组)
		mv.addObject("nodeTypeList", list);
		mv.addObject("roleList", roleList);
		//模板列表
		List<PageData>	tableList = tablesService.listAll(pd);
		mv.addObject("tableList", tableList);

		return mv;
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
			if(approveruleService.findByBianma(pd) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
