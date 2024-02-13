package com.as.service.business.template.impl;

import com.as.dao.DaoSupport;
import com.as.entity.business.Column;
import com.as.service.business.common.CommonManager;
import com.as.service.business.common.impl.CommonService;
import com.as.service.business.template.TemplateManager;
import com.as.util.Jurisdiction;
import com.as.util.PageData;
import com.as.util.Tools;
import com.as.util.UuidUtil;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 说明： 每日销售完成情况 创建人：antispy 创建时间：2016-09-04
 * 
 * @version
 */
@Service("templateService")
public class TemplateService implements TemplateManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Resource(name = "commonService")
	public CommonManager commonService;

	private String getColums(String tablesId, List<Column> columns) throws Exception {
		if (columns.size() == 0) {
			return "a." + tablesId + "_ID";
		}
		String encryptCode = Jurisdiction.getEncryptCode();
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < columns.size(); index++) {
			// CAST(AES_DECRYPT(password,'xiaoniukafu') AS CHAR)
			if ("1".equals(columns.get(index).getENCRYPT())) {
				String decryptValue = CommonManager.DECRYPT_VALUE.replace("#COLUMN#",
						"a." + columns.get(index).getNAME_EN());
				decryptValue = decryptValue.replace("#CODE#", encryptCode);
				decryptValue = decryptValue.replace("#ALIAS#", columns.get(index).getNAME_EN());
				sb.append(decryptValue);
				sb.append(",");

			} else {
				sb.append("a.");
				sb.append(columns.get(index).getNAME_EN());
				sb.append(",");
			}
		}
		sb.append("a.");
		sb.append(tablesId + "_ID id");
		return sb.toString();
	}

	private boolean isControllColumn(String column) {
		if (column.equals("num") || column.equals("id") || column.equals("dirty") || column.equals("btn")) {
			return true;
		}
		return false;
	}

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void insert(String tablesId, PageData pd, Map<String, Object> colDef) throws Exception {
		// 过滤控制字段
		String schema = Jurisdiction.getTopCorpBianma();
		String encryptCode = Jurisdiction.getEncryptCode();
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append(schema);
		sb.append(".");
		sb.append(tablesId);
		sb.append(" (");
		List<Object> values = new ArrayList<Object>();
		for (Object key : pd.keySet()) {
			Object value = pd.get(key);
			if (isControllColumn((String) key)) {
				continue;
			}
			if (Tools.notEmpty(value)) {
				if (Tools.isEmpty(value)) {
					// 字符串类型或加密blob字段，为空字符串
					continue;
				}
				// AES_ENCRYPT('AES_ENCRYPT', 'key')
				if (CommonService.isEncryptColumn((String) key, colDef)) {
					String encryptValue = CommonManager.ENCRYPT_VALUE.replace("#VALUE#", (String) value);
					encryptValue = encryptValue.replace("#CODE#", encryptCode);
					values.add(encryptValue);
				} else {
					values.add("'" + value + "'");
				}
				sb.append(key.toString());
				sb.append(",");
			} else {
				// 内联字段，初始为空--比如：学生密码
				if (commonService.isInnerJoinColumn((String) key, colDef)) {
					// 获取对应值
					Map<String, Object> map = (Map<String, Object>) colDef.get((String) key);
					if (map.get("join_select_table_column") == null) {
						// 没有找到关联的源字段
						continue;
					}
					String sourceColumnName = (String) map.get("join_select_table_column");
					Object sourceColumnValue = pd.get(sourceColumnName);
					if (Tools.isEmpty(sourceColumnValue)) {
						// 对应的当前表的源字段没有值，不能进行关联
						continue;
					}
					Object object = commonService.getInnerJoinColumnData((String) key, colDef,
							sourceColumnValue.toString());
					if (Tools.isEmpty(object)) {
						// 源字段没有值，不需要插入
						continue;
					}
					if (CommonService.isEncryptColumn((String) key, colDef)) {
						String encryptValue = CommonManager.ENCRYPT_VALUE.replace("#VALUE#", object.toString());
						encryptValue = encryptValue.replace("#CODE#", encryptCode);
						values.add(encryptValue);
					} else {
						values.add("'" + object.toString() + "'");
					}
					sb.append(key.toString());
					sb.append(",");
				}
			}
		}
		sb.append(tablesId + "_ID");
		sb.append(") VALUES (");
		for (Object value : values) {
			sb.append(value);
			sb.append(",");
		}
		String id = pd.getString("id");
		sb.append("'");
		sb.append(Tools.isEmpty(id) ? UuidUtil.get32UUID() : id);
		sb.append("')");

		dao.save("TemplateMapper.insert", sb.toString());
	}

	public void save(String tablesId, PageData pd, Map<String, Object> colDef) throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		String encryptCode = Jurisdiction.getEncryptCode();
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE ");
		sb.append(schema);
		sb.append(".");
		sb.append(tablesId);
		sb.append(" SET ");
		for (Object key : pd.keySet()) {
			if (isControllColumn((String) key)) {
				continue;
			}
			Object value = pd.get(key);
			if (value instanceof String && Tools.isEmpty((String) value)) {
				// 字符串类型，为空字符串
				sb.append(key);
				sb.append(" = ");
				sb.append("null");
				sb.append(" , ");
			} else {
				sb.append(key);
				// encrypted column
				if (CommonService.isEncryptColumn((String) key, colDef)) {
					String encryptValue = CommonManager.ENCRYPT_VALUE.replace("#VALUE#", (String) value);
					encryptValue = encryptValue.replace("#CODE#", encryptCode);
					sb.append(" = ");
					sb.append(encryptValue);
					sb.append(" , ");
				} else {
					sb.append(" = '");
					sb.append(value);
					sb.append("' , ");
				}
			}
		}
		String sql = sb.substring(0, sb.length() - 3);
		sql += " WHERE ";
		sql += tablesId + "_ID";
		sql += " = '";
		sql += pd.getString("id");
		sql += "'";

		dao.save("TemplateMapper.save", sql);
	}

	public void deleteBusinessData(String tablesId, PageData pd) throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		String id = pd.getString("id");
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM ");
		sb.append(schema);
		sb.append(".");
		sb.append(tablesId);
		if (Tools.isEmpty(id)) {
			Object date = pd.get("DATA_DATE");
			String dataDate = date.toString();
			sb.append(" WHERE DATA_DATE='");
			sb.append(dataDate);
			sb.append("' AND CORP_ID='");
			sb.append(pd.getString("CORP_ID"));
			sb.append("'");
		} else {
			sb.append(" WHERE ");
			sb.append(tablesId + "_ID = '");
			sb.append(id);
			sb.append("'");
		}

		dao.save("TemplateMapper.save", sb.toString());
	}

	public void deleteSlaveData(String tablesId, PageData pd) throws Exception {
		String schema = Jurisdiction.getTopCorpBianma();
		String parentId = pd.getString("PARENT_ID");
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM ");
		sb.append(schema);
		sb.append(".");
		sb.append(tablesId);
		if (!Tools.isEmpty(parentId)) {
			sb.append(" WHERE ");
			sb.append("PARENT_ID = '");
			sb.append(parentId);
			sb.append("'");
			dao.save("TemplateMapper.save", sb.toString());
		}
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		dao.delete("TemplateMapper.delete", pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		dao.update("TemplateMapper.edit", pd);
	}

	/**
	 * 列表
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageData listAll(PageData pd) throws Exception {

		PageData pdData = new PageData();
		String encryptCode = Jurisdiction.getEncryptCode();
		String schema = Jurisdiction.getTopCorpBianma();
		String tablesId = pd.getString("TABLES_ID");
		String corpId = pd.getString("CORP_ID");
		String dataDate = pd.getString("DATA_DATE");
		int pageNum = Integer.parseInt(pd.getString("pageNum"));
		int pageSize = Integer.parseInt(pd.getString("pageSize"));

		List<Column> columns = (List<Column>) pd.get("LIST_COLUMNS");
		/*
		 * PARENT_ID目前用在LIST-LIST模式中，master弹出slave时的选中行的ID值
		 */
		String parentId = pd.getString("PARENT_ID");

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		// select video_sport,CAST(AES_DECRYPT(password,'xiaoniukafu') AS CHAR) address
		// from student_physical
		sb.append(getColums(tablesId, columns));
		sb.append(" FROM ");
		sb.append(schema);
		sb.append(".");
		sb.append(tablesId);
		sb.append(" a "); // master table's alias
		/*
		 * **********************需要JOIN其他表 * {join_table_name=COURSE,
		 * join_table_column=COURSE_NAME, value=d4b89c682d6542a890dba715d4fceabb} inner
		 * join COURSE COURSE on COURSE.COURSE{_ID} = a.COURSE_NAME where
		 * COURSE.COURSE_ID = {value}
		 */
		List<Map<String, String>> whereList = getWhere(pd);
		if (whereList != null && whereList.size() > 0) {
			for (int index = 0; index < whereList.size(); index++) {
				Map<String, String> map = whereList.get(index);
				String joinTableName = map.get("join_table_name");
				if (Tools.isEmpty(joinTableName)) {
					continue;
				}
				String joinTableColumn = map.get("join_table_column");
				String joinSelectTableColumn = map.get("join_select_table_column");
				String masterTableName = map.get("master_table_name");
				String masterTableColumn = map.get("master_table_column");
				String value = map.get("value");
				String innerJoin = CommonManager.INNER_JOIN_TABLE.replaceAll("#JOIN_TABLE#", joinTableName); // COURSE
				// 判断是否关联的系统database
				if (joinTableName.startsWith("SYS_") || joinTableName.startsWith("B_")) {
					innerJoin = innerJoin.replaceAll("#SCHEMA#", "asdb"); // schema
				} else {
					innerJoin = innerJoin.replaceAll("#SCHEMA#", schema); // schema
				}
				innerJoin = innerJoin.replaceAll("#JOIN_COLUMN#", joinTableColumn); // COURSE_ID
				innerJoin = innerJoin.replaceAll("#MASTER_TABLE#", masterTableName); // ROSTER a
				innerJoin = innerJoin.replaceAll("#MASTER_COLUMN#", masterTableColumn); // COURSE_NAME
				innerJoin = innerJoin.replaceAll("#VALUE#", value);
				sb.append(innerJoin);
			}
		}
		sb.append(" WHERE 1=1 ");
		if (!Tools.isEmpty(corpId)) {
			// 父看子
			String corpCondition = "AND a.CORP_ID IN ( SELECT m.CORP_ID FROM asdb.SYS_CORP m WHERE m.CORP_ID = '#CORP_ID#' "
					+ "UNION ALL SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID = '#CORP_ID#' "
					+ "UNION ALL SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID IN (SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID = '#CORP_ID#') "
					+ "UNION ALL SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID IN (SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID IN (SELECT CORP_ID FROM asdb.SYS_CORP WHERE PARENT_ID = '#CORP_ID#'))) ";
			corpCondition = corpCondition.replaceAll("#CORP_ID#", corpId);
			sb.append(corpCondition);
		}
		if (!Tools.isEmpty(parentId)) {
			sb.append(" AND a.PARENT_ID = '");
			sb.append(parentId);
			sb.append("' ");
		}
		if (!Tools.isEmpty(dataDate)) {
			sb.append(" AND a.DATA_DATE = '");
			sb.append(dataDate);
			sb.append("' ");
		}
		/*
		 * **********************不需要JOIN其他表 * {join_table_column=COURSE_NAME,
		 * value=d4b89c682d6542a890dba715d4fceabb} where a.COURSE_NAME like '{value}%'
		 */
		if (whereList != null && whereList.size() > 0) {
			List<PageData> seach = commonService.execute("show index from " + schema + "." + tablesId);
			LinkedHashMap<String, ArrayList<String>> dynamicSeach = new LinkedHashMap<String, ArrayList<String>>();
			for (int i = 0; i < seach.size(); i++) {
				PageData p = seach.get(i);
				if (p.getString("Key_name").indexOf("_dynamic_search") > 0) {
					if (!dynamicSeach.containsKey(p.getString("Key_name"))) {
						dynamicSeach.put(p.getString("Key_name"), new ArrayList<String>());
					}
					dynamicSeach.get(p.getString("Key_name")).add(p.getString("Column_name"));
				}
			}
			List<String> Seach = new ArrayList<String>();
			LinkedHashMap<String, ArrayList<String>> Seach_join = new LinkedHashMap<String, ArrayList<String>>();

			for (int index = 0; index < whereList.size(); index++) {
				Map<String, String> map = whereList.get(index);
				String joinTableName = map.get("join_table_name");
				String joinTableColumn = map.get("join_table_column");
				String value = map.get("value");
				String type = map.get("type");
				String encrypt = map.get("encrypt"); // encrypt or not
				String sqlWhere = null;
				// 无其他表 JOIN
				if (Tools.isEmpty(joinTableName)) {
					Seach.add(joinTableColumn.trim());
					if ("BOOL".equals(type)) {
						if ("1".equals(encrypt)) {
							sqlWhere = CommonManager.ENCRYPT_NO_INNER_JOIN_WHERE.replaceAll("#JOIN_COLUMN#",
									joinTableColumn);
							sqlWhere = sqlWhere.replaceAll("#CODE#", encryptCode);
						} else {
							sqlWhere = CommonManager.NO_INNER_JOIN_WHERE.replaceAll("#JOIN_COLUMN#", joinTableColumn);
						}
					} else if ("DATE".equals(type) || "DATETIME".equals(type) || "TIME".equals(type)) {
						if ("1".equals(encrypt)) {
							if (value.indexOf(",") > 0) {
								sqlWhere = CommonManager.ENCRYPT_NO_INNER_JOIN_WHERE_BETWEEN.replaceAll("#JOIN_COLUMN#",
										joinTableColumn);
								sqlWhere = sqlWhere.replaceAll("#VALUE#", "'"+value.split(",")[0] +"' and '"+value.split(",")[1]+"'");
							} else {
								sqlWhere = CommonManager.ENCRYPT_NO_INNER_JOIN_WHERE.replaceAll("#JOIN_COLUMN#",
										joinTableColumn);
							}
							sqlWhere = sqlWhere.replaceAll("#CODE#", encryptCode);
						} else {
							if (value.indexOf(",") > 0) {
								sqlWhere = CommonManager.NO_INNER_JOIN_WHERE_BETWEEN.replaceAll("#JOIN_COLUMN#",
										joinTableColumn);
								sqlWhere = sqlWhere.replaceAll("#VALUE#", "'"+value.split(",")[0] +"' and '"+value.split(",")[1]+"'");
							} else {
								sqlWhere = CommonManager.NO_INNER_JOIN_WHERE.replaceAll("#JOIN_COLUMN#",
										joinTableColumn);
							}
						}

					} else {
						// encrypt column
						if ("1".equals(encrypt)) {
							sqlWhere = CommonManager.ENCRYPT_NO_INNER_JOIN_WHERE_LIKE.replaceAll("#JOIN_COLUMN#",
									joinTableColumn);
							sqlWhere = sqlWhere.replaceAll("#CODE#", encryptCode);
						} else {
							sqlWhere = CommonManager.NO_INNER_JOIN_WHERE_LIKE.replaceAll("#JOIN_COLUMN#",
									joinTableColumn);
						}
					}
					sqlWhere = sqlWhere.replaceAll("#VALUE#", value);
				} else {
					if (!joinTableName.startsWith("SYS_") && !joinTableName.startsWith("B_")) {
						if (!Seach_join.containsKey(schema + "." + joinTableName)) {
							Seach_join.put(schema + "." + joinTableName, new ArrayList<String>());
						}
						Seach_join.get(schema + "." + joinTableName).add(joinTableColumn.trim());
					}

					if ("BOOL".equals(type)) {
						if ("1".equals(encrypt)) {
							sqlWhere = CommonManager.ENCRYPT_INNER_JOIN_WHERE.replaceAll("#JOIN_TABLE#", joinTableName);
							sqlWhere = sqlWhere.replaceAll("#CODE#", encryptCode);
						} else {
							sqlWhere = CommonManager.INNER_JOIN_WHERE.replaceAll("#JOIN_TABLE#", joinTableName);
						}
					} else if ("DATE".equals(type) || "TIME".equals(type) || "DATETIME".equals(type)) {
						// encrypt column
						if ("1".equals(encrypt)) {
							if (value.indexOf(",") > 0) {
								sqlWhere = CommonManager.ENCRYPT_INNER_JOIN_WHERE_BETWEEN.replaceAll("#JOIN_TABLE#",
										joinTableName);
								sqlWhere = sqlWhere.replaceAll("#VALUE#", "'"+value.split(",")[0] +"' and '"+value.split(",")[1]+"'");
							} else {
								sqlWhere = CommonManager.ENCRYPT_INNER_JOIN_WHERE.replaceAll("#JOIN_TABLE#",
										joinTableName);
							}
							sqlWhere = sqlWhere.replaceAll("#CODE#", encryptCode);
						} else {
							if (value.indexOf(",") > 0) {
								sqlWhere = CommonManager.INNER_JOIN_WHERE_BETWEEN.replaceAll("#JOIN_TABLE#",joinTableName);
								sqlWhere = sqlWhere.replaceAll("#VALUE#", "'"+value.split(",")[0] +"' and '"+value.split(",")[1]+"'");
							} else {
								sqlWhere = CommonManager.INNER_JOIN_WHERE.replaceAll("#JOIN_TABLE#", joinTableName);
							}
						}
					} else {
						if ("1".equals(encrypt)) {
							sqlWhere = CommonManager.ENCRYPT_INNER_JOIN_WHERE_LIKE.replaceAll("#JOIN_TABLE#",joinTableName);
							sqlWhere = sqlWhere.replaceAll("#CODE#", encryptCode);
						} else {
							sqlWhere = CommonManager.INNER_JOIN_WHERE_LIKE.replaceAll("#JOIN_TABLE#", joinTableName);
						}
					}
					sqlWhere = sqlWhere.replaceAll("#JOIN_COLUMN#", joinTableColumn);
					sqlWhere = sqlWhere.replaceAll("#VALUE#", value);
				}
				sb.append(sqlWhere);
			}
			if (Seach.size() > 0) {
				String s = "";
				Collections.sort(Seach);
				for (int i = 0; i < Seach.size(); i++) {
					s = s.equals("") ? "`" + Seach.get(i) + "`" : s + "," + "`" + Seach.get(i) + "`";
				}
				for (Entry<String, ArrayList<String>> entry : dynamicSeach.entrySet()) {
					Collections.sort(entry.getValue());
					String ss = "";
					for (int i = 0; i < entry.getValue().size(); i++) {
						ss = ss.equals("") ? "`" + entry.getValue().get(i) + "`"
								: ss + "," + "`" + entry.getValue().get(i) + "`";
					}
					if (s.equals(ss)) {
						s = "";
						break;
					}
				}
				if (!s.equals("")) {
					try {
						long UUID = new Date().getTime();
						commonService.execute("ALTER TABLE " + schema + "." + tablesId + " ADD INDEX " + UUID + "_dynamic_search (" + s + ") ");
						dynamicSeach.put(UUID + "_dynamic_search", null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			if (dynamicSeach.size() > 5) {
				int o = dynamicSeach.size() - 5;
				for (Entry<String, ArrayList<String>> entry : dynamicSeach.entrySet()) {
					if (o <= 0) {
						break;
					}
					if (entry.getValue() == null) {
						break;
					}
					try {
						commonService.execute("ALTER TABLE " + schema + "." + tablesId + " DROP INDEX " + entry.getKey());
					} catch (Exception e) {
						e.printStackTrace();
					}
					o--;
				}
			}

			for (Entry<String, ArrayList<String>> entry1 : Seach_join.entrySet()) {
				LinkedHashMap<String, ArrayList<String>> dynamicSeach1 = new LinkedHashMap<String, ArrayList<String>>();
				try {
					List<PageData> seach1 = commonService.execute("show index from " + entry1.getKey());
					for (int j = 0; j < seach1.size(); j++) {
						PageData p = seach1.get(j);
						if (p.getString("Key_name").indexOf("_dynamic_search") > 0) {
							if (!dynamicSeach1.containsKey(p.getString("Key_name")))
								dynamicSeach1.put(p.getString("Key_name"), new ArrayList<String>());
							dynamicSeach1.get(p.getString("Key_name")).add(p.getString("Column_name"));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String s = "";
				for (int i = 0; i < entry1.getValue().size(); i++) {
					s = s.equals("") ? "`" + entry1.getValue().get(i) + "`"
							: s + "," + "`" + entry1.getValue().get(i) + "`";
				}
				for (Entry<String, ArrayList<String>> entry : dynamicSeach1.entrySet()) {
					Collections.sort(entry.getValue());
					String ss = "";
					for (int i = 0; i < entry.getValue().size(); i++) {
						ss = ss.equals("") ? "`" + entry.getValue().get(i) + "`"
								: ss + "," + "`" + entry.getValue().get(i) + "`";
					}
					if (s.equals(ss)) {
						s = "";
						break;
					}
				}
				if (!s.equals("")) {
					try {
						long UUID = new Date().getTime();
						commonService.execute("ALTER TABLE " + entry1.getKey() + " ADD INDEX " + UUID+ "_dynamic_search ( `" + s + "` ) ");
						dynamicSeach1.put(UUID + "_dynamic_search", null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (dynamicSeach1.size() > 5) {
					int o = dynamicSeach1.size() - 5;
					for (Entry<String, ArrayList<String>> entry : dynamicSeach1.entrySet()) {
						if (o <= 0) {
							break;
						}
						try {
							commonService.execute("ALTER TABLE " + schema + "." + entry1.getKey() + " DROP INDEX " + entry.getKey());
						} catch (Exception e) {
							e.printStackTrace();
						}
						o--;
					}
				}

			}

		}

		// sql for rows count
		StringBuffer sbCount = new StringBuffer();
		sbCount.append("SELECT COUNT(0) FROM (");
		sbCount.append(sb);
		sbCount.append(") _AS_TMP_COUNT_");

		sb.append(" ORDER BY a.UPDATE_TIME DESC");
		// pageNum & pageSize
		sb.append(" limit ");
		sb.append(pageNum * pageSize);
		sb.append(",");
		sb.append(pageSize);

		List<PageData> listData = (List<PageData>) dao.findForList("TemplateMapper.listAll", sb.toString());
		long totalRows = (Long) dao.getTotalCount(sbCount.toString(), null);
		pdData.put("varList", listData);
		pdData.put("totalRows", totalRows);

		return pdData;
	}

	/**
	 * 第N步：获取WHERE语句 **********************需要JOIN其他表 * {join_table_name=COURSE,
	 * join_table_column=COURSE_NAME, value=d4b89c682d6542a890dba715d4fceabb} inner
	 * join COURSE COURSE on COURSE.COURSE{_ID} = a.COURSE_NAME where
	 * COURSE.COURSE_ID = {value} **********************不需要JOIN其他表 *
	 * {join_table_column=COURSE_NAME, value=d4b89c682d6542a890dba715d4fceabb} where
	 * a.COURSE_NAME like '{value}%'
	 * 
	 * @param pd
	 * @return
	 */
	public List<Map<String, String>> getWhere(PageData pd) {

		String where = pd.getString("WHERE");
		if (Tools.isEmpty(where)) {
			return null;
		}
		List<Map<String, String>> list = null;
		JSONArray jsonArray = JSONArray.fromObject(where);
		List<Map<String, Map<String, String>>> mapListJson = (List) jsonArray;
		Map<String, String> mapColumn, mapValue;
		for (int i = 0; i < mapListJson.size(); i++) {
			if (list == null) {
				list = new ArrayList<Map<String, String>>();
			}
			Map<String, Map<String, String>> obj = mapListJson.get(i);
			mapValue = obj.get("value");
			mapColumn = obj.get("column");
			String value = String.valueOf(mapValue.get("value"));
			String join_table_name = mapColumn.get("join_table_name");
			Map<String, String> mapInnerJoin = new HashMap<String, String>();
			// 没有JOIN的情况，直接查询文本
			if (Tools.isEmpty(join_table_name)) {
				// 从当前主表查询，增加WHERE语句
				mapInnerJoin.put("join_table_column", mapColumn.get("field"));
			} else {
				// {join_table_column=COURSE_NAME, join_table_name=COURSE,
				// value=d4b89c682d6542a890dba715d4fceabb}
				mapInnerJoin.put("join_table_name", join_table_name);
				mapInnerJoin.put("join_table_column", mapColumn.get("join_table_column"));
				mapInnerJoin.put("join_select_table_column", mapColumn.get("join_select_table_column"));
				mapInnerJoin.put("master_table_name", mapColumn.get("master_table_name"));
				mapInnerJoin.put("master_table_column", mapColumn.get("master_table_column"));
			}
			mapInnerJoin.put("type", mapColumn.get("type"));
			mapInnerJoin.put("value", value);
			mapInnerJoin.put("encrypt", mapColumn.get("encrypt"));
			list.add(mapInnerJoin);
//            "name": "学生姓名",
//            "resizable": true,
//            "sortable": false,
//            "minWidth": 30,
//            "rerenderOnResize": false,
//            "headerCssClass": null,
//            "defaultSortAsc": true,
//            "focusable": true,
//            "selectable": true,
//            "width": 80,
//            "id": "STUDENT_NAME",
//            "field": "STUDENT_NAME",
//            "cssClass": "cell-text-align-left",
//            "regrule": "^[\\u4e00-\\u9fa5_a-zA-Z0-9]{1,10}$",
//            "regmsg": "字符串1-10",
//            "join_table_name": "",
//            "join_table_column": "",
//            "corpid": "",
//            "joinurl": "",
			// COLUMNS_ID==>INDEXES_ID==>COLUMNS_NAME_EN==>STUDENT_NAME
			// tablesId_ID==>STUDENT_ID<==>STUDENT_NAME
//			source_table -> join_table  
//			source_column-> join_column
//			select join_column, id from join_table
		}
		return list;
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("TemplateMapper.datalistPage", pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("TemplateMapper.findById", pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		dao.delete("TemplateMapper.deleteAll", ArrayDATA_IDS);
	}

}
