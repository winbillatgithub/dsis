package com.as.controller.system.datarights;

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
import com.as.entity.system.Role;
import com.as.util.AppUtil;
import com.as.util.ObjectExcelView;
import com.as.util.PageData;
import com.as.util.Jurisdiction;
import com.as.service.system.datarights.DataRightsManager;
import com.as.service.system.role.RoleManager;

/** 
 * 说明：数据权限
 * 创建人：antispy
 * 创建时间：2016-08-01
 */
@Controller
@RequestMapping(value="/datarights")
public class DataRightsController extends BaseController {
	
	String menuUrl = "datarights/list.do"; //菜单地址(权限用)
	@Resource(name="datarightsService")
	private DataRightsManager datarightsService;
	@Resource(name="roleService")
	private RoleManager roleService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增DataRights");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ID", this.get32UUID());	//主键
		datarightsService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除DataRights");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		datarightsService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改DataRights");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		datarightsService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表DataRights");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();

		// 角色列表
		String roleId = pd.getString("roleId");				//角色ID
		List<Role> roleList = null;
		if (roleId == null || roleId.isEmpty()) {
			PageData pd2 = new PageData();
			pd2.put("ROLE_ID", "1");
			roleList = roleService.listAllRolesByPId(pd2);//列出所有系统用户角色
			pd2 = null;
		} else {
			Role role = roleService.getRoleById(roleId);
			roleList = new ArrayList<Role>();
			roleList.add(role);
		}
		mv.addObject("roleList", roleList);

		// 查询列表
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = datarightsService.list(page);	//列出DataRights列表
		mv.setViewName("/system/datarights/datarights_list");
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
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("/system/datarights/datarights_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		// 角色列表
		PageData pd2 = new PageData();
		pd2.put("ROLE_ID", "1");
		List<Role> roleList = roleService.listAllRolesByPId(pd2);//列出所有系统用户角色
		mv.addObject("roleList", roleList);
		pd2 = null;
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
		pd = datarightsService.findById(pd);	//根据ID读取
		mv.setViewName("/system/datarights/datarights_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		// 角色列表
		PageData pd2 = new PageData();
		pd2.put("ROLE_ID", "1");
		List<Role> roleList = roleService.listAllRolesByPId(pd2);//列出所有系统用户角色
		mv.addObject("roleList", roleList);
		pd2 = null;
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除DataRights");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			datarightsService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出DataRights到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("id");	//1
		titles.add("角色ID");	//2
		titles.add("表名");	//3
		titles.add("权限");	//4
		titles.add("操作符");	//5
		dataMap.put("titles", titles);
		List<PageData> varOList = datarightsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("ID"));	//1
			vpd.put("var2", varOList.get(i).getString("ROLE_ID"));	//2
			vpd.put("var3", varOList.get(i).getString("TABLE_NAME"));	//3
			vpd.put("var4", varOList.get(i).getString("RIGHTS"));	//4
			vpd.put("var5", varOList.get(i).getString("OPERATOR"));	//5
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
