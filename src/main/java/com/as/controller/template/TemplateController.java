package com.as.controller.template;

import com.as.controller.base.BaseController;
import com.as.entity.Page;
import com.as.entity.business.ApproveRule;
import com.as.entity.business.Column;
import com.as.entity.system.User;
import com.as.service.business.approverule.ApproveRuleManager;
import com.as.service.business.columns.ColumnsManager;
import com.as.service.business.common.CommonManager;
import com.as.service.business.dict.DictManager;
import com.as.service.business.dimentions.DimentionsManager;
import com.as.service.business.dimentionsdetail.DimentionsDetailManager;
import com.as.service.business.indexes.IndexesManager;
import com.as.service.business.multitablerule.MultiTableRuleManager;
import com.as.service.business.node.NodeManager;
import com.as.service.business.rows.RowsManager;
import com.as.service.business.tablerule.TableRuleManager;
import com.as.service.business.tables.TablesManager;
import com.as.service.business.template.TemplateManager;
import com.as.service.business.trans.TRANSManager;
import com.as.service.system.dictionaries.DictionariesManager;
import com.as.service.system.role.RoleManager;
import com.as.util.Const;
import com.as.util.JsonDateValueProcessor;
import com.as.util.JsonHelper;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import com.as.util.StringUtil;
import com.as.util.Tools;
import com.as.util.UuidUtil;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/** 
 * 说明：List模板Controller
 * 创建人：wibill
 * 创建时间：2016-09-04
 */
public class TemplateController extends BaseController {

	@Resource(name="templateService")
	public TemplateManager templateService;

	@Resource(name="commonService")
	public CommonManager commonService;

	// 模板
	@Resource(name="tablesService")
	public TablesManager tablesService;

	// 模板-维度
	@Resource(name="rowsService")
	public RowsManager rowsService;
	@Resource(name="dimentionsService")
	public DimentionsManager dimentionsService;
	@Resource(name="dimentionsdetailService")
	public DimentionsDetailManager dimentionsdetailService;

	// 模板-指标
	@Resource(name="columnsService")
	public ColumnsManager columnsService;
	@Resource(name="indexesService")
	public IndexesManager indexesService;

	// 数据传输--审批规则
	@Resource(name="transService")
	public TRANSManager transService;
	@Resource(name="approveruleService")
	public ApproveRuleManager approveruleService;
    // 系统字典表
	@Resource(name="dictionariesService")
	public DictionariesManager dictionariesService;
	@Resource(name="roleService")
	public RoleManager roleService;
    // 用户自定义字典表
    @Resource(name="dictService")
    public DictManager dictService;

	@Resource(name="nodeService")
	public NodeManager nodeService;

	// 模板--业务规则
	@Resource(name="tableruleService")
	public TableRuleManager tableruleService;
	// 多表业务规则
	@Resource(name="multitableruleService")
	public MultiTableRuleManager multitableruleService;

	public Page resetPageInfo(Page page) {
		return page;
	}
	/**保存
	 * @param
	 * @throws Exception
	 */
	public Map<String, Object> save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Template");
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);

		Map<String, Object> ret = new HashMap<String, Object>();
		boolean bRet = false;
//		ModelAndView mv = this.getModelAndView();
		try {
			PageData pd = this.getPageData();
			String corpId = pd.getString("CORP_ID");
			String dataDate = pd.getString("DATA_DATE");
			String tablesId = pd.getString("TABLES_ID");
			String appended = pd.getString("APPENDED");
			String modified = pd.getString("MODIFIED");
			String deleted  = pd.getString("DELETED");
			String transId = pd.getString("TRANS_ID");
			String columnsDef = pd.getString("COLUMNS_DEF");
			Map<String,Object> colDef = JsonHelper.parseJSON2Map(columnsDef);
			// 新增记录
			JSONArray jsonArray = JSONArray.fromObject(appended);
			List<Map<String,Object>> mapListJson = (List)jsonArray;  
	        for (int i = 0; i < mapListJson.size(); i++) {
	            Map<String,Object> obj = mapListJson.get(i);
	            PageData data = new PageData();
	            for(Entry<String, Object> entry : obj.entrySet()){
	                data.put(entry.getKey(), entry.getValue());
	            }
	            // 非用户自定义字段
	            data.put("CORP_ID", corpId);
				data.put("DATA_DATE", dataDate);
				data.put("SORT_NO", obj.get("num"));
				data.put("PARENT_ID", "");
				data.put("LEVEL", "1");
				if (Tools.isEmpty(transId)) {
					// 对于不需要审核的记录，默认可用状态为可用
					data.put("STATUS", "1");
				} else {
					// 对于需要审核的记录，默认可用状态为不可用，审批通过之后才可用
					data.put("STATUS", "0");
				}
				data.put("CREATE_ID", Jurisdiction.getUsername());
				data.put("CREATE_TIME", getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				data.put("UPDATE_TIME", data.getString("CREATE_TIME"));
				templateService.insert(tablesId, data, colDef);
	        }
	        // 修改记录
	        jsonArray = JSONArray.fromObject(modified);
			mapListJson = (List)jsonArray;  
	        for (int i = 0; i < mapListJson.size(); i++) {
	            Map<String,Object> obj = mapListJson.get(i);
	            PageData data = new PageData();
	            for(Entry<String, Object> entry : obj.entrySet()){
	                data.put(entry.getKey(), entry.getValue());
	            }
				data.put("UPDATE_TIME", getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
				templateService.save(tablesId, data, colDef);
	        }
	        // 删除记录
	        jsonArray = JSONArray.fromObject(deleted);
			mapListJson = (List)jsonArray;  
	        for (int i = 0; i < mapListJson.size(); i++) {
	            Map<String,Object> obj = mapListJson.get(i);  
	            PageData data = new PageData();
	            for(Entry<String, Object> entry : obj.entrySet()){
	                data.put(entry.getKey(), entry.getValue());
	            }  
				templateService.deleteBusinessData(tablesId, data);
	        }
	        // 更新B_TRANS表的commit flag状态
			PageData data = new PageData();
			data.put("TRANS_ID", transId);
			data.put("COMMIT_TIME", Tools.date2Str(new Date()));
			data.put("COMMIT_FLAG", "1"); //提交标志：0：驳回 1：已保存 2：待审核 3：一审通过 4：二审通过
			data.put("COMMIT_USER", user.getUSER_ID());
			transService.editFlag(data);
			bRet = true;
		} catch (Exception e) {
			ret.put("msg", e.getMessage());
		}
		ret.put("success", bRet);
		return ret;
	}
	/**提交审核
	 * @param
	 * @throws Exception
	 */
	public Map<String, Object> submit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"提交审核");
		Map<String, Object> ret = new HashMap<String, Object>();
		boolean bRet = false;
		try {
			Session session = Jurisdiction.getSession();
			User user = (User) session.getAttribute(Const.SESSION_USER);
			PageData pd = this.getPageData();
			String transId = pd.getString("TRANS_ID");
			String tablesId = pd.getString("TABLES_ID");
			String type = pd.getString("TYPE");
			String corpId = pd.getString("CORP_ID");
			String dataDate = pd.getString("DATA_DATE");
			// 插入B_NODE表
			// 
			PageData pdRule = new PageData();
			pdRule.put("BIANMA", tablesId);
			PageData pdRuleData = approveruleService.findByBianma(pdRule);
			if (pdRuleData == null) {
				ret.put("msg", "请设置审核规则.");
				ret.put("success", false);
				return ret;
			}
			String id0 = pdRuleData.getString("APPROVERULE_ID");
			List<ApproveRule> listLevel1 = approveruleService.listSubDictByParentId(id0); // 第一层审批节点列表
			int levels = listLevel1.size();  // 写回B_TRANS的AUDIT_LEVEL===========================
			ApproveRule ar = listLevel1.get(0); // 获取第一个审批节点
			String id1 = ar.getAPPROVERULE_ID(); // 写回B_TRANS的AUDIT_USER_LEVEL1=================
			List<ApproveRule> listLevel2 = approveruleService.listSubDictByParentId(id1); // 第二层审批角色列表
			PageData pdNode = new PageData();
			for (int index = 0; index < listLevel2.size(); index ++) {
				ApproveRule role = listLevel2.get(index); // 获取审批角色
				String roleId = role.getNAME_EN();
				pdNode.put("NODE_ID", UuidUtil.get32UUID());
				pdNode.put("TRANS_ID", transId);
				pdNode.put("AUDIT_USER_IDS", id1);
				pdNode.put("AUDIT_USER", roleId);
				//pdNode.put("AUDIT_TIME", );
				pdNode.put("AUDIT_STATUS", "0"); // 未审核		
				nodeService.save(pdNode);
			}
			// 更新B_TRANS表
			PageData data = new PageData();
			data.put("TRANS_ID", transId);
			data.put("TYPE", tablesId);
			data.put("CORP_ID", corpId);
			data.put("DATA_DATE", dataDate);
			data.put("COMMIT_TIME", Tools.date2Str(new Date()));
			data.put("COMMIT_FLAG", "2"); //提交标志：0：驳回 1：已保存 2：待审核 3：一审通过 4：二审通过
			data.put("COMMIT_USER", user.getUSER_ID());
			data.put("AUDIT_USER_LEVEL1", id1);
			data.put("AUDIT_LEVEL", levels);
			data.put("MODIFY_DATE", Tools.date2Str(new Date()));
			data.put("OPERATOR", user.getUSER_ID());
			transService.edit(data);
			bRet = true;
		} catch (Exception e) {
			ret.put("msg", e.getMessage());
		}
		ret.put("success", bRet);
		return ret;
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public ModelAndView list(Page page, boolean loadData) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Template");
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
        pd.put("CORP_ID", Jurisdiction.getTopCorpId()); // columns等数据所有分公司都可以查看

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

		if (true == loadData) {
			PageData params = new PageData();
			params.put("TABLES_ID", tablesId);
			params.put("DATA_DATE", dataDate);
			params.put("CORP_ID", corpId);
			params.put("LIST_COLUMNS", columnList);
			PageData pdData = templateService.listAll(params);	//列出List列表
			List<PageData> varList = (List<PageData>) pdData.get("varList");
			// 没有初始数据，并且有维度的情况下，插入初始数据
			if (varList.size() == 0 && dimList.size() > 0) {
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
					templateService.insert(tablesId, data, null);
					dataList.add(data);
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
		mv.setViewName("/business/template/template_list");
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
		/*
		 * 第三步，获取模板业务规则--单表规则
		 */
		List<PageData> ruleList = getSingleTableRuleList(indexesList, tablesId);
		jsonarray = JSONArray.fromObject(ruleList, jsonConfig);
		mv.addObject("ruleList", jsonarray);

		return mv;
	}

	/**
	 * 第一步：获取动态指标、维度信息
	 * 获取多表业务规则，设置对应指标的JOIN规则
	 * Get the columsn (indexes)
	 * @param page
	 * @param corpId
	 * @return
	 */
	public Map<String, Object> getAllColumnsList(Page page, String corpId) {

		Map<String, Object> map = new HashMap<String, Object>();

		try {
            PageData pd = page.getPd();
            pd.put("CORP_ID", corpId);  // Replaced with top corp id
			// main table id
			String tablesId = pd.getString("TABLES_ID");

			page.setShowCount(MAX_SHOW_COUNT);
			List<PageData>	indexesList = columnsService.list(page);		//后面列头--STUDENT_ROSTER
	//		PageData pdTmp = new PageData();
	//		pdTmp.put("CORP_ID", pd.get("CORP_ID"));
			if (indexesList.size() > 0) {
				List<PageData> joinList = multitableruleService.listAll(pd);//STUDENT
				// 设置列的关联属性，前台已下拉列表展现
				for (int idx = 0; idx < indexesList.size(); idx ++) {
					PageData pdColumn = indexesList.get(idx);
					// 如果是SYS_DEFAULT的CORP_ID,自动关联
					if ("CORP_ID".equals(pdColumn.get("COLUMN_NAME_EN"))) {
						pdColumn.put("JOIN_TABLES_ID", "SYS_CORP");			// SYS_CORP. Original table should be SYS_CORP, but CORP matches CORP_ID
						pdColumn.put("JOIN_COLUMNS_NAME_EN", "CORP_ID");	// CORP_ID
						pdColumn.put("JOIN_SELECT_COLUMNS_NAME_EN", "NAME");// NAME.
						pdColumn.put("MASTER_TABLES_ID", tablesId);			// COURSE a
						pdColumn.put("MASTER_COLUMNS_NAME_EN", "CORP_ID");	// a.CORP_ID
						// 可以使用上级设定的关联属性，但只能关联自己/下级的数据（corpId）
                        /*
                         * BUG：有可能关联上级的数据, 数据项可以通过corpId来控制权限,
                         * 但是关联数据项不控制权限,也就是关联数据项都能访问（上级的也可以）
                         */
						pdColumn.put("CORP_ID", corpId); //pdJoin.get("CORP_ID"));--设定者（who）的组织ID
						// 外联SYS_CORP表的CORP_ID字段
						pdColumn.put("JOIN_CONDITION", "1");
						indexesList.set(idx, pdColumn);
						continue;
					}
                    // 如果是关联字典表, 需要设置来源
                    if (StringUtil.strIsNotEmpty(pdColumn.getString("DATA_SOURCE"))) {
                        String dataSource = pdColumn.getString("DATA_SOURCE");
                        pd.put("DICT_ID", dataSource);
                        List<PageData> dictList = dictService.listAllForControls(pd);
                        JSONArray dictArray = JSONArray.fromObject(dictList);
                        pdColumn.put("DATA_SOURCE", dictArray.toString());
                        indexesList.set(idx, pdColumn);
                    }
					// 如果是SYS_DEFAUTL=1的列，设置为只读
					// 不需要，在INDEX表中有配置
//					if ("1".equals(pdColumn.get("SYS_DEFAUTL"))) {
//						pdColumn.put("INITIAL_TYPE_NAME", "ZIDONG");
//						indexesList.set(idx, pdColumn);
//					}
					for (int index = 0; index < joinList.size(); index ++) {
						PageData pdJoin = joinList.get(index);
						// COLUMNS_ID每次都变化，INDEXES_ID不会变化，所以用INDEXES_ID来关联
						String columnsId = pdJoin.getString("COLUMNS_ID");
						if (!pdColumn.get("COLUMNS_ID").equals(columnsId)) {
							continue;
						}
						// {COLUMNS_ID=d9651486cf6b49f19eb8fd87d38279f8, NAME=学生出勤学科名称映射,
						// JOIN_COLUMNS_NAME_EN=COURSE_NAME, JOIN_COLUMNS_ID=d9651486cf6b49f19eb8fd87d38279f8,
						// CORP_ID=29fb9c3d04034e34aa1484c81bf06165, JOIN_CONDITION=,
						// RSV_STR1=, COLUMNS_NAME_EN=COURSE_NAME, RSV_STR2=, TABLES_ID=STUDENT_ROSTER, 
						// MULTITABLERULE_ID=106b530518c5499bb2e5e5b010165791, JOIN_TABLES_ID=COURSE}
						pdColumn.put("JOIN_TABLES_ID", pdJoin.get("JOIN_TABLES_ID"));					// COURSE.
						pdColumn.put("JOIN_COLUMNS_NAME_EN", pdJoin.get("JOIN_TABLES_ID") + "_ID");		// COURSE_ID---外联用
						pdColumn.put("JOIN_COLUMNS_ID", pdJoin.get("JOIN_COLUMNS_ID"));		            // ebc581b9e1ff47afa1ac481ba6e79287---内联用
						pdColumn.put("JOIN_SELECT_COLUMNS_NAME_EN", pdJoin.get("JOIN_COLUMNS_NAME_EN"));// COURSE_NAME---外联JOIN COLUMN NAME是一样的(COLUMNS_ID不一样)
						pdColumn.put("MASTER_TABLES_ID", tablesId);										// ROSTER.
						pdColumn.put("MASTER_COLUMNS_NAME_EN", pdJoin.get("COLUMNS_NAME_EN"));		    // COURSE_NAME
						// 可以使用上级设定的关联属性，但只能关联自己/下级的数据（corpId）
						pdColumn.put("CORP_ID", corpId); //pdJoin.get("CORP_ID"));--设定者（who）的组织ID
						// 外联SYS_CORP表的CORP_ID字段
						pdColumn.put("JOIN_CONDITION", pdJoin.get("JOIN_CONDITION"));
						indexesList.set(idx, pdColumn);
						break;
					}
				}
			}
			map.put("indexesList", indexesList);
			// Get the rows (dimension items)
			List<PageData>	dimList = rowsService.list(page);	//前面列头
			map.put("dimList", dimList);
			// Compose all the columns
			List<Column> columnList = new ArrayList<Column>();
			for (int i = 0; i < dimList.size(); i++) {
			    PageData tmp = dimList.get(i);
			    Column column = new Column();
			    column.setNAME(tmp.getString("DIMENTIONS_NAME_CN"));
			    column.setNAME_EN(tmp.getString("DIMENTIONS_NAME_EN"));
			    columnList.add(column);
			}
			for (int i = 0; i < indexesList.size(); i++) {
			    PageData tmp = indexesList.get(i);
			    Column column = new Column();
			    column.setNAME(tmp.getString("COLUMN_NAME_CN"));
			    column.setNAME_EN(tmp.getString("COLUMN_NAME_EN"));
			    column.setENCRYPT(tmp.getString("RSV_STR2"));
			    columnList.add(column);
			}
			map.put("columnList", columnList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 第三步，获取模板业务规则--单表规则
	 * @param indexesList
	 * @param tablesId
	 * @return
	 */
	public List<PageData> getSingleTableRuleList(List<PageData> indexesList, String tablesId) {
		/*
		 */
		List<PageData> ruleList = null;
		try {
			PageData pdTableRule = new PageData();
			pdTableRule.put("TABLES_ID", tablesId);
			ruleList = tableruleService.listAll(pdTableRule);
			for (int index = 0; index < ruleList.size(); index ++) {
				boolean bChecked = false;
				String dataType = "";
				PageData item = ruleList.get(index);
				String OUT_FIELD = item.getString("OUT_FIELD");
				String IN_FIELD1 = item.getString("IN_FIELD1");
				String IN_FIELD2 = item.getString("IN_FIELD2");
				for (int i = 0; i < indexesList.size(); i ++) {
					PageData tmp = indexesList.get(i);
					if (OUT_FIELD.equals(tmp.getString("COLUMN_NAME_EN"))) {
						bChecked = true;
						dataType = tmp.getString("DATA_TYPE_NAME");
						break;
					}
				}
				if (!bChecked) {
					item.put("CHECKED", bChecked);
					ruleList.set(index, item);
					continue;
				}
				bChecked = false;
				for (int i = 0; i < indexesList.size(); i ++) {
					PageData tmp = indexesList.get(i);
					if (IN_FIELD1.equals(tmp.getString("COLUMN_NAME_EN"))) {
						bChecked = true;
	//						dataType = tmp.getString("DATA_TYPE_NAME");
						break;
					}
				}
				if (!bChecked) {
					item.put("CHECKED", bChecked);
					ruleList.set(index, item);
					continue;
				}
				bChecked = false;
				for (int i = 0; i < indexesList.size(); i ++) {
					PageData tmp = indexesList.get(i);
					if (IN_FIELD2.equals(tmp.getString("COLUMN_NAME_EN"))) {
						bChecked = true;
	//						dataType = tmp.getString("DATA_TYPE_NAME");
						break;
					}
				}
	
				item.put("CHECKED", bChecked);
				item.put("DATA_TYPE", dataType);
				ruleList.set(index, item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ruleList;
	}
	public int getChangeSize(int index, List<Integer> dimSizeList) {
		int size = 1;
		for (int i = index + 1; i < dimSizeList.size(); i ++) {
			size *= dimSizeList.get(i);
		}
		return size;
	}

	/**
	 * 第N步：获取WHERE语句
	 * @param page
	 * @param pd
	 * @param corpId
	 * @return
	 */
	public Map<String, Object> getWhere(Page page, PageData pd, String corpId) {
		String where = pd.getString("WHERE");
		JSONArray jsonArray = JSONArray.fromObject(where);
		List<Map<String,Object>> mapListJson = (List)jsonArray;  
        for (int i = 0; i < mapListJson.size(); i++) {
            Map<String,Object> obj = mapListJson.get(i);
            PageData data = new PageData();
            for(Entry<String, Object> entry : obj.entrySet()){
                data.put(entry.getKey(), entry.getValue());
            }
        }
		return null;
	}
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}


	public ModelAndView doList(Page page, ModelAndView mv, String menuUrl, boolean loadData) throws Exception {
		Session session = Jurisdiction.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);

		PageData pd = this.getPageData();
		String dataDate = pd.getString("DATA_DATE");
		if (dataDate != null && !"".equals(dataDate)) {
			dataDate = dataDate.substring(0, 10);
		}
		String corpId = pd.getString("CORP_ID");	// 提交人公司ID
		if (corpId == null) {
			corpId = user.getCORP_ID();					// 否则当前登录人就是提交人
		}
		pd.put("CORP_ID", Jurisdiction.getTopCorpId()); // columns等数据所有分公司都可以查看

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

		if (true == loadData) {
			PageData params = new PageData();
			params.put("TABLES_ID", tablesId);
			params.put("DATA_DATE", dataDate);
			params.put("CORP_ID", corpId);
			params.put("LIST_COLUMNS", columnList);
			params.put("WHERE", pd.getString("WHERE"));
			params.put("pageNum", pd.getString("pageNum"));
			params.put("pageSize", pd.getString("pageSize"));
			System.out.println(params.toString());
			PageData pdData = templateService.listAll(params);	//列出List列表
			List<PageData> varList = (List<PageData>) pdData.get("varList");
			// 没有初始数据，并且有维度的情况下，插入初始数据
			if (varList.size() == 0 && dimList.size() > 0) {
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
					templateService.insert(tablesId, data, null);
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
		mv.addObject("fromAction", menuUrl);
		/*
		 * 第三步，获取模板业务规则--单表规则
		 */
		List<PageData> ruleList = getSingleTableRuleList(indexesList, tablesId);
		jsonarray = JSONArray.fromObject(ruleList, jsonConfig);
		mv.addObject("ruleList", jsonarray);

		return mv;
	}
}
