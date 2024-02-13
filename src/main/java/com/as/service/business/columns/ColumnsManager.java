package com.as.service.business.columns;

import java.util.List;
import com.as.entity.Page;
import com.as.util.PageData;

/** 
 * 说明： 模板指标对照表接口
 * 创建人：antispy
 * 创建时间：2016-09-09
 * @version
 */
public interface ColumnsManager{

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

    /**通过indexesid获取数据
     * @param pd
     * @throws Exception
     */
    public List<PageData> findByIndexesId(PageData pd)throws Exception;

    /**通过Column english name获取数据
     * @param pd
     * @throws Exception
     */
    public PageData findByEnName(PageData pd)throws Exception;

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**增加列
	 * @param pd
	 * @throws Exception
	 */
	public void addColumn(PageData pd)throws Exception;
	/**删除列
	 * @param pd
	 * @throws Exception
	 */
	public void dropColumn(PageData pd)throws Exception;
	/**修改列
	 * @param pd
	 * @throws Exception
	 */
	public void changeColumn(PageData pd)throws Exception;
	/**修改列
	 * @param pd
	 * @throws Exception
	 */
	public void modifyColumn(PageData pd)throws Exception;
}

