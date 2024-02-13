package com.as.entity.system;

import com.as.entity.Page;

/**
 * 
* 类名称：用户
* 类描述： 
* @author antispy
* 作者单位： 
* 联系方式：
* 创建时间：2014年6月28日
* @version 1.0
 */
public class User {
	private String USER_ID;		//用户id
	private String USERNAME;	//用户名
	private String PASSWORD; 	//密码
	private String NAME;		//姓名
	private String RIGHTS;		//权限
	private String ROLE_ID;		//角色id
	private String LAST_LOGIN;	//最后登录时间
	private String IP;			//用户登录ip地址
	private String STATUS;		//状态
	private Role role;			//角色对象
	private Page page;			//分页对象
	private String SKIN;		//皮肤
	private String CORP_ID;		//公司ID
	private String CORP_NAME;	//公司名称
	private String TOP_CORP_ID;	//总公司ID
	private String TOP_CORP_BIANMA; //总公司编码
	private String ENCRYPT_CODE;//加密密钥

	public String getSKIN() {
		return SKIN;
	}
	public void setSKIN(String sKIN) {
		SKIN = sKIN;
	}
	
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getRIGHTS() {
		return RIGHTS;
	}
	public void setRIGHTS(String rIGHTS) {
		RIGHTS = rIGHTS;
	}
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	public String getLAST_LOGIN() {
		return LAST_LOGIN;
	}
	public void setLAST_LOGIN(String lAST_LOGIN) {
		LAST_LOGIN = lAST_LOGIN;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Page getPage() {
		if(page==null)
			page = new Page();
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getCORP_ID() {
		return CORP_ID;
	}
	public void setCORP_ID(String cORP_ID) {
		CORP_ID = cORP_ID;
	}
	public String getCORP_NAME() {
		return CORP_NAME;
	}
	public void setCORP_NAME(String cORP_NAME) {
		CORP_NAME = cORP_NAME;
	}
	public String getTOP_CORP_ID() {
		return TOP_CORP_ID;
	}
	public void setTOP_CORP_ID(String tOP_CORP_ID) {
		TOP_CORP_ID = tOP_CORP_ID;
	}
	public String getTOP_CORP_BIANMA() {
		return TOP_CORP_BIANMA;
	}
	public void setTOP_CORP_BIANMA(String tOP_CORP_BIANMA) {
		TOP_CORP_BIANMA = tOP_CORP_BIANMA;
	}
	public String getENCRYPT_CODE() {
		return ENCRYPT_CODE;
	}
	public void setENCRYPT_CODE(String eNCRYPT_CODE) {
		ENCRYPT_CODE = eNCRYPT_CODE;
	}
}
