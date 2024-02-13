package com.as.controller.system.role;

import com.as.controller.base.BaseController;
import com.as.entity.system.Department;
import com.as.entity.system.Menu;
import com.as.entity.system.Role;
import com.as.entity.system.User;
import com.as.service.system.appuser.AppuserManager;
import com.as.service.system.department.DepartmentManager;
import com.as.service.system.menu.MenuManager;
import com.as.service.system.role.RoleManager;
import com.as.service.system.user.UserManager;
import com.as.util.AppUtil;
import com.as.util.Const;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import com.as.util.RightsHelper;
import com.as.util.Tools;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/** 
 * 类名称：RoleController 角色权限管理
 * 创建人：antispy
 * 修改时间：2015年11月6日
 * @version
 */
@Controller
@RequestMapping(value="/role")
public class RoleController extends BaseController {
	
	String menuUrl = "role.do"; //菜单地址(权限用)
	@Resource(name="menuService")
	private MenuManager menuService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="userService")
	private UserManager userService;
	@Resource(name="appuserService")
	private AppuserManager appuserService;
	@Resource(name="departmentService")
	private DepartmentManager departmentService;

	/** 进入权限首页
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping
	public ModelAndView list()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USER);
			pd = this.getPageData();
			if(pd.getString("ROLE_ID") == null || "".equals(pd.getString("ROLE_ID").trim())){
				pd.put("ROLE_ID", "1");										//默认列出第一组角色(初始设计系统用户和会员组不能删除)
			}
			pd.put("CORP_ID", user.getCORP_ID());
			PageData fpd = new PageData();
			/*
			 * SQL：where PARENT_ID = #{ROLE_ID}
			 * 用户注册时设置的pdRole.put("PARENT_ID", "1");
			 * fpd.put("ROLE_ID", "0");会导致查询不到用户注册的角色列表,针对普通用户. 超级管理员用户不确定逻辑
			 * PARENT_ID: 普通管理员必须是1, 只有超级管理员是0
			 */
            if ("admin".equals(user.getUSERNAME())) {
                fpd.put("ROLE_ID", "0");
            } else {
                fpd.put("ROLE_ID", "1");
            }
			fpd.put("CORP_ID", user.getCORP_ID());
			List<Role> roleList = roleService.listAllRolesByPId(fpd);		//列出组(页面横向排列的一级组)
			List<Department> corpList = departmentService.listAllDepartment(user.getCORP_ID());	//列出Dictionaries列表
//			StringBuffer sb = new StringBuffer();
//			for (Department corp : corpList) {
//				sb.append("'");
//				sb.append(corp.getCORP_ID());
//				sb.append("',");
//			}
//			String in = sb.toString();
//			if (in.length() > 0) {
//				in = in.substring(0, in.length() - 1);
//			}
//			pd.put("IN_CORP_ID", in);
			List<Role> roleList_z = roleService.listAllRolesByPId(pd);		//列出此组下架角色
			pd = roleService.findObjectById(pd);							//取得点击的角色组(横排的)
			mv.addObject("pd", pd);
			mv.addObject("roleList", roleList);
			mv.addObject("roleList_z", roleList_z);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
			mv.setViewName("/system/role/role_list");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**去新增页面
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd(){
		ModelAndView mv = this.getModelAndView();

		User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USER);

		try{
			PageData pd = this.getPageData();
			pd.put("CORP_ID", user.getCORP_ID());			
			List<Department> varList = departmentService.listAllDepartment(pd);	//列出Dictionaries列表

			pd.put("CORP_NAME", user.getCORP_NAME());
			mv.addObject("msg", "add");
			mv.setViewName("/system/role/role_edit");
			mv.addObject("pd", pd);
			mv.addObject("departmentList", varList);

		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**保存新增角色
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public ModelAndView add()throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"新增角色");
		ModelAndView mv = this.getModelAndView();

		try{
			PageData pd = this.getPageData();
			String parent_id = pd.getString("PARENT_ID");		//父类角色id
			pd.put("ROLE_ID", parent_id);			
			if("0".equals(parent_id)){
				pd.put("RIGHTS", "");							//菜单权限
			}else{
				String rights = roleService.findObjectById(pd).getString("RIGHTS");
				pd.put("RIGHTS", (null == rights)?"":rights);	//组菜单权限
			}
			pd.put("ROLE_ID", this.get32UUID());				//主键
			pd.put("ADD_QX", "0");	//初始新增权限为否
			pd.put("DEL_QX", "0");	//删除权限
			pd.put("EDIT_QX", "0");	//修改权限
			pd.put("CHA_QX", "0");	//查看权限
			roleService.add(pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
			mv.addObject("msg","failed");
		}
		mv.setViewName("/save_result");
		return mv;
	}
	
	/**请求编辑
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView toEdit( String ROLE_ID )throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();

		pd.put("CORP_ID", Jurisdiction.getCorpId());			
		List<Department> varList = departmentService.listAllDepartment(pd);	//列出Dictionaries列表
		try{
			pd = this.getPageData();
			pd.put("ROLE_ID", ROLE_ID);
			pd = roleService.findObjectById(pd);
			mv.addObject("msg", "edit");
			mv.addObject("pd", pd);
			mv.setViewName("/system/role/role_edit");
			mv.addObject("departmentList", varList);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**保存修改
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit()throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"修改角色");
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = this.getPageData();
			roleService.edit(pd);
			mv.addObject("msg","success");
		} catch(Exception e){
			logger.error(e.toString(), e);
			mv.addObject("msg","failed");
		}
		mv.setViewName("/save_result");
		return mv;
	}
	
	/**删除角色
	 * @param ROLE_ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object deleteRole(@RequestParam String ROLE_ID)throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"删除角色");
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		String errInfo = "";
		try{
			pd.put("ROLE_ID", ROLE_ID);
			List<Role> roleList_z = roleService.listAllRolesByPId(pd);		//列出此部门的所有下级
			if(roleList_z.size() > 0){
				errInfo = "false";											//下级有数据时，删除失败
			}else{
				List<PageData> userlist = userService.listAllUserByRoldId(pd);			//此角色下的用户
				List<PageData> appuserlist = appuserService.listAllAppuserByRorlid(pd);	//此角色下的会员
				if(userlist.size() > 0 || appuserlist.size() > 0){						//此角色已被使用就不能删除
					errInfo = "false2";
				}else{
				roleService.deleteRoleById(ROLE_ID);	//执行删除
				errInfo = "success";
				}
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 显示菜单列表ztree(菜单授权菜单)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/menuqx")
	public ModelAndView listAllMenu(Model model,String ROLE_ID)throws Exception{
		logger.debug("/role/menuqx?ROLE_ID=" + ROLE_ID);
		ModelAndView mv = this.getModelAndView();
		try{
			Role role = roleService.getRoleById(ROLE_ID);			//根据角色ID获取角色对象
			String roleRights = role.getRIGHTS();					//取出本角色菜单权限
			PageData pd = new PageData();
			String USER_NAME = Jurisdiction.getUsername();
			String CORP_ID = Jurisdiction.getCorpId();
			if ("admin".equals(USER_NAME)) {
				CORP_ID = null;
//			} else {
//				CORP_ID = "0";	// 对于公司管理员来说，权限局限于menu表中corp_id为0的菜单和公司管理员自建的菜单
			}
			pd.put("PARENT_ID", "0");
			pd.put("CORP_ID", CORP_ID);			
			List<Menu> menuList = menuService.listAllMenuQx(pd);	//获取所有菜单
			menuList = this.readMenu(menuList, roleRights);			//根据角色权限处理菜单权限状态(递归处理)
			JSONArray arr = JSONArray.fromObject(menuList);
			String json = arr.toString();
			json = json.replaceAll("MENU_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("ROLE_ID",ROLE_ID);
			mv.setViewName("/system/role/menuqx");
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**保存角色菜单权限
	 * @param ROLE_ID 角色ID
	 * @param menuIds 菜单ID集合
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/saveMenuqx")
	public void saveMenuqx(@RequestParam String ROLE_ID,@RequestParam String menuIds,PrintWriter out)throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"修改菜单权限");
		PageData pd = new PageData();
		try{
			if(null != menuIds && !"".equals(menuIds.trim())){
				BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds));//用菜单ID做权处理
				Role role = roleService.getRoleById(ROLE_ID);	//通过id获取角色对象
				role.setRIGHTS(rights.toString());
				roleService.updateRoleRights(role);				//更新当前角色菜单权限
				pd.put("rights",rights.toString());
			}else{
				Role role = new Role();
				role.setRIGHTS("");
				role.setROLE_ID(ROLE_ID);
				roleService.updateRoleRights(role);				//更新当前角色菜单权限(没有任何勾选)
				pd.put("rights","");
			}
				pd.put("ROLE_ID", ROLE_ID);
				roleService.setAllRights(pd);					//更新此角色所有子角色的菜单权限
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}

	/**请求角色按钮授权页面(增删改查)
	 * @param ROLE_ID： 角色ID
	 * @param msg： 区分增删改查
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/b4Button")
	public ModelAndView b4Button(@RequestParam String ROLE_ID,@RequestParam String msg,Model model)throws Exception{
		ModelAndView mv = this.getModelAndView();
		try{
			PageData pd = new PageData();
			String USER_NAME = Jurisdiction.getUsername();
			String CORP_ID = Jurisdiction.getCorpId();
			if ("admin".equals(USER_NAME)) {
				CORP_ID = null;
//			} else {
//				CORP_ID = "0";	// 对于公司管理员来说，权限局限于menu表中corp_id为0的菜单和公司管理员自建的菜单
			}
			pd.put("PARENT_ID", "0");
			pd.put("CORP_ID", CORP_ID);
			List<Menu> menuList = menuService.listAllMenuQx(pd); //获取所有菜单
			Role role = roleService.getRoleById(ROLE_ID);		  //根据角色ID获取角色对象
			String roleRights = "";
			if("add_qx".equals(msg)){
				roleRights = role.getADD_QX();	//新增权限
			}else if("del_qx".equals(msg)){
				roleRights = role.getDEL_QX();	//删除权限
			}else if("edit_qx".equals(msg)){
				roleRights = role.getEDIT_QX();	//修改权限
			}else if("cha_qx".equals(msg)){
				roleRights = role.getCHA_QX();	//查看权限
			}
			menuList = this.readMenu(menuList, roleRights);		//根据角色权限处理菜单权限状态(递归处理)
			JSONArray arr = JSONArray.fromObject(menuList);
			String json = arr.toString();
			json = json.replaceAll("MENU_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("MENU_NAME", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("ROLE_ID",ROLE_ID);
			mv.addObject("msg", msg);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		mv.setViewName("/system/role/b4Button");
		return mv;
	}
	
	/**根据角色权限处理权限状态(递归处理)
	 * @param menuList：传入的总菜单
	 * @param roleRights：加密的权限字符串
	 * @return
	 */
	public List<Menu> readMenu(List<Menu> menuList,String roleRights){
		for(int i=0;i<menuList.size();i++){
			menuList.get(i).setHasMenu(RightsHelper.testRights(roleRights, menuList.get(i).getMENU_ID()));
			this.readMenu(menuList.get(i).getSubMenu(), roleRights);					//是：继续排查其子菜单
		}
		return menuList;
	}
	
	/**
	 * 保存角色按钮权限
	 */
	/**
	 * @param ROLE_ID
	 * @param menuIds
	 * @param msg
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/saveB4Button")
	public void saveB4Button(@RequestParam String ROLE_ID,@RequestParam String menuIds,@RequestParam String msg,PrintWriter out)throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"修改"+msg+"权限");
		PageData pd = this.getPageData();
		try {
            Map<String, String> roleRights = Jurisdiction.getHC();
            /*
             * QX.edit
             * QX.del
             * QX.add
             * QX.cha
             */
            String key = null;
            if ("add_qx".equals(msg)) {
                key = "add";
            } else if("del_qx".equals(msg)) {
                key = "del";
            } else if("edit_qx".equals(msg)) {
                key = "edit";
            } else if("cha_qx".equals(msg)) {
                key = "cha";
            }
            String operator = roleRights.get(key);
            boolean bHasPrivilege = RightsHelper.testRights(operator, 0);
            if (bHasPrivilege) {
                menuIds = "0," + menuIds;
            }

			if(null != menuIds && !"".equals(menuIds.trim())){
				BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds));
				pd.put("value", rights.toString());
			}else{
				pd.put("value", "");
			}
			pd.put("ROLE_ID", ROLE_ID);
			roleService.saveB4Button(msg,pd);
			out.write("success");
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
	}
	
}