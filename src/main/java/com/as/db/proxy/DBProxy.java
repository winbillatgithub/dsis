package com.as.db.proxy;

import com.as.util.PageData;

public class DBProxy implements DBFactory {

	private DBFactory factory;

	public DBProxy() {
		// 先支持Mysql
		factory = new DBMysql();
	}

	public PageData resetDataType(PageData pd) {
		// TODO Auto-generated method stub
		return factory.resetDataType(pd);
	}

}
