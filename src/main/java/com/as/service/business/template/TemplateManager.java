package com.as.service.business.template;

import java.util.List;
import java.util.Map;

import com.as.entity.Page;
import com.as.entity.business.Column;
import com.as.util.PageData;

/** 
 * 说明： 通用二维列表接口
 * 创建人：antispy
 * 创建时间：2016-09-04
 * @version
 */
public interface TemplateManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(String tablesId, PageData pd, Map<String,Object> colDef)throws Exception;
	public void insert(String tablesId, PageData pd, Map<String,Object> colDef)throws Exception;

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
	public void deleteSlaveData(String tablesId, PageData pd)throws Exception;

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(PageData pd)throws Exception;

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public PageData listAll(PageData pd)throws Exception;

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

}

