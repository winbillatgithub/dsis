package com.as.controller.system.department;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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

import com.as.controller.base.BaseController;
import com.as.entity.Page;
import com.as.util.AppUtil;
import com.as.util.PageData;
import com.as.util.Tools;
import com.as.util.Jurisdiction;
import com.as.service.system.department.DepartmentManager;

/** 
 * 说明：组织机构
 * 创建人：antispy
 * 创建时间：2015-12-16
 */
@Controller
@RequestMapping(value="/department")
public class DepartmentController extends BaseController {
	
	String menuUrl = "department/list.do"; //菜单地址(权限用)
	@Resource(name="departmentService")
	private DepartmentManager departmentService;
	
	/**保存
	 * 只有在建总公司的时候建立数据库，调用createDatabase接口
	 * TODO winbill
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增department");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("CORP_ID", this.get32UUID());	//主键
        pd.put("STOP", "0");
        pd.put("STATUS", "1");
		pd.put("CREATE_DATE", Tools.date2Str(new Date()));	//创建时间
		pd.put("MODIFY_DATE", pd.get("CREATE_DATE"));	//修改日期
		pd.put("OPERATOR", "");	//操作者		departmentService.save(pd);
		departmentService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		return mv;
	}
	
	/**
	 * 删除
	 * @param CORP_ID
	 * @param
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete(@RequestParam String CORP_ID) throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"删除department");
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd.put("CORP_ID", CORP_ID);
		String errInfo = "success";
		if(departmentService.listSubDepartmentByParentId(CORP_ID).size() > 0){//判断是否有子级，是：不允许删除
			errInfo = "false";
		}else{
			departmentService.delete(pd);	//执行删除
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
		logBefore(logger, Jurisdiction.getUsername()+"修改department");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		departmentService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表department");
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		String keywords = pd.getString("keywords");					//检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String CORP_ID = null == pd.get("CORP_ID")?"":pd.get("CORP_ID").toString();
		if(null != pd.get("id") && !"".equals(pd.get("id").toString())){
			CORP_ID = pd.get("id").toString();
		}
		pd.put("CORP_ID", CORP_ID);					//上级ID
		page.setPd(pd);
		List<PageData>	varList = departmentService.datalistSubDepartmentPage(page);	//列出Dictionaries列表
		mv.addObject("pd", departmentService.findById(pd));		//传入上级所有信息
		mv.addObject("CORP_ID", CORP_ID);			//上级ID
		mv.addObject("USER_CORP_ID",Jurisdiction.getCorpId());
		mv.setViewName("/system/department/department_list");
		mv.addObject("varList", varList);
		mv.addObject("QX",Jurisdiction.getHC());				//按钮权限
		return mv;
	}
	
	/**
	 * 显示列表ztree
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/listAllDepartment")
	public ModelAndView listAllDepartment(Model model,String CORP_ID)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();

		// 只使用登录用户的公司ID来查询，展现该公司下所有的节点
		CORP_ID = Jurisdiction.getCorpId();
		//
		if (Tools.isEmpty(CORP_ID)) {
			CORP_ID = "0";	// admin
		}
		try{
			JSONArray arr = JSONArray.fromObject(departmentService.listAllDepartment(CORP_ID));
			String json = arr.toString();
			json = json.replaceAll("CORP_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("NAME", "name").replaceAll("subDepartment", "nodes").replaceAll("hasDepartment", "checked").replaceAll("treeurl", "url");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("CORP_ID",CORP_ID);
			mv.addObject("pd", pd);	
			mv.setViewName("/system/department/department_ztree");
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
		String CORP_ID = null == pd.get("CORP_ID")?"":pd.get("CORP_ID").toString();
		pd.put("CORP_ID", CORP_ID);					//上级ID
		mv.addObject("pds",departmentService.findById(pd));		//传入上级所有信息
		mv.addObject("CORP_ID", CORP_ID);			//传入ID，作为子级ID用
		mv.setViewName("/system/department/department_edit");
		mv.addObject("msg", "save");
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
		String CORP_ID = pd.getString("CORP_ID");
		pd = departmentService.findById(pd);	//根据ID读取
		mv.addObject("pd", pd);					//放入视图容器
		pd.put("CORP_ID",pd.get("PARENT_ID").toString());			//用作上级信息
		mv.addObject("pds",departmentService.findById(pd));				//传入上级所有信息
		mv.addObject("CORP_ID", pd.get("PARENT_ID").toString());	//传入上级ID，作为子ID用
		pd.put("CORP_ID",CORP_ID);							//复原本ID
		mv.setViewName("/system/department/department_edit");
		mv.addObject("msg", "edit");
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
			pd.put("CORP_ID", Jurisdiction.getTopCorpId());
			if(departmentService.findByBianma(pd) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			errInfo = "error";
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
