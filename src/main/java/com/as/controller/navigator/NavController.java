package com.as.controller.navigator;

import com.as.controller.base.BaseController;
import com.as.entity.Page;
import com.as.service.business.tables.TablesManager;
import com.as.util.Encrypt;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import com.as.util.StringUtil;
import com.as.util.TwoDimensionCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：List模板Controller
 * 创建人：wibill
 * 创建时间：2016-09-04
 */
@Controller
@RequestMapping(value = "/nav")
public class NavController extends BaseController {

    private static String CARD_BASIC_PUBLIC_URI = "/pub/card.do?formEditFlag=preview&TABLES_ID=";
    private static String LIST_BASIC_PUBLIC_URI = "/pub/list.do?TABLES_ID=";
    private static String CORP_ID_URI = "&CORP_ID=";
    private static String CORP_BIANMA = "&code=";

    // 模板
    @Resource(name = "tablesService")
    public TablesManager tablesService;

    /**
     * 导航首页
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/my_forms")
    public ModelAndView listForm(Page page, boolean loadData) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "/nav/my_forms");
        ModelAndView mv = this.getModelAndView();

        PageData pd = this.getPageData();
        pd.put("CORP_ID", Jurisdiction.getCorpId());
        List<PageData> tableList = tablesService.listAll(pd);
        Map<String, Object> mapForm = new HashMap<String, Object>();
        mapForm.put("total", tableList.size());
        mapForm.put("rows", tableList);
        mv.addObject("mapForm", mapForm);
        mv.addObject("domain", pd.getString("domain"));
        mv.setViewName("/navigator/my_forms");

        return mv;
    }

    /**
     * 导航首页
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/create_form_steps")
    public ModelAndView createFormSteps(Page page, boolean loadData) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "导航首页");
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String tablesId = pd.getString("TABLES_ID");
        String formType = pd.getString("formType");
        if (StringUtil.strIsEmpty(formType)) {
            formType = "card";               // 默认卡片类型
        }
//        pd.put("CORP_ID", Jurisdiction.getCorpId());
//        List<PageData> tableList = tablesService.listAll(pd);
//        Map<String, Object> mapForm = new HashMap<String, Object>();
//        mapForm.put("total", tableList.size());
//        mapForm.put("rows", tableList);
        mv.addObject("currentStep", "0");   // 配置界面元素
        mv.addObject("formEditFlag", "add"); // 默认增加表单
        mv.addObject("formType", formType);   // 表单展示类型 card/list
        mv.addObject("TABLES_ID", tablesId);
        mv.addObject("domain", pd.getString("domain"));
        mv.setViewName("/navigator/create_form_steps");

        return mv;
    }

    @RequestMapping(value = "/code")
    public ModelAndView code(Page page, boolean loadData) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "导航首页");
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String tablesId = pd.getString("TABLES_ID");
        String corpId = pd.getString("CORP_ID");
        String undo = pd.getString("undo");
        String corpBianma = Jurisdiction.getTopCorpBianma();

        StringBuffer url = getRequest().getRequestURL();
        logger.debug("getRequestURL() returns:" + url);
        String currentPath = url.substring(0, url.indexOf("/nav"));
        if (currentPath.endsWith("/dsis")) {
            currentPath = url.substring(0, url.indexOf("/dsis"));
        }
        String urlContent = currentPath + CARD_BASIC_PUBLIC_URI + tablesId;
        urlContent += CORP_ID_URI + corpId;
        urlContent += CORP_BIANMA + corpBianma;
        urlContent += "&token=";
        urlContent += Encrypt.EncryptUri(CARD_BASIC_PUBLIC_URI + tablesId + CORP_ID_URI + corpId + CORP_BIANMA + corpBianma);

        BufferedImage bufImg = TwoDimensionCode.qRCodeCommon(urlContent, "png", 100);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        ImageIO.write(bufImg, "png", baos);//写入流中
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        mv.addObject("publicCard", png_base64);

        urlContent = currentPath + LIST_BASIC_PUBLIC_URI + tablesId;
        urlContent += CORP_ID_URI + corpId;
        urlContent += CORP_BIANMA + corpBianma;
        urlContent += "&token=";
        urlContent += Encrypt.EncryptUri(LIST_BASIC_PUBLIC_URI + tablesId + CORP_ID_URI + corpId + CORP_BIANMA + corpBianma);
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();//io流
        BufferedImage bufImg1 = TwoDimensionCode.qRCodeCommon(urlContent, "png", 100);
        ImageIO.write(bufImg1, "png", baos1);
        bytes = baos1.toByteArray();//转换成字节
        encoder = new BASE64Encoder();
        png_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        mv.addObject("publicList", png_base64);
        mv.addObject("pd", pd);

        mv.setViewName("/navigator/code");
        /*
         * 更新TABLE表记录状态 0:未公开 1:已公开
         */
        if ("0".equals(undo)) {
            PageData pdTable = new PageData();
            pdTable.put("TABLES_ID", tablesId);
            pdTable.put("STATUS", "1");
            tablesService.updateStatus(pdTable);
        }

        return mv;
    }

    @RequestMapping(value = "/make_private")
    @ResponseBody
    public Map unlink() {
        logBefore(logger, Jurisdiction.getUsername() + "导航首页");
        ModelAndView mv = this.getModelAndView();
        Map<String, Object> ret = new HashMap<String, Object>();
        PageData pd = this.getPageData();
        String tablesId = pd.getString("TABLES_ID");
        String corpId = pd.getString("CORP_ID");
        /*
         * 更新TABLE表记录状态 0:未公开 1:已公开
         */
        try {
            PageData pdTable = new PageData();
            pdTable.put("TABLES_ID", tablesId);
            pdTable.put("STATUS", "0");
            tablesService.updateStatus(pdTable);
        } catch (Exception e) {
            ret.put("msg", e.getLocalizedMessage());
            ret.put("success", false);
            return ret;
        }

        ret.put("success", true);
        return ret;
    }

    /**
     * 选择表单类型
     *
     * @throws Exception
     */
    @RequestMapping(value = "/form_type")
    public ModelAndView selectFormType() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "表单类型");
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        String tablesId = pd.getString("TABLES_ID");
        String formType = pd.getString("formType");
        if (StringUtil.strIsEmpty(formType)) {
            formType = "card";               // 默认卡片类型
        }
        // 配置界面元素
        mv.addObject("currentStep", "2");
        // 表单展示类型 card/list
        mv.addObject("formType", formType);
        mv.addObject("TABLES_ID", tablesId);
        mv.setViewName("/navigator/form_type");

        return mv;
    }
}
