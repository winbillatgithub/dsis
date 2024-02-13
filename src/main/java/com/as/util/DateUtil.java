package com.as.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** 
 * 说明：日期处理
 * 创建人：antispy
 * 修改时间：2015年11月24日
 * @version
 */
public class DateUtil {

	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**缺省日期格式*/
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/**缺省时间格式*/
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

	/**缺省月格式*/
	public static final String DEFAULT_MONTH = "MONTH";

	/**缺省年格式*/
	public static final String DEFAULT_YEAR = "YEAR";

	/**缺省日格式*/
	public static final String DEFAULT_DATE = "DAY";

	/**缺省小时格式*/  
	public static final String DEFAULT_HOUR = "HOUR";  
	  
	/**缺省分钟格式*/  
	public static final String DEFAULT_MINUTE = "MINUTE";  
	  
	/**缺省秒格式*/  
	public static final String DEFAULT_SECOND = "SECOND";  
	  
	/**缺省长日期格式*/  
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH-mm";  
	
	/**精确到分钟格式*/  
	public static final String DEFAULT_DATETIME_FORMAT_MINUTE = "yyyy-MM-dd HH:mm";  

	/**缺省长日期格式,精确到秒*/  
	public static final String DEFAULT_DATETIME_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss";  

	/**星期数组*/  
	public static final String[] WEEKS = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};  

	/** 
	 * 取当前日期的字符串表示 
	 * @return 当前日期的字符串 ,如2010-05-28 
	 **/  
	public static String today() {  
		return today(DEFAULT_DATE_FORMAT);  
	}  

	/** 
	 * 根据输入的格式得到当前日期的字符串 
	 * @param strFormat 日期格式 
	 * @return 
	 */  
	public static String today(String strFormat) {  
		return toString(new Date(), strFormat);  
	}  
	  
	 /** 
	  * 将java.util.date型按照指定格式转为字符串 
	  * @param date  源对象 
	  * @param format 想得到的格式字符串 
	  * @return 如：2010-05-28 
	  */  
	 public static String toString(Date date, String format) {  
		 return getSimpleDateFormat(format).format(date);  
	 }  
		/** 
		 * 获取格式化对象 
		 * @param strFormat 格式化的格式 如"yyyy-MM-dd" 
		 * @return 格式化对象 
		 */  
		public static SimpleDateFormat getSimpleDateFormat(String strFormat){  
			if(strFormat != null && !"".equals(strFormat.trim())){  
				return new SimpleDateFormat(strFormat);  
			}else{  
				return new SimpleDateFormat();  
			}  
		}  
		  
	 /** 
	  * 将java.util.date型按照缺省格式转为字符串 
	  * @param date 源对象 
	  * @return 如：2010-05-28 
	  */  
	 public static String toString(Date date) {  
		 return toString(date, DEFAULT_DATE_FORMAT);  
	 }  

	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}
	
	/**
	 * 获取YYYYMMDD格式
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	* @Title: compareDate
	* @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean  
	* @throws
	* @author as
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//long aa=0;
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}
	 
	/**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;
        
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
      
        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);
        
        return dateStr;
    }
    
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        return dateStr;
    }

	/** 
	 * getCurDate 取当前日期 
	 * @return java.util.Date型日期 
	 **/  
	public static Date getCurDate() {  
		return (new Date());  
	}  

	/** 
	 * getCurTimestamp 取当前时间戳 
	 * @return java.sql.Timestamp 
	 **/  
	public static Timestamp getCurTimestamp() {  
		return new Timestamp(new Date().getTime());  
	}  

	/** 
	 * getCurTimestamp 取遵循格式的当前时间 
	 * @param sFormat 遵循格式 
	 * @return java.sql.Timestamp 
	 **/  
	public static Date getCurDate(String format) throws Exception {  
		return getSimpleDateFormat(format).parse(toString(new Date(), format));  
	}

	public static void main(String[] args) {
    	System.out.println(getDays());
    	System.out.println(getAfterDayWeek("3"));
    }

}
