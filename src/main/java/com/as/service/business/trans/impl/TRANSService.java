package com.as.service.business.trans.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.util.PageData;
import com.as.service.business.trans.TRANSManager;

/** 
 * 说明： 数据传输表
 * 创建人：antispy
 * 创建时间：2016-09-15
 * @version
 */
@Service("transService")
public class TRANSService implements TRANSManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("TRANSMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("TRANSMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("TRANSMapper.edit", pd);
	}
	public void editFlag(PageData pd)throws Exception{
		dao.update("TRANSMapper.editFlag", pd);
	}
	/**审批
	 * @param pd
	 * @throws Exception
	 */
	public void approve(PageData pd)throws Exception{
		dao.update("TRANSMapper.approve", pd);
	}
	/**驳回
	 * @param pd
	 * @throws Exception
	 */
	public void deny(PageData pd)throws Exception{
		dao.update("TRANSMapper.deny", pd);
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("TRANSMapper.datalistPage", page);
	}
	
	@SuppressWarnings("unchecked")
	public List<PageData> excel(Page page)throws Exception{
		return (List<PageData>) dao.findForList("TRANSMapper.excel", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("TRANSMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("TRANSMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("TRANSMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

