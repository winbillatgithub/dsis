package com.as.service.business.multitablerule.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.util.PageData;
import com.as.service.business.multitablerule.MultiTableRuleManager;

/** 
 * 说明： 多表规则
 * 创建人：antispy
 * 创建时间：2017-03-05
 * @version
 */
@Service("multitableruleService")
public class MultiTableRuleService implements MultiTableRuleManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("MultiTableRuleMapper.save", pd);
	}
	
	public void saveList(List<PageData> list)throws Exception{
		dao.batchSave("MultiTableRuleMapper.saveList", list);
	}
	
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("MultiTableRuleMapper.delete", pd);
	}
	
	public void deleteCORP(String CORP_ID)throws Exception{
		dao.delete("MultiTableRuleMapper.deleteCORP", CORP_ID);
	}
	

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("MultiTableRuleMapper.edit", pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("MultiTableRuleMapper.datalistPage", page);
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("MultiTableRuleMapper.listAll", pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("MultiTableRuleMapper.findById", pd);
	}

    /**
     * 通过JOIN_TABLES_ID&JOIN_COLUMNS_ID获取数据
     * 判断表的字段是否被关联
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> findByJoinColumnsId(PageData pd)throws Exception {
        return (List<PageData>)dao.findForList("MultiTableRuleMapper.findByJoinColumnsId", pd);
    }

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("MultiTableRuleMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

