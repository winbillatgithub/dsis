package com.as.db.proxy;

import com.as.util.PageData;

public class DBMysql implements DBFactory {

	private String MYSQL_TYPE_NUMBER = "NUMBER";
	private String MYSQL_TYPE_INTEGER = "INTEGER";
	private String MYSQL_TYPE_DATE = "DATE";
	private String MYSQL_TYPE_TIME = "TIME";
	private String MYSQL_TYPE_DATETIME = "DATETIME";
	private String MYSQL_TYPE_BOOL = "BOOL";
	private String MYSQL_TYPE_VAR_STRING = "STRING";
	
	public PageData resetDataType(PageData pd) {
		// TODO Auto-generated method stub
		String dataType = (String) pd.get("DATA_TYPE_NAME");
		
		Integer len = 0;
		Object obj = pd.get("LENG");
		if (obj instanceof String) {
			len = Integer.parseInt((String) obj);
		} else if (obj instanceof Integer) {
			len = (Integer) obj;
		}
		Integer precision = 0;
		obj = pd.get("DATA_PRECISION");
		if (obj instanceof String) {
			precision = Integer.parseInt((String) obj);
		} else if (obj instanceof Integer) {
			precision = (Integer) obj;
		}
		if (this.MYSQL_TYPE_NUMBER.equalsIgnoreCase(dataType)) {
			if (len <= 0) {
				dataType = "INTEGER";
			} else {
				dataType = "DECIMAL(" + len + "," + precision + ")";
			}
		} else if (this.MYSQL_TYPE_VAR_STRING.equalsIgnoreCase(dataType)) {
			if (len <= 1) {
				dataType = "CHAR(1)";
			} else {
				dataType = "VARCHAR(" + len + ")";
			}
		} else if (this.MYSQL_TYPE_DATETIME.equalsIgnoreCase(dataType)) {
			dataType = "DATETIME";
		} else if (this.MYSQL_TYPE_DATE.equalsIgnoreCase(dataType)) {
			dataType = "DATE";
		} else if (this.MYSQL_TYPE_TIME.equalsIgnoreCase(dataType)) {
			dataType = "TIME";
		} else if (this.MYSQL_TYPE_BOOL.equalsIgnoreCase(dataType)) {
			dataType = "VARCHAR(32)";
		}

		// Encrypt
		obj = pd.get("RSV_STR2");
		if ("1".equals(obj)) {
			dataType = "BLOB";
		}

		pd.put("COLUMN_TYPE", dataType);
		return pd;
	}

}
