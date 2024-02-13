package com.as.service.business.tables;

import com.as.entity.Page;
import com.as.util.PageData;

import java.util.List;

/** 
 * 说明： 模板维护接口
 * 创建人：antispy
 * 创建时间：2016-09-09
 * @version
 */
public interface TablesManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
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

	/**修改状态
	 * @param pd
	 * @throws Exception
	 */
	public void updateStatus(PageData pd)throws Exception;

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
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

	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception;

	/**创建物理表
	 * @param pd
	 * @throws Exception
	 */
	public void createTable(PageData pd)throws Exception;
	/**rename物理表
	 * @param pd
	 * @throws Exception
	 */
	public void renameTable(PageData pd)throws Exception;
	/**删除物理表
	 * @param pd
	 * @throws Exception
	 */
	public void dropTable(PageData pd)throws Exception;
}

