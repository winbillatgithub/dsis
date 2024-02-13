package com.as.controller.base;


import com.as.entity.Page;
import com.as.util.Logger;
import com.as.util.PageData;
import com.as.util.Tools;
import com.as.util.UuidUtil;
import net.sf.json.JSONArray;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author antispy
 * 修改时间：2015、12、11
 */
public class BaseController  implements Serializable{

	public static Logger logger = Logger.getLogger(BaseController.class);

	private static final long serialVersionUID = 6357869213649815390L;
	
	public static final int MAX_SHOW_COUNT = 999;
	/** new PageData对象
	 * @return
	 */
	public PageData getPageData(){
		PageData pd = new PageData(this.getRequest());
		// 默认分页参数 0：15
		if (Tools.isEmpty(pd.getString("pageNum"))) {
			pd.put("pageNum", "0");
		}
		if (Tools.isEmpty(pd.getString("pageSize"))) {
			pd.put("pageSize", "25");
		}
		return pd;
	}
	
	/**得到ModelAndView
	 * @return
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}

	/**得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
	/**得到分页列表的信息
	 * @return
	 */
	public Page getPage(){
		return new Page();
	}

	/**
	 * 获取当前日期/时间
	 * @param dateFormat
	 * @return
	 */
	public String getCurrentDateTime(String dateFormat) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		String date =  simpleDateFormat.format(calendar.getTime());
		return date;
	}
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
    public static void log(Logger logger, String interfaceName){
        logger.info("");
        logger.info("===");
        logger.info(interfaceName);
    }
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
	/**
	 * 返回json的数据
	 * @param success
	 * @param msg
	 * @return
	 */
	public Map<String, Object> jsonResult(boolean success, Object msg) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("success", success);
		data.put("msg", msg);
		return data;
	}

    /**
     * 是否为系统默认列
     * @param column
     * @return
     */
    public boolean isSysDefault(String column) {
        if ("SORT_NO".equals(column) ||
                "PARENT_ID".equals(column) ||
                "LEVEL".equals(column) ||
                "STATUS".equals(column) ||
                "CREATE_ID".equals(column) ||
                "CREATE_TIME".equals(column) ||
                "UPDATE_TIME".equals(column) ||
                "CORP_ID".equals(column)
//                "FILE_ATTACH".equals(column) ||
//                "VIDEO_ATTACH".equals(column)
                ) {
            return true;
        }
        return false;
    }
    public String getSysDefaultIndexesId(String column) {
        if ("SORT_NO".equals(column)) {
            return "SYS_DEFAUTL_1";
        } else if ("PARENT_ID".equals(column)) {
            return "SYS_DEFAUTL_2";
        } else if ("LEVEL".equals(column)) {
            return "SYS_DEFAUTL_3";
        } else if ("STATUS".equals(column)) {
            return "SYS_DEFAUTL_4";
        } else if ("CREATE_ID".equals(column)) {
            return "SYS_DEFAUTL_5";
        } else if ("CREATE_TIME".equals(column)) {
            return "SYS_DEFAUTL_6";
        } else if ("UPDATE_TIME".equals(column)) {
            return "SYS_DEFAUTL_7";
        } else if ("CORP_ID".equals(column)) {
            return "SYS_DEFAUTL_8";
        }
        return "";
    }
    public String getCorpIdFromEnv() {
        Properties props = new Properties(System.getProperties());
        return props.getProperty("ENV_CORP_ID", "f2c0364ed75c4ac7b2806942553233ff"); // 小牛卡夫
    }


    /**
     * 根据表信息和表字段信息获取json格式元数据
     * @param tableList
     * @param columnsList
     * @return
     */
    public JSONArray getMetaData(List<PageData> tableList, List<PageData>	columnsList) {
        List<Map<String,Object>> initList = new ArrayList<Map<String,Object>>();
        for (int i=0;i<tableList.size();i++) {
            PageData tbPd = tableList.get(i);
            Map<String,Object> oneMap = new HashMap<String,Object>();
            Map<String,Object> twoMap = new HashMap<String,Object>();
            oneMap.put(tbPd.getString("TABLES_ID"), twoMap);
            twoMap.put("name", tbPd.get("TABLE_NAME"));
            twoMap.put("code", tbPd.get("TABLES_ID"));

            //Map<String,Object> threeMap = new HashMap<String,Object>();
            List<Map<String,Object>> threeList = new ArrayList<Map<String,Object>>();
            //twoMap.put("properties", threeMap);
            twoMap.put("properties", threeList);
            for (int j=0;j<columnsList.size();j++) {
                // 匹配该字段表是否对应主表
                PageData colPd = columnsList.get(j);
                if (!colPd.getString("TABLES_ID").equals(tbPd.getString("TABLES_ID"))) {
                    continue;
                }
                Map<String,Object> fourMap = new HashMap<String,Object>();

                fourMap.put("colCode", colPd.getString("COLUMNS_ID"));
                fourMap.put("colIndex", colPd.getString("INDEXES_ID"));
                fourMap.put("des", colPd.getString("COLUMN_NAME_CN"));
                fourMap.put("sortno", String.valueOf(colPd.get("SORT_NO")));
                threeList.add(fourMap);
            }
            initList.add(oneMap);

        }
        JSONArray jsonArray = JSONArray.fromObject(initList);
        return jsonArray;
    }
}
