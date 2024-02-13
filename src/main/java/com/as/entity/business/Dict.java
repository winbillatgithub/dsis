package com.as.entity.business;

import java.util.List;

/**
 * 
* 类名称：用户自定义编码
* 类描述： 
* @author antispy
* 作者单位： 
* 联系方式：
* 修改时间：2015年12月16日
* @version 2.0
 */
public class Dict {

	private String NAME;			//名称
	private String NAME_EN;			//英文名称
	private String BIANMA;			//编码
	private String ORDER_BY;		//排序	
	private String PARENT_ID;		//上级ID
	private String BZ;				//备注
	private String TBSNAME;			//关联表
	private String DICT_ID;	//主键
	private String CORP_ID;			//组织ID
	private String target;
	private Dict dict;
	private List<Dict> subDict;
	private boolean hasDict = false;
	private String treeurl;
	
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
	public String getBIANMA() {
		return BIANMA;
	}
	public void setBIANMA(String bIANMA) {
		BIANMA = bIANMA;
	}
	public String getORDER_BY() {
		return ORDER_BY;
	}
	public void setORDER_BY(String oRDER_BY) {
		ORDER_BY = oRDER_BY;
	}
	public String getPARENT_ID() {
		return PARENT_ID;
	}
	public void setPARENT_ID(String pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}
	public String getBZ() {
		return BZ;
	}
	public void setBZ(String bZ) {
		BZ = bZ;
	}
	public String getTBSNAME() {
		return TBSNAME;
	}
	public void setTBSNAME(String tBSNAME) {
		TBSNAME = tBSNAME;
	}
	public String getDICT_ID() {
		return DICT_ID;
	}
	public void SetDICT_ID(String dictId) {
		DICT_ID = dictId;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Dict getDict() {
		return dict;
	}
	public void setDict(Dict dict) {
		this.dict = dict;
	}
	public List<Dict> getSubDict() {
		return subDict;
	}
	public void setSubDict(List<Dict> subDict) {
		this.subDict = subDict;
	}
	public boolean isHasDict() {
		return hasDict;
	}
	public void setHasDict(boolean hasDict) {
		this.hasDict = hasDict;
	}
	public String getTreeurl() {
		return treeurl;
	}
	public void setTreeurl(String treeurl) {
		this.treeurl = treeurl;
	}
	public String getCORP_ID() {
		return CORP_ID;
	}
	public void setCORP_ID(String cORP_ID) {
		CORP_ID = cORP_ID;
	}
	
}
