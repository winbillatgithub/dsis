package com.as.service.business.listlistruledetail.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.util.PageData;
import com.as.service.business.listlistruledetail.ListListRuleDetailManager;

/** 
 * 说明： ListList规则
 * 创建人：antispy
 * 创建时间：2017-03-28
 * @version
 */
@Service("listlistruledetailService")
public class ListListRuleDetailService implements ListListRuleDetailManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ListListRuleDetailMapper.save", pd);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ListListRuleDetailMapper.delete", pd);
	}
	public void deleteByRuleId(PageData pd)throws Exception {
		dao.delete("ListListRuleDetailMapper.deleteByRuleId", pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ListListRuleDetailMapper.edit", pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ListListRuleDetailMapper.datalistPage", page);
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ListListRuleDetailMapper.listAll", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ListListRuleDetailMapper.findById", pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ListListRuleDetailMapper.deleteAll", ArrayDATA_IDS);
	}

}
