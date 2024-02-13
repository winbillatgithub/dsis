package com.as.service.business.node;

import java.util.List;
import com.as.entity.Page;
import com.as.util.PageData;

/** 
 * 说明： 审核节点人员表接口
 * 创建人：antispy
 * 创建时间：2016-09-17
 * @version
 */
public interface NodeManager{

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
	/**驳回-更新
	 * @param pd
	 * @throws Exception
	 */
	public void denyUpdate(PageData pd)throws Exception;
	/**驳回-删除
	 * @param pd
	 * @throws Exception
	 */
	public void denyDelete(PageData pd)throws Exception;
	
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
	
}

