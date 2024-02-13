package com.as.util;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 字符串相关方法
 *
 */
public class StringUtil {

	private static Random random = new Random();
	
	/**
	 * 生成指定范围内的随机数
	 * @param size 生成随机数的个数
	 * @param start 开始范围 
	 * @param end  结束范围
	 * @return
	 */
	public static String getRandomNum(int size, int min, int max) {
		if (max - min >= size) {
			StringBuffer str = new StringBuffer();
			for(int i=0; i<size; i++) {
				str.append(random.nextInt(max)%(max-min+1) + min);
			}
	        return str.toString();
		}
		return null;
	}
	
	/**
	 * 获取某个范围内的随机数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomNum(int min, int max) {
		if(max > min) {
			return random.nextInt(max)%(max-min+1) + min;
		}else{
			return 0;
		}
	}
	
	/**
	 * 判断字符串为空
	 * @param str
	 * @return
	 */
	public static boolean strIsEmpty(String str) {
		return (null == str || "".equals(str.trim())); 
	}

	/**
	 * 判断字符串非空
	 * @param str
	 * @return
	 */
	public static boolean strIsNotEmpty(String str) {
		return (null != str && !"".equals(str));
	}
	
	/**
	 * 判断List集合为空
	 * @param list
	 * @return
	 */
	public static boolean listIsEmpty(List<?> list) {
		return (null == list || list.size() == 0);
	}
	
	/**
	 * 判断List集合非空
	 * @param list
	 * @return
	 */
	public static boolean listIsNotEmpty(List<?> list) {
		return (null != list && list.size() > 0);
	}
	
	/**
	 * 判断Set集合为空
	 * @param set
	 * @return
	 */
	public static boolean setIsEmpty(Set<?> set) {
		return (null == set || set.size() == 0);
	}
	
	/**
	 * 判断Set集合非空
	 * @param set
	 * @return
	 */
	public static boolean setIsNotEmpty(Set<?> set) {
		return (null != set && set.size() > 0);
	}
	
	/**
	 * 判断Map集合为空
	 * @param map
	 * @return
	 */
	public static boolean mapIsEmpty(Map<?, ?> map) {
		return (null == map || map.size() == 0);
	}
	
	/**
	 * 判断Map集合非空
	 * @param map
	 * @return
	 */
	public static boolean mapIsNotEmpty(Map<?, ?> map) {
		return (null != map && map.size() > 0);
	}
	
	/**
	 * 字符串转Integer
	 * @param args
	 */
	public static Integer stringToInteger(String string) {
		if (strIsNotEmpty(string)) {
			return Integer.parseInt(string);
		}
		return null;
	}
	
	/**
	 * 随机生成带有 大小写字母 和数字的 字符串
	 * @Title: getRandomChar
	 * @Description: TODO
	 * @param size 长度
	 * @return String
	 */
	 public static String getRandomChar(int size) { 
		 String randChar = ""; 
		 while (randChar.length() < size) {
			 int index = (int) Math.round(Math.random() * 2); 
		     switch (index) { 
		     case 0://大写字符 
				  randChar += String.valueOf((char)Math.round(Math.random() * 25 + 65)); 
				  break; 
		     default://数字 
				  randChar += String.valueOf(Math.round(Math.random() * 9)); 
				  break; 
		     } 
		}
	     return randChar; 
	 } 
	 
	 /**
	  * 在字符串某个位置插入新字符串
	  * @param original  原字符串
	  * @param target    新字符串
	  * @param index     插入位置
	  * @return
	 */
	 public static Object insertStr(Object original, Object target, int index) {
		 if(null != original && null != target && index >= 0) {
			 return String.valueOf(original).substring(0,index)+target+String.valueOf(original).substring(index,String.valueOf(original).length());
		 }else{
			 return null;
		 }
	 }
	 
	 /**
	  * 获取客户端ip
	  * @param request
	  * @return
	 */
	 public static String getClientIp(HttpServletRequest request) {
/*		try {
			LoginUser loginUser = ContextUtil.getLoginUser();
			if(null != loginUser && StringUtil.strIsNotEmpty(loginUser.getClientIp())) {
				return loginUser.getClientIp();
			}else{				
				String ip = request.getHeader("X-Real-IP");
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("X-Forwarded-For");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("Proxy-Client-IP");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("WL-Proxy-Client-IP");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getRemoteAddr();
				}
				return isBoolIp(ip) ? ip : null;
			}
		} catch (Exception e) {
		}*/
		return null;
	 }

	/**
	 * 获取服务器ip
	 * @param request
	 * @return
	 */
	 public static String getServerIp(HttpServletRequest request) {
		 try {
			 return getDomainName(request);
		 } catch (Exception e) {
		 }
		 return null;
	 }
	 
	 /**
	  * 获取域名
	 * @param request
	 * @return
	 */
	public static String getDomainName(HttpServletRequest request) {
		try {
			StringBuffer url = request.getRequestURL();
			Pattern p =Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
			Matcher matcher = p.matcher(url);
			matcher.find();
			return matcher.group();
		} catch (Exception e) {
		}
		return request.getServerName();
	 }
	
	/**
	 * 判断是否为合法IP
	 * @param ipAddress
	 * @return
	 */
	public static boolean isBoolIp(String ipAddress) {
		try {			
			Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
			Matcher matcher = pattern.matcher(ipAddress);
			return matcher.matches();
		} catch (Exception e) {
		}
		return false;
	}
	 
	
	 public static String getContactCode() {
		return Long.toString(new Date().getTime(), 36).toUpperCase() + getRandomChar(4);
	 }
	/**
	 * 将以逗号分隔的字符串转换成字符串数组
	 * @param valStr
	 * @return String[]
	 */
	public static String[] StrList(String valStr){
	    int i = 0;
	    String TempStr = valStr;
	    String[] returnStr = new String[valStr.length() + 1 - TempStr.replace(",", "").length()];
	    valStr = valStr + ",";
	    while (valStr.indexOf(',') > 0)
	    {
	        returnStr[i] = valStr.substring(0, valStr.indexOf(','));
	        valStr = valStr.substring(valStr.indexOf(',')+1 , valStr.length());
	        
	        i++;
	    }
	    return returnStr;
	}
	
	/**获取字符串编码
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {      
	       String encode = "GB2312";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s = encode;      
	              return s;      
	           }      
	       } catch (Exception exception) {      
	       }      
	       encode = "ISO-8859-1";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s1 = encode;      
	              return s1;      
	           }      
	       } catch (Exception exception1) {      
	       }      
	       encode = "UTF-8";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s2 = encode;      
	              return s2;      
	           }      
	       } catch (Exception exception2) {      
	       }      
	       encode = "GBK";      
	      try {      
	          if (str.equals(new String(str.getBytes(encode), encode))) {      
	               String s3 = encode;      
	              return s3;      
	           }      
	       } catch (Exception exception3) {      
	       }      
	      return "";      
	   } 
	public static boolean isEmpty(String str) {
		return (str == null) || (str.trim().equals(""));
	}
}
