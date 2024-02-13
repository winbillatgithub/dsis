package com.as.controller.template.list;

import com.as.controller.template.TemplateController;
import com.as.entity.Page;
import com.as.entity.business.Column;
import com.as.entity.system.User;
import com.as.service.system.department.DepartmentManager;
import com.as.util.Const;
import com.as.util.JsonDateValueProcessor;
import com.as.util.Jurisdiction;
import com.as.util.ObjectExcelView;
import com.as.util.PageData;
import com.as.util.excel.ExportExcel;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 
 * 说明：List模板Controller
 * 创建人：wibill
 * 创建时间：2016-09-04
 */
@Controller
@RequestMapping(value="/list")
public class ListController extends TemplateController {

	//String menuUrl = "list/list.do"; //菜单地址(权限用)
	public static String menuUrl = "list/list.do"; //菜单地址(权限用)

	@Resource(name="departmentService")
	private DepartmentManager departmentManager;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save",  method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增List");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		return super.save();
	}
	/**提交审核
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/submit",  method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"提交审核");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		return super.submit();
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/ajaxlist")
	@ResponseBody
	public Map ajaxlist(Page page) throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			ModelAndView mv = list(page, true);
			ret.put("success", true);
			ret.put("data", mv.getModel().get("varList"));
			ret.put("totalRows", mv.getModel().get("totalRows"));
		} catch (Exception e) {
			e.printStackTrace();
			ret.put("success", false);
			ret.put("msg", e.getMessage());
		}
		return ret;
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page, boolean loadData) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表List");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		mv = doList(page, mv, menuUrl, loadData);
		return mv;
	}

	/**excel导出
	 * @param page
	 * @throws Exception
	 */
//	@ResponseBody
	@RequestMapping(value="/ajaxlistExcel")
	public void ajaxlistExcel(Page page, HttpServletResponse response) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表List导出");
		try {
			//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
			Session session = Jurisdiction.getSession();
			User user = (User) session.getAttribute(Const.SESSION_USER);

			ModelAndView mv = this.getModelAndView();
			PageData pd = this.getPageData();
			String dataDate = pd.getString("DATA_DATE");
			if (dataDate != null && !"".equals(dataDate)) {
				dataDate = dataDate.substring(0, 10);
			}
			String corpId = pd.getString("CORP_ID");	// 提交人公司ID
			if (corpId == null) {
				corpId = user.getCORP_ID();					// 否则当前登录人就是提交人
			}
            pd.put("CORP_ID", Jurisdiction.getTopCorpId());

			String tablesId = pd.getString("TABLES_ID");
			String transId = pd.getString("TRANS_ID");
			String approveFlag = pd.getString("APPROVE_FLAG");
			String commitUser = pd.getString("COMMIT_USER");

			String LIST_KEYWORDS = pd.getString("LIST_KEYWORDS");				//关键词检索条件
			if(null != LIST_KEYWORDS && !"".equals(LIST_KEYWORDS)){
				pd.put("LIST_KEYWORDS", LIST_KEYWORDS.trim());
			}
			pd.put("CHECKED", "1");
			page.setPd(pd);

			/*
			 * 第一步：获取动态指标、维度信息
			 * 获取多表业务规则，设置对应指标的JOIN规则
			 * Get the columns (indexes)
			 */
			Map<String, Object> map = getAllColumnsList(page, Jurisdiction.getTopCorpId());

			List<Column> columnList = (List<Column>) map.get("columnList");
			List<PageData>	indexesList = (List<PageData>) map.get("indexesList");
			List<PageData>	dimList = (List<PageData>) map.get("dimList");

			JSONArray jsonarray = JSONArray.fromObject(indexesList);
			mv.addObject("indexesList", jsonarray);
			jsonarray = JSONArray.fromObject(dimList); 
			mv.addObject("dimList", jsonarray);

			int rowsLength = 1;
			List<Integer> dimSizeList = new ArrayList<Integer>();
			List<PageData> dimTreeList = new ArrayList<PageData>();
			for(int index = 0; index < dimList.size(); index ++) {
				PageData parent = dimList.get(index);
				String parent_id = parent.getString("DIMENTIONS_ID");
				pd.put("DIMENTIONS_ID", parent_id);
				page.setPd(pd);
				List<PageData>	dimItemList = dimentionsdetailService.list(page);
				jsonarray = JSONArray.fromObject(dimItemList);
				mv.addObject("dimItemList" + index, jsonarray);
				if (dimItemList.size() > 0) {
					rowsLength = rowsLength * dimItemList.size();
				}
				parent.put("SON_DIM_LIST", dimItemList);
				dimTreeList.add(parent);
				dimSizeList.add(dimItemList.size());
			}

			/* 
			 * 第二步：判断数据库是否有数，有数的情况下要先取数
			 */
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
			jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			jsonConfig.registerJsonValueProcessor(java.sql.Time.class, new JsonDateValueProcessor("HH:mm:ss"));

			PageData params = new PageData();
			params.put("TABLES_ID", tablesId);
			params.put("DATA_DATE", dataDate);
			params.put("CORP_ID", corpId);
			params.put("LIST_COLUMNS", columnList);
			params.put("WHERE", pd.getString("WHERE"));
			params.put("pageNum", "0");
			params.put("pageSize", "100000");
			PageData pdData = templateService.listAll(params);	//列出List列表
			List<PageData> varList = (List<PageData>) pdData.get("varList");
			List<Object> list=new ArrayList<Object>();
			for (int index = 0 ; index < varList.size(); index ++) {
				LinkedHashMap<String, Object> m=new LinkedHashMap<String, Object>();
				
				for (int i = 0; i < indexesList.size(); i++) {
//					if(indexesList.get(i).get("TABLES_ID")
					
					if(indexesList.get(i).get("JOIN_TABLES_ID")!=null&&indexesList.get(i).get("CORP_ID")!=null&&indexesList.get(i).get("JOIN_COLUMNS_NAME_EN")!=null&&indexesList.get(i).get("MASTER_COLUMNS_NAME_EN")!=null) {
						try {
                            PageData pdCommon = new PageData();
                            pdCommon.put("tablesId", indexesList.get(i).getString("JOIN_TABLES_ID"));
                            pdCommon.put("joinColumnsId", indexesList.get(i).getString("JOIN_COLUMNS_NAME_EN"));
                            pdCommon.put("selectColumnsId", indexesList.get(i).getString("MASTER_COLUMNS_NAME_EN"));
                            pdCommon.put("corpId", indexesList.get(i).getString("CORP_ID"));
							List<PageData> list1 = commonService.getColumnIDMap(pdCommon, null);
							for (int j = 0; j < list1.size(); j++) {
								if(varList.get(index).getString(indexesList.get(i).getString("COLUMN_NAME_EN")).equals(list1.get(j).getString(indexesList.get(i).getString("JOIN_COLUMNS_NAME_EN")))) {
									if(indexesList.get(i).get("JOIN_CONDITION")!=null&&indexesList.get(i).getString("JOIN_CONDITION").equals("1")) {
										PageData pd1 = new PageData();
										pd1.put("CORP_ID", list1.get(j).get(indexesList.get(i).getString("MASTER_COLUMNS_NAME_EN")));
										pd1 =departmentManager.findById(pd1);
										if(null != pd1.get("NAME") && !"".equals(pd1.getString("NAME"))){
											m.put(indexesList.get(i).getString("COLUMN_NAME_CN"), pd1.getString("NAME"));
										}
									}else {
										m.put(indexesList.get(i).getString("COLUMN_NAME_CN"), list1.get(j).get(indexesList.get(i).getString("MASTER_COLUMNS_NAME_EN")));
									}
								}
							}
						}catch (Exception e) {
						}
					}else if(indexesList.get(i).get("JOIN_TABLES_ID")!=null&&indexesList.get(i).get("CORP_ID")!=null&&indexesList.get(i).get("JOIN_COLUMNS_NAME_EN")!=null&&indexesList.get(i).get("MASTER_COLUMNS_NAME_EN")!=null) {
						try {
                            PageData pdCommon = new PageData();
                            pdCommon.put("tablesId", indexesList.get(i).getString("JOIN_TABLES_ID"));
                            pdCommon.put("joinColumnsId", indexesList.get(i).getString("JOIN_COLUMNS_NAME_EN"));
                            pdCommon.put("selectColumnsId", indexesList.get(i).getString("MASTER_COLUMNS_NAME_EN"));
                            pdCommon.put("corpId", indexesList.get(i).getString("CORP_ID"));
							List<PageData> list1 = commonService.getColumnIDMap(pdCommon, null);
							for (int j = 0; j < list1.size(); j++) {
								if(varList.get(index).getString(indexesList.get(i).getString("COLUMN_NAME_EN")).equals(list1.get(j).getString(indexesList.get(i).getString("JOIN_COLUMNS_NAME_EN")))) {
									m.put(indexesList.get(i).getString("COLUMN_NAME_CN"), list1.get(j).get(indexesList.get(i).getString("MASTER_COLUMNS_NAME_EN")));
								}
							}
						}catch (Exception e) {
						}
					}else {
						m.put(indexesList.get(i).getString("COLUMN_NAME_CN"), varList.get(index).get(indexesList.get(i).getString("COLUMN_NAME_EN")));
						
					}
				}
				list.add(m);
			}
			
			ExportExcel e = new ExportExcel();
			e.fileExcel(list, "列表List",response);
			
//			mv.addObject("varList", jsonarray);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出List到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("创建日期");	//1
		titles.add("修改日期");	//2
		titles.add("操作人");	//3
		dataMap.put("titles", titles);
		List<PageData> varOList = templateService.list(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("CREATE_DATE"));	//1
			vpd.put("var2", varOList.get(i).getString("MODIFY_DATE"));	//2
			vpd.put("var3", varOList.get(i).getString("OPERATOR"));	//3
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
