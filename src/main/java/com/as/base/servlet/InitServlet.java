package com.as.base.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import net.sf.json.JSONArray;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerGroup;

import com.as.entity.Constant;

/**
 * 系统初始化数据
 * @author 熊焱
 * @date 2013-1-8 下午04:44:46
 * 
 */
public class InitServlet extends HttpServlet {
	
	protected static Log log = LogFactory.getLog(InitServlet.class);
	
	//配置文件
	private static String PROPERTIES = "context.properties";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 系统数据初始化
	 */
	public void init() {
		try {
			//初始化baseService
			//baseService = (IBaseService) SpringContextHelper.getBean("baseService");
			
			//初始化数据
			initData();
			
			//初始化持续监测上传间隔时间
			initMonitorUploadTime();
			
			//初始化数据放入缓存
			initPermanentCache();
			
			//初始化数据字典
			initDictionary();
			
			//初始化系统配置信息放入ServletContext中
			initSysConfig();
			
			log.info("系统数据初始化成功！");
		} catch (Exception e) {
			log.error("系统数据初始化失败！", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化数据
	 */
	@SuppressWarnings("unchecked")
	private void initData()  {
		Configuration config = null;
		try {
			//配置文件
			config = new PropertiesConfiguration(PROPERTIES);
			
			//初始化FASTDFS
			//连接超时的时限，单位为毫秒  
			ClientGlobal.setG_connect_timeout(Integer.parseInt((String) config.getProperty("connect_timeout")));  
			//网络超时的时限，单位为毫秒  
			ClientGlobal.setG_network_timeout(Integer.parseInt((String) config.getProperty("network_timeout")));  
			//令牌
			ClientGlobal.setG_anti_steal_token(Boolean.parseBoolean((String) config.getProperty("anti_steal_token")));  
			//字符集  
			ClientGlobal.setG_charset((String) config.getProperty("charset"));  
			//秘密密钥
			ClientGlobal.setG_secret_key((String) config.getProperty("secret_key"));  
			//HTTP访问服务的端口号    
			ClientGlobal.setG_tracker_http_port(Integer.parseInt((String) config.getProperty("tracker_http_port")));  
			//Tracker服务器列表  
//			List<String> servers = (List<String>) config.getProperty("tracker_server");
//			InetSocketAddress[] tracker_servers = new InetSocketAddress[servers.size()];  
//			for(int i=0; i<servers.size(); i++) {				
//				tracker_servers[i] = new InetSocketAddress(servers.get(i).split("\\:")[0].trim(), Integer.parseInt(servers.get(i).split("\\:")[1].trim()));  
//			}
			String server = String.valueOf(config.getProperty("tracker_server"));
			InetSocketAddress[] tracker_servers = new InetSocketAddress[1];
			tracker_servers[0] = new InetSocketAddress(server.split("\\:")[0].trim(), Integer.parseInt(server.split("\\:")[1].trim())); 
			ClientGlobal.setG_tracker_group(new TrackerGroup(tracker_servers));
			
			//AES对称加密算法 一半的key
			Constant.AES = (String) config.getProperty("AES");
		} catch(Exception e){
			log.error("初始化配置信息失败！", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化持续监测上传间隔时间
	 */
	private void initMonitorUploadTime() {
//		try {
//			if(null != baseService) {
//				Constant.MONITOR_UPLOAD_TIME = (Integer) baseService.getObjectBySql("select upload_frequency from sys_frontend_configunify", null, Integer.class);
//			}
//		} catch (Exception e) {
//			log.error("初始化持续监测上传间隔时间失败！", e);
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 初始化数据字典写入js文件
	 */
	private void initDictionary() {
//		try {
//			//获取ISysDictionaryCategoryService
//			ISysDictionaryCategoryService sysDictionaryCategoryService = (ISysDictionaryCategoryService) SpringContextHelper.getBean("sysDictionaryCategoryServiceImpl");
//			if(null != sysDictionaryCategoryService) {
//				String filePath = getServletContext().getRealPath("js/dictionary.js");
//				sysDictionaryCategoryService.createDictionary(filePath);
//			}
//			
//			//创建省市县js文件
//			//String baseRegionFilePath = getServletContext().getRealPath("js/baseRegion.js");
//			//createBaseRegion(baseRegionFilePath);
//		} catch (Exception e) {
//			log.error("初始化数据字典失败！", e);
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 初始化数据放入缓存
	 */
	private void initPermanentCache() {
//		try {			
//			CacheMethod cacheMethod = (CacheMethod) SpringContextHelper.getBean("cacheMethod");
//			if(null != cacheMethod) {
//				cacheMethod.initPermanentCache();
//			}
//		} catch (Exception e) {
//			log.error("初始化数据放入缓存失败！", e);
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 初始化系统配置信息放入ServletContext中
	 */
	private void initSysConfig() {
//		String configValue = (String) baseService.getObjectBySql("select config_value from sys_config where config_key = 'person'", null, String.class);
//		if(StringHelper.strIsNotEmpty(configValue)) {			
//			ServletContext application = this.getServletContext();
//			//设置ServletContext属性
//			application.setAttribute("person", configValue);
//		}
	}
	
	/**
	 * 创建省市县js文件
	 * @param filePath
	 */
	public void createBaseRegion(String filePath) {
//		try {
//			if(null != baseService) {
//				List<BaseRegion> baseRegions = baseService.getListByHql("from BaseRegion");
//				List<Province> provinces = new ArrayList<Province>();
//				for(BaseRegion baseRegion : baseRegions) {
//					if(StringHelper.strIsEmpty(baseRegion.getParentCode())) { //省
//						Province province = new Province();
//						province.setRegionCode(baseRegion.getRegionCode());
//						province.setRegionName(baseRegion.getRegionName());
//						provinces.add(province);
//						
//						List<City> citys = new ArrayList<City>();
//						for(BaseRegion baseRegion2 : baseRegions) {
//							if(StringHelper.strIsNotEmpty(baseRegion2.getParentCode()) && baseRegion2.getParentCode().equals(baseRegion.getRegionCode())) { //市
//								City city = new City();
//								city.setRegionCode(baseRegion2.getRegionCode());
//								city.setRegionName(baseRegion2.getRegionName());
//								citys.add(city);
//								
//								List<Division> divisions = new ArrayList<Division>();
//								for(BaseRegion baseRegion3 : baseRegions) {
//									if(StringHelper.strIsNotEmpty(baseRegion3.getParentCode()) && baseRegion3.getParentCode().equals(baseRegion2.getRegionCode())) { //县
//										Division division = new Division();
//										division.setRegionCode(baseRegion3.getRegionCode());
//										division.setRegionName(baseRegion3.getRegionName());
//										divisions.add(division);
//									}
//								}
//								city.setDivisions(divisions);
//							}
//						}
//						province.setCitys(citys);
//					}
//				}
//				
//				JSONArray jsonArr= JSONArray.fromObject(provinces); 
//				
//				//服务器文件地址
//				File file = new File(filePath);
//				if (!file.exists()) { 
//					file.createNewFile(); 
//				} 
//				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));  
//				writer.write("var baseRegion = "+String.valueOf(jsonArr)+";");
//				writer.flush();
//				writer.close();
//			}
//		} catch (Exception e) {
//			log.error("创建省市县js文件失败！", e);
//			e.printStackTrace();
//		}
	}
}
