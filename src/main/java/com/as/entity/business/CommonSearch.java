package com.as.entity.business;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CommonSearch {
	private String corpId;
	private String tableName;
	private List<String> columns;
	private String whereColumnName;
	private String whereColumnValue;
	private String sourceColumnsNameEn;		// 仅仅是list-list规则获取source数据时使用
	private boolean matchText;				// 仅仅是list-list规则中匹配ID还是匹配TEXT标志

	public String getWhereColumnName() {
		return whereColumnName;
	}
	public void setWhereColumnName(String whereColumnName) {
		this.whereColumnName = whereColumnName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getWhereColumnValue() {
		return whereColumnValue;
	}
	public void setWhereColumnValue(String whereColumnValue) {
		this.whereColumnValue = whereColumnValue;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	public String getSourceColumnsNameEn() {
		return sourceColumnsNameEn;
	}
	public void setSourceColumnsNameEn(String sourceColumnsNameEn) {
		this.sourceColumnsNameEn = sourceColumnsNameEn;
	}
	public boolean isMatchText() {
		return matchText;
	}
	public void setMatchText(boolean matchText) {
		this.matchText = matchText;
	}
}
