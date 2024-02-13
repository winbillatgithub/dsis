package com.as.entity.business;

import java.util.List;

/**
 * 
* 类名称：数据字典
* 类描述： 
* @author antispy
* 作者单位： 
* 联系方式：
* 修改时间：2015年12月16日
* @version 2.0
 */
public class Column {

	private String NAME;			//名称
	private String NAME_EN;			//英文名称
	private Object VALUE;
	private String ENCRYPT;

	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getNAME_EN() {
		return NAME_EN;
	}
	public void setNAME_EN(String nAME_EN) {
		NAME_EN = nAME_EN;
	}
	public Object getVALUE() {
		return VALUE;
	}
	public void setVALUE(Object vALUE) {
		VALUE = vALUE;
	}
	public String getENCRYPT() {
		return ENCRYPT;
	}
	public void setENCRYPT(String eNCRYPT) {
		ENCRYPT = eNCRYPT;
	}
}
