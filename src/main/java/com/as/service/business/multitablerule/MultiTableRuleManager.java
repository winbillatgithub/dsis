package com.as.service.business.multitablerule;

import java.util.List;
import com.as.entity.Page;
import com.as.util.PageData;

/** 
 * 说明： 多表规则接口
 * 创建人：antispy
 * 创建时间：2017-03-05
 * @version
 */
public interface MultiTableRuleManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	public void saveList(List<PageData> list)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	public void deleteCORP(String CORP_ID)throws Exception;
	
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

    /**
     * 通过JOIN_TABLES_ID&JOIN_COLUMNS_ID获取数据
     * 判断表的字段是否被关联
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> findByJoinColumnsId(PageData pd)throws Exception;

    /**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

