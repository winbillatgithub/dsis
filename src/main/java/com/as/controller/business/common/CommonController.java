package com.as.controller.business.common;

import java.io.PrintWriter;
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

import org.apache.shiro.session.Session;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.as.controller.base.BaseController;
import com.as.entity.Page;
import com.as.entity.business.ApproveRule;
import com.as.entity.business.Column;
import com.as.entity.system.Department;
import com.as.entity.system.User;
import com.as.util.AppUtil;
import com.as.util.Const;
import com.as.util.JsonDateValueProcessor;
import com.as.util.JsonHelper;
import com.as.util.ObjectExcelView;
import com.as.util.PageData;
import com.as.util.Tools;
import com.as.util.UuidUtil;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import com.as.util.Jurisdiction;
import com.as.service.business.approverule.ApproveRuleManager;
import com.as.service.business.columns.ColumnsManager;
import com.as.service.business.common.CommonManager;
import com.as.service.business.dimentions.DimentionsManager;
import com.as.service.business.dimentionsdetail.DimentionsDetailManager;
import com.as.service.business.indexes.IndexesManager;
import com.as.service.business.multitablerule.MultiTableRuleManager;
import com.as.service.business.node.NodeManager;
import com.as.service.business.rows.RowsManager;
import com.as.service.business.tablerule.TableRuleManager;
import com.as.service.business.tables.TablesManager;
import com.as.service.business.common.CommonManager;
import com.as.service.business.trans.TRANSManager;
import com.as.service.system.department.DepartmentManager;
import com.as.service.system.dictionaries.DictionariesManager;
import com.as.service.system.role.RoleManager;

/** 
 * 说明：每日销售完成情况
 * 创建人：antispy
 * 创建时间：2016-09-04
 */
@Controller
@RequestMapping(value="/common")
public class CommonController extends BaseController {

	String menuUrl = "common/list.do"; //菜单地址(权限用)
	@Resource(name="commonService")
	private CommonManager commonService;

	// 模板
	@Resource(name="tablesService")
	private TablesManager tablesService;

	// 模板-维度
	@Resource(name="rowsService")
	private RowsManager rowsService;
	@Resource(name="dimentionsService")
	private DimentionsManager dimentionsService;
	@Resource(name="dimentionsdetailService")
	private DimentionsDetailManager dimentionsdetailService;

	// 模板-指标
	@Resource(name="columnsService")
	private ColumnsManager columnsService;
	@Resource(name="indexesService")
	private IndexesManager indexesService;

	// 数据传输--审批规则
	@Resource(name="transService")
	private TRANSManager transService;
	@Resource(name="approveruleService")
	private ApproveRuleManager approveruleService;
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	@Resource(name="roleService")
	private RoleManager roleService;
	@Resource(name="nodeService")
	private NodeManager nodeService;

	/*
	 * 应用在两表之间字段关联
	 * columnsId -> COLUMN_NAME_EN
	 */
	@RequestMapping(value="/getjoinlist", method = RequestMethod.POST)
	@ResponseBody
	public Map getjoinlist(String tablesId, String joinColumnsId, String selectColumnsId, String corpId, String in,
                           String reserved) {
		// 保持顺序
		Map<String, Object> ret = new HashMap<String, Object>();
		boolean bRet = false;
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		// 增加空行，用来不选中的情况
		map.put("", "");
		try {
//			PageData pd = new PageData();
//			pd.put("COLUMNS_ID", columnsId);
//			pd = columnsService.findById(pd);
//			String columnsName = pd.getString("COLUMN_NAME_EN");
            PageData pdCommon = new PageData();
            pdCommon.put("tablesId", tablesId);
            pdCommon.put("joinColumnsId", joinColumnsId);
            pdCommon.put("selectColumnsId", selectColumnsId);
            pdCommon.put("corpId", corpId);
            pdCommon.put("in", in);
			List<PageData> list = commonService.getColumnIDMap(pdCommon, null);
			for (PageData item : list) {
				// COLUMNS_ID==>INDEXES_ID==>COLUMNS_NAME_EN==>STUDENT_NAME
				// tablesId_ID==>STUDENT_ID<==>STUDENT_NAME
				map.put(item.getString(joinColumnsId), item.getString(selectColumnsId));
			}
			bRet = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret.put("msg", e.getMessage());
		}
		ret.put("success", bRet);
		ret.put("data", map);
        ret.put("reserved", reserved);
		return ret;
	}

}
