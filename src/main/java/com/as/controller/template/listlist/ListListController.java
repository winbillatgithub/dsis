package com.as.controller.template.listlist;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.as.controller.template.TemplateController;
import com.as.entity.Page;
import com.as.entity.business.Column;
import com.as.entity.system.User;
import com.as.service.business.common.CommonManager;
import com.as.service.business.listlistrule.ListListRuleManager;
import com.as.service.business.listlistruledetail.ListListRuleDetailManager;
import com.as.service.business.multitablerule.MultiTableRuleManager;
import com.as.service.system.department.DepartmentManager;
import com.as.util.Const;
import com.as.util.JsonDateValueProcessor;
import com.as.util.JsonHelper;
import com.as.util.ObjectExcelView;
import com.as.util.PageData;
import com.as.util.excel.ExportExcel;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import com.as.util.Jurisdiction;

/** 
 * 说明：List模板Controller
 * 创建人：winbill
 * 创建时间：2016-09-04
 */
@Controller
@RequestMapping(value="/listlist")
public class ListListController extends TemplateController {

	String menuUrl = "listlist/list.do"; //菜单地址(权限用)

	// List-List规则服务
	@Resource(name="listlistruleService")
	public ListListRuleManager listListRuleService;
	@Resource(name="listlistruledetailService")
	public ListListRuleDetailManager listListRuleDetailService;
	@Resource(name="commonService")
	public CommonManager commonService;
	@Resource(name="departmentService")
	private DepartmentManager departmentManager;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save",  method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增ListList");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		// 调用父类的保存方法
		Map<String, Object> map = super.save();
		// 对于master-slave-source模式，如果master数据被删除，也需要删除相应的slave数据
		PageData pd = this.getPageData();
		String tablesId = pd.getString("TABLES_ID");
		String deleted  = pd.getString("DELETED");
		JSONArray jsonArray = JSONArray.fromObject(deleted);
		List<Map<String,Object>> mapListJson = (List)jsonArray;
		if (mapListJson.size() > 0) {
			// 获取当前表对应的slave表
			pd.put("MASTER_TABLES_ID", tablesId);
			List<PageData> listListRule = listListRuleService.listAll(pd);
	        for (int i = 0; i < mapListJson.size(); i++) {
	            Map<String,Object> obj = mapListJson.get(i);
				for (int index = 0; index < listListRule.size(); index ++) {
					String slaveTablesId = listListRule.get(index).getString("SLAVE_TABLES_ID");
		            PageData data = new PageData();
		            data.put("PARENT_ID", obj.get("id"));	// master table, id column
		            //data.put("TABLES_ID", slaveTablesId);
					templateService.deleteSlaveData(slaveTablesId, data);
		        }
			}
		}

		return map;
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
	@RequestMapping(value="/ajax_ll_master")
	@ResponseBody
	public Map ajax_ll_master(Page page) throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			ModelAndView mv = master(page, true);
			ret.put("success", true);
			ret.put("data", mv.getModel().get("varList"));
			ret.put("totalRows", mv.getModel().get("totalRows"));
		} catch (Exception e) {
			ret.put("success", false);
			ret.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}
	@RequestMapping(value="/ajax_ll_slave")
	@ResponseBody
	public Map ajax_ll_slave(Page page) throws Exception{
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			ModelAndView mv = slave(page, true);
			ret.put("success", true);
			ret.put("data", mv.getModel().get("varList"));
			ret.put("totalRows", mv.getModel().get("totalRows"));
		} catch (Exception e) {
			ret.put("success", false);
			ret.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}

	/**列表--主Master
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/master")
	public ModelAndView master(Page page, boolean loadData) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"主列表List");
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
		 * 第1步：获取动态指标、维度信息
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
		 * 第2步：判断数据库是否有数，有数的情况下要先取数
		 */
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		jsonConfig.registerJsonValueProcessor(java.sql.Time.class, new JsonDateValueProcessor("HH:mm:ss"));

		if (true == loadData) {
			PageData params = new PageData();
			params.put("TABLES_ID", tablesId);
			params.put("DATA_DATE", dataDate);
			params.put("CORP_ID", corpId);
			params.put("LIST_COLUMNS", columnList);
			params.put("WHERE", pd.getString("WHERE"));
			params.put("pageNum", pd.getString("pageNum"));
			params.put("pageSize", pd.getString("pageSize"));
			PageData pdData = templateService.listAll(params);	//列出List列表
			List<PageData> varList = (List<PageData>) pdData.get("varList");
			// 没有初始数据，并且有维度的情况下，插入初始数据
			if (varList.size() == 0 && dimList.size() > 0) {
				String columnsDef = pd.getString("COLUMNS_DEF");
				Map<String,Object> colDef = JsonHelper.parseJSON2Map(columnsDef);
				// Insert initial data into database
				List<PageData> dataList = new ArrayList<PageData>();
				boolean colFlag = false;
				for (int i = 0; i < rowsLength; i ++) {
					PageData data = new PageData();
					for (int j = 0; j < dimTreeList.size(); j++) {
						PageData tmp = dimTreeList.get(j);
						List<PageData>	dimItem = (List<PageData>) tmp.get("SON_DIM_LIST");
						int dimSize = dimItem.size();
						int changeSize = getChangeSize(j, dimSizeList);
						int row = i / changeSize;		// 先计算每一行对应该列的数据索引 0,1,2,3,4,5...
						row = row % dimSize;			// 如果索引大于数据长度，需要重复,0,1,2,0,1,2...
						PageData rowData = dimItem.get(row);
						String val = rowData.getString("SON_DIMENTIONS_NAME");
						data.put(tmp.getString("DIMENTIONS_NAME_EN"), val);
					}
					data.put("CORP_ID", corpId);
					data.put("DATA_DATE", pd.getString("DATA_DATE"));
					data.put("SORT_NO", i);
					// 插入数据库
					templateService.insert(tablesId, data, colDef);
					dataList.add(data);
				}
				// 插入完数据后，再次获取数据
				pdData = templateService.listAll(params);	//列出List列表
				varList = (List<PageData>) pdData.get("varList");
			}
            /*
             * 获取内联字段值,MERGE到列表中
             */
            String columnsDef = pd.getString("COLUMNS_DEF");
            columnsDef = URLDecoder.decode(columnsDef, "UTF-8");
            Map<String,Object> colDef = JsonHelper.parseJSON2Map(columnsDef);
            if (!colDef.isEmpty()) {
                commonService.mergeInnerSourceData(varList, colDef);
            }

            jsonarray = JSONArray.fromObject(varList, jsonConfig);
			//jsonarray = JsonHelper.listToJson(varList);
			mv.addObject("varList", jsonarray);
			mv.addObject("totalRows", pdData.get("totalRows"));
		}
		/*
		 * 第3步，获取模板业务规则--单表规则
		 */
		List<PageData> ruleList = getSingleTableRuleList(indexesList, tablesId);
		jsonarray = JSONArray.fromObject(ruleList, jsonConfig);
		mv.addObject("ruleList", jsonarray);
		/*
		 * 第4步，获取ListList规则
		 */
		// 设置主-从的触发button
		mv.addObject("LIST_LIST_MASTER", "2");

		pd.put("MASTER_TABLES_ID", tablesId);
		List<PageData> listListRule = listListRuleService.listAll(pd);
		if (listListRule.size() == 1) {
			// 设置主-从的触发button
			mv.addObject("LIST_LIST_MASTER", "1");
			JSONObject jsonObject = JSONObject.fromObject(listListRule.get(0));
			jsonObject.remove("JSON_DATA");
			mv.addObject("LIST_LIST_RULE", jsonObject);
		} else if (listListRule.size() > 1) {
			logger.error("Master table has more than 1 list-list rule:" + tablesId);
		} else {
			logger.error("Master table has NO list-list rule:" + tablesId);
		}
		/*
		 * 第N步，设置控制参数和数据
		 */
		mv.setViewName("/template/list/template_list");
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		mv.addObject("FROM", pd.getString("FROM"));
		mv.addObject("TRANS_ID", transId);
		mv.addObject("TABLES_ID", tablesId);
		mv.addObject("CORP_ID", corpId);
		mv.addObject("CORP_NAME", user.getCORP_NAME());
		mv.addObject("DATA_DATE", pd.getString("DATA_DATE"));
		mv.addObject("TYPE", tablesId);
		mv.addObject("APPROVE_FLAG", approveFlag);
		//COMMIT_FLAG,AUDIT_LEVEL,AUDIT_USER_LEVEL1,AUDIT_USER_LEVEL2,AUDIT_USER_LEVEL3
		mv.addObject("COMMIT_FLAG", pd.getString("COMMIT_FLAG"));
		mv.addObject("AUDIT_LEVEL", pd.getString("AUDIT_LEVEL"));
		mv.addObject("AUDIT_USER_LEVEL1", pd.getString("AUDIT_USER_LEVEL1"));
		mv.addObject("AUDIT_USER_LEVEL2", pd.getString("AUDIT_USER_LEVEL2"));
		mv.addObject("AUDIT_USER_LEVEL3", pd.getString("AUDIT_USER_LEVEL3"));
		// 当前用户是提交人的时候，不能审批和驳回
		if (user.getUSER_ID().equals(commitUser)) {
			mv.addObject("IS_COMMIT_USER", "1");
		} else {
			mv.addObject("IS_COMMIT_USER", "0");
		}
		mv.addObject("fromAction", "listlist/master.do");

		return mv;
	}

	/**
	 * 列表--从Slave
	 * 0. LISTLISTRULE_ID, SLAVE_TABLES_ID要传递过来
	 * 1. 从表关联字段和值需要传递过来，比如COURSE_NAME='123' | MASTER_COLUMNS_ID/MASTER_COLUMNS_VALUE
	 * 2. Slave<->Master, Slave<->Source的关系，需要从规则表查询出来
	 * 3. 如果Slave表有数据，直接取数据，没有数据的话，需要从Source表获取（注意要从Source表获取哪些字段的信息，及根据哪个字段获取）
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/slave")
	public ModelAndView slave(Page page, boolean loadData) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"从列表List");
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

		String tablesId = pd.getString("SLAVE_TABLES_ID");
		String transId = pd.getString("TRANS_ID");
		String approveFlag = pd.getString("APPROVE_FLAG");
		String commitUser = pd.getString("COMMIT_USER");
		String masterRowIdValue = pd.getString("MASTER_ROW_ID_VALUE");

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
		pd.put("TABLES_ID", tablesId);
		Map<String, Object> map = getAllColumnsList(page, Jurisdiction.getTopCorpId());

		List<Column>   columnList = (List<Column>) map.get("columnList");
		List<PageData> indexesList = (List<PageData>) map.get("indexesList");
		List<PageData> dimList = (List<PageData>) map.get("dimList");
		//
		// Slave表的所有字段都为只读
		List<PageData> listListRuleDetail = listListRuleDetailService.listAll(pd);
		for (int i = 0; i < indexesList.size(); i ++) {
			PageData pdIndex = indexesList.get(i);
			String indexColumnName = pdIndex.getString("COLUMN_NAME_EN");
			for (int j = 0; j < listListRuleDetail.size(); j ++) {
				String slaveColumnNameEn = listListRuleDetail.get(j).getString("SLAVE_COLUMNS_NAME_EN");
				if (indexColumnName.equals(slaveColumnNameEn)) {
					pdIndex.put("INITIAL_TYPE_NAME", "ZIDONG");
					indexesList.set(i, pdIndex);
					break;
				}
			}
		}

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

		if (true == loadData) {
			PageData params = new PageData();
			params.put("TABLES_ID", tablesId);
			params.put("DATA_DATE", dataDate);
			params.put("CORP_ID", corpId);
			params.put("LIST_COLUMNS", columnList);
			params.put("PARENT_ID", masterRowIdValue);
			params.put("WHERE", pd.getString("WHERE"));
			params.put("pageNum", pd.getString("pageNum"));
			params.put("pageSize", pd.getString("pageSize"));
			PageData pdData = templateService.listAll(params);	//列出List列表
			List<PageData> varList = (List<PageData>) pdData.get("varList");
			// 没有初始数据，从源表插入初始数据
			if (varList.size() == 0) {
				// Insert initial data into database
				boolean colFlag = false;
				String listListRuleId = pd.getString("LISTLISTRULE_ID");
				String slaveTablesId = pd.getString("SLAVE_TABLES_ID");
				String masterColumnsId = pd.getString("MASTER_COLUMNS_ID");
				String masterColumnsValue = pd.getString("MASTER_COLUMNS_VALUE");
				String sourceColumnsName = pd.getString("SOURCE_COLUMNS_NAME_EN");
				String columnsDef = pd.getString("COLUMNS_DEF");
				Map<String,Object> colDef = JsonHelper.parseJSON2Map(columnsDef);
				// 因为list-list模式，slave表的数据是从source表获取，所以不是在save时更新
				// Get joined columns between slave table & source table
				List<PageData> listSourceData = commonService.getSourceTableData(pd, listListRuleDetail, colDef);
				// 获取从表内联数据
				for(int i = 0; i < listSourceData.size(); i ++) {
					PageData data = listSourceData.get(i);
	
					// 启用PARENT_ID字段来隔离数据-实现Master->slave的数据隔离
					data.put("PARENT_ID", masterRowIdValue);
					data.put("CORP_ID", corpId);
					data.put("DATA_DATE", pd.getString("DATA_DATE"));
					data.put("SORT_NO", i);
					// 插入数据库
					templateService.insert(tablesId, data, colDef);
				}
				// 插入完数据后，再次获取数据
				pdData = templateService.listAll(params);	//列出List列表
				varList = (List<PageData>) pdData.get("varList");
			}
			jsonarray = JSONArray.fromObject(varList, jsonConfig);
			//jsonarray = JsonHelper.listToJson(varList);
			mv.addObject("varList", jsonarray);
			mv.addObject("totalRows", pdData.get("totalRows"));
		}
		mv.setViewName("/template/list/template_list");
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		mv.addObject("FROM", pd.getString("FROM"));
		mv.addObject("TRANS_ID", transId);
		mv.addObject("TABLES_ID", tablesId);
		mv.addObject("SLAVE_TABLES_ID", tablesId);
		mv.addObject("CORP_ID", corpId);
		mv.addObject("CORP_NAME", user.getCORP_NAME());
		mv.addObject("DATA_DATE", pd.getString("DATA_DATE"));
		mv.addObject("TYPE", tablesId);
		mv.addObject("APPROVE_FLAG", approveFlag);
		//COMMIT_FLAG,AUDIT_LEVEL,AUDIT_USER_LEVEL1,AUDIT_USER_LEVEL2,AUDIT_USER_LEVEL3
		mv.addObject("COMMIT_FLAG", pd.getString("COMMIT_FLAG"));
		mv.addObject("AUDIT_LEVEL", pd.getString("AUDIT_LEVEL"));
		mv.addObject("AUDIT_USER_LEVEL1", pd.getString("AUDIT_USER_LEVEL1"));
		mv.addObject("AUDIT_USER_LEVEL2", pd.getString("AUDIT_USER_LEVEL2"));
		mv.addObject("AUDIT_USER_LEVEL3", pd.getString("AUDIT_USER_LEVEL3"));
		// 当前用户是提交人的时候，不能审批和驳回
		if (user.getUSER_ID().equals(commitUser)) {
			mv.addObject("IS_COMMIT_USER", "1");
		} else {
			mv.addObject("IS_COMMIT_USER", "0");
		}
		mv.addObject("fromAction", "listlist/slave.do");
		// 设置主-从的触发button
		mv.addObject("LIST_LIST_MASTER", "0");
		mv.addObject("MASTER_ROW_ID_VALUE", masterRowIdValue);
		mv.addObject("LISTLISTRULE_ID", pd.getString("LISTLISTRULE_ID"));
		mv.addObject("SLAVE_TABLES_ID", pd.getString("SLAVE_TABLES_ID"));
		mv.addObject("MASTER_COLUMNS_ID", pd.getString("MASTER_COLUMNS_ID"));
		mv.addObject("MASTER_COLUMNS_VALUE", pd.getString("MASTER_COLUMNS_VALUE"));
		mv.addObject("SOURCE_COLUMNS_NAME_EN", pd.getString("SOURCE_COLUMNS_NAME_EN"));

		/*
		 * 第三步，获取模板业务规则--单表规则
		 */
		List<PageData> ruleList = getSingleTableRuleList(indexesList, tablesId);
		jsonarray = JSONArray.fromObject(ruleList, jsonConfig);
		mv.addObject("ruleList", jsonarray);

		return mv;
	}
	
	
	@RequestMapping(value="/ajax_ll_masterExcel")
	public void ajax_ll_masterExcel(Page page, HttpServletResponse response) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"主列表List");
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
		 * 第1步：获取动态指标、维度信息
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
		 * 第2步：判断数据库是否有数，有数的情况下要先取数
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
            /*
             * 获取内联字段值,MERGE到列表中
             */
            String columnsDef = pd.getString("COLUMNS_DEF");
            columnsDef = URLDecoder.decode(columnsDef, "UTF-8");
            Map<String,Object> colDef = JsonHelper.parseJSON2Map(columnsDef);
            if (!colDef.isEmpty()) {
                commonService.mergeInnerSourceData(varList, colDef);
            }
			jsonarray = JSONArray.fromObject(varList, jsonConfig);
			//jsonarray = JsonHelper.listToJson(varList);
			mv.addObject("varList", jsonarray);
			mv.addObject("totalRows", pdData.get("totalRows"));
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
					}else {
						m.put(indexesList.get(i).getString("COLUMN_NAME_CN"), varList.get(index).get(indexesList.get(i).getString("COLUMN_NAME_EN")));
						
					}
				}
				list.add(m);
			}
			
			ExportExcel e = new ExportExcel();
			e.fileExcel(list, "列表List",response);
	}

	/**
	 * 列表--从Slave
	 * 0. LISTLISTRULE_ID, SLAVE_TABLES_ID要传递过来
	 * 1. 从表关联字段和值需要传递过来，比如COURSE_NAME='123' | MASTER_COLUMNS_ID/MASTER_COLUMNS_VALUE
	 * 2. Slave<->Master, Slave<->Source的关系，需要从规则表查询出来
	 * 3. 如果Slave表有数据，直接取数据，没有数据的话，需要从Source表获取（注意要从Source表获取哪些字段的信息，及根据哪个字段获取）
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/ajax_ll_slaveExcel")
	public void ajax_ll_slaveExcel(Page page, HttpServletResponse response) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"从列表List");
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

		String tablesId = pd.getString("SLAVE_TABLES_ID");
		String transId = pd.getString("TRANS_ID");
		String approveFlag = pd.getString("APPROVE_FLAG");
		String commitUser = pd.getString("COMMIT_USER");
		String masterRowIdValue = pd.getString("MASTER_ROW_ID_VALUE");

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
		pd.put("TABLES_ID", tablesId);
		Map<String, Object> map = getAllColumnsList(page, Jurisdiction.getTopCorpId());

		List<Column>   columnList = (List<Column>) map.get("columnList");
		List<PageData> indexesList = (List<PageData>) map.get("indexesList");
		List<PageData> dimList = (List<PageData>) map.get("dimList");
		//
		// Slave表的所有字段都为只读
		List<PageData> listListRuleDetail = listListRuleDetailService.listAll(pd);
		for (int i = 0; i < indexesList.size(); i ++) {
			PageData pdIndex = indexesList.get(i);
			String indexColumnName = pdIndex.getString("COLUMN_NAME_EN");
			for (int j = 0; j < listListRuleDetail.size(); j ++) {
				String slaveColumnNameEn = listListRuleDetail.get(j).getString("SLAVE_COLUMNS_NAME_EN");
				if (indexColumnName.equals(slaveColumnNameEn)) {
					pdIndex.put("INITIAL_TYPE_NAME", "ZIDONG");
					indexesList.set(i, pdIndex);
					break;
				}
			}
		}

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
			params.put("PARENT_ID", masterRowIdValue);
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
					}else {
						m.put(indexesList.get(i).getString("COLUMN_NAME_CN"), varList.get(index).get(indexesList.get(i).getString("COLUMN_NAME_EN")));
						
					}
				}
				list.add(m);
			}
			
			ExportExcel e = new ExportExcel();
			e.fileExcel(list, "列表List",response);
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
