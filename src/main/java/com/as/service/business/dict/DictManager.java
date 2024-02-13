package com.as.service.business.dict;

import com.as.entity.Page;
import com.as.entity.business.Dict;
import com.as.util.PageData;

import java.util.List;

/** 
 * 说明： 用户自定义编码接口类
 * 创建人：antispy
 * 创建时间：2015-12-16
 * @version
 */
public interface DictManager {

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
    public List<PageData> listAll(PageData page)throws Exception;
    public List<PageData> listAllForControls(PageData page)throws Exception;

    /**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception;
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Dict> listSubDictByParentId(String parentId) throws Exception;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Dict> listAllDict(String parentId) throws Exception;
	
	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFromTbs(PageData pd)throws Exception;

    /**通过id获取子编码的最大编号
     * @param pd
     * @throws Exception
     */
    public PageData findMaxSortNo(PageData pd)throws Exception;
}

