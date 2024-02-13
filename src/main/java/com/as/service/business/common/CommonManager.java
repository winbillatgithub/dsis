package com.as.service.business.common;

import java.util.List;
import java.util.Map;

import com.as.entity.Page;
import com.as.entity.business.Column;
import com.as.util.PageData;

/** 
 * 说明： 通用模板接口，一维、二维、多媒体等模板的共通接口
 * 创建人：antispy
 * 创建时间：2016-09-04
 * @version
 */
public interface CommonManager{

	// inner join sql template
	public static String INNER_JOIN_TABLE = " INNER JOIN #SCHEMA#.#JOIN_TABLE# #JOIN_TABLE# ON #JOIN_TABLE#.#JOIN_COLUMN# = a.#MASTER_COLUMN# ";
	public static String INNER_JOIN_WHERE = " AND #JOIN_TABLE#.#JOIN_COLUMN# = '#VALUE#' ";
	public static String INNER_JOIN_WHERE_LIKE = " AND #JOIN_TABLE#.#JOIN_COLUMN# LIKE '#VALUE#%' ";
	public static String INNER_JOIN_WHERE_BETWEEN = " AND #JOIN_TABLE#.#JOIN_COLUMN# between #VALUE# ";
	// where a.COURSE_NAME like '{value}%'
	public static String NO_INNER_JOIN_WHERE = " AND a.#JOIN_COLUMN# = '#VALUE#' ";
	public static String NO_INNER_JOIN_WHERE_LIKE = " AND a.#JOIN_COLUMN# like '#VALUE#%' ";
	public static String NO_INNER_JOIN_WHERE_BETWEEN = " AND a.#JOIN_COLUMN# between #VALUE# ";

	// ===================================================encrypt columns in where clause==================================================
	// where CAST(AES_DECRYPT(a.PASSWORD,'xiaoniukafu') AS CHAR) LIKE 'world%'
	public static String ENCRYPT_INNER_JOIN_WHERE = " AND CAST(AES_DECRYPT(#JOIN_TABLE#.#JOIN_COLUMN#,'#CODE#') AS CHAR) = '#VALUE#' ";
	public static String ENCRYPT_INNER_JOIN_WHERE_LIKE = " AND CAST(AES_DECRYPT(#JOIN_TABLE#.#JOIN_COLUMN#,'#CODE#') AS CHAR) LIKE '#VALUE#%' ";
	public static String ENCRYPT_INNER_JOIN_WHERE_BETWEEN = " AND CAST(AES_DECRYPT(#JOIN_TABLE#.#JOIN_COLUMN#,'#CODE#') AS CHAR) between #VALUE# ";
	// where a.COURSE_NAME like '{value}%'
	public static String ENCRYPT_NO_INNER_JOIN_WHERE = " AND CAST(AES_DECRYPT(a.#JOIN_COLUMN#,'#CODE#') AS CHAR) = '#VALUE#' ";
	public static String ENCRYPT_NO_INNER_JOIN_WHERE_LIKE = " AND CAST(AES_DECRYPT(a.#JOIN_COLUMN#,'#CODE#') AS CHAR) like '#VALUE#%' ";
	public static String ENCRYPT_NO_INNER_JOIN_WHERE_BETWEEN = " AND CAST(AES_DECRYPT(a.#JOIN_COLUMN#,'#CODE#') AS CHAR) between #VALUE# ";
	// ===================================================encrypt columns in where clause==================================================

	// ===================================================encrypt columns in select clause=================================================
	public static String ENCRYPT_VALUE = "AES_ENCRYPT('#VALUE#', '#CODE#')";
	// CAST(AES_DECRYPT(password,'xiaoniukafu') AS CHAR)
	public static String DECRYPT_VALUE = "CAST(AES_DECRYPT(#COLUMN#,'#CODE#') AS CHAR) AS #ALIAS#";
	// ===================================================encrypt columns in select clause=================================================

	/**
	 * 判断当前列是否为内联字段
	 * @param column
	 * @param colDef
	 * @return
	 */
	public boolean isInnerJoinColumn(String column, Map<String, Object> colDef);
	/**获取通用业务表的数据
	 * @param pd
	 * @param page
	 * @throws Exception
	 */
    public long getColumnIDMapCount(PageData pd, Page page)throws Exception;
    public List<PageData> getColumnIDMap(PageData pd, Page page)throws Exception;

	/**
	 * 对于单独list中，获取内联字段对应的值
	 * @param column
	 * @param colDef
	 * @return
	 */
	public Object getInnerJoinColumnData(String column, Map<String, Object> colDef, String sourceColumnValue) throws Exception;
	/**
	 * List-List模式中Slave需要从Source查询数据
	 * @param pdpdParams
	 * @param listListRuleDetail
	 * @param colDef
	 * @return
	 */
	public List<PageData> getSourceTableData(PageData pdpdParams, List<PageData> listListRuleDetail, Map<String,Object> colDef);

	/**
	 * 更新STATUS为可用
	 * @param tablesId
	 * @param corpId
	 * @param dataDate
	 * @throws Exception
	 */
	public void updateStatus(String tablesId, String corpId, String dataDate)throws Exception;

	/**
	 * 获取公司/组织已使用的Mysql数据库空间
	 * 获取公司/组织已使用的附件/硬盘空间
	 * getDatabaseUsedSize
	 * @param corpId
	 * @return
	 */
	public PageData getDatabaseUsedSize(String corpId);
	public PageData getFastdfsUsedSize(String corpId);

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(String tablesId, PageData pd)throws Exception;
	public void insert(String tablesId, PageData pd)throws Exception;

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	public void deleteBusinessData(String tablesId, PageData pd)throws Exception;

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(PageData pd)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(String tablesId, String dataDate, String departmentId, List<Column> columns)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

    /**
     * 获取内联字段数据
     * @param outerSourceDataList
     * @param colDef
     * @return
     */
    public List<PageData> mergeInnerSourceData(List<PageData> outerSourceDataList, Map<String,Object> colDef);

    public List<PageData> execute(String sql) throws Exception;
	/** 日志专属查询，修改*/
	public List<PageData> log(String sql) throws Exception;
}

