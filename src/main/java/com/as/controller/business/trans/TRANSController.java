package com.as.controller.business.trans;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Case;
import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.as.controller.base.BaseController;
import com.as.entity.Page;
import com.as.entity.business.ApproveRule;
import com.as.entity.system.User;
import com.as.util.AppUtil;
import com.as.util.Const;
import com.as.util.ObjectExcelView;
import com.as.util.PageData;
import com.as.util.Tools;
import com.as.util.UuidUtil;
import com.as.util.excel.ExportExcel;

import net.sf.json.JSONObject;

import com.as.util.Jurisdiction;
import com.as.service.business.approverule.ApproveRuleManager;
import com.as.service.business.common.CommonManager;
import com.as.service.business.node.NodeManager;
import com.as.service.business.template.TemplateManager;
import com.as.service.business.trans.TRANSManager;
import com.as.service.system.department.DepartmentManager;
import com.as.service.system.role.RoleManager;
import com.as.service.system.user.UserManager;

/** 
 * 说明：数据传输表
 * 创建人：antispy
 * 创建时间：2016-09-15
 */
@Controller
@RequestMapping(value="/trans")
public class TRANSController extends BaseController {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String menuUrl = "trans/list.do"; //菜单地址(权限用)
	@Resource(name="transService")
	private TRANSManager transService;
	@Resource(name="departmentService")
	private DepartmentManager departmentService;
	@Resource(name="nodeService")
	private NodeManager nodeService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="userService")
	private UserManager userService;
	@Resource(name="approveruleService")
	private ApproveRuleManager approveruleService;
	@Resource(name="templateService")
	private TemplateManager templateService;
	@Resource(name="commonService")
	public CommonManager commonService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增TRANS");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("TRANS_ID", this.get32UUID());	//主键
		pd.put("REMARK", "");	//备注
		pd.put("R_TYPE", "");	//记录类型
		pd.put("RSV_STR2", "");	//保留字段2
		pd.put("CREATE_DATE", Tools.date2Str(new Date()));	//创建日期
		transService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除TRANS");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = this.getPageData();
		String tableId = pd.getString("TABLES_ID");
		// 先删除业务表
		PageData pdTrans = new PageData();
		pdTrans.put("TRANS_ID", pd.getString("TRANS_ID"));
		PageData pdResult = transService.findById(pd);
		templateService.deleteBusinessData(tableId, pdResult);
		// 再删trans表
		transService.delete(pd);

		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改TRANS");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		transService.edit(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		return mv;
	}
	/**审批
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/approve",  method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> approve() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"审批TRANS");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "approve")){return null;} //校验权限
		Map<String, Object> ret = new HashMap<String, Object>();

		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		boolean bRet = false;
		try {
			PageData pd = this.getPageData();
			// 保存B_TRANS表
			String transId = pd.getString("TRANS_ID");
			String tablesId = pd.getString("TYPE");
			String corpId = pd.getString("CORP_ID");
			String dataDate = pd.getString("DATA_DATE");
			String group1 = pd.getString("AUDIT_USER_LEVEL1");
			String group2 = pd.getString("AUDIT_USER_LEVEL2");
			String group3 = pd.getString("AUDIT_USER_LEVEL3");
			int commitFlag = Integer.parseInt(pd.getString("COMMIT_FLAG"));
			int auditLevel = Integer.parseInt(pd.getString("AUDIT_LEVEL"));
			String flag = pd.getString("APPROVE");
	
			PageData pdRule = new PageData();
			pdRule.put("BIANMA", tablesId);
			PageData pdRuleData = approveruleService.findByBianma(pdRule);
			String id0 = pdRuleData.getString("APPROVERULE_ID");
			List<ApproveRule> listLevel1 = approveruleService.listSubDictByParentId(id0); // 第一层审批节点列表
			int levels = listLevel1.size();  // 写回B_TRANS的AUDIT_LEVEL===========================
			String group = "NaN";
			if (group3 != null && !"".equals(group3)) {
				group = group3;
			} else if (group2 != null && !"".equals(group2)) {
				group = group2;
			} else if (group1 != null && !"".equals(group1)) {
				group = group1;
			}
	
			if ("ok".equals(flag)) {
				// 如果没有审批结束，设置下一级审批人
				boolean bNodeOver = true;	// 节点结束标志
				boolean bOver = false;		// 流程结束标志
				// 保存B_NODE表--设置当前节点状态
	//			ApproveRule ar = listLevel1.get(commitFlag - 2); // 获取当前审批节点
	//			String id1 = ar.getAPPROVERULE_ID(); // 写回B_TRANS的AUDIT_USER_LEVEL1=================
				PageData nodeInput = new PageData();
				nodeInput.put("TRANS_ID", transId);
				nodeInput.put("AUDIT_USER_IDS", group);
				List<PageData> nodeList = nodeService.listAll(nodeInput); //当前节点下所有角色列表
				PageData pdNode = new PageData();
				for (int index = 0; index < nodeList.size(); index ++) {
					pdNode = nodeList.get(index); // 获取审批角色
					String roleId = pdNode.getString("AUDIT_USER");
					String auditStatus = pdNode.getString("AUDIT_STATUS");
					if (roleId.equals(user.getROLE_ID())) {
	//					pdNode.put("TRANS_ID", transId);
	//					pdNode.put("AUDIT_USER_IDS", id1);
	//					pdNode.put("AUDIT_USER", roleId);
						pdNode.put("AUDIT_TIME", Tools.date2Str(new Date()));
						pdNode.put("AUDIT_STATUS", "1"); // 已审核
						nodeService.edit(pdNode);
					} else {
						if (auditStatus == null || "0".equals(auditStatus)) {
							bNodeOver = false;
						}
					}
				}
				if (bNodeOver && commitFlag - levels >= 1) {
					bOver = true;
				}
				// 设置下一个--节点状态
				String nextGroupId = null;
				if (!bOver && bNodeOver) {
					ApproveRule ar = listLevel1.get(commitFlag + 1 - 2); // 获取下一个审批节点
					nextGroupId = ar.getAPPROVERULE_ID(); // 写回B_TRANS的AUDIT_USER_LEVEL1=================
					List<ApproveRule> listLevel2 = approveruleService.listSubDictByParentId(nextGroupId); // 第二层审批角色列表
	
					for (int index = 0; index < listLevel2.size(); index ++) {
						ApproveRule role = listLevel2.get(index); // 获取审批角色
						String roleId = role.getNAME_EN();
						pdNode.clear();
						pdNode.put("NODE_ID", UuidUtil.get32UUID());
						pdNode.put("TRANS_ID", transId);
						pdNode.put("AUDIT_USER_IDS", nextGroupId);
						pdNode.put("AUDIT_USER", roleId);
						pdNode.put("AUDIT_TIME", null);
						pdNode.put("AUDIT_STATUS", "0"); // 未审核		
						nodeService.save(pdNode);
					}
				}
				// 设置B_TRANS
				if (bNodeOver) {
					if (commitFlag - levels >= 1) {
						pd.put("COMMIT_FLAG", 9);		// 审批结束
						// 设置业务表的状态STATUS winbill
						// TABLES_ID CORP_ID（提交者） DATA_DATE TYPE->tablesId
						commonService.updateStatus(tablesId, corpId, dataDate);
					} else {
						pd.put("COMMIT_FLAG", commitFlag + 1);
					}
					if (group3 != null && !"".equals(group3)) {
						pd.put("AUDIT_TIME_LEVEL3", Tools.date2Str(new Date()));
					} else if (group2 != null && !"".equals(group2)) {
						pd.put("AUDIT_TIME_LEVEL2", Tools.date2Str(new Date()));
					} else if (group1 != null && !"".equals(group1)) {
						pd.put("AUDIT_TIME_LEVEL1", Tools.date2Str(new Date()));
					}
					if (!bOver) {
						if (group2 != null && !"".equals(group2)) {
							pd.put("AUDIT_USER_LEVEL3", nextGroupId);
						} else if (group1 != null && !"".equals(group1)) {
							pd.put("AUDIT_USER_LEVEL2", nextGroupId);
						}
					}
				}
				pd.put("AUDIT_LEVEL", levels);
				transService.approve(pd);
	
			} else {
				pd.put("COMMIT_FLAG", 0);		// 0:驳回
				pd.put("AUDIT_LEVEL", levels);
				transService.deny(pd);
				// 保存B_NODE表
				// group1 变更状态为0,AUDIT_TIME为null
				PageData pdNode = new PageData();
				pdNode.put("TRANS_ID", transId);
				pdNode.put("AUDIT_USER_IDS", group1);
				pdNode.put("AUDIT_TIME", "");
				pdNode.put("AUDIT_STATUS", "0");
				nodeService.denyUpdate(pdNode);
				// group2 group3 删除
				nodeService.denyDelete(pdNode);
			}
			bRet = true;
		} catch (Exception e) {
			ret.put("msg", e.getMessage());
		}
		ret.put("success", bRet);

		return ret;
	}
	/**每日填报待办列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listDaily")
	public ModelAndView listDaily(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表TRANS");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = getListData(page);
		mv.setViewName("/business/trans/todo_list");
		return mv;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/listDailyExcel")
	public void listDailyExcel(Page page, HttpServletResponse response) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表TRANS导出");
		List<PageData> varList = transService.excel(page);	//列出TRANS列表
		PageData pdUser = new PageData();
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		String roleId = user.getROLE_ID();
		pdUser.clear();
		// 只能搜索该公司的人员
		pdUser.put("CORP_ID", user.getTOP_CORP_ID());	// 只列举总公司下面的所有部门列表
		List<PageData> userList1 = userService.listAllUser(pdUser);
		Page pg = new Page();
		PageData corpPd = new PageData();
		corpPd.put("CORP_ID", user.getTOP_CORP_ID());	// 只列举总公司下面的所有部门列表
		pg.setPd(corpPd);
		List<PageData> pdDept = departmentService.list(pg);
		List<Object> list=new ArrayList<Object>();
		
		for (int index = 0 ; index < varList.size(); index ++) {
			pdUser.clear();
			PageData item = varList.get(index);
			String transId = item.getString("TRANS_ID");
			String groupId = item.getString("AUDIT_USER_LEVEL3");
			pdUser.clear();
			pdUser.put("TRANS_ID", transId);
			List<PageData> userList = null;
			List<PageData> approveUserList = null;
			if (groupId != null) {
				pdUser.put("AUDIT_USER_IDS", groupId);
				userList = nodeService.listAll(pdUser);
				item.put("USER_LIST3", userList);
				if (approveUserList == null) {
					approveUserList = userList;
					item.put("approveUserList", approveUserList);
				}
			}
			groupId = item.getString("AUDIT_USER_LEVEL2");
			if (groupId != null) {
				pdUser.put("AUDIT_USER_IDS", groupId);
				userList = nodeService.listAll(pdUser);
				item.put("USER_LIST2", userList);
				if (approveUserList == null) {
					approveUserList = userList;
					item.put("approveUserList", approveUserList);
				}
			}
			groupId = item.getString("AUDIT_USER_LEVEL1");
			if (groupId != null) {
				pdUser.put("AUDIT_USER_IDS", groupId);
				userList = nodeService.listAll(pdUser);
				item.put("USER_LIST1", userList);
				if (approveUserList == null) {
					approveUserList = userList;
					item.put("approveUserList", approveUserList);
				}
			}
			if (approveUserList != null)  {
				for(int i = 0; i < approveUserList.size(); i ++) {
					PageData au = approveUserList.get(i);
					String auditStatus = au.getString("AUDIT_STATUS");
					if ("0".equals(auditStatus) && au.get("AUDIT_USER").equals(roleId)) {
						item.put("APPROVE_FLAG", "1");
						break;
					}
				}
			} else {
				item.put("APPROVE_FLAG", "0");
			}
			LinkedHashMap<String, Object> m=new LinkedHashMap<String, Object>();
			String NAME="";
			for (int i = 0; i < pdDept.size(); i++) {
				try {
					if(pdDept.get(i).getString("CORP_ID").equals(item.getString("CORP_ID"))) {
						m.put("项目名称",item.get("DATA_DATE")==null?"":item.get("DATA_DATE")+"-"+pdDept.get(i).get("NAME")+"-"+item.get("TYPE"));
					}
				}catch (Exception e) {
				}
			}
			
			for (int i = 0; i < userList1.size(); i++) {
				try {
					if(userList1.get(i).getString("USER_ID").equals(item.getString("COMMIT_USER"))) {
						NAME=userList1.get(i).getString("NAME");
					}
				}catch (Exception e) {
				}
			}
			
			String COMMIT_FLAG=item.get("COMMIT_FLAG")==null?"0":item.getString("COMMIT_FLAG");
			String COMMIT_FLAG_ZH="";
			if ("0".equals(COMMIT_FLAG)) {
                COMMIT_FLAG_ZH = "驳回";
            } else if ("1".equals(COMMIT_FLAG)) {
				COMMIT_FLAG_ZH="已保存";
            } else if ("2".equals(COMMIT_FLAG)) {
				COMMIT_FLAG_ZH="待审核";
            } else if ("3".equals(COMMIT_FLAG)) {
				COMMIT_FLAG_ZH="一审通过";
            } else if ("4".equals(COMMIT_FLAG)) {
				COMMIT_FLAG_ZH="二审通过";
            } else if ("5".equals(COMMIT_FLAG)) {
				COMMIT_FLAG_ZH="三审通过";
            } else if ("9".equals(COMMIT_FLAG)) {
				COMMIT_FLAG_ZH="审批完成";
            } else {
				COMMIT_FLAG_ZH="";
			}

			m.put("状态", COMMIT_FLAG_ZH);
			m.put("提交人", NAME);
			m.put("提交时间", item.get("COMMIT_TIME")==null?"":item.getString("COMMIT_TIME"));
			List USER_LIST1=item.get("USER_LIST1")==null?new ArrayList<Object>():(ArrayList<Object>) item.get("USER_LIST1");
			if(USER_LIST1.size()>0) {
				Map json_test = (Map) USER_LIST1.get(0);
				NAME=(String) json_test.get("NAME");
				m.put("一级审核人", NAME);
				m.put("一审时间", item.get("AUDIT_TIME_LEVEL1")==null?"":item.get("AUDIT_TIME_LEVEL1"));
			}else {
				m.put("一级审核人", "");
				m.put("一审时间", "");
			}
			List USER_LIST2=item.get("USER_LIST2")==null?new ArrayList<Object>():(ArrayList<Object>)item.get("USER_LIST2");
			if(USER_LIST2.size()>0) {
				Map json_test2 = (Map) USER_LIST2.get(0);
				NAME=(String) json_test2.get("NAME");
				m.put("二级审核人", NAME);
				m.put("二审时间", item.get("AUDIT_TIME_LEVEL2")==null?"":item.get("AUDIT_TIME_LEVEL2"));
			}else {
				m.put("二级审核人", "");
				m.put("二审时间", "");
			}
			List USER_LIST3=item.get("USER_LIST3")==null?new ArrayList<Object>():(ArrayList<Object>)item.get("USER_LIST3");
			if(USER_LIST3.size()>0) {
				Map json_test3 = (Map) USER_LIST3.get(0);
				NAME=(String) json_test3.get("NAME");
				m.put("三级审核人", NAME);
				m.put("三审时间", item.get("AUDIT_TIME_LEVEL3")==null?"":item.get("AUDIT_TIME_LEVEL3"));
			}else {
				m.put("三级审核人", "");
				m.put("三审时间", "");
			}
			m.put("意见", item.get("REJECT_REASON")==null?"":item.getString("REJECT_REASON"));
			list.add(m);
		}
		ExportExcel e = new ExportExcel();
		e.fileExcel(list, "列表TRANS",response);
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表TRANS");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		return getListData(page);
	}
	private ModelAndView getListData(Page page) throws Exception{
		ModelAndView mv = this.getModelAndView();
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);

		PageData pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String tablesId = pd.getString("TABLES_ID");
		String from = pd.getString("FROM");
		if ("MAINPAGE".equals(from)) {
			tablesId = null;
			pd.remove("TABLES_ID");
		}
		String userId = user.getUSER_ID();
		String roleId = user.getROLE_ID();
		String corpId = user.getCORP_ID();
		if ("1".equals(roleId)) {	// Admin
			roleId = null;
		}
		pd.put("ROLE_ID", roleId);
		pd.put("USER_ID", userId);
		pd.put("CORP_ID", corpId);
		page.setPd(pd);
		List<PageData>	varList = transService.list(page);	//列出TRANS列表
		mv.setViewName("/business/trans/trans_list");
		mv.addObject("TABLES_ID", tablesId);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		mv.addObject("FROM", from);

		//公司编码，审核人
		Page pg = new Page();
		PageData corpPd = new PageData();
		corpPd.put("CORP_ID", user.getTOP_CORP_ID());	// 只列举总公司下面的所有部门列表
		pg.setPd(corpPd);
		List<PageData> pdDept = departmentService.list(pg);
		mv.addObject("deptList", pdDept);
		PageData pdUser = new PageData();
		for (int index = 0 ; index < varList.size(); index ++) {
			PageData item = varList.get(index);
			String transId = item.getString("TRANS_ID");
			String groupId = item.getString("AUDIT_USER_LEVEL3");
			pdUser.clear();
			pdUser.put("TRANS_ID", transId);
			List<PageData> userList = null;
			List<PageData> approveUserList = null;
			if (groupId != null) {
				pdUser.put("AUDIT_USER_IDS", groupId);
				userList = nodeService.listAll(pdUser);
				item.put("USER_LIST3", userList);
				if (approveUserList == null) {
					approveUserList = userList;
					item.put("approveUserList", approveUserList);
				}
			}
			groupId = item.getString("AUDIT_USER_LEVEL2");
			if (groupId != null) {
				pdUser.put("AUDIT_USER_IDS", groupId);
				userList = nodeService.listAll(pdUser);
				item.put("USER_LIST2", userList);
				if (approveUserList == null) {
					approveUserList = userList;
					item.put("approveUserList", approveUserList);
				}
			}
			groupId = item.getString("AUDIT_USER_LEVEL1");
			if (groupId != null) {
				pdUser.put("AUDIT_USER_IDS", groupId);
				userList = nodeService.listAll(pdUser);
				item.put("USER_LIST1", userList);
				if (approveUserList == null) {
					approveUserList = userList;
					item.put("approveUserList", approveUserList);
				}
			}
			if (approveUserList != null)  {
				for(int i = 0; i < approveUserList.size(); i ++) {
					PageData au = approveUserList.get(i);
					String auditStatus = au.getString("AUDIT_STATUS");
					if ("0".equals(auditStatus) && au.get("AUDIT_USER").equals(roleId)) {
						item.put("APPROVE_FLAG", "1");
						break;
					}
				}
			} else {
				item.put("APPROVE_FLAG", "0");
			}
			varList.set(index, item);
		}
		mv.addObject("varList", varList);
		// 人员列表
		pdUser.clear();
		// 只能搜索该公司的人员
		pdUser.put("CORP_ID", user.getTOP_CORP_ID());	// 只列举总公司下面的所有部门列表
		List<PageData> userList = userService.listAllUser(pdUser);
		mv.addObject("userList", userList);
		mv.addObject("ROLE_ID", roleId);
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
		mv.setViewName("/business/trans/trans_edit");
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
		String APPROVE_FLAG = pd.getString("APPROVE_FLAG");
		pd = transService.findById(pd);	//根据ID读取
		mv.setViewName("/business/trans/trans_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		mv.addObject("APPROVE_FLAG", APPROVE_FLAG);
		return mv;
	}	

	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goNew")
	public ModelAndView goNew()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// 获取昨日
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
		String date =  simpleDateFormat.format(calendar.getTime());
		pd.put("DATA_DATE", date);
		mv.setViewName("/business/trans/date_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/newTrans")
	public ModelAndView newTrans() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增TRANS");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		//
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		String departmentId = user.getCORP_ID();
		String tablesId = pd.getString("TABLES_ID");
		pd.put("TRANS_ID", this.get32UUID());	//主键
		pd.put("TYPE", tablesId);
		pd.put("CORP_ID", departmentId);
		pd.put("COMMIT_FLAG", "1");						//已保存
		pd.put("COMMIT_USER", user.getUSER_ID());		//填报用户
		pd.put("CREATE_DATE", Tools.date2Str(new Date()));	//创建日期
		pd.put("MODIFY_DATE", Tools.date2Str(new Date()));	//创建日期
		pd.put("OPERATOR", user.getUSER_ID());	//创建日期
		pd.put("REMARK", "");	//备注
		pd.put("R_TYPE", "");	//记录类型
		pd.put("RSV_STR2", "");	//保留字段2
		pd.put("AUDIT_LEVEL", "0");
		transService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		return mv;
	}
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除TRANS");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		String tableId = pd.getString("TABLES_ID");
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			// 先删除业务表
			PageData pdTrans = new PageData();
			for(int index = 0; index < ArrayDATA_IDS.length; index ++) {
				pdTrans.put("TRANS_ID", ArrayDATA_IDS[index]);
				PageData pdResult = transService.findById(pdTrans);
				templateService.deleteBusinessData(tableId, pdResult);
			}
			transService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出TRANS到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("类型");	//1
		titles.add("公司编码");	//2
		titles.add("业务日期");	//3
		titles.add("提交时间");	//4
		titles.add("提交标志");	//5
		titles.add("提交人");	//6
		titles.add("一级审核人");	//7
		titles.add("一级审核时间");	//8
		titles.add("二级审核人");	//9
		titles.add("二级审核时间");	//10
		titles.add("三级审核人");	//11
		titles.add("三级审核时间");	//12
		titles.add("审核级别");	//13
		titles.add("驳回原因");	//14
		titles.add("备注");	//15
		titles.add("记录类型");	//16
		titles.add("保留字段2");	//17
		titles.add("保留字段3");	//18
		titles.add("保留字段4");	//19
		titles.add("创建日期");	//20
		titles.add("修改日期");	//21
		titles.add("操作人");	//22
		dataMap.put("titles", titles);
		List<PageData> varOList = transService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("TYPE"));	//1
			vpd.put("var2", varOList.get(i).getString("CORP_ID"));	//2
			vpd.put("var3", varOList.get(i).getString("DATA_DATE"));	//3
			vpd.put("var4", varOList.get(i).getString("COMMIT_TIME"));	//4
			vpd.put("var5", varOList.get(i).getString("COMMIT_FLAG"));	//5
			vpd.put("var6", varOList.get(i).getString("COMMIT_USER"));	//6
			vpd.put("var7", varOList.get(i).getString("AUDIT_USER_LEVEL1"));	//7
			vpd.put("var8", varOList.get(i).getString("AUDIT_TIME_LEVEL1"));	//8
			vpd.put("var9", varOList.get(i).getString("AUDIT_USER_LEVEL2"));	//9
			vpd.put("var10", varOList.get(i).getString("AUDIT_TIME_LEVEL2"));	//10
			vpd.put("var11", varOList.get(i).getString("AUDIT_USER_LEVEL3"));	//11
			vpd.put("var12", varOList.get(i).getString("AUDIT_TIME_LEVEL3"));	//12
			vpd.put("var13", varOList.get(i).get("AUDIT_LEVEL").toString());	//13
			vpd.put("var14", varOList.get(i).getString("REJECT_REASON"));	//14
			vpd.put("var15", varOList.get(i).getString("REMARK"));	//15
			vpd.put("var16", varOList.get(i).getString("R_TYPE"));	//16
			vpd.put("var17", varOList.get(i).getString("RSV_STR2"));	//17
			vpd.put("var18", varOList.get(i).getString("RSV_STR3"));	//18
			vpd.put("var19", varOList.get(i).getString("RSV_STR4"));	//19
			vpd.put("var20", varOList.get(i).getString("CREATE_DATE"));	//20
			vpd.put("var21", varOList.get(i).getString("MODIFY_DATE"));	//21
			vpd.put("var22", varOList.get(i).getString("OPERATOR"));	//22
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
