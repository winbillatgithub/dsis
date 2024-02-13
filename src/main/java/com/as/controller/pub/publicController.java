package com.as.controller.pub;

import com.as.controller.template.TemplateController;
import com.as.controller.template.list.ListController;
import com.as.entity.Page;
import com.as.entity.business.Column;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import com.as.util.StringUtil;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 说明：List模板Controller
 * 创建人：wibill
 * 创建时间：2016-09-04
 */
@Controller
@RequestMapping(value = "/pub")
public class publicController extends TemplateController {
    //菜单地址(权限用)
    String menuUrl = "card/list.do";

    /**
     * 跳转到
     * /card/card.do?formEditFlag=preview&TABLES_ID=yuyaoyanche_67f07323fb8c4cd4b2fb6eecc68a6b22
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/card")
    public ModelAndView card(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "公开card访问");
        //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        }
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();

        // 跳转方式会出现安全漏洞, /card /list会被匿名访问,因为shiro关闭了
//        String tablesId = pd.getString("TABLES_ID");
//        String formEditFlag = pd.getString("formEditFlag");
//        String url = "redirect:/card/card.do?TABLES_ID=";
//        url += tablesId;
//        url += "&formEditFlag=";
//        url += formEditFlag;
//        url += "&dataEditFlag=add";
//
//        mv.setViewName(url);

        pd.put("CORP_ID", Jurisdiction.getCorpId());
        //关键词检索条件
        String keywords = pd.getString("keywords");
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        pd.put("CHECKED", "1");
        page.setPd(pd);
        // 提交人公司ID
        String corpId = pd.getString("CORP_ID");
        String tablesId = pd.getString("TABLES_ID");
        if (StringUtil.strIsNotEmpty(tablesId)) {
            PageData tables = tablesService.findById(pd);
            // 当前表单没有公开
            if (!"1".equals(tables.getString("STATUS"))) {
                // 无访问权限
                mv.setViewName("/system/index/login");
                return mv;
            }
            String jsonData = tables.getString("JSON_DATA");
            pd.put("JSON_DATA", jsonData);
            pd.put("TABLE_NAME", tables.getString("TABLE_NAME"));
            pd.put("TABLE_NAME_EN", tables.getString("TABLE_NAME_EN"));
            pd.put("REMARK", tables.getString("REMARK"));
        } else {
            // 无访问权限
            mv.setViewName("/system/index/login");
            return mv;
        }
        mv.addObject("formEditFlag", pd.getString("formEditFlag"));
        mv.addObject("dataEditFlag", "add");
        mv.addObject("DATA_ID", pd.getString("DATA_ID"));
        String userData = pd.getString("USER_DATA");
        mv.addObject("USER_DATA", (userData == null) ? "{}" : userData);
        // Get columns definition
        Map<String, Object> map = getAllColumnsList(page, Jurisdiction.getTopCorpId());

        List<Column> columnList = (List<Column>) map.get("columnList");
        List<PageData> indexesList = (List<PageData>) map.get("indexesList");
        List<PageData> dimList = (List<PageData>) map.get("dimList");

        JSONArray jsonarray = JSONArray.fromObject(indexesList);
        mv.addObject("indexesList", jsonarray);
        jsonarray = JSONArray.fromObject(dimList);
        mv.addObject("dimList", jsonarray);

        mv.addObject("pd", pd);
        mv.setViewName("/business/card/card_single");
        return mv;
    }

    /**
     * 列表
     * 跳转到/list/list.do?TABLES_ID=jiashizheng_eb939468cade4ec6bb4370bb01712836
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表Logs");
        if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();

        mv = doList(page, mv, ListController.menuUrl, false);

        return mv;
    }

}
