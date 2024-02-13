package com.as.service.business.common.impl;

import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.entity.business.Column;
import com.as.entity.business.CommonSearch;
import com.as.service.business.common.CommonManager;
import com.as.service.business.multitablerule.MultiTableRuleManager;
import com.as.service.system.department.DepartmentManager;
import com.as.util.Jurisdiction;
import com.as.util.Logger;
import com.as.util.PageData;
import com.as.util.Tools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * 说明： 每日销售完成情况
 * 创建人：antispy
 * 创建时间：2016-09-04
 * @version
 */
@Service("commonService")
public class CommonService implements CommonManager{
	protected Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	// 多表业务规则
	@Resource(name="multitableruleService")
	public MultiTableRuleManager multitableruleService;
	// 公司
	@Resource(name="departmentService")
	private DepartmentManager departmentService;

	/**
	 * 判断是否为加密字段
	 * @param column
	 * @param colDef
	 * @return
	 */
	public static boolean isEncryptColumn(String column, Map<String, Object> colDef) {
		if (colDef == null || colDef.get(column) == null) {
			// Attached file column or no column definition
			return false;
		}
		Map<String, Object> map = (Map<String, Object>) colDef.get(column);
		if (map.get("encrypt") == null) {
			return false;
		}
		String encrypt = (String) map.get("encrypt");
		if ("1".equals(encrypt)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为内联字段
	 * @param column
	 * @param colDef
	 * {
 	 *    NAME={
 	 *        "encrypt": "0"
	 *     },
	 *     PASSWORD={
	 *         "encrypt": "1",
	 *         "join_condition": "0",
	 *         "join_table_name": "STUDENT_PHYSICAL",      -- source table
	 *         "join_select_table_column": "STUDENT_NAME", -- source column
	 *         "master_table_name": "STUDENT_PHYSICAL",
	 *         "master_table_column": "PASSWORD",
	 *         "corp_id": "14ff0e052e7749d08aee31f25452f42f"
	 *     }
	 * }
	 * @return
	 */
	public boolean isInnerJoinColumn(String column, Map<String, Object> colDef) {
		if (colDef == null || colDef.get(column) == null) {
			// Attached file column or no column definition
			return false;
		}
		Map<String, Object> map = (Map<String, Object>) colDef.get(column);
		if (map.get("join_condition") == null) {
			return false;
		}
		String joinCondition = (String) map.get("join_condition");
		if ("0".equals(joinCondition)) {
			return true;
		}
		return false;
	}
    /**
     * 获取字段值和主键ID的映射关系的数量
     * @param pd
     * @param page
     * @throws Exception
     */
    public long getColumnIDMapCount(PageData pd, Page page) throws Exception {
        StringBuffer sb = generateColumnIDMapSql(pd, page);
        String countSql = "select count(0) as _count_ from (" + sb.toString() + ") as tmp_count";
        List<PageData> list = execute(countSql);
        return (Long) list.get(0).get("_count_");
    }
	/**
	 * 获取字段值和主键ID的映射关系，显示下拉列表用
	 * @param pd
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> getColumnIDMap(PageData pd, Page page) throws Exception {
        StringBuffer sb = generateColumnIDMapSql(pd, page);
		sb.append(" ORDER BY SORT_NO");
        if (!Tools.isEmpty(page)) {
            sb.append(" limit " + (page.getCurrentPage() - 1) * page.getShowCount() + "," + page.getShowCount());
        }
		return execute(sb.toString());
	}
    private StringBuffer generateColumnIDMapSql(PageData pd, Page page) {
        String schema = Jurisdiction.getTopCorpBianma();
        String tablesId = pd.getString("tablesId");
        String joinColumnsId = pd.getString("joinColumnsId");
        String joinColumnsIdAlias = pd.getString("joinColumnsIdAlias");
        String selectColumnsId = pd.getString("selectColumnsId");
        String selectColumnsIdAlias = pd.getString("selectColumnsIdAlias");
        String corpId = pd.getString("corpId");
        String in = pd.getString("in");
        String keywords = pd.getString("keywords");

        if (tablesId.startsWith("SYS_")) {
            schema = "asdb";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");
        // SYS_CORP matches CORP_ID, not SYS_CORP_ID,
        // this is a exception
        if (Tools.isEmpty(joinColumnsIdAlias)) {
            sb.append(joinColumnsId + ",");
        } else {
            sb.append(joinColumnsId + " AS " + joinColumnsIdAlias + ",");
        }
        if (Tools.isEmpty(selectColumnsIdAlias)) {
            sb.append(selectColumnsId);
        } else {
            sb.append(selectColumnsId + " AS " + selectColumnsIdAlias);
        }
        sb.append(" FROM ");
        sb.append(schema);
        sb.append(".");
        sb.append(tablesId);
        sb.append(" WHERE 1=1 ");
        sb = appendCorpId(sb, corpId);
        if (!Tools.isEmpty(in)) {
            // Fixed bug: 'a','b','c'  ==> ''a'',''b'',''c''
            in = in.replaceAll("''", "'");
            sb.append(" AND ");
            sb.append(joinColumnsId);
            sb.append(" IN (");
            sb.append(in);
            sb.append(") ");
        }
        if (!Tools.isEmpty(keywords)) {
            sb.append(" AND ");
            sb.append(selectColumnsId);
            sb.append(" LIKE '%");
            sb.append(keywords);
            sb.append("%' ");
        }
        sb.append(" AND STATUS = '1' ");
        return sb;
    }

	/**
	 * 对于单独list中，获取内联字段对应的值
	 * @param column
	 * @param colDef
	 * {
 	 *    NAME={
 	 *        "encrypt": "0"
	 *     },
	 *     PASSWORD={
	 *         "encrypt": "1",
	 *         "join_condition": "0",
	 *         "join_table_name": "STUDENT_PHYSICAL",         -- source table
	 *         "join_select_table_column": "STUDENT_NAME",    -- source column
	 *         "master_table_name": "STUDENT_PHYSICAL",
	 *         "master_table_column": "PASSWORD",
	 *         "corp_id": "14ff0e052e7749d08aee31f25452f42f"
	 *     }
	 * }
	 * @return
	 */
	public Object getInnerJoinColumnData(String column, Map<String, Object> colDef, String sourceColumnValue) throws Exception {
		Map<String, Object> map = (Map<String, Object>) colDef.get(column);
		if (map.get("join_condition") == null) {
			return null;
		}
		// 只处理内联
		String joinCondition = (String) map.get("join_condition");
		if (!"0".equals(joinCondition)) {
			return null;
		}
		// 内联字段没有设置多表规则，内联字段-->对应的源表字段-->JOIN_TABLE+JOIN_COLUMN(源头)->对应字段的TEXT
		String sourceTableId = (String) map.get("join_table_name");				// source table
		String sourceColumnsId = (String) map.get("join_columns_id");	        // source column---eed7b2854be44ae2b8b3d3c6a54a33b7
		String sourceColumnsName = (String) map.get("master_table_column");
		// 返回最基础的JOIN表和JOIN字段
		List<PageData> joinList = getOriginalJoinTableColumn(sourceTableId, sourceColumnsId);
		// 假定这种情况下一定有JOIN表和字段，需要考虑么有的情况
		// 所以以下逻辑正常情况不会有
		String msg = "";
		if (joinList.size() == 0) {
			msg = "多表规则中没有配置表[" + sourceTableId + "]列["+sourceColumnsId+"]的内联字段";
			throw new Exception(msg);
		} else if (joinList.size() > 1) {
			msg = "多表规则配置歧义，表[" + sourceTableId + "]列["+sourceColumnsId+"]对应多个内联字段";
			throw new Exception(msg);
		}
		// Slave表的内联字段设置了多表规则时，内联字段存储JOIN_TABLE的记录ID即可，显示的时候会进行ID-VALUE的反向映射
		PageData pdJoin = joinList.get(0);
		// 源表和字段找到了，需要获取源表中家长姓名字段对应的数据
		//    （JOIN_COLUMNS_ID）          WHERE COLUMN
		// 	从表名称      从表字段	源表名称	    源表字段	关联条件
		//学生出勤明细表	家长姓名	学生出勤明细表	学生姓名	0
		// SELECT JOIN_COLUMNS_ID1,JOIN_COLUMNS_ID2 
		// FROM JOIN_TABLES_ID
		// WHERE 源表字段=?
		// 如果一个[JOIN_TABLES_ID]对应多个[源表字段]，根据唯一性原则，这写字段[源表字段]都能过滤出唯一的[从表字段]的值
		String fromTablesId = pdJoin.getString("JOIN_TABLES_ID");	// 学生基础信息表
		CommonSearch cs = new CommonSearch();
		cs.setTableName(fromTablesId);
		cs.setMatchText(true);
		List<String> joinColumns = new ArrayList<String>();
		if (CommonService.isEncryptColumn((String) column, colDef)) {
			String encryptCode = Jurisdiction.getEncryptCode();
			String decryptValue = CommonManager.DECRYPT_VALUE.replace("#COLUMN#", sourceColumnsName);
			decryptValue = decryptValue.replace("#CODE#", encryptCode);
			decryptValue = decryptValue.replace("#ALIAS#", sourceColumnsName);
			joinColumns.add(decryptValue);
		} else {
			joinColumns.add(sourceColumnsName);
		}

		cs.setColumns(joinColumns);
		// 多表关联存储的关联字段的值，都是JOIN_TABLES_ID对应表的主键值
		// 例如排课表中的学生姓名字段对应的是STUDENT_ID
		cs.setWhereColumnName(fromTablesId + "_ID");// 注意，不是pd.getString("SOURCE_COLUMNS_NAME_EN"));
		cs.setWhereColumnValue(sourceColumnValue);
		List<PageData> innerSourceDataList = searchTable(cs);
		/*
		 * 3. Merge到list中
		 */
		if (innerSourceDataList.size() > 1) {
			msg = "多表的内联设置违反唯一性规则";
			logger.error(msg + cs.toString());
			throw new Exception(msg);
		} else if (innerSourceDataList.size() == 0) {
			msg = "没有找到内联数据, " + cs.toString();
			logger.warn(msg + cs.toString());
		} else {
			PageData pd = innerSourceDataList.get(0);
			return (pd == null) ? null : pd.get(sourceColumnsName);
		}
		return null;
	}

	/**
	 * List-List模式中Slave需要从Source查询数据
	 * 支持多数据源查询
	 * 目前只支持一个外联表，多个内联表
	 * 支持数据字段反向映射SOURCE_COLUMNS_NAME_EN-》SLAVE_COLUMNS_NAME_EN,
	 * 这样返回的数据可以直接插入Slave
	 * @param listListRuleDetail
	 * @return
	 */
	public List<PageData> getSourceTableData(PageData pdParams, List<PageData> listListRuleDetail, Map<String,Object> colDef) {
		String schema = Jurisdiction.getTopCorpBianma();
		String corpId = pdParams.getString("CORP_ID");
		String whereColumnName = pdParams.getString("SOURCE_COLUMNS_NAME_EN");
		String whereColumnValue = pdParams.getString("MASTER_COLUMNS_VALUE");
		String slaveTablesId = pdParams.getString("SLAVE_TABLES_ID");
		String sourceTablesId = null;
		List<PageData> outerSourceDataList = null;
		StringBuffer sb = new StringBuffer();
		boolean bFirst = true;
		try {
			for (int i = 0; i < listListRuleDetail.size(); i ++) {
				PageData pd = listListRuleDetail.get(i);
				// 只考虑外联，且只能有1个外联
				if (!"1".equals(pd.getString("JOIN_CONDITION"))) {
					continue;
				}
				if (bFirst) {
					sourceTablesId = pd.getString("SOURCE_TABLES_ID");	//1
					sb.append("SELECT ");
					bFirst = false;
				}
				String newSourceTablesId = pd.getString("SOURCE_TABLES_ID");
				if (!newSourceTablesId.equals(sourceTablesId)) {
					// 做第一次查询
					// Retrieve data from source table
					//删除逗号
					sb.deleteCharAt(sb.length() - 1);

					sb.append(" FROM ");
					sb.append(schema);
					sb.append(".");
					sb.append(sourceTablesId);
					sb.append(" WHERE 1=1 ");
					sb = appendCorpId(sb, corpId);
					sb.append(" AND ");
					sb.append(whereColumnName + " = '");
					sb.append(whereColumnValue);
					sb.append("'");

					outerSourceDataList = execute(sb.toString());

					// Empty sql buffer, renew another sql
					sb.delete(0,  sb.length());
					sb.append("SELECT ");
	
					sourceTablesId = newSourceTablesId;
				}
				String sourceColumnsName = pd.getString("SOURCE_COLUMNS_NAME_EN");	// 
				String slaveColumnsName = pd.getString("SLAVE_COLUMNS_NAME_EN");	// 别名,应该==sourceColumnsName
				if (CommonService.isEncryptColumn((String) sourceColumnsName, colDef)) {
					String encryptCode = Jurisdiction.getEncryptCode();
					String decryptValue = CommonManager.DECRYPT_VALUE.replace("#COLUMN#", sourceColumnsName);
					decryptValue = decryptValue.replace("#CODE#", encryptCode);
					decryptValue = decryptValue.replace("#ALIAS#", slaveColumnsName);
				} else {
					sb.append(sourceColumnsName + " AS " + slaveColumnsName);					
				}
				sb.append(",");
	
			}
			//删除逗号
			sb.deleteCharAt(sb.length() - 1);
			// 最后一个Source表
			sb.append(" FROM ");
			sb.append(schema);
			sb.append(".");
			sb.append(sourceTablesId);
			sb.append(" WHERE 1=1 ");
			sb = appendCorpId(sb, corpId);
			sb.append(" AND ");
			sb.append(whereColumnName + " = '");
			sb.append(whereColumnValue);
			sb.append("'");

			outerSourceDataList = execute(sb.toString());

			// 没有内联，直接返回源数据
			if (colDef.isEmpty()) {
				return outerSourceDataList;
			}
            outerSourceDataList = mergeInnerSourceData(outerSourceDataList, colDef);

			// outerSourceDataList = getInnerSourceData(corpId, outerSourceDataList, listListRuleDetail);
//			Map<String, Object> joinTableColumns = getListListInnerJoinSourceData(corpId, slaveTablesId);
//			if (joinTableColumns != null) {
//				for (int index = 0; index < outerSourceDataList.size(); index ++) {
//					PageData pd = outerSourceDataList.get(index);
//					/*
//					 * 1. 从多表规则中，根据从名表+从表字段获取对应的目标表[JOIN_TABLES_ID]+字段[JOIN_COLUMNS_ID]（学生基础信息表+家长）
//					 */
//					/*
//					 * 2. 根据内联的源表名+源表字段值（在list中获取），在目标表中获取从表字段值
//					 *    joinTableColumns可能出现多个规则，所以要循环取值
//					 */
//					for (String key : joinTableColumns.keySet()) {
//						CommonSearch commonSearch = (CommonSearch) joinTableColumns.get(key);
//						whereColumnValue = pd.getString(commonSearch.getSourceColumnsNameEn());
//						boolean matchText = commonSearch.isMatchText();
//						if (matchText) {
//							commonSearch.setWhereColumnValue(whereColumnValue);
//							commonSearch.setCorpId(corpId);
//							List<PageData> innerSourceDataList = searchTable(commonSearch);
//							/*
//							 * 3. Merge到list中
//							 */
//							if (innerSourceDataList.size() > 1) {
//								String msg = "LIST-LIST的内联设置违反唯一性规则";
//								logger.error(msg + commonSearch.toString());
//								throw new Exception(msg);
//							} else if (innerSourceDataList.size() == 0) {
//								String msg = "没有找到内联数据, " + commonSearch.toString();
//								logger.warn(msg + commonSearch.toString());
//							} else {
//								pd.putAll(innerSourceDataList.get(0));
//								outerSourceDataList.set(index, pd);
//							}
//						} else {
//							// Match ID
//							List<String> columns = commonSearch.getColumns();
//							for (String column : columns) {
//								pd.put(column, whereColumnValue);
//							}
//						}
//					}
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outerSourceDataList;
	}
    /*
     * 从表名称	从表字段	源表名称	源表字段	内联/外联
     * 返回的多List需要做merge
     * 外联: 多个外联表要做笛卡尔，先支持一个外联表
     *      主要数据源
     * 内联：根据从表字段，相同的从表字段值merge进去(put)
     *      张三 19岁 男 |<-家长姓名 电话
     */
    public List<PageData> mergeInnerSourceData(List<PageData> outerSourceDataList, Map<String,Object> colDef) {
        try {
            String encryptCode = Jurisdiction.getEncryptCode();
            for (int index = 0; index < outerSourceDataList.size(); index++) {
                PageData pd = outerSourceDataList.get(index);
                for (Object key : colDef.keySet()) {
                    // 获取对应值
                    Map<String, Object> map = (Map<String, Object>) colDef.get((String) key);
                    Object join_condition = map.get("join_condition");
                    // 只处理内联字段
                    if (!"0".equals(join_condition)) {
                        continue;
                    }
                    if (Tools.isEmpty(map.get("join_select_table_column"))) {
                        // 没有找到关联的源字段
                        continue;
                    }
                    String sourceColumnName = (String) map.get("join_select_table_column");
                    Object sourceColumnValue = pd.get(sourceColumnName);
                    if (Tools.isEmpty(sourceColumnValue)) {
                        // 对应的当前表的源字段没有值，不能进行关联
                        continue;
                    }
                    Object object = getInnerJoinColumnData((String) key, colDef, sourceColumnValue.toString());
                    if (Tools.isEmpty(object)) {
                        // 源字段没有值，不需要插入
                        continue;
                    }
                    pd.put(key, object);
                    // 不需要在这儿加密，后面的insert函数会自动判断加密  templateService.insert(tablesId, data, colDef);
//                if (isEncryptColumn((String) key, colDef)) {
//                    String encryptValue = CommonManager.ENCRYPT_VALUE.replace("#VALUE#", object.toString());
//                    encryptValue = encryptValue.replace("#CODE#", encryptCode);
//                    pd.put(key, encryptValue);
//                    outerSourceDataList.set(index, pd);
//                } else {
//                    pd.put(key, object);
//                }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outerSourceDataList;
    }
	/**
	 * 模式一：直接取关联的数据值----LISTLIST(SLAVE_COLUMNS)->MULTITABLE(JOIN_COLUMNS)->SOURCE_COLUMN
	 * 1. 从多表规则中，根据从名表+从表字段获取对应的目标表+字段（学生基础信息表+家长）
	 * 2. 根据内联的源表名+源表字段值（在list中获取），在目标表中获取从表字段值
	 * 3. Merge到list中
	 * 从表名称	   |从表字段 |源表名称	   |源表字段
	 * 学生出勤明细表	家长姓名	学生出勤明细表	学科名称
	 * 唯一性原则：内联的从表字段值可以通过内联的源表字段[JOIN_COLUMNS_ID]查出唯一值（无歧义）
	 * 内联需要校验1：[从表名称]+[从表字段]在多表规则中存在映射规则[JOIN_TABLES_ID]+[JOIN_COLUMNS_ID]
	 * 内联需要校验2：[源表字段]是否存在于[从表名称]对应的JOIN_TABLES_ID对应的物理表中
	 * 
	 * 模式二：取关联的数据的表ID
	 * 只要内联，就把内联的字段的值设为一样即可，多表规则采用key-value的方式反向映射
	 * 比如：        学生姓名                      家长姓名                            手机号码
	 * 219711d745b44129b6d177750ff48df0	(null)	219711d745b44129b6d177750ff48df0  219711d745b44129b6d177750ff48df0
	 * @param outerSourceDataList
	 * @param listListRuleDetail
	 * @return
	 */
	private List<PageData> getInnerSourceData(String corpId, List<PageData> outerSourceDataList, List<PageData> listListRuleDetail)
		throws Exception {
		/*
		 * 0. 从List-List规则->内联的从表字段->多表规则中的JOIN_TABLE和JOIN_COLUMN的字段名字
		 *                    ROSTER_DETAIL->PARENT_NAME<->STUDENT->PARENT_NAME
		 * 只要是内联，两个字段的数据均出自同一张表，该步骤是为了找源表
		 */
		Map<String, Object> joinTableColumns = new HashMap<String, Object>();
		List<String> joinColumns = null;
		CommonSearch cs = null;
		for (int i = 0; i < listListRuleDetail.size(); i ++) {
			PageData pd = listListRuleDetail.get(i);
			// 只考虑内联
			if (!"0".equals(pd.getString("JOIN_CONDITION"))) {
				continue;
			}
			// 对应case1 或 case2
			boolean bMatchText = false;
			String slaveTableId = pd.getString("SLAVE_TABLES_ID");
			String slaveColumnsId = pd.getString("SLAVE_COLUMNS_ID");
			String slaveColumnsName = pd.getString("SLAVE_COLUMNS_NAME_EN");
			// Parameters only
			pd.put("TABLES_ID", slaveTableId);		//  内联表
			pd.put("COLUMNS_ID", slaveColumnsId);	//  内联字段
			List<PageData> joinList = multitableruleService.listAll(pd);//STUDENT
			if (joinList.size() == 0) {
				String msg = "多表规则中没有配置表[" + slaveTableId + "]列["+slaveColumnsName+"]的关联字段";
				logger.info(msg);
				// Case 1:
				// Slave表的内联字段没有设置多表规则，内联字段-->对应的源表字段-->JOIN_TABLE+JOIN_COLUMN(源头)->对应字段的TEXT
				String sourceTableId = pd.getString("SOURCE_TABLES_ID");
				String sourceColumnsId = pd.getString("SOURCE_COLUMNS_ID");
				String sourceColumnsName = pd.getString("SOURCE_COLUMNS_NAME_EN");
				// 返回最基础的JOIN表和JOIN字段
				joinList = getOriginalJoinTableColumn(sourceTableId, sourceColumnsId);
				// 假定这种情况下一定有JOIN表和字段，需要考虑么有的情况
				// TODO winbill. 20170303
				bMatchText = true;
				// Slave表的SOURCE_COLUMN是关联的SOURCE表，如果想展示，必须要设置JOIN_COLUMN
				// 所以以下逻辑正常情况不会有
				if (joinList.size() == 0) {
					msg = "多表规则中没有配置表[" + sourceTableId + "]列["+sourceColumnsName+"]的关联字段";
					throw new Exception(msg);
				} else if (joinList.size() > 1) {
					msg = "多表规则配置歧义，表[" + sourceTableId + "]列["+sourceColumnsName+"]有多个关联字段";
					throw new Exception(msg);
				}
			} else if (joinList.size() > 1) {
				String msg = "多表规则配置歧义，表[" + slaveTableId + "]列["+slaveColumnsName+"]有多个关联字段";
				logger.error(msg);
				throw new Exception(msg);
			}
			// Case 2:
			// Slave表的内联字段设置了多表规则时，内联字段存储JOIN_TABLE的记录ID即可，显示的时候会进行ID-VALUE的反向映射
			PageData pdJoin = joinList.get(0);
			// 源表和字段找到了，需要获取源表中家长姓名字段对应的数据
			//    （JOIN_COLUMNS_ID）          WHERE COLUMN
			// 	从表名称      从表字段	源表名称	    源表字段	关联条件
			//学生出勤明细表	家长姓名	学生出勤明细表	学生姓名	0
			// SELECT JOIN_COLUMNS_ID1,JOIN_COLUMNS_ID2 
			// FROM JOIN_TABLES_ID
			// WHERE 源表字段=?
			// 如果一个[JOIN_TABLES_ID]对应多个[源表字段]，根据唯一性原则，这写字段[源表字段]都能过滤出唯一的[从表字段]的值
			String fromTablesId = pdJoin.getString("JOIN_TABLES_ID");	// 学生基础信息表
			String selectColumnsId = pd.getString("SLAVE_COLUMNS_NAME_EN");	// 家长姓名，要改为SLAVE_COLUMNS_NAME_EN, 就是内联字段
			if (!joinTableColumns.containsKey(fromTablesId)) {
				cs = new CommonSearch();
				joinColumns = new ArrayList<String>();
				cs.setTableName(fromTablesId);
			}
			cs.setMatchText(bMatchText);
			joinColumns.add(selectColumnsId);
			cs.setColumns(joinColumns);
			// 多表关联存储的关联字段的值，都是JOIN_TABLES_ID对应表的主键值
			// 例如排课表中的学生姓名字段对应的是STUDENT_ID
			cs.setWhereColumnName(fromTablesId + "_ID");// 注意，不是pd.getString("SOURCE_COLUMNS_NAME_EN"));
			cs.setSourceColumnsNameEn(pd.getString("SOURCE_COLUMNS_NAME_EN")); // 传STUDENT_ID的目的是从外联数据中获取对应的那行数据
			joinTableColumns.put(fromTablesId, cs);
		}
		for (int index = 0; index < outerSourceDataList.size(); index ++) {
			PageData pd = outerSourceDataList.get(index);
			/*
			 * 1. 从多表规则中，根据从名表+从表字段获取对应的目标表[JOIN_TABLES_ID]+字段[JOIN_COLUMNS_ID]（学生基础信息表+家长）
			 */
			/*
			 * 2. 根据内联的源表名+源表字段值（在list中获取），在目标表中获取从表字段值
			 *    joinTableColumns可能出现多个规则，所以要循环取值
			 */
			for (String key : joinTableColumns.keySet()) {
				CommonSearch commonSearch = (CommonSearch) joinTableColumns.get(key);
				String whereColumnValue = pd.getString(cs.getSourceColumnsNameEn());
				boolean matchText = commonSearch.isMatchText();
				if (matchText) {
					commonSearch.setWhereColumnValue(whereColumnValue);
					commonSearch.setCorpId(corpId);
					List<PageData> innerSourceDataList = searchTable(commonSearch);
					/*
					 * 3. Merge到list中
					 */
					if (innerSourceDataList.size() > 1) {
						String msg = "LIST-LIST的内联设置违反唯一性规则";
						logger.error(msg + commonSearch.toString());
						throw new Exception(msg);
					} else if (innerSourceDataList.size() == 0) {
						String msg = "没有找到内联数据, " + commonSearch.toString();
						logger.warn(msg + commonSearch.toString());
					} else {
						pd.putAll(innerSourceDataList.get(0));
						outerSourceDataList.set(index, pd);
					}
				} else {
					// Match ID
					List<String> columns = commonSearch.getColumns();
					for (String column : columns) {
						pd.put(column, whereColumnValue);
					}
				}
			}
		}
		return outerSourceDataList;
	}
	/**
	 * 针对ListList从表内联字段
	 * 考虑到加密等字段，不采用模式二，全部冗余字段值
	 * @param corpId
	 * @param slaveTablesId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getListListInnerJoinSourceData(String corpId, String slaveTablesId)
			throws Exception {
		/*
		 * 0. 从多表规则中获取内联规则
		 */
		// Parameters only
		PageData pdParams = new PageData();
		pdParams.put("TABLES_ID", slaveTablesId);		//  内联表
		pdParams.put("CORP_ID", corpId);	            //  单位编码
		pdParams.put("JOIN_CONDITION", "0");	        //  内联
		List<PageData> innerJoinList = multitableruleService.listAll(pdParams);//STUDENT_ROSTER_ITEM
		if (innerJoinList.size() == 0) {
			return null;
		}
		/*
		 * 0. 从List-List规则->内联的从表字段->多表规则中的JOIN_TABLE和JOIN_COLUMN的字段名字
		 *                    ROSTER_DETAIL->PARENT_NAME<->STUDENT->PARENT_NAME
		 * 只要是内联，两个字段的数据均出自同一张表，该步骤是为了找源表
		 * 内联字段，不能是外联字段
		 */
		Map<String, Object> joinTableColumns = new HashMap<String, Object>();
		List<String> joinColumns = null;
		CommonSearch cs = null;
		for (int i = 0; i < innerJoinList.size(); i ++) {
			PageData pd = innerJoinList.get(i);
			// 对应case1
			boolean bMatchText = true;
			String slaveTableId = pd.getString("TABLES_ID");
			String slaveColumnsId = pd.getString("COLUMNS_ID");
			String slaveColumnsName = pd.getString("COLUMNS_NAME_EN");
			// Case 1:
			// Slave表的内联字段没有设置多表规则，内联字段-->对应的源表字段-->JOIN_TABLE+JOIN_COLUMN(源头)->对应字段的TEXT
			String sourceTableId = pd.getString("JOIN_TABLES_ID");
			String sourceColumnsId = pd.getString("JOIN_COLUMNS_ID");
			String sourceColumnsName = pd.getString("JOIN_COLUMNS_NAME_EN");
			// 返回最基础的JOIN表和JOIN字段
			List<PageData> joinList = getOriginalJoinTableColumn(sourceTableId, sourceColumnsId);
			// 假定这种情况下一定有JOIN表和字段，需要考虑么有的情况
			// Slave表的SOURCE_COLUMN是关联的SOURCE表，如果想展示，必须要设置JOIN_COLUMN
			// 所以以下逻辑正常情况不会有
			String msg = null;
			if (joinList.size() == 0) {
				msg = "多表规则中没有配置表[" + sourceTableId + "]列["+sourceColumnsName+"]的外联字段";
				throw new Exception(msg);
			} else if (joinList.size() > 1) {
				msg = "多表规则配置歧义，表[" + sourceTableId + "]列["+sourceColumnsName+"]有多个外联字段";
				throw new Exception(msg);
			}
			// Case 2:
			// Slave表的内联字段设置了多表规则时，内联字段存储JOIN_TABLE的记录ID即可，显示的时候会进行ID-VALUE的反向映射
			PageData pdJoin = joinList.get(0);
			// 源表和字段找到了，需要获取源表中家长姓名字段对应的数据
			//    （JOIN_COLUMNS_ID）          WHERE COLUMN
			// 	从表名称      从表字段	源表名称	    源表字段	关联条件
			//学生出勤明细表	家长姓名	学生出勤明细表	学生姓名	0
			// SELECT JOIN_COLUMNS_ID1,JOIN_COLUMNS_ID2 
			// FROM JOIN_TABLES_ID
			// WHERE 源表字段=?
			// 如果一个[JOIN_TABLES_ID]对应多个[源表字段]，根据唯一性原则，这写字段[源表字段]都能过滤出唯一的[从表字段]的值
			String fromTablesId = pdJoin.getString("JOIN_TABLES_ID");	// 学生基础信息表
			String selectColumnsId = pd.getString("COLUMNS_NAME_EN");	// 家长姓名，要改为SLAVE_COLUMNS_NAME_EN, 就是内联字段
			if (!joinTableColumns.containsKey(fromTablesId)) {
				cs = new CommonSearch();
				joinColumns = new ArrayList<String>();
				cs.setTableName(fromTablesId);
			}
			cs.setMatchText(bMatchText);
			joinColumns.add(selectColumnsId);
			cs.setColumns(joinColumns);
			// 多表关联存储的关联字段的值，都是JOIN_TABLES_ID对应表的主键值
			// 例如排课表中的学生姓名字段对应的是STUDENT_ID
			cs.setWhereColumnName(fromTablesId + "_ID");// 注意，不是pd.getString("SOURCE_COLUMNS_NAME_EN"));
			cs.setSourceColumnsNameEn(pd.getString("JOIN_COLUMNS_NAME_EN")); // 传STUDENT_ID的目的是从外联数据中获取对应的那行数据
			joinTableColumns.put(fromTablesId, cs);
		}

		return joinTableColumns;
	}
	List<PageData> getOriginalJoinTableColumn(String tablesId, String columnsId) throws Exception {
		PageData pd = new PageData();
		pd.put("TABLES_ID", tablesId);
		pd.put("COLUMNS_ID", columnsId);
		List<PageData> joinListOld = null;
		while (true) {
			List<PageData> joinList = multitableruleService.listAll(pd);//STUDENT
			// Found one
			if (joinList.size() == 0) {
				break;
			}
			joinListOld = joinList;
			pd.put("TABLES_ID", joinList.get(0).getString("JOIN_TABLES_ID"));
			pd.put("COLUMNS_ID", joinList.get(0).getString("JOIN_COLUMNS_ID"));
		}
		return joinListOld;
	}
	/**
	 * 使用CommonSearch结构做通用查询
	 * @param cs
	 * @return
	 * @throws Exception
	 */
	public List<PageData> searchTable(CommonSearch cs) throws Exception {
		StringBuffer sb = new StringBuffer();
		String schema = Jurisdiction.getTopCorpBianma();
		List<String> columns = cs.getColumns();
		sb.append("SELECT ");
		for (int i = 0; i < columns.size(); i ++) {
			sb.append(columns.get(i));
			if (i != columns.size() - 1) {
				sb.append(",");
			}
		}
		sb.append(" FROM ");
		sb.append(schema);
		sb.append(".");
		sb.append(cs.getTableName());
		sb.append(" WHERE 1=1 ");
		sb = appendCorpId(sb, cs.getCorpId());
		sb.append(" AND ");
		sb.append(cs.getWhereColumnName());
		sb.append(" = '");
		sb.append(cs.getWhereColumnValue());
		sb.append("'");
		return execute(sb.toString());
	}

	public List<PageData> execute(String sql) throws Exception {
		return (List<PageData>) dao.findForList("CommonMapper.listAll", sql);
	}
	/** 日志专属查询，修改*/
	public List<PageData> log(String sql) throws Exception {
		return (List<PageData>) dao.findForList("CommonMapper.listAll", sql);
	}

	private PageData findOne(String sql) throws Exception {
		return (PageData) dao.findForObject("CommonMapper.findById", sql);
	}

	/**
	 * 追加公司权限
	 * @param sb
	 * @param corpId
	 * @return
	 */
	private StringBuffer appendCorpId(StringBuffer sb, String corpId) {
		if (!Tools.isEmpty(corpId)) {
			// 父看子
			String corpCondition = "AND CORP_ID IN ( SELECT m.CORP_ID FROM asdb.SYS_CORP m WHERE m.CORP_ID = '#CORP_ID#' "
					+ "UNION ALL SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID = '#CORP_ID#' "
					+ "UNION ALL SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID IN (SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID = '#CORP_ID#') "
					+ "UNION ALL SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID IN (SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID IN (SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID = '#CORP_ID#'))) ";
			corpCondition = corpCondition.replaceAll("#CORP_ID#", corpId);
			sb.append(corpCondition);
		}
		return sb;
	}

	/**
	 * 更新STATUS为可用
	 * @param tablesId
	 * @param corpId
	 * @param dataDate
	 * @throws Exception
	 */
	public void updateStatus(String tablesId, String corpId, String dataDate)throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE ");
		sb.append(schema);
		sb.append(".");
		sb.append(tablesId);
		sb.append(" SET STATUS = '1' ");
		sb.append(" WHERE CORP_ID = '");
		sb.append(corpId);
		sb.append("' AND DATA_DATE = '");
		sb.append(dataDate);
		sb.append("'");
		dao.update("CommonMapper.edit", sb.toString());
	}

	/**
	 * 获取公司/组织已使用的Mysql数据库空间
	 * 获取公司/组织已使用的附件/硬盘空间
	 * getDatabaseUsedSize
	 * @param corpId
	 * @return
	 */
	public PageData getDatabaseUsedSize(String corpId) {
		PageData pdInput = new PageData();
		pdInput.put("corpId", corpId);
		PageData pdOutput;
		try {
			pdOutput = departmentService.findById(pdInput);
			if (pdOutput == null) {
				return null;
			}
			// Bianma 也是Mysql的schema
			String bianma = (String) pdOutput.get("BIANMA");
			// select concat(round(sum(data_length),2),'Byte') as usage from tables where table_schema='xiaoniukafu';
			String sql = "select round(sum(data_length),2) as usage_size from tables where table_schema='" + bianma + "'";
			return findOne(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public PageData getFastdfsUsedSize(String corpId) {
		String sql = "select sum(file_size) as usage_size from asdb.sys_file_info";
		try {
			return findOne(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void insert(String tablesId, PageData pd)throws Exception{
		// 过滤控制字段
		StringBuffer sb = new StringBuffer();
		
		dao.save("CommonMapper.insert", sb.toString());
	}
	public void save(String tablesId, PageData pd)throws Exception{
		StringBuffer sb = new StringBuffer();

		dao.save("CommonMapper.save", sb.toString());
	}

	public void deleteBusinessData(String tablesId, PageData pd)throws Exception{
		String id = pd.getString("id");
		StringBuffer sb = new StringBuffer();

		dao.save("CommonMapper.save", sb.toString());
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("CommonMapper.delete", pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("CommonMapper.edit", pd);
	}

	/**列表
	 * @param tablesId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(String tablesId, String dataDate, String departmentId, List<Column> columns)throws Exception{
		
		StringBuffer sb = new StringBuffer();

		return (List<PageData>)dao.findForList("CommonMapper.listAll", sb.toString());
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CommonMapper.datalistPage", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CommonMapper.findById", pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CommonMapper.deleteAll", ArrayDATA_IDS);
	}
	

}

