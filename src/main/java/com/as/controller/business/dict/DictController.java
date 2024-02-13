package com.as.controller.business.dict;

import com.as.controller.base.BaseController;
import com.as.entity.Page;
import com.as.entity.system.Dictionaries;
import com.as.entity.system.Role;
import com.as.service.business.dict.DictManager;
import com.as.util.AppUtil;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** 
 * 说明：数据字典
 * 创建人：antispy
 * 创建时间：2015-12-16
 */
@Controller
@RequestMapping(value="/dict")
public class DictController extends BaseController {
	
	String menuUrl = "dict/list.do"; //菜单地址(权限用)
	@Resource(name="dictService")
	private DictManager dictService;

	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Dict");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		pd.put("DICT_ID", this.get32UUID());	//主键
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		dictService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("/save_result");
		return mv;
	}

    /**保存
     * 新增或者编辑, 从json获取的数据
     * 新增只考虑:added 编辑考虑:added, deleted, modified
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/saveJson")
    @ResponseBody
    public Object saveJson() throws Exception{
        logBefore(logger, Jurisdiction.getUsername()+"新增Dict");
        if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
        Map<String,String> map = new HashMap<String,String>();
        PageData pd = this.getPageData();

        // add / edit
        String editMode = pd.getString("EDIT_MODE");
        String dictId = pd.getString("DICT_ID");
        String dictName = pd.getString("NAME");
        String dictNameEn = pd.getString("NAME_EN");
        String bianma = pd.getString("BIANMA");

        /*
         * uiosdfj23:大, sdjfl:小
         */
        String jsonData = pd.getString("codeItems");
        JSONArray jsonArray = JSONArray.fromObject(jsonData);

        try {
            PageData pdMaster = new PageData();
            pdMaster.put("DICT_ID", dictId);
            pdMaster.put("NAME", dictName);
            pdMaster.put("NAME_EN", dictNameEn);
            pdMaster.put("BIANMA", bianma);
            pdMaster.put("ORDER_BY", 0);
            pdMaster.put("PARENT_ID", "0");
            pdMaster.put("NUMBER", "0");
            pdMaster.put("STATUS", "0");        // 0:正常 1:停用
            pdMaster.put("CORP_ID", Jurisdiction.getTopCorpId());
            if ("add".equals(editMode)) {
                dictService.save(pdMaster);
            } else {
                dictService.edit(pdMaster);
            }

            pdMaster.put("NAME_EN", "");
            pdMaster.put("PARENT_ID", dictId);
            for (int index = 0; index < jsonArray.size(); index++) {
                JSONObject obj = jsonArray.getJSONObject(index);
                String editStatus = obj.getString("edit_status");
                // 当前行无变化
                if ("nochange".equals(editStatus)) {
                    continue;
                }
                dictName = obj.getString("NAME");
                String parentId = obj.getString("PARENT_ID");
                bianma = obj.getString("BIANMA");
                dictId = obj.getString("DICT_ID");
                int orderBy = obj.getInt("ORDER_BY");
                String dictStatus = obj.getString("STATUS");
                pdMaster.put("DICT_ID", dictId);
                pdMaster.put("NAME", dictName);
                pdMaster.put("BIANMA", bianma);
                pdMaster.put("STATUS", dictStatus);        // 0:正常 1:停用
                pdMaster.put("ORDER_BY", orderBy);
                if ("added".equals(editStatus)) {
                    dictService.save(pdMaster);
                } else if ("deleted".equals(editStatus)) {
                    pdMaster.put("STATUS", "1");        // 0:正常 1:停用
                    dictService.edit(pdMaster);
                } else if ("modified".equals(editStatus)) {
                    dictService.edit(pdMaster);
                }
            }
        } catch (Exception e) {
            map.put("result", "failed");
            map.put("msg", e.getMessage());
            e.printStackTrace();
            return map;
        }

        map.put("result","success");
        return map;
    }

	/**
	 * 删除
	 * @param DICT_ID
	 * @param
	 * @throws Exception 
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete(@RequestParam String DICT_ID) throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"删除Dict");
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd.put("DICT_ID", DICT_ID);
		String errInfo = "success";
		if(dictService.listSubDictByParentId(DICT_ID).size() > 0){//判断是否有子级，是：不允许删除
			errInfo = "false";
		}else{
			pd = dictService.findById(pd);//根据ID读取
			if(null != pd.get("TBSNAME") && !"".equals(pd.getString("TBSNAME"))){
				String[] table = pd.getString("TBSNAME").split(",");
				for(int i=0;i<table.length;i++){
					pd.put("thisTable", table[i]);
					try {
						if(Integer.parseInt(dictService.findFromTbs(pd).get("zs").toString())>0){//判断是否被占用，是：不允许删除(去排查表检查字典表中的编码字段)
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
			dictService.delete(pd);	//执行删除
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
		logBefore(logger, Jurisdiction.getUsername()+"修改Dict");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		if (pd.getString("BIANMA") == null) {
			pd.put("BIANMA", "");
		}
		dictService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Dict");
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// Add CORP_ID Winbill. 20170301 ==>
		pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		// Add CORP_ID Winbill. 20170301 <==
		String keywords = pd.getString("keywords");					//检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String DICT_ID = null == pd.get("DICT_ID")?"":pd.get("DICT_ID").toString();
		if(null != pd.get("id") && !"".equals(pd.get("id").toString())){
			DICT_ID = pd.get("id").toString();
		}
		pd.put("DICT_ID", DICT_ID);					//上级ID
		page.setPd(pd);
		List<PageData>	varList = dictService.list(page);	//列出Dict列表
		mv.addObject("pd", dictService.findById(pd));		//传入上级所有信息
		mv.addObject("DICT_ID", DICT_ID);			//上级ID
		mv.setViewName("/business/dict/dict_list");
		mv.addObject("varList", varList);
		mv.addObject("QX",Jurisdiction.getHC());					//按钮权限
		
		Integer NUMBER = (pd.get("NUMBER") == null) ? 0 : Integer.parseInt(pd.getString("NUMBER"));
		if (NUMBER == null) {
			NUMBER = 0;
		}
		mv.addObject("NUMBER", NUMBER);					//层级

		return mv;
	}

    /**
     * 根据父节点ID,查找所有子项
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/findItems")
    @ResponseBody
    public Object findItems() throws Exception{
        logBefore(logger, Jurisdiction.getUsername()+" findItems");
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = this.getPageData();
        pd.put("CORP_ID", Jurisdiction.getTopCorpId());
        map.put("pd", dictService.listAll(pd));		//传入上级所有信息
        map.put("result", "success");
        return map;
    }
	/**
	 * 显示列表ztree
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/listAllDict")
	public ModelAndView listAllDict(Model model,String DICT_ID,Integer NUMBER)throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		try{
			JSONArray arr = JSONArray.fromObject(dictService.listAllDict("0"));
			String json = arr.toString();
			json = json.replaceAll("DICT_ID", "id").replaceAll("PARENT_ID", "pId").replaceAll("NAME", "name").replaceAll("subDict", "nodes").replaceAll("hasDict", "checked").replaceAll("treeurl", "url");
			model.addAttribute("zTreeNodes", json);
			mv.addObject("DICT_ID",DICT_ID);
			if (NUMBER == null) {
				NUMBER = 0;
			}
			mv.addObject("NUMBER",NUMBER);
			mv.addObject("pd", pd);	
			mv.setViewName("/business/dict/dict_ztree");
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
		String DICT_ID = null == pd.get("DICT_ID")?"":pd.get("DICT_ID").toString();
		pd.put("DICT_ID", DICT_ID);					//上级ID
		mv.addObject("pds",dictService.findById(pd));		//传入上级所有信息
		mv.addObject("DICT_ID", DICT_ID);			//传入ID，作为子级ID用
		mv.setViewName("/business/dict/dict_edit");
		mv.addObject("msg", "save");
		
		Integer NUMBER = (pd.get("NUMBER") == null) ? 0 : Integer.parseInt(pd.getString("NUMBER"));
		if (NUMBER == null) {
			NUMBER = 0;
		}
		mv.addObject("NUMBER", NUMBER);					//层级

        // New added item
        if (pd.get("ORDER_BY") == null) {
            // Max sort no
            int maxSortNo = 1;
            PageData sortNoPd = dictService.findMaxSortNo(pd);
            if (sortNoPd != null) {
                if (sortNoPd.get("ORDER_BY") != null) {
                    try {
                        maxSortNo = Integer.parseInt(sortNoPd.get("ORDER_BY").toString());
                    } catch (Exception e) {
                    }
                    maxSortNo++;
                }
            } else {
                sortNoPd = new PageData();
            }
            sortNoPd.put("ORDER_BY", maxSortNo);            //最大排序号
            sortNoPd.put("STATUS", "0");			        //状态:启用
            mv.addObject("pd", sortNoPd);					//放入视图容器
        }

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

		String DICT_ID = pd.getString("DICT_ID");
		pd = dictService.findById(pd);	//根据ID读取
		mv.addObject("pd", pd);					//放入视图容器
		pd.put("DICT_ID",pd.get("PARENT_ID").toString());			//用作上级信息
		mv.addObject("pds",dictService.findById(pd));				//传入上级所有信息
		mv.addObject("DICT_ID", pd.get("PARENT_ID").toString());	//传入上级ID，作为子ID用
		pd.put("DICT_ID",DICT_ID);							//复原本ID
		mv.setViewName("/business/dict/dict_edit");
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
			if(dictService.findByBianma(pd) != null){
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
