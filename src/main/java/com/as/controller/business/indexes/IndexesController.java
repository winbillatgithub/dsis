package com.as.controller.business.indexes;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.as.service.business.columns.ColumnsManager;
import com.as.service.business.dict.DictManager;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.as.controller.base.BaseController;
import com.as.entity.Page;
import com.as.entity.system.Dictionaries;
import com.as.util.AppUtil;
import com.as.util.JsonHelper;
import com.as.util.ObjectExcelView;
import com.as.util.PageData;
import com.as.util.StringUtil;
import com.as.util.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.as.util.Jurisdiction;
import com.as.service.business.cellrule.CellRuleManager;
import com.as.service.business.indexes.IndexesManager;
import com.as.service.system.dictionaries.DictionariesManager;

/** 
 * 说明：指标维护
 * 创建人：antispy
 * 创建时间：2016-09-08
 */
@Controller
@RequestMapping(value="/indexes")
public class IndexesController extends BaseController {
	
	String menuUrl = "indexes/list.do"; //菜单地址(权限用)
	@Resource(name="indexesService")
	private IndexesManager indexesService;
    @Resource(name="columnsService")
    private ColumnsManager columnsService;
	@Resource(name="dictionariesService")
	private DictionariesManager dictionariesService;
	@Resource(name="cellruleService")
	private CellRuleManager cellruleService;
    @Resource(name="dictService")
    private DictManager dictService;

    // 数据类型（存储）, string, date, integer
	private final String DICTIONARIES_ID = "210803cc78fe48539a9dc70513f05b04";
    // 显示控件类型subType
	private final String XIANSHIKONGJIAN_LEIXING_ID = "5be955f0f3d54398a0d16cf94563eb74";
    // 初始化类型,手动/自动
	private final String SHUJUCHUSHIHUA_LEIXING_ID = "a77a89be2d1148f6a08d487056049c58";

    // 控件类型
    private static final List<PageData> controlTypeList = new ArrayList<PageData>();
    // 控件自类型
    private static final List<PageData> controlSubTypeList = new ArrayList<PageData>();

    // 指标类型的key值转换
	private static final Map<String, String> itemMap;
	static
    {
        itemMap = new HashMap<String, String>();
        itemMap.put("31de79884df949b4bc8c54946ef540ef", "left_wb");  // 文本
        itemMap.put("d2fb286ac25f469a981a1db968799344", "left_zs");  // 整书
        itemMap.put("e02399f99f254975bf5516f141bad8e6", "left_xs");  // 小数
        itemMap.put("31de79884df949b4bc8c54946ef540ee", "left_fx");  // 复选 						   // 复选框
        itemMap.put("5d8a580c34fc45d8bde9bab467284067", "left_dx");  // 单选
        itemMap.put("5d8a580c34fc45d8bde9bab467284067", "left_xl");  // 下拉
        itemMap.put("369962cda2384b6f8dd65f046df8109a", "left_rqsj");  // 日期 时间
        itemMap.put("64867ea408d84da1a5d8162b02af04ba", "left_rq");  // 日期
        itemMap.put("3cab25ba75434848807adc0ff4594ccc", "left_sj");  // 时间
        itemMap.put("1871c3254b414ec0822ed40b45256cce", "left_tp");  // 图片
        itemMap.put("ea5c18254ff1476ab9e8d6acec878963", "left_pdf"); // pdf
        itemMap.put("3dad472def0e4bec841ee214cbeacc47", "left_office");// office
        itemMap.put("4a4cad441c78407d8b8606b4a331aced", "left_sp");  // 视频
        itemMap.put("711f1e52b6af41f782f5d680b166d521", "left_qt");  // 其他附件

        // "text”, ”textarea”, ”select”,  ”radio-group”, ”number”, ”checkbox-group”, ”asfile”, ”date”, ”datetime”
        PageData pdStatic = new PageData();
        pdStatic.put("key", "text");
        pdStatic.put("value", "单行文本");
        controlTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "textarea");
        pdStatic.put("value", "多行文本");
        controlTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "number");
        pdStatic.put("value", "数值");
        controlTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "select");
        pdStatic.put("value", "下拉列表");
        controlTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "radio-group");
        pdStatic.put("value", "单选框");
        controlTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "checkbox-group");
        pdStatic.put("value", "复选框");
        controlTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "date");
        pdStatic.put("value", "日期");
        controlTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "datetime");
        pdStatic.put("value", "日期时间");
        controlTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "asfile");
        pdStatic.put("value", "附件");
        controlTypeList.add(pdStatic);

        // text / password / email / color / tel  / datetime-local / texture / tinymce / quill
        pdStatic = new PageData();
        pdStatic.put("key", "text");
        pdStatic.put("value", "文本");
        controlSubTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "password");
        pdStatic.put("value", "密码");
        controlSubTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "email");
        pdStatic.put("value", "邮箱");
        controlSubTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "color");
        pdStatic.put("value", "颜色");
        controlSubTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "textarea");
        pdStatic.put("value", "多行文本");
        controlSubTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "tinymce");
        pdStatic.put("value", "tinymce富文本");
        controlSubTypeList.add(pdStatic);
        pdStatic = new PageData();
        pdStatic.put("key", "quill");
        pdStatic.put("value", "quill富文本");
        controlSubTypeList.add(pdStatic);
    }
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Indexes");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("INDEXES_ID", this.get32UUID());	//主键
		pd.put("DATA_SOURCE", "");	//数据来源
		pd.put("RSV_STR1", "0");	//免费/收费
		//pd.put("RSV_STR2", "");		//加密存储
		pd.put("SYS_DEFAUTL", "0");	//系统预留
		pd.put("CREATE_DATE", Tools.date2Str(new Date()));	//创建日期
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		indexesService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		return mv;
	}
	
	/**删除
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(HttpServletResponse response) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除Indexes");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        PageData pd = this.getPageData();
        // 查看b_columns表中是否被引用,如果被引用不能删除
        List<PageData> pdColumns = columnsService.findByIndexesId(pd);
        if (pdColumns.size() <= 0) {
            indexesService.delete(pd);
            out.write("success");
        } else {
            String tips = "表单元素<";
            for (int index = 0; index < pdColumns.size(); index++) {
                pd = pdColumns.get(index);
                String columnName = pd.getString("COLUMN_NAME_CN");
                if (index == 0) {
                    tips += columnName;
                    tips += ">被表单<";
                }
                String tableName = pd.getString("TABLE_NAME");
                tips += tableName;
                if (index < pdColumns.size() - 1) {
                    tips += ",";
                }
            }
            tips += ">引用, 不能被删除.";
            out.write(tips);
        }
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Indexes");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		indexesService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Indexes");
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
		pd.put("SYS_DEFAUTL", "0");
		List<PageData>	varList = indexesService.listAll(pd);	//列出Indexes列表
		String viewStyle = String.valueOf(pd.get("view_type"));
		if ("dragdrop".equals(viewStyle)) {

			for (PageData var : varList) {
				var.put("ITEM_TYPE", itemMap.get(var.get("DATA_TYPE")));
			}
			mv.setViewName("/business/indexes/indexes_dragdrop");
		} else {
			mv.setViewName("/business/indexes/indexes_list");
		}
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		
		// 字典
		List<Dictionaries> list = dictionariesService.listSubDictByParentId(DICTIONARIES_ID);
		mv.addObject("indexesList", list);
//		list = dictionariesService.listSubDictByParentId(XIANSHIKONGJIAN_LEIXING_ID);
		mv.addObject("controlTypeList", controlTypeList);
        mv.addObject("controlSubTypeList", controlSubTypeList);
		list = dictionariesService.listSubDictByParentId(SHUJUCHUSHIHUA_LEIXING_ID);
		mv.addObject("dataTypeList", list);
		List<PageData> listRule = cellruleService.listAll(pd);
		mv.addObject("ruleList", listRule);

        // 用户自定义字典
        PageData pdDict = new PageData();
        pdDict.put("CORP_ID", Jurisdiction.getTopCorpId());
        pdDict.put("DICT_ID", "0");
        List<PageData> sourcelist = dictService.listAll(pdDict);
        mv.addObject("sourcelist", sourcelist);

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
		pd.put("LENG", 0);
		pd.put("DATA_PRECISION", 0);
		pd.put("NULLABLE", "1");
		pd.put("RSV_STR2", "0");		//加密存储
		// Add CORP_ID Winbill. 20170301 <==
		mv.setViewName("/business/indexes/indexes_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		
		// 字典
		List<Dictionaries> list = dictionariesService.listSubDictByParentId(DICTIONARIES_ID);
		mv.addObject("indexesList", list);
//		list = dictionariesService.listSubDictByParentId(XIANSHIKONGJIAN_LEIXING_ID);
        mv.addObject("controlTypeList", controlTypeList);
        mv.addObject("controlSubTypeList", controlSubTypeList);
		list = dictionariesService.listSubDictByParentId(SHUJUCHUSHIHUA_LEIXING_ID);
		mv.addObject("dataTypeList", list);
		List<PageData> listRule = cellruleService.listAll(pd);
		mv.addObject("ruleList", listRule);

        PageData pdDict = new PageData();
        pdDict.put("CORP_ID", Jurisdiction.getTopCorpId());
        pdDict.put("DICT_ID", "0");
        List<PageData> sourcelist = dictService.listAll(pdDict);
        mv.addObject("sourcelist", sourcelist);

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
		pd = indexesService.findById(pd);	//根据ID读取
		mv.setViewName("/business/indexes/indexes_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		
		// 字典
		List<Dictionaries> list = dictionariesService.listSubDictByParentId(DICTIONARIES_ID);
		mv.addObject("indexesList", list);
//		list = dictionariesService.listSubDictByParentId(XIANSHIKONGJIAN_LEIXING_ID);
		mv.addObject("controlTypeList", controlTypeList);
        mv.addObject("controlSubTypeList", controlSubTypeList);
		list = dictionariesService.listSubDictByParentId(SHUJUCHUSHIHUA_LEIXING_ID);
		mv.addObject("dataTypeList", list);
		List<PageData> listRule = cellruleService.listAll(pd);
		mv.addObject("ruleList", listRule);

        PageData pdDict = new PageData();
        pdDict.put("CORP_ID", Jurisdiction.getTopCorpId());
        pdDict.put("DICT_ID", "0");
        List<PageData> sourcelist = dictService.listAll(pdDict);
        mv.addObject("sourcelist", sourcelist);

		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Indexes");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			indexesService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Indexes到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("指标标识");	//1
		titles.add("指标标题");	//2
		titles.add("指标类型");	//3
		titles.add("指标精度");	//4
		titles.add("小数位");	//5
		titles.add("初始化类型");	//6
		titles.add("默认值");	//7
		titles.add("校验规则");	//8
		titles.add("显示控件类型");	//9
		titles.add("输入属性");	//10
		titles.add("排序号");	//11
		titles.add("指标说明");	//12
		titles.add("可空");	//13
		titles.add("控件格式");	//14
		titles.add("数据来源");	//15
		titles.add("保留字段1");	//16
		titles.add("保留字段2");	//17
		titles.add("保留字段3");	//18
		titles.add("保留字段4");	//19
		titles.add("创建日期");	//20
		titles.add("修改日期");	//21
		titles.add("操作人");	//22
		dataMap.put("titles", titles);
		List<PageData> varOList = indexesService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("COLUMN_NAME_EN"));	//1
			vpd.put("var2", varOList.get(i).getString("COLUMN_NAME_CN"));	//2
			vpd.put("var3", varOList.get(i).getString("DATA_TYPE"));	//3
			vpd.put("var4", varOList.get(i).get("LENG").toString());	//4
			vpd.put("var5", varOList.get(i).get("DATA_PRECISION").toString());	//5
			vpd.put("var6", varOList.get(i).getString("INITIAL_TYPE"));	//6
			vpd.put("var7", varOList.get(i).getString("INITIAL_DATA"));	//7
			vpd.put("var8", varOList.get(i).getString("VALIDATE_RULE"));	//8
			vpd.put("var9", varOList.get(i).getString("CONTROL_TYPE"));	//9
			vpd.put("var10", varOList.get(i).getString("READ_DISABLE"));	//10
			vpd.put("var11", varOList.get(i).get("SORT_NO").toString());	//11
			vpd.put("var12", varOList.get(i).getString("REMARK"));	//12
			vpd.put("var13", varOList.get(i).getString("NULLABLE"));	//13
			vpd.put("var14", varOList.get(i).getString("CONTROL_FORMAT"));	//14
			vpd.put("var15", varOList.get(i).getString("DATA_SOURCE"));	//15
			vpd.put("var16", varOList.get(i).getString("RSV_STR1"));	//16
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

	/**
	 * 同一公司下COLUMN_NAME_EN不能重复，并且不能和SYS_DEFAULT的字段重名
	 */
	@RequestMapping(value="/hasBianma")
	@ResponseBody
	public Object hasBianma() {
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = this.getPageData();
		try{
			pd.put("CORP_ID", Jurisdiction.getTopCorpId());
			String columnNameEn = pd.getString("COLUMN_NAME_EN");
			if (isSysDefault(columnNameEn)) {
				errInfo = "err_sys";
			} else {
				List<PageData>	indexesList = indexesService.listAll(pd);
				if (indexesList.size() > 0) {
					errInfo = "err_dup";
				}
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
			errInfo = "err_excep";
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/saveNew")
	public ModelAndView saveNew() throws Exception{
		
		
		logBefore(logger, Jurisdiction.getUsername()+"新增Indexes");
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		String jsonData = pd.getString("data");
		JSONObject fields = JSONObject.fromObject(jsonData);
		String delFields = fields.getString("delFields");
		String[] delFieldArray = delFields.split(",");
		for (String delField:delFieldArray) {
			// 做删除操作
		}
		
		JSONArray jsArray =  fields.getJSONArray("existFields");
//		 for (int i=0;i<jsArray.size();i++) {
//			 JSONObject tempObj = jsArray.getJSONObject(i);
//			 PageData pdSave = new PageData();
//			 pd.put("CORP_ID", Jurisdiction.getTopCorpId());	//组织编码
//			 if ("undefine".equals(tempObj.getString("INDEXES_ID")) || StringUtils.isEmpty(tempObj.getString("INDEXES_ID"))) {
//				 // 说明新增
//				 pd.put("INDEXES_ID", this.get32UUID());	//主键
////				 pd.put("COLUMN_NAME_EN", tempObj.getString("COLUMN_NAME_EN"));	//主键
//				 pd.put("COLUMN_NAME_CN", tempObj.getString("COLUMN_NAME_CN"));	//主键
//				 pd.put("DATA_TYPE", tempObj.getString("DATA_TYPE"));	//主键
// 		 		 pd.put("CONTROL_TYPE", "");	//是否多行 ???????????????????
//				 pd.put("LENG", tempObj.getString("LENG"));	//主键
//				 pd.put("DATA_PRECISION", tempObj.getString("DATA_PRECISION"));	//主键
//				 pd.put("INITIAL_TYPE", tempObj.getString("INITIAL_TYPE"));	//主键
//				 pd.put("INITIAL_DATA", tempObj.getString("INITIAL_DATA"));	//主键
//				 pd.put("VALIDATE_RULE", tempObj.getString("VALIDATE_RULE"));	//主键
//				 pd.put("RSV_STR2", tempObj.getString("RSV_STR2"));	//主键
//				 pd.put("DATA_SOURCE", "");	//数据来源
//				 pd.put("RSV_STR1", "0");	//免费
//				 	//保留字段2
//				 pd.put("SYS_DEFAUTL", "0");	//系统预留
//				pd.put("CREATE_DATE", Tools.date2Str(new Date()));	//创建日期
//				indexesService.save(pd);
//		 	 } else {
//		 		 // 如果有修改标记,但有id,说明是编辑已有,update操作
//		 		 if ("true".equals(tempObj.getString("changed")) {
	//		 		 pd.put("INDEXES_ID", tempObj.getString("INDEXES_ID"));	//主键
	////				 pd.put("COLUMN_NAME_EN", tempObj.getString("COLUMN_NAME_EN"));	//主键
	//				 pd.put("COLUMN_NAME_CN", tempObj.getString("COLUMN_NAME_CN"));	//主键
	//				 pd.put("DATA_TYPE", tempObj.getString("DATA_TYPE"));	//主键
	//	 		 	 pd.put("CONTROL_TYPE", "");	//是否多行 ???????????????????
	//				 pd.put("LENG", tempObj.getString("LENG"));	//主键
	//				 pd.put("DATA_PRECISION", tempObj.getString("DATA_PRECISION"));	//主键
	//				 pd.put("INITIAL_TYPE", tempObj.getString("INITIAL_TYPE"));	//主键
	//				 pd.put("INITIAL_DATA", tempObj.getString("INITIAL_DATA"));	//主键
	//				 pd.put("VALIDATE_RULE", tempObj.getString("VALIDATE_RULE"));	//主键
	//				 pd.put("RSV_STR2", tempObj.getString("RSV_STR2"));	//主键
	//		 		 pd.put("DATA_SOURCE", "");	//数据来源
	//		 		 indexesService.save(pd);
//					}
//		 	 }
//			
//			
//			 
//		 }
		
		System.out.println(jsonData);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		return mv;
	}

    /**批量删除
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/saveDrapdrop")
    @ResponseBody
    public Object saveDrapdrop() {

        logBefore(logger, Jurisdiction.getUsername()+"新增/修改Indexes");

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            PageData pd = this.getPageData();
            String changedFlag = pd.getString("changedFlag");

            pd.put("RSV_STR1", "0");    //免费/收费
            pd.put("SYS_DEFAUTL", "0");    //系统预留
            pd.put("CORP_ID", Jurisdiction.getTopCorpId());
            if ("added".equals(changedFlag)) {
                PageData index = indexesService.findByEnName(pd);
                if (index != null) {
                    throw new Exception("元素英文标识已存在:" + pd.getString("COLUMN_NAME_EN") + ", 请在属性面板设置不同的标题.");
                }
                pd.put("CREATE_DATE", Tools.date2Str(new Date()));    //创建日期
                indexesService.save(pd);
            } else if ("modified".equals(changedFlag)) {
                pd.put("MODIFY_DATE", Tools.date2Str(new Date()));    //修改日期
                indexesService.edit(pd);
            }
            map.put("success", true);
        } catch (Exception e) {
            map.put("success", false);
            map.put("msg", e.getLocalizedMessage());
        }

        return map;
    }
}
