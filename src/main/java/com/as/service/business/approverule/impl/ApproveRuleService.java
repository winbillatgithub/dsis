package com.as.service.business.approverule.impl;

import java.util.List;

import javax.annotation.Resource;

import com.as.util.Jurisdiction;
import org.springframework.stereotype.Service;

import com.as.dao.DaoSupport;
import com.as.entity.Page;
import com.as.entity.business.ApproveRule;
import com.as.util.PageData;
import com.as.service.business.approverule.ApproveRuleManager;

/** 
 * 说明： 数据字典
 * 创建人：antispy
 * 创建时间：2015-12-16
 * @version
 */
@Service("approveruleService")
public class ApproveRuleService implements ApproveRuleManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ApproveRuleMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ApproveRuleMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ApproveRuleMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ApproveRuleMapper.datalistPage", page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ApproveRuleMapper.findById", pd);
	}
	
	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ApproveRuleMapper.findByBianma", pd);
	}
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ApproveRule> listSubDictByParentId(String parentId) throws Exception {
        PageData pd = new PageData();
        pd.put("parentId", parentId);
        pd.put("CORP_ID", Jurisdiction.getTopCorpId());
		return (List<ApproveRule>) dao.findForList("ApproveRuleMapper.listSubDictByParentId", pd);
	}

	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<ApproveRule> listAllDict(String parentId) throws Exception {
		List<ApproveRule> dictList = this.listSubDictByParentId(parentId);
		for(ApproveRule dict : dictList){
			dict.setTreeurl("approverule/list.do?APPROVERULE_ID="+dict.getAPPROVERULE_ID());
			dict.setSubDict(this.listAllDict(dict.getAPPROVERULE_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}

	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFromTbs(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ApproveRuleMapper.findFromTbs", pd);
	}
	
}

